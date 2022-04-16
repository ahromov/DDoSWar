import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;
import ua.cc.lajdev.ddoswar.DDOSPattern;
import ua.cc.lajdev.ddoswar.utils.FileParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

@Slf4j
public class MainTest {

    @Test
    public void test() {
        DDOSPattern pattern = getPattern();
        System.out.println(pattern);
        Assert.assertNotNull(pattern);
    }

    @Test
    public void test_parser(){
        List<String> strings = FileParser.parse("conf/task.txt");
        System.out.println(strings);
    }

    private DDOSPattern getPattern() {
        try (Scanner reader = new Scanner(new FileReader("conf/ddos.properties"))) {
            Properties properties = new Properties();
            while (reader.hasNextLine()) {
                String[] trim = reader.nextLine().split("=");
                properties.setProperty(trim[0].trim(), trim[1].trim());
            }

            DDOSPattern ddosPattern = new DDOSPattern();
            ddosPattern.setHost(properties.getProperty("host"));
            ddosPattern.setProtocol(properties.getProperty("protocol"));
            ddosPattern.setPort(Integer.parseInt(properties.getProperty("port")));
            ddosPattern.setThreads(Integer.parseInt(properties.getProperty("threads")));
            ddosPattern.setMessage(properties.getProperty("message"));
            ddosPattern.setHours(Integer.parseInt(properties.getProperty("hours")));
            ddosPattern.setMinutes(Integer.parseInt(properties.getProperty("minutes")));
            ddosPattern.setSeconds(Integer.parseInt(properties.getProperty("seconds")));
            ddosPattern.setTimeout(Integer.parseInt(properties.getProperty("timeout")));
            ddosPattern.setSocketTimeout(Integer.parseInt(properties.getProperty("socketTimeout")));

            return ddosPattern;
        } catch (IOException e) {
            log.error("{}", e.getCause());
        }
        return null;
    }
}
