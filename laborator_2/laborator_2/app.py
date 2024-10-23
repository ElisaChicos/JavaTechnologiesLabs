import requests
from bs4 import BeautifulSoup

url = "http://localhost:8080/Lab1/homework_1"

params = {
    'numVertices': 4,
    'numEdges': 3
}

response = requests.post(url, data=params)

if response.status_code == 200:
    soup = BeautifulSoup(response.text, "html.parser")
    table = soup.find('table')
    
    # Extract the matrix from the table rows and cells
    matrix = []
    for row in table.find_all('tr'):
        cells = [cell.text for cell in row.find_all('td')]
        matrix.append(cells)

    print("Formatted Adjacency Matrix:")
    for row in matrix:
        print(" ".join(row))
else:
    print(f"Failed to get response: {response.status_code}")

