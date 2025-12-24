import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getMyAccounts } from "../services/meService";
import { transferMoney } from "../services/transferService";

export default function TransferPage() {
  const navigate = useNavigate();

  const [accounts, setAccounts] = useState([]);
  const [fromAccountId, setFromAccountId] = useState("");
  const [toRib, setToRib] = useState("");
  const [amount, setAmount] = useState("");
  const [motif, setMotif] = useState("");
  const [msg, setMsg] = useState("");

  // Charger les comptes du client
  useEffect(() => {
    async function loadAccounts() {
      try {
        const data = await getMyAccounts();
        setAccounts(data);
        if (data.length > 0) setFromAccountId(String(data[0].id));
      } catch (e) {
        console.error(e);
        setMsg("❌ Impossible de charger vos comptes.");
      }
    }
    loadAccounts();
  }, []);

  async function onSubmit(e) {
    e.preventDefault();
    setMsg("Virement en cours...");

    try {
      await transferMoney(Number(fromAccountId), toRib, Number(amount), motif);
      setMsg("✅ Virement effectué avec succès !");
      navigate("/dashboard-client");
    } catch (e) {
      console.error(e);
      setMsg("❌ Erreur lors du virement (solde / RIB / statut).");
    }
  }

  return (
    <div className="app-container">
      <div className="login-card">
        <h2>Faire un virement</h2>
        <p className="subtitle">Effectuer un virement vers un autre compte</p>

        {msg && (
          <div
            className={msg.startsWith("✅") ? "info-message" : "error-message"}
          >
            {msg}
          </div>
        )}

        <form onSubmit={onSubmit}>
          <div className="form-group">
            <label htmlFor="fromAccountId">Compte source *</label>
            <select
              id="fromAccountId"
              value={fromAccountId}
              onChange={(e) => setFromAccountId(e.target.value)}
              required
            >
              {accounts.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.rib} — Solde: {a.balance}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="toRib">RIB destination *</label>
            <input
              id="toRib"
              type="text"
              value={toRib}
              onChange={(e) => setToRib(e.target.value)}
              placeholder="RIB du bénéficiaire"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="amount">Montant *</label>
            <input
              id="amount"
              type="number"
              min="1"
              step="0.01"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder="0.00"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="motif">Motif (optionnel)</label>
            <input
              id="motif"
              type="text"
              value={motif}
              onChange={(e) => setMotif(e.target.value)}
              placeholder="Ex: Loyer, Facture, etc."
            />
          </div>

          <button className="btn-login" type="submit">
            Effectuer le virement
          </button>

          <button
            type="button"
            className="btn-secondary"
            onClick={() => navigate("/dashboard-client")}
          >
            Annuler
          </button>
        </form>
      </div>
    </div>
  );
}
