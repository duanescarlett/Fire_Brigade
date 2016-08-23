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
    private SocketChannel socketChannel;
    private ServerSocketChannel serverSocketChannel;
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
            this.socketChannel = SocketChannel.open();
            this.socketChannel.connect(this.con.getHostAddress());

            // Selector: multiplexor of SelectableChannel objects
            this.selector = Selector.open(); // selector is open here
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        this.buffer = ByteBuffer.allocateDirect(1024);
        this.in();

    }

    public void sendFile(File f) throws IOException {
        FileChannel fc = new FileInputStream(f).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(fc.read(buffer) > 0) {
            buffer.flip();
            while(this.socketChannel.write(buffer) > 0);
            buffer.clear();
        }
    }

    public void out(String s){

        System.out.println("(Request.java): -> going out " + s);

        this.buffer = ByteBuffer.wrap(s.getBytes());

        try {
            this.socketChannel.write(this.buffer);
            this.buffer.clear();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //this.buffer.flip();
        //this.buffer.clear();

    }

    private void in(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

//                try {
//                    socketChannel.read(buffer);
//                    buffer.flip();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                inComingFromServer = new String(buffer.array()).trim();
//                System.out.println("(Request.java):  -> " + inComingFromServer);
//                buffer.clear();

                ByteBuffer miniBuf = ByteBuffer.allocate(1024);
                StringBuilder sb = new StringBuilder();
                int read = 0;

                try {

                    while(true){
                        System.out.println("(Request.java): -> Inside first listener loop");

//                        while ((read = socketChannel.read(miniBuf)) > 0){
//                            System.out.println("(Request.java): -> Inside second listener loop");
//                            miniBuf.flip();
//                            byte[] bytes = new byte[miniBuf.limit()];
//                            miniBuf.get(bytes);
//                            sb.append(new String(bytes));
//                            miniBuf.clear();
//                        }

                        while (socketChannel.isConnected()){
                            read = socketChannel.read(miniBuf);
                            System.out.println("(Request.java): -> Inside second listener loop");
                            miniBuf.flip();
                            byte[] bytes = new byte[miniBuf.limit()];
                            miniBuf.get(bytes);
                            sb.append(new String(bytes));
                            miniBuf.clear();
                        }

                        String msg;
                        if(read<0) {
                            msg = "left the chat.\n";
                            socketChannel.close();
                        }
                        else {
//                msg = key.attachment()+": "+sb.toString();
                            msg = sb.toString();
                        }

                        miniBuf.clear();
                        System.out.println("(Request.java): -> in coming " + msg);
                        inComingFromServer = msg;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

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