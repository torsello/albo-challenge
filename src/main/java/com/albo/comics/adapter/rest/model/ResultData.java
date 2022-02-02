package com.albo.comics.adapter.rest.model;

import lombok.Data;

@Data
public class ResultData {

    private Long id;
    private String title;
    private CreatorData creators;
    private CharacterData characters;

    public ResultData() {
    }

}
