package services;

import org.example.model.MusicalInstrument;
import org.example.services.MusicalInstrumentService;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MusicalInstrumentServiceTest {
    private MusicalInstrumentService musicalInstrumentService;
    private AppHelper<MusicalInstrument> appHelperMusicalInstrumentMock;
    private Input inputProviderMock;
    private FileRepository<MusicalInstrument> musicalInstrumentRepositoryMock;

    @Before
    public void setUp() throws Exception {
        // Создаем мок-объекты
        appHelperMusicalInstrumentMock = mock(AppHelper.class);
        inputProviderMock = mock(Input.class);
        musicalInstrumentRepositoryMock = mock(FileRepository.class);

        // Инициализируем сервис
        musicalInstrumentService = new MusicalInstrumentService(
                appHelperMusicalInstrumentMock,
                inputProviderMock
        );

        // Используем рефлексию для замены приватного поля sportEquipmentRepository на мок
        Field repositoryField = MusicalInstrumentService.class.getDeclaredField("musicalInstrumentRepository");
        repositoryField.setAccessible(true);
        repositoryField.set(musicalInstrumentService, musicalInstrumentRepositoryMock);
    }

    // Тест для метода add() - успешный случай
    @Test
    public void testAddSuccess() {
        // Arrange
        MusicalInstrument newEquipment = new MusicalInstrument();
        when(appHelperMusicalInstrumentMock.create()).thenReturn(newEquipment);

        List<MusicalInstrument> equipmentList = new ArrayList<>();
        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);

        // Act
        boolean result = musicalInstrumentService.add();

        // Assert
        assertTrue(result);
        verify(musicalInstrumentRepositoryMock).save(equipmentList);
        assertEquals(1, equipmentList.size());
        assertEquals(newEquipment, equipmentList.get(0));
    }

    // Тест для метода add() - неудачный случай
    @Test
    public void testAddFailure() {
        // Arrange
        when(appHelperMusicalInstrumentMock.create()).thenReturn(null);

        // Act
        boolean result = musicalInstrumentService.add();

        // Assert
        assertFalse(result);
        verify(musicalInstrumentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода print() с пустым списком
    @Test
    public void testPrintEmptyList() {
        // Arrange
        when(musicalInstrumentRepositoryMock.load()).thenReturn(new ArrayList<>());

        // Act
        musicalInstrumentService.print();

        // Assert
        verify(appHelperMusicalInstrumentMock, never()).printList(anyList());
    }

    // Тест для метода print() с непустым списком
    @Test
    public void testPrintWithEquipments() {
        // Arrange
        List<MusicalInstrument> equipmentList = Arrays.asList(new MusicalInstrument(), new MusicalInstrument());
        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);

        // Act
        musicalInstrumentService.print();

        // Assert
        verify(appHelperMusicalInstrumentMock).printList(equipmentList);
    }

    // Тест для метода list()
    @Test
    public void testList() {
        // Arrange
        List<MusicalInstrument> equipmentList = Arrays.asList(new MusicalInstrument(), new MusicalInstrument());
        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);

        // Act
        List<MusicalInstrument> result = musicalInstrumentService.list();

        // Assert
        assertEquals(equipmentList, result);
    }

    // Тест для метода edit() - null updatedEquipment
    @Test
    public void testEditNullEquipment() {
        // Act
        boolean result = musicalInstrumentService.edit(null);

        // Assert
        assertFalse(result);
    }

    // Тест для метода edit() - updatedEquipment с null ID
    @Test
    public void testEditEquipmentWithNullId() {
        // Arrange
        MusicalInstrument updatedEquipment = new MusicalInstrument();
        updatedEquipment.setId(null);

        // Act
        boolean result = musicalInstrumentService.edit(updatedEquipment);

        // Assert
        assertFalse(result);
    }

    // Тест для метода edit() - оборудование найдено и обновлено
    @Test
    public void testEditSuccess() {
        // Arrange
        UUID id123 = UUID.randomUUID();
        MusicalInstrument updatedEquipment = new MusicalInstrument();
        updatedEquipment.setId(id123);
        updatedEquipment.setName("Updated Name");
        updatedEquipment.setCategories(Arrays.asList("Category1", "Category2"));
        updatedEquipment.setPrice(150.0);

        MusicalInstrument existingEquipment = new MusicalInstrument();
        existingEquipment.setId(id123);
        existingEquipment.setName("Old Name");
        existingEquipment.setCategories(Arrays.asList("Category1"));
        existingEquipment.setPrice(100.0);

        List<MusicalInstrument> equipmentList = new ArrayList<>();
        equipmentList.add(existingEquipment);

        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);

        // Act
        boolean result = musicalInstrumentService.edit(updatedEquipment);

        // Assert
        assertTrue(result);
        assertEquals(updatedEquipment, equipmentList.get(0));
        verify(musicalInstrumentRepositoryMock).save(equipmentList);
    }

    // Тест для метода edit() - оборудование не найдено
    @Test
    public void testEditEquipmentNotFound() {
        // Arrange
        UUID id123 = UUID.randomUUID();
        MusicalInstrument updatedEquipment = new MusicalInstrument();
        updatedEquipment.setId(id123);

        MusicalInstrument existingEquipment = new MusicalInstrument();
        existingEquipment.setId(UUID.randomUUID()); // Различный UUID

        List<MusicalInstrument> equipmentList = new ArrayList<>();
        equipmentList.add(existingEquipment);

        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);

        // Act
        boolean result = musicalInstrumentService.edit(updatedEquipment);

        // Assert
        assertFalse(result);
        verify(musicalInstrumentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода removeProduct() - пустой список
    @Test
    public void testRemoveProductEmptyList() {
        // Arrange
        when(musicalInstrumentRepositoryMock.load()).thenReturn(new ArrayList<>());

        // Act
        musicalInstrumentService.removeProduct();

        // Assert
        verify(inputProviderMock, never()).getInput();
        verify(musicalInstrumentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода removeProduct() - корректный индекс
    @Test
    public void testRemoveProductSuccess() {
        // Arrange
        MusicalInstrument equipment1 = new MusicalInstrument();
        equipment1.setName("Equipment1");
        MusicalInstrument equipment2 = new MusicalInstrument();
        equipment2.setName("Equipment2");

        List<MusicalInstrument> equipmentList = new ArrayList<>();
        equipmentList.add(equipment1);
        equipmentList.add(equipment2);

        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("1");

        // Act
        musicalInstrumentService.removeProduct();

        // Assert
        assertEquals(1, equipmentList.size());
        assertEquals(equipment2, equipmentList.get(0));
        verify(musicalInstrumentRepositoryMock).save(equipmentList);
    }

    // Тест для метода removeProduct() - некорректный индекс
    @Test
    public void testRemoveProductInvalidIndex() {
        // Arrange
        MusicalInstrument equipment1 = new MusicalInstrument();
        equipment1.setName("Equipment1");

        List<MusicalInstrument> equipmentList = new ArrayList<>();
        equipmentList.add(equipment1);

        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("5");

        // Act
        musicalInstrumentService.removeProduct();

        // Assert
        assertEquals(1, equipmentList.size());
        verify(musicalInstrumentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода removeProduct() - нечисловой ввод
    @Test
    public void testRemoveProductNonNumericInput() {
        // Arrange
        MusicalInstrument equipment1 = new MusicalInstrument();
        equipment1.setName("Equipment1");

        List<MusicalInstrument> equipmentList = new ArrayList<>();
        equipmentList.add(equipment1);

        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("abc");

        // Act
        musicalInstrumentService.removeProduct();

        // Assert
        assertEquals(1, equipmentList.size());
        verify(musicalInstrumentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода editProduct() - успешный случай
    @Test
    public void testEditProductSuccess() {
        // Arrange
        UUID equipmentId = UUID.randomUUID();
        MusicalInstrument equipment = new MusicalInstrument();
        equipment.setId(equipmentId);
        equipment.setName("Old Name");
        equipment.setCategories(Arrays.asList("Category1"));
        equipment.setPrice(100.0);

        List<MusicalInstrument> equipmentList = new ArrayList<>();
        equipmentList.add(equipment);

        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("1", "New Name", "Category2,Category3", "200.0");

        // Act
        musicalInstrumentService.editProduct();

        // Assert
        assertEquals("New Name", equipment.getName());
        assertEquals(Arrays.asList("Category2", "Category3"), equipment.getCategories());
        assertEquals(200.0, equipment.getPrice(), 0.001);
        verify(musicalInstrumentRepositoryMock).save(equipmentList);
    }

    // Тест для метода editProduct() - некорректный индекс
    @Test
    public void testEditProductInvalidIndex() {
        // Arrange
        MusicalInstrument equipment = new MusicalInstrument();
        equipment.setId(UUID.randomUUID());
        equipment.setName("Equipment1");

        List<MusicalInstrument> equipmentList = new ArrayList<>();
        equipmentList.add(equipment);

        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("5");

        // Act
        musicalInstrumentService.editProduct();

        // Assert
        verify(musicalInstrumentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода editProduct() - нечисловой ввод индекса
    @Test
    public void testEditProductNonNumericIndex() {
        // Arrange
        MusicalInstrument equipment = new MusicalInstrument();
        equipment.setId(UUID.randomUUID());
        equipment.setName("Equipment1");

        List<MusicalInstrument> equipmentList = new ArrayList<>();
        equipmentList.add(equipment);

        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("abc");

        // Act
        musicalInstrumentService.editProduct();

        // Assert
        verify(musicalInstrumentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода editProduct() - некорректный ввод цены
    @Test
    public void testEditProductInvalidPriceInput() {
        // Arrange
        UUID equipmentId = UUID.randomUUID();
        MusicalInstrument equipment = new MusicalInstrument();
        equipment.setId(equipmentId);
        equipment.setName("Old Name");
        equipment.setCategories(Arrays.asList("Category1"));
        equipment.setPrice(100.0);

        List<MusicalInstrument> equipmentList = new ArrayList<>();
        equipmentList.add(equipment);

        when(musicalInstrumentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("1", "New Name", "Category2", "invalid price");

        // Act
        musicalInstrumentService.editProduct();

        // Assert
        // Проверяем, что оборудование не обновлено
        assertEquals("Old Name", equipment.getName());
        assertEquals(Arrays.asList("Category1"), equipment.getCategories());
        assertEquals(100.0, equipment.getPrice(), 0.001);
        verify(musicalInstrumentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода editProduct() - пустой список
    @Test
    public void testEditProductEmptyList() {
        // Arrange
        when(musicalInstrumentRepositoryMock.load()).thenReturn(new ArrayList<>());
        when(inputProviderMock.getInput()).thenReturn("1");

        // Act
        musicalInstrumentService.editProduct();

        // Assert
        verify(musicalInstrumentRepositoryMock, never()).save(anyList());
    }
}