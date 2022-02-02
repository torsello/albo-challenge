package com.albo.comics.adapter.rest.model;

import lombok.Data;

import java.util.Objects;

@Data
public class SecondayCharacter {

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecondayCharacter character = (SecondayCharacter) o;
        return Objects.equals(name, character.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
