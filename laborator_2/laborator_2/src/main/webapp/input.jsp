<html lang="en">
<head>
    <title>Upload Text File</title>
</head>
<body>
<h2>Upload a Text File</h2>
<form action="ShuffledFile" method="post" enctype="multipart/form-data">
    <label for="fileUpload">Choose a text file:</label>
    <input type="file" id="fileUpload" name="fileUpload" accept=".txt" required>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <div class="g-recaptcha" data-sitekey="6LetCWkqAAAAAC9CRLaZ9KRi7lfL-ckS02UVV77V"></div>
    <button type="submit">Upload</button>
</form>
</body>
</html>
