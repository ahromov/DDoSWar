package ua.cc.lajdev.ddoswar.protocols.interfaces;

import ua.cc.lajdev.ddoswar.DDOSPattern;
import ua.cc.lajdev.ddoswar.DDOSer;

import java.net.SocketAddress;

/**
 * Abstract super class of all different protocols.
 * Every protocol implementation must inherit from this abstract class.
 *
 * @author Tobias Schmidradler
 * <p>
 * Last changed: 13.05.2015
 */
public abstract class DDOS implements Runnable {
    private DDOSPattern ddosPattern;
    private SocketAddress address;
    protected DDOSer ddoSer;

    public DDOS() {

    }

    public DDOS(DDOSPattern ddosPattern, DDOSer ddoSer) {
        this.ddosPattern = ddosPattern;
        this.ddoSer = ddoSer;
    }

    /**
     * This method writes something (Protocol dependent) to the socket.
     *
     * @param message
     */
    public abstract void writeLineToSocket(String message);

    /**
     * Creates the socket. (Protocol dependent)
     */
    protected abstract void createSocket();

    /**
     * Connect to the socket. (Protocol dependent)
     */
    protected abstract void connectToSocket();

    /**
     * Close the socket (Protocol dependent)
     */
    protected abstract void closeSocket();

    /**
     * Get the DDOS pattern.
     *
     * @return the pattern of the current DDOS
     */
    public DDOSPattern getDdosPattern() {
        return ddosPattern;
    }

    /**
     * Set the DDOS pattern.
     *
     * @param ddosPattern pattern of the current DDOS
     */
    public void setDdosPattern(DDOSPattern ddosPattern) {
        this.ddosPattern = ddosPattern;
    }

    /**
     * Get the address of the victim
     *
     * @return
     */
    public SocketAddress getAddress() {
        return address;
    }

    /**
     * Set the address of the victim
     *
     * @param address
     */
    public void setAddress(SocketAddress address) {
        this.address = address;
    }
}
