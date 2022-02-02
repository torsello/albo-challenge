package com.albo.comics.adapter.controller.model;

import com.albo.comics.adapter.jdbc.model.CollaboratorWrap;
import com.albo.comics.domain.Collaborator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@Builder
public class GetCollaboratorsResponse {
    @JsonProperty("last_sync")
    private String lastSync;
    private Map<String, Set<Collaborator>> collaborators;

    public static GetCollaboratorsResponse toResponse(CollaboratorWrap wrap) {

        return GetCollaboratorsResponse.builder()
                .lastSync(wrap.getLastSync())
                .collaborators(wrap.getCollaborators())
                .build();
    }

}
