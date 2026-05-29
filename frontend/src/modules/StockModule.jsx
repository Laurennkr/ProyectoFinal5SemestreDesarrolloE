import { useState } from "react";
import apiClient from "../api/apiClient";
import DataTable from "../components/dashboard/DataTable";

function StockModule({ stores, products, isAdmin, reload, setError, setSuccessMessage }) {
  const [selectedStoreId, setSelectedStoreId] = useState("");
  const [storeStock, setStoreStock] = useState([]);
  const [centralStock, setCentralStock] = useState([]);

  const [centralEntryForm, setCentralEntryForm] = useState({
    productId: "",
    quantity: "",
    minimumStock: "",
  });

  const [distributionForm, setDistributionForm] = useState({
    productId: "",
    storeId: "",
    quantity: "",
    minimumStock: "",
  });

  const storeStockColumns = [
    {
      key: "productReference",
      label: "Referencia",
      render: (item) =>
        item.productReference || item.reference || item.productId || "No registrado",
    },
    {
      key: "productName",
      label: "Producto",
      render: (item) => item.productName || item.name || "No registrado",
    },
    {
      key: "availableQuantity",
      label: "Disponible",
    },
    {
      key: "minimumStock",
      label: "Stock mínimo",
    },
    {
      key: "storeName",
      label: "Local",
      render: (item) => item.storeName || item.storeId || "No registrado",
    },
  ];

  const centralStockColumns = [
    {
      key: "productReference",
      label: "Referencia",
      render: (item) =>
        item.productReference || item.reference || item.productId || "No registrado",
    },
    {
      key: "productName",
      label: "Producto",
      render: (item) => item.productName || item.name || "No registrado",
    },
    {
      key: "availableQuantity",
      label: "Disponible central",
    },
    {
      key: "minimumStock",
      label: "Stock mínimo",
    },
  ];

  const loadStockByStore = async (storeId) => {
    if (!storeId) {
      setStoreStock([]);
      return;
    }

    try {
      setError("");

      const response = await apiClient.get(`/stock-locales/${storeId}`);
      setStoreStock(response.data?.data ?? response.data ?? []);
    } catch (err) {
      setStoreStock([]);
      setError(
        err.response?.data?.message ||
          "No fue posible consultar el stock del local."
      );
    }
  };

  const loadCentralStock = async () => {
    if (!isAdmin) return;

    try {
      setError("");

      const response = await apiClient.get("/stock-central");
      setCentralStock(response.data?.data ?? response.data ?? []);
    } catch (err) {
      setCentralStock([]);
      setError(
        err.response?.data?.message ||
          "No fue posible consultar el stock central."
      );
    }
  };

  const handleStoreChange = async (event) => {
    const value = event.target.value;
    setSelectedStoreId(value);
    await loadStockByStore(value);
  };

  const handleCentralEntryChange = (event) => {
    const { name, value } = event.target;

    setCentralEntryForm((current) => ({
      ...current,
      [name]: value,
    }));
  };

  const handleDistributionChange = (event) => {
    const { name, value } = event.target;

    setDistributionForm((current) => ({
      ...current,
      [name]: value,
    }));
  };

  const handleAddCentralStock = async (event) => {
    event.preventDefault();

    try {
      setError("");
      setSuccessMessage("");

      await apiClient.post("/stock-central/entrada", {
        productId: Number(centralEntryForm.productId),
        quantity: Number(centralEntryForm.quantity),
        minimumStock: Number(centralEntryForm.minimumStock),
      });

      setSuccessMessage("Entrada registrada correctamente en stock central.");

      setCentralEntryForm({
        productId: "",
        quantity: "",
        minimumStock: "",
      });

      await loadCentralStock();
      await reload();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "No fue posible registrar la entrada al stock central."
      );
    }
  };

  const handleDistribute = async (event) => {
    event.preventDefault();

    try {
      setError("");
      setSuccessMessage("");

      await apiClient.post("/distribuciones", {
        productId: Number(distributionForm.productId),
        storeId: Number(distributionForm.storeId),
        quantity: Number(distributionForm.quantity),
        minimumStock: Number(distributionForm.minimumStock),
      });

      setSuccessMessage("Stock distribuido correctamente al local.");

      setDistributionForm({
        productId: "",
        storeId: "",
        quantity: "",
        minimumStock: "",
      });

      await loadStockByStore(selectedStoreId);
      await loadCentralStock();
      await reload();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "No fue posible distribuir el stock al local."
      );
    }
  };

  return (
    <section className="section-card">
      <div className="section-actions">
        <div>
          <h2>Stock</h2>
          <p>
            Consulta de disponibilidad por local, entrada a stock central y distribución a puntos de venta.
          </p>
        </div>

        {isAdmin && (
          <button className="secondary-action" onClick={loadCentralStock}>
            Consultar stock central
          </button>
        )}
      </div>

      <div className="form-card">
        <h3>Consultar stock por local</h3>

        <div className="form-grid">
          <div className="form-field">
            <label>Local</label>
            <select value={selectedStoreId} onChange={handleStoreChange}>
              <option value="">Seleccione un local</option>

              {stores.map((store) => (
                <option key={store.id} value={store.id}>
                  {store.name}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      <DataTable columns={storeStockColumns} data={storeStock} />

      {isAdmin && (
        <>
          <form className="form-card" onSubmit={handleAddCentralStock}>
            <h3>Entrada a stock central</h3>

            <div className="form-grid">
              <div className="form-field">
                <label>Producto</label>
                <select
                  name="productId"
                  value={centralEntryForm.productId}
                  onChange={handleCentralEntryChange}
                  required
                >
                  <option value="">Seleccione un producto</option>

                  {products.map((product) => (
                    <option key={product.id} value={product.id}>
                      {product.reference} - {product.name}
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-field">
                <label>Cantidad de entrada</label>
                <input
                  type="number"
                  name="quantity"
                  min="1"
                  value={centralEntryForm.quantity}
                  onChange={handleCentralEntryChange}
                  required
                />
              </div>

              <div className="form-field">
                <label>Stock mínimo central</label>
                <input
                  type="number"
                  name="minimumStock"
                  min="1"
                  value={centralEntryForm.minimumStock}
                  onChange={handleCentralEntryChange}
                  required
                />
              </div>
            </div>

            <div className="form-actions">
              <button className="primary-action" type="submit">
                Registrar entrada central
              </button>
            </div>
          </form>

          <form className="form-card" onSubmit={handleDistribute}>
            <h3>Distribuir stock a local</h3>

            <div className="form-grid">
              <div className="form-field">
                <label>Producto</label>
                <select
                  name="productId"
                  value={distributionForm.productId}
                  onChange={handleDistributionChange}
                  required
                >
                  <option value="">Seleccione un producto</option>

                  {products.map((product) => (
                    <option key={product.id} value={product.id}>
                      {product.reference} - {product.name}
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-field">
                <label>Local destino</label>
                <select
                  name="storeId"
                  value={distributionForm.storeId}
                  onChange={handleDistributionChange}
                  required
                >
                  <option value="">Seleccione un local</option>

                  {stores.map((store) => (
                    <option key={store.id} value={store.id}>
                      {store.name}
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-field">
                <label>Cantidad</label>
                <input
                  type="number"
                  name="quantity"
                  min="1"
                  value={distributionForm.quantity}
                  onChange={handleDistributionChange}
                  required
                />
              </div>

              <div className="form-field">
                <label>Stock mínimo local</label>
                <input
                  type="number"
                  name="minimumStock"
                  min="1"
                  value={distributionForm.minimumStock}
                  onChange={handleDistributionChange}
                  required
                />
              </div>
            </div>

            <div className="form-actions">
              <button className="primary-action" type="submit">
                Distribuir stock
              </button>
            </div>
          </form>

          <h3>Stock central</h3>
          <DataTable columns={centralStockColumns} data={centralStock} />
        </>
      )}
    </section>
  );
}

export default StockModule;