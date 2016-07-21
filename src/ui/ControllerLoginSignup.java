package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Duane on 10/07/2016.
 */
public class ControllerLoginSignup implements Initializable {
    private Main main;
    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnLogin;

    public void setMain(Main main){
        this.main = main;
    }

    public ControllerLoginSignup(){
        this.btnSignUp = new Button();
        this.btnLogin = new Button();
    }

    public void signUpClick(ActionEvent actionEvent) {
        main.signUp();
    }

    public void loginClick(ActionEvent actionEvent) {
        main.login();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("(ControllerLoginSignup.java): Initialized");
    }
}
