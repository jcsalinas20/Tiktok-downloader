package application;

public class DataBaseIds {
    protected String username;
    protected String url;
    protected long id;
    protected String currentTimestamp;

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
