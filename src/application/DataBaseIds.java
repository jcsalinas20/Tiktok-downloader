package application;

public class DataBaseIds {
    protected String username;
    protected String url;
    protected long id;
    protected String currentTimestamp;

    public DataBaseIds() {
        super();
    }

    public DataBaseIds(String username, String url, long id, String currentTimestamp) {
        this.username = username;
        this.url = url;
        this.id = id;
        this.currentTimestamp = currentTimestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrentTimestamp() {
        return currentTimestamp;
    }

    public void setCurrentTimestamp(String currentTimestamp) {
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
