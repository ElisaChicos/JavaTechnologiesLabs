import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Logger {

    private static final String LOG_FILE_PATH = "../../../../src/main/resources/logs.txt"; // Path to log file

    public static void logRequest(HttpServletRequest request) {
        String method = request.getMethod();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String[] acceptedLanguages = request.getHeader("Accept-Language") != null
                ? request.getHeader("Accept-Language").split(",")
                : new String[0];

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Request Method: ").append(method)
                .append(", Client IP: ").append(ipAddress)
                .append(", User-Agent: ").append(userAgent)
                .append(", Client Languages: ");

        for (String lang : acceptedLanguages) {
            logMessage.append(lang.trim()).append(" ");
        }

        // Log parameters
        logMessage.append(", Parameters: ");
        HashMap<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
        parameterMap.forEach((key, value) -> {
            logMessage.append(key).append("=").append(String.join(",", value)).append(" ");
        });

        // Write log message to file
        writeLogToFile(logMessage.toString());
        System.out.println(logMessage.toString());
    }

    private static void writeLogToFile(String logMessage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(logMessage);
            writer.newLine(); // Add a new line after each log entry
        } catch (IOException e) {
            System.err.println("Failed to log request: " + e.getMessage());
        }
    }
}
