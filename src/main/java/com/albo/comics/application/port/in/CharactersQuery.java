package com.albo.comics.application.port.in;

import com.albo.comics.adapter.controller.model.GetCharactersResponse;

public interface CharactersQuery {

    GetCharactersResponse getCharactersByName(String name);

}
