package com.albo.comics.adapter.rest.model;

import lombok.Data;

import java.util.List;

@Data
public class CharacterData {
    private List<SecondayCharacter> items;

    public CharacterData() {
    }

}
