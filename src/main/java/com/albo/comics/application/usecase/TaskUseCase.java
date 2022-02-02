package com.albo.comics.application.usecase;

import com.albo.comics.adapter.jdbc.CharactersDao;
import com.albo.comics.adapter.jdbc.CollaboratorsDao;
import com.albo.comics.adapter.jdbc.model.Character;
import com.albo.comics.adapter.jdbc.model.CollaboratorWrap;
import com.albo.comics.application.port.in.TaskCommand;
import com.albo.comics.application.port.out.MarvelRepository;
import com.albo.comics.domain.ComicInformation;
import com.albo.comics.util.LastSyncGenerator;
import com.albo.comics.util.TimestampGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskUseCase implements TaskCommand {

    private static final Logger log = LoggerFactory.getLogger(TaskUseCase.class);

    @Autowired
    private MarvelRepository marvelRepository;

    @Autowired
    private CharactersDao charactersDao;

    @Autowired
    private CollaboratorsDao collaboratorsDao;

    @Override
    public void dailyRevision(List<String> charactersToReview) {
        log.info("Iniciamos eliminación de la db");
        charactersDao.deleteAll();
        collaboratorsDao.deleteAll();

        List<Long> characterIds = new ArrayList<>();
        Long timestamp = TimestampGenerator.getTimestamp();

        for (String name : charactersToReview) {
            characterIds.add(marvelRepository.getCharacterId(name, timestamp));
        }

        List<ComicInformation> comicInformationList = new ArrayList<>();

        for (Long id : characterIds) {
            comicInformationList.add(marvelRepository.getCrossInformation(id, timestamp));
        }

        String lastSync = LastSyncGenerator.generateLastSync();
        List<Character> characterModelToSave = comicInformationList.stream()
                .flatMap(comicInformation -> comicInformation.getCharactersList().stream())
                .map(character -> Character
                        .builder()
                        .characterName(character.getCharacterName())
                        .comics(character.getComics())
                        .ownerId(character.getOwnerId())
                        .lastSync(lastSync)
                        .build())
                .collect(Collectors.toList());

        log.info("Guardamos los personajes secundarios");
        charactersDao.insert(characterModelToSave);

        log.info("Guardamos los colaboradores agrupados");
        comicInformationList.forEach(comicInformation ->
                collaboratorsDao.insert(CollaboratorWrap
                        .builder()
                        .collaborators(comicInformation.getCollaboratorSet())
                        .lastSync(lastSync)
                        .build())
        );

    }

}

