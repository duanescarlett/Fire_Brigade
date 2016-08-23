package ui;

import controller.ChatClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.Request;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class Controller implements Initializable{
    private Main main;
    private static boolean ans;
    private static Button btnYes, btnNo;
    private static Stage window;
    private Request request;
    private User user;
    private ChatClient im;
    private File file;
    private String username;
    //private DomParser dom;
    @FXML
    private Button btnSend;
    @FXML
    private TextField chatTextInput;
    @FXML
    private TextArea chatWindow;
    @FXML
    private ListView<String> chatMemberBox;
    @FXML
    private ListView<String> notificationList;
    @FXML
    private Label lblUsername;
    @FXML
    public ImageView imgLogo;
    @FXML
    public ToolBar toolBar;



    public Controller() {
        this.toolBar = new ToolBar();
        this.btnSend = new Button();
        this.chatTextInput = new TextField();
        this.chatWindow = new TextArea();
        this.chatMemberBox = new ListView<String>();
        this.chatMemberBox.setPrefSize(200, 250);
        this.chatMemberBox.setEditable(true);
        ObservableList<String> items =FXCollections.observableArrayList ( "A", "B", "C", "D");

        this.notificationList = new ListView<String>();
        this.notificationList.setItems(items);

        this.chatMemberBox.setItems(items);
        this.lblUsername = new Label();
        this.imgLogo = new ImageView();
        this.request = Request.getInstance();
        this.user = User.getInstance();

        this.im = new ChatClient();

        this.listenToServer();
    }

    private void parser(String s){
        String[] stringPeices = s.split(":", 2);

        if(stringPeices[0].equals("Test")){

        }
        else if(stringPeices[0].equals("Chat")){
            this.chatWindow.appendText(stringPeices[1].trim());
        }
        else if(stringPeices[0].equals("Username")){
            System.out.println("(Controller.java): -> username " + stringPeices[1]);
            user.setUsername(stringPeices[1].trim());
            this.lblUsername.setText(user.getUsername());
        }
        else {
            //System.out.println("(Controller.java): -> This did not work");
            //System.out.println("(Controller.java): -> " + stringPeices[0]);
        }

    }

    private void listenToServer(){

        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                boolean state = true;
                String test;

                while (state){

                    test = request.getServerResponse();

                    if(test.equals("null")){
                        //continue;
                    }
                    else {
                        //System.out.println("(Controller.java): String response from the server -> " + request.getServerResponse());
                        //System.out.println("(Controller.java): looping -> " + count++);
                        parser(request.getServerResponse().trim());
                        //state = false;
//                        user.setUsername(request.getServerResponse());
                    }

                }

            }
        });

        t.setName("Listener for auth main view");
        t.start();

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
//        this.im.sendMessage();
        this.request.out("Chat:" + chatTextInput.getText());
        chatWindow.appendText("Me: -> " + chatTextInput.getText() + "\n");
        chatTextInput.clear();
        //this.listen();
    }

    public void attachBtnClick(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Stage stage = new Stage();

        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        this.file = fileChooser.showOpenDialog(stage);
        System.out.println(file);

        File fileObj = new File(this.file.toString());
        this.request.sendFile(fileObj);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("(Controller.java): Initialized");
        //System.out.println("(Controller.java): This is the username -> " + this.request.getServerResponse());
        //this.lblUsername.setText(this.request.getServerResponse());
        //this.lblUsername.setText("Work");
    }

}