package com.albo.comics.adapter.controller;

import com.albo.comics.adapter.controller.model.GetCharactersResponse;
import com.albo.comics.application.port.in.CharactersQuery;
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
@RequestMapping("/marvel/characters")
public class CharactersController {

    private final CharactersQuery charactersQuery;

    public CharactersController(
            CharactersQuery charactersQuery) {
        this.charactersQuery = charactersQuery;
    }

    @GetMapping("/ironman")
    public ResponseEntity<GetCharactersResponse> getIronmanSecondaryCharacters(
            final HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(charactersQuery.getCharactersByName(Constants.IRONMAN), HttpStatus.OK);
    }


    @GetMapping("/capamerica")
    public ResponseEntity<GetCharactersResponse> getCapAmericaSecondaryCharacters(
            final HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(charactersQuery.getCharactersByName(Constants.CAPTAIN_AMERICA), HttpStatus.OK);
    }
}
