import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getClients } from "../services/clientService";
import { createAccount } from "../services/accountService";

export default function AddAccountPage() {
  const navigate = useNavigate();

  const [clients, setClients] = useState([]);
  const [clientId, setClientId] = useState("");
  const [initialBalance, setInitialBalance] = useState("0");
  const [msg, setMsg] = useState("");
  const [searchCin, setSearchCin] = useState(""); //pour saisir cin

  // Charger la liste des clients pour remplir le select
  useEffect(() => {
    async function load() {
      try {
        const data = await getClients();
        setClients(data);
        if (data.length > 0) setClientId(String(data[0].id)); // par défaut
      } catch (e) {
        console.error(e);
        setMsg("❌ Impossible de charger les clients.");
      }
    }
    load();
  }, []);

  async function onSubmit(e) {
    e.preventDefault();
    setMsg("Création du compte...");

    try {
      await createAccount(Number(clientId), Number(initialBalance));
      setMsg("✅ Compte créé avec succès !");
      navigate("/dashboard");
    } catch (e) {
      console.error(e);
      setMsg(
        "❌ Erreur lors de la création du compte (vérifie le rôle AGENT)."
      );
    }
  }
  const filteredClients = clients.filter((c) =>
    c.identityNumber.toLowerCase().includes(searchCin.toLowerCase())
  );

  return (
    <div className="app-container">
      <div className="login-card">
        <h2>Créer un compte</h2>
        <p className="subtitle">
          Ouvrir un nouveau compte bancaire pour un client
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
            <label htmlFor="searchCin">Rechercher par CIN</label>
            <input
              id="searchCin"
              type="text"
              placeholder="Ex: AB123456"
              value={searchCin}
              onChange={(e) => setSearchCin(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label htmlFor="clientId">Client *</label>
            <select
              id="clientId"
              value={clientId}
              onChange={(e) => setClientId(e.target.value)}
              required
            >
              {filteredClients.length === 0 && (
                <option value="">Aucun client trouvé</option>
              )}

              {filteredClients.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.firstName} {c.lastName} — {c.identityNumber}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="initialBalance">Solde initial *</label>
            <input
              id="initialBalance"
              type="number"
              value={initialBalance}
              onChange={(e) => setInitialBalance(e.target.value)}
              min="0"
              step="0.01"
              placeholder="0.00"
              required
            />
          </div>

          <button className="btn-login" type="submit">
            Créer le compte
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
