import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/compulsory")
public class Compulsory extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.logRequest(request);

        // Display the input form when accessed via GET
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Input Form</title></head><body>");
        out.println("<h2>Enter a string:</h2>");
        out.println("<form action='compulsory' method='post'>");
        out.println("<label for='inputString'>String:</label>");
        out.println("<input type='text' id='inputString' name='inputString'>");
        out.println("<input type='submit' value='Submit'>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.logRequest(request);

        String inputString = request.getParameter("inputString");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>String Display</title></head><body>");
        out.println("<h2>Characters in the string:</h2>");
        out.println("<ol>");

        // Generate the ordered list of characters
        if (inputString != null && !inputString.isEmpty()) {
            for (char c : inputString.toCharArray()) {
                out.println("<li>" + c + "</li>");
            }
        } else {
            out.println("<li>No input provided</li>");
        }

        out.println("</ol>");
        out.println("</body></html>");
    }
}
