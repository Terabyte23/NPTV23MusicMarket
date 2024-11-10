package org.example.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Purchase implements Serializable {
    private static final long serialVersionUID = 1L; // добавьте это поле

    private UUID id;
    private Client client;
    private MusicalInstrument musicalInstrument;
    private LocalDate purchaseDate;

    public Purchase() {
        this.id = UUID.randomUUID();
    }

    public Purchase(Client client, MusicalInstrument musicalInstrument, LocalDate purchaseDate) {
        this.id = UUID.randomUUID();
        this.client = client;
        this.musicalInstrument = musicalInstrument;
        this.purchaseDate = purchaseDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public MusicalInstrument getMusicalInstrument() {
        return musicalInstrument;
    }

    public void setMusicalInstrument(MusicalInstrument musicalInstrument) {
        this.musicalInstrument = musicalInstrument;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return String.format("Покупка: %s, Товар: %s, Дата: %s",
                client.getFirstname() + " " + client.getLastname(),
                musicalInstrument.getName(),
                purchaseDate);
    }

    public void setDetails(String updatedDetails) {
    }
}