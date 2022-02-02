package com.albo.comics.adapter.rest.model;

import lombok.Data;

import java.util.List;

@Data
public class MarvelData {

    private Integer limit;
    private Integer total;
    private Integer count;
    private List<ResultData> results;

    public MarvelData() {
    }


}
