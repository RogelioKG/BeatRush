package org.notiva.beatrush.controller;

import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.notiva.beatrush.component.MaskLayer;
import org.notiva.beatrush.component.SongItemCard;
import org.notiva.beatrush.core.RhythmGameManager;
import org.notiva.beatrush.core.StageManager;
import org.notiva.beatrush.event.SongSelectedEvent;

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
        // 新增範例歌曲
        addSongs();
    }

    private void addSongs() {
        List<SongItemCard> cards = List.of(
                new SongItemCard(
                        "Racing into the Night", "YOASOBI", Duration.seconds(262),
                        "https://upload.wikimedia.org/wikipedia/en/9/93/Yoru_ni_Kakeru_cover_art.jpg",
                        true),
                new SongItemCard(
                        "Restriction", "Team Grimoire", Duration.seconds(150),
                        "https://static.wikia.nocookie.net/cytus/images/8/8c/ROBO_Head.jpg/revision/latest/scale-to-width-down/250?cb=20180911170605",
                        true),
                new SongItemCard(
                        "Cold", "Noizenecio", Duration.seconds(123),
                        "https://static.wikia.nocookie.net/cytus/images/8/8c/ROBO_Head.jpg/revision/latest/scale-to-width-down/250?cb=20180911170605",
                        true),
                new SongItemCard(
                        "Plastic Love", "Mariya Takeuchi", Duration.seconds(308),
                        "https://upload.wikimedia.org/wikipedia/en/6/66/Mariya_Takeuchi_-_Plastic_Love_2021.jpg",
                        true),
                new SongItemCard(
                        "Where Do I Belong", "Infected Mushroom", Duration.seconds(207),
                        "https://i1.sndcdn.com/artworks-000244494041-c33od8-t500x500.jpg",
                        true)
        );

        List<Group> scalingEffectCards = cards.stream()
                .map(card -> {
                    Group g = new Group();
                    g.getChildren().add(card);
                    return g;
                })
                .toList();

        allSongList.getChildren().addAll(scalingEffectCards);
    }
}
