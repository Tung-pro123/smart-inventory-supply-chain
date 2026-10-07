# UC-01: Đăng nhập hệ thống

> **Use Case**: Đăng nhập hệ thống
> **ID**: UC-01
> **Priority**: High
> **Status**: Done

---

## 1. Thông tin chung

| Thuộc tính | Giá trị |
|------------|----------|
| **Use Case ID** | UC-01 |
| **Tên** | Đăng nhập hệ thống (Login) |
| **Mô tả** | Người dùng đăng nhập vào hệ thống bằng email và password |
| **Actor** | Tất cả actors (Admin, Manager, Staff) |
| **Priority** | High |
| **Status** | ✅ Done |

---

## 2. Pre-conditions & Post-conditions

### 2.1 Pre-conditions

| # | Điều kiện | Mô tả |
|---|-----------|--------|
| 1 | User có tài khoản trong hệ thống | Email đã được đăng ký |
| 2 | Tài khoản đang active | Không bị vô hiệu hóa |
| 3 | User chưa đăng nhập | Không có valid JWT token |

### 2.2 Post-conditions

| # | Điều kiện | Mô tả |
|---|-----------|--------|
| 1 | Success | JWT token được tạo và lưu trữ |
| 2 | Success | User được chuyển đến Dashboard |
| 3 | Success | Role-based navigation được hiển thị |
| 4 | Failure | Hiển thị thông báo lỗi |

---

## 3. Main Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    MAIN FLOW                                 │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. [ACTOR] User mở trang login                           │
│                     │                                        │
│                     ▼                                        │
│  2. [SYSTEM] Hiển thị form login:                        │
│     - Email input                                          │
│     - Password input                                       │
│     - Login button                                         │
│                     │                                        │
│                     ▼                                        │
│  3. [ACTOR] User nhập email và password                   │
│                     │                                        │
│                     ▼                                        │
│  4. [ACTOR] User click "Login"                           │
│                     │                                        │
│                     ▼                                        │
│  5. [SYSTEM] Validate input:                              │
│     - Email format hợp lệ                                 │
│     - Password không trống                                 │
│                     │                                        │
│                     ▼                                        │
│  6. [SYSTEM] Gọi API /api/v1/auth/login                 │
│     POST { email, password }                               │
│                     │                                        │
│                     ▼                                        │
│  7. [SYSTEM] Backend xác thực:                          │
│     - Tìm user theo email                                 │
│     - Verify password (BCrypt)                            │
│     - Generate JWT token                                   │
│                     │                                        │
│                     ▼                                        │
│  8. [SYSTEM] Trả về response:                           │
│     - 200 OK: { token, user, role }                      │
│     - 401 Unauthorized: Invalid credentials                │
│                     │                                        │
│                     ▼                                        │
│  9. [SYSTEM] Lưu token vào localStorage/cookie           │
│                     │                                        │
│                     ▼                                        │
│  10. [SYSTEM] Redirect đến Dashboard                     │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Alternative Flows

### 4.1 AF-01: Invalid Email Format

```
┌─────────────────────────────────────────────────────────────┐
│  AF-01: Invalid Email Format                               │
├─────────────────────────────────────────────────────────────┤
│  1. User nhập email không đúng format                      │
│  2. User click "Login"                                    │
│  3. System hiển thị lỗi: "Invalid email format"           │
│  4. User nhập lại email                                   │
│  5. Quay lại Main Flow step 4                              │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 AF-02: Invalid Credentials

```
┌─────────────────────────────────────────────────────────────┐
│  AF-02: Invalid Credentials                                │
├─────────────────────────────────────────────────────────────┤
│  1. User nhập sai email hoặc password                     │
│  2. User click "Login"                                    │
│  3. System gọi API                                        │
│  4. Backend trả về 401                                   │
│  5. System hiển thị: "Invalid email or password"          │
│  6. User thử lại                                          │
│  7. Quay lại Main Flow step 3                              │
└─────────────────────────────────────────────────────────────┘
```

### 4.3 AF-03: Account Disabled

```
┌─────────────────────────────────────────────────────────────┐
│  AF-03: Account Disabled                                   │
├─────────────────────────────────────────────────────────────┤
│  1. User đăng nhập với tài khoản bị disable              │
│  2. Backend trả về 403 Forbidden                          │
│  3. System hiển thị: "Account is disabled"               │
│  4. Hệ thống không cho phép đăng nhập                    │
└─────────────────────────────────────────────────────────────┘
```

---

## 5. Exception Flows

### 5.1 EF-01: Network Error

```
┌─────────────────────────────────────────────────────────────┐
│  EF-01: Network Error                                      │
├─────────────────────────────────────────────────────────────┤
│  1. User click "Login"                                    │
│  2. Request thất bại do network                           │
│  3. System hiển thị: "Unable to connect. Check internet"  │
│  4. User thử lại sau                                      │
└─────────────────────────────────────────────────────────────┘
```

### 5.2 EF-02: Server Error (500)

```
┌─────────────────────────────────────────────────────────────┐
│  EF-02: Server Error                                       │
├─────────────────────────────────────────────────────────────┤
│  1. User click "Login"                                    │
│  2. Server trả về 500                                    │
│  3. System hiển thị: "Server error. Try again later"      │
│  4. Log lỗi để debug                                     │
└─────────────────────────────────────────────────────────────┘
```

---

## 6. UI Mockup

### 6.1 Login Form

```
┌─────────────────────────────────────────┐
│                                         │
│        ┌───────────────────┐           │
│        │  🔐 SMART INVENTORY │           │
│        └───────────────────┘           │
│                                         │
│        Welcome back                      │
│        Sign in to continue               │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ 📧 Email                        │   │
│  │ [                           ]   │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ 🔒 Password                     │   │
│  │ [                           ]   │   │
│  └─────────────────────────────────┘   │
│                                         │
│      [        Sign In         ]          │
│                                         │
│        Forgot password?                  │
│                                         │
└─────────────────────────────────────────┘
```

### 6.2 Error State

```
┌─────────────────────────────────────────┐
│                                         │
│        ┌───────────────────┐           │
│        │  🔐 SMART INVENTORY │           │
│        └───────────────────┘           │
│                                         │
│        ┌───────────────────────────┐   │
│        │ ⚠️ Invalid email or        │   │
│        │    password               │   │
│        └───────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ 📧 Email                        │   │
│  │ [wrong@email.com            ]   │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ 🔒 Password                     │   │
│  │ [••••••••                 ]   │   │
│  └─────────────────────────────────┘   │
│                                         │
│      [        Sign In         ]          │
│                                         │
└─────────────────────────────────────────┘
```

---

## 7. API Contract

### 7.1 Request

```
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@bosch.com",
  "password": "SecurePass123"
}
```

### 7.2 Success Response (200 OK)

```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "user@bosch.com",
      "fullName": "Nguyen Van A",
      "role": "MANAGER",
      "warehouseIds": [1, 2]
    },
    "expiresAt": "2024-01-16T10:00:00Z"
  }
}
```

### 7.3 Error Response (401 Unauthorized)

```json
{
  "success": false,
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "Invalid email or password"
  }
}
```

---

## 8. Business Rules Applied

| Rule ID | Rule Name | Applied In |
|---------|-----------|-----------|
| BR-AUTH-001 | JWT Token Expiration | Step 7 |
| BR-AUTH-010 | Password Requirements | Backend validation |

---

## 9. Acceptance Criteria

| # | Criteria | Test Scenario |
|---|----------|---------------|
| AC-01 | User có thể đăng nhập với credentials hợp lệ | Input valid email/password → Dashboard |
| AC-02 | User không thể đăng nhập với sai credentials | Input wrong password → Error message |
| AC-03 | User không thể đăng nhập với email không tồn tại | Input unknown email → Error message |
| AC-04 | Token được lưu sau khi đăng nhập thành công | Check localStorage after login |
| AC-05 | User được redirect đến Dashboard | Check URL after login |
| AC-06 | Navigation hiển thị đúng theo role | Check visible tabs match role |

---

## 10. Related UCs

| Related UC | Relationship |
|------------|-------------|
| [UC-02: Dashboard](./uc-02-dashboard.md) | Redirect target |
| - | - |

---

** Quay lại**: [UC Index](./index.md) | **Tiếp theo**: [UC-02](./uc-02-dashboard.md)
