function CrudForm({
  title,
  fields,
  formData,
  onChange,
  onSubmit,
  onCancel,
  isSaving,
}) {
  return (
    <form className="form-card" onSubmit={onSubmit}>
      <h3>{title}</h3>

      <div className="form-grid">
        {fields.map((field) => (
          <div className="form-field" key={field.name}>
            <label>{field.label}</label>

            {field.type === "select" ? (
              <select
                name={field.name}
                value={formData[field.name] || ""}
                onChange={onChange}
                required={field.required}
              >
                <option value="">Seleccione una opción</option>
                {field.options?.map((option) => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            ) : (
              <input
                type={field.type || "text"}
                name={field.name}
                value={formData[field.name] || ""}
                onChange={onChange}
                min={field.min}
                required={field.required}
              />
            )}
          </div>
        ))}
      </div>

      <div className="form-actions">
        <button className="primary-action" type="submit" disabled={isSaving}>
          {isSaving ? "Guardando..." : "Guardar"}
        </button>

        <button className="secondary-action" type="button" onClick={onCancel}>
          Cancelar
        </button>
      </div>
    </form>
  );
}

export default CrudForm;