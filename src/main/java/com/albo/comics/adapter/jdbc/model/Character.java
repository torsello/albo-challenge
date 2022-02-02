package com.albo.comics.adapter.jdbc.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document
@Builder
@Data
public class Character {

    @Id
    private String id;
    private String characterName;
    private List<String> comics;
    private Long ownerId;
    private String lastSync;

}
