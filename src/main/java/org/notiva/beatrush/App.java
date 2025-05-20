package org.notiva.beatrush;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.notiva.beatrush.util.Note;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Note>>() {
        }.getType();

        InputStream file = getClass().getResourceAsStream("/chart/Restriction.json");
        try (InputStreamReader reader = new InputStreamReader(file, StandardCharsets.UTF_8)) {
            List<Note> notes = gson.fromJson(reader, listType);
            for (Note note : notes) {
                System.out.println(note);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
