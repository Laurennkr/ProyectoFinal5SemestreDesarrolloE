import { useState } from "react";
import apiClient from "../api/apiClient";
import DataTable from "../components/dashboard/DataTable";
import CrudForm from "../components/dashboard/CrudForm";

const initialForm = {
  name: "",
  unit: "",
  availableQuantity: "",
  minimumStock: "",
  description: "",
  supplierId: "",
};

function SuppliesModule({ supplies, suppliers, isAdmin, reload, setError, setSuccessMessage }) {
  const [formData, setFormData] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [isSaving, setIsSaving] = useState(false);

  const fields = [
    { name: "name", label: "Nombre", required: true },
    { name: "unit", label: "Unidad", required: true },
    { name: "availableQuantity", label: "Cantidad disponible", type: "number", min: "1", required: true },
    { name: "minimumStock", label: "Stock mínimo", type: "number", min: "1", required: true },
    { name: "description", label: "Descripción" },
    {
      name: "supplierId",
      label: "Proveedor",
      type: "select",
      required: true,
      options: suppliers.map((supplier) => ({
        value: supplier.id,
        label: supplier.name,
      })),
    },
  ];

  const columns = [
    { key: "name", label: "Nombre" },
    { key: "unit", label: "Unidad" },
    { key: "availableQuantity", label: "Cantidad" },
    { key: "minimumStock", label: "Mínimo" },
    {
      key: "supplierName",
      label: "Proveedor",
      render: (item) => item.supplierName || item.supplierId || "No registrado",
    },
    { key: "status", label: "Estado" },
  ];

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((current) => ({ ...current, [name]: value }));
  };

  const buildPayload = () => ({
    name: formData.name,
    unit: formData.unit,
    availableQuantity: Number(formData.availableQuantity),
    minimumStock: Number(formData.minimumStock),
    description: formData.description,
    supplierId: Number(formData.supplierId),
  });

  const openCreate = () => {
    setEditingId(null);
    setFormData(initialForm);
    setShowForm(true);
  };

  const openEdit = (supply) => {
    setEditingId(supply.id);
    setFormData({
      name: supply.name || "",
      unit: supply.unit || "",
      availableQuantity: supply.availableQuantity ?? "",
      minimumStock: supply.minimumStock ?? "",
      description: supply.description || "",
      supplierId: supply.supplierId || "",
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
        await apiClient.put(`/insumos/${editingId}`, buildPayload());
        setSuccessMessage("Insumo actualizado correctamente.");
      } else {
        await apiClient.post("/insumos", buildPayload());
        setSuccessMessage("Insumo creado correctamente.");
      }

      setShowForm(false);
      setEditingId(null);
      setFormData(initialForm);
      await reload();
    } catch (err) {
      setError(err.response?.data?.message || "No fue posible guardar el insumo.");
    } finally {
      setIsSaving(false);
    }
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm("¿Seguro que deseas eliminar este insumo?");
    if (!confirmed) return;

    try {
      setError("");
      setSuccessMessage("");
      await apiClient.delete(`/insumos/${id}`);
      setSuccessMessage("Insumo eliminado correctamente.");
      await reload();
    } catch (err) {
      setError(err.response?.data?.message || "No fue posible eliminar el insumo.");
    }
  };

  return (
    <section className="section-card">
      <div className="section-actions">
        <div>
          <h2>Insumos</h2>
          <p>Administración y consulta de insumos disponibles.</p>
        </div>

        {isAdmin && (
          <button className="primary-action" onClick={openCreate}>
            Nuevo insumo
          </button>
        )}
      </div>

      {showForm && (
        <CrudForm
          title={editingId ? "Editar insumo" : "Nuevo insumo"}
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
        data={supplies}
        actions={
          isAdmin
            ? (supply) => (
                <>
                  <button className="edit-button" onClick={() => openEdit(supply)}>
                    Editar
                  </button>

                  <button className="delete-button" onClick={() => handleDelete(supply.id)}>
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

export default SuppliesModule;