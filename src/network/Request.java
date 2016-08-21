package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class Request {

    private String inComingFromServer = "null";

    private SocketChannel socketChannel;
    ByteBuffer buffer;

    private class Connections {
        private InetSocketAddress hostAddress = new InetSocketAddress("127.0.0.1", 6066);

        public InetSocketAddress getHostAddress(){
            return hostAddress;
        }
    }

    Connections con;

    private static class SingletonHolder {
        private static final Request INSTANCE = new Request();
    }

    public static Request getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private Request() {
        this.con = new Connections();

        try {
            this.socketChannel = SocketChannel.open(this.con.getHostAddress());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        this.buffer = ByteBuffer.allocateDirect(1024);
        this.in();

    }

    public void out(String s){

        System.out.println("(Request.java): -> " + s);

        this.buffer = ByteBuffer.wrap(s.getBytes());

        try {
            this.socketChannel.write(this.buffer);
            this.buffer.clear();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.buffer.flip();
        //this.buffer.clear();

    }

    private synchronized void in(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    socketChannel.read(buffer);
                    buffer.flip();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                inComingFromServer = new String(buffer.array()).trim();
                System.out.println("(Request.java):  -> " + inComingFromServer);
                //buffer.clear();
            }

        });

        t.setName("Server Listener");
        t.start();

    }

    public synchronized String getServerResponse(){
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