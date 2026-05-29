function DashboardLayout({
  user,
  menuItems,
  activeSection,
  setActiveSection,
  onLogout,
  onRefresh,
  error,
  successMessage,
  children,
}) {
  const sectionTitle =
    menuItems.find((item) => item.key === activeSection)?.label || "Panel principal";

  return (
    <main className="dashboard-page">
      <aside className="sidebar">
        <div className="sidebar-logo">
          <div className="brand-icon">G</div>

          <div>
            <h2>Groccy</h2>
            <p>Panel empresarial</p>
          </div>
        </div>

        <nav className="sidebar-menu">
          {menuItems.map((item) => (
            <button
              key={item.key}
              className={activeSection === item.key ? "active" : ""}
              onClick={() => setActiveSection(item.key)}
            >
              {item.label}
            </button>
          ))}
        </nav>

        <button className="logout-button" onClick={onLogout}>
          Cerrar sesión
        </button>
      </aside>

      <section className="dashboard-content">
        <header className="dashboard-header">
          <div>
            <h1>{sectionTitle}</h1>

            <p>
              Bienvenido/a, <strong>{user?.fullName || user?.email}</strong>. Rol actual:{" "}
              <strong>{user?.role}</strong>
            </p>
          </div>

          <button onClick={onRefresh}>Actualizar datos</button>
        </header>

        {successMessage && <p className="success-message">{successMessage}</p>}
        {error && <p className="dashboard-error">{error}</p>}

        {children}
      </section>
    </main>
  );
}

export default DashboardLayout;