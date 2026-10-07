# Documentation Overview

> Tài liệu phân tích và thiết kế hệ thống Smart Inventory & Supply Chain

## Cấu trúc

```
docs/
├── index.md              # Tổng quan tài liệu
├── business/             # Phân tích nghiệp vụ
│   ├── actors.md        # Danh sách Actor
│   ├── permissions.md   # Ma trận phân quyền
│   └── business-rules.md # Business Rules
├── uc/                  # Use Cases
│   ├── index.md         # Danh sách UC
│   ├── uc-01-login.md   # UC-01: Đăng nhập
│   ├── uc-02-dashboard.md
│   ├── uc-03-inventory.md
│   ├── uc-04-product-detail.md
│   ├── uc-05-inbound.md
│   ├── uc-06-outbound.md
│   ├── uc-07-low-stock.md
│   ├── uc-08-event-log.md
│   └── uc-09-adjustment.md
└── us/                  # User Stories
    ├── index.md         # Danh sách US
    ├── us-01-inbound.md
    ├── us-02-outbound.md
    ├── us-03-low-stock.md
    ├── us-04-audit.md
    └── us-05-dashboard.md
```

## Mục đích

- **Business**: Mô tả nghiệp vụ, actors, quy tắc nghiệp vụ
- **UC (Use Cases)**: Các trường hợp sử dụng chi tiết với flow
- **US (User Stories)**: Câu chuyện người dùng với acceptance criteria

## Liên kết

- [Business Analysis](./business/)
- [Use Cases](./uc/)
- [User Stories](./us/)
