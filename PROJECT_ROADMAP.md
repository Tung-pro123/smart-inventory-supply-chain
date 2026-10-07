# PROJECT ROADMAP - Smart Inventory & Supply Chain

> Chiến lược hoàn thiện dự án để impress nhà tuyển dụng (Fresher/Junior Intern)

---

## 1. PHÂN TÍCH HIỆN TRẠNG

### 1.1 Đã có ✅

```
Backend (Spring Boot 3.3.4 Monolith)
├── Spring Security + JWT (BCrypt, RBAC)
├── Spring Data JPA (Event Sourcing)
├── Validation (Bean Validation)
├── Actuator (Health checks)
├── SpringDoc OpenAPI (Swagger UI)
├── 20 Unit Tests
└── Docker + Docker Compose

Frontend (React 18 + Vite)
├── Components (Dashboard, Inventory, LowStock, Events)
├── Role-based Navigation
├── Mock Data
└── Tailwind CSS
```

### 1.2 Tech Stack Hiện Tại

| Layer | Technology | Level |
|-------|------------|-------|
| **Backend** | Java 17, Spring Boot 3.3 | ⭐⭐⭐ |
| **Security** | Spring Security, JWT, BCrypt, RBAC | ⭐⭐⭐ |
| **Database** | MySQL 8, JPA, Event Sourcing | ⭐⭐⭐ |
| **API** | REST, OpenAPI/Swagger | ⭐⭐ |
| **Frontend** | React 18, Vite, Tailwind | ⭐⭐ |
| **Testing** | JUnit 5, Mockito | ⭐⭐ |
| **DevOps** | Docker Compose | ⭐⭐ |

---

## 2. CÁC CẤP ĐỘ ĐỂ IMPRESS

### 2.1 Fresher Intern Level (Cơ bản - đã đạt 70%)

Những thứ bạn đã có:
- ✅ Spring Boot application structure
- ✅ JWT Authentication với RBAC
- ✅ Event Sourcing pattern
- ✅ REST API với OpenAPI docs
- ✅ Unit tests
- ✅ Clean code

### 2.2 Fresher Intern Level (Điểm cộng)

Những thứ nên có thêm:
- ⬜ Redis caching cho hot data
- ⬜ Scheduled jobs cho alert processing
- ⬜ File handling (Excel import/export)
- ⬜ Real-time notifications (WebSocket)
- ⬜ Integration tests
- ⬜ Better API documentation

### 2.3 Junior Level (Nâng cao)

Những thứ thể hiện kỹ năng vượt trội:
- ⬜ Spring Cloud Gateway (API Gateway)
- ⬜ Spring Cloud Config (Centralized config)
- ⬜ Redis Cluster / Session management
- ⬜ Message Queue (RabbitMQ/Kafka)
- ⬜ Circuit Breaker (Resilience4j)
- ⬜ Rate Limiting

---

## 3. KẾ HOẠCH HOÀN THIỆN (THEO THỨ TỰ ƯU TIÊN)

### PHASE 1: Backend Enhancements (2-3 tuần)

#### Week 1: Caching + Performance

```
Mục tiêu: Tối ưu performance với Redis
```

| Task | Mô tả | Priority | Status |
|------|--------|----------|--------|
| 1.1 | Thêm Redis dependency vào pom.xml | HIGH | ⬜ |
| 1.2 | Cấu hình Redis connection | HIGH | ⬜ |
| 1.3 | Cache inventory snapshots với @Cacheable | HIGH | ⬜ |
| 1.4 | Cache API responses (products, warehouses) | MEDIUM | ⬜ |
| 1.5 | Evict cache khi có inventory changes | HIGH | ⬜ |

**Code example:**
```java
@Cacheable(value = "inventory-snapshots", key = "#productId + '-' + #warehouseId")
public InventorySnapshotResponse getSnapshot(Long productId, Long warehouseId) {
    // ...
}

@CacheEvict(value = "inventory-snapshots", key = "#request.productId + '-' + #request.warehouseId")
public InventoryEventResponse processInbound(InboundRequest request, String username) {
    // ...
}
```

#### Week 2: Scheduled Jobs + Background Processing

```
Mục tiêu: Tự động hóa với @Scheduled
```

| Task | Mô tả | Priority | Status |
|------|--------|----------|--------|
| 2.1 | Enhanced InventoryAlertScheduler | HIGH | ⬜ |
| 2.2 | Scheduled stock level checks (every 5 min) | HIGH | ⬜ |
| 2.3 | Auto-resolve alerts when stock restored | MEDIUM | ⬜ |
| 2.4 | Daily inventory summary report job | MEDIUM | ⬜ |
| 2.5 | Log cleanup job (rotate old logs) | LOW | ⬜ |

**Code example:**
```java
@Scheduled(cron = "0 */5 * * * *") // Every 5 minutes
public void checkLowStockLevels() {
    List<InventorySnapshot> lowStockItems = snapshotRepository.findAllLowStock();
    for (InventorySnapshot snapshot : lowStockItems) {
        alertService.createOrUpdateAlert(snapshot);
    }
}
```

#### Week 3: File Processing

```
Mục tiêu: Import/Export data
```

| Task | Mô tả | Priority | Status |
|------|--------|----------|--------|
| 3.1 | Apache POI dependency | MEDIUM | ⬜ |
| 3.2 | Export products to Excel | MEDIUM | ⬜ |
| 3.3 | Export inventory events to Excel/CSV | MEDIUM | ⬜ |
| 3.4 | Import products from Excel | MEDIUM | ⬜ |
| 3.5 | Bulk inbound from Excel | MEDIUM | ⬜ |

### PHASE 2: Frontend Completions (1-2 tuần)

#### Week 4: Connect Frontend to Backend

```
Mục tiêu: Hook up React với Spring Boot API
```

| Task | Mô tả | Priority | Status |
|------|--------|----------|--------|
| 4.1 | Setup Axios với interceptors | HIGH | ⬜ |
| 4.2 | Auth context + JWT handling | HIGH | ⬜ |
| 4.3 | Connect Dashboard với API | HIGH | ⬜ |
| 4.4 | Connect Inventory list với API | HIGH | ⬜ |
| 4.5 | Connect Inbound/Outbound forms | HIGH | ⬜ |
| 4.6 | Connect Low Stock page | MEDIUM | ⬜ |
| 4.7 | Connect Event Log page | MEDIUM | ⬜ |
| 4.8 | Toast notifications | MEDIUM | ⬜ |

#### Week 5: UI Polish

```
Mục tiêu: Hoàn thiện UX
```

| Task | Mô tả | Priority | Status |
|------|--------|----------|--------|
| 5.1 | Loading states + Skeletons | MEDIUM | ⬜ |
| 5.2 | Error handling + Error boundaries | MEDIUM | ⬜ |
| 5.3 | Pagination cho large lists | MEDIUM | ⬜ |
| 5.4 | Optimistic updates | MEDIUM | ⬜ |
| 5.5 | Empty states | LOW | ⬜ |

### PHASE 3: Testing & Documentation (1 tuần)

#### Week 6: Testing Coverage

```
Mục tiêu: Tăng test coverage lên 70%+
```

| Task | Mô tả | Priority | Status |
|------|--------|----------|--------|
| 6.1 | Integration tests cho InventoryService | HIGH | ⬜ |
| 6.2 | Integration tests cho Auth flows | HIGH | ⬜ |
| 6.3 | Controller tests với MockMvc | MEDIUM | ⬜ |
| 6.4 | Repository tests với @DataJpaTest | MEDIUM | ⬜ |
| 6.5 | Testcontainers for MySQL (optional) | LOW | ⬜ |

#### Week 7: API Documentation

```
Mục tiêu: Professional API docs
```

| Task | Mô tả | Priority | Status |
|------|--------|----------|--------|
| 7.1 | OpenAPI annotations cho all endpoints | HIGH | ⬜ |
| 7.2 | Request/Response examples | MEDIUM | ⬜ |
| 7.3 | Error response documentation | MEDIUM | ⬜ |
| 7.4 | Postman collection | MEDIUM | ⬜ |

### PHASE 4: Advanced Features (Optional - thể hiện vượt trội)

#### Week 8-10: Spring Cloud Essentials

```
Mục tiêu: Thể hiện knowledge về microservices
```

| Task | Mô tả | Priority | Status |
|------|--------|----------|--------|
| 8.1 | Spring Cloud Gateway (API Gateway) | MEDIUM | ⬜ |
| 8.2 | Centralized config (Spring Cloud Config) | MEDIUM | ⬜ |
| 8.3 | Redis Session management | MEDIUM | ⬜ |
| 8.4 | Circuit Breaker (Resilience4j) | MEDIUM | ⬜ |
| 8.5 | Rate Limiting | MEDIUM | ⬜ |

#### Week 11-12: Real-time + Messaging

```
Mục tiêu: Real-time features
```

| Task | Mô tả | Priority | Status |
|------|--------|----------|--------|
| 9.1 | WebSocket cho stock updates | MEDIUM | ⬜ |
| 9.2 | STOMP protocol integration | MEDIUM | ⬜ |
| 9.3 | Real-time alerts notification | MEDIUM | ⬜ |
| 9.4 | RabbitMQ integration (optional) | LOW | ⬜ |

---

## 4. DETAILED TASK LIST

### 4.1 Backend Priority Tasks

```markdown
## CRITICAL (Phải có cho Fresher)

### B1: Redis Caching
- [ ] Thêm spring-boot-starter-data-redis
- [ ] Cấu hình Redis connection
- [ ] Cache inventory snapshots
- [ ] Cache products list
- [ ] Invalidate cache on updates

### B2: Enhanced API Documentation  
- [ ] @Operation, @ApiResponse annotations
- [ ] Example values cho request/response
- [ ] Group endpoints by tag
- [ ] Document error responses

### B3: Scheduled Jobs
- [ ] Low stock alert checker (5 min)
- [ ] Daily summary job
- [ ] Alert auto-resolve logic

### B4: Integration Tests
- [ ] InventoryServiceTest (in-memory DB)
- [ ] AuthServiceTest với JWT
- [ ] Controller tests

### B5: File Processing
- [ ] Export products to Excel
- [ ] Export events to CSV
- [ ] Import products from Excel

## IMPORTANT (Điểm cộng)

### B6: Enhanced Security
- [ ] Refresh token
- [ ] Password change endpoint
- [ ] Account lockout policy

### B7: Advanced Validation
- [ ] Custom validators
- [ ] Cross-field validation
- [ ] Better error messages

### B8: Observability
- [ ] Custom health indicators
- [ ] Metrics với Micrometer
- [ ] Structured logging
```

### 4.2 Frontend Priority Tasks

```markdown
## CRITICAL (Phải có)

### F1: API Integration
- [ ] Axios setup với interceptors
- [ ] Auth context với JWT
- [ ] Dashboard API
- [ ] Inventory list API
- [ ] Inbound/Outbound API
- [ ] Low Stock API
- [ ] Events API

### F2: Error Handling
- [ ] API error interceptor
- [ ] Toast notifications
- [ ] Error boundaries
- [ ] Loading states

### F3: Form Validation
- [ ] Client-side validation
- [ ] Error messages
- [ ] Disabled states

## IMPORTANT (Điểm cộng)

### F4: User Experience
- [ ] Optimistic updates
- [ ] Pagination
- [ ] Search debounce
- [ ] Empty states

### F5: Role-based Features
- [ ] Protected routes
- [ ] Conditional rendering
- [ ] Permission checks
```

---

## 5. SPRINT PLANNING

### Sprint 1: Foundation (Days 1-7)

```
Week 1 Tasks:
├── B1.1: Redis setup ✅ → ⬜ (2h)
├── B1.2: Cache inventory snapshots ⬜ (4h)
├── B1.3: Cache products ⬜ (2h)
├── B2.1: API documentation ⬜ (3h)
├── B2.2: Request examples ⬜ (2h)
└── F1.1: Axios setup ⬜ (2h)

Total: ~15h
```

### Sprint 2: Core Features (Days 8-14)

```
Week 2 Tasks:
├── B3.1: Scheduled alert job ⬜ (3h)
├── B4.1: Integration tests ⬜ (4h)
├── F1.2: Auth context ⬜ (3h)
├── F1.3: Dashboard API ⬜ (2h)
├── F1.4: Inventory API ⬜ (2h)
└── F2.1: Error handling ⬜ (2h)

Total: ~16h
```

### Sprint 3: Completion (Days 15-21)

```
Week 3 Tasks:
├── B5.1: Excel export ⬜ (4h)
├── B5.2: Excel import ⬜ (4h)
├── F1.5: Inbound/Outbound ⬜ (3h)
├── F1.6: Low Stock API ⬜ (2h)
├── F1.7: Events API ⬜ (2h)
└── F3.1: Form validation ⬜ (2h)

Total: ~17h
```

### Sprint 4: Polish (Days 22-28)

```
Week 4 Tasks:
├── B6.1: Refresh token ⬜ (3h)
├── B7.1: Custom validators ⬜ (2h)
├── F4.1: Optimistic updates ⬜ (3h)
├── F4.2: Pagination ⬜ (2h)
├── B8.1: Custom health indicator ⬜ (2h)
└── Documentation + README ⬜ (4h)

Total: ~16h
```

---

## 6. TECHNICAL SKILLS TO DEMONSTRATE

### 6.1 Đã có (Fresher Level)

| Skill | Evidence | Level |
|-------|----------|-------|
| Java Core | Entity classes, services | ⭐⭐⭐ |
| Spring Boot | Application structure | ⭐⭐⭐ |
| Spring Security | JWT + RBAC | ⭐⭐⭐ |
| Database Design | Event Sourcing, 3NF | ⭐⭐⭐ |
| REST API | Controllers, DTOs | ⭐⭐⭐ |
| Testing | Unit tests | ⭐⭐ |
| Git | Version control | ⭐⭐ |
| Docker | Containerization | ⭐⭐ |

### 6.2 Cần thể hiện thêm

| Skill | Evidence | Level |
|-------|----------|-------|
| Caching | Redis integration | ⭐⭐⭐ |
| Scheduled Jobs | @Scheduled annotations | ⭐⭐⭐ |
| Performance | Query optimization | ⭐⭐ |
| API Documentation | SpringDoc + OpenAPI | ⭐⭐⭐ |
| File Processing | Apache POI | ⭐⭐ |
| Real-time | WebSocket (optional) | ⭐⭐ |
| Microservices | Spring Cloud (optional) | ⭐⭐ |

---

## 7. PORTFOLIO PRESENTATION

### 7.1 GitHub README Structure

```
README.md
├── Banner image
├── Quick demo GIF
├── Tech Stack badges
├── Features (với emojis)
├── Architecture diagram
├── Getting Started
├── API Documentation link
├── Testing coverage
└── Demo credentials
```

### 7.2 Key Points để nhấn mạnh

1. **Event Sourcing Architecture**
   - Immutable audit trail
   - Balance verification
   - Historical reconstruction

2. **Security**
   - JWT với refresh token
   - BCrypt password hashing
   - Role-based access control

3. **Performance**
   - Redis caching
   - Optimistic locking
   - Database indexing

4. **Reliability**
   - Transaction management
   - Comprehensive tests
   - Error handling

5. **Developer Experience**
   - OpenAPI/Swagger docs
   - Docker Compose setup
   - Clean code structure

---

## 8. ESTIMATED TIMELINE

```
Week 1-2: Redis + Scheduled Jobs + API Docs
Week 3-4: Integration Tests + File Processing
Week 5-6: Frontend API Integration
Week 7-8: Polish + Documentation

Total: ~8 weeks (part-time)
```

---

## 9. RECOMMENDED LEARNING RESOURCES

### Để hiểu sâu hơn

1. **Redis Caching**
   - Baeldung: "Spring Cache"
   - Official Redis documentation

2. **Scheduled Jobs**
   - Baeldung: "@Scheduled Example"
   - Spring Batch (for complex jobs)

3. **Testing**
   - Baeldung: "Spring Boot Testing"
   - Testcontainers documentation

4. **Spring Cloud (Optional)**
   - Spring Cloud official guides
   - Microservices patterns

---

## 10. IMMEDIATE NEXT STEPS

### Bắt đầu ngay hôm nay:

```
1. [ ] Thêm Redis vào pom.xml
2. [ ] Cấu hình Redis connection
3. [ ] Implement @Cacheable cho inventory
4. [ ] Viết Integration test đầu tiên
5. [ ] Kết nối 1 API endpoint với frontend
```

### Priority Order:
```
1. Redis Caching (performance) - 3h
2. API Documentation (impress recruiters) - 2h  
3. Integration Tests (code quality) - 4h
4. Frontend API Integration (completeness) - 6h
5. File Processing (feature richness) - 4h
```

---

## 11. Q&A

### Q: Nên làm microservices không?
**A:** Không cần thiết cho Fresher level. Monolith với Spring Boot đã đủ ấn tượng. Nếu muốn, chỉ cần thêm Spring Cloud Gateway là đủ.

### Q: Cần bao nhiêu test coverage?
**A:** 50-70% là target tốt cho Fresher. Tập trung vào Service layer tests.

### Q: Có cần Docker không?
**A:** Có, đã có docker-compose.yml. Đảm bảo nó chạy được.

### Q: Frontend hay Backend quan trọng hơn?
**A:** Backend quan trọng hơn cho Fresher Java. Frontend chỉ cần đủ để demo.

---

**File tiếp theo**: [TODO List](./TODO.md)
