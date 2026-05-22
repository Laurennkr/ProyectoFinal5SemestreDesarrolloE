import { useEffect, useState } from "react";
import apiClient from "../api/apiClient";
import "../styles/dashboard.css";

function DashboardPage({ user, onLogout }) {
  const [activeSection, setActiveSection] = useState("dashboard");
  const [suppliers, setSuppliers] = useState([]);
  const [products, setProducts] = useState([]);
  const [stores, setStores] = useState([]);
  const [error, setError] = useState("");

  const loadData = async () => {
    setError("");

    try {
      const [suppliersResponse, productsResponse, storesResponse] = await Promise.all([
        apiClient.get("/proveedores"),
        apiClient.get("/productos"),
        apiClient.get("/locales"),
      ]);

      setSuppliers(suppliersResponse.data?.data ?? suppliersResponse.data ?? []);
      setProducts(productsResponse.data?.data ?? productsResponse.data ?? []);
      setStores(storesResponse.data?.data ?? storesResponse.data ?? []);
    } catch (err) {
      setError("No fue posible cargar la información. Revisa el backend o los permisos del usuario.");
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const formatMoney = (value) => {
    if (value === null || value === undefined) return "$0";
    return `$${Number(value).toLocaleString("es-CO")}`;
  };

  const renderDashboard = () => (
    <>
      <section className="stats-grid">
        <article className="stat-card">
          <span>Proveedores</span>
          <h2>{suppliers.length}</h2>
          <p>Registros disponibles en la base de datos</p>
        </article>

        <article className="stat-card">
          <span>Productos</span>
          <h2>{products.length}</h2>
          <p>Chaquetas registradas para venta o producción</p>
        </article>

        <article className="stat-card">
          <span>Locales</span>
          <h2>{stores.length}</h2>
          <p>Puntos de venta asociados al sistema</p>
        </article>
      </section>

      <section className="tables-grid">
        <article className="table-card">
          <h3>Últimos proveedores</h3>
          <table>
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Correo</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {suppliers.slice(0, 5).map((supplier) => (
                <tr key={supplier.id}>
                  <td>{supplier.name}</td>
                  <td>{supplier.email || "Sin correo"}</td>
                  <td>{supplier.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </article>

        <article className="table-card">
          <h3>Últimos productos</h3>
          <table>
            <thead>
              <tr>
                <th>Referencia</th>
                <th>Nombre</th>
                <th>Precio</th>
              </tr>
            </thead>
            <tbody>
              {products.slice(0, 5).map((product) => (
                <tr key={product.id}>
                  <td>{product.sku || product.reference}</td>
                  <td>{product.name}</td>
                  <td>{formatMoney(product.salePrice)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </article>
      </section>
    </>
  );

  const renderSuppliers = () => (
    <section className="section-card">
      <h2>Proveedores</h2>
      <p>Consulta de proveedores registrados en el sistema.</p>

      <table>
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Contacto</th>
            <th>Teléfono</th>
            <th>Correo</th>
            <th>Dirección</th>
            <th>Estado</th>
          </tr>
        </thead>
        <tbody>
          {suppliers.map((supplier) => (
            <tr key={supplier.id}>
              <td>{supplier.name}</td>
              <td>{supplier.contactName || "No registrado"}</td>
              <td>{supplier.phone || "No registrado"}</td>
              <td>{supplier.email || "No registrado"}</td>
              <td>{supplier.address || "No registrada"}</td>
              <td>{supplier.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );

  const renderProducts = () => (
    <section className="section-card">
      <h2>Productos</h2>
      <p>Consulta de productos registrados para producción o venta.</p>

      <table>
        <thead>
          <tr>
            <th>Referencia</th>
            <th>Nombre</th>
            <th>Categoría</th>
            <th>Talla</th>
            <th>Color</th>
            <th>Costo</th>
            <th>Precio venta</th>
            <th>Estado</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id}>
              <td>{product.sku || product.reference}</td>
              <td>{product.name}</td>
              <td>{product.category}</td>
              <td>{product.size}</td>
              <td>{product.color}</td>
              <td>{formatMoney(product.costPrice)}</td>
              <td>{formatMoney(product.salePrice)}</td>
              <td>{product.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );

  const renderStores = () => (
    <section className="section-card">
      <h2>Locales</h2>
      <p>Consulta de puntos de venta asociados al sistema.</p>

      <table>
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Ciudad</th>
            <th>Dirección</th>
            <th>Estado</th>
          </tr>
        </thead>
        <tbody>
          {stores.map((store) => (
            <tr key={store.id}>
              <td>{store.name}</td>
              <td>{store.city}</td>
              <td>{store.address}</td>
              <td>{store.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );

  const renderComingSoon = (title) => (
    <section className="section-card">
      <h2>{title}</h2>
      <p className="empty-state">
        hoy no
      </p>
    </section>
  );

  const renderContent = () => {
    if (activeSection === "dashboard") return renderDashboard();
    if (activeSection === "suppliers") return renderSuppliers();
    if (activeSection === "products") return renderProducts();
    if (activeSection === "stores") return renderStores();
    if (activeSection === "sales") return renderComingSoon("Ventas");
    if (activeSection === "production") return renderComingSoon("Producción");
    if (activeSection === "alerts") return renderComingSoon("Alertas");

    return renderDashboard();
  };

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
          <button
            className={activeSection === "dashboard" ? "active" : ""}
            onClick={() => setActiveSection("dashboard")}
          >
            Panel principal
          </button>

          <button
            className={activeSection === "suppliers" ? "active" : ""}
            onClick={() => setActiveSection("suppliers")}
          >
            Proveedores
          </button>

          <button
            className={activeSection === "products" ? "active" : ""}
            onClick={() => setActiveSection("products")}
          >
            Productos
          </button>

          <button
            className={activeSection === "stores" ? "active" : ""}
            onClick={() => setActiveSection("stores")}
          >
            Locales
          </button>

          <button
            className={activeSection === "sales" ? "active" : ""}
            onClick={() => setActiveSection("sales")}
          >
            Ventas
          </button>

          <button
            className={activeSection === "production" ? "active" : ""}
            onClick={() => setActiveSection("production")}
          >
            Producción
          </button>

          <button
            className={activeSection === "alerts" ? "active" : ""}
            onClick={() => setActiveSection("alerts")}
          >
            Alertas
          </button>
        </nav>

        <button className="logout-button" onClick={onLogout}>
          Cerrar sesión
        </button>
      </aside>

      <section className="dashboard-content">
        <header className="dashboard-header">
          <div>
            <h1>
              {activeSection === "dashboard" && "Panel principal"}
              {activeSection === "suppliers" && "Proveedores"}
              {activeSection === "products" && "Productos"}
              {activeSection === "stores" && "Locales"}
              {activeSection === "sales" && "Ventas"}
              {activeSection === "production" && "Producción"}
              {activeSection === "alerts" && "Alertas"}
            </h1>

            <p>
              Bienvenido/a, <strong>{user?.fullName || user?.email}</strong>. Rol actual:{" "}
              <strong>{user?.role}</strong>
            </p>
          </div>

          <button onClick={loadData}>Actualizar datos</button>
        </header>

        {error && <p className="dashboard-error">{error}</p>}

        {renderContent()}
      </section>
    </main>
  );
}

export default DashboardPage;