# SPEC.md - Frontend Specification Overview

> **Lưu ý**: Tài liệu chi tiết đã được tách ra thành nhiều file trong thư mục `/docs`

---

## Cấu trúc tài liệu

```
docs/
├── index.md              # Trang chủ tài liệu
├── business/             # Phân tích nghiệp vụ
│   ├── actors.md        # Actors (Admin, Manager, Staff)
│   ├── permissions.md   # Permission Matrix
│   └── business-rules.md # Business Rules
├── uc/                  # Use Cases
│   ├── index.md         # Danh sách UC
│   ├── uc-01-login.md
│   ├── uc-02-dashboard.md
│   ├── uc-03-inventory.md
│   ├── uc-04-product-detail.md
│   ├── uc-05-inbound.md
│   ├── uc-06-outbound.md
│   ├── uc-07-low-stock.md
│   ├── uc-08-event-log.md
│   └── uc-09-adjustment.md
└── us/                  # User Stories
    ├── index.md
    ├── us-01-inbound.md
    ├── us-02-outbound.md
    ├── us-03-low-stock.md
    ├── us-04-audit.md
    └── us-05-dashboard.md
```

---

## Tổng quan hệ thống

### Actors

| Actor | Vai trò | Mô tả |
|-------|---------|--------|
| **ADMIN** | Quản trị viên | Toàn quyền hệ thống |
| **MANAGER** | Quản lý kho | Giám sát, điều chỉnh |
| **STAFF** | Nhân viên kho | Thực hiện inbound/outbound |

### Permission Matrix (Tóm tắt)

| Feature | STAFF | MANAGER | ADMIN |
|---------|-------|---------|-------|
| Dashboard | ✅ | ✅ | ✅ |
| Inventory | ✅ | ✅ | ✅ |
| Low Stock | ❌ | ✅ | ✅ |
| Events | ❌ | ✅ | ✅ |
| Inbound/Outbound | ✅ | ✅ | ✅ |
| Adjustment | ❌ | ✅ | ✅ |

---

## Pages & Navigation

| Page | URL | STAFF | MANAGER | ADMIN |
|------|-----|-------|---------|-------|
| Dashboard | `/dashboard` | ✅ | ✅ | ✅ |
| Inventory | `/inventory` | ✅ | ✅ | ✅ |
| Low Stock | `/low-stock` | ❌ | ✅ | ✅ |
| Events | `/events` | ❌ | ✅ | ✅ |

---

## Quick Links

- [Actors chi tiết](./docs/business/actors.md)
- [Permission Matrix](./docs/business/permissions.md)
- [Business Rules](./docs/business/business-rules.md)
- [Use Cases](./docs/uc/index.md)
- [User Stories](./docs/us/index.md)

---

## Implementation Status

### Use Cases
| ID | Name | Status |
|----|------|--------|
| UC-01 | Đăng nhập | ✅ Done |
| UC-02 | Dashboard | ✅ Done |
| UC-03 | Inventory List | ✅ Done |
| UC-04 | Product Detail | ✅ Done |
| UC-05 | Inbound | ✅ Done |
| UC-06 | Outbound | ✅ Done |
| UC-07 | Low Stock | ✅ Done |
| UC-08 | Event Log | ✅ Done |
| UC-09 | Adjustment | ⏳ Pending |

### User Stories
| ID | Name | Priority | Status |
|----|------|----------|--------|
| US-01 | Inbound nhanh | Must | ✅ Done |
| US-02 | Outbound hàng | Must | ✅ Done |
| US-03 | Giám sát Low Stock | Should | ✅ Done |
| US-04 | Kiểm tra Audit | Should | ✅ Done |
| US-05 | Dashboard | Must | ✅ Done |

---

## Core Business Rules

### Inventory Validation
- **Inbound**: quantity > 0, reference_number required
- **Outbound**: quantity > 0, quantity <= stock
- **Adjustment**: new_quantity >= 0, reason required (min 10 chars)

### Stock Status
- stock = 0 → **Out of Stock** (rose)
- stock <= threshold → **Low Stock** (amber)
- stock > threshold → **OK** (emerald)

### Event Types
- INBOUND → ArrowDownLeft (emerald)
- OUTBOUND → ArrowUpRight (indigo)
- ADJUSTMENT → RefreshCw (amber)
