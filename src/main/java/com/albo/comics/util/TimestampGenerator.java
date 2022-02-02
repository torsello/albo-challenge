package com.albo.comics.util;

import java.sql.Timestamp;

public class TimestampGenerator {
    public static Long getTimestamp() {
        return new Timestamp(System.currentTimeMillis()).getTime();
    }
}
