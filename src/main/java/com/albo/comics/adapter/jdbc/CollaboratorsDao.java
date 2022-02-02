package com.albo.comics.adapter.jdbc;

import com.albo.comics.adapter.jdbc.model.CollaboratorWrap;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CollaboratorsDao extends MongoRepository<CollaboratorWrap, String> {

}
