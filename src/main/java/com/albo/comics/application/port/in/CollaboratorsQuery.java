package com.albo.comics.application.port.in;

import com.albo.comics.adapter.controller.model.GetCollaboratorsResponse;

public interface CollaboratorsQuery {

    GetCollaboratorsResponse getCollaboratorsByName(String name);

}
