package com.albo.comics.application.usecase;

import com.albo.comics.adapter.jdbc.CharactersDao;
import com.albo.comics.adapter.jdbc.model.Character;
import com.albo.comics.application.port.out.MarvelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test related to CharacterUseCase")
public class CharactersUseCaseTest {

    private static final Long ID = 1L;
    private static final int INVOCATIONS = 1;
    private static final String IRONMAN = "Iron Man";
    private static final String LAST_SYNC = "dd/MM/yyyy hh:mm:ss";

    @Mock
    private CharactersDao charactersDao;

    @Mock
    private MarvelRepository marvelRepository;

    @InjectMocks
    CharactersUseCase charactersUseCase;

    @Test
    @DisplayName("Retrieves secondary characters")
    void retrieveSecondaryCharacters() {
        when(marvelRepository.getCharacterId(anyString(), anyLong()))
                .thenReturn(ID);

        Character character = Character.builder().characterName(IRONMAN).lastSync(LAST_SYNC).ownerId(ID).build();
        when(charactersDao.findByOwnerId(anyLong())).thenReturn(List.of(character));

        charactersUseCase.getCharactersByName(IRONMAN);

        verify(charactersDao, times(INVOCATIONS)).findByOwnerId(anyLong());
        verify(marvelRepository, times(INVOCATIONS)).getCharacterId(anyString(), anyLong());
    }

}
