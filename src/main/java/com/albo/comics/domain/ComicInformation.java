package com.albo.comics.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComicInformation {

    private List<Character> charactersList = new ArrayList<>();
    private Map<String, Set<Collaborator>> collaboratorSet = new HashMap<>();

}
