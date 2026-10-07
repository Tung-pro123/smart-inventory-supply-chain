# UC-08: Xem Event Log

> **Use Case**: Xem nhật ký sự kiện (Audit Trail)
> **ID**: UC-08
> **Priority**: High
> **Status**: Done

---

## 1. Thông tin chung

| Thuộc tính | Giá trị |
|------------|----------|
| **Use Case ID** | UC-08 |
| **Tên** | Xem Event Log |
| **Mô tả** | Manager/Admin xem lịch sử tất cả thay đổi tồn kho |
| **Actor** | Manager, Admin |
| **Priority** | High |
| **Status** | ✅ Done |

---

## 2. Thông tin chung

| Thuộc tính | Giá trị |
|------------|----------|
| **Use Case ID** | UC-08 |
| **Tên** | Xem Event Log |
| **Mô tả** | Manager/Admin xem audit trail |
| **Actor** | Manager, Admin |
| **Priority** | High |
| **Status** | ✅ Done |

---

## 3. Access Control

```
┌─────────────────────────────────────────────────────────────┐
│                    ACCESS CONTROL                           │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. User navigate to /events                            │
│                     │                                        │
│                     ▼                                        │
│  2. [SYSTEM] Check user role                            │
│                     │                                        │
│                     ▼                                        │
│  3. IF role = STAFF                                    │
│     → Redirect to /inventory                            │
│                                                              │
│  4. IF role = MANAGER or ADMIN                          │
│     → Show Event Log page                                │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Event Types

| Type | Icon | Color | Description |
|------|------|-------|-------------|
| **INBOUND** | ArrowDownLeft | Emerald | Nhập hàng vào kho |
| **OUTBOUND** | ArrowUpRight | Indigo | Xuất hàng ra khỏi kho |
| **ADJUSTMENT** | RefreshCw | Amber | Điều chỉnh tồn kho (kiểm kê) |
| **DAMAGE** | AlertTriangle | Rose | Hàng hư hỏng/hỏng |

---

## 5. Event Card Structure

```
┌─────────────────────────────────────────────────────────────┐
│  ┌───────┐                                                 │
│  │  ↓   │  [INBOUND]  BOSCH-SEN-001                      │
│  │  50  │  Industrial Radar Distance Sensor               │
│  └───────┘                                                 │
│             nguyen.van.a • 2024-01-15 09:23 • PO-2024-0123 │
│                                                             │
│                                          +50  →  95       │
│                                        ┗━━━━━━━┛           │
└─────────────────────────────────────────────────────────────┘
```

---

## 6. Filter Chips

### 6.1 Available Filters

| Filter | Shows | Count Example |
|--------|-------|---------------|
| ALL | Tất cả events | (47) |
| INBOUND | Chỉ inbound | (20) |
| OUTBOUND | Chỉ outbound | (18) |
| ADJUSTMENT | Chỉ adjustment | (9) |

### 6.2 UI

```
┌─────────────────────────────────────────────────────────────┐
│  🔍 Filter:                                               │
│                                                             │
│  [All (47)] [Inbound (20)] [Outbound (18)] [Adj (9)]    │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 7. Immutable Notice

```
┌─────────────────────────────────────────────────────────────┐
│  🔒 Events cannot be edited or deleted                     │
└─────────────────────────────────────────────────────────────┘
```

> **Lưu ý**: Events là append-only, không có edit/delete buttons

---

## 8. API Contract

### 8.1 Request

```
GET /api/v1/inventory/events
GET /api/v1/inventory/events?type=INBOUND
GET /api/v1/inventory/events?startDate=2024-01-01&endDate=2024-01-15
GET /api/v1/inventory/events?sku=BOSCH-SEN-001
Authorization: Bearer {token}
```

### 8.2 Response

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "type": "INBOUND",
        "sku": "BOSCH-SEN-001",
        "productName": "Industrial Radar Distance Sensor",
        "quantityDelta": 50,
        "balanceAfter": 95,
        "referenceNumber": "PO-2024-0123",
        "operator": "nguyen.van.a",
        "timestamp": "2024-01-15T09:23:00Z"
      }
    ],
    "totalElements": 47,
    "totalPages": 5
  }
}
```

---

## 9. Pagination

| Setting | Value |
|---------|-------|
| Default page size | 20 |
| Max page size | 100 |
| Default date range | Last 7 days |
| Max date range | 90 days |

---

## 10. Event Fields

| Field | Description | Example |
|-------|-------------|---------|
| ID | Unique event ID | 123 |
| Type | Event type | INBOUND |
| SKU | Product SKU | BOSCH-SEN-001 |
| Product Name | Tên sản phẩm | Industrial Radar... |
| Quantity Delta | Số lượng thay đổi | +50, -8 |
| Balance After | Tồn kho sau event | 95 |
| Reference Number | Số tham chiếu | PO-2024-0123 |
| Operator | Người thực hiện | nguyen.van.a |
| Timestamp | Thời gian | 2024-01-15T09:23:00Z |

---

## 11. Empty State

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│              📋 No events in this period                    │
│                                                             │
│          Try adjusting your date range or filters            │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 12. Business Rules Applied

| Rule ID | Rule | Applied In |
|---------|------|-----------|
| BR-PER-001 | Permission: Event Log view | Access control |
| BR-EVT-001 | Event Type Visualization | Card styling |
| BR-EVT-010 | Event Immutability | No edit/delete |
| BR-RPT-001 | Date Range Filter | Default 7 days |

---

## 13. Acceptance Criteria

| # | Criteria | Expected Result |
|---|----------|-----------------|
| AC-01 | Manager access | See Event Log page |
| AC-02 | Staff access | Redirect or 403 |
| AC-03 | Filter by type | Show matching events |
| AC-04 | Event card | Show all fields |
| AC-05 | No edit button | Events are immutable |
| AC-06 | Date filter | Filter by date range |
| AC-07 | Empty filter | Show "No events" message |

---

** Quay lại**: [UC-07](./uc-07-low-stock.md) | **Tiếp theo**: [UC-09](./uc-09-adjustment.md)
