import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import ua.cc.lajdev.ddoswar.utils.FileParser;

import java.util.List;
import java.util.Map;

@Slf4j
public class MainTest {

    @Test
    public void test_parser(){
        Map<String, List<Integer>> strings = FileParser.parseTask("conf/task.txt");
        System.out.println(strings);
    }
}
