import { useState } from "react";
import apiClient from "../api/apiClient";
import DataTable from "../components/dashboard/DataTable";
import CrudForm from "../components/dashboard/CrudForm";

function SalesModule({ sales, stores, products, reload, setError, setSuccessMessage }) {
  const [formData, setFormData] = useState({
    storeId: "",
    productId: "",
    quantity: "",
  });
  const [isSaving, setIsSaving] = useState(false);

  const formatMoney = (value) => {
    if (value === null || value === undefined) return "$0";
    return `$${Number(value).toLocaleString("es-CO")}`;
  };

  const fields = [
    {
      name: "storeId",
      label: "Local",
      type: "select",
      required: true,
      options: stores.map((store) => ({
        value: store.id,
        label: store.name,
      })),
    },
    {
      name: "productId",
      label: "Producto",
      type: "select",
      required: true,
      options: products.map((product) => ({
        value: product.id,
        label: `${product.reference} - ${product.name}`,
      })),
    },
    {
      name: "quantity",
      label: "Cantidad",
      type: "number",
      min: "1",
      required: true,
    },
  ];

  const columns = [
    { key: "id", label: "ID" },
    { key: "saleDate", label: "Fecha" },
    { key: "storeName", label: "Local" },
    { key: "sellerName", label: "Vendedor" },
    { key: "totalAmount", label: "Total", render: (item) => formatMoney(item.totalAmount) },
    { key: "status", label: "Estado" },
  ];

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((current) => ({ ...current, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      setIsSaving(true);
      setError("");
      setSuccessMessage("");

      await apiClient.post("/ventas", {
        storeId: Number(formData.storeId),
        items: [
          {
            productId: Number(formData.productId),
            quantity: Number(formData.quantity),
          },
        ],
      });

      setSuccessMessage("Venta registrada correctamente.");
      setFormData({
        storeId: "",
        productId: "",
        quantity: "",
      });

      await reload();
    } catch (err) {
      setError(err.response?.data?.message || "No fue posible registrar la venta.");
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <section className="section-card">
      <div className="section-actions">
        <div>
          <h2>Ventas</h2>
          <p>Registro y consulta de ventas realizadas en los locales.</p>
        </div>
      </div>

      <CrudForm
        title="Registrar venta"
        fields={fields}
        formData={formData}
        onChange={handleChange}
        onSubmit={handleSubmit}
        onCancel={() =>
          setFormData({
            storeId: "",
            productId: "",
            quantity: "",
          })
        }
        isSaving={isSaving}
      />

      <DataTable columns={columns} data={sales} />
    </section>
  );
}

export default SalesModule;