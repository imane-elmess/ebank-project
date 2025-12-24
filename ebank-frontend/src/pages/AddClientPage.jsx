import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createClient } from "../services/clientService";

export default function AddClientPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    identityNumber: "",
    firstName: "",
    lastName: "",
    birthDate: "",
    email: "",
    postalAddress: "",
  });

  const [msg, setMsg] = useState("");

  function onChange(e) {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  }

  async function onSubmit(e) {
    e.preventDefault();
    setMsg("Création...");

    try {
      await createClient(form);
      setMsg("✅ Client créé avec succès !");
      navigate("/dashboard");
    } catch (err) {
      console.error(err);
      setMsg("❌ Erreur : vérifie les champs / rôle AGENT.");
    }
  }

  return (
    <div className="app-container">
      <div className="login-card">
        <h2>Ajouter un client</h2>
        <p className="subtitle">Créer un nouveau client dans le système</p>

        {msg && (
          <div
            className={msg.startsWith("✅") ? "info-message" : "error-message"}
          >
            {msg}
          </div>
        )}

        <form onSubmit={onSubmit}>
          <div className="form-group">
            <label htmlFor="identityNumber">CIN *</label>
            <input
              id="identityNumber"
              name="identityNumber"
              type="text"
              placeholder="Ex: AB123456"
              value={form.identityNumber}
              onChange={onChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="firstName">Prénom *</label>
            <input
              id="firstName"
              name="firstName"
              type="text"
              placeholder="Prénom du client"
              value={form.firstName}
              onChange={onChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="lastName">Nom *</label>
            <input
              id="lastName"
              name="lastName"
              type="text"
              placeholder="Nom du client"
              value={form.lastName}
              onChange={onChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="birthDate">Date de naissance *</label>
            <input
              id="birthDate"
              name="birthDate"
              type="date"
              value={form.birthDate}
              onChange={onChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email *</label>
            <input
              id="email"
              name="email"
              type="email"
              placeholder="client@example.com"
              value={form.email}
              onChange={onChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="postalAddress">Adresse postale *</label>
            <input
              id="postalAddress"
              name="postalAddress"
              type="text"
              placeholder="Adresse complète"
              value={form.postalAddress}
              onChange={onChange}
              required
            />
          </div>

          <button className="btn-login" type="submit">
            Créer le client
          </button>

          <button
            type="button"
            className="btn-secondary"
            onClick={() => navigate("/dashboard")}
          >
            Annuler
          </button>
        </form>
      </div>
    </div>
  );
}
