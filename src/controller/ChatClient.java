package controller;

/**
 * Created by Duane on 08/06/2016.
 */

import network.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatClient {
    Request request;

    public ChatClient(){
        this.request = Request.getInstance();
    }

    public void sendMessage(String message){
        this.request.out(message);
    }

    private void displayMessage(){
        System.out.println(this.request.in());
    }
}
