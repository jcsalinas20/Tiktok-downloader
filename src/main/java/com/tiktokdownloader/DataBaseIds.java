package com.tiktokdownloader;

public class DataBaseIds {
    public String username;
    public String url;
    public long id;
    public String currentTimestamp;

    public DataBaseIds(String username, String url, long id, String currentTimestamp) {
        this.username = username;
        this.url = url;
        this.id = id;
        this.currentTimestamp = currentTimestamp;
    }

    @Override
    public String toString() {
        return "DataBaseIds{" +
                "username='" + username + '\'' +
                ", url='" + url + '\'' +
                ", id=" + id +
                ", currentTimestamp='" + currentTimestamp + '\'' +
                '}';
    }
}