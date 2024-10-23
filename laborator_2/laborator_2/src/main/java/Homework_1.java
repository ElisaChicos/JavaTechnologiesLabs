import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/homework_1")
public class Homework_1 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.logRequest(request);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Logger.logRequest(request);

        int numVertices = Integer.parseInt(request.getParameter("numVertices"));
        int numEdges = Integer.parseInt(request.getParameter("numEdges"));

        int[][] adjacencyMatrix = generateAdjacencyMatrix(numVertices, numEdges);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Adjacency Matrix</title></head><body>");
        out.println("<h2>Adjacency Matrix for a Graph</h2>");
        out.println("<table border='1'>");

        for (int i = 0; i < numVertices; i++) {
            out.println("<tr>");
            for (int j = 0; j < numVertices; j++) {
                out.println("<td>" + adjacencyMatrix[i][j] + "</td>");
            }
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body></html>");
    }

    private int[][] generateAdjacencyMatrix(int numVertices, int numEdges) {
        int[][] matrix = new int[numVertices][numVertices];
        Random random = new Random();
        HashSet<String> edges = new HashSet<>();

        while (edges.size() < numEdges) {
            int u = random.nextInt(numVertices);
            int v = random.nextInt(numVertices);
            // Prevent self-loops and duplicate edges
            if (u != v && edges.add(u + "-" + v)) {
                matrix[u][v] = 1; // Directed graph: edge from u to v
                matrix[v][u] = 1;
            }
        }
        return matrix;
    }
}
