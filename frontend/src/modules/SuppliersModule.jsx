import { useState } from "react";
import apiClient from "../api/apiClient";
import DataTable from "../components/dashboard/DataTable";
import CrudForm from "../components/dashboard/CrudForm";

const initialForm = {
  name: "",
  phone: "",
  email: "",
  address: "",
};

function SuppliersModule({ suppliers, isAdmin, reload, setError, setSuccessMessage }) {
  const [formData, setFormData] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [isSaving, setIsSaving] = useState(false);

  const fields = [
    { name: "name", label: "Nombre", required: true },
    { name: "phone", label: "Teléfono", required: true },
    { name: "email", label: "Correo", type: "email", required: true },
    { name: "address", label: "Dirección", required: true },
  ];

  const columns = [
    { key: "name", label: "Nombre" },
    { key: "phone", label: "Teléfono" },
    { key: "email", label: "Correo" },
    { key: "address", label: "Dirección" },
    { key: "status", label: "Estado" },
  ];

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((current) => ({ ...current, [name]: value }));
  };

  const openCreate = () => {
    setEditingId(null);
    setFormData(initialForm);
    setShowForm(true);
  };

  const openEdit = (supplier) => {
    setEditingId(supplier.id);
    setFormData({
      name: supplier.name || "",
      phone: supplier.phone || "",
      email: supplier.email || "",
      address: supplier.address || "",
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
        await apiClient.put(`/proveedores/${editingId}`, formData);
        setSuccessMessage("Proveedor actualizado correctamente.");
      } else {
        await apiClient.post("/proveedores", formData);
        setSuccessMessage("Proveedor creado correctamente.");
      }

      setShowForm(false);
      setEditingId(null);
      setFormData(initialForm);
      await reload();
    } catch (err) {
      setError(err.response?.data?.message || "No fue posible guardar el proveedor.");
    } finally {
      setIsSaving(false);
    }
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm("¿Seguro que deseas eliminar este proveedor?");
    if (!confirmed) return;

    try {
      setError("");
      setSuccessMessage("");
      await apiClient.delete(`/proveedores/${id}`);
      setSuccessMessage("Proveedor eliminado correctamente.");
      await reload();
    } catch (err) {
      setError(err.response?.data?.message || "No fue posible eliminar el proveedor.");
    }
  };

  return (
    <section className="section-card">
      <div className="section-actions">
        <div>
          <h2>Proveedores</h2>
          <p>Administración de proveedores registrados en el sistema.</p>
        </div>

        {isAdmin && (
          <button className="primary-action" onClick={openCreate}>
            Nuevo proveedor
          </button>
        )}
      </div>

      {showForm && (
        <CrudForm
          title={editingId ? "Editar proveedor" : "Nuevo proveedor"}
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
        data={suppliers}
        actions={
          isAdmin
            ? (supplier) => (
                <>
                  <button className="edit-button" onClick={() => openEdit(supplier)}>
                    Editar
                  </button>

                  <button className="delete-button" onClick={() => handleDelete(supplier.id)}>
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

export default SuppliersModule;