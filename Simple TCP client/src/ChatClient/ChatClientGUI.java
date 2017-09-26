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

    private VBox textOutputField;
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
        textOutputField = new VBox();

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHmax(400);
        scrollPane.setVmax(400);

        textOutputField.setSpacing(5);
        textOutputField.setPrefWidth(370);
        textOutputField.setMaxWidth(370);

        Button likeBtn = new Button();
        Image like = new Image (ChatClient.class.getResourceAsStream("like.png"));
        likeBtn.setGraphic(new ImageView(like));

        likeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.sendMessage("(Y)");
                ownMessage("(Y)");
            }
        });

        Button dislikeBtn = new Button();
        Image dislike = new Image (ChatClient.class.getResourceAsStream("dislike.png"));
        dislikeBtn.setGraphic(new ImageView(dislike));

        dislikeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.sendMessage("(N)");
                ownMessage("(N)");
            }
        });

        likeBtn.setMaxWidth(Double.MAX_VALUE);
        dislikeBtn.setMaxWidth(Double.MAX_VALUE);

        emojis.setHgrow(likeBtn, Priority.ALWAYS);
        emojis.setHgrow(dislikeBtn, Priority.ALWAYS);

        emojis.getChildren().addAll(likeBtn, dislikeBtn);
        inputField.getChildren().addAll(textInputField, emojis);

        renderScene();

        scrollPane.setContent(textOutputField);

        Scene scene = new Scene(borderPane, 400, 400);
        scene.getStylesheets().add(this.getClass().getResource("StyleSheet.css").toExternalForm());
        scrollPane.setId("scrollPane");

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

        newLogin();

    }

    public void newLogin() {
        Stage loginStage = new Stage();
        loginStage.setTitle("Login");
        BorderPane bp = new BorderPane();
        TextField tf = new TextField();
        Text t = new Text(10, 50, "Enter username:");
        t.setFont(new Font(13));

        Button bt = new Button("Submit");
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String txt = tf.getText();
                client.sendMessage(txt);
                client.sendMessage(txt + " has just logged in");
                ownMessage("Good day " + txt);
                loginStage.close();
            }
        });

        tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    bt.fire();
                }
            }
        });

        Region rLeft = new Region();
        Region rTop = new Region();
        rLeft.setPrefWidth(30);
        rTop.setPrefHeight(35);
        HBox hb = new HBox();
        hb.getChildren().addAll(tf, bt);
        bp.setLeft(rLeft);
        bp.setTop(rTop);
        bp.setCenter(hb);
        loginStage.setScene(new Scene(bp, 250, 100));
        loginStage.setResizable(false);
        loginStage.show();
    }

    public void renderScene() {
        borderPane.setCenter(scrollPane);
        borderPane.setBottom(inputField);
    }

    public void getMessage(String username, String msg) {
        scrollPane.vvalueProperty().bind(textOutputField.heightProperty());
        Platform.runLater(() -> {
            if(msg.contains("(Y)")) {
                Image like = new Image (ChatClient.class.getResourceAsStream("like.png"));
                textOutputField.getChildren().add(new ImageView(like));
            } else if (msg.contains("(N)")) {
                Image dislike = new Image (ChatClient.class.getResourceAsStream("dislike.png"));
                textOutputField.getChildren().add(new ImageView(dislike));
            } else if (msg.contains(":lul:")) {
                Image lul = new Image (ChatClient.class.getResourceAsStream("lul.png"));
                textOutputField.getChildren().add(new ImageView(lul));
            } else {
                if(msg.length() > 12) {
                    System.out.println("Message is too long");
                }
                Label txt = new Label(msg);
                txt.getStyleClass().add("otherUsers");
                Label userName = new Label(username);
                userName.getStyleClass().add("smallUserName");
                VBox chatMessage = new VBox();
                chatMessage.getChildren().addAll(userName, txt);
                textOutputField.getChildren().add(chatMessage);
            }
        });
    }

    public void ownMessage(String msg) {
        scrollPane.vvalueProperty().bind(textOutputField.heightProperty());
        Platform.runLater(() -> {

            if(msg.length() > 12) {
                System.out.println("Message is too long");
            }

            if(msg.contains("(Y)")) {
                HBox hb = new HBox();
                Pane spacer = new Pane();
                //spacer.setStyle("-fx-background-color: rgb(0,0,0);");
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Image like = new Image (ChatClient.class.getResourceAsStream("like.png"));
                hb.getChildren().addAll(spacer, new ImageView(like));
                textOutputField.getChildren().add(hb);
            } else if(msg.contains("(N)")) {
                HBox hb = new HBox();
                Pane spacer = new Pane();
                //spacer.setStyle("-fx-background-color: rgb(0,0,0);");
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Image like = new Image (ChatClient.class.getResourceAsStream("dislike.png"));
                hb.getChildren().addAll(spacer, new ImageView(like));
                textOutputField.getChildren().add(hb);
            } else if (msg.contains(":lul:")) {
                Image lul = new Image (ChatClient.class.getResourceAsStream("lul.png"));
                textOutputField.getChildren().add(new ImageView(lul));
            } else {

                HBox hb = new HBox();
                Pane spacer = new Pane();
                //spacer.setStyle("-fx-background-color: rgb(0,0,0);");
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Label txt = new Label(msg);
                txt.getStyleClass().add("user");
                hb.getChildren().addAll(spacer, txt);
                textOutputField.getChildren().add(hb);
            }
        });
    }
}
