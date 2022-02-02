package com.albo.comics.adapter.jdbc;

import com.albo.comics.adapter.jdbc.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CharactersDao extends MongoRepository<Character, String> {

    List<Character> findByOwnerId(Long ownerId);
}
