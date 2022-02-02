package com.albo.comics.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Builder
@Data
public class Character {

    private String characterName;
    private List<String> comics;
    private Long ownerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return Objects.equals(characterName, character.characterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterName);
    }
}
