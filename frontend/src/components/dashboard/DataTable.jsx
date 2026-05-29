function DataTable({ columns, data, actions }) {
  return (
    <table>
      <thead>
        <tr>
          {columns.map((column) => (
            <th key={column.key}>{column.label}</th>
          ))}

          {actions && <th>Acciones</th>}
        </tr>
      </thead>

      <tbody>
        {data.length === 0 && (
          <tr>
            <td colSpan={actions ? columns.length + 1 : columns.length}>
              No hay registros disponibles.
            </td>
          </tr>
        )}

        {data.map((item) => (
          <tr key={item.id}>
            {columns.map((column) => (
              <td key={column.key}>
                {column.render ? column.render(item) : item[column.key] || "No registrado"}
              </td>
            ))}

            {actions && (
              <td>
                <div className="row-actions">
                  {actions(item)}
                </div>
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default DataTable;