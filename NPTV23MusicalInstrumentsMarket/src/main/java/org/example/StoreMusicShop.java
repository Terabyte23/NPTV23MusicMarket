package org.example;

import org.example.model.Client;
import org.example.model.Purchase;
import org.example.model.MusicalInstrument;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Service;
import org.example.repository.FileRepositoryImpl;
import org.example.services.ClientService;
import org.example.services.MusicalInstrumentService;
import org.example.services.PurchaseService;
import org.example.interfaces.Input;

import java.util.Scanner;

public class StoreMusicShop {

    public static void main(String[] args) {

        // Инициализация репозиториев для хранения данных в файлах
        FileRepository<MusicalInstrument> musicalInstrumentRepository = new FileRepositoryImpl<>("musical_instrument.dat");
        FileRepository<Client> clientRepository = new FileRepositoryImpl<>("clients.dat");
        FileRepository<Purchase> purchaseRepository = new FileRepositoryImpl<>("purchases.dat");

        // Инициализация сканера для ввода
        Scanner scanner = new Scanner(System.in);

        // Лямбда-выражение для функционального интерфейса Input
        Input inputProvider = scanner::nextLine;

        // Инициализация AppHelpers с использованием inputProvider для гибкости ввода
        AppHelperClient appHelperClient = new AppHelperClient(clientRepository, inputProvider);
        AppHelperMusicalInstrument appHelperMusicalInstrument = new AppHelperMusicalInstrument(musicalInstrumentRepository, inputProvider);
        AppHelperPurchase appHelperPurchase = new AppHelperPurchase(purchaseRepository, clientRepository, musicalInstrumentRepository, inputProvider);

        // Инициализация сервисов, используя только репозитории
        Service<Client> clientService = new ClientService(appHelperClient, inputProvider);
        Service<MusicalInstrument> musicalInstrumentService = new MusicalInstrumentService(appHelperMusicalInstrument, inputProvider);
        Service<Purchase> purchaseService = new PurchaseService(appHelperPurchase, inputProvider);

        // Запуск приложения
        App app = new App(musicalInstrumentService, clientService, purchaseService);
        app.run();
    }
}