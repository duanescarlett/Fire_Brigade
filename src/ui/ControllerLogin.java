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
    @FXML private Button btnLogin;
    private Request request;

    public ControllerLogin(){
        usernameTextField = new TextField();
        passwordTextField = new TextField();
        btnLogin = new Button();
        btnLogin.setText("Login");
        this.request = Request.getInstance();
    }

    public void setMain(Main main){
        this.main = main;
    }

    @FXML
    public void btnLoginClick(ActionEvent actionEvent){
        boolean ans = false;
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        request.out("Login:SELECT username FROM profile WHERE password='"+password+"' AND username='"+username+"'");

        usernameTextField.clear();
        passwordTextField.clear();
        this.main.mainInterface(); // Go to the interface

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("(ControllerLogin.java): Initialized");
    }
}
