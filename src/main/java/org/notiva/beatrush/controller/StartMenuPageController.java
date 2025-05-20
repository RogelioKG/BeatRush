package org.notiva.beatrush.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import org.notiva.beatrush.component.Card;

public class StartMenuPageController {
    @FXML
    FlowPane flowContainer;

    @FXML
    protected void initialize() {
        Card card3 = new Card("https://picsum.photos/500/300", "Third Card", "The description of third card");
        flowContainer.getChildren().add(card3);
    }
}
