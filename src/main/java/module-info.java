module org.notiva.beatrush {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;

    opens org.notiva.beatrush.component to javafx.fxml, com.google.gson;
    opens org.notiva.beatrush.controller to javafx.fxml, com.google.gson;
    opens org.notiva.beatrush.util to javafx.fxml, com.google.gson;
    exports org.notiva.beatrush;
    opens org.notiva.beatrush.core to com.google.gson, javafx.fxml;
}