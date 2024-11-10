package org.example;

import org.example.services.ClientService;
import org.example.services.MusicalInstrumentService;
import org.example.interfaces.Service;
import org.example.interfaces.Input;
import org.example.model.Client;
import org.example.model.MusicalInstrument;
import org.example.model.Purchase;
import java.util.Scanner;

public class App implements Input {

    private final Service<MusicalInstrument> musicalInstrumentService;
    private final Service<Client> clientService;
    private final Service<Purchase> purchaseService;
    private final Scanner scanner = new Scanner(System.in);

    public App(Service<MusicalInstrument> musicalInstrumentService, Service<Client> clientService, Service<Purchase> purchaseService) {
        this.musicalInstrumentService = musicalInstrumentService;
        this.clientService = clientService;
        this.purchaseService = purchaseService;
    }

    public void run() {
        System.out.println("------ Магазин спортивного инвентаря группы NPTV23 ------");
        System.out.println("-------------------------------------------------------");

        boolean repeat = true;
        do {
            displayMenu();

            System.out.print("Введите номер задачи: ");
            int task = Integer.parseInt(getInput());
            switch (task) {
                case 0:
                    repeat = false;
                    break;
                case 1:
                    System.out.println("----- Добавление товара -----");
                    musicalInstrumentService.add();
                    break;
                case 2:
                    System.out.println("----- Список товаров -----");
                    musicalInstrumentService.print();
                    break;
                case 3:
                    System.out.println("----- Редактирование товара -----");
                    // Вызов editProduct, если это SportEquipmentService
                    if (musicalInstrumentService instanceof MusicalInstrumentService) {
                        ((MusicalInstrumentService) musicalInstrumentService).editProduct();
                    } else {
                        System.out.println("Редактирование товара недоступно.");
                    }
                    break;
                case 4:
                    System.out.println("----- Удаление товара -----");
                    if (musicalInstrumentService instanceof MusicalInstrumentService) {
                        ((MusicalInstrumentService) musicalInstrumentService).removeProduct();
                    } else {
                        System.out.println("Удаление товара недоступно.");
                    }
                    break;
                case 5:
                    System.out.println("----- Добавление клиента -----");
                    clientService.add();
                    break;
                case 6:
                    System.out.println("----- Список клиентов -----");
                    clientService.print();
                    break;
                case 7:
                    System.out.println("----- Редактирование клиента -----");
                    clientService.editClient(); // Вызов метода редактирования клиента
                    break;
                case 8:
                    System.out.println("----- Удаление клиента -----");
                    if (clientService instanceof ClientService) {
                        ((ClientService) clientService).removeClient();
                    } else {
                        System.out.println("Удаление клиентов недоступно.");
                    }
                    break;
                case 9:
                    System.out.println("----- Покупка товара -----");
                    purchaseService.add();
                    break;
                case 10:
                    System.out.println("----- Список приобретенных товаров -----");
                    purchaseService.print();
                    break;
                default:
                    System.out.println("Выберите задачу из списка!");
            }
            System.out.println("-------------------------------------------------------");
        } while (repeat);

        System.out.println("До свидания :)");
    }

    private void displayMenu() {
        System.out.println("Список задач: ");
        System.out.println("0. Выйти из программы");
        System.out.println("1. Добавить товар");
        System.out.println("2. Список товаров");
        System.out.println("3. Редактировать товар");
        System.out.println("4. Удалить товар");
        System.out.println("5. Добавить клиента");
        System.out.println("6. Список клиентов");
        System.out.println("7. Редактировать клиента");
        System.out.println("8. Удалить клиента");
        System.out.println("9. Купить товар");
        System.out.println("10. Список приобретенных товаров");
    }

    @Override
    public String getInput() {
        return scanner.nextLine();
    }
}