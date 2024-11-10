package org.example.services;

import org.example.model.MusicalInstrument;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.example.interfaces.Service;
import org.example.repository.FileRepositoryImpl;

import java.util.Arrays;
import java.util.List;

public class MusicalInstrumentService implements Service<MusicalInstrument> {
    private final AppHelper<MusicalInstrument> appHelperMusicalInstrument;
    private final Input inputProvider;
    private final FileRepository<MusicalInstrument> musicalInstrumentRepository;

    public MusicalInstrumentService(AppHelper<MusicalInstrument> appHelperMusicalInstrument, Input inputProvider) {
        this.appHelperMusicalInstrument = appHelperMusicalInstrument;
        this.inputProvider = inputProvider;
        this.musicalInstrumentRepository = new FileRepositoryImpl<>("musical_instrument.dat"); // Change to file repository
    }

    @Override
    public boolean add() {
        MusicalInstrument equipment = appHelperMusicalInstrument.create();
        if (equipment != null) {
            List<MusicalInstrument> musicalInstrument = musicalInstrumentRepository.load();
            musicalInstrument.add(equipment);
            musicalInstrumentRepository.save(musicalInstrument);
            System.out.println("Музыкальный инструмент успешно добавлен.");
            return true;
        }
        System.out.println("Ошибка при добавлении музыкального инструмента.");
        return false;
    }

    @Override
    public void print() {
        List<MusicalInstrument> musicalInstrument = musicalInstrumentRepository.load();
        if (musicalInstrument.isEmpty()) {
            System.out.println("Список товаров пуст.");
            return;
        }
        appHelperMusicalInstrument.printList(musicalInstrument);
    }

    @Override
    public List<MusicalInstrument> list() {
        return musicalInstrumentRepository.load();
    }

    @Override
    public void editClient() {

    }

    @Override
    public boolean edit(MusicalInstrument updatedEquipment) {
        if (updatedEquipment == null || updatedEquipment.getId() == null) {
            System.out.println("Ошибка: объект для редактирования не может быть null или без ID.");
            return false;
        }

        List<MusicalInstrument> musicalInstrument = musicalInstrumentRepository.load();
        for (int i = 0; i < musicalInstrument.size(); i++) {
            MusicalInstrument equipment = musicalInstrument.get(i);
            if (equipment.getId().equals(updatedEquipment.getId())) {
                musicalInstrument.set(i, updatedEquipment);
                musicalInstrumentRepository.save(musicalInstrument);
                return true;
            }
        }
        System.out.println("Товар не найден.");
        return false;
    }

    @Override
    public boolean remove(MusicalInstrument entity) {
        return false;
    }

    public void removeProduct() {
        List<MusicalInstrument> equipmentList = musicalInstrumentRepository.load();

        if (equipmentList.isEmpty()) {
            System.out.println("Список товаров пуст. Нечего удалять.");
            return;
        }

        System.out.println("Доступные товары для удаления:");
        appHelperMusicalInstrument.printList(equipmentList);

        try {
            System.out.print("Введите номер товара для удаления: ");
            int index = Integer.parseInt(inputProvider.getInput()) - 1;

            if (index < 0 || index >= equipmentList.size()) {
                System.out.println("Некорректный номер товара.");
                return;
            }

            MusicalInstrument equipmentToRemove = equipmentList.remove(index);
            musicalInstrumentRepository.save(equipmentList);
            System.out.println("Товар \"" + equipmentToRemove.getName() + "\" успешно удален.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: некорректный ввод числа.");
        }
    }

    public void editProduct() {
        List<MusicalInstrument> equipmentList = musicalInstrumentRepository.load();
        if (equipmentList.isEmpty()) {
            System.out.println("Список товаров пуст.");
            return;
        }
        appHelperMusicalInstrument.printList(equipmentList);
        System.out.print("Введите номер товара для редактирования: ");
        String indexInput = inputProvider.getInput();
        int index;
        try {
            index = Integer.parseInt(indexInput) - 1;
            if (index < 0 || index >= equipmentList.size()) {
                System.out.println("Некорректный номер товара.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: некорректный ввод числа.");
            return;
        }

        MusicalInstrument equipment = equipmentList.get(index);

        // Сбор новых данных во временные переменные
        System.out.print("Новое название товара: ");
        String newName = inputProvider.getInput();
        System.out.print("Введите новую модель (например Fender Stratocaster): ");
        String categoriesInput = inputProvider.getInput();
        List<String> newCategories = Arrays.asList(categoriesInput.split(","));
        System.out.print("Введите новую цену: ");
        String priceInput = inputProvider.getInput();
        double newPrice;
        try {
            newPrice = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: некорректный ввод числа.");
            return;
        }

        // Обновляем объект только после успешных проверок
        equipment.setName(newName);
        equipment.setCategories(newCategories);
        equipment.setPrice(newPrice);
        musicalInstrumentRepository.save(equipmentList);
        System.out.println("Товар успешно отредактирован.");
    }
}