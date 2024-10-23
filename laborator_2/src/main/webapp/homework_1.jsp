<!DOCTYPE html>
<html>
<head>
  <title>Graph Input Form</title>
</head>
<body>
<h2>Enter Graph Parameters</h2>
<form action="homework_1" method="post">
  <label for="numVertices">Number of Vertices:</label>
  <input type="number" id="numVertices" name="numVertices" min="1" required>
  <br><br>
  <label for="numEdges">Number of Edges:</label>
  <input type="number" id="numEdges" name="numEdges" min="0" required>
  <br><br>
  <input type="submit" value="Generate Graph">
</form>
</body>
</html>
