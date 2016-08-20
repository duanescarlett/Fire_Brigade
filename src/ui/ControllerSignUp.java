package ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import network.Request;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ControllerSignUp implements Initializable{
    Main main;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnButton;
    @FXML
    private RadioButton radioMale;
    @FXML
    private RadioButton radioFemale;
    private Request request;
    String sqlStatement;

    public ControllerSignUp() {
        this.btnButton = new Button("Sign Up");
        this.txtFirstName = new TextField();
        this.txtLastName = new TextField();
        this.txtEmail = new TextField();
        this.txtUsername = new TextField();
        this.txtPassword = new TextField();
        this.radioMale = new RadioButton();
        this.radioFemale = new RadioButton();
        this.request = Request.getInstance();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void handleBtnSendClick() {

        // Get value from radio button
        String gender;
        if(this.radioMale.isSelected()){
            gender = this.radioMale.getText();
        }
        else if(this.radioFemale.isSelected()){
            gender = this.radioFemale.getText();
        }
        else{
            gender = "";
        }

        // Get the date and time
        Date dateTime = new Date();
        String email = txtEmail.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String ip = this.request.ip();
        String timeStamp = dateTime.toString();
        String name = txtFirstName.getText() + " " + txtLastName.getText();

        sqlStatement = "Sign Up:INSERT INTO fire_brigade.profile (username, password, email, gender, datetime, ip_address, name, online) VALUES('"+username+"', '"+password+"', '"+email+"', '"+gender+"', '"+timeStamp+"', '"+ip+"', '"+name+"', 'yes')";

        // Clear the field after send button clicked
        txtFirstName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtUsername.clear();
        txtPassword.clear();

        this.request.out(sqlStatement); // Send off to the server
        this.main.login();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("(ControllerSignUp.java): Initialized");
    }

}