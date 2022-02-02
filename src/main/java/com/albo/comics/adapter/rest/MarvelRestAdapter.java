package com.albo.comics.adapter.rest;

import com.albo.comics.adapter.rest.exception.NotFoundRestClientException;
import com.albo.comics.adapter.rest.exception.RestClientGenericException;
import com.albo.comics.adapter.rest.exception.TimeoutRestClientException;
import com.albo.comics.adapter.rest.handler.RestTemplateErrorHandler;
import com.albo.comics.adapter.rest.model.MarvelData;
import com.albo.comics.adapter.rest.model.MarvelDataResponse;
import com.albo.comics.adapter.rest.model.ResultData;
import com.albo.comics.application.port.out.MarvelRepository;
import com.albo.comics.config.ErrorCode;
import com.albo.comics.config.MarvelServiceProperties;
import com.albo.comics.domain.ComicInformation;
import com.albo.comics.util.HashGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Repository
public class MarvelRestAdapter implements MarvelRepository {
    private static final Logger log = LoggerFactory.getLogger(MarvelRestAdapter.class);

    private static final String OFFSET = "&offset=%s";
    private final RestTemplate restTemplate;
    private final String urlMarvelByCharacter;
    private final String urlMarvelByName;
    private final String privateKey;
    private final String publicKey;

    public MarvelRestAdapter(
            RestTemplate restTemplate,
            MarvelServiceProperties marvelServiceProperties
    ) {
        this.restTemplate = restTemplate;
        this.urlMarvelByCharacter = marvelServiceProperties.getUrlCharacter();
        this.urlMarvelByName = marvelServiceProperties.getUrlCharacterByName();
        this.privateKey = marvelServiceProperties.getPrivateKey();
        this.publicKey = marvelServiceProperties.getPublicKey();
        var errorHandler = new RestTemplateErrorHandler(
                Map.of(
                        HttpStatus.INTERNAL_SERVER_ERROR, new RestClientGenericException(ErrorCode.INTERNAL_ERROR),
                        HttpStatus.REQUEST_TIMEOUT, new TimeoutRestClientException(ErrorCode.REST_CLIENT_TIMEOUT),
                        HttpStatus.NOT_FOUND, new NotFoundRestClientException(ErrorCode.NOT_FOUND_MARVEL)
                )
        );

        this.restTemplate.setErrorHandler(errorHandler);
    }


    @Override
    public ComicInformation getCrossInformation(Long mainCharacterId, Long timestamp) {
        log.info("Procedemos a buscar la información de los personajes secundarios y sus colaboradores");

        String hash = getHash(timestamp);

        String byCharactersURI = String.format(urlMarvelByCharacter, timestamp, publicKey, hash);

        MarvelDataResponse response = Optional.ofNullable(restTemplate.getForObject(byCharactersURI, MarvelDataResponse.class, mainCharacterId))
                .orElseThrow(() -> new NotFoundRestClientException(ErrorCode.MARVEL_EXCEPTION));

        if (HttpStatus.OK.value() != response.getCode()) {
            throw new RestClientGenericException(ErrorCode.MARVEL_EXCEPTION);
        }

        //I Use this logic to create a completableFuture, to consume all pages from the API
        int totalPages = response.getData().getTotal() - response.getData().getCount();
        int limit = 100;

        List<CompletableFuture<MarvelDataResponse>> futureMarvelResponse = new ArrayList<>();

        for (int i = limit; i <= totalPages; i += limit) {
            int finalI = i;
            futureMarvelResponse.add(CompletableFuture.supplyAsync(() -> this.getCrossInformationFuture(finalI, hash, mainCharacterId, timestamp)));
        }

        List<MarvelDataResponse> responsesFuture = futureMarvelResponse.stream().map(CompletableFuture::join).collect(Collectors.toList());

        List<ResultData> resultData = responsesFuture.stream().flatMap(data -> data.getData().getResults().stream()).collect(Collectors.toList());

        MarvelDataResponse joinedResponse = new MarvelDataResponse();
        MarvelData marvelData = new MarvelData();

        marvelData.setResults(resultData);
        joinedResponse.setData(marvelData);

        return joinedResponse.toDomain(mainCharacterId);
    }

    @Override
    public Long getCharacterId(String characterName, Long timestamp) {
        log.info("Buscamos ids de los personajes ingresados");
        String hash = getHash(timestamp);

        String byCharacterNameUri = String.format(urlMarvelByName, timestamp, publicKey, hash, characterName);

        return Optional.ofNullable(restTemplate.getForObject(byCharacterNameUri, MarvelDataResponse.class))
                .orElseThrow(() -> new NotFoundRestClientException(ErrorCode.CHARACTERS_NOT_PRESENT)).getData().getResults().stream().findFirst()
                .orElseThrow(() -> new NotFoundRestClientException(ErrorCode.CHARACTERS_NOT_PRESENT)).getId();
    }

    public MarvelDataResponse getCrossInformationFuture(int offset, String hash, Long mainCharacterId, Long timestamp) {

        String byCharactersURI = String.format(urlMarvelByCharacter, timestamp, publicKey, hash).concat(String.format(OFFSET, offset));

        MarvelDataResponse response = Optional.ofNullable(restTemplate.getForObject(byCharactersURI, MarvelDataResponse.class, mainCharacterId))
                .orElseThrow(() -> new NotFoundRestClientException(ErrorCode.NOT_FOUND_MARVEL));

        if (HttpStatus.OK.value() != response.getCode()) {
            throw new RestClientGenericException(ErrorCode.MARVEL_EXCEPTION);
        }

        return response;
    }

    private String getHash(Long timestamp) {
        return HashGenerator.generateHash(timestamp.toString(), this.publicKey, this.privateKey);
    }

}
