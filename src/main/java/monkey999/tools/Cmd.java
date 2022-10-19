package monkey999.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * command prompt wrapper.
 */
public class Cmd {
    /**
     * @param async   - async/await
     * @param command command list. running by {@link ProcessBuilder}.
     * @return running result by command output from standard out.
     *
     * usage
     *  Cmd.execute(false, new String[]{"curl", "-L", "-s", "http://sample.com"});
     */
    public static String execute(boolean async, String[] command) throws Exception{
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
}
