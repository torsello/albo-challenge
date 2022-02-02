package com.albo.comics.adapter.jdbc.model;

import com.albo.comics.domain.Collaborator;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Map;
import java.util.Set;

@Document
@Builder
@Data
public class CollaboratorWrap {

    @Id
    private String id;
    private String lastSync;
    private Map<String, Set<Collaborator>> collaborators;

}
