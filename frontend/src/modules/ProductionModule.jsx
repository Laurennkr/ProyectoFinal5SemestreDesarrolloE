import { useState } from "react";
import apiClient from "../api/apiClient";
import DataTable from "../components/dashboard/DataTable";
import CrudForm from "../components/dashboard/CrudForm";

function ProductionModule({
  productionSheets,
  products,
  supplies,
  user,
  isAdmin,
  isTailor,
  reload,
  setError,
  setSuccessMessage,
}) {
  const [sheetForm, setSheetForm] = useState({
    title: "",
    requestedQuantity: "",
    referenceImageUrl: "",
    cuttingGuide: "",
    deadline: "",
    productId: "",
    assignedTailorId: "",
  });

  const [reportForm, setReportForm] = useState({
    productionSheetId: "",
    completedQuantity: "",
    observations: "",
  });

  const [shortageForm, setShortageForm] = useState({
    productionSheetId: "",
    supplyId: "",
    missingQuantity: "",
    reason: "",
  });

  const columns = [
    { key: "id", label: "ID" },
    { key: "title", label: "Título" },
    {
      key: "productName",
      label: "Producto",
      render: (item) => item.productName || item.productId || "No registrado",
    },
    {
      key: "requestedQuantity",
      label: "Solicitadas",
      render: (item) => item.requestedQuantity ?? "No registrado",
    },
    {
      key: "completedQuantity",
      label: "Producidas",
      render: (item) =>
        item.completedQuantity ??
        item.producedQuantity ??
        item.finishedQuantity ??
        "No disponible",
    },
    {
      key: "pendingQuantity",
      label: "Pendientes",
      render: (item) =>
        item.pendingQuantity ??
        item.remainingQuantity ??
        "No disponible",
    },
    { key: "deadline", label: "Fecha límite" },
    { key: "status", label: "Estado" },
  ];

  const sheetFields = [
    {
      name: "title",
      label: "Título",
      required: true,
    },
    {
      name: "requestedQuantity",
      label: "Cantidad solicitada",
      type: "number",
      min: "1",
      required: true,
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
      name: "assignedTailorId",
      label: "ID del costurero asignado",
      type: "number",
      min: "1",
      required: true,
    },
    {
      name: "deadline",
      label: "Fecha límite",
      type: "date",
      required: true,
    },
    {
      name: "referenceImageUrl",
      label: "URL imagen de referencia",
    },
    {
      name: "cuttingGuide",
      label: "Guía de corte",
    },
  ];

  const reportFields = [
    {
      name: "productionSheetId",
      label: "Ficha",
      type: "select",
      required: true,
      options: productionSheets.map((sheet) => ({
        value: sheet.id,
        label: sheet.title || `Ficha ${sheet.id}`,
      })),
    },
    {
      name: "completedQuantity",
      label: "Cantidad completada",
      type: "number",
      min: "1",
      required: true,
    },
    {
      name: "observations",
      label: "Observaciones",
    },
  ];

  const shortageFields = [
    {
      name: "productionSheetId",
      label: "Ficha",
      type: "select",
      required: true,
      options: productionSheets.map((sheet) => ({
        value: sheet.id,
        label: sheet.title || `Ficha ${sheet.id}`,
      })),
    },
    {
      name: "supplyId",
      label: "Insumo",
      type: "select",
      required: true,
      options: supplies.map((supply) => ({
        value: supply.id,
        label: supply.name,
      })),
    },
    {
      name: "missingQuantity",
      label: "Cantidad faltante",
      type: "number",
      min: "1",
      required: true,
    },
    {
      name: "reason",
      label: "Motivo",
      required: true,
    },
  ];

  const handleSheetChange = (event) => {
    const { name, value } = event.target;

    setSheetForm((current) => ({
      ...current,
      [name]: value,
    }));
  };

  const handleReportChange = (event) => {
    const { name, value } = event.target;

    setReportForm((current) => ({
      ...current,
      [name]: value,
    }));
  };

  const handleShortageChange = (event) => {
    const { name, value } = event.target;

    setShortageForm((current) => ({
      ...current,
      [name]: value,
    }));
  };

  const getUserId = () => user?.userId || user?.id;

  const handleCreateSheet = async (event) => {
    event.preventDefault();

    try {
      setError("");
      setSuccessMessage("");

      await apiClient.post("/fichas-produccion", {
        title: sheetForm.title,
        requestedQuantity: Number(sheetForm.requestedQuantity),
        referenceImageUrl: sheetForm.referenceImageUrl,
        cuttingGuide: sheetForm.cuttingGuide,
        deadline: sheetForm.deadline,
        productId: Number(sheetForm.productId),
        assignedTailorId: Number(sheetForm.assignedTailorId),
        requiredMaterials: [],
      });

      setSuccessMessage("Ficha de producción creada correctamente.");

      setSheetForm({
        title: "",
        requestedQuantity: "",
        referenceImageUrl: "",
        cuttingGuide: "",
        deadline: "",
        productId: "",
        assignedTailorId: "",
      });

      await reload();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "No fue posible crear la ficha de producción."
      );
    }
  };

  const handleReportSubmit = async (event) => {
    event.preventDefault();

    try {
      setError("");
      setSuccessMessage("");

      await apiClient.post(`/producciones/${getUserId()}`, {
        productionSheetId: Number(reportForm.productionSheetId),
        completedQuantity: Number(reportForm.completedQuantity),
        observations: reportForm.observations,
      });

      setSuccessMessage("Avance de producción reportado correctamente.");

      setReportForm({
        productionSheetId: "",
        completedQuantity: "",
        observations: "",
      });

      await reload();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "No fue posible reportar el avance de producción."
      );
    }
  };

  const handleShortageSubmit = async (event) => {
    event.preventDefault();

    try {
      setError("");
      setSuccessMessage("");

      await apiClient.post(`/faltantes/${getUserId()}`, {
        productionSheetId: Number(shortageForm.productionSheetId),
        supplyId: Number(shortageForm.supplyId),
        missingQuantity: Number(shortageForm.missingQuantity),
        reason: shortageForm.reason,
      });

      setSuccessMessage("Faltante reportado correctamente.");

      setShortageForm({
        productionSheetId: "",
        supplyId: "",
        missingQuantity: "",
        reason: "",
      });

      await reload();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "No fue posible reportar el faltante."
      );
    }
  };

  const updateStatus = async (sheetId, status) => {
    try {
      setError("");
      setSuccessMessage("");

      await apiClient.patch(`/fichas-produccion/${sheetId}/estado?status=${status}`);

      setSuccessMessage("Estado de ficha actualizado correctamente.");
      await reload();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "No fue posible actualizar el estado de la ficha."
      );
    }
  };

  const cancelSheet = async (sheetId) => {
    const confirmed = window.confirm("¿Seguro que deseas cancelar esta ficha?");
    if (!confirmed) return;

    try {
      setError("");
      setSuccessMessage("");

      await apiClient.delete(`/fichas-produccion/${sheetId}/cancelar`);

      setSuccessMessage("Ficha cancelada correctamente.");
      await reload();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "No fue posible cancelar la ficha."
      );
    }
  };

  return (
    <section className="section-card">
      <div className="section-actions">
        <div>
          <h2>Producción</h2>
          <p>Creación, consulta y seguimiento de fichas de producción.</p>
        </div>
      </div>

      {isAdmin && (
        <CrudForm
          title="Crear ficha de producción"
          fields={sheetFields}
          formData={sheetForm}
          onChange={handleSheetChange}
          onSubmit={handleCreateSheet}
          onCancel={() =>
            setSheetForm({
              title: "",
              requestedQuantity: "",
              referenceImageUrl: "",
              cuttingGuide: "",
              deadline: "",
              productId: "",
              assignedTailorId: "",
            })
          }
        />
      )}

      {isTailor && (
        <>
          <CrudForm
            title="Reportar avance"
            fields={reportFields}
            formData={reportForm}
            onChange={handleReportChange}
            onSubmit={handleReportSubmit}
            onCancel={() =>
              setReportForm({
                productionSheetId: "",
                completedQuantity: "",
                observations: "",
              })
            }
          />

          <CrudForm
            title="Reportar faltante"
            fields={shortageFields}
            formData={shortageForm}
            onChange={handleShortageChange}
            onSubmit={handleShortageSubmit}
            onCancel={() =>
              setShortageForm({
                productionSheetId: "",
                supplyId: "",
                missingQuantity: "",
                reason: "",
              })
            }
          />
        </>
      )}

      <DataTable
        columns={columns}
        data={productionSheets}
        actions={(sheet) => (
          <>
            <button
              className="edit-button"
              onClick={() => updateStatus(sheet.id, "IN_PROGRESS")}
            >
              En proceso
            </button>

            <button
              className="edit-button"
              onClick={() => updateStatus(sheet.id, "COMPLETED")}
            >
              Completar
            </button>

            {isAdmin && (
              <button
                className="delete-button"
                onClick={() => cancelSheet(sheet.id)}
              >
                Cancelar
              </button>
            )}
          </>
        )}
      />
    </section>
  );
}

export default ProductionModule;