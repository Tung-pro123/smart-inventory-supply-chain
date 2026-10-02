package com.tung.inventory.service;

import com.tung.inventory.dto.request.InboundRequest;
import com.tung.inventory.dto.request.OutboundRequest;
import com.tung.inventory.dto.response.InventoryEventResponse;
import com.tung.inventory.dto.response.InventorySnapshotResponse;
import com.tung.inventory.entity.*;
import com.tung.inventory.exception.InsufficientStockException;
import com.tung.inventory.exception.ResourceNotFoundException;
import com.tung.inventory.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InventoryService Tests")
class InventoryServiceTest {

    @Mock
    private InventorySnapshotRepository snapshotRepository;

    @Mock
    private InventoryEventRepository eventRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Product testProduct;
    private Warehouse testWarehouse;
    private InventorySnapshot testSnapshot;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .sku("TEST-SKU-001")
                .name("Test Product")
                .unitOfMeasure(Product.UnitOfMeasure.PIECE)
                .minStockLevel(10)
                .maxStockLevel(100)
                .status(Product.ProductStatus.ACTIVE)
                .build();

        testWarehouse = Warehouse.builder()
                .id(1L)
                .code("WH-TEST-01")
                .name("Test Warehouse")
                .status(Warehouse.WarehouseStatus.ACTIVE)
                .build();

        testSnapshot = InventorySnapshot.builder()
                .id(1L)
                .product(testProduct)
                .warehouse(testWarehouse)
                .quantityOnHand(50)
                .quantityReserved(0)
                .stockStatus(InventorySnapshot.StockStatus.OPTIMAL)
                .build();
    }

    @Nested
    @DisplayName("Process Inbound Tests")
    class ProcessInboundTests {

        @Test
        @DisplayName("Should process inbound successfully when product and warehouse exist")
        void shouldProcessInboundSuccessfully() {
            InboundRequest request = InboundRequest.builder()
                    .productId(1L)
                    .warehouseId(1L)
                    .quantity(100)
                    .referenceNumber("PO-" + UUID.randomUUID())
                    .reason("Purchase order receipt")
                    .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));
            when(snapshotRepository.findByProductIdAndWarehouseId(1L, 1L))
                    .thenReturn(Optional.of(testSnapshot));
            when(snapshotRepository.save(any(InventorySnapshot.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(eventRepository.save(any(InventoryEvent.class)))
                    .thenAnswer(invocation -> {
                        InventoryEvent event = invocation.getArgument(0);
                        event.setId(1L);
                        return event;
                    });

            InventoryEventResponse response = inventoryService.processInbound(request, "operator1");

            assertThat(response).isNotNull();
            assertThat(response.getEventType()).isEqualTo("INBOUND");
            assertThat(response.getQuantityChange()).isEqualTo(100);
            assertThat(response.getQuantityBefore()).isEqualTo(50);
            assertThat(response.getQuantityAfter()).isEqualTo(150);

            verify(snapshotRepository, times(2)).save(any(InventorySnapshot.class));
            verify(eventRepository).save(any(InventoryEvent.class));
        }

        @Test
        @DisplayName("Should create new snapshot when none exists for inbound")
        void shouldCreateNewSnapshotForInbound() {
            InboundRequest request = InboundRequest.builder()
                    .productId(1L)
                    .warehouseId(1L)
                    .quantity(25)
                    .referenceNumber("PO-NEW-001")
                    .binLocation("A-01-01")
                    .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));
            when(snapshotRepository.findByProductIdAndWarehouseId(1L, 1L))
                    .thenReturn(Optional.empty());
            when(snapshotRepository.save(any(InventorySnapshot.class)))
                    .thenAnswer(invocation -> {
                        InventorySnapshot snapshot = invocation.getArgument(0);
                        snapshot.setId(2L);
                        return snapshot;
                    });
            when(eventRepository.save(any(InventoryEvent.class)))
                    .thenAnswer(invocation -> {
                        InventoryEvent event = invocation.getArgument(0);
                        event.setId(1L);
                        return event;
                    });

            InventoryEventResponse response = inventoryService.processInbound(request, "operator1");

            assertThat(response).isNotNull();
            assertThat(response.getQuantityBefore()).isEqualTo(0);
            assertThat(response.getQuantityAfter()).isEqualTo(25);

            verify(snapshotRepository, times(3)).save(any(InventorySnapshot.class));
        }

        @Test
        @DisplayName("Should throw exception when product not found")
        void shouldThrowExceptionWhenProductNotFound() {
            InboundRequest request = InboundRequest.builder()
                    .productId(999L)
                    .warehouseId(1L)
                    .quantity(10)
                    .referenceNumber("PO-001")
                    .build();

            when(productRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> inventoryService.processInbound(request, "operator1"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Product");
        }

        @Test
        @DisplayName("Should throw exception when warehouse not found")
        void shouldThrowExceptionWhenWarehouseNotFound() {
            InboundRequest request = InboundRequest.builder()
                    .productId(1L)
                    .warehouseId(999L)
                    .quantity(10)
                    .referenceNumber("PO-001")
                    .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(warehouseRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> inventoryService.processInbound(request, "operator1"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Warehouse");
        }
    }

    @Nested
    @DisplayName("Process Outbound Tests")
    class ProcessOutboundTests {

        @Test
        @DisplayName("Should process outbound successfully when sufficient stock")
        void shouldProcessOutboundSuccessfully() {
            OutboundRequest request = OutboundRequest.builder()
                    .productId(1L)
                    .warehouseId(1L)
                    .quantity(20)
                    .referenceNumber("SO-" + UUID.randomUUID())
                    .reason("Customer order")
                    .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));
            when(snapshotRepository.findByProductIdAndWarehouseIdWithLock(1L, 1L))
                    .thenReturn(Optional.of(testSnapshot));
            when(snapshotRepository.save(any(InventorySnapshot.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(eventRepository.save(any(InventoryEvent.class)))
                    .thenAnswer(invocation -> {
                        InventoryEvent event = invocation.getArgument(0);
                        event.setId(1L);
                        return event;
                    });

            InventoryEventResponse response = inventoryService.processOutbound(request, "operator1");

            assertThat(response).isNotNull();
            assertThat(response.getEventType()).isEqualTo("OUTBOUND");
            assertThat(response.getQuantityChange()).isEqualTo(-20);
            assertThat(response.getQuantityBefore()).isEqualTo(50);
            assertThat(response.getQuantityAfter()).isEqualTo(30);
        }

        @Test
        @DisplayName("Should throw InsufficientStockException when not enough stock")
        void shouldThrowExceptionWhenInsufficientStock() {
            OutboundRequest request = OutboundRequest.builder()
                    .productId(1L)
                    .warehouseId(1L)
                    .quantity(100)
                    .referenceNumber("SO-LARGE-001")
                    .reason("Large order")
                    .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));
            when(snapshotRepository.findByProductIdAndWarehouseIdWithLock(1L, 1L))
                    .thenReturn(Optional.of(testSnapshot));

            assertThatThrownBy(() -> inventoryService.processOutbound(request, "operator1"))
                    .isInstanceOf(InsufficientStockException.class);
        }

        @Test
        @DisplayName("Should throw exception when snapshot not found for outbound")
        void shouldThrowExceptionWhenSnapshotNotFound() {
            OutboundRequest request = OutboundRequest.builder()
                    .productId(1L)
                    .warehouseId(1L)
                    .quantity(10)
                    .referenceNumber("SO-001")
                    .reason("Order")
                    .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));
            when(snapshotRepository.findByProductIdAndWarehouseIdWithLock(1L, 1L))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> inventoryService.processOutbound(request, "operator1"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("snapshot");
        }
    }

    @Nested
    @DisplayName("Get Snapshot Tests")
    class GetSnapshotTests {

        @Test
        @DisplayName("Should get snapshot successfully")
        void shouldGetSnapshotSuccessfully() {
            when(snapshotRepository.findByProductIdAndWarehouseId(1L, 1L))
                    .thenReturn(Optional.of(testSnapshot));

            InventorySnapshotResponse response = inventoryService.getSnapshot(1L, 1L);

            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getQuantityOnHand()).isEqualTo(50);
            assertThat(response.getQuantityAvailable()).isEqualTo(50);
            assertThat(response.getStockStatus()).isEqualTo("OPTIMAL");
        }

        @Test
        @DisplayName("Should throw exception when snapshot not found")
        void shouldThrowExceptionWhenSnapshotNotFound() {
            when(snapshotRepository.findByProductIdAndWarehouseId(1L, 1L))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> inventoryService.getSnapshot(1L, 1L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Stock Status Update Tests")
    class StockStatusTests {

        @Test
        @DisplayName("Should update stock status to LOW_STOCK when below threshold")
        void shouldUpdateToLowStock() {
            testSnapshot.setQuantityOnHand(5); // Below minStockLevel of 10

            InboundRequest request = InboundRequest.builder()
                    .productId(1L)
                    .warehouseId(1L)
                    .quantity(5)
                    .referenceNumber("PO-001")
                    .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));
            when(snapshotRepository.findByProductIdAndWarehouseId(1L, 1L))
                    .thenReturn(Optional.of(testSnapshot));
            when(snapshotRepository.save(any(InventorySnapshot.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(eventRepository.save(any(InventoryEvent.class)))
                    .thenAnswer(invocation -> {
                        InventoryEvent event = invocation.getArgument(0);
                        event.setId(1L);
                        return event;
                    });

            InventoryEventResponse response = inventoryService.processInbound(request, "operator1");

            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("Should update stock status to OUT_OF_STOCK when quantity is zero")
        void shouldUpdateToOutOfStock() {
            testSnapshot.setQuantityOnHand(10);

            OutboundRequest request = OutboundRequest.builder()
                    .productId(1L)
                    .warehouseId(1L)
                    .quantity(10)
                    .referenceNumber("SO-001")
                    .reason("Full depletion")
                    .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));
            when(snapshotRepository.findByProductIdAndWarehouseIdWithLock(1L, 1L))
                    .thenReturn(Optional.of(testSnapshot));
            when(snapshotRepository.save(any(InventorySnapshot.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(eventRepository.save(any(InventoryEvent.class)))
                    .thenAnswer(invocation -> {
                        InventoryEvent event = invocation.getArgument(0);
                        event.setId(1L);
                        return event;
                    });

            InventoryEventResponse response = inventoryService.processOutbound(request, "operator1");

            assertThat(response).isNotNull();
            assertThat(response.getQuantityAfter()).isEqualTo(0);
        }
    }
}
