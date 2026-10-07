# UC-05: Thực hiện Inbound

> **Use Case**: Nhập kho (Inbound)
> **ID**: UC-05
> **Priority**: High
> **Status**: Done

---

## 1. Thông tin chung

| Thuộc tính | Giá trị |
|------------|----------|
| **Use Case ID** | UC-05 |
| **Tên** | Thực hiện Inbound |
| **Mô tả** | User nhập số lượng hàng hóa vào kho |
| **Actor** | Staff, Manager, Admin |
| **Priority** | High |
| **Status** | ✅ Done |

---

## 2. Pre-conditions & Post-conditions

### 2.1 Pre-conditions

| # | Điều kiện |
|---|-----------|
| 1 | User đã chọn sản phẩm từ Inventory |
| 2 | User có quyền thực hiện inbound |

### 2.2 Post-conditions

| # | Điều kiện |
|---|-----------|
| 1 | Event INBOUND được tạo |
| 2 | Inventory snapshot được cập nhật |
| 3 | Toast notification hiển thị |

---

## 3. Main Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    MAIN FLOW                                 │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. [ACTOR] Click "Inbound" button                      │
│                     │                                        │
│                     ▼                                        │
│  2. [SYSTEM] Open Inbound modal                          │
│     - Show product info (SKU, current stock)             │
│     - Empty form: quantity, reference number              │
│                     │                                        │
│                     ▼                                        │
│  3. [ACTOR] Enter quantity                              │
│                     │                                        │
│                     ▼                                        │
│  4. [ACTOR] Enter reference number                      │
│                     │                                        │
│                     ▼                                        │
│  5. [ACTOR] Click "Confirm Inbound"                     │
│                     │                                        │
│                     ▼                                        │
│  6. [SYSTEM] Validate input:                            │
│     - quantity > 0                                      │
│     - quantity is integer                                │
│     - reference_number not empty                         │
│                     │                                        │
│                     ▼                                        │
│  7. [SYSTEM] Call API: POST /api/v1/inventory/inbound   │
│     { sku, quantity, reference_number }                   │
│                     │                                        │
│                     ▼                                        │
│  8. [SYSTEM] Backend: BEGIN TRANSACTION                │
│     - INSERT inventory_events                            │
│     - UPDATE inventory_snapshots                         │
│     - COMMIT                                            │
│                     │                                        │
│                     ▼                                        │
│  9. [SYSTEM] Show success toast                        │
│                     │                                        │
│                     ▼                                        │
│  10. [SYSTEM] Close modal                              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Form Fields

### 4.1 Read-only Info

| Field | Value | Notes |
|-------|-------|-------|
| SKU | BOSCH-SEN-001 | From selected product |
| Current Stock | 45 | For reference |

### 4.2 Input Fields

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| Quantity | Number | ✅ | > 0, integer |
| Reference Number | Text | ✅ | Not empty |

---

## 5. Validation Rules

| Rule ID | Rule | Error Message |
|---------|------|---------------|
| BR-INV-001 | quantity > 0 | "Quantity must be greater than 0" |
| BR-INV-001 | quantity is integer | "Quantity must be a whole number" |
| BR-INV-001 | reference_number not empty | "Reference number is required" |

---

## 6. UI Specifications

### 6.1 Modal Layout

```
┌─────────────────────────────────────┐
│  📥 Inbound                  [X]    │ ← Header
├─────────────────────────────────────┤
│                                     │
│  📦 BOSCH-SEN-001 • Current: 45   │ ← Product info
│                                     │
│  ┌─────────────────────────────────┐│
│  │ Quantity                       ││ ← Input
│  │ [                          ]  ││
│  └─────────────────────────────────┘│
│                                     │
│  ┌─────────────────────────────────┐│
│  │ Reference Number               ││ ← Input
│  │ [ e.g., PO-2024-0123       ]  ││
│  └─────────────────────────────────┘│
│                                     │
├─────────────────────────────────────┤
│                                     │
│  [Cancel]  [Confirm Inbound]       │ ← Buttons
│                                     │
└─────────────────────────────────────┘
```

### 6.2 Button States

| State | Button Style |
|-------|-------------|
| Default | bg-emerald-600, hover:bg-emerald-500 |
| Disabled | bg-slate-700, text-slate-500 |
| Loading | "Processing..." + spinner |

---

## 7. API Contract

### 7.1 Request

```
POST /api/v1/inventory/inbound
Authorization: Bearer {token}
Content-Type: application/json

{
  "sku": "BOSCH-SEN-001",
  "quantity": 50,
  "referenceNumber": "PO-2024-0123",
  "warehouseId": 1
}
```

### 7.2 Success Response

```json
{
  "success": true,
  "data": {
    "eventId": 123,
    "type": "INBOUND",
    "sku": "BOSCH-SEN-001",
    "quantity": 50,
    "balanceAfter": 95,
    "timestamp": "2024-01-15T10:30:00Z"
  }
}
```

### 7.3 Error Response

```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Quantity must be greater than 0"
  }
}
```

---

## 8. Business Rules Applied

| Rule ID | Rule | Applied In |
|---------|------|-----------|
| BR-INV-001 | Inbound Validation | Step 6 |
| BR-TXN-001 | Transaction Atomicity | Step 8 |
| BR-EVT-010 | Event Immutability | Step 8 |

---

## 9. Acceptance Criteria

| # | Criteria | Expected Result |
|---|----------|-----------------|
| AC-01 | Valid input | Submit success, show toast |
| AC-02 | quantity = 0 | Show error, disable submit |
| AC-03 | Empty reference | Show error, disable submit |
| AC-04 | Loading state | Show "Processing..." |
| AC-05 | Success | Close modal, refresh list |
| AC-06 | New stock | Reflected in product list |

---

** Quay lại**: [UC-04](./uc-04-product-detail.md) | **Tiếp theo**: [UC-06](./uc-06-outbound.md)
