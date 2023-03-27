package com.example;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML //bibliotecas controladoras da JavaFX e Maven
    private void switchToSecondary() throws IOException {
        NumberPuzzle.setRoot("secondary");
    }
}
