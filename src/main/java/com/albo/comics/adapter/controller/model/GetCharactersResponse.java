package com.albo.comics.adapter.controller.model;

import com.albo.comics.adapter.jdbc.model.Character;
import com.albo.comics.config.ErrorCode;
import com.albo.comics.config.exception.NotPresentException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class GetCharactersResponse {
    @JsonProperty("last_sync")
    private String lastSync;
    private List<CharacterComics> characters;

    @Data
    @Builder
    static class CharacterComics {
        @JsonProperty("character")
        private String characterName;
        private List<String> comics;
    }

    public static GetCharactersResponse toResponse(List<Character> characters) {
        return GetCharactersResponse.builder()
                .lastSync(characters.stream().findFirst()
                        .orElseThrow(() -> new NotPresentException(ErrorCode.CHARACTERS_NOT_PRESENT))
                        .getLastSync())
                .characters(characters.stream()
                        .map(model -> CharacterComics
                                .builder()
                                .characterName(model.getCharacterName())
                                .comics(model.getComics()).build())
                        .collect(Collectors.toList())).build();

    }
}
