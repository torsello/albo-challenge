package com.albo.comics.adapter.rest.model;

import com.albo.comics.domain.Character;
import com.albo.comics.domain.Collaborator;
import com.albo.comics.domain.ComicInformation;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class MarvelDataResponse {

    private Integer code;
    private MarvelData data;

    public MarvelDataResponse() {
    }

    public ComicInformation toDomain(Long characterId) {
        ComicInformation comicInformation = new ComicInformation();

        List<ResultData> results = data.getResults();

        List<String> secondaryCharacters = results.stream().flatMap(resultData -> resultData.getCharacters().getItems().stream()).distinct()
                .map(SecondayCharacter::getName).collect(Collectors.toList());

        List<Character> characterList = comicInformation.getCharactersList();

        secondaryCharacters.forEach(
                character -> {
                    SecondayCharacter characterModel = new SecondayCharacter();
                    characterModel.setName(character);
                    List<String> comics = results.stream().filter(resultData -> resultData.getCharacters().getItems().contains(characterModel))
                            .map(ResultData::getTitle).collect(Collectors.toList());

                    characterList.add(Character.builder().characterName(character).ownerId(characterId).comics(comics).build());
                }
        );

        Map<String, Set<Collaborator>> collaborators = results.stream()
                .flatMap(resultData -> resultData.getCreators().getItems().stream())
                .collect(Collectors.groupingBy(Creator::getRole,
                        Collectors.mapping(map -> Collaborator
                                .builder()
                                .name(map.getName())
                                .ownerId(characterId)
                                .build(), Collectors.toSet())));

        comicInformation.setCollaboratorSet(collaborators);

        return comicInformation;
    }
}
