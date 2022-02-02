package com.albo.comics.application.usecase;

import com.albo.comics.adapter.controller.model.GetCollaboratorsResponse;
import com.albo.comics.adapter.jdbc.CollaboratorsDao;
import com.albo.comics.adapter.jdbc.model.CollaboratorWrap;
import com.albo.comics.application.port.in.CollaboratorsQuery;
import com.albo.comics.application.port.out.MarvelRepository;
import com.albo.comics.config.ErrorCode;
import com.albo.comics.config.exception.NotPresentException;
import com.albo.comics.domain.Collaborator;
import com.albo.comics.util.TimestampGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollaboratorsUseCase implements CollaboratorsQuery {
    private static final Logger log = LoggerFactory.getLogger(CollaboratorsUseCase.class);

    @Autowired
    private CollaboratorsDao collaboratorsDao;

    @Autowired
    private MarvelRepository marvelRepository;

    @Override
    public GetCollaboratorsResponse getCollaboratorsByName(String name) {
        log.info("Procedemos a buscar los colaboradores del personaje {}", name);
        Long timestamp = TimestampGenerator.getTimestamp();

        Long ownerId = marvelRepository.getCharacterId(name, timestamp);

        Collaborator collaboratorFilter = Collaborator.builder().ownerId(ownerId).build();

        List<CollaboratorWrap> collaboratorsMap = collaboratorsDao.findAll().stream()
                .filter(collaboratorWrap -> collaboratorWrap.getCollaborators().values().stream()
                        .findFirst().orElseThrow(() -> new NotPresentException(ErrorCode.COLLABORATORS_NOT_PRESENT))
                        .contains(collaboratorFilter))
                .collect(Collectors.toList());

        return GetCollaboratorsResponse.toResponse(collaboratorsMap.stream()
                .findFirst()
                .orElseThrow(() -> new NotPresentException(ErrorCode.COLLABORATORS_NOT_PRESENT)));
    }
}
