package com.albo.comics.config;

public enum ErrorCode {

    INTERNAL_ERROR(100, "Error interno del servidor"),
    REST_CLIENT_TIMEOUT(101, "Timeout de cliente rest"),
    WEB_CLIENT_GENERIC(102, "Error del Web Client"),
    MARVEL_EXCEPTION(103, "Ha ocurrido un error al consultar servicio Marvel"),
    COLLABORATORS_NOT_PRESENT(104, "No se encontró ningun colaborador"),
    CHARACTERS_NOT_PRESENT(105, "No se encontró ningun personaje"),
    BAD_REQUEST(106, "Bad request"),
    NOT_FOUND_MARVEL(107, "No se encontró información en Marvel");

    private final int value;
    private final String reasonPhrase;

    ErrorCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
}