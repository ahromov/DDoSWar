package ua.cc.lajdev.ddoswar.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FileParser {

    public static List<String> parse(String fileName) {
        List<String> hostNames = new ArrayList<>();
        try (Scanner reader = new Scanner(new FileReader(fileName))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.startsWith("http")) {
                    line = line.replace("https://", "");
                    StringTokenizer stringTokenizer = new StringTokenizer(line);
                    line = stringTokenizer.nextToken("/");
                    hostNames.add(line);
                } else {
                    String[] split = line.split(" ");
                    hostNames.add(split[0]);
                }
            }
        } catch (IOException e) {
            e.getCause();
        }
        return hostNames;
    }
}
