package com.example;

import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML  //bibliotecas controladoras da JavaFX e Maven
    private void switchToPrimary() throws IOException {
        NumberPuzzle.setRoot("primary");
    }
}