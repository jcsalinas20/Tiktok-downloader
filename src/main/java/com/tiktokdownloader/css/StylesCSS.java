package com.tiktokdownloader.css;

import javafx.scene.control.TextField;

public class StylesCSS {

    public static void errorTextField(TextField textField) {
        textField.setStyle("-fx-background-color: #DBB1B1, #FFF0F0;");
    }

    public static void defaultTextField(TextField textField) {
        textField.setStyle(null);
    }

}