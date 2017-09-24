package ChatClient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.image.*;

public class ChatClientGUI extends Application {

    private VBox textOutputText;
    private ScrollPane scrollPane;
    private ChatClient client;
    private TextField textInputField;
    private BorderPane borderPane;
    private VBox inputField;
    private HBox emojis;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        client = new ChatClient("localhost", 5558, this);

        primaryStage.setTitle("ChatClientGUI");

        borderPane = new BorderPane();
        emojis = new HBox();
        inputField = new VBox();
        textInputField = new TextField();
        scrollPane = new ScrollPane();
        textOutputText = new VBox();

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHmax(400);
        scrollPane.setVmax(400);

        textOutputText.setSpacing(5);
        textOutputText.setPrefWidth(400);

        Button likeBtn = new Button("Hello world!");
        Image like = new Image (ChatClient.class.getResourceAsStream("like.png"));
        likeBtn.setGraphic(new ImageView(like));

        likeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.sendMessage("(Y)");
                ownMessage("(Y)");
            }
        });

        Button dislikeBtn = new Button("Hello world!");
        Image dislike = new Image (ChatClient.class.getResourceAsStream("dislike.png"));
        dislikeBtn.setGraphic(new ImageView(dislike));

        dislikeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.sendMessage("(Y)");
                ownMessage("(Y)");
            }
        });

        likeBtn.setMaxWidth(Double.MAX_VALUE);
        dislikeBtn.setMaxWidth(Double.MAX_VALUE);

        emojis.setHgrow(likeBtn, Priority.ALWAYS);
        emojis.setHgrow(dislikeBtn, Priority.ALWAYS);

        emojis.getChildren().addAll(likeBtn, dislikeBtn);
        inputField.getChildren().addAll(textInputField, emojis);

        renderScene();

        scrollPane.setContent(textOutputText);

        Scene scene = new Scene(borderPane, 400, 400);
        scene.getStylesheets().add(this.getClass().getResource("StyleSheet.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        textInputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    String txt = textInputField.getText();
                    client.sendMessage(txt);
                    ownMessage(txt);
                    textInputField.setText("");
                }
            }
        });
    }

    public void renderScene() {
        borderPane.setCenter(scrollPane);
        borderPane.setBottom(inputField);
    }

    public void getMessage(String msg) {
        Platform.runLater(() -> {
            if(msg.contains("(Y)")) {
                Image like = new Image (ChatClient.class.getResourceAsStream("like.png"));
                textOutputText.getChildren().add(new ImageView(like));
            } else {
                Label txt = new Label(msg);
                txt.getStyleClass().add("otherUsers");
                textOutputText.getChildren().add(txt);
            }
        });
    }

    public void ownMessage(String msg) {
        Platform.runLater(() -> {
            if(msg.contains("(Y)")) {
                Image like = new Image (ChatClient.class.getResourceAsStream("like.png"));
                textOutputText.getChildren().add(new ImageView(like));
            } else {

                HBox hb = new HBox();
                Pane spacer = new Pane();
                spacer.setStyle("-fx-background-color: rgb(0,0,0);");
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Label txt = new Label(msg);
                txt.getStyleClass().add("user");
                hb.getChildren().addAll(spacer, txt);
                textOutputText.getChildren().add(hb);
            }
        });
    }
}
