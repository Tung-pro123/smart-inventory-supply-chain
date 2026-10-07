# UC-06: Thực hiện Outbound

> **Use Case**: Xuất kho (Outbound)
> **ID**: UC-06
> **Priority**: High
> **Status**: Done

---

## 1. Thông tin chung

| Thuộc tính | Giá trị |
|------------|----------|
| **Use Case ID** | UC-06 |
| **Tên** | Thực hiện Outbound |
| **Mô tả** | User xuất số lượng hàng hóa ra khỏi kho |
| **Actor** | Staff, Manager, Admin |
| **Priority** | High |
| **Status** | ✅ Done |

---

## 2. Pre-conditions & Post-conditions

### 2.1 Pre-conditions

| # | Điều kiện |
|---|-----------|
| 1 | User đã chọn sản phẩm từ Inventory |
| 2 | Sản phẩm có stock > 0 |

### 2.2 Post-conditions

| # | Điều kiện |
|---|-----------|
| 1 | Event OUTBOUND được tạo |
| 2 | Inventory snapshot được cập nhật |
| 3 | Toast notification hiển thị |

---

## 3. Main Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    MAIN FLOW                                 │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. [ACTOR] Click "Outbound" button                     │
│                     │                                        │
│                     ▼                                        │
│  2. [SYSTEM] Open Outbound modal                         │
│     - Show product info (SKU, available stock)            │
│     - Empty form: quantity, reference number              │
│                     │                                        │
│                     ▼                                        │
│  3. [ACTOR] Enter quantity                              │
│                     │                                        │
│                     ▼                                        │
│  4. [SYSTEM] Real-time validation:                      │
│     IF quantity > available_stock                        │
│     → Show error "Insufficient stock"                   │
│     → Disable submit button                              │
│                     │                                        │
│                     ▼                                        │
│  5. [ACTOR] Enter reference number                      │
│                     │                                        │
│                     ▼                                        │
│  6. [ACTOR] Click "Confirm Outbound"                    │
│                     │                                        │
│                     ▼                                        │
│  7. [SYSTEM] Validate input                             │
│     - quantity > 0                                      │
│     - quantity <= available_stock                       │
│     - reference_number not empty                        │
│                     │                                        │
│                     ▼                                        │
│  8. [SYSTEM] Call API: POST /api/v1/inventory/outbound │
│     { sku, quantity, reference_number }                   │
│                     │                                        │
│                     ▼                                        │
│  9. [SYSTEM] Backend: BEGIN TRANSACTION                 │
│     - Check stock (prevent race condition)               │
│     - INSERT inventory_events                           │
│     - UPDATE inventory_snapshots                        │
│     - COMMIT                                            │
│                     │                                        │
│                     ▼                                        │
│  10. [SYSTEM] Show success toast                        │
│                     │                                        │
│                     ▼                                        │
│  11. [SYSTEM] Close modal                               │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Form Fields

### 4.1 Read-only Info

| Field | Value | Notes |
|-------|-------|-------|
| SKU | BOSCH-SEN-001 | From selected product |
| Available Stock | 45 | Current stock for reference |

### 4.2 Input Fields

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| Quantity | Number | ✅ | > 0, integer, <= stock |
| Reference Number | Text | ✅ | Not empty |

---

## 5. Validation Rules

| Rule ID | Rule | Error Message |
|---------|------|---------------|
| BR-INV-001 | quantity > 0 | "Quantity must be greater than 0" |
| BR-INV-001 | quantity <= available_stock | "Insufficient stock. Available: {n}" |
| BR-INV-001 | reference_number not empty | "Reference number is required" |

---

## 6. Real-time Validation UX

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ Quantity                                           │  │
│  │ [ 100                                          ]  │  │
│  │ ⚠️ Insufficient stock. Available: 45            │  │ ← Error
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
│  Reference Number                                          │
│  [ SO-2024-0089                                        ]  │
│                                                             │
│  [Cancel]  [    Confirm Outbound (Disabled)    ]          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 7. Concurrency Handling

### 7.1 Race Condition Scenario

```
User A opens modal: stock = 50
User B processes outbound: 50 units → stock = 0
User A submits: tries to outbound 30 units
→ Should fail: "Insufficient stock"
```

### 7.2 Backend Handling

```java
// Optimistic locking + stock check in transaction
@Transactional
public OutboundResult processOutbound(OutboundRequest request) {
    // Lock the row
    InventorySnapshot snapshot = repository.findBySkuForUpdate(request.getSku());
    
    if (snapshot.getQuantity() < request.getQuantity()) {
        throw new InsufficientStockException(
            "Insufficient stock. Available: " + snapshot.getQuantity()
        );
    }
    
    // Proceed with update
    // ...
}
```

---

## 8. API Contract

### 8.1 Request

```
POST /api/v1/inventory/outbound
Authorization: Bearer {token}
Content-Type: application/json

{
  "sku": "BOSCH-BRK-004",
  "quantity": 8,
  "referenceNumber": "SO-2024-0089",
  "warehouseId": 1
}
```

### 8.2 Success Response

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

### 8.3 Error Response (Insufficient Stock)

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

## 9. Business Rules Applied

| Rule ID | Rule | Applied In |
|---------|------|-----------|
| BR-INV-002 | Outbound Validation | Step 7 |
| BR-TXN-001 | Transaction Atomicity | Step 9 |
| BR-TXN-010 | Optimistic Locking | Step 9 |
| BR-EVT-010 | Event Immutability | Step 9 |

---

## 10. Acceptance Criteria

| # | Criteria | Expected Result |
|---|----------|-----------------|
| AC-01 | quantity <= stock | Submit success |
| AC-02 | quantity > stock | Show error, disable submit |
| AC-03 | quantity = 0 | Show error, disable submit |
| AC-04 | Empty reference | Show error, disable submit |
| AC-05 | Concurrent update | One succeeds, one fails |
| AC-06 | New stock | Reflected in product list |

---

** Quay lại**: [UC-05](./uc-05-inbound.md) | **Tiếp theo**: [UC-07](./uc-07-low-stock.md)
