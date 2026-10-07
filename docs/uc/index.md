# Use Cases - Danh sách Use Cases

> Tổng hợp các Use Cases của hệ thống

---

## 1. Danh sách Use Cases

| ID | Tên Use Case | Actor | Priority | Status |
|----|-------------|-------|----------|--------|
| [UC-01](./uc-01-login.md) | Đăng nhập hệ thống | All | High | Done |
| [UC-02](./uc-02-dashboard.md) | Xem Dashboard | All | High | Done |
| [UC-03](./uc-03-inventory.md) | Xem danh sách Inventory | All | High | Done |
| [UC-04](./uc-04-product-detail.md) | Xem chi tiết sản phẩm | All | Medium | Done |
| [UC-05](./uc-05-inbound.md) | Thực hiện Inbound | Staff, Manager, Admin | High | Done |
| [UC-06](./uc-06-outbound.md) | Thực hiện Outbound | Staff, Manager, Admin | High | Done |
| [UC-07](./uc-07-low-stock.md) | Xem Low Stock Alerts | Manager, Admin | High | Done |
| [UC-08](./uc-08-event-log.md) | Xem Event Log | Manager, Admin | High | Done |
| [UC-09](./uc-09-adjustment.md) | Điều chỉnh tồn kho | Manager, Admin | Medium | Pending |

---

## 2. Use Case Diagram

```
                        ┌─────────────────┐
                        │   SMART INVENTORY │
                        └────────┬────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
         ▼                       ▼                       ▼
   ┌───────────┐          ┌───────────┐          ┌───────────┐
   │   ADMIN   │          │  MANAGER  │          │   STAFF   │
   └─────┬─────┘          └─────┬─────┘          └─────┬─────┘
         │                       │                       │
         │   ┌─────────────────────┼─────────────────────┐   │
         │   │                     │                     │   │
         ▼   │                     ▼                     │   ▼
   ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
   │  UC-09 Adjustment│    │  UC-07 Low Stock│    │  UC-05 Inbound  │
   │  UC-10 User Mgmt │    │  UC-08 Event Log│    │  UC-06 Outbound │
   │  UC-11 Settings  │    │                 │    │                 │
   └─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 3. Priority Classification

| Priority | Mô tả | SLA |
|----------|--------|-----|
| **High** | Core business functions, must work | Same day |
| **Medium** | Important features, workarounds exist | 1 week |
| **Low** | Nice to have, can be deferred | 1 month |

---

## 4. Cross-Cutting UCs

| ID | Tên | Mô tả |
|----|-----|--------|
| UC-00 | Xem trang Login | Hiển thị form đăng nhập |
| UC-01 | Đăng nhập | Xác thực credentials |
| UC-02 | Đăng xuất | Kết thúc session |

---

## 5. Liên kết

### Xem chi tiết từng UC:

- [UC-01: Đăng nhập](./uc-01-login.md)
- [UC-02: Dashboard](./uc-02-dashboard.md)
- [UC-03: Inventory](./uc-03-inventory.md)
- [UC-04: Product Detail](./uc-04-product-detail.md)
- [UC-05: Inbound](./uc-05-inbound.md)
- [UC-06: Outbound](./uc-06-outbound.md)
- [UC-07: Low Stock](./uc-07-low-stock.md)
- [UC-08: Event Log](./uc-08-event-log.md)
- [UC-09: Adjustment](./uc-09-adjustment.md)

---

** Quay lại**: [Business Rules](../business/business-rules.md) | **Tiếp theo**: [UC-01](./uc-01-login.md)
