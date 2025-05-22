package org.notiva.beatrush.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.notiva.beatrush.core.Loader;

public class Card extends AnchorPane {

    @FXML
    private ImageView imageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    // 屬性定義
    private final StringProperty imageUrl = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    /**
     * 預設建構子，會載入對應的 FXML 版面，並綁定屬性。
     * <p>
     * 建議在 FXML 中使用此建構子（由 JavaFX 自動呼叫）。
     *
     * <p><b>FXML 使用範例：</b></p>
     * <pre>{@code
     * <Card imageUrl="https://picsum.photos/500/300"
     *       title="First Card"
     *       description="The description of first card" />
     * }</pre>
     */
    public Card() {
        Loader.loadComponentView(this, "/view/component/Card.fxml");
        bindProperty();
    }

    /**
     * 使用指定參數建立卡片元件。
     * <p>
     * 建議在 Java 程式中手動建立元件時使用，例如控制器中動態加入卡片。
     *
     * <p><b>Java 使用範例：</b></p>
     * <pre>{@code
     * FlowPane container = new FlowPane();
     * Card card = new Card(
     *     "https://picsum.photos/500/300",
     *     "Third Card",
     *     "The description of third card"
     * );
     * container.getChildren().add(card);
     * }</pre>
     *
     * @param imageUrl    卡片圖片的 URL
     * @param title       卡片標題
     * @param description 卡片描述文字
     */
    public Card(String imageUrl, String title, String description) {
        this();
        setImageUrl(imageUrl);
        setTitle(title);
        setDescription(description);
    }

    private void bindProperty() {
        // 綁定屬性到 UI 元素
        imageView.imageProperty().bind(imageUrlProperty().map(url -> {
            if (url == null || url.isEmpty()) {
                return null;
            }
            try {
                return new Image(url);
            } catch (Exception e) {
                System.err.printf("Failed to load image: %s%n", url);
                return null;
            }
        }));

        titleLabel.textProperty().bind(titleProperty());
        descriptionLabel.textProperty().bind(descriptionProperty());
    }

    // ImageUrl 屬性相關方法
    public String getImageUrl() {
        return imageUrl.get();
    }

    public void setImageUrl(String value) {
        imageUrl.set(value);
    }

    public StringProperty imageUrlProperty() {
        return imageUrl;
    }

    // Title 屬性相關方法
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String value) {
        title.set(value);
    }

    public StringProperty titleProperty() {
        return title;
    }

    // Description 屬性相關方法
    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}