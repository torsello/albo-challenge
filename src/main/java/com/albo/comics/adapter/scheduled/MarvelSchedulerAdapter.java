package com.albo.comics.adapter.scheduled;

import com.albo.comics.application.port.in.TaskCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MarvelSchedulerAdapter {

    private static final Logger log = LoggerFactory.getLogger(MarvelSchedulerAdapter.class);

    private static final String CRON_EXPRESSION = "${scheduled.marvel.cron}";

    @Value("${marvel.characters.needed}")
    private String marvelCharacters;

    private final TaskCommand taskCommand;

    public MarvelSchedulerAdapter(
            TaskCommand taskCommand) {
        this.taskCommand = taskCommand;
    }

    //@Scheduled(cron = CRON_EXPRESSION)
    @Scheduled(fixedDelay = 5000)
    public void verifyMarvelInformation() {
        log.info("Comienza tarea de revision diaria");
        /*List<String> characterNames = Arrays.asList(marvelCharacters.split(","));
        taskCommand.dailyRevision(characterNames);*/
    }

}
