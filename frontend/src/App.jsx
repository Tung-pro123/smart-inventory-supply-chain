import React, { useState } from 'react';
import { Boxes, AlertTriangle, Activity, LogOut, User } from 'lucide-react';
import Dashboard from './components/Dashboard';
import InventoryPage from './components/InventoryPage';
import LowStockPage from './components/LowStockPage';
import EventLogPage from './components/EventLogPage';

// Role configuration
const ROLES = {
  ADMIN: 'ADMIN',
  MANAGER: 'MANAGER',
  STAFF: 'STAFF',
};

// All tabs with their role requirements
const ALL_TABS = [
  { id: 'dashboard', label: 'Dashboard', icon: Activity, roles: [ROLES.ADMIN, ROLES.MANAGER, ROLES.STAFF] },
  { id: 'inventory', label: 'Inventory', icon: Boxes, roles: [ROLES.ADMIN, ROLES.MANAGER, ROLES.STAFF] },
  { id: 'low-stock', label: 'Low Stock', icon: AlertTriangle, roles: [ROLES.ADMIN, ROLES.MANAGER] },
  { id: 'events', label: 'Events', icon: Activity, roles: [ROLES.ADMIN, ROLES.MANAGER] },
];

// Mock users for demo - chọn role để test
const DEMO_USERS = [
  { id: 1, name: 'Admin User', email: 'admin@bosch.com', role: ROLES.ADMIN },
  { id: 2, name: 'Warehouse Manager', email: 'manager@bosch.com', role: ROLES.MANAGER },
  { id: 3, name: 'Warehouse Staff', email: 'staff@bosch.com', role: ROLES.STAFF },
];

export default function App() {
  // Demo: select user role (trong thực tế sẽ từ JWT)
  const [currentUser, setCurrentUser] = useState(DEMO_USERS[1]); // Default: Manager
  const [activeTab, setActiveTab] = useState('dashboard');
  const [showUserMenu, setShowUserMenu] = useState(false);

  // Filter tabs based on user role
  const accessibleTabs = ALL_TABS.filter(tab =>
    tab.roles.includes(currentUser.role)
  );

  const renderPage = () => {
    switch (activeTab) {
      case 'dashboard':
        return <Dashboard user={currentUser} onNavigate={setActiveTab} />;
      case 'inventory':
        return <InventoryPage user={currentUser} />;
      case 'low-stock':
        return <LowStockPage user={currentUser} />;
      case 'events':
        return <EventLogPage user={currentUser} />;
      default:
        return <Dashboard user={currentUser} onNavigate={setActiveTab} />;
    }
  };

  const roleLabel = {
    [ROLES.ADMIN]: { text: 'Admin', color: 'text-rose-400' },
    [ROLES.MANAGER]: { text: 'Manager', color: 'text-amber-400' },
    [ROLES.STAFF]: { text: 'Staff', color: 'text-emerald-400' },
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col">
      {/* Header */}
      <header className="border-b border-slate-800 bg-slate-900/80 backdrop-blur px-6 py-3 flex items-center justify-between sticky top-0 z-50">
        {/* Logo */}
        <div className="flex items-center gap-3">
          <div className="p-1.5 bg-indigo-600/20 text-indigo-400 rounded-lg">
            <Boxes className="w-5 h-5" />
          </div>
          <h1 className="text-base font-semibold text-white">
            Smart Inventory
          </h1>
        </div>

        {/* Navigation - Role-based */}
        <nav className="flex items-center gap-1">
          {accessibleTabs.map((tab) => {
            const Icon = tab.icon;
            const isActive = activeTab === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`
                  flex items-center gap-2 px-3 py-1.5 rounded-lg text-sm font-medium transition-all
                  ${isActive
                    ? 'bg-indigo-600/20 text-indigo-300 border border-indigo-500/30'
                    : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/50'
                  }
                `}
              >
                <Icon className="w-4 h-4" />
                {tab.label}
              </button>
            );
          })}
        </nav>

        {/* User Menu - Demo role switcher */}
        <div className="relative">
          <button
            onClick={() => setShowUserMenu(!showUserMenu)}
            className="flex items-center gap-3 px-3 py-1.5 rounded-lg bg-slate-800/50 hover:bg-slate-800 border border-slate-700 transition"
          >
            <div className="text-right">
              <p className="text-sm font-medium text-white">{currentUser.name}</p>
              <p className={`text-xs ${roleLabel[currentUser.role].color}`}>
                {roleLabel[currentUser.role].text}
              </p>
            </div>
            <User className="w-5 h-5 text-slate-400" />
          </button>

          {/* Demo Role Switcher */}
          {showUserMenu && (
            <div className="absolute right-0 mt-2 w-64 bg-slate-900 border border-slate-700 rounded-xl shadow-xl overflow-hidden z-50">
              <div className="p-3 border-b border-slate-800">
                <p className="text-xs text-slate-500 uppercase tracking-wider">Demo: Switch Role</p>
              </div>
              {DEMO_USERS.map((user) => (
                <button
                  key={user.id}
                  onClick={() => {
                    setCurrentUser(user);
                    setShowUserMenu(false);
                    // Reset to dashboard when changing role
                    setActiveTab('dashboard');
                  }}
                  className={`
                    w-full text-left px-4 py-3 hover:bg-slate-800/50 transition
                    ${currentUser.id === user.id ? 'bg-indigo-600/10' : ''}
                  `}
                >
                  <p className="text-sm font-medium text-white">{user.name}</p>
                  <p className={`text-xs ${roleLabel[user.role].color}`}>
                    {roleLabel[user.role].text} • {user.email}
                  </p>
                </button>
              ))}
              <div className="p-3 border-t border-slate-800 bg-slate-950/50">
                <p className="text-xs text-slate-500">
                  Role determines visible tabs and features
                </p>
              </div>
            </div>
          )}
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1 max-w-6xl w-full mx-auto p-6">
        {renderPage()}
      </main>
    </div>
  );
}
