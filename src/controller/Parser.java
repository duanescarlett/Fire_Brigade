package controller;

import ui.Controller;

/**
 * Created by root on 8/25/16.
 */
public class Parser {

    public void parse(String s){
        System.out.println("(Controller.java): -> Inside parser");

        String[] stringPeices = s.split(":", 2);

        System.out.println("(Controller.java): -> Inside parser 2 " + s);

        if(stringPeices[0].equals("Test")){

        }
        else if(stringPeices[0].equals("Chat")){
            chatWindow.appendText(stringPeices[1].trim());
        }
        else if(stringPeices[0].equals("Username")){
            System.out.println("(Controller.java): -> username " + stringPeices[1]);
            user.setUsername(stringPeices[1].trim());
            this.lblUsername.setText(user.getUsername());
        }
        else {
            System.out.println("(Controller.java): -> This did not work");
            System.out.println("(Controller.java): -> " + stringPeices[0]);
        }

    }
}
