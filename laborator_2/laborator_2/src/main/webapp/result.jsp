<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shuffled File Lines</title>
</head>
<body>
<h2>Shuffled Lines of the Uploaded File:</h2>
<ul>
    <%
        // Get the shuffled lines from the request attribute
        List<String> shuffledLines = (List<String>) request.getAttribute("shuffledLines");

        if (shuffledLines != null) {
            // Iterate and display each line as a list item
            for (String line : shuffledLines) {
                out.println("<li>" + line + "</li>");
            }
        } else {
            out.println("<p>No lines available.</p>");
        }
    %>
</ul>
</body>
</html>
