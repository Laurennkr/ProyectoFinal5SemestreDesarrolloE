import { useState } from "react";
import apiClient from "../api/apiClient";
import DataTable from "../components/dashboard/DataTable";
import CrudForm from "../components/dashboard/CrudForm";

const initialForm = {
  name: "",
  zone: "",
  type: "",
  address: "",
};

function StoresModule({ stores, isAdmin, reload, setError, setSuccessMessage }) {
  const [formData, setFormData] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [isSaving, setIsSaving] = useState(false);

  const fields = [
    { name: "name", label: "Nombre", required: true },
    { name: "zone", label: "Zona", required: true },
    { name: "type", label: "Tipo", required: true },
    { name: "address", label: "Dirección", required: true },
  ];

  const columns = [
    { key: "name", label: "Nombre" },
    { key: "zone", label: "Zona" },
    { key: "type", label: "Tipo" },
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

  const openEdit = (store) => {
    setEditingId(store.id);
    setFormData({
      name: store.name || "",
      zone: store.zone || "",
      type: store.type || "",
      address: store.address || "",
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
        await apiClient.put(`/locales/${editingId}`, formData);
        setSuccessMessage("Local actualizado correctamente.");
      } else {
        await apiClient.post("/locales", formData);
        setSuccessMessage("Local creado correctamente.");
      }

      setShowForm(false);
      setEditingId(null);
      setFormData(initialForm);
      await reload();
    } catch (err) {
      setError(err.response?.data?.message || "No fue posible guardar el local.");
    } finally {
      setIsSaving(false);
    }
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm("¿Seguro que deseas eliminar este local?");
    if (!confirmed) return;

    try {
      setError("");
      setSuccessMessage("");
      await apiClient.delete(`/locales/${id}`);
      setSuccessMessage("Local eliminado correctamente.");
      await reload();
    } catch (err) {
      setError(err.response?.data?.message || "No fue posible eliminar el local.");
    }
  };

  return (
    <section className="section-card">
      <div className="section-actions">
        <div>
          <h2>Locales</h2>
          <p>Administración de puntos de venta asociados al sistema.</p>
        </div>

        {isAdmin && (
          <button className="primary-action" onClick={openCreate}>
            Nuevo local
          </button>
        )}
      </div>

      {showForm && (
        <CrudForm
          title={editingId ? "Editar local" : "Nuevo local"}
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
        data={stores}
        actions={
          isAdmin
            ? (store) => (
                <>
                  <button className="edit-button" onClick={() => openEdit(store)}>
                    Editar
                  </button>

                  <button className="delete-button" onClick={() => handleDelete(store.id)}>
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

export default StoresModule;