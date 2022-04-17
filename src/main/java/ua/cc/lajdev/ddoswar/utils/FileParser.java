package ua.cc.lajdev.ddoswar.utils;

import ua.cc.lajdev.ddoswar.DDOSer;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class FileParser {

    public static Properties parseProperties() {
        Properties properties = new Properties();
        try (Scanner reader = new Scanner(new FileReader("conf/ddos.properties"))) {
            while (reader.hasNextLine()) {
                String[] trim = reader.nextLine().split("=");
                properties.setProperty(trim[0].trim(), trim[1].trim());
            }
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
        return properties;
    }

    public static Map<String, List<Integer>> parseTask(String fileName) {
        Map<String, List<Integer>> hostNames = new HashMap<>();
        try (Scanner reader = new Scanner(new FileReader(fileName))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (!line.startsWith("http")) {
                    String[] split = line.split(" ");
                    List<Integer> ports = parsePorts(split);
                    hostNames.put(split[0], ports);
                }
            }
        } catch (IOException e) {
            DDOSer.appendToConsole(e.getMessage());
        }
        return hostNames;
    }

    private static List<Integer> parsePorts(String[] input) {
//        (80/tcp, 443/tcp, 8443/tcp, 8443/https)
        List<Integer> portsNumbers = new ArrayList<>();
        for (int i = 1; i < input.length; i++) {
            input[i] = extracted(input, i);
            StringTokenizer tokenizer = new StringTokenizer(input[i].trim());
            input[i] = tokenizer.nextToken("/");
            portsNumbers.add(Integer.parseInt(input[i]));
        }
        return portsNumbers;
    }

    private static String extracted(String[] input, int i) {
        return input[i]
                .replaceAll("\\)", "")
                .replaceAll("\\(", "")
                .replaceAll(",", "");
    }
}
