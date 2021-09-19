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

import java.util.Objects;

public class Main extends Application {

    public static Stage screenDownloaderList;
    public static Stage screenAllUserVideos;
    public static Stage screenOneVideo;
    public static Stage screenLogs;
    public static Stage frameSaveAsFile;
    public static Stage frameImportFile;
    public static Stage frameStartDownload;

    //	Main Windows
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader root = new FXMLLoader(Main.class.getResource("downloadlist/DownloaderList.fxml"));
            Scene scene = new Scene(root.load(), 840, 580);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toString());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Tiktok Downloader - Download List");
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
            screenDownloaderList = primaryStage;
            screenDownloaderList.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showScreenDownloadList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("downloadlist/DownloaderList.fxml"));
            Parent root = loader.load();
            screenDownloaderList = new Stage();
            screenDownloaderList.setTitle("Tiktok Downloader - Download List");
            screenDownloaderList.setScene(new Scene(root));
            screenDownloaderList.setResizable(false);
            screenDownloaderList.initModality(Modality.APPLICATION_MODAL);
            screenDownloaderList.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
            screenDownloaderList.show();
            screenAllUserVideos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showScreenAllUserVideos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("alluservideos/AllUserVideos.fxml"));
            Parent root = loader.load();
            screenAllUserVideos = new Stage();
            screenAllUserVideos.setTitle("Tiktok Downloader - All User Videos");
            screenAllUserVideos.setScene(new Scene(root));
            screenAllUserVideos.setResizable(false);
            screenAllUserVideos.initModality(Modality.APPLICATION_MODAL);
            screenAllUserVideos.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
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
            screenOneVideo.setTitle("Tiktok Downloader - One Video");
            screenOneVideo.setScene(new Scene(root));
            screenOneVideo.setResizable(false);
            screenOneVideo.initModality(Modality.APPLICATION_MODAL);
            screenOneVideo.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
            screenOneVideo.show();
            screenDownloaderList.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showScreenLogs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logs/Logs.fxml"));
            Parent root = loader.load();
            screenLogs = new Stage();
            screenLogs.setTitle("Tiktok Downloader - Logs");
            screenLogs.setScene(new Scene(root));
            screenLogs.setResizable(false);
            screenLogs.initModality(Modality.APPLICATION_MODAL);
            screenLogs.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
            screenLogs.show();
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
            frameSaveAsFile.setTitle("Tiktok Downloader - Save As File");
            frameSaveAsFile.setScene(new Scene(root));
            frameSaveAsFile.setResizable(false);
            frameSaveAsFile.initModality(Modality.APPLICATION_MODAL);
            frameSaveAsFile.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
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
            frameImportFile.setTitle("Tiktok Downloader - Import File");
            frameImportFile.setScene(new Scene(root));
            frameImportFile.setResizable(false);
            frameImportFile.initModality(Modality.APPLICATION_MODAL);
            frameImportFile.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
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
            frameStartDownload.setTitle("Tiktok Downloader - Start Download");
            frameStartDownload.setScene(new Scene(root));
            frameStartDownload.setResizable(false);
            frameStartDownload.initModality(Modality.APPLICATION_MODAL);
            frameStartDownload.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
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