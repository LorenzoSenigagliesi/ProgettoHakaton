package unicam;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {
    public void start(Stage primaryStage) throws IOException {
        HBox root = new HBox();

        primaryStage.setTitle("HACKATHON");
        primaryStage.setScene(new Scene(root, 1000, 730));
        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}