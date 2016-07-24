package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Duane on 04/07/2016.
 */
public class Request {

    private String serverName = "127.0.0.1";
    private Socket client;
    private ObjectOutputStream oOutput;
    private ObjectInputStream oInput;
    private String inComingFromServer = "";

    private static class SingletonHolder {
        private static final Request INSTANCE = new Request();
    }

    public static Request getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public Request() {
        try {
            System.out.println("(Request.java): Connecting to " + serverName + " on port " + 6066);
            this.client = new Socket(serverName, 6066);
            this.oOutput = new ObjectOutputStream(client.getOutputStream());
            this.oInput = new ObjectInputStream(client.getInputStream());
            this.in();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void out(String s){

        try {
            this.oOutput.writeObject(s);
            this.oOutput.flush();
        }catch (IOException e) {
            e.printStackTrace();

        }

    }

    private void in(){
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true){
                    try {
                        inComingFromServer = oInput.readObject().toString().trim();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        t.start();
    }

    public String getServerResponse(){
        return inComingFromServer;
    }

    public String ip(){
        try {
            return InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "null";
    }

    public Boolean bool(){
        try {
            return this.oInput.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
