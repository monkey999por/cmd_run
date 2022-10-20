import monkey999.tools.Cmd;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.security.URIParameter;

public class CmdTest {

    private String getBatch(String fileName){
        var loader = getClass().getClassLoader();
        var path =  new File(loader.getResource(fileName).getPath());

        System.out.println(path.getAbsolutePath());
        return path.getAbsolutePath();
    }

    @Test
    void test_sync () throws Exception {
        System.out.println("start sync");
        var result = Cmd.execute(false, new String[]{getBatch("batch_sync.bat")});
        System.out.println();
        System.out.format("バッチ実行結果：%s\r\n", result);
        Thread.sleep(4000L);
        System.out.format("バッチ実行結果：%s\r\n", result);
    }

    @Test
    void test_async() throws Exception {
        System.out.println("start async");
        var result = Cmd.execute(true, new String[]{getBatch("batch_async.bat")});

        System.out.println();
        System.out.format("バッチ実行結果：%s\r\n", result);
        System.out.format("バッチ実行結果：%s\r\n", result);
    }

    @Test
    void test_a() throws Exception {
        String outer = "this is local variable";
        Cmd.execute(new String[]{getBatch("batch_async.bat")}, r -> {
            System.out.println(r + "OK, callback !!" + outer);
        });
        System.out.println("main thread done");
        Thread.sleep(5000L);
    }
}
