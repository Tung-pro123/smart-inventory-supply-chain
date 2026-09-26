import React, { useState } from 'react';
import { 
  Boxes, 
  AlertTriangle, 
  TrendingUp, 
  ShieldCheck, 
  ArrowDownLeft, 
  ArrowUpRight, 
  Search, 
  RefreshCw,
  Activity,
  Layers,
  Database
} from 'lucide-react';

export default function App() {
  const [searchTerm, setSearchTerm] = useState('');
  const [activeTab, setActiveTab] = useState('inventory');

  // Initial mockup data demonstrating industrial inventory
  const mockProducts = [
    { id: 1, sku: 'BOSCH-SEN-001', name: 'Industrial Radar Distance Sensor', category: 'Sensors', stock: 45, threshold: 15, status: 'OPTIMAL' },
    { id: 2, sku: 'BOSCH-ECU-002', name: 'Automotive Electronic Control Unit', category: 'ECU', stock: 8, threshold: 10, status: 'LOW_STOCK' },
    { id: 3, sku: 'BOSCH-ACT-003', name: 'Hydraulic Micro-Actuator 24V', category: 'Actuators', stock: 120, threshold: 20, status: 'OPTIMAL' },
    { id: 4, sku: 'BOSCH-BRK-004', name: 'High-Performance Brake Caliper Pad', category: 'Braking Systems', stock: 14, threshold: 25, status: 'CRITICAL' },
    { id: 5, sku: 'BOSCH-INV-005', name: 'Power Inverter Module 400V', category: 'Power Electronics', stock: 62, threshold: 15, status: 'OPTIMAL' },
  ];

  const filtered = mockProducts.filter(p => 
    p.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    p.sku.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col font-sans">
      {/* Top Navigation */}
      <header className="border-b border-slate-800 bg-slate-900/80 backdrop-blur px-6 py-4 flex items-center justify-between sticky top-0 z-50">
        <div className="flex items-center space-x-3">
          <div className="p-2 bg-indigo-600/20 text-indigo-400 rounded-lg border border-indigo-500/30">
            <Boxes className="w-6 h-6" />
          </div>
          <div>
            <h1 className="text-lg font-bold tracking-tight text-white flex items-center gap-2">
              Smart Inventory & Supply Chain
              <span className="text-xs px-2 py-0.5 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 font-mono">
                LIVE DEMO
              </span>
            </h1>
            <p className="text-xs text-slate-400">Industrial Logistics Tracking Engine • Event-Sourced Architecture</p>
          </div>
        </div>

        <div className="flex items-center space-x-4">
          <div className="flex items-center space-x-2 text-xs text-slate-300 bg-slate-800/80 px-3 py-1.5 rounded-md border border-slate-700">
            <Database className="w-3.5 h-3.5 text-indigo-400" />
            <span>MySQL 8.0 • Spring Boot 3.3</span>
          </div>
          <div className="flex items-center space-x-2 text-xs text-slate-300 bg-slate-800/80 px-3 py-1.5 rounded-md border border-slate-700">
            <ShieldCheck className="w-3.5 h-3.5 text-emerald-400" />
            <span>RBAC: ADMIN</span>
          </div>
        </div>
      </header>

      {/* Main Content Area */}
      <main className="flex-1 max-w-7xl w-full mx-auto p-6 space-y-6">
        {/* KPI Metric Cards */}
        <section className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="bg-slate-900/60 border border-slate-800 p-5 rounded-xl flex items-center justify-between">
            <div>
              <p className="text-xs font-medium text-slate-400 uppercase tracking-wider">Total Active SKUs</p>
              <h3 className="text-2xl font-bold text-white mt-1">1,482</h3>
              <p className="text-xs text-emerald-400 flex items-center mt-1">
                <ArrowUpRight className="w-3 h-3 mr-0.5" /> +12 new this week
              </p>
            </div>
            <div className="p-3 bg-blue-500/10 text-blue-400 rounded-lg">
              <Layers className="w-6 h-6" />
            </div>
          </div>

          <div className="bg-slate-900/60 border border-slate-800 p-5 rounded-xl flex items-center justify-between">
            <div>
              <p className="text-xs font-medium text-slate-400 uppercase tracking-wider">Low Stock Alerts</p>
              <h3 className="text-2xl font-bold text-amber-400 mt-1">6 Items</h3>
              <p className="text-xs text-amber-400/80 mt-1">Threshold trigger active</p>
            </div>
            <div className="p-3 bg-amber-500/10 text-amber-400 rounded-lg">
              <AlertTriangle className="w-6 h-6" />
            </div>
          </div>

          <div className="bg-slate-900/60 border border-slate-800 p-5 rounded-xl flex items-center justify-between">
            <div>
              <p className="text-xs font-medium text-slate-400 uppercase tracking-wider">24h Inbound / Outbound</p>
              <h3 className="text-2xl font-bold text-white mt-1">340 moves</h3>
              <p className="text-xs text-indigo-400 mt-1">Audit log updated O(1)</p>
            </div>
            <div className="p-3 bg-indigo-500/10 text-indigo-400 rounded-lg">
              <Activity className="w-6 h-6" />
            </div>
          </div>

          <div className="bg-slate-900/60 border border-slate-800 p-5 rounded-xl flex items-center justify-between">
            <div>
              <p className="text-xs font-medium text-slate-400 uppercase tracking-wider">Event Integrity</p>
              <h3 className="text-2xl font-bold text-emerald-400 mt-1">100.0%</h3>
              <p className="text-xs text-slate-400 mt-1">Event replay verified</p>
            </div>
            <div className="p-3 bg-emerald-500/10 text-emerald-400 rounded-lg">
              <ShieldCheck className="w-6 h-6" />
            </div>
          </div>
        </section>

        {/* Catalog Control Bar */}
        <section className="bg-slate-900/40 border border-slate-800 rounded-xl p-5 space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
            <div className="relative flex-1 max-w-md">
              <Search className="w-4 h-4 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                type="text"
                placeholder="Search by SKU, part name, or category..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full bg-slate-950 border border-slate-700/80 rounded-lg pl-9 pr-4 py-2 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:border-indigo-500"
              />
            </div>

            <div className="flex items-center space-x-3">
              <button className="px-3.5 py-2 text-xs font-semibold bg-emerald-600 hover:bg-emerald-500 text-white rounded-lg flex items-center space-x-1.5 transition">
                <ArrowDownLeft className="w-4 h-4" />
                <span>Quick Inbound</span>
              </button>
              <button className="px-3.5 py-2 text-xs font-semibold bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg flex items-center space-x-1.5 transition">
                <ArrowUpRight className="w-4 h-4" />
                <span>Process Outbound</span>
              </button>
            </div>
          </div>

          {/* Product Table */}
          <div className="overflow-x-auto rounded-lg border border-slate-800">
            <table className="w-full text-left text-sm text-slate-300">
              <thead className="bg-slate-900/90 text-xs uppercase text-slate-400 border-b border-slate-800">
                <tr>
                  <th className="px-4 py-3">SKU Identifier</th>
                  <th className="px-4 py-3">Component Name</th>
                  <th className="px-4 py-3">Category</th>
                  <th className="px-4 py-3 text-right">Current Stock</th>
                  <th className="px-4 py-3 text-right">Threshold</th>
                  <th className="px-4 py-3 text-center">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800/60 bg-slate-950/40">
                {filtered.map((item) => (
                  <tr key={item.id} className="hover:bg-slate-900/40 transition">
                    <td className="px-4 py-3.5 font-mono text-xs text-indigo-400 font-semibold">{item.sku}</td>
                    <td className="px-4 py-3.5 font-medium text-slate-100">{item.name}</td>
                    <td className="px-4 py-3.5 text-slate-400 text-xs">{item.category}</td>
                    <td className="px-4 py-3.5 text-right font-mono font-semibold">{item.stock}</td>
                    <td className="px-4 py-3.5 text-right font-mono text-slate-400">{item.threshold}</td>
                    <td className="px-4 py-3.5 text-center">
                      {item.status === 'OPTIMAL' && (
                        <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                          Optimal
                        </span>
                      )}
                      {item.status === 'LOW_STOCK' && (
                        <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-amber-500/10 text-amber-400 border border-amber-500/20">
                          Low Stock
                        </span>
                      )}
                      {item.status === 'CRITICAL' && (
                        <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-rose-500/10 text-rose-400 border border-rose-500/20">
                          Critical Alert
                        </span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="border-t border-slate-800 bg-slate-950 py-4 px-6 text-center text-xs text-slate-500">
        Engineered by Le Thanh Tung (FPT University) • Designed for Bosch Full-Stack Internship Engineering Showcase
      </footer>
    </div>
  );
}
