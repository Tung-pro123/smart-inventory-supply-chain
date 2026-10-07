import React, { useState } from 'react';
import { ArrowDownLeft, ArrowUpRight, RefreshCw, Filter, Lock } from 'lucide-react';

// Mock events - immutable audit log
const MOCK_EVENTS = [
  { id: 1, type: 'INBOUND', sku: 'BOSCH-SEN-001', product: 'Industrial Radar Distance Sensor', quantity: 50, balance: 95, operator: 'nguyen.van.a', timestamp: '2024-01-15 09:23', ref: 'PO-2024-0123' },
  { id: 2, type: 'OUTBOUND', sku: 'BOSCH-BRK-004', product: 'High-Performance Brake Caliper Pad', quantity: -8, balance: 14, operator: 'tran.thi.b', timestamp: '2024-01-15 10:45', ref: 'SO-2024-0089' },
  { id: 3, type: 'ADJUSTMENT', sku: 'BOSCH-ECU-002', product: 'Automotive Electronic Control Unit', quantity: -2, balance: 8, operator: 'admin', timestamp: '2024-01-15 11:30', ref: 'ADJ-2024-0045' },
  { id: 4, type: 'INBOUND', sku: 'BOSCH-ACT-003', product: 'Hydraulic Micro-Actuator 24V', quantity: 30, balance: 120, operator: 'nguyen.van.a', timestamp: '2024-01-15 14:20', ref: 'PO-2024-0124' },
  { id: 5, type: 'OUTBOUND', sku: 'BOSCH-INV-005', product: 'Power Inverter Module 400V', quantity: -5, balance: 62, operator: 'le.van.c', timestamp: '2024-01-15 15:00', ref: 'SO-2024-0090' },
  { id: 6, type: 'INBOUND', sku: 'BOSCH-CAP-008', product: 'Ceramic Capacitor 100nF', quantity: 100, balance: 103, operator: 'admin', timestamp: '2024-01-15 16:30', ref: 'PO-2024-0125' },
];

function getEventStyle(type) {
  switch (type) {
    case 'INBOUND':
      return { bg: 'bg-emerald-500/10', text: 'text-emerald-400', border: 'border-emerald-500/20', icon: ArrowDownLeft, label: 'Inbound' };
    case 'OUTBOUND':
      return { bg: 'bg-indigo-500/10', text: 'text-indigo-400', border: 'border-indigo-500/20', icon: ArrowUpRight, label: 'Outbound' };
    case 'ADJUSTMENT':
      return { bg: 'bg-amber-500/10', text: 'text-amber-400', border: 'border-amber-500/20', icon: RefreshCw, label: 'Adjustment' };
    default:
      return { bg: 'bg-slate-500/10', text: 'text-slate-400', border: 'border-slate-500/20', icon: RefreshCw, label: type };
  }
}

export default function EventLogPage({ user }) {
  const [filterType, setFilterType] = useState('ALL');

  const filtered = filterType === 'ALL'
    ? MOCK_EVENTS
    : MOCK_EVENTS.filter(e => e.type === filterType);

  const counts = {
    ALL: MOCK_EVENTS.length,
    INBOUND: MOCK_EVENTS.filter(e => e.type === 'INBOUND').length,
    OUTBOUND: MOCK_EVENTS.filter(e => e.type === 'OUTBOUND').length,
    ADJUSTMENT: MOCK_EVENTS.filter(e => e.type === 'ADJUSTMENT').length,
  };

  return (
    <div className="space-y-4">
      {/* Header */}
      <div>
        <h2 className="text-lg font-semibold text-white">Event Log</h2>
        <p className="text-sm text-slate-400">
          Immutable audit trail • {filtered.length} events
        </p>
      </div>

      {/* Immutable Notice */}
      <div className="flex items-center gap-2 text-xs text-slate-500 bg-slate-900/30 border border-slate-800 rounded-lg px-3 py-2">
        <Lock className="w-3.5 h-3.5" />
        <span>Events cannot be edited or deleted</span>
      </div>

      {/* Filter Chips */}
      <div className="flex items-center gap-2 flex-wrap">
        <Filter className="w-4 h-4 text-slate-500" />
        {['ALL', 'INBOUND', 'OUTBOUND', 'ADJUSTMENT'].map((type) => (
          <button
            key={type}
            onClick={() => setFilterType(type)}
            className={`
              px-3 py-1.5 rounded-lg text-xs font-medium transition-all
              ${filterType === type
                ? 'bg-indigo-600/20 text-indigo-300 border border-indigo-500/30'
                : 'text-slate-400 hover:text-slate-200 bg-slate-800/50 border border-slate-700/50'
              }
            `}
          >
            {type === 'ALL' ? 'All' : type.charAt(0) + type.slice(1).toLowerCase()} ({counts[type]})
          </button>
        ))}
      </div>

      {/* Event List */}
      <div className="space-y-2">
        {filtered.map((event) => {
          const style = getEventStyle(event.type);
          const Icon = style.icon;
          const isPositive = event.quantity > 0;

          return (
            <div
              key={event.id}
              className={`p-4 rounded-xl border bg-slate-900/40 ${style.border}`}
            >
              <div className="flex items-center gap-4">
                {/* Type Icon */}
                <div className={`p-2 rounded-lg ${style.bg}`}>
                  <Icon className={`w-4 h-4 ${style.text}`} />
                </div>

                {/* Main Info */}
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2">
                    <span className={`px-1.5 py-0.5 rounded text-xs font-medium ${style.bg} ${style.text}`}>
                      {style.label}
                    </span>
                    <span className="text-xs font-mono text-indigo-400">{event.sku}</span>
                  </div>
                  <p className="text-sm text-white mt-1 truncate">{event.product}</p>
                  <p className="text-xs text-slate-500 mt-0.5">
                    {event.operator} • {event.timestamp} • {event.ref}
                  </p>
                </div>

                {/* Quantity Change */}
                <div className="text-right">
                  <p className={`text-lg font-bold ${isPositive ? 'text-emerald-400' : 'text-rose-400'}`}>
                    {isPositive ? '+' : ''}{event.quantity}
                  </p>
                  <p className="text-xs text-slate-500">→ {event.balance}</p>
                </div>
              </div>
            </div>
          );
        })}

        {filtered.length === 0 && (
          <div className="p-8 text-center text-slate-500 text-sm">
            No events match the selected filter
          </div>
        )}
      </div>
    </div>
  );
}
