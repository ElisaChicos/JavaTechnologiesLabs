package org.laborator.lab3;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

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
    }

    public void prepareNewItem() {
        this.currentItem = new Client();
    }
    @Transactional
    public void saveItem() {
        clientRepository.save(currentItem);
        loadClients();
        currentItem = new Client();
    }
    @Transactional
    public void removeClient(Client client) {
        clientRepository.delete(client);
        loadClients();
    }

    public boolean isProduct() {
        return false;
    }

    public boolean isClient() {
        return true;
    }
}
