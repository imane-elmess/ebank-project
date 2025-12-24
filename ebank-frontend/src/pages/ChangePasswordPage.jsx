import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { changePassword } from "../services/meAuthService";

export default function ChangePasswordPage() {
  const navigate = useNavigate();

  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [msg, setMsg] = useState("");

  async function onSubmit(e) {
    e.preventDefault();

    if (newPassword.length < 6) {
      setMsg("❌ Mot de passe trop court (min 6).");
      return;
    }
    if (newPassword !== confirm) {
      setMsg("❌ Confirmation incorrecte.");
      return;
    }

    setMsg("Changement en cours...");

    try {
      await changePassword(oldPassword, newPassword);
      setMsg("✅ Mot de passe modifié.");
      navigate(-1); // revient à la page précédente
    } catch (e) {
      console.error(e);
      setMsg("❌ Ancien mot de passe incorrect ou erreur.");
    }
  }

  return (
    <div className="app-container">
      <div className="login-card">
        <h2>Changer mon mot de passe</h2>
        <p className="subtitle">
          Mets à jour votre mot de passe pour sécuriser votre compte
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
            <label htmlFor="oldPassword">Ancien mot de passe</label>
            <input
              id="oldPassword"
              type="password"
              placeholder="Saisis votre mot de passe actuel"
              value={oldPassword}
              onChange={(e) => setOldPassword(e.target.value)}
              required
            />
          </div>

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
            <label htmlFor="confirm">Confirmer le nouveau mot de passe</label>
            <input
              id="confirm"
              type="password"
              placeholder="Retape le nouveau mot de passe"
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
            onClick={() => navigate(-1)}
          >
            Annuler
          </button>
        </form>
      </div>
    </div>
  );
}
