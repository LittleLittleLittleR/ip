package littler.gui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import littler.LittleR;
import littler.command.Command;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {

    private static final double REPLY_DELAY_SECONDS = 1.5;
    private static final double WINDOW_CLOSE_DELAY_SECONDS = 2.5;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private LittleR littleR;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image littleRImage = new Image(this.getClass().getResourceAsStream("/images/LittleR.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the LittleR instance */
    public void setLittleR(LittleR lr) {
        littleR = lr;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other
     * containing LittleR's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = littleR.converse(input);
        PauseTransition replyDelay = new PauseTransition(Duration.seconds(REPLY_DELAY_SECONDS));

        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));
        replyDelay.setOnFinished(event -> {
            dialogContainer.getChildren().add(DialogBox.getLittleRDialog(response, littleRImage));
        });
        replyDelay.play();
        userInput.clear();

        if (Command.fromInput(input) == Command.EXIT) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition closeDelay = new PauseTransition(Duration.seconds(WINDOW_CLOSE_DELAY_SECONDS));

            closeDelay.setOnFinished(event -> {
                Stage stage = (Stage) dialogContainer.getScene().getWindow();
                stage.close();
                Platform.exit();
            });
            closeDelay.play();
        }
    }
}
