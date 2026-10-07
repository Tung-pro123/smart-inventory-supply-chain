# US-01: Thực hiện Inbound nhanh

> **User Story**: Nhập kho nhanh
> **ID**: US-01
> **Priority**: Must
> **Story Points**: 3

---

## 1. Story

```
┌─────────────────────────────────────────────────────────────┐
│  AS A     Warehouse Staff                                │
│  I WANT   Nhập số lượng hàng hóa vào kho              │
│  SO THAT  Cập nhật số tồn kho chính xác và có audit  │
│           log cho mỗi thao tác                          │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. Mô tả nghiệp vụ

### 2.1 Bối cảnh

Nhân viên kho nhận được lô hàng mới từ nhà cung cấp. Họ cần nhập số lượng vào hệ thống để:
- Cập nhật tồn kho thực tế
- Có bằng chứng (audit log) cho mỗi thao tác
- Đối tác với P/O (Purchase Order) từ nhà cung cấp

### 2.2 User Flow

```
1. Nhận hàng từ supplier
        ↓
2. Kiểm tra số lượng thực tế
        ↓
3. Mở app → Inventory → Tìm sản phẩm
        ↓
4. Click "Inbound"
        ↓
5. Nhập số lượng + Reference Number (P/O)
        ↓
6. Xác nhận
        ↓
7. Hệ thống tự động:
   - Tạo event INBOUND
   - Cập nhật snapshot
   - Hiển thị toast thành công
```

---

## 3. Acceptance Criteria

### 3.1 Core Criteria

```
☐ AC-01: User có thể mở form inbound từ Product Detail
☐ AC-02: Form hiển thị SKU (readonly) và current stock
☐ AC-03: Input quantity với validation > 0
☐ AC-04: Input reference number (P/O number)
☐ AC-05: Submit tạo event INBOUND trong database
☐ AC-06: Submit cập nhật inventory snapshot
☐ AC-07: Toast notification thành công
☐ AC-08: Modal đóng sau khi submit thành công
☐ AC-09: Product list tự cập nhật stock mới
```

### 3.2 Validation Criteria

```
☐ AC-10: Không cho submit nếu quantity = 0
☐ AC-11: Không cho submit nếu quantity không phải số nguyên
☐ AC-12: Không cho submit nếu reference number trống
☐ AC-13: Error message hiển thị rõ ràng cho từng lỗi
```

### 3.3 UX Criteria

```
☐ AC-14: Loading state khi submit
☐ AC-15: Button disabled khi đang loading
☐ AC-16: Enter key submit form
☐ AC-17: ESC key đóng modal
☐ AC-18: Click backdrop đóng modal
```

---

## 4. UI Specifications

### 4.1 Inbound Modal

```
┌─────────────────────────────────────────────────────────────┐
│  📥 Inbound                                        [X]  │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐│
│  │ 📦 BOSCH-SEN-001 • Current: 45 units              ││
│  └─────────────────────────────────────────────────────┘│
│                                                             │
│  ┌─────────────────────────────────────────────────────┐│
│  │ Quantity *                                          ││
│  │ [                                                 ]││
│  └─────────────────────────────────────────────────────┘│
│                                                             │
│  ┌─────────────────────────────────────────────────────┐│
│  │ Reference Number (P/O) *                            ││
│  │ [ PO-2024-0123                                ]    ││
│  └─────────────────────────────────────────────────────┘│
│                                                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  [Cancel]              [    Confirm Inbound    ]           │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 Success Toast

```
┌─────────────────────────────────────────────────────────────┐
│  ✅ Inbound created: BOSCH-SEN-001 (+50 units)           │
└─────────────────────────────────────────────────────────────┘
```

---

## 5. API Contract

### 5.1 Endpoint

```
POST /api/v1/inventory/inbound
```

### 5.2 Request

```json
{
  "sku": "BOSCH-SEN-001",
  "quantity": 50,
  "referenceNumber": "PO-2024-0123",
  "warehouseId": 1
}
```

### 5.3 Success Response

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

---

## 6. Business Rules Applied

| Rule ID | Rule | Applied |
|---------|------|---------|
| BR-INV-001 | Inbound validation | AC-10, AC-11, AC-12 |
| BR-TXN-001 | Transaction atomicity | AC-05, AC-06 |
| BR-EVT-010 | Event immutability | AC-05 |

---

## 7. Related UCs

| UC | Relationship |
|----|-------------|
| [UC-04: Product Detail](./uc-04-product-detail.md) | Entry point |
| [UC-05: Inbound](./uc-05-inbound.md) | Technical flow |

---

## 8. Test Scenarios

| # | Scenario | Expected Result |
|---|----------|-----------------|
| TS-01 | Submit with qty=50 | Success, stock +50 |
| TS-02 | Submit with qty=0 | Error, "Quantity > 0" |
| TS-03 | Submit without ref | Error, "Ref required" |
| TS-04 | Submit with negative | Error, "Quantity > 0" |
| TS-05 | Submit while loading | Button disabled |

---

** Quay lại**: [US Index](./index.md) | **Tiếp theo**: [US-02](./us-02-outbound.md)
