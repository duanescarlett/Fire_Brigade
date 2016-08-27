package ui;

import controller.ChatClient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.Request;
import network.SocketHolder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    private Main main;
    private static boolean ans;
    private static Button btnYes, btnNo;
    private static Stage window;
    private Request request;
    private User user;
    private ChatClient im;
    private File file;
    private ByteBuffer buffer;
    private SocketHolder socketHolder;
    private ObservableList<String> items;
    private ObservableList<String> noteItems;
    private String chatListener;

    @FXML
    private Button btnSend;
    @FXML
    private TextField chatTextInput;
    @FXML
    public TextArea chatWindow;
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
    @FXML
    private MenuButton menuBtn;
    @FXML
    private MenuItem NotificationMenuBtn;
    @FXML
    private Button btnMap;
    @FXML
    private WebView webView;
    private WebEngine webEngine;
    TextArea textArea = new TextArea();
    Label lblTopic = new Label("Topic");
    Label lblNotification = new Label("Notification");
    TextField textField = new TextField();
    Button noteSubmit = new Button("Create Notification");


    public Controller() {

        this.NotificationMenuBtn = new MenuItem();
        this.menuBtn = new MenuButton();

        this.buffer = ByteBuffer.allocateDirect(1024);
        this.socketHolder = SocketHolder.getInstance();

        this.toolBar = new ToolBar();
        this.btnSend = new Button();
        this.btnMap = new Button();

        this.chatTextInput = new TextField();
        this.chatWindow = new TextArea();
        this.chatMemberBox = new ListView<String>();
        this.chatMemberBox.setPrefSize(200, 250);
        this.chatMemberBox.setEditable(true);
        this.chatListener = "";

        this.items = FXCollections.observableArrayList();
        this.noteItems = FXCollections.observableArrayList();

        this.notificationList = new ListView<String>();
        this.notificationList.setPrefSize(200, 250);
        this.notificationList.setEditable(true);
        this.notificationList.setItems(noteItems);

        this.chatMemberBox.setItems(items);
        this.lblUsername = new Label();
        this.imgLogo = new ImageView();
        this.request = Request.getInstance();
        this.user = User.getInstance();

        this.im = new ChatClient();

        this.in();
    }

    private void parser(String s){

        String[] stringPeices = s.split(":", 2);

        System.out.println("(Controller.java): -> Inside parser 2 " + s);

        if(stringPeices[0].equals("Test")){

        }
        else if(stringPeices[0].equals("Chat")){
            this.chatWindow.appendText("Server -> " + stringPeices[1].trim());
        }
        else if(stringPeices[0].equals("Username")){
            System.out.println("(Controller.java): -> username " + stringPeices[1]);
            user.setUsername(stringPeices[1].trim());
            //this.lblUsername.setText(user.getUsername());
            this.lblUsername.setText(this.user.getUsername());
        }
        else if(stringPeices[0].equals("UserList")){
            this.items.add(stringPeices[1]);
        }
        else if(stringPeices[0].equals("Notification")){
            this.noteItems.add(stringPeices[1]);
        }
//        else if(stringPeices[0].equals("Messages")){
//            this.chatWindow.appendText(chatListener + "-> " +stringPeices[1]);
//        }

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

    public void mapBtnClick(ActionEvent actionEvent) {

    }

    public void handleBtnSendClick(ActionEvent actionEvent) {
        //System.out.println(chatTextInput.getText());
        this.request.out("Chat:" + chatListener + ":" + chatTextInput.getText());
        chatWindow.appendText(this.user.getUsername() + "-> " + chatTextInput.getText() + "\n");
        chatTextInput.clear();
    }

    public void notificationBtnClick(ActionEvent actionEvent){

        window = new Stage();

        // Force user interaction with dialog box
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create a Notification");
        window.setMaxWidth(750);
        window.setWidth(550);
        window.setHeight(450);

        noteSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ans = true;
                String topic = textField.getText();
                String content = textArea.getText();
                String sqlStatement = "Notification:INSERT INTO fire_brigade.notification (content, category) VALUES('"+content+"', '"+topic+"')";
                request.out(sqlStatement);
                window.close();
            }
        });

        VBox layout = new VBox(10);
        textField.setMaxWidth(350);
        textArea.setMaxWidth(350);

        lblTopic.setAlignment(Pos.CENTER_LEFT);
        lblNotification.setAlignment(Pos.CENTER_LEFT);

        layout.getChildren().addAll(lblTopic, textField, lblNotification, textArea, noteSubmit);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);

        window.showAndWait();
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

        if(fileObj != null){
            this.request.sendFile(fileObj);
            chatWindow.appendText("\n\nFile Sent: -> " + fileObj.getName().toString());
        }

    }

    private void in(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                int i = -1;

                while(true){

                    try {
                        int amount_read = -1;

                        try {
                            amount_read = socketHolder.socketChannel.read(buffer);
                            //buffer.clear();
                        } catch (Throwable t) { }

                        if(amount_read != -1){
                            System.out.println("sending back " + buffer.position() + " bytes");

                            // turn this bus right around and send it back!
                            buffer.flip();
                            byte[] buff = new byte[1024];
                            buffer.rewind();
                            //buffer.flip();
                            buffer.get(buff, 0, amount_read);
                            //System.out.println("Server said: " + new String(buff));

                            parser(new String(buff));
                            buffer.compact();
                        }

                        if (amount_read == -1)
                            //disconnect();

                            if (amount_read < 1)
                                return; // if zero


                        //socketHolder.socketChannel.write(buffer);
                    }
                    catch (Throwable t) {
                        //disconnect();
                        t.printStackTrace();
                    }

                }

            }

        });

        t.setName("Server Listener");
        t.start();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("(Controller.java): Initialized");
        this.chatMemberBox.setItems(items);
        this.chatMemberBox.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.chatMemberBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                System.out.println("Selected item: ");
                System.out.println("Selected item: " + newValue);
                chatListener = newValue;
                //request.out("Messages:" + chatListener);
            }
        });
        this.notificationList.setItems(noteItems);
        this.notificationList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

}