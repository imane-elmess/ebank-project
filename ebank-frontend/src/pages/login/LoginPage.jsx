import { useState } from "react";
import { useNavigate } from "react-router-dom";
import LoginForm from "../../components/LoginForm";
import { useAuth } from "../../context/AuthContext";
import { ROLES } from "../../constants/roles";
import "./LoginPage.css";

function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [infoMessage, setInfoMessage] = useState("");

  const navigate = useNavigate();
  const { login } = useAuth();

  async function handleLogin(event) {
    event.preventDefault();
    setInfoMessage("Connexion en cours...");

    try {
      const data = await login(email, password);
      setInfoMessage(`✅ Connecté : ${data.username} (${data.role})`);
      if (data.role === ROLES.AGENT_GUICHET) {
        navigate("/dashboard");
      } else if (data.role === ROLES.CLIENT) {
        navigate("/dashboard-client");
      } else {
        navigate("/forbidden");
      }
    } catch (error) {
      console.error("Erreur login:", error);
      setInfoMessage("❌ Login ou mot de passe erronés.");
    }
  }

  return (
    <div className="login-wrapper dark">
      <LoginForm
        email={email}
        password={password}
        infoMessage={infoMessage}
        onEmailChange={setEmail}
        onPasswordChange={setPassword}
        onSubmit={handleLogin}
      />

      
    </div>
  );
}

export default LoginPage;
