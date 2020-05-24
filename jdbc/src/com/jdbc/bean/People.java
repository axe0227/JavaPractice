package com.jdbc.bean;

public class People {

    private String playerID;
    private int birthYear;
    private int birthDay;

    public People(String playerID, int birthYear, int birthDay) {
        super();
        this.playerID = playerID;
        this.birthYear = birthYear;
        this.birthDay = birthDay;
    }

    public People() { };

    @Override
    public String toString() {
        return "People{" +
                "playerID='" + playerID + '\'' +
                ", birthYear=" + birthYear +
                ", birthDay=" + birthDay +
                '}';
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public String getPlayerID() {
        return playerID;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public int getBirthDay() {
        return birthDay;
    }


}

