package apphelpers;

import org.example.AppHelperPurchase;
import org.example.model.Client;
import org.example.model.Purchase;
import org.example.model.MusicalInstrument;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppHelperPurchaseTest {

    private AppHelperPurchase appHelperPurchase;
    private FileRepository<Purchase> purchaseRepository;
    private FileRepository<Client> clientRepository;
    private FileRepository<MusicalInstrument> musicalInstrumentRepository;
    private Input inputProvider;

    @BeforeEach
    void setUp() {
        // Мокаем репозитории и интерфейс ввода
        purchaseRepository = mock(FileRepository.class);
        clientRepository = mock(FileRepository.class);
        musicalInstrumentRepository = mock(FileRepository.class);
        inputProvider = mock(Input.class);

        // Инициализируем AppHelperPurchase с моками
        appHelperPurchase = new AppHelperPurchase(purchaseRepository, clientRepository, musicalInstrumentRepository, inputProvider);
    }

    @Test
    void testCreatePurchaseSuccess() {
        // Настройка моков для списка клиентов
        Client client = new Client("Иван", "Иванов", "123456789");
        List<Client> clients = List.of(client);
        when(clientRepository.load()).thenReturn(clients);

        // Настройка моков для списка спортивного инвентаря
        MusicalInstrument equipment = new MusicalInstrument("Гитара", List.of("Ibanez"), 250);
        List<MusicalInstrument> equipmentList = List.of(equipment);
        when(musicalInstrumentRepository.load()).thenReturn(equipmentList);

        // Настройка ввода пользователя
        when(inputProvider.getInput())
                .thenReturn("1")               // Выбор клиента
                .thenReturn("1")               // Выбор спортивного инвентаря
                .thenReturn("2024-11-10");     // Дата покупки

        // Вызов метода create и проверка результата
        Purchase purchase = appHelperPurchase.create();

        assertNotNull(purchase, "Покупка должна быть создана");
        assertEquals(client, purchase.getClient(), "Клиент должен совпадать с выбранным");
        assertEquals(equipment, purchase.getMusicalInstrument(), "Музыкальный инструмент должен совпадать с выбранным");
        assertEquals(LocalDate.of(2024, 11, 10), purchase.getPurchaseDate(), "Дата покупки должна совпадать с введённой");
    }

    @Test
    void testCreatePurchaseNoClients() {
        // Настройка моков для пустого списка клиентов
        when(clientRepository.load()).thenReturn(List.of());

        // Вызов метода create и проверка результата
        Purchase purchase = appHelperPurchase.create();

        assertNull(purchase, "Покупка не должна быть создана при пустом списке клиентов");
    }

    @Test
    void testCreatePurchaseNoSportEquipment() {
        // Настройка моков для списка клиентов
        Client client = new Client("Иван", "Иванов", "123456789");
        when(clientRepository.load()).thenReturn(List.of(client));

        // Настройка моков для пустого списка спортивного инвентаря
        when(musicalInstrumentRepository.load()).thenReturn(List.of());

        // Настройка ввода пользователя для выбора клиента
        when(inputProvider.getInput()).thenReturn("1"); // Выбор клиента

        // Вызов метода create и проверка результата
        Purchase purchase = appHelperPurchase.create();

        assertNull(purchase, "Покупка не должна быть создана при пустом списке музыкальных инструментов");
    }

    @Test
    void testCreatePurchaseInvalidClientIndex() {
        // Настройка моков для списка клиентов
        Client client = new Client("Иван", "Иванов", "123456789");
        when(clientRepository.load()).thenReturn(List.of(client));

        // Настройка ввода пользователя для некорректного выбора клиента
        when(inputProvider.getInput()).thenReturn("2"); // Неверный индекс клиента

        // Вызов метода create и проверка результата
        Purchase purchase = appHelperPurchase.create();

        assertNull(purchase, "Покупка не должна быть создана при некорректном номере клиента");
    }

    @Test
    void testCreatePurchaseInvalidEquipmentIndex() {
        // Настройка моков для списка клиентов и спортивного инвентаря
        Client client = new Client("Иван", "Иванов", "123456789");
        when(clientRepository.load()).thenReturn(List.of(client));

        MusicalInstrument equipment = new MusicalInstrument("Гитара", List.of("Ibanez"), 250);
        when(musicalInstrumentRepository.load()).thenReturn(List.of(equipment));

        // Настройка ввода пользователя для выбора клиента и некорректного выбора инвентаря
        when(inputProvider.getInput())
                .thenReturn("1") // Выбор клиента
                .thenReturn("2"); // Неверный индекс спортивного инвентаря

        // Вызов метода create и проверка результата
        Purchase purchase = appHelperPurchase.create();

        assertNull(purchase, "Покупка не должна быть создана при некорректном номере музыкального инструмента");
    }
}