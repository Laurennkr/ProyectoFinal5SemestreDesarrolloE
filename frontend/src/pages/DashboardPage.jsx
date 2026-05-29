import { useEffect, useMemo, useState } from "react";
import apiClient from "../api/apiClient";

import DashboardLayout from "../components/dashboard/DashboardLayout";

import SuppliersModule from "../modules/SuppliersModule";
import ProductsModule from "../modules/ProductsModule";
import StoresModule from "../modules/StoresModule";
import SuppliesModule from "../modules/SuppliesModule";
import SalesModule from "../modules/SalesModule";
import AlertsModule from "../modules/AlertsModule";
import ProductionModule from "../modules/ProductionModule";
import StockModule from "../modules/StockModule";

import "../styles/dashboard.css";

function DashboardPage({ user, onLogout }) {
  const [activeSection, setActiveSection] = useState("dashboard");

  const [suppliers, setSuppliers] = useState([]);
  const [products, setProducts] = useState([]);
  const [stores, setStores] = useState([]);
  const [supplies, setSupplies] = useState([]);
  const [sales, setSales] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [productionSheets, setProductionSheets] = useState([]);

  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const normalizedRole = useMemo(() => {
    return user?.role?.replace("ROLE_", "") || "";
  }, [user]);

  const isAdmin = normalizedRole === "ADMIN";
  const isSeller = normalizedRole === "SELLER";
  const isTailor = normalizedRole === "TAILOR";

  const userId = user?.userId || user?.id;

  const canViewSuppliers = isAdmin;
  const canViewProducts = isAdmin || isSeller || isTailor;
  const canViewStores = isAdmin || isSeller;
  const canViewSupplies = isAdmin || isTailor;
  const canViewStock = isAdmin || isSeller;
  const canViewSales = isAdmin || isSeller;
  const canViewProduction = isAdmin || isTailor;
  const canViewAlerts = isAdmin;

  const menuItems = useMemo(() => {
    const items = [
      { key: "dashboard", label: "Panel principal", visible: true },
      { key: "suppliers", label: "Proveedores", visible: canViewSuppliers },
      { key: "products", label: "Productos", visible: canViewProducts },
      { key: "stores", label: "Locales", visible: canViewStores },
      { key: "supplies", label: "Insumos", visible: canViewSupplies },
      { key: "stock", label: "Stock", visible: canViewStock },
      { key: "sales", label: "Ventas", visible: canViewSales },
      { key: "production", label: "Producción", visible: canViewProduction },
      { key: "alerts", label: "Alertas", visible: canViewAlerts },
    ];

    return items.filter((item) => item.visible);
  }, [
    canViewSuppliers,
    canViewProducts,
    canViewStores,
    canViewSupplies,
    canViewStock,
    canViewSales,
    canViewProduction,
    canViewAlerts,
  ]);

  useEffect(() => {
    const isAllowed = menuItems.some((item) => item.key === activeSection);

    if (!isAllowed) {
      setActiveSection("dashboard");
    }
  }, [activeSection, menuItems]);

  const normalizeResponse = (response) => {
    return response.data?.data ?? response.data ?? [];
  };

  const loadData = async () => {
    setError("");

    try {
      if (canViewSuppliers) {
        const response = await apiClient.get("/proveedores");
        setSuppliers(normalizeResponse(response));
      } else {
        setSuppliers([]);
      }

      if (canViewProducts || canViewSales || canViewStock || canViewProduction) {
        const response = await apiClient.get("/productos");
        setProducts(normalizeResponse(response));
      } else {
        setProducts([]);
      }

      if (canViewStores || canViewSales || canViewStock) {
        const response = await apiClient.get("/locales");
        setStores(normalizeResponse(response));
      } else {
        setStores([]);
      }

      if (canViewSupplies || canViewProduction) {
        const response = await apiClient.get("/insumos");
        setSupplies(normalizeResponse(response));
      } else {
        setSupplies([]);
      }

      if (canViewSales) {
        const response = await apiClient.get("/ventas");
        setSales(normalizeResponse(response));
      } else {
        setSales([]);
      }

      if (canViewAlerts) {
        const response = await apiClient.get("/alertas");
        setAlerts(normalizeResponse(response));
      } else {
        setAlerts([]);
      }

      if (canViewProduction) {
        if (isAdmin) {
          const response = await apiClient.get("/fichas-produccion");
          setProductionSheets(normalizeResponse(response));
        } else if (isTailor && userId) {
          const response = await apiClient.get(
            `/fichas-produccion/mis-fichas/${userId}`
          );
          setProductionSheets(normalizeResponse(response));
        } else {
          setProductionSheets([]);
        }
      } else {
        setProductionSheets([]);
      }
    } catch (err) {
      setError(
        err.response?.data?.message ||
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

  const clearMessages = () => {
    setError("");
    setSuccessMessage("");
  };

  const handleSetActiveSection = (section) => {
    clearMessages();
    setActiveSection(section);
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
            <p>Productos activos registrados para venta o producción</p>
          </article>
        )}

        {canViewStores && (
          <article className="stat-card">
            <span>Locales</span>
            <h2>{stores.length}</h2>
            <p>Puntos de venta activos asociados al sistema</p>
          </article>
        )}

        {canViewSupplies && (
          <article className="stat-card">
            <span>Insumos</span>
            <h2>{supplies.length}</h2>
            <p>Insumos disponibles para producción</p>
          </article>
        )}

        {canViewStock && (
          <article className="stat-card">
            <span>Stock</span>
            <h2>Local</h2>
            <p>Consulta de disponibilidad por punto de venta</p>
          </article>
        )}

        {canViewSales && (
          <article className="stat-card">
            <span>Ventas</span>
            <h2>{sales.length}</h2>
            <p>Ventas registradas en el sistema</p>
          </article>
        )}

        {canViewProduction && (
          <article className="stat-card">
            <span>Producción</span>
            <h2>{productionSheets.length}</h2>
            <p>Fichas de producción disponibles</p>
          </article>
        )}

        {canViewAlerts && (
          <article className="stat-card">
            <span>Alertas</span>
            <h2>{alerts.length}</h2>
            <p>Alertas activas pendientes</p>
          </article>
        )}
      </section>

      <section className="tables-grid">
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
                {products.length === 0 && (
                  <tr>
                    <td colSpan="3">No hay productos disponibles.</td>
                  </tr>
                )}

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

        {canViewSales && (
          <article className="table-card">
            <h3>Últimas ventas</h3>

            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Local</th>
                  <th>Total</th>
                </tr>
              </thead>

              <tbody>
                {sales.length === 0 && (
                  <tr>
                    <td colSpan="3">No hay ventas registradas.</td>
                  </tr>
                )}

                {sales.slice(0, 5).map((sale) => (
                  <tr key={sale.id}>
                    <td>{sale.id}</td>
                    <td>{sale.storeName || "Sin local"}</td>
                    <td>{formatMoney(sale.totalAmount)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </article>
        )}

        {canViewProduction && (
          <article className="table-card">
            <h3>Últimas fichas de producción</h3>

            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Título</th>
                  <th>Estado</th>
                </tr>
              </thead>

              <tbody>
                {productionSheets.length === 0 && (
                  <tr>
                    <td colSpan="3">No hay fichas disponibles.</td>
                  </tr>
                )}

                {productionSheets.slice(0, 5).map((sheet) => (
                  <tr key={sheet.id}>
                    <td>{sheet.id}</td>
                    <td>{sheet.title || "Sin título"}</td>
                    <td>{sheet.status || "Sin estado"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </article>
        )}

        {canViewAlerts && (
          <article className="table-card">
            <h3>Alertas activas</h3>

            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Mensaje</th>
                  <th>Estado</th>
                </tr>
              </thead>

              <tbody>
                {alerts.length === 0 && (
                  <tr>
                    <td colSpan="3">No hay alertas activas.</td>
                  </tr>
                )}

                {alerts.slice(0, 5).map((alert) => (
                  <tr key={alert.id}>
                    <td>{alert.id}</td>
                    <td>
                      {alert.message ||
                        alert.description ||
                        alert.alertMessage ||
                        "Sin mensaje"}
                    </td>
                    <td>{alert.status || "Sin estado"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </article>
        )}
      </section>
    </>
  );

  const renderContent = () => {
    if (activeSection === "dashboard") {
      return renderDashboard();
    }

    if (activeSection === "suppliers") {
      return (
        <SuppliersModule
          suppliers={suppliers}
          isAdmin={isAdmin}
          reload={loadData}
          setError={setError}
          setSuccessMessage={setSuccessMessage}
        />
      );
    }

    if (activeSection === "products") {
      return (
        <ProductsModule
          products={products}
          isAdmin={isAdmin}
          reload={loadData}
          setError={setError}
          setSuccessMessage={setSuccessMessage}
        />
      );
    }

    if (activeSection === "stores") {
      return (
        <StoresModule
          stores={stores}
          isAdmin={isAdmin}
          reload={loadData}
          setError={setError}
          setSuccessMessage={setSuccessMessage}
        />
      );
    }

    if (activeSection === "supplies") {
      return (
        <SuppliesModule
          supplies={supplies}
          suppliers={suppliers}
          isAdmin={isAdmin}
          reload={loadData}
          setError={setError}
          setSuccessMessage={setSuccessMessage}
        />
      );
    }

    if (activeSection === "stock") {
      return (
        <StockModule
          stores={stores}
          products={products}
          isAdmin={isAdmin}
          reload={loadData}
          setError={setError}
          setSuccessMessage={setSuccessMessage}
        />
      );
    }

    if (activeSection === "sales") {
      return (
        <SalesModule
          sales={sales}
          stores={stores}
          products={products}
          reload={loadData}
          setError={setError}
          setSuccessMessage={setSuccessMessage}
        />
      );
    }

    if (activeSection === "production") {
      return (
        <ProductionModule
          productionSheets={productionSheets}
          products={products}
          supplies={supplies}
          user={user}
          isAdmin={isAdmin}
          isTailor={isTailor}
          reload={loadData}
          setError={setError}
          setSuccessMessage={setSuccessMessage}
        />
      );
    }

    if (activeSection === "alerts") {
      return (
        <AlertsModule
          alerts={alerts}
          reload={loadData}
          setError={setError}
          setSuccessMessage={setSuccessMessage}
        />
      );
    }

    return renderDashboard();
  };

  return (
    <DashboardLayout
      user={user}
      menuItems={menuItems}
      activeSection={activeSection}
      setActiveSection={handleSetActiveSection}
      onLogout={onLogout}
      onRefresh={loadData}
      error={error}
      successMessage={successMessage}
    >
      {renderContent()}
    </DashboardLayout>
  );
}

export default DashboardPage;