package org.laborator.lab3;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named("clientBean")
@RequestScoped
public class ClientBean {

    @Inject
    private ClientRepository clientRepository;

    private List<Client> clients;
    private Client currentItem;

    @PostConstruct
    public void init() {
        currentItem = new Client();
        loadClients();
    }

    private void loadClients() {
        clients = clientRepository.findAll();
    }

    public List<Client> getClients() {
        return clients;
    }

    public Client getCurrentItem() {
        return currentItem;
    }

    public void prepareEdit(Client client) {
        this.currentItem = client;
        System.out.println("Editing client: " + currentItem.getName());
    }

    public void prepareNewItem() {
        this.currentItem = new Client();
        System.out.println("Preparing new client");
    }

    public void saveItem() {
        clientRepository.save(currentItem);
        loadClients();
        currentItem = new Client();
    }

    public void addClient(Client client) {
        clientRepository.save(client);
    }

    public void updateClient(Client client) {
        clientRepository.save(client);
    }

    public boolean isClient() {
        return true;
    }

    public boolean isProduct() {
        return false;
    }
}
