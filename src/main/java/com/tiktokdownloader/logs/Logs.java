package com.tiktokdownloader.logs;

import com.tiktokdownloader.Main;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Logs extends Application implements Initializable {

    /************************* VARIABLES FXML **************************/

    @FXML
    Menu menuDownloadList;

    @FXML
    Menu menuAllUserVideos;

    @FXML
    Menu menuOneVideo;

    @FXML
    Menu menuFileGenerator;

    @FXML
    Menu menuLogs;

    /**************************** VARIABLES ****************************/

    Main main = new Main();

    /***************************** METHODS *****************************/

    /*************************** METHODS FXML***************************/

    /************************ METHODS INITIALIZE ************************/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Label menuDownloadListLabel = new Label("Download List");
        menuDownloadListLabel.setOnMouseClicked(event -> main.showScreenDownloadList());
        menuDownloadList.setGraphic(menuDownloadListLabel);

        Label menuAllUserVideosLabel = new Label("All User Videos");
        menuAllUserVideosLabel.setOnMouseClicked(event -> main.showScreenAllUserVideos());
        menuAllUserVideos.setGraphic(menuAllUserVideosLabel);

        Label menuOneVideoLabel = new Label("One Video");
        menuOneVideoLabel.setOnMouseClicked(event -> main.showScreenOneVideo());
        menuOneVideo.setGraphic(menuOneVideoLabel);

        Label menuFileGeneratorLabel = new Label("File Generator");
        menuFileGeneratorLabel.setOnMouseClicked(event -> main.showScreenFileGenerator());
        menuFileGenerator.setGraphic(menuFileGeneratorLabel);

        Label menuLogsLabel = new Label("Logs");
        menuLogs.setGraphic(menuLogsLabel);
    }

    @Override
    public void start(Stage primaryStage) {}
}
