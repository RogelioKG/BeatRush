package org.notiva.beatrush.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.notiva.beatrush.component.GlowingBorderButton;
import org.notiva.beatrush.component.MaskLayer;
import org.notiva.beatrush.core.StageManager;


public class StartMenuPageController {

    @FXML
    private StackPane root;

    @FXML
    private MaskLayer maskLayer;

    @FXML
    private GlowingBorderButton startButton;

    @FXML
    protected void initialize() {
        maskLayer.show();
        startButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> maskLayer.hide());
        startButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> maskLayer.show());
    }

    @FXML
    protected void onStartButtonClick() {
        StageManager stageManager = StageManager.getInstance();
        stageManager.showStage("BeatRush", "/view/page/SongSelectPage.fxml");
    }
}
