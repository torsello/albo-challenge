package com.albo.comics.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LastSyncGenerator {

    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static String generateLastSync() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        return "Fecha de la última sincronización en " + dtf.format(now);
    }
}
