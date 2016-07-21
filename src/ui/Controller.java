package ui;

import controller.ChatClient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.Request;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    private Main main;
    private static boolean ans;
    private static Button btnYes, btnNo;
    private static Stage window;
    private Request request;
    private ChatClient im;
    @FXML
    private Button btnSend;
    @FXML
    private TextField chatTextInput;
    @FXML
    private TextArea chatWindow;
    @FXML
    private ListView<String> chatMemberBox;

    public Controller() {
        this.btnSend = new Button();
        this.chatTextInput = new TextField();
        this.chatWindow = new TextArea();
        this.chatMemberBox = new ListView<String>();
        this.btnSend.setText("Send");
        this.request = Request.getInstance();
    }

    public void setMain(Main main){
        this.main = main;
    }

    public static boolean confirm(String title, String message){
        window = new Stage();
        btnYes = new Button("Yes");
        btnNo = new Button("No");

        // Force user interaction with dialog box
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMaxWidth(450);
        window.setWidth(250);
        window.setHeight(150);

        Label label = new Label();
        label.setText(message);

        btnYes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ans = true;
                window.close();
            }
        });

        btnNo.setOnAction(event -> {
            ans = false;
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, btnYes, btnNo);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);

        window.showAndWait();

        return ans;
    }

    public void handleBtnSendClick(ActionEvent actionEvent) {
        System.out.println(chatTextInput.getText());
        this.im.sendMessage(chatTextInput.getText());
        chatWindow.appendText(chatTextInput.getText() + "\n");
        chatTextInput.clear();
    }

    private void threadSelector(){
        while (true){
            switch (request.in()){
                case "User Stack":
                    System.out.println("(Controller.java) Indentified the user stack");
                break;

                case "Server":
                    System.out.println("(Controller.java) Indentified a server response");
                break;

                default:
                    System.out.println("We hit the default");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("(Controller.java): Initialized");
    }

}