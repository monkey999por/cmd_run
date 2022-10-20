package monkey999.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * command prompt wrapper.
 */
public class Cmd {

    /**
     * @param async   - async/await
     * @param command command list. running by {@link ProcessBuilder}.
     * @return running result by command output from standard out.
     * <p>
     * usage
     * Cmd.execute(false, new String[]{"curl", "-L", "-s", "http://sample.com"});
     */
    @Deprecated
    public static String execute(boolean async, String[] command) throws Exception {
        var process = new ProcessBuilder(command);
        Process p;
        process.redirectErrorStream(true);

        p = process.start();
        if (async) {
            return "";
        }
        p.waitFor();

        var reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        var builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        p.destroy();
        return builder.toString();
    }

    /**
     * @param command  command list. running by {@link ProcessBuilder}.
     * @param callback
     * @return
     * @throws Exception <br>
     *                   run with async.<br>
     *                   <p>
     *                   usage:
     *                   Cmd.execute(false, new String[]{"curl", "-L", "-s", "http://sample.com"});
     *                   </p>
     */
    public static void execute(String[] command, Consumer<String> callback) throws Exception {
        var executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            var process = new ProcessBuilder(command);
            Process p;
            process.redirectErrorStream(true);

            try {
                p = process.start();
                p.waitFor();
                var reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                var builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                callback.accept(builder.toString());

                p.destroy();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
