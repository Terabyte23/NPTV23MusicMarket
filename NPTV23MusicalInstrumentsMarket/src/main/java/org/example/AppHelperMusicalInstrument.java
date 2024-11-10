package org.example;

import org.example.model.MusicalInstrument;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;

import java.util.List;
import java.util.UUID;

public class AppHelperMusicalInstrument implements AppHelper<MusicalInstrument> {
    private final FileRepository<MusicalInstrument> musicalInstrumentRepository;
    private final Input inputProvider;

    // Конструктор принимает Input для гибкости
    public AppHelperMusicalInstrument(FileRepository<MusicalInstrument> sportEquipmentRepository, Input inputProvider) {
        this.musicalInstrumentRepository = sportEquipmentRepository;
        this.inputProvider = inputProvider;
    }

    public FileRepository<MusicalInstrument> getRepository() {
        return musicalInstrumentRepository;
    }

    @Override
    public MusicalInstrument create() {
        try {
            MusicalInstrument equipment = new MusicalInstrument();
            equipment.setId(UUID.randomUUID());
            System.out.print("Название музыкального инструмента (например гитара): ");
            equipment.setName(getInput());
            System.out.print("Введите модель (например, Fender Stratocaster): ");
            String[] categoriesArray = getInput().split(",");
            equipment.setCategories(List.of(categoriesArray));
            System.out.print("Цена (например, 19,99): ");

            // Получаем строку с ценой и заменяем запятую на точку
            String priceInput = getInput().replace(",", ".");
            equipment.setPrice(Double.parseDouble(priceInput));

            return equipment;
        } catch (Exception e) {
            // Убираем или логируем сообщение об ошибке, если не нужно его выводить
            // System.out.println("Ошибка при создании спортивного инвентаря: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void printList(List<MusicalInstrument> equipmentList) {
        System.out.println("---------- Список музыкальных инструментов --------");
        for (int i = 0; i < equipmentList.size(); i++) {
            MusicalInstrument equipment = equipmentList.get(i);
            System.out.printf("%d. Название: %s, Категории: %s, Цена: %.2f%n", i + 1, equipment.getName(), String.join(", ", equipment.getCategories()), equipment.getPrice());
        }
    }

    // Используем метод getInput() вместо scanner.nextLine
    private String getInput() {
        return inputProvider.getInput();
    }
}