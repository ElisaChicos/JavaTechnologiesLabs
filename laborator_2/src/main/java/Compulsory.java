import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/compulsory")
public class Compulsory extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.logRequest(request);
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
