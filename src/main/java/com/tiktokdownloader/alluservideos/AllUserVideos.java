package com.tiktokdownloader.alluservideos;

import com.tiktokdownloader.Main;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AllUserVideos extends Application implements Initializable {

    /************************* VARIABLES FXML **************************/

    @FXML
    Menu menuDownloadList;

    @FXML
    Menu menuAllUserVideos;

    @FXML
    Menu menuOneVideo;

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
        menuAllUserVideos.setGraphic(menuAllUserVideosLabel);

        Label menuOneVideoLabel = new Label("One Video");
        menuOneVideoLabel.setOnMouseClicked(event -> main.showScreenOneVideo());
        menuOneVideo.setGraphic(menuOneVideoLabel);
    }

    @Override
    public void start(Stage primaryStage) {}
}
