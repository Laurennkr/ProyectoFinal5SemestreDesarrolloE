import apiClient from "../api/apiClient";
import DataTable from "../components/dashboard/DataTable";

function AlertsModule({ alerts, reload, setError, setSuccessMessage }) {
  const columns = [
    { key: "id", label: "ID" },
    {
      key: "type",
      label: "Tipo",
      render: (item) => item.type || item.alertType || "Sin tipo",
    },
    {
      key: "message",
      label: "Mensaje",
      render: (item) => item.message || item.description || "Sin mensaje",
    },
    { key: "status", label: "Estado" },
    {
      key: "createdAt",
      label: "Fecha",
      render: (item) => item.createdAt || item.alertDate || "Sin fecha",
    },
  ];

  const handleResolve = async (id) => {
    try {
      setError("");
      setSuccessMessage("");

      await apiClient.patch(`/alertas/${id}/resolver`);

      setSuccessMessage("Alerta resuelta correctamente.");
      await reload();
    } catch (err) {
      setError(err.response?.data?.message || "No fue posible resolver la alerta.");
    }
  };

  return (
    <section className="section-card">
      <div className="section-actions">
        <div>
          <h2>Alertas</h2>
          <p>Consulta y resolución de alertas activas del sistema.</p>
        </div>
      </div>

      <DataTable
        columns={columns}
        data={alerts}
        actions={(alert) => (
          <button className="edit-button" onClick={() => handleResolve(alert.id)}>
            Resolver
          </button>
        )}
      />
    </section>
  );
}

export default AlertsModule;