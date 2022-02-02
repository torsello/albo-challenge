package com.albo.comics.adapter.controller;

import brave.Tracer;
import com.albo.comics.adapter.controller.model.GetCharactersResponse;
import com.albo.comics.application.port.in.CharactersQuery;
import com.albo.comics.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@WebMvcTest(controllers = CharactersController.class)
@DisplayName("Test related to CharacterController")
class CharactersControllerTest {

    private static final String PATH_GET_CHARACTERS = "/marvel/characters/ironman";
    private static final String IRONMAN = "Iron Man";
    private static final String LAST_SYNC = "dd/MM/yyyy hh:mm:ss";
    private static final int INVOCATIONS = 1;

    @MockBean
    private CharactersQuery charactersQuery;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private Tracer tracer;


    @Test
    @DisplayName("When request characters by name, then return 200")
    void whenRequestCharactersByNameThenReturn200() throws Exception {
        GetCharactersResponse response = GetCharactersResponse.builder().characters(new ArrayList<>()).lastSync(LAST_SYNC).build();

        when(charactersQuery.getCharactersByName(IRONMAN))
                .thenReturn(response);

        mvc
                .perform(
                        get(PATH_GET_CHARACTERS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.last_sync").exists())
                .andExpect(jsonPath("$.characters").isArray());

        verify(charactersQuery, times(INVOCATIONS)).getCharactersByName(anyString());
    }
}
