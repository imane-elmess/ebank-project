import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { forgotPassword } from "../services/passwordService";

export default function ForgotPasswordPage() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [msg, setMsg] = useState("");

  async function onSubmit(e) {
    e.preventDefault();
    setMsg("Envoi de la demande...");

    try {
      await forgotPassword(email);
      // même si email n’existe pas, backend répond 204 (sécurité)
      setMsg("✅ Si l’email existe, un lien de réinitialisation a été envoyé.");
    } catch (e) {
      console.error(e);
      setMsg("❌ Erreur technique. Réessaie.");
    }
  }

  return (
    <div className="app-container">
      <div className="login-card">
        <h2>Mot de passe oublié</h2>
        <p className="subtitle">
          Saisis ton email. Tu recevras un lien de réinitialisation.
        </p>

        {msg && (
          <div
            className={msg.startsWith("✅") ? "info-message" : "error-message"}
          >
            {msg}
          </div>
        )}

        <form onSubmit={onSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              placeholder="agent@ebank.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <button className="btn-login" type="submit">
            Envoyer
          </button>

          <button
            type="button"
            className="btn-secondary"
            onClick={() => navigate("/login")}
          >
            Retour à la connexion
          </button>
        </form>
      </div>
    </div>
  );
}
