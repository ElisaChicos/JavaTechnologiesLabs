import requests
from bs4 import BeautifulSoup

# URL to the servlet (replace with your actual servlet URL)
url = "http://localhost:8080/Lab1/homework_1"

# Parameters for the POST request
params = {
    'numVertices': 4,
    'numEdges': 3
}

# Send the POST request
response = requests.post(url, data=params)

# Check if the request was successful
if response.status_code == 200:
    # Parse the HTML response using BeautifulSoup
    soup = BeautifulSoup(response.text, "html.parser")
    
    # Find the table in the HTML
    table = soup.find('table')
    
    # Extract the matrix from the table rows and cells
    matrix = []
    for row in table.find_all('tr'):
        cells = [cell.text for cell in row.find_all('td')]
        matrix.append(cells)
    
    # Print the matrix in a more readable format
    print("Formatted Adjacency Matrix:")
    for row in matrix:
        print(" ".join(row))
else:
    print(f"Failed to get response: {response.status_code}")

