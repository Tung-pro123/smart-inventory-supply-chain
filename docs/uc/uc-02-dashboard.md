# UC-02: Xem Dashboard

> **Use Case**: Xem Dashboard tổng quan
> **ID**: UC-02
> **Priority**: High
> **Status**: Done

---

## 1. Thông tin chung

| Thuộc tính | Giá trị |
|------------|----------|
| **Use Case ID** | UC-02 |
| **Tên** | Xem Dashboard tổng quan |
| **Mô tả** | Người dùng xem các chỉ số tổng quan của kho hàng |
| **Actor** | Tất cả actors |
| **Priority** | High |
| **Status** | ✅ Done |

---

## 2. Pre-conditions & Post-conditions

### 2.1 Pre-conditions

| # | Điều kiện |
|---|-----------|
| 1 | User đã đăng nhập |
| 2 | User có JWT token hợp lệ |

### 2.2 Post-conditions

| # | Điều kiện |
|---|-----------|
| 1 | Dashboard hiển thị đúng KPIs |
| 2 | KPIs clickable để drill-down |

---

## 3. KPIs theo Role

### 3.1 STAFF View

```
┌─────────────────────────────────────────────────────────────┐
│  DASHBOARD - STAFF                                        │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                                                     │   │
│  │     📦 Total Products                              │   │
│  │                                                     │   │
│  │            1,482                                   │   │
│  │                                                     │   │
│  │     Click để xem Inventory List                   │   │
│  │                                                     │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                              │
│  ⚠️ Low Stock & Events: Hidden for Staff                 │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 MANAGER/ADMIN View

```
┌─────────────────────────────────────────────────────────────┐
│  DASHBOARD - MANAGER / ADMIN                               │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌───────────────┐ ┌───────────────┐ ┌───────────────┐   │
│  │ 📦 Total     │ │ ⚠️ Low Stock │ │ 📊 Movements │   │
│  │   Products   │ │    Alerts    │ │   Today      │   │
│  ├───────────────┤ ├───────────────┤ ├───────────────┤   │
│  │               │ │               │ │               │   │
│  │   1,482      │ │      6       │ │      47      │   │
│  │               │ │               │ │               │   │
│  └───────┬───────┘ └───────┬───────┘ └───────┬───────┘   │
│          │                 │                 │              │
│          ▼                 ▼                 ▼              │
│     Inventory          Low Stock         Event Log         │
│       List              Alerts           History          │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. KPI Definitions

### 4.1 Total Products (Tất cả roles)

| Thuộc tính | Giá trị |
|------------|----------|
| **Label** | Total Products |
| **Icon** | Layers / Package |
| **Color** | Blue (#3b82f6) |
| **Format** | Number với thousand separator |
| **Drill-down** | → Inventory List |

**Calculation:**
```sql
SELECT COUNT(*) FROM products WHERE is_active = true;
```

---

### 4.2 Low Stock Alerts (Manager+)

| Thuộc tính | Giá trị |
|------------|----------|
| **Label** | Low Stock Alerts |
| **Icon** | AlertTriangle |
| **Color** | Amber (#f59e0b) |
| **Format** | Number |
| **Drill-down** | → Low Stock Page |

**Calculation:**
```sql
SELECT COUNT(*) FROM products p
JOIN inventory_snapshots s ON p.id = s.product_id
WHERE s.quantity <= p.low_stock_threshold;
```

---

### 4.3 Today's Movements (Manager+)

| Thuộc tính | Giá trị |
|------------|----------|
| **Label** | Today's Movements |
| **Icon** | Activity |
| **Color** | Emerald (#10b981) |
| **Format** | Number |
| **Drill-down** | → Event Log Page |

**Calculation:**
```sql
SELECT COUNT(*) FROM inventory_events
WHERE DATE(timestamp) = CURRENT_DATE;
```

---

## 5. UI Specifications

### 5.1 Card Component

```
┌─────────────────────────────────────┐
│                                     │
│  [Label - uppercase, muted]        │
│                                     │
│  [Value - 3xl, bold, colored]     │
│                                     │
│  [Subtext - muted]                 │
│                                     │
│  ┌───────────────┐                 │
│  │ [Icon]       │                  │
│  └───────────────┘                 │
│                                     │
│  Click to view details →           │
│                                     │
└─────────────────────────────────────┘
```

### 5.2 Colors

| Role | Color | Hex |
|------|-------|-----|
| Total Products | Blue | #3b82f6 |
| Low Stock | Amber | #f59e0b |
| Movements | Emerald | #10b981 |

---

## 6. Interaction Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    INTERACTION FLOW                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. User click vào KPI card                               │
│                     │                                        │
│                     ▼                                        │
│  2. System xác định target page:                          │
│     - Total Products → /inventory                          │
│     - Low Stock → /low-stock                              │
│     - Movements → /events                                 │
│                     │                                        │
│                     ▼                                        │
│  3. System cập nhật activeTab/state                      │
│                     │                                        │
│                     ▼                                        │
│  4. UI re-render với page mới                            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 7. API Contract

### 7.1 Request

```
GET /api/v1/dashboard/stats
Authorization: Bearer {token}
```

### 7.2 Response

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

## 8. Business Rules Applied

| Rule ID | Rule | Applied In |
|---------|------|-----------|
| BR-PER-001 | Permission: Dashboard view | Step 1 |

---

## 9. Acceptance Criteria

| # | Criteria | Expected Result |
|---|----------|-----------------|
| AC-01 | Staff xem Dashboard | Thấy 1 KPI card (Total Products) |
| AC-02 | Manager xem Dashboard | Thấy 3 KPI cards |
| AC-03 | Admin xem Dashboard | Thấy 3 KPI cards |
| AC-04 | Click KPI card | Navigate đến page tương ứng |
| AC-05 | Data refresh | KPIs cập nhật real-time |

---

## 10. Related UCs

| Related UC | Relationship |
|------------|-------------|
| [UC-03: Inventory](./uc-03-inventory.md) | Drill-down target |
| [UC-07: Low Stock](./uc-07-low-stock.md) | Drill-down target |
| [UC-08: Event Log](./uc-08-event-log.md) | Drill-down target |

---

** Quay lại**: [UC-01](./uc-01-login.md) | **Tiếp theo**: [UC-03](./uc-03-inventory.md)
