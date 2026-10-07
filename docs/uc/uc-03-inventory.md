# UC-03: Xem danh sách Inventory

> **Use Case**: Xem danh sách sản phẩm
> **ID**: UC-03
> **Priority**: High
> **Status**: Done

---

## 1. Thông tin chung

| Thuộc tính | Giá trị |
|------------|----------|
| **Use Case ID** | UC-03 |
| **Tên** | Xem danh sách Inventory |
| **Mô tả** | User xem danh sách sản phẩm với thông tin cơ bản |
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
| 1 | Danh sách sản phẩm được hiển thị |
| 2 | User có thể click để xem chi tiết |

---

## 3. Main Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    MAIN FLOW                                 │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. [ACTOR] User navigate đến Inventory page           │
│                     │                                        │
│                     ▼                                        │
│  2. [SYSTEM] Hiển thị:                                │
│     - Header: "Inventory"                                │
│     - Search bar (empty)                                 │
│     - Table header                                       │
│     - Loading skeleton                                   │
│                     │                                        │
│                     ▼                                        │
│  3. [SYSTEM] Fetch products: GET /api/v1/products    │
│                     │                                        │
│                     ▼                                        │
│  4. [SYSTEM] Render table:                              │
│     - SKU | Product | Stock | Status                   │
│                     │                                        │
│                     ▼                                        │
│  5. [ACTOR] User type in search                       │
│                     │                                        │
│                     ▼                                        │
│  6. [SYSTEM] Filter products locally or API            │
│                     │                                        │
│                     ▼                                        │
│  7. [ACTOR] User click row                             │
│                     │                                        │
│                     ▼                                        │
│  8. [SYSTEM] Open Product Detail Modal                │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Table Columns

| Column | Width | Content | Sortable |
|--------|-------|---------|----------|
| SKU | 140px | Mã sản phẩm (font-mono) | ✅ |
| Product | flex | Tên sản phẩm + Category | ✅ |
| Stock | 80px | Số lượng hiện tại | ✅ |
| Status | 100px | Badge (OK/Low/Out) | ❌ |

---

## 5. Search Behavior

### 5.1 Search Fields

- **SKU**: Exact match hoặc partial
- **Product Name**: Contains match (case-insensitive)

### 5.2 Search UX

```
┌─────────────────────────────────────────────────────────────┐
│ 🔍 [Search SKU or name...]                    │
└─────────────────────────────────────────────────────────────┘
```

- Debounce: 300ms
- Clear button khi có text
- Empty state: "No products found"

---

## 6. Status Badge Rules

| Stock | Status | Color | Label |
|-------|--------|-------|-------|
| stock = 0 | OUT_OF_STOCK | rose | Out |
| stock <= threshold | LOW_STOCK | amber | Low |
| stock > threshold | OPTIMAL | emerald | OK |

---

## 7. API Contract

### 7.1 Request

```
GET /api/v1/products
GET /api/v1/products?search=ECU
GET /api/v1/products?page=0&size=20&sort=name,asc
```

### 7.2 Response

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "sku": "BOSCH-SEN-001",
        "name": "Industrial Radar Distance Sensor",
        "category": "Sensors",
        "stock": 45,
        "threshold": 15,
        "warehouse": "Main Warehouse",
        "status": "OPTIMAL"
      }
    ],
    "totalElements": 1482,
    "totalPages": 75,
    "number": 0
  }
}
```

---

## 8. UI Specifications

### 8.1 Table Row Height

```
Row height: 56px (py-3.5)
Cell padding: px-4 py-3.5
Hover state: bg-slate-800/40
```

### 8.2 Empty State

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│                    📦 No products found                     │
│                                                             │
│              Try adjusting your search or                    │
│              check your filters                              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 9. Business Rules Applied

| Rule ID | Rule | Applied In |
|---------|------|-----------|
| BR-INV-010 | Stock Status Classification | Status column |
| BR-PER-001 | Permission: View Inventory | Page access |

---

## 10. Acceptance Criteria

| # | Criteria | Expected Result |
|---|----------|-----------------|
| AC-01 | Page load | Show loading → Show products |
| AC-02 | Search by SKU | Filter to matching SKUs |
| AC-03 | Search by name | Filter to matching names |
| AC-04 | Click row | Open detail modal |
| AC-05 | Status badge | Show correct color based on stock |
| AC-06 | Empty search | Show "No products found" |

---

** Quay lại**: [UC-02](./uc-02-dashboard.md) | **Tiếp theo**: [UC-04](./uc-04-product-detail.md)
