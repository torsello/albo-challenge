package com.albo.comics.application.port.in;

import java.util.List;

public interface TaskCommand {

    void dailyRevision(List<String> charactersToReview);

}
