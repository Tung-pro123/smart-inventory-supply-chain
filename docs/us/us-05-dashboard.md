# US-05: Dashboard tổng quan

> **User Story**: Xem dashboard tổng quan
> **ID**: US-05
> **Priority**: Must
> **Story Points**: 1

---

## 1. Story

```
┌─────────────────────────────────────────────────────────────┐
│  AS A     System Administrator                            │
│  I WANT   Xem tổng quan toàn bộ hệ thống kho           │
│  SO THAT  Đánh giá hiệu suất và trạng thái kho hàng     │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. Mô tả nghiệp vụ

### 2.1 Bối cảnh

Quản trị viên/Quản lý cần một cái nhìn tổng quan để:
- Nắm bắt nhanh tình trạng kho
- Phát hiện vấn đề (low stock alerts)
- Navigate nhanh đến chi tiết

### 2.2 Design Principle: Less is More

Dashboard chỉ hiển thị **3 KPIs quan trọng nhất**:
- Click vào KPI → Drill down để xem chi tiết
- Không nhồi nhét mọi thông tin vào dashboard

---

## 3. Acceptance Criteria

### 3.1 Access Criteria

```
☐ AC-01: Tất cả users đều thấy Dashboard
☐ AC-02: Dashboard hiển thị ngay sau khi login
☐ AC-03: Tab "Dashboard" luôn hiển thị (không phân quyền)
```

### 3.2 Content Criteria

```
☐ AC-04: Hiển thị 3 KPI cards chính
☐ AC-05: KPI "Total Products" - luôn hiển thị
☐ AC-06: KPI "Low Stock Alerts" - Manager+ only
☐ AC-07: KPI "Today's Movements" - Manager+ only
☐ AC-08: Mỗi KPI hiển thị: label, value, subtext
```

### 3.3 Interaction Criteria

```
☐ AC-09: Click KPI "Total Products" → Navigate to Inventory
☐ AC-10: Click KPI "Low Stock" → Navigate to Low Stock
☐ AC-11: Click KPI "Movements" → Navigate to Event Log
☐ AC-12: KPI cards có hover effect
```

---

## 4. KPIs Configuration

### 4.1 Role-Based Display

| KPI | STAFF | MANAGER | ADMIN |
|-----|:-----:|:-------:|:-----:|
| Total Products | ✅ | ✅ | ✅ |
| Low Stock Alerts | ❌ | ✅ | ✅ |
| Today's Movements | ❌ | ✅ | ✅ |

### 4.2 KPI Definitions

| KPI | Icon | Color | Formula | Drill-down |
|-----|------|-------|---------|------------|
| Total Products | Layers | Blue | COUNT(products) | → Inventory |
| Low Stock Alerts | AlertTriangle | Amber | COUNT(stock <= threshold) | → Low Stock |
| Today's Movements | Activity | Emerald | COUNT(events WHERE date = today) | → Events |

---

## 5. UI Specifications

### 5.1 Dashboard Layout (Manager+)

```
┌─────────────────────────────────────────────────────────────┐
│  Overview                                                 │
│  Manager dashboard - monitor warehouse operations           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌───────────────┐ ┌───────────────┐ ┌───────────────┐   │
│  │     📦       │ │     ⚠️       │ │     📊       │   │
│  │              │ │              │ │              │   │
│  │  Total       │ │  Low Stock   │ │  Movements   │   │
│  │  Products    │ │   Alerts     │ │   Today      │   │
│  │              │ │              │ │              │   │
│  │   1,482     │ │      6       │ │      47      │   │
│  │              │ │              │ │              │   │
│  │ Active SKUs  │ │ Below        │ │ Inbound +    │   │
│  │              │ │ threshold    │ │ Outbound     │   │
│  └───────────────┘ └───────────────┘ └───────────────┘   │
│       ↓                ↓                ↓                  │
│    Inventory        Low Stock         Events              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 5.2 Dashboard Layout (Staff)

```
┌─────────────────────────────────────────────────────────────┐
│  Overview                                                 │
│  Staff dashboard - process inbound/outbound                │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐ │
│  │                                                     │ │
│  │     📦                                              │ │
│  │                                                     │ │
│  │  Total Products                                     │ │
│  │                                                     │ │
│  │   1,482                                            │ │
│  │                                                     │ │
│  │  Active SKUs in catalog                            │ │
│  │                                                     │ │
│  └─────────────────────────────────────────────────────┘ │
│                        ↓                                  │
│                     Inventory                              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 5.3 KPI Card Specifications

```
┌─────────────────────────────────────┐
│                                     │
│  [Label - uppercase, muted]        │
│                                     │
│  [Icon - colored bg]               │
│                                     │
│  [Value - 3xl, bold, colored]     │
│                                     │
│  [Subtext - muted]                 │
│                                     │
│  Click to view details →           │
│                                     │
└─────────────────────────────────────┘

Dimensions:
- Padding: p-5
- Border-radius: rounded-xl (12px)
- Border: 1px solid
- Hover: scale-[1.02]
```

---

## 6. Data Refresh

### 6.1 Initial Load

```
1. User navigates to Dashboard
2. Show loading skeleton
3. Fetch KPIs from API
4. Render cards with data
```

### 6.2 API Contract

```
GET /api/v1/dashboard/stats
```

### 6.3 Response

```json
{
  "success": true,
  "data": {
    "totalProducts": 1482,
    "lowStockCount": 6,
    "todayMovements": 47,
    "lastUpdated": "2024-01-15T10:30:00Z"
  }
}
```

---

## 7. Progressive Disclosure

```
Dashboard (3 KPIs)
    │
    ├── Click "Total Products"
    │       └── Inventory List
    │               └── Click row
    │                       └── Product Detail Modal
    │                               └── Inbound/Outbound
    │
    ├── Click "Low Stock" (Manager+)
    │       └── Low Stock List
    │               └── Click item
    │                       └── Reorder Modal
    │
    └── Click "Movements" (Manager+)
            └── Event Log
                    └── Filter by type
```

---

## 8. Business Rules Applied

| Rule ID | Rule | Applied |
|---------|------|---------|
| BR-PER-001 | Role-based visibility | AC-04, AC-05, AC-06, AC-07 |

---

## 9. Related UCs

| UC | Relationship |
|----|-------------|
| [UC-02: Dashboard](./uc-02-dashboard.md) | Technical flow |
| [US-03: Inventory](./uc-03-inventory.md) | Drill-down target |
| [US-03: Low Stock](./uc-07-low-stock.md) | Drill-down target |
| [US-04: Events](./uc-08-event-log.md) | Drill-down target |

---

## 10. Test Scenarios

| # | Scenario | Expected Result |
|---|----------|-----------------|
| TS-01 | Admin views | 3 KPIs visible |
| TS-02 | Manager views | 3 KPIs visible |
| TS-03 | Staff views | 1 KPI visible |
| TS-04 | Click Total Products | Navigate to Inventory |
| TS-05 | Click Low Stock (Staff) | Tab hidden |
| TS-06 | Click Low Stock (Manager) | Navigate to Low Stock |

---

** Quay lại**: [US-04](./us-04-audit.md) | ** Quay về**: [US Index](./index.md)
