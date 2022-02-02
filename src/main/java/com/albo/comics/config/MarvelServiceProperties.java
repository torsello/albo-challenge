package com.albo.comics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "url.marvel")
public class MarvelServiceProperties {

    private String base;
    private String comicsByCharacterId;
    private String idByCharacterName;
    private String publicKey;
    private String privateKey;

    public MarvelServiceProperties() {
    }

    public MarvelServiceProperties(String base, String comicsByCharacterId, String idByCharacterName, String publicKey, String privateKey) {
        this.base = base;
        this.comicsByCharacterId = comicsByCharacterId;
        this.idByCharacterName = idByCharacterName;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getUrlCharacter() {
        return base.concat(comicsByCharacterId);
    }

    public String getUrlCharacterByName() {
        return base.concat(idByCharacterName);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setComicsByCharacterId(String comicsByCharacterId) {
        this.comicsByCharacterId = comicsByCharacterId;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getComicsByCharacterId() {
        return comicsByCharacterId;
    }

    public String getIdByCharacterName() {
        return idByCharacterName;
    }

    public void setIdByCharacterName(String idByCharacterName) {
        this.idByCharacterName = idByCharacterName;
    }
}
