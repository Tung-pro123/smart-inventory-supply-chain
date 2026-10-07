# US-02: Thực hiện Outbound

> **User Story**: Xuất kho hàng
> **ID**: US-02
> **Priority**: Must
> **Story Points**: 3

---

## 1. Story

```
┌─────────────────────────────────────────────────────────────┐
│  AS A     Warehouse Staff                                │
│  I WANT   Xuất hàng hóa ra khỏi kho                   │
│  SO THAT  Giao hàng cho đơn hàng hoặc sản xuất         │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. Mô tả nghiệp vụ

### 2.1 Bối cảnh

Nhân viên kho cần xuất hàng để:
- Giao cho đơn hàng của khách hàng (S/O)
- Cung cấp nguyên vật liệu cho sản xuất
- Chuyển hàng giữa các warehouses

### 2.2 User Flow

```
1. Nhận yêu cầu xuất hàng (S/O hoặc production order)
        ↓
2. Kiểm tra stock trong hệ thống
        ↓
3. Nếu đủ stock → Tiến hành xuất
        ↓
4. Mở app → Inventory → Tìm sản phẩm
        ↓
5. Click "Outbound"
        ↓
6. Nhập số lượng + Reference Number (S/O)
        ↓
7. Hệ thống kiểm tra:
   - Nếu đủ stock → Cho phép submit
   - Nếu không đủ → Báo lỗi
        ↓
8. Xác nhận → Cập nhật tồn kho
```

---

## 3. Acceptance Criteria

### 3.1 Core Criteria

```
☐ AC-01: User có thể mở form outbound từ Product Detail
☐ AC-02: Form hiển thị SKU (readonly) và available stock
☐ AC-03: Input quantity với validation
☐ AC-04: Input reference number (S/O number)
☐ AC-05: Submit tạo event OUTBOUND trong database
☐ AC-06: Submit cập nhật inventory snapshot
☐ AC-07: Toast notification thành công
☐ AC-08: Modal đóng sau khi submit thành công
☐ AC-09: Product list tự cập nhật stock mới
```

### 3.2 Validation Criteria

```
☐ AC-10: Không cho submit nếu quantity = 0
☐ AC-11: Không cho submit nếu quantity > available stock
☐ AC-12: Không cho submit nếu reference number trống
☐ AC-13: Real-time validation: hiển thị lỗi ngay khi nhập
☐ AC-14: Submit button disabled khi có lỗi validation
```

### 3.3 Stock Check Criteria

```
☐ AC-15: Nếu stock = 0 → Không cho outbound
☐ AC-16: Nếu quantity > stock → Hiển thị "Insufficient stock"
☐ AC-17: Error hiển thị số stock khả dụng
```

---

## 4. UI Specifications

### 4.1 Outbound Modal

```
┌─────────────────────────────────────────────────────────────┐
│  📤 Outbound                                       [X]    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐│
│  │ 📦 BOSCH-BRK-004 • Available: 14 units            ││
│  └─────────────────────────────────────────────────────┘│
│                                                             │
│  ┌─────────────────────────────────────────────────────┐│
│  │ Quantity *                                          ││
│  │ [                                                 ]││
│  │ ⚠️ Insufficient stock. Available: 14            ││
│  └─────────────────────────────────────────────────────┘│
│                                                             │
│  ┌─────────────────────────────────────────────────────┐│
│  │ Reference Number (S/O) *                            ││
│  │ [ SO-2024-0089                                ]    ││
│  └─────────────────────────────────────────────────────┘│
│                                                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  [Cancel]              [    Confirm Outbound   ]           │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 Error State

```
┌─────────────────────────────────────────────────────────────┐
│  ⚠️ Insufficient stock                                   │
│                                                             │
│  Requested: 20 units                                     │
│  Available: 14 units                                     │
│                                                             │
│  Please adjust quantity to 14 or less.                    │
└─────────────────────────────────────────────────────────────┘
```

---

## 5. Concurrency Handling

### 5.1 Scenario: Race Condition

```
Tình huống:
- User A mở outbound modal cho SKU-001, stock = 50
- User B đồng thời xuất 50 units → stock = 0
- User A submit 20 units → FAILED (stock = 0)

Hệ thống cần:
1. Lock row khi bắt đầu transaction
2. Kiểm tra stock lại trước khi update
3. Nếu không đủ → Rollback và báo lỗi
```

### 5.2 Backend Implementation

```java
@Transactional
public OutboundResult processOutbound(OutboundRequest request) {
    // 1. Lock row with SELECT FOR UPDATE
    InventorySnapshot snapshot = repository.findBySkuForUpdate(request.getSku());
    
    // 2. Re-check stock
    if (snapshot.getQuantity() < request.getQuantity()) {
        throw new InsufficientStockException(
            "Insufficient stock. Available: " + snapshot.getQuantity()
        );
    }
    
    // 3. Proceed with update
    return inventoryService.processOutbound(request);
}
```

---

## 6. API Contract

### 6.1 Endpoint

```
POST /api/v1/inventory/outbound
```

### 6.2 Request

```json
{
  "sku": "BOSCH-BRK-004",
  "quantity": 8,
  "referenceNumber": "SO-2024-0089",
  "warehouseId": 1
}
```

### 6.3 Success Response

```json
{
  "success": true,
  "data": {
    "eventId": 124,
    "type": "OUTBOUND",
    "sku": "BOSCH-BRK-004",
    "quantity": -8,
    "balanceAfter": 6,
    "timestamp": "2024-01-15T10:45:00Z"
  }
}
```

### 6.4 Error Response

```json
{
  "success": false,
  "error": {
    "code": "INSUFFICIENT_STOCK",
    "message": "Insufficient stock. Available: 6",
    "details": {
      "requested": 8,
      "available": 6
    }
  }
}
```

---

## 7. Business Rules Applied

| Rule ID | Rule | Applied |
|---------|------|---------|
| BR-INV-002 | Outbound validation | AC-10, AC-11, AC-12 |
| BR-TXN-001 | Transaction atomicity | AC-05, AC-06 |
| BR-TXN-010 | Optimistic locking | Concurrency |
| BR-EVT-010 | Event immutability | AC-05 |

---

## 8. Related UCs

| UC | Relationship |
|----|-------------|
| [UC-04: Product Detail](./uc-04-product-detail.md) | Entry point |
| [UC-06: Outbound](./uc-06-outbound.md) | Technical flow |

---

## 9. Test Scenarios

| # | Scenario | Expected Result |
|---|----------|-----------------|
| TS-01 | Submit qty=8, stock=14 | Success, stock -8 |
| TS-02 | Submit qty=20, stock=14 | Error, "Insufficient stock" |
| TS-03 | Submit qty=0 | Error, "Quantity > 0" |
| TS-04 | Submit without ref | Error, "Ref required" |
| TS-05 | Concurrent outbound | One succeeds, one fails |

---

** Quay lại**: [US-01](./us-01-inbound.md) | **Tiếp theo**: [US-03](./us-03-low-stock.md)
