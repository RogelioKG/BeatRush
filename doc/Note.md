# Note

## Caution

### 1. 註解
+ 只要 method 有註解即可，實作內容不需要註解 (當然你願意加上註解是更好的)
+ 可交給 AI 生成
+ 註解格式：JavaDoc

### 2. 風格
+ 務必遵守命名 Java 命名規則
  + 請參考[菜鳥工程師 肉豬 - Java 程式的命名慣例](https://matthung0807.blogspot.com/2019/05/java-naming-convention.html)
+ 每個類別只負責一項具體工作
  + 例如：`Note` 類別做好音符的工作，`Score` 類別做好計分的工作，不要讓它們混為一談
+ 多用 `enum`
  + 避免使用 magic number (0, 1, 2, 3 是什麼？哪條軌道？)
    ```java
    int track = 1;
    switch (track) {
        case 0:
            break;
        case 1:
            break;
        case 2:
            break;
        case 3:
            break;
        default:
            break;
    }
    ```
  + 噢，所以有四條軌道，左邊、左中、右中、右邊
    ```java
    enum Track {
        LEFT,
        MIDDLE_LEFT,
        MIDDLE_RIGHT,
        RIGHT
    };
    ```
    ```java
    Track track = Track.LEFT;
    switch (track) {
        case LEFT:
            break;
        case MIDDLE_LEFT:
            break;
        case MIDDLE_RIGHT:
            break;
        case RIGHT:
            break;
        default:
            break;
    }
    ```

### 3. 千萬不能用系統時鐘計時
+ 系統時鐘不是單調時鐘 (monotonic)，會根據 NTC 調整時間 (時間會倒帶！)
+ 而且後續若要實作暫停功能會很麻煩
+ 我的建議是直接使用播放器本身的計時 `mediaPlayer.getCurrentTime()`

### 4. push 上來請保持 App 是空的
+ 你可以在 `App` 裡面實驗一些功能，但要推上來請保持空的 (要不然 App 你改一下、我改一下會很多衝突)
  ```java
  package org.notiva.beatrush;

  import javafx.application.Application;
  import javafx.stage.Stage;
  
  public class App extends Application {
  
      @Override
      public void start(Stage primaryStage) {
      }
  
      public static void main(String[] args) {
          launch(args);
      }
  }
  ```

### 5. 反射問題
+ 反射 (reflection)：在執行期去存取任意類別的資訊（甭管有沒有 `public`）
  + 在採用【模組系統】 (我們現在的專案) 構建的 Java 程式，預設是禁止反射的
  + 但問題是，`FXMLLoader` 需要在執行期去存取你的 Controller 類別
+ 所以我們要在 `module-info.java` 去開放某個套件，允許這個套件的所有類別，讓指定的第三方庫可以對它們反射
  + 語法是 `opens 我們的套件 to 第三方庫, 第三方庫...;`
    ```java
    module org.notiva.beatrush {
        requires javafx.controls;
        requires javafx.fxml;
        requires javafx.media;
        requires com.google.gson;

        // controller 套件內的所有類別，都能反射存取到
        opens org.notiva.beatrush.controller to javafx.fxml, com.google.gson;
        // util 套件內的所有類別，都能被反射存取到
        opens org.notiva.beatrush.util to javafx.fxml, com.google.gson;

        exports org.notiva.beatrush;
    }
    ```


## GUI
![](https://i.meee.com.tw/5E7yRZ7.png)

## Component

### 1. 卡片元件範例
![](https://i.meee.com.tw/hBcS9we.png)

### 2. 元件在哪使用？
+ 寫法 A - 用在 Java：`java/org/notiva/beatrush/controller/StartMenuPageController.java`
    > 你對這個寫法應該早已見怪不怪。
    ```diff
    package org.notiva.beatrush.controller;

    import javafx.fxml.FXML;
    import javafx.scene.layout.FlowPane;
    import org.notiva.beatrush.component.Card;

    public class StartMenuPageController {
        @FXML
        FlowPane flowContainer;

        @FXML
        protected void initialize() {
    +       Card card3 = new Card("https://picsum.photos/500/300", "Third Card", "The description of third card");
            flowContainer.getChildren().add(card3);
        }
    }
    ```
+ 寫法 B - 用在 FXML：`resources/view/page/StartMenuPage.fxml`
    > 這個寫法的好處在於，你把重複的 FXML 抽離出來了。\
    > 你想想這裏如果有 100 張重複的卡片，然後你因為某些原因要改變卡片的布局。\
    > 那用原始的寫死方式，肯定會改到死。
    ```diff
    <?xml version="1.0" encoding="UTF-8"?>

    <?import javafx.scene.layout.VBox?>
    <?import javafx.scene.layout.FlowPane?>
    <?import org.notiva.beatrush.component.Card?>

    <VBox xmlns="http://javafx.com/javafx" xmlns:fx="http://javafx.com/fxml"
        fx:controller="org.notiva.beatrush.controller.StartMenuPageController">
        <FlowPane hgap="20" vgap="20" fx:id="flowContainer">
    +       <Card imageUrl="https://picsum.photos/500/300"
    +             title="First Card"
    +             description="The description of first card" />

    +       <Card imageUrl="https://picsum.photos/500/300"
    +             title="Second Card"
    +             description="The description of second card" />
        </FlowPane>
    </VBox>
    ```

### 3. 元件在哪宣告？
+ `resources/view/component/Card.fxml`
    > 這裡就是元件的模板宣告，\
    > 注意到了嗎？重複的地方都在這裡宣告完了（從此脫離複製貼上的惡夢）。\
    > 可能你會說，啊這些重複的地方，我就在 Java 把它寫成 JavaFX 物件設定的方式就好了。\
    > 那就 `getChildren()` 啊，然後開始 `add()` 嘛...\
    > 但你冷靜想一下，你覺得下面 FXML 巢狀結構可讀性比較高？\
    > 還是 JavaFX 物件扁平設定的可讀性比較高？\
    > 註：樹狀結構用巢狀表達，這不是天經地義嗎🤔？\
    > `fx:root` 表示：根節點並非 FXML 決定，而是由一個外部 Java 類別決定。
    > `type="javafx.scene.layout.AnchorPane"` 表示：這個由外部 Java 類別決定的根節點，將會繼承 `AnchorPane`，你可以理解為它是一個你自定義的 `AnchorPane` 節點！
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>

    <?import javafx.scene.control.Label?>
    <?import javafx.scene.image.ImageView?>
    <?import javafx.scene.layout.AnchorPane?>
    <?import javafx.scene.layout.VBox?>
    <?import javafx.geometry.Insets?>

    <fx:root type="javafx.scene.layout.AnchorPane" xmlns="http://javafx.com/javafx"
            xmlns:fx="http://javafx.com/fxml" styleClass="card">
        <VBox spacing="8" styleClass="card-container">
            <padding>
                <Insets top="10" right="10" bottom="10" left="10" />
            </padding>
            <ImageView fx:id="imageView" fitWidth="200" fitHeight="120" preserveRatio="true" styleClass="card-image" />
            <Label fx:id="titleLabel" styleClass="card-title" wrapText="true" />
            <Label fx:id="descriptionLabel" styleClass="card-description" wrapText="true" />
        </VBox>
    </fx:root>
    ```

### 4. 元件的外部類別
+ `java/org/notiva/beatrush/component/Card.java`
    > 所以說，我們在【2. 元件在哪使用？】-【寫法 B - 用在 FXML】\
    > 看到的 `imageUrl="..." title="..." description="..."` 是什麼魔法？\
    > 別急，你先看到這裡有兩個 constructor。\
    > 第一個 constructor `Card()`，\
    > 是用在【寫法 B - 用在 FXML】的，`FXMLLoader` 會自動呼叫這個沒參數的 constructor，\
    > 然後會接著呼叫你的 setter (`setImageUrl` / `setTitle` / `setDescription`)。\
    > 沒錯，你在 FXML 傳進去的參數就是這樣設定的。\
    > 所以你的元件外部類別，<mark>一定要為傳進來的參數準備好 getter / setter</mark>！\
    > 第二個 constructor `Card(String imageUrl, String title, String description)`，\
    > 是用在【寫法 A - 用在 Java】的，這東西和元件其實沒什麼關係，\
    > 純粹就是抽出一個 constructor，讓你還是可以用 JavaFX 物件設定的方式使用元件。\
    > 因為這次沒有人會幫你呼叫 setter，所以你要自己 set，就這樣啦。


    ```diff
    package org.notiva.beatrush.component;

    import javafx.beans.property.SimpleStringProperty;
    import javafx.beans.property.StringProperty;
    import javafx.fxml.FXML;
    import javafx.scene.control.Label;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.AnchorPane;
    import org.notiva.beatrush.core.ResourceLoader;

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

    +   public Card() {
            Loader.loadComponentView(this, "/view/component/Card.fxml");
            bindProperty();
        }

    +   public Card(String imageUrl, String title, String description) {
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
                    System.err.println("無法載入圖片: " + url);
                    return null;
                }
            }));

            titleLabel.textProperty().bind(titleProperty());
            descriptionLabel.textProperty().bind(descriptionProperty());
        }

        // ImageUrl 屬性相關方法
    +   public String getImageUrl() {
            return imageUrl.get();
        }

    +   public void setImageUrl(String value) {
            imageUrl.set(value);
        }

        public StringProperty imageUrlProperty() {
            return imageUrl;
        }

        // Title 屬性相關方法
    +   public String getTitle() {
            return title.get();
        }

    +   public void setTitle(String value) {
            title.set(value);
        }

        public StringProperty titleProperty() {
            return title;
        }

        // Description 屬性相關方法
    +   public String getDescription() {
            return description.get();
        }

    +   public void setDescription(String value) {
            description.set(value);
        }

        public StringProperty descriptionProperty() {
            return description;
        }
    }
    ```