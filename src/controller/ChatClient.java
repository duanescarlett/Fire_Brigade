package controller;

import network.Request;

public class ChatClient {
    private Request request;

    public ChatClient(){
        this.request = Request.getInstance();
    }

    public void sendMessage(String message){
        this.request.out(message);
    }

    public String displayMessage(){
        return this.request.getServerResponse();
    }
}
