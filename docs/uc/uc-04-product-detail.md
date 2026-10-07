# UC-04: Xem chi tiết sản phẩm

> **Use Case**: Xem chi tiết sản phẩm
> **ID**: UC-04
> **Priority**: Medium
> **Status**: Done

---

## 1. Thông tin chung

| Thuộc tính | Giá trị |
|------------|----------|
| **Use Case ID** | UC-04 |
| **Tên** | Xem chi tiết sản phẩm |
| **Mô tả** | User xem thông tin đầy đủ của sản phẩm và thực hiện actions |
| **Actor** | Tất cả actors |
| **Priority** | Medium |
| **Status** | ✅ Done |

---

## 2. Pre-conditions & Post-conditions

### 2.1 Pre-conditions

| # | Điều kiện |
|---|-----------|
| 1 | User đang ở Inventory page |
| 2 | User đã click vào một product row |

### 2.2 Post-conditions

| # | Điều kiện |
|---|-----------|
| 1 | Modal hiển thị thông tin đầy đủ |
| 2 | User có thể thực hiện Inbound/Outbound |

---

## 3. Modal Structure

```
┌─────────────────────────────────────┐
│  [X]                               │ ← Close button
├─────────────────────────────────────┤
│                                     │
│  BOSCH-SEN-001                     │ ← SKU (mono, indigo)
│  Industrial Radar Distance Sensor   │ ← Name (white, bold)
│                                     │
├─────────────────────────────────────┤
│                                     │
│  Current Stock        45 Sensors    │ ← Stock + Category
│  Threshold            15            │
│  Warehouse            Main Warehouse │
│  Status               [OK]          │ ← Badge
│                                     │
├─────────────────────────────────────┤
│                                     │
│  [📥 Inbound]  [📤 Outbound]      │ ← Action buttons
│                                     │
└─────────────────────────────────────┘
```

---

## 4. Information Displayed

| Field | Value | Notes |
|-------|-------|-------|
| SKU | BOSCH-SEN-001 | Monospace, indigo |
| Name | Industrial Radar Distance Sensor | Bold |
| Category | Sensors | Muted |
| Current Stock | 45 | Large, bold |
| Threshold | 15 | Normal |
| Warehouse | Main Warehouse | Normal |
| Status | OK/Low/Out | Badge |

---

## 5. Action Buttons

### 5.1 Inbound Button

| Property | Value |
|----------|-------|
| Label | Inbound |
| Icon | ArrowDownLeft |
| Color | Emerald (#10b981) |
| Action | Open Inbound modal |

### 5.2 Outbound Button

| Property | Value |
|----------|-------|
| Label | Outbound |
| Icon | ArrowUpRight |
| Color | Indigo (#6366f1) |
| Action | Open Outbound modal |

---

## 6. Modal Specifications

### 6.1 Dimensions

| Property | Value |
|----------|-------|
| Width | max-w-md (448px) |
| Border-radius | rounded-2xl (16px) |
| Padding | p-5 (20px) |

### 6.2 Backdrop

| Property | Value |
|----------|-------|
| Background | bg-black/60 |
| Blur | backdrop-blur-sm |
| Close on click | ✅ |

---

## 7. Business Rules Applied

| Rule ID | Rule | Applied In |
|---------|------|-----------|
| BR-INV-010 | Stock Status Classification | Status badge |
| BR-INV-001 | Inbound Validation | Inbound modal |
| BR-INV-002 | Outbound Validation | Outbound modal |

---

## 8. Acceptance Criteria

| # | Criteria | Expected Result |
|---|----------|-----------------|
| AC-01 | Click row | Open modal |
| AC-02 | Modal content | Show all product info |
| AC-03 | Click backdrop | Close modal |
| AC-04 | Click X | Close modal |
| AC-05 | Click Inbound | Open Inbound modal |
| AC-06 | Click Outbound | Open Outbound modal |

---

** Quay lại**: [UC-03](./uc-03-inventory.md) | **Tiếp theo**: [UC-05](./uc-05-inbound.md)
