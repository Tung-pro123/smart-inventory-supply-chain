# Permission Matrix - Ma trận phân quyền

> Chi tiết quyền hạn của từng actor trên từng feature

---

## 1. Tổng quan Permissions

### 1.1 Permission Categories

| Category | Mô tả |
|----------|--------|
| **VIEW** | Chỉ được xem, không được thao tác |
| **EXECUTE** | Được thực hiện action |
| **MANAGE** | Được tạo, sửa, xóa |
| **CONFIGURE** | Thay đổi cấu hình hệ thống |

---

## 2. Feature Permission Matrix

| Feature | Mô tả | STAFF | MANAGER | ADMIN |
|---------|--------|:-----:|:-------:|:-----:|
| **Authentication** | | | | |
| Login | Đăng nhập hệ thống | ✅ | ✅ | ✅ |
| Logout | Đăng xuất | ✅ | ✅ | ✅ |
| View Profile | Xem thông tin cá nhân | ✅ | ✅ | ✅ |
| **Dashboard** | | | | |
| View Dashboard | Xem trang tổng quan | ✅ | ✅ | ✅ |
| View Total SKUs | Xem tổng số sản phẩm | ✅ | ✅ | ✅ |
| View Low Stock Count | Xem số cảnh báo low stock | ❌ | ✅ | ✅ |
| View Today's Movements | Xem số movements hôm nay | ❌ | ✅ | ✅ |
| **Inventory Management** | | | | |
| View Product List | Xem danh sách sản phẩm | ✅ | ✅ | ✅ |
| View Product Detail | Xem chi tiết sản phẩm | ✅ | ✅ | ✅ |
| Search Products | Tìm kiếm sản phẩm | ✅ | ✅ | ✅ |
| **Inbound Operations** | | | | |
| Create Inbound | Tạo phiếu nhập kho | ✅ | ✅ | ✅ |
| View Own Inbounds | Xem các phiếu nhập của mình | ✅ | ✅ | ✅ |
| View All Inbounds | Xem tất cả phiếu nhập | ❌ | ✅ | ✅ |
| Cancel Inbound | Hủy phiếu nhập | ❌ | ✅ | ✅ |
| **Outbound Operations** | | | | |
| Create Outbound | Tạo phiếu xuất kho | ✅ | ✅ | ✅ |
| View Own Outbounds | Xem các phiếu xuất của mình | ✅ | ✅ | ✅ |
| View All Outbounds | Xem tất cả phiếu xuất | ❌ | ✅ | ✅ |
| Cancel Outbound | Hủy phiếu xuất | ❌ | ✅ | ✅ |
| **Inventory Adjustments** | | | | |
| Create Adjustment | Tạo điều chỉnh tồn kho | ❌ | ✅ | ✅ |
| Approve Adjustment | Phê duyệt điều chỉnh | ❌ | ✅ | ✅ |
| View Adjustment History | Xem lịch sử điều chỉnh | ❌ | ✅ | ✅ |
| **Alert Management** | | | | |
| View Low Stock Alerts | Xem cảnh báo low stock | ❌ | ✅ | ✅ |
| Acknowledge Alert | Xác nhận đã xử lý alert | ❌ | ✅ | ✅ |
| Configure Alert Threshold | Cấu hình ngưỡng cảnh báo | ❌ | ❌ | ✅ |
| **Event Log / Audit** | | | | |
| View Event Log | Xem audit trail | ❌ | ✅ | ✅ |
| Filter Events | Lọc events | ❌ | ✅ | ✅ |
| Export Events | Xuất event log | ❌ | ✅ | ✅ |
| **Analytics** | | | | |
| View Analytics | Xem báo cáo phân tích | ❌ | ✅ | ✅ |
| Export Reports | Xuất báo cáo | ❌ | ✅ | ✅ |
| **User Management** | | | | |
| View User List | Xem danh sách users | ❌ | ❌ | ✅ |
| Create User | Tạo user mới | ❌ | ❌ | ✅ |
| Edit User | Sửa thông tin user | ❌ | ❌ | ✅ |
| Deactivate User | Vô hiệu hóa user | ❌ | ❌ | ✅ |
| Assign Role | Gán role cho user | ❌ | ❌ | ✅ |
| **System Configuration** | | | | |
| View Settings | Xem cấu hình hệ thống | ❌ | ❌ | ✅ |
| Edit Settings | Sửa cấu hình | ❌ | ❌ | ✅ |
| View System Logs | Xem system logs | ❌ | ❌ | ✅ |

---

## 3. Page/Tab Access Control

| Page | URL Path | STAFF | MANAGER | ADMIN |
|------|----------|:-----:|:-------:|:-----:|
| Dashboard | `/dashboard` | ✅ | ✅ | ✅ |
| Inventory | `/inventory` | ✅ | ✅ | ✅ |
| Low Stock | `/low-stock` | ❌ | ✅ | ✅ |
| Events | `/events` | ❌ | ✅ | ✅ |
| Analytics | `/analytics` | ❌ | ✅ | ✅ |
| Users | `/users` | ❌ | ❌ | ✅ |
| Settings | `/settings` | ❌ | ❌ | ✅ |

---

## 4. API Endpoint Permissions

### 4.1 Authentication APIs

| Method | Endpoint | Permission | STAFF | MANAGER | ADMIN |
|--------|----------|------------|:-----:|:-------:|:-----:|
| POST | `/api/v1/auth/login` | PUBLIC | ✅ | ✅ | ✅ |
| POST | `/api/v1/auth/logout` | AUTHENTICATED | ✅ | ✅ | ✅ |
| GET | `/api/v1/auth/me` | AUTHENTICATED | ✅ | ✅ | ✅ |

### 4.2 Product APIs

| Method | Endpoint | Permission | STAFF | MANAGER | ADMIN |
|--------|----------|------------|:-----:|:-------:|:-----:|
| GET | `/api/v1/products` | STAFF+ | ✅ | ✅ | ✅ |
| GET | `/api/v1/products/{id}` | STAFF+ | ✅ | ✅ | ✅ |
| GET | `/api/v1/products/low-stock` | MANAGER+ | ❌ | ✅ | ✅ |
| GET | `/api/v1/products/search` | STAFF+ | ✅ | ✅ | ✅ |

### 4.3 Inventory APIs

| Method | Endpoint | Permission | STAFF | MANAGER | ADMIN |
|--------|----------|------------|:-----:|:-------:|:-----:|
| POST | `/api/v1/inventory/inbound` | STAFF+ | ✅ | ✅ | ✅ |
| POST | `/api/v1/inventory/outbound` | STAFF+ | ✅ | ✅ | ✅ |
| POST | `/api/v1/inventory/adjust` | MANAGER+ | ❌ | ✅ | ✅ |
| GET | `/api/v1/inventory/events` | MANAGER+ | ❌ | ✅ | ✅ |
| GET | `/api/v1/inventory/snapshot` | STAFF+ | ✅ | ✅ | ✅ |

### 4.4 User Management APIs

| Method | Endpoint | Permission | STAFF | MANAGER | ADMIN |
|--------|----------|------------|:-----:|:-------:|:-----:|
| GET | `/api/v1/users` | ADMIN | ❌ | ❌ | ✅ |
| POST | `/api/v1/users` | ADMIN | ❌ | ❌ | ✅ |
| PUT | `/api/v1/users/{id}` | ADMIN | ❌ | ❌ | ✅ |
| DELETE | `/api/v1/users/{id}` | ADMIN | ❌ | ❌ | ✅ |

---

## 5. Row-Level Security

### 5.1 Inbound/Outbound Visibility

| Role | Visibility |
|------|-----------|
| STAFF | Chỉ thấy records do mình tạo |
| MANAGER | Thấy tất cả records trong warehouse của mình |
| ADMIN | Thấy tất cả records |

### 5.2 Product Visibility

| Role | Visibility |
|------|-----------|
| STAFF | Tất cả products (read-only) |
| MANAGER | Tất cả products + low stock alerts |
| ADMIN | Tất cả products + all management features |

---

## 6. Permission Enforcement

### 6.1 Backend Enforcement

```java
// Spring Security - Method Security
@PreAuthorize("hasRole('STAFF')")
public Product getProduct(Long id) { ... }

@PreAuthorize("hasRole('MANAGER')")
public List<Alert> getLowStockAlerts() { ... }

@PreAuthorize("hasRole('ADMIN')")
public User createUser(UserRequest request) { ... }
```

### 6.2 Frontend Enforcement

```jsx
// React - Conditional Rendering
{user.role !== 'STAFF' && (
  <Tab key="low-stock" icon={AlertTriangle}>
    Low Stock
  </Tab>
)}

{user.role === 'ADMIN' && (
  <Tab key="users" icon={Users}>
    User Management
  </Tab>
)}
```

---

## 7. Error Handling

### 7.1 Unauthorized Access

| Scenario | HTTP Status | Response |
|----------|-------------|----------|
| Not logged in | 401 | `{ "error": "unauthorized", "message": "Please login" }` |
| Insufficient role | 403 | `{ "error": "forbidden", "message": "Insufficient permissions" }` |
| Resource not found | 404 | `{ "error": "not_found", "message": "Resource not found" }` |

### 7.2 Error Messages (i18n)

```json
{
  "en": {
    "error.unauthorized": "Please login to continue",
    "error.forbidden": "You don't have permission to perform this action",
    "error.insufficient_role": "This feature requires {required_role} role"
  },
  "vi": {
    "error.unauthorized": "Vui lòng đăng nhập để tiếp tục",
    "error.forbidden": "Bạn không có quyền thực hiện thao tác này",
    "error.insufficient_role": "Tính năng này yêu cầu quyền {required_role}"
  }
}
```

---

## 8. Audit Logging

### 8.1 Permission Denied Events

```json
{
  "event": "PERMISSION_DENIED",
  "user_id": 123,
  "user_role": "STAFF",
  "attempted_action": "VIEW_LOW_STOCK",
  "required_role": "MANAGER",
  "timestamp": "2024-01-15T10:30:00Z",
  "ip_address": "192.168.1.100"
}
```

### 8.2 Admin Actions

| Action | Log Level | Description |
|--------|-----------|-------------|
| Create User | INFO | New user created |
| Edit User | INFO | User details modified |
| Deactivate User | WARN | User account disabled |
| Change User Role | WARN | Role hierarchy change |
| System Config Change | WARN | Critical settings modified |

---

## 9. Phụ lục

### A. Role Constants

```java
public class Roles {
    public static final String ADMIN = "ADMIN";
    public static final String MANAGER = "MANAGER";
    public static final String STAFF = "STAFF";
}
```

### B. Permission Constants

```java
public class Permissions {
    // View permissions
    public static final String VIEW_DASHBOARD = "VIEW_DASHBOARD";
    public static final String VIEW_INVENTORY = "VIEW_INVENTORY";
    public static final String VIEW_LOW_STOCK = "VIEW_LOW_STOCK";
    public static final String VIEW_EVENTS = "VIEW_EVENTS";
    
    // Execute permissions
    public static final String CREATE_INBOUND = "CREATE_INBOUND";
    public static final String CREATE_OUTBOUND = "CREATE_OUTBOUND";
    public static final String CREATE_ADJUSTMENT = "CREATE_ADJUSTMENT";
    
    // Manage permissions
    public static final String MANAGE_USERS = "MANAGE_USERS";
    public static final String MANAGE_SETTINGS = "MANAGE_SETTINGS";
}
```

---

**File tiếp theo**: [Business Rules](./business-rules.md)
