# UC-07: Xem Low Stock Alerts

> **Use Case**: Xem cảnh báo hàng sắp hết
> **ID**: UC-07
> **Priority**: High
> **Status**: Done

---

## 1. Thông tin chung

| Thuộc tính | Giá trị |
|------------|----------|
| **Use Case ID** | UC-07 |
| **Tên** | Xem Low Stock Alerts |
| **Mô tả** | Manager/Admin xem danh sách sản phẩm dưới ngưỡng |
| **Actor** | Manager, Admin |
| **Priority** | High |
| **Status** | ✅ Done |

---

## 2. Pre-conditions & Post-conditions

### 2.1 Pre-conditions

| # | Điều kiện |
|---|-----------|
| 1 | User đã đăng nhập |
| 2 | User có role MANAGER hoặc ADMIN |
| 3 | Có ít nhất 1 sản phẩm dưới ngưỡng |

### 2.2 Post-conditions

| # | Điều kiện |
|---|-----------|
| 1 | Danh sách alerts được hiển thị |
| 2 | User có thể click để tạo đơn nhập |

---

## 3. Access Control

```
┌─────────────────────────────────────────────────────────────┐
│                    ACCESS FLOW                               │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. User navigate to /low-stock                         │
│                     │                                        │
│                     ▼                                        │
│  2. [SYSTEM] Check user role                            │
│                     │                                        │
│                     ▼                                        │
│  3. IF role = STAFF                                    │
│     → Redirect to /inventory (or show 403)              │
│                                                              │
│  4. IF role = MANAGER or ADMIN                          │
│     → Show Low Stock page                               │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Alert Card Structure

```
┌─────────────────────────────────────────────────────────────┐
│  ┌───────┐                                                 │
│  │  ⚠️  │  BOSCH-ECU-002 • [HIGH]                        │
│  │       │  Automotive Electronic Control Unit             │
│  └───────┘                                                 │
│                                                             │
│                                        8                    │
│                                      of 10                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│   │
│  └─────────────────────────────────────────────────────┘   │
│  Progress: 80% → HIGH URGENCY                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 5. Urgency Levels

| Level | Condition | Color | Icon |
|-------|-----------|-------|------|
| **CRITICAL** | stock = 0 OR stock < threshold * 0.3 | Rose | AlertTriangle |
| **HIGH** | stock < threshold * 0.6 | Amber | AlertTriangle |
| **MEDIUM** | stock <= threshold | Yellow | AlertTriangle |

---

## 6. Progress Bar Calculation

```javascript
const progress = Math.max((stock / threshold) * 100, 5);

// Example:
// stock = 8, threshold = 10
// progress = (8 / 10) * 100 = 80%
// Display: 80% filled bar with HIGH urgency color
```

---

## 7. Reorder Modal

### 7.1 Trigger

User click vào alert card → Open Reorder modal

### 7.2 Modal Content

```
┌─────────────────────────────────────┐
│  📦 Reorder: BOSCH-ECU-002   [X]  │
├─────────────────────────────────────┤
│                                     │
│  Current Stock:     8              │
│  Threshold:        10               │
│  Suggested Order: 12 units          │ ← threshold * 2 - stock
│                                     │
│  ┌─────────────────────────────────┐│
│  │ Order Quantity                  ││
│  │ [ 12                         ]  ││
│  └─────────────────────────────────┘│
│                                     │
├─────────────────────────────────────┤
│                                     │
│  [    Create Inbound Order    ]     │
│                                     │
└─────────────────────────────────────┘
```

### 7.3 Suggested Quantity Formula

```
suggested_quantity = (threshold * 2) - current_stock

// Example:
// current_stock = 8, threshold = 10
// suggested = (10 * 2) - 8 = 12 units
```

---

## 8. API Contract

### 8.1 Request

```
GET /api/v1/products/low-stock
Authorization: Bearer {token}
```

### 8.2 Response

```json
{
  "success": true,
  "data": [
    {
      "id": 2,
      "sku": "BOSCH-ECU-002",
      "name": "Automotive Electronic Control Unit",
      "stock": 8,
      "threshold": 10,
      "urgency": "HIGH",
      "urgencyPriority": 2
    },
    {
      "id": 4,
      "sku": "BOSCH-BRK-004",
      "name": "High-Performance Brake Caliper Pad",
      "stock": 14,
      "threshold": 25,
      "urgency": "HIGH",
      "urgencyPriority": 2
    }
  ]
}
```

---

## 9. Empty State

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│                  ✅ All items well stocked                  │
│                                                             │
│            No products are below their threshold.           │
│                    Everything looks good!                    │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 10. Business Rules Applied

| Rule ID | Rule | Applied In |
|---------|------|-----------|
| BR-PER-001 | Permission: Low Stock view | Access control |
| BR-INV-020 | Urgency Calculation | Card styling |
| BR-ALR-001 | Alert Generation | Alert creation |

---

## 11. Acceptance Criteria

| # | Criteria | Expected Result |
|---|----------|-----------------|
| AC-01 | Manager access | See Low Stock page |
| AC-02 | Staff access | Redirect or 403 |
| AC-03 | Critical items | Show rose badge |
| AC-04 | High items | Show amber badge |
| AC-05 | Click card | Open Reorder modal |
| AC-06 | Suggested qty | Auto-calculated |
| AC-07 | Empty list | Show success message |

---

** Quay lại**: [UC-06](./uc-06-outbound.md) | **Tiếp theo**: [UC-08](./uc-08-event-log.md)
