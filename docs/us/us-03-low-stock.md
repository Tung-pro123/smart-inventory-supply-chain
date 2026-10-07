# US-03: Giám sát Low Stock

> **User Story**: Giám sát hàng sắp hết
> **ID**: US-03
> **Priority**: Should
> **Story Points**: 2

---

## 1. Story

```
┌─────────────────────────────────────────────────────────────┐
│  AS A     Warehouse Manager                               │
│  I WANT   Xem danh sách hàng sắp hết                    │
│  SO THAT  Lên kế hoạch nhập hàng trước khi hết stock   │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. Mô tả nghiệp vụ

### 2.1 Bối cảnh

Quản lý kho cần theo dõi các mặt hàng sắp hết để:
- Lên kế hoạch đặt hàng với supplier
- Tránh tình trạng stockout ảnh hưởng đến sản xuất
- Ưu tiên xử lý các mặt hàng nguy hiểm (critical) trước

### 2.2 User Flow

```
1. Login với role MANAGER
        ↓
2. Dashboard → Click "Low Stock Alerts"
        ↓
3. Xem danh sách alerts (sắp xếp theo urgency)
        ↓
4. Click vào item cần nhập
        ↓
5. Xem suggested order quantity
        ↓
6. Tạo đơn nhập hàng (Inbound)
```

---

## 3. Acceptance Criteria

### 3.1 Access Criteria

```
☐ AC-01: MANAGER có thể truy cập Low Stock page
☐ AC-02: ADMIN có thể truy cập Low Stock page
☐ AC-03: STAFF bị chặn (redirect hoặc 403)
☐ AC-04: Tab "Low Stock" chỉ hiển thị với Manager+
```

### 3.2 Display Criteria

```
☐ AC-05: Hiển thị danh sách items có stock <= threshold
☐ AC-06: Sắp xếp theo urgency (CRITICAL → HIGH → MEDIUM)
☐ AC-07: Hiển thị urgency badge cho mỗi item
☐ AC-08: Hiển thị progress bar (stock/threshold)
☐ AC-09: Hiển thị SKU, tên sản phẩm
☐ AC-10: Hiển thị số stock hiện tại và threshold
```

### 3.3 Reorder Criteria

```
☐ AC-11: Click item → Mở Reorder modal
☐ AC-12: Modal hiển thị current stock và threshold
☐ AC-13: Suggested order quantity được tính sẵn
☐ AC-14: Có thể điều chỉnh số lượng order
☐ AC-15: Tạo Inbound từ modal
```

---

## 4. Urgency Levels

| Level | Condition | Color | Icon |
|-------|-----------|-------|------|
| **CRITICAL** | stock = 0 OR stock < threshold * 0.3 | Rose | AlertTriangle |
| **HIGH** | stock < threshold * 0.6 | Amber | AlertTriangle |
| **MEDIUM** | stock <= threshold | Yellow | AlertTriangle |

---

## 5. UI Specifications

### 5.1 Low Stock List

```
┌─────────────────────────────────────────────────────────────┐
│  ⚠️ Low Stock Alerts                               4 items │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐│
│  │ ⬛ [CRITICAL] BOSCH-CAP-008                        ││
│  │     Ceramic Capacitor 100nF                          ││
│  │                                        3            ││
│  │                                      of 50          ││
│  │ ▓▓▓▓▓▓░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░││
│  └─────────────────────────────────────────────────────┘│
│                                                             │
│  ┌─────────────────────────────────────────────────────┐│
│  │ ⬛ [HIGH] BOSCH-BRK-004                            ││
│  │     High-Performance Brake Caliper Pad                ││
│  │                                       14            ││
│  │                                      of 25          ││
│  │ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓░░░░░░░░░░░░░░░░░░░░░░░░░░░││
│  └─────────────────────────────────────────────────────┘│
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 5.2 Progress Bar Calculation

```javascript
const progress = Math.max((stock / threshold) * 100, 5);
// Min width = 5% (để hiển thị khi stock = 0)
```

---

## 6. Reorder Modal

### 6.1 Trigger

Click vào alert card

### 6.2 Content

```
┌─────────────────────────────────────────────────────────────┐
│  📦 Reorder: BOSCH-CAP-008                        [X]    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Current Stock:     3 units                              │
│  Threshold:        50 units                               │
│  ─────────────────────────────────                        │
│  Suggested Order:  97 units                               │
│  (to reach 2x threshold)                                │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐│
│  │ Order Quantity                                      ││
│  │ [ 97                                             ]  ││
│  └─────────────────────────────────────────────────────┘│
│                                                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  [    Create Inbound Order    ]                            │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 6.3 Suggested Quantity Formula

```javascript
suggestedQuantity = (threshold * 2) - currentStock
// Khi nào stock = 2x threshold = an toàn
```

---

## 7. Empty State

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│                  ✅ All items well stocked                  │
│                                                             │
│            No products are below their threshold.          │
│                    Everything looks good!                   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 8. API Contract

### 8.1 Endpoint

```
GET /api/v1/products/low-stock
```

### 8.2 Response

```json
{
  "success": true,
  "data": [
    {
      "id": 6,
      "sku": "BOSCH-CAP-008",
      "name": "Ceramic Capacitor 100nF",
      "stock": 3,
      "threshold": 50,
      "urgency": "CRITICAL",
      "urgencyPriority": 1
    }
  ]
}
```

---

## 9. Business Rules Applied

| Rule ID | Rule | Applied |
|---------|------|---------|
| BR-PER-001 | Permission: Manager+ | AC-01, AC-02, AC-03 |
| BR-INV-020 | Urgency calculation | AC-06, AC-07 |
| BR-ALR-001 | Alert generation | AC-05 |

---

## 10. Related UCs

| UC | Relationship |
|----|-------------|
| [UC-07: Low Stock](./uc-07-low-stock.md) | Technical flow |
| [US-01: Inbound](./us-01-inbound.md) | Creates inbound |

---

## 11. Test Scenarios

| # | Scenario | Expected Result |
|---|----------|-----------------|
| TS-01 | Manager accesses | See Low Stock page |
| TS-02 | Staff accesses | Redirect/403 |
| TS-03 | Stock = 0 | CRITICAL badge |
| TS-04 | Stock < 30% | CRITICAL badge |
| TS-05 | Stock < 60% | HIGH badge |
| TS-06 | Click item | Open Reorder modal |
| TS-07 | Submit reorder | Create Inbound |

---

** Quay lại**: [US-02](./us-02-outbound.md) | **Tiếp theo**: [US-04](./us-04-audit.md)
