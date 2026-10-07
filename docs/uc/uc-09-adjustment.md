# UC-09: Điều chỉnh tồn kho (Adjustment)

> **Use Case**: Điều chỉnh tồn kho
> **ID**: UC-09
> **Priority**: Medium
> **Status**: Pending

---

## 1. Thông tin chung

| Thuộc tính | Giá trị |
|------------|----------|
| **Use Case ID** | UC-09 |
| **Tên** | Điều chỉnh tồn kho (Adjustment) |
| **Mô tả** | Manager/Admin điều chỉnh số lượng tồn kho thực tế (kiểm kê) |
| **Actor** | Manager, Admin |
| **Priority** | Medium |
| **Status** | ⏳ Pending |

---

## 2. Mục đích

Adjustment được sử dụng khi:
- Kiểm kê thực tế phát hiện sai lệch
- Hàng hóa bị hư hỏng cần ghi nhận
- Điều chỉnh do lỗi hệ thống
- Reconciliation sau kiểm kê định kỳ

---

## 3. Pre-conditions & Post-conditions

### 3.1 Pre-conditions

| # | Điều kiện |
|---|-----------|
| 1 | User đã đăng nhập với role MANAGER hoặc ADMIN |
| 2 | User đã chọn sản phẩm từ Inventory |

### 3.2 Post-conditions

| # | Điều kiện |
|---|-----------|
| 1 | Event ADJUSTMENT được tạo |
| 2 | Inventory snapshot được cập nhật về giá trị mới |
| 3 | Lý do điều chỉnh được ghi nhận |

---

## 4. Main Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    MAIN FLOW                                 │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. [ACTOR] Click "Adjustment" button                    │
│     (From Product Detail Modal)                            │
│                     │                                        │
│                     ▼                                        │
│  2. [SYSTEM] Open Adjustment modal                        │
│     - Show product info                                    │
│     - Show current stock (snapshot)                        │
│     - Empty form: new quantity, reason                   │
│                     │                                        │
│                     ▼                                        │
│  3. [ACTOR] Enter new quantity                           │
│     (Quantity sau khi điều chỉnh)                        │
│                     │                                        │
│                     ▼                                        │
│  4. [SYSTEM] Calculate delta:                           │
│     delta = new_quantity - current_stock                  │
│     Display: "Change: +X" or "Change: -X"                │
│                     │                                        │
│                     ▼                                        │
│  5. [ACTOR] Enter reason for adjustment                 │
│                     │                                        │
│                     ▼                                        │
│  6. [ACTOR] Click "Confirm Adjustment"                  │
│                     │                                        │
│                     ▼                                        │
│  7. [SYSTEM] Validate:                                  │
│     - new_quantity >= 0                                  │
│     - reason not empty                                   │
│     - reason length >= 10 characters                      │
│                     │                                        │
│                     ▼                                        │
│  8. [SYSTEM] Call API: POST /api/v1/inventory/adjust   │
│     { sku, newQuantity, reason }                         │
│                     │                                        │
│                     ▼                                        │
│  9. [SYSTEM] Create ADJUSTMENT event                    │
│     - quantity_delta = new - old                         │
│     - reason = user-provided reason                       │
│                     │                                        │
│                     ▼                                        │
│  10. [SYSTEM] Show success toast                        │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 5. Form Fields

### 5.1 Read-only Info

| Field | Value | Notes |
|-------|-------|-------|
| SKU | BOSCH-SEN-001 | From selected product |
| Current Stock | 45 | Snapshot value |

### 5.2 Input Fields

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| New Quantity | Number | ✅ | >= 0, integer |
| Reason | Text | ✅ | Min 10 chars |

---

## 6. Delta Calculation

```javascript
const currentStock = 45;
const newQuantity = 40;
const delta = newQuantity - currentStock; // -5

// Display:
// Previous: 45
// New: 40
// Change: -5
```

---

## 7. Adjustment Modal UI

```
┌─────────────────────────────────────────────────────────────┐
│  🔄 Adjustment: BOSCH-SEN-001                      [X]    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  📦 BOSCH-SEN-001 • Current: 45                         │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ New Quantity (after adjustment)                     │  │
│  │ [ 40                                            ]  │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ Adjustment: -5 units                               │  │ ← Auto-calculated
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ Reason for adjustment *                              │  │
│  │ [ Physical count revealed 5 units short...        ]  │  │
│  │ Minimum 10 characters required                      │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  [Cancel]  [    Confirm Adjustment    ]                    │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 8. Validation Rules

| Rule | Validation | Error Message |
|------|------------|---------------|
| new_quantity >= 0 | Required | "Quantity cannot be negative" |
| reason.length >= 10 | Required | "Please provide a detailed reason" |
| reason not empty | Required | "Reason is required" |

---

## 9. API Contract

### 9.1 Request

```
POST /api/v1/inventory/adjust
Authorization: Bearer {token}
Content-Type: application/json

{
  "sku": "BOSCH-SEN-001",
  "newQuantity": 40,
  "reason": "Physical count revealed 5 units short during monthly audit",
  "warehouseId": 1
}
```

### 9.2 Response

```json
{
  "success": true,
  "data": {
    "eventId": 125,
    "type": "ADJUSTMENT",
    "sku": "BOSCH-SEN-001",
    "quantityDelta": -5,
    "balanceAfter": 40,
    "reason": "Physical count revealed 5 units short during monthly audit",
    "operator": "manager@bosch.com",
    "timestamp": "2024-01-15T14:00:00Z"
  }
}
```

---

## 10. Use Cases cho Adjustment

### 10.1 Cycle Count Reconciliation

```
Scenario: Monthly physical inventory count
1. Staff count physical items: 42 units
2. System shows: 45 units
3. Manager creates adjustment: 42 units
4. Reason: "Monthly cycle count - January 2024"
```

### 10.2 Damage Write-off

```
Scenario: Water damage during storage
1. Manager inspects damaged goods
2. Damaged items: 8 units
3. Creates adjustment to 0
4. Reason: "Water damage from roof leak - 8 units written off"
```

### 10.3 Stock Correction

```
Scenario: System error caused incorrect stock
1. Audit finds discrepancy
2. Manager verifies actual count
3. Creates adjustment to correct value
4. Reason: "Correcting system error from stock transfer #123"
```

---

## 11. Business Rules Applied

| Rule ID | Rule | Applied In |
|---------|------|-----------|
| BR-TXN-001 | Transaction Atomicity | Step 9 |
| BR-EVT-010 | Event Immutability | Event creation |
| BR-EVT-001 | Event Type: ADJUSTMENT | Event classification |

---

## 12. Acceptance Criteria

| # | Criteria | Expected Result |
|---|----------|-----------------|
| AC-01 | Manager can adjust | Adjustment modal opens |
| AC-02 | Staff cannot adjust | No adjustment button |
| AC-03 | Delta calculation | Shows +/- change |
| AC-04 | Empty reason | Validation error |
| AC-05 | Short reason | Validation error |
| AC-06 | New stock | Updated in snapshot |
| AC-07 | Event log | Shows ADJUSTMENT type |

---

## 13. Related UCs

| Related UC | Relationship |
|------------|-------------|
| [UC-08: Event Log](./uc-08-event-log.md) | ADJUSTMENT appears in Event Log |

---

** Quay lại**: [UC-08](./uc-08-event-log.md) | ** Quay về**: [UC Index](./index.md)
