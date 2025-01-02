package demojavaapp;

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static String readf(String file) {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (final InputStream is = classloader.getResourceAsStream(file)) {
            if (is == null) {
                throw new IllegalArgumentException("file %s not found".formatted(file));
            }
            return new String(is.readAllBytes());
        } catch (IOException ioe) {
            throw new IllegalArgumentException("file %s not readable".formatted(file));
        }
    }
}
