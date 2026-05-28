import { useEffect, useMemo, useState } from "react";
import apiClient from "../api/apiClient";
import "../styles/dashboard.css";

function DashboardPage({ user, onLogout }) {
  const [activeSection, setActiveSection] = useState("dashboard");
  const [suppliers, setSuppliers] = useState([]);
  const [products, setProducts] = useState([]);
  const [stores, setStores] = useState([]);
  const [error, setError] = useState("");

  const normalizedRole = useMemo(() => {
    return user?.role?.replace("ROLE_", "") || "";
  }, [user]);

  const isAdmin = normalizedRole === "ADMIN";
  const isSeller = normalizedRole === "SELLER";
  const isTailor = normalizedRole === "TAILOR";

  const canViewSuppliers = isAdmin;
  const canViewProducts = isAdmin || isSeller || isTailor;
  const canViewStores = isAdmin || isSeller;
  const canViewSales = isAdmin || isSeller;
  const canViewProduction = isAdmin || isTailor;
  const canViewAlerts = isAdmin;

  const menuItems = useMemo(() => {
    const items = [
      {
        key: "dashboard",
        label: "Panel principal",
        visible: true,
      },
      {
        key: "suppliers",
        label: "Proveedores",
        visible: canViewSuppliers,
      },
      {
        key: "products",
        label: "Productos",
        visible: canViewProducts,
      },
      {
        key: "stores",
        label: "Locales",
        visible: canViewStores,
      },
      {
        key: "sales",
        label: "Ventas",
        visible: canViewSales,
      },
      {
        key: "production",
        label: "Producción",
        visible: canViewProduction,
      },
      {
        key: "alerts",
        label: "Alertas",
        visible: canViewAlerts,
      },
    ];

    return items.filter((item) => item.visible);
  }, [
    canViewSuppliers,
    canViewProducts,
    canViewStores,
    canViewSales,
    canViewProduction,
    canViewAlerts,
  ]);

  useEffect(() => {
    const sectionIsAllowed = menuItems.some((item) => item.key === activeSection);

    if (!sectionIsAllowed) {
      setActiveSection("dashboard");
    }
  }, [activeSection, menuItems]);

  const loadData = async () => {
    setError("");

    try {
      if (canViewSuppliers) {
        const suppliersResponse = await apiClient.get("/proveedores");
        setSuppliers(suppliersResponse.data?.data ?? suppliersResponse.data ?? []);
      } else {
        setSuppliers([]);
      }

      if (canViewProducts) {
        const productsResponse = await apiClient.get("/productos");
        setProducts(productsResponse.data?.data ?? productsResponse.data ?? []);
      } else {
        setProducts([]);
      }

      if (canViewStores) {
        const storesResponse = await apiClient.get("/locales");
        setStores(storesResponse.data?.data ?? storesResponse.data ?? []);
      } else {
        setStores([]);
      }
    } catch (err) {
      setError(
        "No fue posible cargar la información. Revisa que el backend esté activo y que el usuario tenga permisos."
      );
    }
  };

  useEffect(() => {
    loadData();
  }, [normalizedRole]);

  const formatMoney = (value) => {
    if (value === null || value === undefined) return "$0";
    return `$${Number(value).toLocaleString("es-CO")}`;
  };

  const renderDashboard = () => (
    <>
      <section className="stats-grid">
        {canViewSuppliers && (
          <article className="stat-card">
            <span>Proveedores</span>
            <h2>{suppliers.length}</h2>
            <p>Proveedores activos disponibles en el sistema</p>
          </article>
        )}

        {canViewProducts && (
          <article className="stat-card">
            <span>Productos</span>
            <h2>{products.length}</h2>
            <p>Chaquetas activas registradas para venta o producción</p>
          </article>
        )}

        {canViewStores && (
          <article className="stat-card">
            <span>Locales</span>
            <h2>{stores.length}</h2>
            <p>Puntos de venta activos asociados al sistema</p>
          </article>
        )}

        {canViewSales && (
          <article className="stat-card">
            <span>Ventas</span>
            <h2>Próximo</h2>
            <p>Módulo preparado para el registro de ventas</p>
          </article>
        )}

        {canViewProduction && (
          <article className="stat-card">
            <span>Producción</span>
            <h2>Próximo</h2>
            <p>Gestión de fichas y reportes de producción</p>
          </article>
        )}

        {canViewAlerts && (
          <article className="stat-card">
            <span>Alertas</span>
            <h2>Próximo</h2>
            <p>Seguimiento de faltantes, stock bajo y producción</p>
          </article>
        )}
      </section>

      <section className="tables-grid">
        {canViewSuppliers && (
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
                    <td>{supplier.name || "Sin nombre"}</td>
                    <td>{supplier.email || "Sin correo"}</td>
                    <td>{supplier.status || "Sin estado"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </article>
        )}

        {canViewProducts && (
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
                    <td>{product.reference || "Sin referencia"}</td>
                    <td>{product.name || "Sin nombre"}</td>
                    <td>{formatMoney(product.salePrice)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </article>
        )}

        {canViewStores && (
          <article className="table-card">
            <h3>Últimos locales</h3>

            <table>
              <thead>
                <tr>
                  <th>Nombre</th>
                  <th>Zona</th>
                  <th>Estado</th>
                </tr>
              </thead>

              <tbody>
                {stores.slice(0, 5).map((store) => (
                  <tr key={store.id}>
                    <td>{store.name || "Sin nombre"}</td>
                    <td>{store.zone || "No registrada"}</td>
                    <td>{store.status || "Sin estado"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </article>
        )}
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
            <th>Teléfono</th>
            <th>Correo</th>
            <th>Dirección</th>
            <th>Estado</th>
          </tr>
        </thead>

        <tbody>
          {suppliers.map((supplier) => (
            <tr key={supplier.id}>
              <td>{supplier.name || "Sin nombre"}</td>
              <td>{supplier.phone || "No registrado"}</td>
              <td>{supplier.email || "No registrado"}</td>
              <td>{supplier.address || "No registrada"}</td>
              <td>{supplier.status || "Sin estado"}</td>
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
            <th>Costo producción</th>
            <th>Precio venta</th>
            <th>Estado</th>
          </tr>
        </thead>

        <tbody>
          {products.map((product) => (
            <tr key={product.id}>
              <td>{product.reference || "Sin referencia"}</td>
              <td>{product.name || "Sin nombre"}</td>
              <td>{product.category || "Sin categoría"}</td>
              <td>{product.size || "Sin talla"}</td>
              <td>{product.color || "Sin color"}</td>
              <td>{formatMoney(product.productionCost)}</td>
              <td>{formatMoney(product.salePrice)}</td>
              <td>{product.status || "Sin estado"}</td>
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
            <th>Zona</th>
            <th>Tipo</th>
            <th>Dirección</th>
            <th>Estado</th>
          </tr>
        </thead>

        <tbody>
          {stores.map((store) => (
            <tr key={store.id}>
              <td>{store.name || "Sin nombre"}</td>
              <td>{store.zone || "No registrada"}</td>
              <td>{store.type || "No registrado"}</td>
              <td>{store.address || "No registrada"}</td>
              <td>{store.status || "Sin estado"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );

  const renderComingSoon = (title, description) => (
    <section className="section-card">
      <h2>{title}</h2>
      <p className="empty-state">{description}</p>
    </section>
  );

  const renderUnauthorized = () => (
    <section className="section-card">
      <h2>Acceso no disponible</h2>
      <p className="empty-state">
        Este módulo no está disponible para el rol actual.
      </p>
    </section>
  );

  const renderContent = () => {
    if (activeSection === "dashboard") return renderDashboard();

    if (activeSection === "suppliers") {
      return canViewSuppliers ? renderSuppliers() : renderUnauthorized();
    }

    if (activeSection === "products") {
      return canViewProducts ? renderProducts() : renderUnauthorized();
    }

    if (activeSection === "stores") {
      return canViewStores ? renderStores() : renderUnauthorized();
    }

    if (activeSection === "sales") {
      return canViewSales
        ? renderComingSoon(
            "Ventas",
            "Módulo en construcción para registrar ventas y descontar stock local."
          )
        : renderUnauthorized();
    }

    if (activeSection === "production") {
      return canViewProduction
        ? renderComingSoon(
            "Producción",
            "Módulo en construcción para consultar fichas de producción y reportar avances."
          )
        : renderUnauthorized();
    }

    if (activeSection === "alerts") {
      return canViewAlerts
        ? renderComingSoon(
            "Alertas",
            "Módulo en construcción para consultar alertas de stock, producción y faltantes."
          )
        : renderUnauthorized();
    }

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