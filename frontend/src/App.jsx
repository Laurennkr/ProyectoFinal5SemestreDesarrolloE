import { useEffect, useState } from "react";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import "./App.css";

function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const storedUser = localStorage.getItem("groccy_user");

    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const handleLogin = (authData) => {
    setUser(authData);
  };

  const handleLogout = () => {
    localStorage.removeItem("groccy_token");
    localStorage.removeItem("groccy_user");
    setUser(null);
  };

  if (!user) {
    return <LoginPage onLogin={handleLogin} />;
  }

  return <DashboardPage user={user} onLogout={handleLogout} />;
}

export default App;
