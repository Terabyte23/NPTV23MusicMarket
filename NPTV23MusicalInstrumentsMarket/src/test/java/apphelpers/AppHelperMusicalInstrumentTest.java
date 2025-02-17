package apphelpers;

import org.example.AppHelperMusicalInstrument;
import org.example.model.MusicalInstrument;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppHelperSportEquipmentTest {

    private AppHelperMusicalInstrument appHelperMusicalInstrument;
    private FileRepository<MusicalInstrument> musicalInstrumentRepository;
    private Input inputProvider;

    @BeforeEach
    void setUp() {
        // Мокаем репозиторий и интерфейс ввода
        musicalInstrumentRepository = mock(FileRepository.class);
        inputProvider = mock(Input.class);

        // Инициализируем AppHelperSportEquipment с моками
        appHelperMusicalInstrument = new AppHelperMusicalInstrument(musicalInstrumentRepository, inputProvider);
    }

    @Test
    void testCreateSportEquipmentSuccess() {
        // Настройка последовательного ввода для создания спортивного инвентаря
        when(inputProvider.getInput())
                .thenReturn("Гитара")       // Название инвентаря
                .thenReturn("Ibanez")           // Категории
                .thenReturn("250");               // Цена с запятой

        // Вызов метода create и проверка результата
        MusicalInstrument equipment = appHelperMusicalInstrument.create();

        assertNotNull(equipment, "Музыкальный инструмент должен быть создан");
        assertEquals("Гитара", equipment.getName());
        assertEquals(List.of("Ibanez"), equipment.getCategories());
        assertEquals(250, equipment.getPrice(), 0.01);
    }

    @Test
    void testCreateSportEquipmentInvalidPrice() {
        // Настройка ввода с некорректной ценой
        when(inputProvider.getInput())
                .thenReturn("Гитара")      // Название инвентаря
                .thenReturn("Ibanez")          // Категории
                .thenReturn("двести пятьдесят");       // Некорректная цена

        // Вызов метода create и проверка результата
        MusicalInstrument equipment = appHelperMusicalInstrument.create();

        assertNull(equipment, "Музыкальный инструмент не должен быть создан с некорректной ценой");
    }

    @Test
    void testPrintList() {
        // Настройка списка спортивного инвентаря
        List<MusicalInstrument> equipmentList = List.of(
                new MusicalInstrument("Гитара", List.of("Ibanez"), 250),
                new MusicalInstrument("Барабаны", List.of("GRETSCH"), 1422.31)
        );

        // Вызываем метод printList и проверяем, что он выводит данные
        appHelperMusicalInstrument.printList(equipmentList);

        // Здесь можно проверить, что вывод выполнен корректно.
        // Если нужно, перенаправьте System.out на поток для захвата вывода и последующей проверки.
    }
}