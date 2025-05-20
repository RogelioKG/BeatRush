# Example

> [!CAUTION]
> 僅作為參考使用

## JSON 譜面格式

```json
{
  "title": "Example song",
  "artist": "Example artist",
  "bpm": 120,
  "audioFile": "audio/demo_song.mp3",
  "offset": 0.0,
  "notes": [
    {
      "type": "tap",
      "track": 0,
      "timestamp": 1000,
      "duration": 0
    },
    {
      "type": "tap",
      "track": 2,
      "timestamp": 1500,
      "duration": 0
    },
    {
      "type": "hold",
      "track": 1,
      "timestamp": 2000,
      "duration": 1.5
    },
    {
      "type": "tap",
      "track": 3,
      "timestamp": 3000,
      "duration": 0
    }
  ]
}
```

## 核心介面和類別設計

### 1. 模型 (Model)

#### `Note.java` (介面)
```java
public interface Note {
    long getTimestamp();
    int getTrack();
    NoteType getType();
    double getDuration(); // 對於 TapNote 來說是 0
    
    enum NoteType {
        TAP,
        HOLD
    }
}
```

#### `TapNote.java`
```java
public class TapNote implements Note {
    private final long timestamp;
    private final int track;

    // 建構子和 getter 方法
    // 實作 Note 介面的方法
    
    @Override
    public NoteType getType() {
        return NoteType.TAP;
    }
    
    @Override
    public double getDuration() {
        return 0; // Tap note 沒有持續時間
    }
}
```

#### `HoldNote.java`
```java
public class HoldNote implements Note {
    private final long timestamp;
    private final int track;
    private final double duration;

    // 建構子和 getter 方法
    // 實作 Note 介面的方法
    
    @Override
    public NoteType getType() {
        return NoteType.HOLD;
    }
}
```

#### `Chart.java`
```java
public class Chart {
    private final String title;
    private final String artist;
    private final String audioFilePath;
    private final int bpm;
    private final List<Note> notes;
    
    // 建構子和 getter 方法
}
```

#### `ChartLoader.java`
```java
public class ChartLoader {
    /**
     * 從 JSON 檔案載入譜面
     */
    public Chart loadFromJson(String jsonFilePath) throws IOException {
        // 實作從 JSON 檔案讀取譜面的邏輯
        // 將 JSON 資料轉換為 Chart 物件和 Note 列表
    }
}
```

#### `JudgmentType.java`
```java
public enum JudgmentType {
    PERFECT(100, "Perfect!"),
    GREAT(80, "Great!"),
    GOOD(50, "Good"),
    BAD(20, "Bad"),
    MISS(0, "Miss");
    
    private final int score;
    private final String displayText;
    
    // 建構子和 getter 方法
}
```

#### `Score.java`
```java
public class Score {
    private int currentScore;
    private int combo;
    private int maxCombo;
    private final Map<JudgmentType, Integer> judgmentCounts;
    
    // 方法用於更新分數和 combo
    public void addJudgment(JudgmentType judgment) {
        // 更新分數、combo 和判定統計
    }
    
    // 其他分數相關方法
}
```

#### `GameModel.java`
```java
public class GameModel {
    private final StringProperty songTitle = new SimpleStringProperty();
    private final StringProperty artistName = new SimpleStringProperty();
    private final IntegerProperty currentScore = new SimpleIntegerProperty(0);
    private final IntegerProperty combo = new SimpleIntegerProperty(0);
    
    private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private final DoubleProperty songProgress = new SimpleDoubleProperty(0);
    
    private final ObjectProperty<Chart> currentChart = new SimpleObjectProperty<>();
    private final ObjectProperty<Score> score = new SimpleObjectProperty<>(new Score());
    
    private final List<ObjectProperty<Note>> activeNotes = new ArrayList<>();
    private final List<BooleanProperty> trackPressed = new ArrayList<>(
        Arrays.asList(
            new SimpleBooleanProperty(false),
            new SimpleBooleanProperty(false),
            new SimpleBooleanProperty(false),
            new SimpleBooleanProperty(false)
        )
    );
    
    // 一般 getter/setter 和 Property getter
    public StringProperty songTitleProperty() {
        return songTitle;
    }
    
    public StringProperty artistNameProperty() {
        return artistName;
    }
    
    public IntegerProperty currentScoreProperty() {
        return currentScore;
    }
    
    public IntegerProperty comboProperty() {
        return combo;
    }
    
    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }
    
    public BooleanProperty trackPressedProperty(int trackIndex) {
        return trackPressed.get(trackIndex);
    }
    
    public DoubleProperty songProgressProperty() {
        return songProgress;
    }
    
    // 其他必要的 getter/setter 和 Property getter
}
```

### 2. 視圖 (View)

#### `GameView.fxml` (部分內容)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.control.*?>
<?import com.rhythmgame.view.components.*?>

<BorderPane xmlns="http://javafx.com/javafx"
            xmlns:fx="http://javafx.com/fxml"
            fx:controller="com.rhythmgame.view.GameViewController">
    <top>
        <HBox alignment="CENTER">
            <Label fx:id="songTitleLabel" />
            <Label fx:id="artistLabel" />
        </HBox>
    </top>
    <center>
        <StackPane>
            <HBox alignment="BOTTOM_CENTER">
                <TrackView fx:id="track1" />
                <TrackView fx:id="track2" />
                <TrackView fx:id="track3" />
                <TrackView fx:id="track4" />
            </HBox>
        </StackPane>
    </center>
    <right>
        <VBox alignment="TOP_CENTER">
            <Label fx:id="scoreLabel" />
            <Label fx:id="comboLabel" />
        </VBox>
    </right>
    <bottom>
        <HBox alignment="CENTER">
            <ProgressBar fx:id="songProgressBar" />
            <Button fx:id="pauseButton" text="Pause" />
        </HBox>
    </bottom>
</BorderPane>
```

#### `GameViewController.java`
```java
public class GameViewController {
    @FXML private Label songTitleLabel;
    @FXML private Label artistLabel;
    @FXML private Label scoreLabel;
    @FXML private Label comboLabel;
    @FXML private ProgressBar songProgressBar;
    @FXML private Button pauseButton;
    @FXML private TrackView track1;
    @FXML private TrackView track2;
    @FXML private TrackView track3;
    @FXML private TrackView track4;
    
    private Runnable onPauseAction;
    private final Map<Integer, Consumer<KeyEvent>> trackKeyHandlers = new HashMap<>();
    
    public void initialize(GameModel model, Map<Integer, Consumer<KeyEvent>> keyHandlers) {
        // 綁定 UI 元素到模型
        songTitleLabel.textProperty().bind(model.songTitleProperty());
        artistLabel.textProperty().bind(model.artistNameProperty());
        scoreLabel.textProperty().bind(model.currentScoreProperty().asString());
        comboLabel.textProperty().bind(model.comboProperty().asString().concat(" Combo"));
        songProgressBar.progressProperty().bind(model.songProgressProperty());
        
        // 初始化軌道視圖
        this.trackKeyHandlers.putAll(keyHandlers);
        
        // 設置按鈕動作和其他事件處理
        pauseButton.setOnAction(e -> {
            if (onPauseAction != null) onPauseAction.run();
        });
    }
    
    public void setOnPauseAction(Runnable action) {
        this.onPauseAction = action;
    }
    
    public void addNote(Note note) {
        // 根據音符類型創建適當的視圖元件
        NoteView noteView;
        
        if (note.getType() == Note.NoteType.TAP) {
            noteView = new TapNoteView();
        } else {
            noteView = new HoldNoteView(((HoldNote) note).getDuration());
        }
        
        // 將音符添加到正確的軌道
        switch (note.getTrack()) {
            case 0:
                track1.addNote(noteView);
                break;
            case 1:
                track2.addNote(noteView);
                break;
            case 2:
                track3.addNote(noteView);
                break;
            case 3:
                track4.addNote(noteView);
                break;
        }
    }
    
    public void showHitEffect(int track, JudgmentType judgment) {
        // 在指定軌道顯示命中特效
        TrackView trackView = getTrackByIndex(track);
        trackView.showHitEffect(judgment);
    }
    
    public void handleKeyPressed(KeyEvent event) {
        // 根據按下的鍵調用相應的處理器
        for (Map.Entry<Integer, Consumer<KeyEvent>> entry : trackKeyHandlers.entrySet()) {
            int track = entry.getKey();
            Consumer<KeyEvent> handler = entry.getValue();
            
            // 檢查是否是該軌道的按鍵
            if (isTrackKey(event, track)) {
                handler.accept(event);
                break;
            }
        }
    }
    
    private boolean isTrackKey(KeyEvent event, int track) {
        // 檢查按鍵是否對應到指定軌道
        // 可以配置特定按鍵對應到特定軌道
    }
    
    private TrackView getTrackByIndex(int track) {
        switch (track) {
            case 0: return track1;
            case 1: return track2;
            case 2: return track3;
            case 3: return track4;
            default: throw new IllegalArgumentException("Invalid track index: " + track);
        }
    }
}
```

#### `TrackView.java`
```java
public class TrackView extends Pane {
    private static final double TRACK_WIDTH = 100;
    private static final double TRACK_HEIGHT = 600;
    
    private final Rectangle background;
    private final Rectangle hitZone;
    private final List<NoteView> activeNotes = new ArrayList<>();
    
    public TrackView() {
        setPrefSize(TRACK_WIDTH, TRACK_HEIGHT);
        
        // 初始化背景
        background = new Rectangle(0, 0, TRACK_WIDTH, TRACK_HEIGHT);
        background.getStyleClass().add("track-background");
        
        // 初始化判定區域
        hitZone = new Rectangle(0, TRACK_HEIGHT - 50, TRACK_WIDTH, 50);
        hitZone.getStyleClass().add("hit-zone");
        
        getChildren().addAll(background, hitZone);
    }
    
    public void addNote(NoteView noteView) {
        // 設定音符的初始位置
        noteView.setLayoutX((TRACK_WIDTH - noteView.getPrefWidth()) / 2);
        noteView.setLayoutY(0 - noteView.getPrefHeight());
        
        // 添加到視圖和追蹤列表
        getChildren().add(noteView);
        activeNotes.add(noteView);
    }
    
    public void updateNotes(double deltaTime, double speed) {
        // 更新所有活動音符的位置
        Iterator<NoteView> iterator = activeNotes.iterator();
        while (iterator.hasNext()) {
            NoteView noteView = iterator.next();
            noteView.update(deltaTime, speed);
            
            // 移除已經超出畫面的音符
            if (noteView.getLayoutY() > TRACK_HEIGHT) {
                getChildren().remove(noteView);
                iterator.remove();
            }
        }
    }
    
    public void showHitEffect(JudgmentType judgment) {
        // 建立並顯示命中特效
        HitEffect effect = new HitEffect(judgment);
        effect.setLayoutX((TRACK_WIDTH - effect.getPrefWidth()) / 2);
        effect.setLayoutY(hitZone.getLayoutY());
        
        getChildren().add(effect);
        
        // 設定特效動畫完成後自動移除
        effect.playAnimation(() -> getChildren().remove(effect));
    }
    
    public void setPressed(boolean pressed) {
        // 更新軌道外觀以顯示按壓狀態
        if (pressed) {
            hitZone.getStyleClass().add("hit-zone-pressed");
        } else {
            hitZone.getStyleClass().remove("hit-zone-pressed");
        }
    }
}
```

#### `NoteView.java` (介面)
```java
public interface NoteView {
    void update(double deltaTime, double speed);
    double getPrefWidth();
    double getPrefHeight();
    double getDistanceToHitZone(double hitZoneY);
    boolean isHit();
    void markAsHit();
}
```

#### `TapNoteView.java`
```java
public class TapNoteView extends Rectangle implements NoteView {
    private static final double NOTE_SIZE = 80;
    private boolean hit = false;
    
    public TapNoteView() {
        super(NOTE_SIZE, NOTE_SIZE);
        getStyleClass().add("tap-note");
    }
    
    @Override
    public void update(double deltaTime, double speed) {
        setLayoutY(getLayoutY() + speed * deltaTime);
    }
    
    @Override
    public double getPrefWidth() {
        return NOTE_SIZE;
    }
    
    @Override
    public double getPrefHeight() {
        return NOTE_SIZE;
    }
    
    @Override
    public double getDistanceToHitZone(double hitZoneY) {
        return Math.abs((getLayoutY() + NOTE_SIZE / 2) - hitZoneY);
    }
    
    @Override
    public boolean isHit() {
        return hit;
    }
    
    @Override
    public void markAsHit() {
        hit = true;
        getStyleClass().add("note-hit");
    }
}
```

#### `HoldNoteView.java`
```java
public class HoldNoteView extends Pane implements NoteView {
    private static final double NOTE_WIDTH = 80;
    
    private final Rectangle headNote;
    private final Rectangle bodyNote;
    private final Rectangle tailNote;
    
    private final double duration;
    private boolean headHit = false;
    private boolean tailHit = false;
    
    public HoldNoteView(double duration) {
        this.duration = duration;
        
        // 計算 Hold Note 的長度
        double noteHeight = duration * 100; // 假設速度為 100 單位/秒
        
        // 初始化 hold note 的三個部分
        headNote = new Rectangle(NOTE_WIDTH, 30);
        headNote.getStyleClass().add("hold-note-head");
        
        bodyNote = new Rectangle(NOTE_WIDTH, noteHeight - 60);
        bodyNote.setLayoutY(30);
        bodyNote.getStyleClass().add("hold-note-body");
        
        tailNote = new Rectangle(NOTE_WIDTH, 30);
        tailNote.setLayoutY(noteHeight - 30);
        tailNote.getStyleClass().add("hold-note-tail");
        
        getChildren().addAll(bodyNote, headNote, tailNote);
        setPrefSize(NOTE_WIDTH, noteHeight);
    }
    
    @Override
    public void update(double deltaTime, double speed) {
        setLayoutY(getLayoutY() + speed * deltaTime);
    }
    
    @Override
    public double getPrefWidth() {
        return NOTE_WIDTH;
    }
    
    @Override
    public double getPrefHeight() {
        return headNote.getHeight() + bodyNote.getHeight() + tailNote.getHeight();
    }
    
    @Override
    public double getDistanceToHitZone(double hitZoneY) {
        // 頭部距離判定線的距離
        return Math.abs((getLayoutY() + headNote.getHeight() / 2) - hitZoneY);
    }
    
    public double getTailDistanceToHitZone(double hitZoneY) {
        // 尾部距離判定線的距離
        return Math.abs((getLayoutY() + getPrefHeight() - tailNote.getHeight() / 2) - hitZoneY);
    }
    
    @Override
    public boolean isHit() {
        return headHit && tailHit;
    }
    
    @Override
    public void markAsHit() {
        headHit = true;
        headNote.getStyleClass().add("note-hit");
    }
    
    public void markTailAsHit() {
        tailHit = true;
        tailNote.getStyleClass().add("note-hit");
    }
    
    public boolean isHeadHit() {
        return headHit;
    }
}
```

### 3. 控制器 (Controller)

#### `GameController.java`
```java
public class GameController {
    private final GameModel model;
    private final Region view;
    private final GameInteractor interactor;
    
    private final Map<Integer, Consumer<KeyEvent>> trackKeyHandlers = new HashMap<>();
    
    public GameController() {
        // 初始化模型
        model = new GameModel();
        
        // 初始化互動器
        interactor = new GameInteractor(model);
        
        // 設置按鍵處理器
        setupKeyHandlers();
        
        // 初始化視圖 (通過 FXML 載入器)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/rhythmgame/view/GameView.fxml"));
        try {
            view = loader.load();
            GameViewController viewController = loader.getController();
            viewController.initialize(model, trackKeyHandlers);
            viewController.setOnPauseAction(interactor::togglePause);
            
            // 添加鍵盤事件處理
            view.setOnKeyPressed(this::handleKeyPressed);
            view.setOnKeyReleased(this::handleKeyReleased);
            
            // 確保視圖可以獲得焦點
            view.setFocusTraversable(true);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load game view", e);
        }
    }
    
    public Region getView() {
        return view;
    }
    
    public void loadChart(String jsonFilePath) {
        interactor.loadChart(jsonFilePath);
    }
    
    public void startGame() {
        interactor.startGame();
    }
    
    private void setupKeyHandlers() {
        // 設置每個軌道的按鍵處理器
        trackKeyHandlers.put(0, e -> interactor.handleTrackKeyPressed(0));
        trackKeyHandlers.put(1, e -> interactor.handleTrackKeyPressed(1));
        trackKeyHandlers.put(2, e -> interactor.handleTrackKeyPressed(2));
        trackKeyHandlers.put(3, e -> interactor.handleTrackKeyPressed(3));
    }
    
    private void handleKeyPressed(KeyEvent event) {
        // 將鍵盤事件傳遞給視圖控制器
        if (view instanceof Parent) {
            Node node = ((Parent) view).lookup("#gameViewController");
            if (node instanceof GameViewController) {
                ((GameViewController) node).handleKeyPressed(event);
            }
        }
    }
    
    private void handleKeyReleased(KeyEvent event) {
        // 處理按鍵釋放事件
        // 這裡主要用於處理 HOLD 音符的釋放
        for (int i = 0; i < 4; i++) {
            if (isTrackKey(event, i)) {
                interactor.handleTrackKeyReleased(i);
                break;
            }
        }
    }
    
    private boolean isTrackKey(KeyEvent event, int track) {
        // 檢查按鍵是否對應到指定軌道
        switch (track) {
            case 0: return event.getCode() == KeyCode.D;
            case 1: return event.getCode() == KeyCode.F;
            case 2: return event.getCode() == KeyCode.J;
            case 3: return event.getCode() == KeyCode.K;
            default: return false;
        }
    }
}
```

### 4. 互動器 (Interactor)

#### `GameInteractor.java`
```java
public class GameInteractor {
    private final GameModel model;
    private final AudioPlayer audioPlayer;
    private final ChartLoader chartLoader;
    private final InputHandler inputHandler;
    private final NoteMovement noteMovement;
    
    private AnimationTimer gameLoop;
    private long lastFrameTime;
    
    public GameInteractor(GameModel model) {
        this.model = model;
        this.audioPlayer = new AudioPlayer();
        this.chartLoader = new ChartLoader();
        this.inputHandler = new InputHandler(model);
        this.noteMovement = new NoteMovement();
        
        initializeGameLoop();
    }
    
    public void loadChart(String jsonFilePath) {
        try {
            Chart chart = chartLoader.loadFromJson(jsonFilePath);
            model.getCurrentChart().set(chart);
            model.getSongTitle().set(chart.getTitle());
            model.getArtistName().set(chart.getArtist());
            
            // 預載音訊
            audioPlayer.loadAudio(chart.getAudioFilePath());
        } catch (IOException e) {
            // 處理載入失敗
            System.err.println("Failed to load chart: " + e.getMessage());
        }
    }
    
    public void startGame() {
        if (model.getCurrentChart().get() == null) {
            throw new IllegalStateException("No chart loaded");
        }
        
        // 重置遊戲狀態
        model.getCurrentScore().set(0);
        model.getCombo().set(0);
        model.getScore().set(new Score());
        
        // 開始音訊播放
        audioPlayer.play();
        
        // 開始遊戲迴圈
        lastFrameTime = System.nanoTime();
        model.getIsPlaying().set(true);
        gameLoop.start();
    }
    
    public void togglePause() {
        if (model.getIsPlaying().get()) {
            gameLoop.stop();
            audioPlayer.pause();
            model.getIsPlaying().set(false);
        } else {
            lastFrameTime = System.nanoTime();
            gameLoop.start();
            audioPlayer.resume();
            model.getIsPlaying().set(true);
        }
    }
    
    public void handleTrackKeyPressed(int track) {
        if (!model.getIsPlaying().get()) return;
        
        // 更新模型中的軌道狀態
        model.getTrackPressed(track).set(true);
        
        // 處理音符判定
        inputHandler.handleInput(track, audioPlayer.getCurrentTime());
    }
    
    public void handleTrackKeyReleased(int track) {
        if (!model.getIsPlaying().get()) return;
        
        // 更新模型中的軌道狀態
        model.getTrackPressed(track).set(false);
        
        // 處理長音符的釋放判定
        inputHandler.handleRelease(track, audioPlayer.getCurrentTime());
    }
    
    private void initializeGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // 計算幀間隔時間
                double deltaTime = (now - lastFrameTime) / 1_000_000_000.0;
                lastFrameTime = now;
                
                // 更新歌曲進度
                double currentTime = audioPlayer.getCurrentTime();
                double totalDuration = audioPlayer.getTotalDuration();
                model.getSongProgress().set(currentTime / totalDuration);
                
                // 更新音符位置
                updateNotes(deltaTime, currentTime);
                
                // 檢查是否結束
                if (audioPlayer.isFinished() && model.getActiveNotes().isEmpty()) {
                    endGame();
                }
            }
        };
    }
    
    private void updateNotes(double deltaTime, double currentTime) {
        Chart chart = model.getCurrentChart().get();
        if (chart == null) return;
        
        // 更新現有音符
        for (ObjectProperty<Note> noteProperty : model.getActiveNotes()) {
            Note note = noteProperty.get();
            if (note != null) {
                // 使用 NoteMovement 更新音符位置
                noteMovement.updateNotePosition(note, deltaTime);
                
                // 檢查 MISS 判定
                if (note.getTimestamp() + 200 < currentTime * 1000 && !inputHandler.isNoteHit(note)) {
                    // 標記為 MISS
                    inputHandler.judgeNote(note, JudgmentType.MISS);
                }
            }
        }
        
        // 檢查譜面中的音符，將即將出現的音符添加到活動音符列表
        for (Note note : chart.getNotes()) {
            // 如果音符時間還沒到，且在可視範圍內
            if (note.getTimestamp() > currentTime * 1000 && 
                note.getTimestamp() < (currentTime + 3) * 1000) { // 假設 3 秒可視範圍
                
                // 創建新的音符屬性並添加到活動音符列表
                ObjectProperty<Note> noteProperty = new SimpleObjectProperty<>(note);
                model.getActiveNotes().add(noteProperty);
            }
        }
    }
    
    private void endGame() {
        gameLoop.stop();
        model.getIsPlaying().set(false);
        
        // 遊戲結束處理邏輯
        // 例如顯示分數結算界面等
    }
}
```

#### `AudioPlayer.java`
```java
public class AudioPlayer {
    private Media media;
    private MediaPlayer mediaPlayer;
    
    public void loadAudio(String audioFilePath) {
        try {
            media = new Media(new File(audioFilePath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnEndOfMedia(() -> {
                // 音訊播放結束的處理
            });
        } catch (Exception e) {
            System.err.println("Failed to load audio: " + e.getMessage());
        }
    }
    
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
    
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
    
    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
    
    public double getCurrentTime() {
        return mediaPlayer != null ? mediaPlayer.getCurrentTime().toSeconds() : 0;
    }
    
    public double getTotalDuration() {
        return media != null ? media.getDuration().toSeconds() : 0;
    }
    
    public boolean isFinished() {
        return mediaPlayer != null && mediaPlayer.getCurrentTime().equals(mediaPlayer.getTotalDuration());
    }
}
```

#### `InputHandler.java`
```java
public class InputHandler {
    private static final double PERFECT_THRESHOLD = 50; // 毫秒
    private static final double GREAT_THRESHOLD = 100;
    private static final double GOOD_THRESHOLD = 150;
    private static final double BAD_THRESHOLD = 200;
    
    private final GameModel model;
    private final Map<Note, Boolean> hitNotes = new HashMap<>();
    private final Map<Integer, HoldNote> activeHoldNotes = new HashMap<>();
    
    public InputHandler(GameModel model) {
        this.model = model;
    }
    
    public void handleInput(int track, double currentTimeSeconds) {
        long currentTime = (long) (currentTimeSeconds * 1000);
        
        // 查找該軌道最近的音符
        Note nearestNote = findNearestNote(track, currentTime);
        if (nearestNote == null) return;
        
        // 計算時間差
        long timeDiff = Math.abs(nearestNote.getTimestamp() - currentTime);
        
        // 判定命中精度
        JudgmentType judgment;
        if (timeDiff <= PERFECT_THRESHOLD) {
            judgment = JudgmentType.PERFECT;
        } else if (timeDiff <= GREAT_THRESHOLD) {
            judgment = JudgmentType.GREAT;
        } else if (timeDiff <= GOOD_THRESHOLD) {
            judgment = JudgmentType.GOOD;
        } else if (timeDiff <= BAD_THRESHOLD) {
            judgment = JudgmentType.BAD;
        } else {
            // 超出判定範圍，不處理
            return;
        }
        
        // 處理不同類型的音符
        if (nearestNote.getType() == Note.NoteType.TAP) {
            // 處理一般點擊音符
            judgeNote(nearestNote, judgment);
        } else if (nearestNote.getType() == Note.NoteType.HOLD) {
            // 處理長按音符的開始部分
            HoldNote holdNote = (HoldNote) nearestNote;
            if (!isNoteHit(holdNote)) {
                judgeNote(holdNote, judgment);
                // 記錄活動中的長按音符
                activeHoldNotes.put(track, holdNote);
            }
        }
    }
    
    public void handleRelease(int track, double currentTimeSeconds) {
        // 處理長按音符的結束部分
        HoldNote holdNote = activeHoldNotes.get(track);
        if (holdNote == null) return;
        
        long currentTime = (long) (currentTimeSeconds * 1000);
        long holdEndTime = holdNote.getTimestamp() + (long)(holdNote.getDuration() * 1000);
        long timeDiff = Math.abs(holdEndTime - currentTime);
        
        // 判定長按結束的精度
        JudgmentType judgment;
        if (timeDiff <= PERFECT_THRESHOLD) {
            judgment = JudgmentType.PERFECT;
        } else if (timeDiff <= GREAT_THRESHOLD) {
            judgment = JudgmentType.GREAT;
        } else if (timeDiff <= GOOD_THRESHOLD) {
            judgment = JudgmentType.GOOD;
        } else if (timeDiff <= BAD_THRESHOLD) {
            judgment = JudgmentType.BAD;
        } else {
            judgment = JudgmentType.MISS;
        }
        
        // 更新得分並顯示判定
        Score score = model.getScore().get();
        score.addJudgment(judgment);
        model.getCurrentScore().set(score.getCurrentScore());
        model.getCombo().set(score.getCombo());
        
        // 移除活動中的長按音符
        activeHoldNotes.remove(track);
    }
    
    public void judgeNote(Note note, JudgmentType judgment) {
        // 標記音符為已命中
        hitNotes.put(note, true);
        
        // 更新得分
        Score score = model.getScore().get();
        score.addJudgment(judgment);
        model.getCurrentScore().set(score.getCurrentScore());
        model.getCombo().set(score.getCombo());
    }
    
    public boolean isNoteHit(Note note) {
        return hitNotes.containsKey(note) && hitNotes.get(note);
    }
    
    private Note findNearestNote(int track, long currentTime) {
        Note nearestNote = null;
        long minTimeDiff = Long.MAX_VALUE;
        
        // 查找最接近當前時間的音符
        for (ObjectProperty<Note> noteProperty : model.getActiveNotes()) {
            Note note = noteProperty.get();
            if (note != null && note.getTrack() == track && !isNoteHit(note)) {
                long timeDiff = Math.abs(note.getTimestamp() - currentTime);
                if (timeDiff < minTimeDiff && timeDiff <= BAD_THRESHOLD) {
                    minTimeDiff = timeDiff;
                    nearestNote = note;
                }
            }
        }
        
        return nearestNote;
    }
}