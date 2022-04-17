package ua.cc.lajdev.ddoswar;

import ua.cc.lajdev.ddoswar.protocols.implementations.TCPDDos;
import ua.cc.lajdev.ddoswar.protocols.implementations.UDPDDos;
import ua.cc.lajdev.ddoswar.protocols.interfaces.DDOS;

/**
 * This is just a simple factory class for creating
 * the corresponding DDOS subclass.
 *
 */
public class DdosFactory {

    /**
     * Factory method which is responsible for
     * creating the corresponding DDOS subclass.
     *
     * @param ddosPattern
     * @return a subclass of DDOS
     */
    public static DDOS createDDOS(DDOSPattern ddosPattern, DDOSer ddoSer) {
        switch (ddosPattern.getProtocol().toLowerCase()) {
            case "tcp":
                return new TCPDDos(ddosPattern, ddoSer);
            case "udp":
                return new UDPDDos(ddosPattern, ddoSer);
        }
        return new TCPDDos(ddosPattern, ddoSer);
    }
}
