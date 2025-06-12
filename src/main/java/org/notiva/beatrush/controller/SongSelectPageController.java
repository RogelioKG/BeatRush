package org.notiva.beatrush.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.notiva.beatrush.component.MaskLayer;
import org.notiva.beatrush.component.SongItemCard;
import org.notiva.beatrush.core.ResourceLoader;
import org.notiva.beatrush.core.RhythmGameManager;
import org.notiva.beatrush.core.StageManager;
import org.notiva.beatrush.event.SongSelectedEvent;
import org.notiva.beatrush.util.Song;

public class SongSelectPageController {

    @FXML
    private StackPane root;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox allSongList;
    @FXML
    private VBox favoriteSongList;
    @FXML
    private MaskLayer maskLayer;

    private final StageManager stageManager = StageManager.getInstance();
    private final RhythmGameManager rhythmGameManager = RhythmGameManager.getInstance();

    @FXML
    protected void initialize() {
        // 開啟頁面時，應滾動到最上面
        Platform.runLater(() -> scrollPane.setVvalue(0));
        // root 監聽 MaskLayerShowEvent 和 MaskLayerHideEvent，
        maskLayer.addEventHandlersFor(root);
        // root 監聽 SongSelectedEvent
        root.addEventHandler(SongSelectedEvent.SONG_SELECTED, e -> {
            rhythmGameManager.setCurrentSong(e.getSong());
            stageManager.showStage("BeatRush", "/view/page/RhythmGamePage.fxml");
        });
        // 新增歌曲
        addSongs();
    }

    private void addSongs() {
        List<Group> cards = ResourceLoader.loadAllMetadata()
                .stream()
                .map(song -> SongItemCard.createFromSong(song, true))
                .map(SongItemCard::wrapInGroup)
                .toList();
        allSongList.getChildren().addAll(cards);
    }
}
