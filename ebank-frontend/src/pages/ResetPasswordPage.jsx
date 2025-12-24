import { useMemo, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { resetPassword } from "../services/passwordService";

export default function ResetPasswordPage() {
  const navigate = useNavigate();
  const [params] = useSearchParams();

  const token = useMemo(() => params.get("token") || "", [params]);

  const [newPassword, setNewPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [msg, setMsg] = useState("");

  async function onSubmit(e) {
    e.preventDefault();

    if (!token) {
      setMsg("❌ Token manquant dans l’URL.");
      return;
    }

    if (newPassword.length < 6) {
      setMsg("❌ Mot de passe trop court (min 6).");
      return;
    }

    if (newPassword !== confirm) {
      setMsg("❌ Confirmation incorrecte.");
      return;
    }

    setMsg("Réinitialisation...");

    try {
      await resetPassword(token, newPassword);
      setMsg("✅ Mot de passe mis à jour. Tu peux te connecter.");
      navigate("/login");
    } catch (e) {
      console.error(e);
      setMsg("❌ Token invalide/expiré ou erreur technique.");
    }
  }

  return (
    <div className="app-container">
      <div className="login-card">
        <h2>Nouveau mot de passe</h2>
        <p className="subtitle">Définis un nouveau mot de passe sécurisé</p>

        {!token && (
          <div className="error-message">
            Token manquant. Ouvre le lien reçu par email.
          </div>
        )}

        {msg && (
          <div
            className={msg.startsWith("✅") ? "info-message" : "error-message"}
          >
            {msg}
          </div>
        )}

        <form onSubmit={onSubmit}>
          <div className="form-group">
            <label htmlFor="newPassword">Nouveau mot de passe</label>
            <input
              id="newPassword"
              type="password"
              placeholder="Minimum 6 caractères"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="confirm">Confirmer le mot de passe</label>
            <input
              id="confirm"
              type="password"
              placeholder="Retape le même mot de passe"
              value={confirm}
              onChange={(e) => setConfirm(e.target.value)}
              required
            />
          </div>

          <button className="btn-login" type="submit">
            Valider
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
