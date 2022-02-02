package com.albo.comics.adapter.controller;

import brave.Tracer;
import com.albo.comics.adapter.controller.model.GetCollaboratorsResponse;
import com.albo.comics.application.port.in.CollaboratorsQuery;
import com.albo.comics.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@WebMvcTest(controllers = CollaboratorsController.class)
@DisplayName("Test related to CollaboratorsController")
class CollaboratorsControllerTest {

    private static final String PATH_GET_COLLABORATORS = "/marvel/colaborators/ironman";
    private static final String IRONMAN = "Iron Man";
    private static final String LAST_SYNC = "dd/MM/yyyy hh:mm:ss";
    private static final int INVOCATIONS = 1;

    @MockBean
    private CollaboratorsQuery collaboratorsQuery;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private Tracer tracer;

    @Test
    @DisplayName("When request collaborators by name, then return 200")
    void whenRequestCollaboratorsByNameThenReturn200() throws Exception {
        GetCollaboratorsResponse response = GetCollaboratorsResponse.builder().collaborators(new HashMap<>()).lastSync(LAST_SYNC).build();

        when(collaboratorsQuery.getCollaboratorsByName(IRONMAN))
                .thenReturn(response);

        mvc
                .perform(
                        get(PATH_GET_COLLABORATORS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.last_sync").exists())
                .andExpect(jsonPath("$.collaborators").isMap());

        verify(collaboratorsQuery, times(INVOCATIONS)).getCollaboratorsByName(anyString());
    }
}
