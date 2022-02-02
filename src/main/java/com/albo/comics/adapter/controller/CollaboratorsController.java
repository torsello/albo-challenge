package com.albo.comics.adapter.controller;

import com.albo.comics.adapter.controller.model.GetCollaboratorsResponse;
import com.albo.comics.application.port.in.CollaboratorsQuery;
import com.albo.comics.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Validated
@RequestMapping("/marvel/colaborators")
public class CollaboratorsController {

    private final CollaboratorsQuery collaboratorsQuery;

    public CollaboratorsController(
            CollaboratorsQuery collaboratorsQuery) {
        this.collaboratorsQuery = collaboratorsQuery;
    }

    @GetMapping("/ironman")
    public ResponseEntity<GetCollaboratorsResponse> getIronmanCollaborators(
            final HttpServletRequest httpServletRequest) {

        return new ResponseEntity<>(collaboratorsQuery.getCollaboratorsByName(Constants.IRONMAN), HttpStatus.OK);
    }


    @GetMapping("/capamerica")
    public ResponseEntity<GetCollaboratorsResponse> getCapAmericaCollaborators(
            final HttpServletRequest httpServletRequest) {

        return new ResponseEntity<>(collaboratorsQuery.getCollaboratorsByName(Constants.CAPTAIN_AMERICA), HttpStatus.OK);
    }
}
