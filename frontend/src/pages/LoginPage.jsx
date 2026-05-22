import { useState } from "react";
import apiClient from "../api/apiClient";
import "../styles/login.css";

function LoginPage({ onLogin }) {
  const [email, setEmail] = useState("admin@groccy.com");
  const [password, setPassword] = useState("Admin12345");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError("");

    try {
      const response = await apiClient.post("/auth/login", {
        email,
        password,
      });

      const data = response.data;

      localStorage.setItem("groccy_token", data.token);
      localStorage.setItem("groccy_user", JSON.stringify(data));

      onLogin(data);
    } catch (err) {
      setError("Correo o contraseña incorrectos. Verifica los datos e intenta nuevamente.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="login-page">
      <section className="login-card">
        <div className="login-brand">
          <div className="brand-icon">G</div>
          <div>
            <h1>Groccy</h1>
            <p>Sistema de gestión de producción, inventario y ventas</p>
          </div>
        </div>

        <form className="login-form" onSubmit={handleSubmit}>
          <h2>Iniciar sesión</h2>
          <p className="login-description">
            Accede con tu usuario asignado según tu rol dentro del sistema.
          </p>

          <label htmlFor="email">Correo electrónico</label>
          <input
            id="email"
            type="email"
            placeholder="admin@groccy.com"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
          />

          <label htmlFor="password">Contraseña</label>
          <input
            id="password"
            type="password"
            placeholder="Ingresa tu contraseña"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
          />

          {error && <p className="login-error">{error}</p>}

          <button type="submit" disabled={loading}>
            {loading ? "Ingresando..." : "Ingresar"}
          </button>
        </form>

        <div className="login-users">
          <p>Usuarios de prueba:</p>
          <span>Administrador: admin@groccy.com / Admin12345</span>
          <span>Vendedor: vendedor@groccy.com / Vendedor12345</span>
          <span>Costurero: costurero@groccy.com / Costurero12345</span>
        </div>
      </section>
    </main>
  );
}

export default LoginPage;