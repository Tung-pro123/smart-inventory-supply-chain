# Actors - Phân tích Actor

> Mô tả các tác nhân tham gia hệ thống

---

## 1. Tổng quan Actor

```
┌─────────────────────────────────────────────────────────────────┐
│                        HỆ THỐNG SMART INVENTORY                  │
│                                                                 │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐           │
│  │   ADMIN     │  │  MANAGER    │  │   STAFF     │           │
│  │  (Quản trị) │  │  (Quản lý) │  │  (Nhân viên)│           │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘           │
│         │                 │                 │                    │
└─────────┼─────────────────┼─────────────────┼────────────────────┘
          │                 │                 │
          ▼                 ▼                 ▼
     Toàn quyền      Giám sát +        Thực thi
     hệ thống       điều chỉnh        inbound/outbound
```

---

## 2. Chi tiết từng Actor

### 2.1 ADMIN - Quản trị viên

| Thuộc tính | Mô tả |
|------------|--------|
| **ID** | ACT-001 |
| **Tên** | Administrator |
| **Vai trò** | Quản trị hệ thống |
| **Quyền hạn** | Toàn quyền truy cập và quản lý |

#### Nhiệm vụ chính
- Quản lý người dùng (tạo, sửa, xóa tài khoản)
- Cấu hình hệ thống (ngưỡng cảnh báo, warehouse settings)
- Xem toàn bộ dữ liệu và audit log
- Thực hiện tất cả các thao tác inventory

#### Đặc điểm hành vi
- Có thể delegate quyền cho MANAGER
- Có quyền override mọi business rule
- Truy cập vào system configuration

---

### 2.2 MANAGER - Quản lý kho

| Thuộc tính | Mô tả |
|------------|--------|
| **ID** | ACT-002 |
| **Tên** | Warehouse Manager |
| **Vai trò** | Quản lý và giám sát kho hàng |
| **Quyền hạn** | Giám sát, điều chỉnh, báo cáo |

#### Nhiệm vụ chính
- Giám sát tồn kho toàn bộ warehouse
- Duyệt và thực hiện điều chỉnh tồn kho (adjustment)
- Xem và phân tích audit log
- Lên kế hoạch nhập hàng khi low stock
- Xem analytics và báo cáo

#### Đặc điểm hành vi
- Không thể quản lý user accounts
- Cần approve các adjustment lớn
- Tập trung vào visibility và control

---

### 2.3 STAFF - Nhân viên kho

| Thuộc tính | Mô tả |
|------------|--------|
| **ID** | ACT-003 |
| **Tên** | Warehouse Staff |
| **Vai trò** | Nhân viên thực thi |
| **Quyền hạn** | Thực hiện inbound/outbound hàng ngày |

#### Nhiệm vụ chính
- Thực hiện nhập kho (inbound)
- Thực hiện xuất kho (outbound)
- Xem danh sách sản phẩm
- Kiểm tra stock trước khi xuất

#### Đặc điểm hành vi
- Không thể điều chỉnh tồn kho trực tiếp
- Không thấy audit log và low stock alerts
- Tập trung vào execution tasks

---

## 3. So sánh Actor

| Tiêu chí | ADMIN | MANAGER | STAFF |
|-----------|-------|---------|-------|
| Quản lý Users | ✅ | ❌ | ❌ |
| Cấu hình hệ thống | ✅ | ❌ | ❌ |
| Xem Dashboard | ✅ | ✅ | ✅ |
| Xem Inventory | ✅ | ✅ | ✅ |
| Thực hiện Inbound | ✅ | ✅ | ✅ |
| Thực hiện Outbound | ✅ | ✅ | ✅ |
| Xem Low Stock | ✅ | ✅ | ❌ |
| Xem Event Log | ✅ | ✅ | ❌ |
| Điều chỉnh tồn kho | ✅ | ✅ | ❌ |
| Xem Analytics | ✅ | ✅ | ❌ |

---

## 4. External Actors

### 4.1 Hệ thống ERP bên thứ 3

| Thuộc tính | Mô tả |
|------------|--------|
| **ID** | ACT-EXT-001 |
| **Tên** | External ERP System |
| **Loại** | System Actor |

#### Nhiệm vụ
- Gửi yêu cầu inbound/outbound tự động
- Đồng bộ dữ liệu sản phẩm
- Nhận thông báo low stock

---

### 4.2 Hệ thống Alert/Notification

| Thuộc tính | Mô tả |
|------------|--------|
| **ID** | ACT-EXT-002 |
| **Tên** | Alert Engine |
| **Loại** | System Actor |

#### Nhiệm vụ
- Kiểm tra ngưỡng tồn kho định kỳ
- Gửi cảnh báo khi low stock
- Log các alert đã gửi

---

## 5. Quan hệ giữa các Actor

```
┌──────────────┐         inherits          ┌──────────────┐
│    STAFF     │◄──────────────────────────│    ADMIN     │
└──────────────┘                          └──────────────┘
       │                                         │
       │         inherits                        │
       └──────────────────►┌──────────────┐       │
                          │   MANAGER   │◄──────┘
                          └──────────────┘
```

> **Lưu ý**: Đây là quan hệ phân cấp quyền, không phải kế thừa OOP

---

## 6. Mã định danh Actor

| Actor ID | Tên hiển thị | Role Code | Cấp độ |
|----------|--------------|-----------|---------|
| ACT-001 | Administrator | `ADMIN` | 1 |
| ACT-002 | Warehouse Manager | `MANAGER` | 2 |
| ACT-003 | Warehouse Staff | `STAFF` | 3 |

---

## 7. Security Constraints

### 7.1 Xác thực (Authentication)
- Tất cả actors phải đăng nhập bằng email/password
- JWT token được sử dụng với thời hạn 24h
- Session không được share giữa các users

### 7.2 Ủy quyền (Authorization)
- Role-based access control (RBAC)
- Mỗi endpoint kiểm tra role trước khi cho phép truy cập
- Principle of Least Privilege được áp dụng

### 7.3 Audit
- Tất cả actions của ADMIN đều được log
- User login/logout được ghi nhận
- Failed authentication attempts được theo dõi

---

## 8. Giả định (Assumptions)

1. Mỗi user chỉ có một role duy nhất tại một thời điểm
2. Users có thể có nhiều warehouse assignments
3. Không có concept "role hierarchy" trong system - mỗi role có permission set riêng
4. Admin không thể bị remove khỏi hệ thống
5. Mật khẩu được hash bằng BCrypt

---

## 9. Phụ lục

### A. Mẫu User trong Database

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'MANAGER', 'STAFF') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);
```

### B. Mã màu theo Role (UI)

| Role | Màu chính | Màu phụ |
|------|-----------|---------|
| ADMIN | `#f43f5e` (rose) | `#fda4af` |
| MANAGER | `#f59e0b` (amber) | `#fcd34d` |
| STAFF | `#10b981` (emerald) | `#6ee7b7` |

---

**File tiếp theo**: [Permissions Matrix](./permissions.md)
