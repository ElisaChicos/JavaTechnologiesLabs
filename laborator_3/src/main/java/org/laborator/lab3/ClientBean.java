package org.laborator.lab3;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Named
@RequestScoped
public class ClientBean {
    private List<Client> clients;
    private Client currentClient;
    @PostConstruct
    public void init() {
        clients = new ArrayList<>();
        currentClient = new Client();
        loadClients();
    }
    private void loadClients() {
        String sql = "SELECT * FROM clients";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Client client = new Client(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("date_of_birth"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Object getClients() {
        return clients;
    }
    public Client getCurrentClient() {
        return currentClient;
    }
    public void prepareEdit(Client client) {
        this.currentClient = client;
        System.out.println("Editing client: " + currentClient.getName()); // Debugging output
    }

    public void initializeClient() {
        if (currentClient == null) {
            currentClient = new Client();
        }
    }
    public void prepareNewClient() {
        this.currentClient = new Client();
        System.out.println("Preparing new client"); // Debugging output
    }

    public void saveClient() {
        if (currentClient.getId() == null) {
            System.out.println("Adding new client: " + currentClient.getName()); // Debugging output
            addClient(currentClient);
        } else {
            System.out.println("Updating client: " + currentClient.getName()); // Debugging output
            updateClient(currentClient);
        }
        loadClients(); // Refresh the list after save
        currentClient = new Client();
    }

    private void addClient(Client client) {
        String sql = "INSERT INTO clients (name, date_of_birth, email, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getDate_of_birth());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getPhone_number());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateClient(Client client) {
        String sql = "UPDATE clients SET name = ?, date_of_birth = ?, email = ?, phone_number = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getDate_of_birth());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getPhone_number());
            pstmt.setLong(5, client.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}