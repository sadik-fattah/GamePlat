package com.guercifzone.gameplate.Models;

public class Feed {

    String Gamename;
    String gameLoc;
    String gameType;
    String gameImage;

    public Feed(String gamename, String gameLoc, String gameType, String gameImage) {
        Gamename = gamename;
        this.gameLoc = gameLoc;
        this.gameType = gameType;
        this.gameImage = gameImage;
    }

    public Feed() {
    }

    public String getGamename() {
        return Gamename;
    }

    public void setGamename(String gamename) {
        Gamename = gamename;
    }

    public String getGameLoc() {
        return gameLoc;
    }

    public void setGameLoc(String gameLoc) {
        this.gameLoc = gameLoc;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameImage() {
        return gameImage;
    }

    public void setGameImage(String gameImage) {
        this.gameImage = gameImage;
    }
}

