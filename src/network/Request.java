package network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class Request {

    private String inComingFromServer = "null";

    // Selector: multiplexor of SelectableChannel objects
    private Selector selector;
    private SocketHolder socketHolder;
    private ServerSocketChannel serverSocketChannel;
    ByteBuffer buffer;



    private static class SingletonHolder {
        private static final Request INSTANCE = new Request();
    }

    public static Request getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private Request() {

        try {
            socketHolder = SocketHolder.getInstance();
            // Selector: multiplexor of SelectableChannel objects
            this.selector = Selector.open(); // selector is open here
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        this.buffer = ByteBuffer.allocateDirect(1024);

    }

    public void sendFile(File f) throws IOException {
        FileChannel fc = new FileInputStream(f).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(fc.read(buffer) > 0) {
            buffer.flip();
            while(socketHolder.socketChannel.write(buffer) > 0);
            buffer.clear();
        }
    }

    public void out(String s){

        System.out.println("(Request.java): -> going out " + s);

        this.buffer = ByteBuffer.wrap(s.getBytes());

        try {
            socketHolder.socketChannel.write(this.buffer);
            this.buffer.clear();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //this.buffer.flip();
        //this.buffer.clear();

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