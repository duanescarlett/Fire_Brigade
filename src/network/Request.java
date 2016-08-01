package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Request {

    private Socket client;
    private OutputStream oOutput;
    private InputStream oInput;
    public String inComingFromServer = "";

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    ByteBuffer buffer;

    private class Connections {
        private String serverAddr = "127.0.0.1";
        private int port = 6066;
    }

    SelectionKey selectionKey;
    SocketChannel socketChannel;
    ByteBuffer byteBuffer;

    Connections con;

    private static class SingletonHolder {
        private static final Request INSTANCE = new Request();
    }

    public static Request getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private Request() {
        try {
            this.con = new Connections();
            this.client = new Socket(this.con.serverAddr, this.con.port);
            this.oOutput = client.getOutputStream();
            this.oInput = client.getInputStream();
            this.buffer = ByteBuffer.allocateDirect(100);
            this.in();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void out(String s){

        System.out.println("(Request.java): -> " + s);
        byte[] set = s.getBytes();

        try {
            oOutput.write(set);
            oOutput.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void disconnect(){
        if(selectionKey != null)
            selectionKey.cancel();

        if(socketChannel == null)
            return;

        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void read(){
        try {

        }catch (Throwable t){
            disconnect();
            t.printStackTrace();
        }
    }

    private void in(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                //byteBuffer = ByteBuffer.wrap(oInput);
                byte[] array1 = new byte[buffer.limit()];

                try {
                    oInput.read(array1);
                    String mes = new String (array1);
                    System.out.println("(Request.java): This is what we got from the server-> " + mes);
                    inComingFromServer = mes;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int read = -1;
                char[] s;

                try {
                    while ((read = oInput.read()) != -1){
                        System.out.println(read);
                        oInput.read();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        t.setName("Server Listener");
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

}