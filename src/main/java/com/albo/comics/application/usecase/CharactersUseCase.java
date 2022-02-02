package com.albo.comics.application.usecase;

import com.albo.comics.adapter.controller.model.GetCharactersResponse;
import com.albo.comics.adapter.jdbc.CharactersDao;
import com.albo.comics.application.port.in.CharactersQuery;
import com.albo.comics.application.port.out.MarvelRepository;
import com.albo.comics.util.TimestampGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CharactersUseCase implements CharactersQuery {
    private static final Logger log = LoggerFactory.getLogger(CharactersUseCase.class);

    @Autowired
    private CharactersDao charactersDao;

    @Autowired
    private MarvelRepository marvelRepository;

    @Override
    public GetCharactersResponse getCharactersByName(String name) {
        log.info("Procedemos a buscar los personajes secundarios relacionados a {}", name);
        Long timestamp = TimestampGenerator.getTimestamp();

        Long ownerId = marvelRepository.getCharacterId(name, timestamp);

        return GetCharactersResponse.toResponse(charactersDao.findByOwnerId(ownerId));
    }

}
