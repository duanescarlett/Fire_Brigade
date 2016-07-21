package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Duane on 04/07/2016.
 */
public class Request {

    String serverName = "127.0.0.1";
    Socket client;
    ObjectOutputStream oOutput;
    ObjectInputStream oInput;

    private static class SingletonHolder {
        private static final Request INSTANCE = new Request();
        //INSTANCE.start();
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

    public String in(){
        String inComingFromServer = "";

        try {
            inComingFromServer = oInput.readObject().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
