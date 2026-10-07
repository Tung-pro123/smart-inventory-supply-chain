import React, { useState } from 'react';
import { AlertTriangle, X, ArrowDownLeft, CheckCircle } from 'lucide-react';

// Mock low stock products
const LOW_STOCK_PRODUCTS = [
  { id: 2, sku: 'BOSCH-ECU-002', name: 'Automotive Electronic Control Unit', stock: 8, threshold: 10, urgency: 'high' },
  { id: 4, sku: 'BOSCH-BRK-004', name: 'High-Performance Brake Caliper Pad', stock: 14, threshold: 25, urgency: 'high' },
  { id: 6, sku: 'BOSCH-CAP-008', name: 'Ceramic Capacitor 100nF', stock: 3, threshold: 50, urgency: 'critical' },
  { id: 8, sku: 'BOSCH-LED-012', name: 'High-Brightness LED Array', stock: 22, threshold: 30, urgency: 'medium' },
];

function getUrgencyStyle(urgency) {
  switch (urgency) {
    case 'critical':
      return { bg: 'bg-rose-500/10', border: 'border-rose-500/30', text: 'text-rose-400', bar: 'bg-rose-500', label: 'Critical' };
    case 'high':
      return { bg: 'bg-amber-500/10', border: 'border-amber-500/30', text: 'text-amber-400', bar: 'bg-amber-500', label: 'High' };
    default:
      return { bg: 'bg-yellow-500/10', border: 'border-yellow-500/30', text: 'text-yellow-400', bar: 'bg-yellow-500', label: 'Medium' };
  }
}

function ReorderModal({ product, onClose }) {
  const [quantity, setQuantity] = useState(product?.threshold * 2 - product?.stock || 0);
  const [submitting, setSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleSubmit = () => {
    setSubmitting(true);
    setTimeout(() => {
      setSubmitting(false);
      setSuccess(true);
      setTimeout(() => {
        onClose();
      }, 1000);
    }, 800);
  };

  if (!product) return null;

  if (success) {
    return (
      <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4"
           onClick={onClose}>
        <div className="bg-slate-900 border border-emerald-500/30 rounded-2xl w-full max-w-sm p-8 text-center"
             onClick={(e) => e.stopPropagation()}>
          <div className="p-3 bg-emerald-500/10 rounded-full w-fit mx-auto mb-4">
            <CheckCircle className="w-8 h-8 text-emerald-400" />
          </div>
          <h3 className="text-lg font-semibold text-white">Inbound Created</h3>
          <p className="text-sm text-slate-400 mt-2">Order {quantity} units of {product.sku}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4"
         onClick={onClose}>
      <div className="bg-slate-900 border border-slate-700 rounded-2xl w-full max-w-sm overflow-hidden"
           onClick={(e) => e.stopPropagation()}>
        <div className="p-5 border-b border-slate-800 flex items-start justify-between">
          <div>
            <p className="text-xs text-indigo-400 font-mono">{product.sku}</p>
            <h3 className="text-base font-semibold text-white mt-1">{product.name}</h3>
          </div>
          <button onClick={onClose} className="p-1 text-slate-400 hover:text-white hover:bg-slate-800 rounded-lg transition">
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="p-5 space-y-3">
          <div className="flex justify-between text-sm">
            <span className="text-slate-400">Current Stock</span>
            <span className="text-white font-semibold">{product.stock}</span>
          </div>
          <div className="flex justify-between text-sm">
            <span className="text-slate-400">Threshold</span>
            <span className="text-white">{product.threshold}</span>
          </div>
          <div className="flex justify-between text-sm">
            <span className="text-slate-400">Suggested Order</span>
            <span className="text-emerald-400 font-semibold">{product.threshold * 2 - product.stock} units</span>
          </div>

          <div className="pt-2">
            <label className="block text-xs text-slate-400 mb-1.5">Order Quantity</label>
            <input
              type="number"
              min="1"
              value={quantity}
              onChange={(e) => setQuantity(parseInt(e.target.value) || 0)}
              className="w-full bg-slate-950 border border-slate-700 rounded-lg px-4 py-2.5 text-white focus:outline-none focus:border-indigo-500"
            />
          </div>
        </div>

        <div className="p-4 border-t border-slate-800">
          <button
            onClick={handleSubmit}
            disabled={!quantity || quantity <= 0 || submitting}
            className="w-full flex items-center justify-center gap-2 px-4 py-2.5 bg-emerald-600 hover:bg-emerald-500 disabled:bg-slate-700 disabled:text-slate-500 text-white rounded-xl font-medium transition"
          >
            {submitting ? 'Processing...' : (
              <>
                <ArrowDownLeft className="w-4 h-4" />
                Create Inbound Order
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  );
}

export default function LowStockPage({ user }) {
  const [selectedProduct, setSelectedProduct] = useState(null);

  return (
    <div className="space-y-4">
      {/* Header */}
      <div>
        <h2 className="text-lg font-semibold text-white">Low Stock Alerts</h2>
        <p className="text-sm text-slate-400">{LOW_STOCK_PRODUCTS.length} items need attention</p>
      </div>

      {/* Alert List */}
      <div className="space-y-2">
        {LOW_STOCK_PRODUCTS.map((product) => {
          const style = getUrgencyStyle(product.urgency);
          const progress = Math.max((product.stock / product.threshold) * 100, 5);

          return (
            <button
              key={product.id}
              onClick={() => setSelectedProduct(product)}
              className={`
                w-full text-left p-4 rounded-xl border transition-all hover:scale-[1.01]
                bg-slate-900/40 ${style.border}
              `}
            >
              <div className="flex items-center gap-4">
                {/* Urgency Icon */}
                <div className={`p-2.5 rounded-lg ${style.bg}`}>
                  <AlertTriangle className={`w-5 h-5 ${style.text}`} />
                </div>

                {/* Info */}
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2">
                    <p className="text-xs font-mono text-indigo-400">{product.sku}</p>
                    <span className={`px-1.5 py-0.5 rounded text-xs font-medium ${style.bg} ${style.text}`}>
                      {style.label}
                    </span>
                  </div>
                  <p className="text-sm font-medium text-white mt-1 truncate">{product.name}</p>
                </div>

                {/* Stock */}
                <div className="text-right">
                  <p className="text-lg font-bold text-white">{product.stock}</p>
                  <p className="text-xs text-slate-500">of {product.threshold}</p>
                </div>
              </div>

              {/* Progress bar */}
              <div className="mt-3 h-1.5 bg-slate-800 rounded-full overflow-hidden">
                <div
                  className={`h-full ${style.bar} transition-all`}
                  style={{ width: `${progress}%` }}
                />
              </div>
            </button>
          );
        })}
      </div>

      <ReorderModal
        product={selectedProduct}
        onClose={() => setSelectedProduct(null)}
      />
    </div>
  );
}
