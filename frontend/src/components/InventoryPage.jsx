import React, { useState } from 'react';
import { Search, ArrowUpRight, ArrowDownLeft, X, Package } from 'lucide-react';

// Mock products
const MOCK_PRODUCTS = [
  { id: 1, sku: 'BOSCH-SEN-001', name: 'Industrial Radar Distance Sensor', category: 'Sensors', stock: 45, threshold: 15, warehouse: 'Main Warehouse' },
  { id: 2, sku: 'BOSCH-ECU-002', name: 'Automotive Electronic Control Unit', category: 'ECU', stock: 8, threshold: 10, warehouse: 'Main Warehouse' },
  { id: 3, sku: 'BOSCH-ACT-003', name: 'Hydraulic Micro-Actuator 24V', category: 'Actuators', stock: 120, threshold: 20, warehouse: 'Zone A' },
  { id: 4, sku: 'BOSCH-BRK-004', name: 'High-Performance Brake Caliper Pad', category: 'Braking Systems', stock: 14, threshold: 25, warehouse: 'Zone B' },
  { id: 5, sku: 'BOSCH-INV-005', name: 'Power Inverter Module 400V', category: 'Power Electronics', stock: 62, threshold: 15, warehouse: 'Main Warehouse' },
  { id: 6, sku: 'BOSCH-CAP-008', name: 'Ceramic Capacitor 100nF', category: 'Capacitors', stock: 3, threshold: 50, warehouse: 'Zone B' },
];

function StatusBadge({ stock, threshold }) {
  if (stock <= 0) {
    return <span className="px-2 py-0.5 rounded text-xs font-medium bg-rose-500/10 text-rose-400 border border-rose-500/20">Out</span>;
  }
  if (stock <= threshold) {
    return <span className="px-2 py-0.5 rounded text-xs font-medium bg-amber-500/10 text-amber-400 border border-amber-500/20">Low</span>;
  }
  return <span className="px-2 py-0.5 rounded text-xs font-medium bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">OK</span>;
}

function InboundModal({ product, onClose }) {
  const [quantity, setQuantity] = useState(10);
  const [refNumber, setRefNumber] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = () => {
    if (!quantity || quantity <= 0) return;
    setSubmitting(true);
    // Simulate API call
    setTimeout(() => {
      setSubmitting(false);
      onClose();
    }, 800);
  };

  if (!product) return null;

  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4"
         onClick={onClose}>
      <div className="bg-slate-900 border border-slate-700 rounded-2xl w-full max-w-md overflow-hidden"
           onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="p-5 border-b border-slate-800 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-emerald-500/10 rounded-lg">
              <ArrowDownLeft className="w-5 h-5 text-emerald-400" />
            </div>
            <div>
              <p className="text-xs text-slate-500">Inbound</p>
              <h3 className="text-base font-semibold text-white">{product.name}</h3>
            </div>
          </div>
          <button onClick={onClose} className="p-1.5 text-slate-400 hover:text-white hover:bg-slate-800 rounded-lg transition">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Form */}
        <div className="p-5 space-y-4">
          <div className="flex items-center gap-2 text-xs text-slate-400 bg-slate-800/50 rounded-lg p-3">
            <Package className="w-4 h-4" />
            <span className="font-mono text-indigo-400">{product.sku}</span>
            <span>•</span>
            <span>Current: {product.stock} units</span>
          </div>

          <div>
            <label className="block text-xs text-slate-400 mb-1.5">Quantity</label>
            <input
              type="number"
              min="1"
              value={quantity}
              onChange={(e) => setQuantity(parseInt(e.target.value) || 0)}
              className="w-full bg-slate-950 border border-slate-700 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:border-emerald-500"
              placeholder="Enter quantity"
            />
          </div>

          <div>
            <label className="block text-xs text-slate-400 mb-1.5">Reference Number</label>
            <input
              type="text"
              value={refNumber}
              onChange={(e) => setRefNumber(e.target.value)}
              className="w-full bg-slate-950 border border-slate-700 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:border-emerald-500"
              placeholder="e.g., PO-2024-0123"
            />
          </div>
        </div>

        {/* Actions */}
        <div className="p-4 border-t border-slate-800 flex gap-3">
          <button
            onClick={onClose}
            className="flex-1 px-4 py-2.5 bg-slate-800 hover:bg-slate-700 text-slate-300 rounded-xl font-medium transition"
          >
            Cancel
          </button>
          <button
            onClick={handleSubmit}
            disabled={!quantity || quantity <= 0 || submitting}
            className="flex-1 px-4 py-2.5 bg-emerald-600 hover:bg-emerald-500 disabled:bg-slate-700 disabled:text-slate-500 text-white rounded-xl font-medium transition"
          >
            {submitting ? 'Processing...' : 'Confirm Inbound'}
          </button>
        </div>
      </div>
    </div>
  );
}

function OutboundModal({ product, onClose }) {
  const [quantity, setQuantity] = useState(1);
  const [refNumber, setRefNumber] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = () => {
    if (quantity > product.stock) {
      setError(`Insufficient stock. Available: ${product.stock}`);
      return;
    }
    setError('');
    setSubmitting(true);
    setTimeout(() => {
      setSubmitting(false);
      onClose();
    }, 800);
  };

  if (!product) return null;

  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4"
         onClick={onClose}>
      <div className="bg-slate-900 border border-slate-700 rounded-2xl w-full max-w-md overflow-hidden"
           onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="p-5 border-b border-slate-800 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-indigo-500/10 rounded-lg">
              <ArrowUpRight className="w-5 h-5 text-indigo-400" />
            </div>
            <div>
              <p className="text-xs text-slate-500">Outbound</p>
              <h3 className="text-base font-semibold text-white">{product.name}</h3>
            </div>
          </div>
          <button onClick={onClose} className="p-1.5 text-slate-400 hover:text-white hover:bg-slate-800 rounded-lg transition">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Form */}
        <div className="p-5 space-y-4">
          <div className="flex items-center gap-2 text-xs text-slate-400 bg-slate-800/50 rounded-lg p-3">
            <Package className="w-4 h-4" />
            <span className="font-mono text-indigo-400">{product.sku}</span>
            <span>•</span>
            <span>Available: {product.stock} units</span>
          </div>

          <div>
            <label className="block text-xs text-slate-400 mb-1.5">Quantity</label>
            <input
              type="number"
              min="1"
              max={product.stock}
              value={quantity}
              onChange={(e) => {
                setQuantity(parseInt(e.target.value) || 0);
                setError('');
              }}
              className={`w-full bg-slate-950 border rounded-lg px-4 py-2.5 text-white focus:outline-none ${error ? 'border-rose-500' : 'border-slate-700 focus:border-indigo-500'}`}
            />
            {error && <p className="text-xs text-rose-400 mt-1">{error}</p>}
          </div>

          <div>
            <label className="block text-xs text-slate-400 mb-1.5">Reference Number</label>
            <input
              type="text"
              value={refNumber}
              onChange={(e) => setRefNumber(e.target.value)}
              className="w-full bg-slate-950 border border-slate-700 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:border-indigo-500"
              placeholder="e.g., SO-2024-0089"
            />
          </div>
        </div>

        {/* Actions */}
        <div className="p-4 border-t border-slate-800 flex gap-3">
          <button
            onClick={onClose}
            className="flex-1 px-4 py-2.5 bg-slate-800 hover:bg-slate-700 text-slate-300 rounded-xl font-medium transition"
          >
            Cancel
          </button>
          <button
            onClick={handleSubmit}
            disabled={!quantity || quantity <= 0 || submitting}
            className="flex-1 px-4 py-2.5 bg-indigo-600 hover:bg-indigo-500 disabled:bg-slate-700 disabled:text-slate-500 text-white rounded-xl font-medium transition"
          >
            {submitting ? 'Processing...' : 'Confirm Outbound'}
          </button>
        </div>
      </div>
    </div>
  );
}

function ProductDetailModal({ product, onClose, onInbound, onOutbound }) {
  if (!product) return null;

  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4"
         onClick={onClose}>
      <div className="bg-slate-900 border border-slate-700 rounded-2xl w-full max-w-md overflow-hidden"
           onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="p-5 border-b border-slate-800 flex items-start justify-between">
          <div>
            <p className="text-xs text-indigo-400 font-mono font-medium">{product.sku}</p>
            <h3 className="text-lg font-semibold text-white mt-1">{product.name}</h3>
          </div>
          <button onClick={onClose} className="p-1 text-slate-400 hover:text-white hover:bg-slate-800 rounded-lg transition">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Content */}
        <div className="p-5 space-y-3">
          <div className="flex justify-between py-3 border-b border-slate-800">
            <span className="text-sm text-slate-400">Current Stock</span>
            <span className="text-white font-semibold">{product.stock} <span className="text-slate-500 text-sm">{product.category}</span></span>
          </div>
          <div className="flex justify-between py-3 border-b border-slate-800">
            <span className="text-sm text-slate-400">Threshold</span>
            <span className="text-white">{product.threshold}</span>
          </div>
          <div className="flex justify-between py-3 border-b border-slate-800">
            <span className="text-sm text-slate-400">Warehouse</span>
            <span className="text-white">{product.warehouse}</span>
          </div>
          <div className="flex justify-between py-3">
            <span className="text-sm text-slate-400">Status</span>
            <StatusBadge stock={product.stock} threshold={product.threshold} />
          </div>
        </div>

        {/* Actions */}
        <div className="p-4 border-t border-slate-800 flex gap-3">
          <button
            onClick={() => { onClose(); onInbound(); }}
            className="flex-1 flex items-center justify-center gap-2 px-4 py-2.5 bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl font-medium transition"
          >
            <ArrowDownLeft className="w-4 h-4" />
            Inbound
          </button>
          <button
            onClick={() => { onClose(); onOutbound(); }}
            className="flex-1 flex items-center justify-center gap-2 px-4 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl font-medium transition"
          >
            <ArrowUpRight className="w-4 h-4" />
            Outbound
          </button>
        </div>
      </div>
    </div>
  );
}

export default function InventoryPage({ user }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [actionModal, setActionModal] = useState(null); // 'inbound' | 'outbound' | null

  const filtered = MOCK_PRODUCTS.filter(p =>
    p.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    p.sku.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleOpenInbound = () => setActionModal('inbound');
  const handleOpenOutbound = () => setActionModal('outbound');
  const handleCloseModal = () => {
    setActionModal(null);
    setSelectedProduct(null);
  };

  return (
    <div className="space-y-4">
      {/* Header */}
      <div>
        <h2 className="text-lg font-semibold text-white">Inventory</h2>
        <p className="text-sm text-slate-400">{filtered.length} products</p>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="w-4 h-4 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" />
        <input
          type="text"
          placeholder="Search SKU or name..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full bg-slate-900 border border-slate-700 rounded-xl pl-10 pr-4 py-2.5 text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:border-indigo-500 transition"
        />
      </div>

      {/* Product Table */}
      <div className="bg-slate-900/40 border border-slate-800 rounded-xl overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-slate-900/80 text-xs uppercase text-slate-500 border-b border-slate-800">
            <tr>
              <th className="px-4 py-3 text-left font-medium">SKU</th>
              <th className="px-4 py-3 text-left font-medium">Product</th>
              <th className="px-4 py-3 text-right font-medium">Stock</th>
              <th className="px-4 py-3 text-center font-medium">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800/50">
            {filtered.map((product) => (
              <tr
                key={product.id}
                onClick={() => setSelectedProduct(product)}
                className="cursor-pointer hover:bg-slate-800/40 transition"
              >
                <td className="px-4 py-3.5 font-mono text-xs text-indigo-400">{product.sku}</td>
                <td className="px-4 py-3.5">
                  <p className="text-slate-100 font-medium">{product.name}</p>
                  <p className="text-xs text-slate-500">{product.category}</p>
                </td>
                <td className="px-4 py-3.5 text-right font-mono font-semibold text-white">{product.stock}</td>
                <td className="px-4 py-3.5 text-center">
                  <StatusBadge stock={product.stock} threshold={product.threshold} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {filtered.length === 0 && (
          <div className="p-8 text-center text-slate-500 text-sm">
            No products found
          </div>
        )}
      </div>

      {/* Modals */}
      {selectedProduct && !actionModal && (
        <ProductDetailModal
          product={selectedProduct}
          onClose={() => setSelectedProduct(null)}
          onInbound={handleOpenInbound}
          onOutbound={handleOpenOutbound}
        />
      )}

      {actionModal === 'inbound' && (
        <InboundModal
          product={selectedProduct}
          onClose={handleCloseModal}
        />
      )}

      {actionModal === 'outbound' && (
        <OutboundModal
          product={selectedProduct}
          onClose={handleCloseModal}
        />
      )}
    </div>
  );
}
