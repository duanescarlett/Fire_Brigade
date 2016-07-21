package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

import static ui.Controller.confirm;


public class Main extends Application{

    private static FXMLLoader root;
    private static Stage window;
    private static Parent pane;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.TheFXMLLoader("loginSignUp.fxml");

        ControllerLoginSignup con = root.getController();
        con.setMain(this);

        this.window = primaryStage;
        this.window.setTitle("Fire Brigade");
        this.window.setOnCloseRequest(e -> {
            e.consume();
            this.closeProgram();
        });
        this.window.setScene(new Scene(pane));
        this.window.show();
    }



    protected void signUp() {
        this.TheFXMLLoader("signUp.fxml");

        ControllerSignUp con = root.getController();
        con.setMain(this);

        this.window.setScene(new Scene(pane));
        this.window.show();
    }

    private void TheFXMLLoader(String s){
        this.root = new FXMLLoader(Main.class.getResource(s));
        try {
            pane = root.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void login() {
        this.TheFXMLLoader("login.fxml");

        ControllerLogin con = root.getController();
        con.setMain(this);

        this.window.setScene(new Scene(pane));
        this.window.show();
    }

    protected void mainInterface() {
        this.TheFXMLLoader("layout.fxml");

        Controller con = root.getController();
        con.setMain(this);

        this.window.setScene(new Scene(pane));
        this.window.show();
    }

    private boolean closeProgram(){
        Boolean answer = confirm("Title", "Are you sure you want to quit");
        if(answer) {
            System.out.println("Window just closed");
            window.close();
            return true;
        }
        else
            System.out.println("close Operation was Cancelled");
            return false;
    }

    public static void main(String[] args) {
        launch(args);
    }

}