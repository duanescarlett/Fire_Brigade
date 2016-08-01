package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import network.Request;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Duane on 10/07/2016.
 */
public class ControllerLogin implements Initializable {
    public Main main;

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button btnLogin;
    private Request request;
    String username = " ";
    String password = " ";
    String splitter = ":";

    public ControllerLogin(){
        usernameTextField = new TextField();
        passwordTextField = new TextField();
        btnLogin = new Button("Login");
        //btnLogin.setText("Login");
        this.request = Request.getInstance();
    }

    public void setMain(Main main){
        this.main = main;
    }

    @FXML
    public void btnLoginClick(ActionEvent actionEvent){

        this.username = usernameTextField.getText();
        this.password = passwordTextField.getText();
        this.splitter = ":";
        String masterString = username + splitter + password;

        request.out("Login:SELECT username FROM profile WHERE password='"+password+"' AND username='"+username+"'");

        String s = request.getServerResponse();
        System.out.println("(ControllerLogin.java): String response from the server -> " + s);

        boolean trigger = true;
        do{
            if(request.getServerResponse() == "Success"){
                usernameTextField.clear();
                passwordTextField.clear();
                System.out.println("User successfully authenticated");
                trigger = false;
                this.main.mainInterface(); // Go to the interface
            }
        }
        while (trigger);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("(ControllerLogin.java): Initialized");
    }
}
