package littler.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import littler.LittleR;

/**
 * A GUI for LittleR using FXML.
 */
public class Main extends Application {

    private LittleR littleR = new LittleR("./data/littler.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setLittleR(littleR); // inject the LittleR instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
