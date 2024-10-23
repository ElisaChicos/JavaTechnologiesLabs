import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/ShuffledFile")
@MultipartConfig // This allows the servlet to handle file uploads
public class FileUploadServlet extends HttpServlet {

    private static final String RECAPTCHA_SECRET_KEY = "6LetCWkqAAAAAFeHpLeSElwn8DsGSMX9BM6O2NPR";
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.logRequest(request);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.logRequest(request);

        // Retrieve the file part from the request
        Part filePart = request.getPart("fileUpload"); // "fileUpload" is the form field name

        if (filePart == null || getFileName(filePart).isEmpty()) {
            response.getWriter().println("No file uploaded.");
            return;
        }

        // List to hold lines of the file
        List<String> lines = new ArrayList<>();

        // Read the file content directly from the input stream
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream()))) {
            String line;
            // Add each line to the list
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            response.getWriter().println("Error reading the file: " + e.getMessage());
            return;
        }

        // Shuffle the lines
        Collections.shuffle(lines);

        // Set the shuffled lines as a request attribute
        request.setAttribute("shuffledLines", lines);

        //Logger.logRequest(request);
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");



        // Forward to result.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
        dispatcher.forward(request, response);
    }

    // Utility method to get the file name from the Part object
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            // Extract the file name from the content-disposition header
            for (String cdPart : contentDisposition.split(";")) {
                if (cdPart.trim().startsWith("filename")) {
                    // Return the file name, trimming any quotes that may be present
                    return cdPart.substring(cdPart.indexOf('=') + 1).trim().replace("\"", "");
                }
            }
        }
        return null; // No file name found
    }

    private boolean verifyCaptcha(String gRecaptchaResponse) throws IOException {
        if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
            return false;
        }

        // Prepare the verification request to Google's API
        URL url = new URL(RECAPTCHA_VERIFY_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        // Build the request parameters
        String postParams = "secret=" + URLEncoder.encode(RECAPTCHA_SECRET_KEY, String.valueOf(StandardCharsets.UTF_8)) +
                "&response=" + URLEncoder.encode(gRecaptchaResponse, String.valueOf(StandardCharsets.UTF_8));

        // Send the request (use OutputStream as necessary for POST data)
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(postParams.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();  // Ensure all data is sent
        }

        // Read the API response
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        // Parse the response (a simple boolean check if "success": true)
        String responseStr = response.toString();
        return responseStr.contains("\"success\": true");
    }

}
