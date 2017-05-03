package sk.drakkar.oar.gui.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class OarGui extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Drakkar GUI");
        Pane myPane = (Pane) FXMLLoader.load(getClass().getResource
                ("MainController.fxml"));
        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();
    }
}
