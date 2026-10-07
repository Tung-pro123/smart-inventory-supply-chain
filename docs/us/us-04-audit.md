# US-04: Kiểm tra Audit Trail

> **User Story**: Kiểm tra lịch sử thay đổi
> **ID**: US-04
> **Priority**: Should
> **Story Points**: 2

---

## 1. Story

```
┌─────────────────────────────────────────────────────────────┐
│  AS A     Warehouse Manager                               │
│  I WANT   Xem lịch sử mọi thay đổi tồn kho             │
│  SO THAT  Đối soát, phát hiện sai sót và bảo mật       │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. Mô tả nghiệp vụ

### 2.1 Bối cảnh

Quản lý kho cần kiểm tra audit trail để:
- Đối soát số lượng với thực tế
- Phát hiện sai sót hoặc gian lận
- Theo dõi hoạt động của nhân viên
- Báo cáo cho quản lý cấp cao

### 2.2 User Flow

```
1. Login với role MANAGER
        ↓
2. Navigate to "Event Log" (Events tab)
        ↓
3. Xem danh sách events (default: 7 ngày gần nhất)
        ↓
4. Filter theo loại event nếu cần
        ↓
5. Kiểm tra chi tiết từng event
        ↓
6. Export nếu cần (future feature)
```

---

## 3. Acceptance Criteria

### 3.1 Access Criteria

```
☐ AC-01: MANAGER có thể truy cập Event Log
☐ AC-02: ADMIN có thể truy cập Event Log
☐ AC-03: STAFF bị chặn (redirect hoặc 403)
☐ AC-04: Tab "Events" chỉ hiển thị với Manager+
```

### 3.2 Display Criteria

```
☐ AC-05: Hiển thị danh sách events
☐ AC-06: Default hiển thị 7 ngày gần nhất
☐ AC-07: Mỗi event hiển thị:
         - Type (INBOUND/OUTBOUND/ADJUSTMENT)
         - SKU
         - Product name
         - Quantity change (+/-)
         - Balance after
         - Operator
         - Timestamp
         - Reference number
☐ AC-08: Sắp xếp theo timestamp giảm dần (mới nhất trước)
```

### 3.3 Filter Criteria

```
☐ AC-09: Filter "All" - hiển thị tất cả
☐ AC-10: Filter "Inbound" - chỉ INBOUND events
☐ AC-11: Filter "Outbound" - chỉ OUTBOUND events
☐ AC-12: Filter "Adjustment" - chỉ ADJUSTMENT events
☐ AC-13: Filter chips hiển thị số lượng mỗi loại
```

### 3.4 Immutability Criteria

```
☐ AC-14: KHÔNG có nút Edit cho events
☐ AC-15: KHÔNG có nút Delete cho events
☐ AC-16: Có notice "Events cannot be edited or deleted"
```

---

## 4. Event Types

| Type | Icon | Color | Shows |
|------|------|-------|-------|
| **INBOUND** | ArrowDownLeft | Emerald | +quantity |
| **OUTBOUND** | ArrowUpRight | Indigo | -quantity |
| **ADJUSTMENT** | RefreshCw | Amber | +/-quantity |
| **DAMAGE** | AlertTriangle | Rose | -quantity |

---

## 5. UI Specifications

### 5.1 Event Log Page

```
┌─────────────────────────────────────────────────────────────┐
│  📋 Event Log                               47 events       │
├─────────────────────────────────────────────────────────────┤
│  🔒 Events cannot be edited or deleted                       │
├─────────────────────────────────────────────────────────────┤
│  Filter: [All(47)] [Inbound(20)] [Outbound(18)] [Adj(9)]│
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐│
│  │ ⬛ [INBOUND] BOSCH-SEN-001                        ││
│  │     Industrial Radar Distance Sensor                  ││
│  │     nguyen.van.a • 2024-01-15 09:23 • PO-2024-0123 ││
│  │                                          +50  →  95  ││
│  └─────────────────────────────────────────────────────┘│
│                                                             │
│  ┌─────────────────────────────────────────────────────┐│
│  │ ⬛ [OUTBOUND] BOSCH-BRK-004                        ││
│  │     High-Performance Brake Caliper Pad               ││
│  │     tran.thi.b • 2024-01-15 10:45 • SO-2024-0089   ││
│  │                                          -8   →   6  ││
│  └─────────────────────────────────────────────────────┘│
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 5.2 Event Card Details

```
┌─────────────────────────────────────────────────────────────┐
│  ┌───────┐                                                 │
│  │  ↓   │  [INBOUND]  BOSCH-SEN-001                      │
│  │  50  │                                                 │
│  └───────┘                                                 │
│                                                             │
│  Industrial Radar Distance Sensor                           │
│                                                             │
│  Operator:     nguyen.van.a                               │
│  Timestamp:    2024-01-15 09:23                          │
│  Reference:    PO-2024-0123                              │
│                                                             │
│  ┌──────────────────┐    ┌──────────────────┐            │
│  │      +50         │ →  │      95         │            │
│  │   quantity       │    │   balance       │            │
│  └──────────────────┘    └──────────────────┘            │
└─────────────────────────────────────────────────────────────┘
```

---

## 6. Immutable Notice

```
┌─────────────────────────────────────────────────────────────┐
│  🔒 Events cannot be edited or deleted                      │
│                                                             │
│  This is an immutable audit trail.                          │
│  All changes are permanently recorded.                      │
└─────────────────────────────────────────────────────────────┘
```

---

## 7. API Contract

### 7.1 Endpoint

```
GET /api/v1/inventory/events
GET /api/v1/inventory/events?type=INBOUND
GET /api/v1/inventory/events?startDate=2024-01-01&endDate=2024-01-15
```

### 7.2 Response

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
    "page": 0,
    "size": 20
  }
}
```

---

## 8. Business Rules Applied

| Rule ID | Rule | Applied |
|---------|------|---------|
| BR-PER-001 | Permission: Manager+ | AC-01, AC-02, AC-03 |
| BR-EVT-001 | Event type visualization | AC-07 |
| BR-EVT-010 | Event immutability | AC-14, AC-15, AC-16 |
| BR-RPT-001 | Date range default | AC-06 |

---

## 9. Related UCs

| UC | Relationship |
|----|-------------|
| [UC-08: Event Log](./uc-08-event-log.md) | Technical flow |
| [US-01: Inbound](./us-01-inbound.md) | Creates events |
| [US-02: Outbound](./us-02-outbound.md) | Creates events |

---

## 10. Test Scenarios

| # | Scenario | Expected Result |
|---|----------|-----------------|
| TS-01 | Manager accesses | See Event Log page |
| TS-02 | Staff accesses | Redirect/403 |
| TS-03 | Default view | Last 7 days, newest first |
| TS-04 | Filter by INBOUND | Only INBOUND events |
| TS-05 | Filter by OUTBOUND | Only OUTBOUND events |
| TS-06 | No edit button | Events are read-only |
| TS-07 | Immutable notice | Displayed on page |

---

** Quay lại**: [US-03](./us-03-low-stock.md) | **Tiếp theo**: [US-05](./us-05-dashboard.md)
