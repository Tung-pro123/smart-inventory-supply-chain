# User Stories - Danh sách User Stories

> Tổng hợp các User Stories của hệ thống

---

## 1. Danh sách User Stories

| ID | Tên | As A | I Want To | So That | Priority |
|----|-----|------|-----------|---------|----------|
| [US-01](./us-01-inbound.md) | Inbound nhanh | Staff | Nhập hàng vào kho | Cập nhật tồn kho chính xác | Must |
| [US-02](./us-02-outbound.md) | Outbound hàng | Staff | Xuất hàng ra kho | Giao hàng cho đơn hàng | Must |
| [US-03](./us-03-low-stock.md) | Giám sát low stock | Manager | Xem hàng sắp hết | Lên kế hoạch nhập hàng | Should |
| [US-04](./us-04-audit.md) | Kiểm tra audit | Manager | Xem lịch sử thay đổi | Đối soát và phát hiện sai sót | Should |
| [US-05](./us-05-dashboard.md) | Dashboard tổng quan | Admin | Xem tổng quan hệ thống | Đánh giá hiệu suất kho | Must |

---

## 2. User Story Template

```
┌─────────────────────────────────────────────────────────────┐
│  USER STORY TEMPLATE                                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  AS A     [Actor]                                         │
│  I WANT   [Action]                                        │
│  SO THAT  [Benefit/Goal]                                 │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  ACCEPTANCE CRITERIA                                       │
│  ☐ [ ] Criterion 1                                        │
│  ☐ [ ] Criterion 2                                        │
│  ☐ [ ] Criterion 3                                        │
└─────────────────────────────────────────────────────────────┘
```

---

## 3. Story Points

| Story Point | Complexity | Description |
|-------------|------------|-------------|
| 1 | Simple | 1-2 hours |
| 2 | Medium | Half day |
| 3 | Complex | 1 day |
| 5 | Very Complex | 2 days |
| 8 | Epic | > 2 days |

---

## 4. Priority Classification

| Priority | Description | Commitment |
|----------|-------------|------------|
| **Must** | Critical path, cannot ship without | Must complete |
| **Should** | Important but not critical | Should complete |
| **Could** | Nice to have | If time permits |
| **Won't** | Not in this sprint | Deferred |

---

## 5. Dependencies

| US | Depends On | Reason |
|----|-----------|--------|
| US-02 | US-01 | Need to see current stock before outbound |
| US-03 | US-01 | Alerts trigger inbound orders |
| US-04 | US-01, US-02 | Events come from inbound/outbound |

---

## 6. Liên kết

### Xem chi tiết từng US:

- [US-01: Inbound](./us-01-inbound.md)
- [US-02: Outbound](./us-02-outbound.md)
- [US-03: Low Stock](./us-03-low-stock.md)
- [US-04: Audit](./us-04-audit.md)
- [US-05: Dashboard](./us-05-dashboard.md)

---

** Quay lại**: [Use Cases](../uc/index.md) | **Tiếp theo**: [US-01](./us-01-inbound.md)
