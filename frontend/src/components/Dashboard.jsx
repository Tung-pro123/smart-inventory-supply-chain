import React from 'react';
import { ArrowRight, Layers, AlertTriangle, Activity } from 'lucide-react';

const ROLES = {
  ADMIN: 'ADMIN',
  MANAGER: 'MANAGER',
  STAFF: 'STAFF',
};

// Mock data - từ API thực tế
const DASHBOARD_DATA = {
  totalSKUs: 1482,
  lowStockCount: 6,
  todayMovements: 47,
};

// Base metrics - tất cả roles đều thấy
const BASE_METRICS = [
  {
    id: 'inventory',
    label: 'Total Products',
    value: DASHBOARD_DATA.totalSKUs.toLocaleString(),
    subtext: 'Active SKUs in catalog',
    icon: Layers,
    color: 'blue',
    page: 'inventory',
  },
];

// Manager+ metrics - ẩn với Staff
const MANAGER_METRICS = [
  {
    id: 'low-stock',
    label: 'Low Stock Alerts',
    value: DASHBOARD_DATA.lowStockCount,
    subtext: 'Items below threshold',
    icon: AlertTriangle,
    color: 'amber',
    page: 'low-stock',
  },
  {
    id: 'events',
    label: "Today's Movements",
    value: DASHBOARD_DATA.todayMovements,
    subtext: 'Inbound + Outbound events',
    icon: Activity,
    color: 'emerald',
    page: 'events',
  },
];

const COLOR_MAP = {
  blue: {
    bg: 'bg-blue-500/10',
    text: 'text-blue-400',
    border: 'border-blue-500/20 hover:border-blue-500/40',
  },
  amber: {
    bg: 'bg-amber-500/10',
    text: 'text-amber-400',
    border: 'border-amber-500/20 hover:border-amber-500/40',
  },
  emerald: {
    bg: 'bg-emerald-500/10',
    text: 'text-emerald-400',
    border: 'border-emerald-500/20 hover:border-emerald-500/40',
  },
};

export default function Dashboard({ user, onNavigate }) {
  // Staff chỉ thấy 1 metric, Manager+ thấy 3
  const canSeeManagerMetrics = user?.role === ROLES.ADMIN || user?.role === ROLES.MANAGER;
  const metrics = canSeeManagerMetrics
    ? [...BASE_METRICS, ...MANAGER_METRICS]
    : BASE_METRICS;

  const getWelcomeMessage = () => {
    switch (user?.role) {
      case ROLES.ADMIN:
        return 'Admin dashboard with full access';
      case ROLES.MANAGER:
        return 'Manager dashboard - monitor warehouse operations';
      case ROLES.STAFF:
        return 'Staff dashboard - process inbound/outbound';
      default:
        return 'Welcome to Smart Inventory';
    }
  };

  return (
    <div className="space-y-6">
      {/* Welcome */}
      <div>
        <h2 className="text-lg font-semibold text-white">Overview</h2>
        <p className="text-sm text-slate-400">{getWelcomeMessage()}</p>
      </div>

      {/* Metric Cards - Số lượng thay đổi theo role */}
      <div className={`grid gap-4 ${metrics.length === 1 ? 'grid-cols-1 md:grid-cols-1 max-w-md' : 'grid-cols-1 md:grid-cols-3'}`}>
        {metrics.map((metric) => {
          const Icon = metric.icon;
          const colors = COLOR_MAP[metric.color];

          return (
            <button
              key={metric.id}
              onClick={() => onNavigate(metric.page)}
              className={`
                w-full text-left p-5 rounded-xl border transition-all
                bg-slate-900/40 ${colors.border}
                hover:scale-[1.02]
              `}
            >
              <div className="flex items-start justify-between">
                <div>
                  <p className="text-xs font-medium text-slate-400 uppercase tracking-wider">
                    {metric.label}
                  </p>
                  <h3 className={`text-3xl font-bold ${colors.text} mt-1`}>
                    {metric.value}
                  </h3>
                  <p className="text-xs text-slate-500 mt-1">
                    {metric.subtext}
                  </p>
                </div>
                <div className={`p-2.5 rounded-lg ${colors.bg}`}>
                  <Icon className={`w-5 h-5 ${colors.text}`} />
                </div>
              </div>
              <div className="flex items-center gap-1 mt-3 text-xs text-slate-500">
                <span>Click to view details</span>
                <ArrowRight className="w-3 h-3" />
              </div>
            </button>
          );
        })}
      </div>

      {/* Role indicator */}
      <div className="bg-slate-900/20 border border-slate-800 rounded-xl p-4">
        <p className="text-xs text-slate-500 text-center">
          {canSeeManagerMetrics
            ? 'You have access to all features (Manager/Admin view)'
            : 'You have access to Dashboard and Inventory (Staff view)'
          }
        </p>
      </div>
    </div>
  );
}
