# Business Rules - Quy tắc nghiệp vụ

> Các quy tắc kinh doanh ràng buộc hệ thống

---

## 1. Tổng quan Business Rules

Business Rules là các ràng buộc nghiệp vụ mà hệ thống phải tuân thủ. Mỗi rule bao gồm:
- **Rule ID**: Mã định danh duy nhất
- **Tên**: Mô tả ngắn gọn
- **Điều kiện**: Khi nào rule được áp dụng
- **Hành động**: Rule yêu cầu gì

---

## 2. Inventory Rules

### 2.1 Stock Validation Rules

#### BR-INV-001: Inbound Quantity Validation

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-INV-001                                            │
│ Tên: Inbound Quantity Must Be Positive                      │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ User submits an inbound request                   │
│ THEN    │ quantity MUST be > 0                             │
│ THEN    │ quantity MUST be an integer                       │
│ AND     │ reference_number MUST NOT be empty                │
│ AND     │ SKU MUST exist in the system                      │
│ ON FAIL │ Display validation error, prevent submission      │
└─────────────────────────────────────────────────────────────┘
```

**Validation Logic (Frontend)**
```javascript
function validateInbound(data) {
  const errors = [];
  
  if (!data.quantity || data.quantity <= 0) {
    errors.push('Quantity must be greater than 0');
  }
  
  if (!Number.isInteger(data.quantity)) {
    errors.push('Quantity must be a whole number');
  }
  
  if (!data.referenceNumber || data.referenceNumber.trim() === '') {
    errors.push('Reference number is required');
  }
  
  if (!productExists(data.sku)) {
    errors.push('Product SKU not found');
  }
  
  return errors;
}
```

---

#### BR-INV-002: Outbound Quantity Validation

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-INV-002                                            │
│ Tên: Outbound Cannot Exceed Current Stock                  │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ User submits an outbound request                  │
│ THEN    │ quantity MUST be > 0                             │
│ THEN    │ quantity MUST be <= current_stock                 │
│ THEN    │ reference_number MUST NOT be empty                │
│ ON FAIL │ Display "Insufficient stock" error                │
│ ON FAIL │ Disable submit button                            │
└─────────────────────────────────────────────────────────────┘
```

**Validation Logic (Frontend)**
```javascript
function validateOutbound(data, currentStock) {
  const errors = [];
  
  if (!data.quantity || data.quantity <= 0) {
    errors.push('Quantity must be greater than 0');
  }
  
  if (data.quantity > currentStock) {
    errors.push(`Insufficient stock. Available: ${currentStock}`);
  }
  
  if (!data.referenceNumber || data.referenceNumber.trim() === '') {
    errors.push('Reference number is required');
  }
  
  return errors;
}
```

---

### 2.2 Stock Status Classification

#### BR-INV-010: Stock Status Determination

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-INV-010                                            │
│ Tên: Stock Status Classification                           │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ System displays stock status                      │
│ THEN    │ IF stock = 0                                     │
│         │   → Status: "OUT_OF_STOCK"                       │
│         │   → Color: rose (#f43f5e)                        │
│         │   → Badge: "Out of Stock"                        │
│ THEN    │ IF stock <= threshold                             │
│         │   → Status: "LOW_STOCK"                          │
│         │   → Color: amber (#f59e0b)                       │
│         │   → Badge: "Low Stock"                           │
│ THEN    │ IF stock > threshold                              │
│         │   → Status: "OPTIMAL"                            │
│         │   → Color: emerald (#10b981)                     │
│         │   → Badge: "OK"                                  │
└─────────────────────────────────────────────────────────────┘
```

**Implementation**
```javascript
function getStockStatus(stock, threshold) {
  if (stock <= 0) {
    return { key: 'OUT_OF_STOCK', label: 'Out of Stock', color: 'rose' };
  }
  if (stock <= threshold) {
    return { key: 'LOW_STOCK', label: 'Low Stock', color: 'amber' };
  }
  return { key: 'OPTIMAL', label: 'OK', color: 'emerald' };
}
```

---

### 2.3 Low Stock Urgency Rules

#### BR-INV-020: Low Stock Urgency Calculation

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-INV-020                                            │
│ Tên: Urgency Level Based on Stock Ratio                    │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ Calculating urgency for low stock item            │
│ THEN    │ IF stock = 0                                     │
│         │   → Urgency: "CRITICAL"                          │
│         │   → Color: rose (#f43f5e)                        │
│         │   → Priority: 1 (highest)                        │
│ THEN    │ IF stock < threshold * 0.3                       │
│         │   → Urgency: "CRITICAL"                          │
│         │   → Color: rose (#f43f5e)                        │
│         │   → Priority: 1                                  │
│ THEN    │ IF stock < threshold * 0.6                       │
│         │   → Urgency: "HIGH"                             │
│         │   → Color: amber (#f59e0b)                      │
│         │   → Priority: 2                                  │
│ THEN    │ IF stock <= threshold                             │
│         │   → Urgency: "MEDIUM"                           │
│         │   → Color: yellow (#eab308)                     │
│         │   → Priority: 3                                  │
└─────────────────────────────────────────────────────────────┘
```

**Implementation**
```javascript
function calculateUrgency(stock, threshold) {
  if (stock === 0 || stock < threshold * 0.3) {
    return { level: 'CRITICAL', color: 'rose', priority: 1 };
  }
  if (stock < threshold * 0.6) {
    return { level: 'HIGH', color: 'amber', priority: 2 };
  }
  return { level: 'MEDIUM', color: 'yellow', priority: 3 };
}
```

---

## 3. Event Rules

### 3.1 Event Type Classification

#### BR-EVT-001: Event Type Visual Representation

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-EVT-001                                            │
│ Tên: Event Type Icons and Colors                           │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ Displaying event type                            │
│ THEN    │ IF type = "INBOUND"                              │
│         │   → Icon: ArrowDownLeft (nhập vào)               │
│         │   → Color: emerald (#10b981)                      │
│         │   → Background: emerald/10                        │
│         │   → Border: emerald/20                           │
│ THEN    │ IF type = "OUTBOUND"                             │
│         │   → Icon: ArrowUpRight (xuất ra)                 │
│         │   → Color: indigo (#6366f1)                      │
│         │   → Background: indigo/10                        │
│         │   → Border: indigo/20                           │
│ THEN    │ IF type = "ADJUSTMENT"                           │
│         │   → Icon: RefreshCw (điều chỉnh)                │
│         │   → Color: amber (#f59e0b)                       │
│         │   → Background: amber/10                         │
│         │   → Border: amber/20                            │
│ THEN    │ IF type = "DAMAGE"                               │
│         │   → Icon: AlertTriangle (hư hỏng)                │
│         │   → Color: rose (#f43f5e)                        │
│         │   → Background: rose/10                         │
│         │   → Border: rose/20                             │
└─────────────────────────────────────────────────────────────┘
```

---

### 3.2 Event Immutability

#### BR-EVT-010: Events Cannot Be Modified

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-EVT-010                                            │
│ Tên: Audit Trail Immutability                             │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ Event record is created                          │
│ THEN    │ Record is APPEND-ONLY                            │
│ THEN    │ No UPDATE operations allowed                      │
│ THEN    │ No DELETE operations allowed                      │
│ THEN    │ Record includes:                                 │
│         │   - Event ID (auto-generated)                    │
│         │   - Timestamp (server-generated)                 │
│         │   - Operator (from JWT token)                    │
│         │   - IP Address (from request)                    │
│ THEN    │ Event ID is monotonically increasing              │
│ ON FAIL │ N/A - database constraint prevents violation       │
└─────────────────────────────────────────────────────────────┘
```

**Database Constraint**
```sql
-- Events table is append-only
-- No UPDATE or DELETE triggers allowed
-- Application layer enforces immutability
```

---

## 4. Authentication Rules

### 4.1 Session Management

#### BR-AUTH-001: JWT Token Expiration

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-AUTH-001                                            │
│ Tên: Session Timeout                                        │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ User logs in successfully                        │
│ THEN    │ Generate JWT with 24-hour expiration              │
│ THEN    │ Token includes user_id, role, warehouse_id        │
│ THEN    │ Token is signed with secret key                   │
│ THEN    │ Client stores token in httpOnly cookie            │
│ WHEN    │ Token expires                                    │
│ THEN    │ Return 401 Unauthorized                          │
│ THEN    │ Client redirects to login page                    │
└─────────────────────────────────────────────────────────────┘
```

---

### 4.2 Password Policy

#### BR-AUTH-010: Password Requirements

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-AUTH-010                                            │
│ Tên: Password Security                                      │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ User creates or updates password                  │
│ THEN    │ Password MUST be at least 8 characters            │
│ THEN    │ Password MUST contain uppercase letter            │
│ THEN    │ Password MUST contain lowercase letter            │
│ THEN    │ Password MUST contain number                      │
│ THEN    │ Password MUST be hashed with BCrypt (cost 12)     │
│ THEN    │ Password MUST NOT be stored in plaintext          │
│ THEN    │ Old passwords CANNOT be reused (last 5)            │
│ ON FAIL │ Display specific validation error                  │
└─────────────────────────────────────────────────────────────┘
```

---

## 5. Transaction Rules

### 5.1 ACID Compliance

#### BR-TXN-001: Inventory Transaction Atomicity

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-TXN-001                                            │
│ Tên: Atomic Inventory Updates                             │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ Inventory transaction is processed                 │
│ THEN    │ Both event AND snapshot are updated               │
│ THEN    │ Updates are wrapped in single database transaction │
│ THEN    │ IF any step fails                                │
│         │   → Rollback entire transaction                   │
│         │   → No partial state changes                      │
│ THEN    │ IF all steps succeed                             │
│         │   → Commit transaction                           │
│         │   → Return success response                      │
└─────────────────────────────────────────────────────────────┘
```

**Implementation Flow**
```
1. BEGIN TRANSACTION
2. INSERT INTO inventory_events (...)
3. UPDATE inventory_snapshots SET quantity = ? WHERE ...
4. IF error → ROLLBACK
5. COMMIT
```

---

### 5.2 Concurrency Control

#### BR-TXN-010: Optimistic Locking

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-TXN-010                                            │
│ Tên: Prevent Concurrent Stock Conflicts                     │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ Two users process stock simultaneously            │
│ THEN    │ Use optimistic locking with version field         │
│ THEN    │ Each update increments version                   │
│ THEN    │ IF version mismatch detected                      │
│         │   → Reject update                                │
│         │   → Return 409 Conflict                          │
│         │   → User must retry                             │
│ THEN    │ IF version matches                               │
│         │   → Process update                              │
│         │   → Increment version                           │
└─────────────────────────────────────────────────────────────┘
```

**Database Schema**
```sql
CREATE TABLE inventory_snapshots (
    id BIGINT PRIMARY KEY,
    product_id BIGINT,
    warehouse_id BIGINT,
    quantity INT NOT NULL,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT uk_prod_wh UNIQUE (product_id, warehouse_id)
);
```

---

## 6. Alert Rules

### 6.1 Low Stock Alert Generation

#### BR-ALR-001: Automatic Low Stock Detection

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-ALR-001                                            │
│ Tên: Trigger Alert When Below Threshold                    │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ Inventory snapshot is updated                     │
│ THEN    │ IF new_quantity <= product.threshold              │
│         │   → Generate alert record                        │
│         │   → Set urgency based on BR-INV-020              │
│         │   → Set status = "UNACKNOWLEDGED"                │
│ THEN    │ IF new_quantity > product.threshold              │
│         │   → Auto-resolve any existing alerts             │
│         │   → Set alert status = "RESOLVED"               │
│         │   → Set resolved_at timestamp                    │
└─────────────────────────────────────────────────────────────┘
```

---

### 6.2 Scheduled Alert Check

#### BR-ALR-010: Background Alert Scanner

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-ALR-010                                            │
│ Tên: Scheduled Stock Level Check                           │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ Scheduled job runs (every 5 minutes)              │
│ THEN    │ Query all products with stock <= threshold         │
│ THEN    │ For each product:                                │
│         │   → Check if active alert exists                  │
│         │   → IF no alert → Create new alert               │
│         │   → IF alert exists → Update urgency if changed   │
│ THEN    │ For resolved alerts:                             │
│         │   → Check if stock still below threshold         │
│         │   → IF above threshold → Mark as RESOLVED        │
│ THEN    │ Send notification if new critical alerts          │
└─────────────────────────────────────────────────────────────┘
```

**Schedule Configuration**
```yaml
# application.yml
spring:
  scheduler:
    enabled: true
    alert-check-cron: "0 */5 * * * *"  # Every 5 minutes
```

---

## 7. Warehouse Rules

### 7.1 Stock Allocation

#### BR-WH-001: Single Warehouse Stock

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-WH-001                                            │
│ Tên: Stock is Warehouse-Specific                           │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ Stock quantity is stored                          │
│ THEN    │ Stock is associated with specific warehouse       │
│ THEN    │ SKU can have different stock in each warehouse    │
│ THEN    │ Outbound MUST specify warehouse_id                │
│ THEN    │ Stock check is per-warehouse                     │
└─────────────────────────────────────────────────────────────┘
```

---

## 8. Reporting Rules

### 8.1 Event Filtering

#### BR-RPT-001: Event Date Range Filter

```
┌─────────────────────────────────────────────────────────────┐
│ RULE: BR-RPT-001                                            │
│ Tên: Default Date Range for Event Queries                   │
├─────────────────────────────────────────────────────────────┤
│ WHEN    │ User requests event list without date filter      │
│ THEN    │ Default to last 7 days                           │
│ THEN    │ Maximum query range = 90 days                     │
│ THEN    │ IF range > 90 days                               │
│         │   → Display warning                              │
│         │   → Require confirmation                         │
│ THEN    │ IF no results in range                           │
│         │   → Display "No events found" message            │
└─────────────────────────────────────────────────────────────┘
```

---

## 9. Summary Matrix

| Rule ID | Category | Severity | Auto-Enforced |
|---------|----------|----------|---------------|
| BR-INV-001 | Inventory | HIGH | ✅ Frontend + Backend |
| BR-INV-002 | Inventory | HIGH | ✅ Frontend + Backend |
| BR-INV-010 | Inventory | INFO | ✅ Frontend only |
| BR-INV-020 | Inventory | INFO | ✅ Frontend only |
| BR-EVT-001 | Events | INFO | ✅ Frontend only |
| BR-EVT-010 | Events | CRITICAL | ✅ Backend (DB) |
| BR-AUTH-001 | Auth | HIGH | ✅ Backend only |
| BR-AUTH-010 | Auth | HIGH | ✅ Backend only |
| BR-TXN-001 | Transaction | CRITICAL | ✅ Backend (DB) |
| BR-TXN-010 | Transaction | HIGH | ✅ Backend (DB) |
| BR-ALR-001 | Alert | HIGH | ✅ Backend only |
| BR-ALR-010 | Alert | MEDIUM | ✅ Backend only |
| BR-WH-001 | Warehouse | HIGH | ✅ Backend only |
| BR-RPT-001 | Reporting | LOW | ✅ Frontend |

---

## 10. Violation Handling

### 10.1 Error Response Format

```json
{
  "error": "VALIDATION_ERROR",
  "code": "BR-INV-002",
  "message": "Outbound quantity exceeds available stock",
  "details": {
    "requested": 100,
    "available": 45
  },
  "rule": "BR-INV-002"
}
```

### 10.2 User-Facing Messages

| Rule | English | Vietnamese |
|------|---------|------------|
| BR-INV-001 | "Quantity must be greater than 0" | "Số lượng phải lớn hơn 0" |
| BR-INV-002 | "Insufficient stock. Available: {n}" | "Không đủ hàng. Khả dụng: {n}" |
| BR-AUTH-001 | "Session expired. Please login again" | "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại" |

---

** Quay lại**: [Actors](./actors.md) | **Tiếp theo**: [Use Cases](../uc/index.md)
