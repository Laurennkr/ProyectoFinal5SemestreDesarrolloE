import { useState } from "react";
import apiClient from "../api/apiClient";
import DataTable from "../components/dashboard/DataTable";
import CrudForm from "../components/dashboard/CrudForm";

const initialForm = {
  reference: "",
  name: "",
  category: "",
  size: "",
  color: "",
  productionCost: "",
  salePrice: "",
};

function ProductsModule({ products, isAdmin, reload, setError, setSuccessMessage }) {
  const [formData, setFormData] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [isSaving, setIsSaving] = useState(false);

  const formatMoney = (value) => {
    if (value === null || value === undefined) return "$0";
    return `$${Number(value).toLocaleString("es-CO")}`;
  };

  const fields = [
    { name: "reference", label: "Referencia", required: true },
    { name: "name", label: "Nombre", required: true },
    { name: "category", label: "Categoría", required: true },
    { name: "size", label: "Talla", required: true },
    { name: "color", label: "Color", required: true },
    { name: "productionCost", label: "Costo producción", type: "number", min: "0", required: true },
    { name: "salePrice", label: "Precio venta", type: "number", min: "0", required: true },
  ];

  const columns = [
    { key: "reference", label: "Referencia" },
    { key: "name", label: "Nombre" },
    { key: "category", label: "Categoría" },
    { key: "size", label: "Talla" },
    { key: "color", label: "Color" },
    { key: "productionCost", label: "Costo", render: (item) => formatMoney(item.productionCost) },
    { key: "salePrice", label: "Precio", render: (item) => formatMoney(item.salePrice) },
    { key: "status", label: "Estado" },
  ];

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((current) => ({ ...current, [name]: value }));
  };

  const buildPayload = () => ({
    reference: formData.reference,
    name: formData.name,
    category: formData.category,
    size: formData.size,
    color: formData.color,
    productionCost: Number(formData.productionCost),
    salePrice: Number(formData.salePrice),
  });

  const openCreate = () => {
    setEditingId(null);
    setFormData(initialForm);
    setShowForm(true);
  };

  const openEdit = (product) => {
    setEditingId(product.id);
    setFormData({
      reference: product.reference || "",
      name: product.name || "",
      category: product.category || "",
      size: product.size || "",
      color: product.color || "",
      productionCost: product.productionCost ?? "",
      salePrice: product.salePrice ?? "",
    });
    setShowForm(true);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      setIsSaving(true);
      setError("");
      setSuccessMessage("");

      if (editingId) {
        await apiClient.put(`/productos/${editingId}`, buildPayload());
        setSuccessMessage("Producto actualizado correctamente.");
      } else {
        await apiClient.post("/productos", buildPayload());
        setSuccessMessage("Producto creado correctamente.");
      }

      setShowForm(false);
      setEditingId(null);
      setFormData(initialForm);
      await reload();
    } catch (err) {
      setError(err.response?.data?.message || "No fue posible guardar el producto.");
    } finally {
      setIsSaving(false);
    }
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm("¿Seguro que deseas eliminar este producto?");
    if (!confirmed) return;

    try {
      setError("");
      setSuccessMessage("");
      await apiClient.delete(`/productos/${id}`);
      setSuccessMessage("Producto eliminado correctamente.");
      await reload();
    } catch (err) {
      setError(err.response?.data?.message || "No fue posible eliminar el producto.");
    }
  };

  return (
    <section className="section-card">
      <div className="section-actions">
        <div>
          <h2>Productos</h2>
          <p>Administración de productos registrados para venta o producción.</p>
        </div>

        {isAdmin && (
          <button className="primary-action" onClick={openCreate}>
            Nuevo producto
          </button>
        )}
      </div>

      {showForm && (
        <CrudForm
          title={editingId ? "Editar producto" : "Nuevo producto"}
          fields={fields}
          formData={formData}
          onChange={handleChange}
          onSubmit={handleSubmit}
          onCancel={() => setShowForm(false)}
          isSaving={isSaving}
        />
      )}

      <DataTable
        columns={columns}
        data={products}
        actions={
          isAdmin
            ? (product) => (
                <>
                  <button className="edit-button" onClick={() => openEdit(product)}>
                    Editar
                  </button>

                  <button className="delete-button" onClick={() => handleDelete(product.id)}>
                    Eliminar
                  </button>
                </>
              )
            : null
        }
      />
    </section>
  );
}

export default ProductsModule;