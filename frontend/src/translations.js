export const translations = {
  vi: {
    brandName: 'Smart Inventory',
    brandSub: 'Hệ Thống Quản Lý Kho & Chuỗi Cung Ứng',
    liveTelemetry: 'Hệ thống hoạt động',
    quickSearch: 'Tìm kiếm nhanh SKU, tên hàng...',
    profileRole: 'Điều hành viên',
    profileDept: 'Quản trị phân phối',
    
    // Navigation Tabs
    tabs: {
      dashboard: 'Tổng quan & Bảng kho',
      inventory: 'Danh mục tồn kho',
      lowStock: 'Cảnh báo thiếu hàng',
      events: 'Nhật ký xuất nhập'
    },

    // KPI Cards
    kpis: {
      catalogVolume: 'Tổng số mặt hàng (SKU)',
      catalogNote: 'Trên tất cả các kho lưu trữ',
      lowStockBuffer: 'Cảnh báo thiếu hàng',
      lowStockNote: 'Cần bổ sung nguồn cung ngay',
      todayThroughput: 'Giao dịch hôm nay',
      todayNote: '28 Nhập kho · 19 Xuất kho',
      assetValuation: 'Tổng giá trị lưu kho',
      assetNote: 'Công suất sử dụng: 78.4%'
    },

    // Table Section
    tableTitle: 'Bảng Quản Lý Tồn Kho & Cung Ứng Chi Tiết',
    tableDesc: 'Kiểm kê trực tiếp từng mã linh kiện, số lượng thực tế, vị trí phân bổ và ngưỡng dự phòng an toàn.',
    searchPlaceholder: 'Lọc theo mã SKU hoặc tên linh kiện...',
    filterCategory: 'Danh mục',
    filterStatus: 'Trạng thái',
    allCategories: 'Tất cả danh mục',
    allStatuses: 'Tất cả trạng thái',
    categories: {
      Sensors: 'Cảm biến (Sensors)',
      ECU: 'Hộp điều khiển ECU',
      Actuators: 'Cơ cấu chấp hành',
      'Braking Systems': 'Hệ thống phanh',
      'Power Electronics': 'Điện tử công suất',
      Capacitors: 'Tụ điện & Linh kiện phụ'
    },
    statuses: {
      ok: 'Đủ hàng',
      warning: 'Sắp hết',
      critical: 'Cần nhập gấp',
      out: 'Hết hàng'
    },

    // Table Headers
    cols: {
      sku: 'Mã SKU',
      item: 'Tên linh kiện & Phân loại',
      warehouse: 'Vị trí kho',
      stock: 'Tồn kho / Ngưỡng',
      level: 'Mức tồn dự phòng',
      status: 'Trạng thái',
      actions: 'Thao tác'
    },

    // Actions & Buttons
    btnInbound: 'Nhập kho',
    btnOutbound: 'Xuất kho',
    btnFilter: 'Bộ lọc',
    btnExport: 'Xuất báo cáo Excel',
    btnViewDetails: 'Chi tiết',
    noItemsFound: 'Không tìm thấy linh kiện nào khớp với điều kiện lọc.',
    showingCount: 'Đang hiển thị {count} / {total} linh kiện',

    // Modal
    modalTitle: 'Cập nhật kho hàng nhanh',
    modalInboundTitle: 'Nhập kho linh kiện',
    modalOutboundTitle: 'Xuất kho linh kiện',
    modalQty: 'Số lượng thay đổi',
    modalReason: 'Lý do / Số phiếu chứng từ',
    modalConfirm: 'Xác nhận thực hiện',
    modalCancel: 'Hủy bỏ',
    modalSuccess: 'Thao tác cập nhật tồn kho thành công!',

    // Footer
    footerCopy: '© 2026 Smart Inventory Engine — Hệ Thống Điều Vận Kho Thông Minh',
    footerIso: 'Tiêu chuẩn ISO-9001',
    footerLatency: 'Độ trễ API: 22ms'
  },

  en: {
    brandName: 'Smart Inventory',
    brandSub: 'Supply Chain & Warehouse Management',
    liveTelemetry: 'Live Telemetry',
    quickSearch: 'Quick search SKU, item...',
    profileRole: 'Warehouse Operator',
    profileDept: 'Dispatch Admin',

    // Navigation Tabs
    tabs: {
      dashboard: 'Stock Overview & Table',
      inventory: 'All Inventory',
      lowStock: 'Low Stock Alerts',
      events: 'Activity Log'
    },

    // KPI Cards
    kpis: {
      catalogVolume: 'Total Catalog Volume',
      catalogNote: 'Across all active warehouse zones',
      lowStockBuffer: 'Low Stock Alerts',
      lowStockNote: 'Requires replenishment soon',
      todayThroughput: "Today's Operations",
      todayNote: '28 Inbound · 19 Outbound',
      assetValuation: 'Total Asset Valuation',
      assetNote: 'Warehouse Capacity: 78.4%'
    },

    // Table Section
    tableTitle: 'Detailed Stock Inventory & Logistics Table',
    tableDesc: 'Real-time item audit by SKU code, warehouse zones, on-hand count, and safety buffers.',
    searchPlaceholder: 'Search by SKU or item name...',
    filterCategory: 'Category',
    filterStatus: 'Status',
    allCategories: 'All Categories',
    allStatuses: 'All Statuses',
    categories: {
      Sensors: 'Sensors',
      ECU: 'ECU Control Units',
      Actuators: 'Micro-Actuators',
      'Braking Systems': 'Braking Systems',
      'Power Electronics': 'Power Electronics',
      Capacitors: 'Capacitors & Passives'
    },
    statuses: {
      ok: 'In Stock',
      warning: 'Low Stock',
      critical: 'Critical Alert',
      out: 'Out of Stock'
    },

    // Table Headers
    cols: {
      sku: 'SKU Code',
      item: 'Part Name & Category',
      warehouse: 'Warehouse Zone',
      stock: 'Stock / Min Safety',
      level: 'Safety Stock Level',
      status: 'Status',
      actions: 'Actions'
    },

    // Actions & Buttons
    btnInbound: 'Inbound',
    btnOutbound: 'Outbound',
    btnFilter: 'Filter',
    btnExport: 'Export to Excel',
    btnViewDetails: 'Details',
    noItemsFound: 'No items match the current filter criteria.',
    showingCount: 'Showing {count} of {total} items',

    // Modal
    modalTitle: 'Quick Stock Adjustment',
    modalInboundTitle: 'Record Inbound Shipment',
    modalOutboundTitle: 'Record Outbound Dispatch',
    modalQty: 'Quantity',
    modalReason: 'Reference / Invoice Code',
    modalConfirm: 'Confirm Action',
    modalCancel: 'Cancel',
    modalSuccess: 'Stock updated successfully!',

    // Footer
    footerCopy: '© 2026 Smart Inventory Engine — Automated Logistics Platform',
    footerIso: 'ISO-9001 Compliant',
    footerLatency: 'API Telemetry Latency: 22ms'
  }
};
