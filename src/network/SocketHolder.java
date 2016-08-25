package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by root on 8/24/16.
 */
public class SocketHolder {
    public SocketChannel socketChannel;

    public static SocketHolder getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final SocketHolder INSTANCE = new SocketHolder();
    }

    private SocketHolder(){
        this.con = new Connections();

        try {
            this.socketChannel = SocketChannel.open();
            this.socketChannel.connect(this.con.getHostAddress());

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Connections {
        private InetSocketAddress hostAddress = new InetSocketAddress("127.0.0.1", 6066);

        public InetSocketAddress getHostAddress(){
            return hostAddress;
        }
    }

    Connections con;
}
