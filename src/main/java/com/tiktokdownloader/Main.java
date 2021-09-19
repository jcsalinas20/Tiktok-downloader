package com.tiktokdownloader;

import com.tiktokdownloader.downloadlist.StartDownload;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

    public static Stage screenDownloaderList;
    public static Stage screenAllUserVideos;
    public static Stage screenOneVideo;
    public static Stage frameSaveAsFile;
    public static Stage frameImportFile;
    public static Stage frameStartDownload;

    //	Main Windows
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader root = new FXMLLoader(Main.class.getResource("downloadlist/DownloaderList.fxml"));
            Scene scene = new Scene(root.load(), 840, 580);
            scene.getStylesheets().add(getClass().getResource("application.css").toString());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Tiktok Downloader");
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            screenDownloaderList = primaryStage;
            screenDownloaderList.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showScreenAllUserVideos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("alluservideos/AllUserVideos.fxml"));
            Parent root = loader.load();
            screenAllUserVideos = new Stage();
            screenAllUserVideos.setTitle("All User Videos");
            screenAllUserVideos.setScene(new Scene(root));
            screenAllUserVideos.setResizable(false);
            screenAllUserVideos.initModality(Modality.APPLICATION_MODAL);
            screenAllUserVideos.show();
            screenDownloaderList.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showScreenOneVideo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("onevideo/OneVideo.fxml"));
            Parent root = loader.load();
            screenOneVideo = new Stage();
            screenOneVideo.setTitle("One Video");
            screenOneVideo.setScene(new Scene(root));
            screenOneVideo.setResizable(false);
            screenOneVideo.initModality(Modality.APPLICATION_MODAL);
            screenOneVideo.show();
            screenDownloaderList.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFrameSaveAsFile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("downloadlist/SaveAsFile.fxml"));
            Parent root = loader.load();
            frameSaveAsFile = new Stage();
            frameSaveAsFile.setTitle("Save As File");
            frameSaveAsFile.setScene(new Scene(root));
            frameSaveAsFile.setResizable(false);
            frameSaveAsFile.initModality(Modality.APPLICATION_MODAL);
            frameSaveAsFile.initOwner(screenDownloaderList);
            frameSaveAsFile.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFrameImportFile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("downloadlist/ImportFile.fxml"));
            Parent root = loader.load();
            frameImportFile = new Stage();
            frameImportFile.setTitle("Import File");
            frameImportFile.setScene(new Scene(root));
            frameImportFile.setResizable(false);
            frameImportFile.initModality(Modality.APPLICATION_MODAL);
            frameImportFile.initOwner(screenDownloaderList);
            frameImportFile.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void showFrameStartDownload() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("downloadlist/StartDownload.fxml"));
            Parent root = loader.load();
            frameStartDownload = new Stage();
            frameStartDownload.setTitle("Start Download");
            frameStartDownload.setScene(new Scene(root));
            frameStartDownload.setResizable(false);
            frameStartDownload.initModality(Modality.APPLICATION_MODAL);
            frameStartDownload.initOwner(screenDownloaderList);
            frameStartDownload.show();

            frameStartDownload.setOnCloseRequest(event -> {
                if (StartDownload.thread.getState().toString().equals("RUNNABLE")) {
                    try {
                        StartDownload.thread.stop();
                        StartDownload.thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Alert alert = new Alert(null);
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Stop Download");
                    alert.setHeaderText(null);
                    alert.setContentText("The download of the list has stopped.");
                    alert.showAndWait();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}