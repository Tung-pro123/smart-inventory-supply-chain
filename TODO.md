# TODO - Smart Inventory Project

> Checklist để hoàn thiện dự án

---

## Legend

```
✅ Done    - Đã hoàn thành
⬜ TODO    - Chưa làm
🔄 In Progress - Đang làm
❌ Skipped - Bỏ qua (có lý do)
```

---

## PHASE 1: Backend Enhancements

### 1.1 Redis Caching

| Task | Status | Notes | Time |
|------|--------|-------|------|
| 1.1.1 Thêm spring-boot-starter-data-redis vào pom.xml | ✅ Done | + caffeine fallback | 10m |
| 1.1.2 Thêm redis config vào application.yml | ✅ Done | Host, port, password | 10m |
| 1.1.3 Tạo RedisConfig.java | ✅ Done | CacheManager config | 30m |
| 1.1.4 Thêm @EnableCaching vào main class | ✅ Done | | 5m |
| 1.1.5 Cache inventory snapshots (getSnapshot) | ✅ Done | @Cacheable | 30m |
| 1.1.6 Cache products list (getProducts) | ✅ Done | @Cacheable | 30m |
| 1.1.7 Cache warehouses (getWarehouses) | ✅ Done | @Cacheable | 30m |
| 1.1.8 Evict cache on inbound/outbound | ✅ Done | @CacheEvict methods | 30m |
| 1.1.9 Update docker-compose with Redis | ✅ Done | | 10m |
| 1.1.10 Create CacheService for manual eviction | ✅ Done | | 20m |
| 1.1.11 Create CacheController for API management | ✅ Done | Admin only | 20m |

**Subtotal: ~3.5h**

### 1.2 Scheduled Jobs

| Task | Status | Notes | Time |
|------|--------|-------|------|
| 1.2.1 Enhanced InventoryAlertScheduler | ⬜ | Refactor existing | 1h |
| 1.2.2 Low stock check (every 5 min) | ⬜ | @Scheduled | 1h |
| 1.2.3 Alert auto-resolve logic | ⬜ | When stock restored | 1h |
| 1.2.4 Daily summary job (midnight) | ⬜ | @Scheduled(cron) | 1h |
| 1.2.5 Test scheduled jobs | ⬜ | Unit tests | 1h |

**Subtotal: ~5h**

### 1.3 API Documentation (SpringDoc OpenAPI)

| Task | Status | Notes | Time |
|------|--------|-------|------|
| 1.3.1 Add @Tag for each controller | ⬜ | Group endpoints | 30m |
| 1.3.2 Add @Operation for each endpoint | ⬜ | Descriptions | 1h |
| 1.3.3 Add @ApiResponse examples | ⬜ | Response codes | 1h |
| 1.3.4 Add @Schema for DTOs | ⬜ | Field descriptions | 1h |
| 1.3.5 Document error responses | ⬜ | 400, 401, 403, 404, 500 | 1h |

**Subtotal: ~4.5h**

### 1.4 Integration Tests

| Task | Status | Notes | Time |
|------|--------|-------|------|
| 1.4.1 Setup H2 test database | ⬜ | @DataJpaTest | 30m |
| 1.4.2 InventoryServiceTest | ⬜ | Inbound, Outbound, Adjust | 2h |
| 1.4.3 AuthServiceTest | ⬜ | Login, Register | 1h |
| 1.4.4 Controller tests với MockMvc | ⬜ | REST endpoints | 2h |
| 1.4.5 Test coverage report | ⬜ | jacoco/pitest | 30m |

**Subtotal: ~6h**

### 1.5 File Processing

| Task | Status | Notes | Time |
|------|--------|-------|------|
| 1.5.1 Thêm Apache POI dependency | ⬜ | poi-ooxml | 10m |
| 1.5.2 Create FileExportService | ⬜ | | 1h |
| 1.5.3 Export products to Excel | ⬜ | Multiple sheets | 2h |
| 1.5.4 Export events to CSV | ⬜ | Audit trail | 1h |
| 1.5.5 Create FileImportService | ⬜ | | 1h |
| 1.5.6 Import products from Excel | ⬜ | Validation | 2h |
| 1.5.7 Test file operations | ⬜ | Unit tests | 1h |

**Subtotal: ~8h**

---

## PHASE 2: Frontend Completions

### 2.1 API Integration

| Task | Status | Notes | Time |
|------|--------|-------|------|
| 2.1.1 Setup axios với interceptors | ⬜ | Auth header, errors | 1h |
| 2.1.2 Create AuthContext | ⬜ | JWT handling | 1h |
| 2.1.3 Connect Dashboard stats API | ⬜ | | 1h |
| 2.1.4 Connect Inventory list API | ⬜ | | 1h |
| 2.1.5 Connect Product detail API | ⬜ | | 1h |
| 2.1.6 Connect Inbound API | ⬜ | | 1h |
| 2.1.7 Connect Outbound API | ⬜ | | 1h |
| 2.1.8 Connect Low Stock API | ⬜ | | 1h |
| 2.1.9 Connect Events API | ⬜ | | 1h |

**Subtotal: ~9h**

### 2.2 Error Handling & UX

| Task | Status | Notes | Time |
|------|--------|-------|------|
| 2.2.1 Global error interceptor | ⬜ | 401, 403 handling | 1h |
| 2.2.2 Toast notifications | ⬜ | react-hot-toast | 1h |
| 2.2.3 Loading skeletons | ⬜ | Dashboard, Tables | 1h |
| 2.2.4 Error boundaries | ⬜ | React error boundary | 1h |
| 2.2.5 Empty states | ⬜ | No data views | 1h |

**Subtotal: ~5h**

### 2.3 Form Validation

| Task | Status | Notes | Time |
|------|--------|-------|------|
| 2.3.1 Client-side validation | ⬜ | React Hook Form + Zod | 2h |
| 2.3.2 Real-time stock check | ⬜ | Outbound validation | 1h |
| 2.3.3 Submit button states | ⬜ | Disabled, Loading | 30m |

**Subtotal: ~3.5h**

---

## PHASE 3: Polish & Documentation

### 3.1 Security Enhancements

| Task | Status | Notes | Time |
|------|--------|-------|------|
| 3.1.1 Refresh token endpoint | ⬜ | | 2h |
| 3.1.2 Token expiration handling | ⬜ | Frontend | 1h |
| 3.1.3 Password change endpoint | ⬜ | | 1h |
| 3.1.4 Account lockout policy | ⬜ | After 5 failed attempts | 1h |

**Subtotal: ~5h**

### 3.2 Observability

| Task | Status | Notes | Time |
|------|--------|-------|------|
| 3.2.1 Custom health indicator | ⬜ | Database, Redis | 1h |
| 3.2.2 Structured logging | ⬜ | JSON format | 1h |
| 3.2.3 Request ID tracing | ⬜ | MDC | 1h |
| 3.2.4 Metrics endpoint | ⬜ | /actuator/metrics | 1h |

**Subtotal: ~4h**

### 3.3 Project Documentation

| Task | Status | Notes | Time |
|------|--------|-------|------|
| 3.3.1 Update README.md | ⬜ | Features, Setup | 2h |
| 3.3.2 Create API documentation | ⬜ | Postman collection | 1h |
| 3.3.3 Architecture diagram | ⬜ | draw.io/png | 1h |
| 3.3.4 Demo video/screenshot | ⬜ | GIF | 1h |

**Subtotal: ~5h**

---

## PROGRESS SUMMARY

| Phase | Tasks | Done | In Progress | TODO |
|-------|-------|:----:|:-----------:|:----:|
| 1.1 Redis | 11 | 11 | 0 | 0 |
| 1.2 Scheduled | 5 | 0 | 0 | 5 |
| 1.3 API Docs | 5 | 0 | 0 | 5 |
| 1.4 Tests | 5 | 0 | 0 | 5 |
| 1.5 File | 7 | 0 | 0 | 7 |
| 2.1 API | 9 | 0 | 0 | 9 |
| 2.2 UX | 5 | 0 | 0 | 5 |
| 2.3 Validation | 3 | 0 | 0 | 3 |
| 3.1 Security | 4 | 0 | 0 | 4 |
| 3.2 Observability | 4 | 0 | 0 | 4 |
| 3.3 Docs | 4 | 0 | 0 | 4 |
| **TOTAL** | **62** | **11** | **0** | **51** |

---

## QUICK START CHECKLIST

Làm những task này TRƯỚC để có demo sớm:

```
Priority 1 (Day 1):
☐ 1.1.1 Thêm Redis dependency
☐ 1.1.3 Tạo RedisConfig
☐ 1.1.4 EnableCaching
☐ 1.1.5 Cache getSnapshot
☐ 1.3.1 Add @Tag annotations

Priority 2 (Day 2):
☐ 1.4.1 Setup test database
☐ 1.4.2 InventoryServiceTest
☐ 2.1.1 Setup axios
☐ 2.1.2 AuthContext
☐ 2.1.3 Dashboard API

Priority 3 (Day 3):
☐ 2.1.6 Inbound API
☐ 2.1.7 Outbound API
☐ 2.2.1 Error interceptor
☐ 2.2.2 Toast notifications
☐ 1.2.1 Scheduled alert job
```

---

## WEEKLY TARGETS

```
Week 1: Phase 1.1 (Redis) + Phase 1.3 (API Docs)
Week 2: Phase 1.2 (Scheduled) + Phase 1.4 (Tests)
Week 3: Phase 2.1 (API Integration)
Week 4: Phase 2.2 + 2.3 (UX) + Phase 1.5 (File)
Week 5: Phase 3 (Polish)
```

---

## COMPLETED TASKS LOG

```
Date       | Task ID | Description                    | Time Spent
-----------|---------|--------------------------------|-----------
           |         |                                |
           |         |                                |
           |         |                                |
           |         |                                |
           |         |                                |
```
