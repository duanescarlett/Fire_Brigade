package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
    private PasswordField passwordTextField;
    @FXML
    private Button btnLogin;
    @FXML
    private ImageView imgViewLogin;
    private Request request;
    String username;
    String password;
    User user;

    public ControllerLogin(){
        imgViewLogin = new ImageView();
        usernameTextField = new TextField();
        passwordTextField = new PasswordField();
        btnLogin = new Button("Login");
//        this.request = Request.getInstance();
        this.request = Request.getInstance();
        this.user = User.getInstance();
    }

    public void setMain(Main main){
        this.main = main;
    }

    @FXML
    public void btnLoginClick(ActionEvent actionEvent){

        this.username = usernameTextField.getText();
        this.password = passwordTextField.getText();

        request.out("Login:SELECT * FROM profile WHERE password='"+password+"' AND username='"+username+"'");

        this.listenToServer();
        this.usernameTextField.clear();
        this.passwordTextField.clear();
        this.main.mainInterface();

    }

    private void listenToServer(){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean state = true;
                String test;

                int count = 0;
                do{

                    test = request.getServerResponse();

                    if(test.equals("null")){
                        //continue;
                    }
                    else {
                        String bool = "yes";
                        System.out.println("(ControllerLogin.java): String response from the server -> " + request.getServerResponse());
                        state = false;
                        user.setUsername(request.getServerResponse());
                    }

                    //System.out.println("(ControllerLogin.java): looping -> " + count++);

                }while (state);

            }

        });

        t.setName("Listener for auth");
        t.start();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("(ControllerLogin.java): Initialized");
    }

}
