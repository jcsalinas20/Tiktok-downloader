module com.example.tiktokdownloader {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.datatransfer;
    requires java.desktop;
    requires java.sql;
    requires db4o;

    opens com.tiktokdownloader.downloadlist to javafx.fxml;
    exports com.tiktokdownloader.downloadlist;
    opens com.tiktokdownloader.onevideo to javafx.fxml;
    exports com.tiktokdownloader.onevideo;
    opens com.tiktokdownloader.alluservideos to javafx.fxml;
    exports com.tiktokdownloader.alluservideos;
    opens com.tiktokdownloader.logs to javafx.fxml;
    exports com.tiktokdownloader.logs;
    exports com.tiktokdownloader;
    opens com.tiktokdownloader to javafx.fxml;
}