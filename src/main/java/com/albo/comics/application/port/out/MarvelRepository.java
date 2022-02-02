package com.albo.comics.application.port.out;

import com.albo.comics.domain.ComicInformation;

public interface MarvelRepository {

    ComicInformation getCrossInformation(Long mainCharacterId, Long timestamp);

    Long getCharacterId(String characterName, Long timestamp);

}
