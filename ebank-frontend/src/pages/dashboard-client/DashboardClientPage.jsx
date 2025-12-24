import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { getMyAccounts, getMyOperations } from "../../services/meService";
import "./DashboardClientPage.css";

export default function DashboardClientPage() {
  const { user, role, logout } = useAuth();
  const navigate = useNavigate();

  const [accounts, setAccounts] = useState([]);
  const [selectedAccountId, setSelectedAccountId] = useState("");
  const [opsPage, setOpsPage] = useState(null);
  const [opsLoading, setOpsLoading] = useState(false);
  const [opsError, setOpsError] = useState("");
  const [page, setPage] = useState(0);
  const size = 10;

  const username = user?.username || "Client";

  useEffect(() => {
    async function loadAccounts() {
      try {
        const data = await getMyAccounts();
        setAccounts(data);
        if (data.length > 0) {
          setSelectedAccountId(String(data[0].id));
        }
      } catch (e) {
        console.error(e);
        setOpsError("Impossible de charger vos comptes.");
      }
    }
    loadAccounts();
  }, []);

  useEffect(() => {
    async function loadOps() {
      if (!selectedAccountId) return;
      setOpsLoading(true);
      setOpsError("");
      try {
        const data = await getMyOperations(selectedAccountId, page, size);
        setOpsPage(data);
      } catch (e) {
        console.error(e);
        setOpsError(" Impossible de charger les opérations.");
      } finally {
        setOpsLoading(false);
      }
    }
    loadOps();
  }, [selectedAccountId, page]);

  const onChangeAccount = (e) => {
    setSelectedAccountId(e.target.value);
    setPage(0);
  };

  const selectedAccount = accounts.find(
    (a) => String(a.id) === selectedAccountId
  );

  return (
    <div className="client-dashboard">
      <div className="client-header">
        <h1>Bienvenue, {username} !</h1>
      </div>

      <div className="card accounts-card">
        <div className="accounts-header">
          <h2>Mes Comptes</h2>
          <button
            className="gold-btn transfer-btn"
            onClick={() => navigate("/transfer")}
          >
            <i className="ri-hand-coin-line"></i>
            Virement
          </button>
        </div>
        {accounts.length === 0 ? (
          <p>Aucun compte trouvé.</p>
        ) : (
          <>
            <label htmlFor="account-select">Choisir un compte :</label>
            <select
              id="account-select"
              value={selectedAccountId}
              onChange={onChangeAccount}
            >
              {accounts.map((a, idx) => (
                <option key={a.id} value={a.id}>
                  {a.rib} - {a.balance} - {a.status}
                </option>
              ))}
            </select>

            {selectedAccount && (
              <div className="account-details">
                <p>
                  <strong>RIB:</strong> {selectedAccount.rib}
                </p>
                <p>
                  <strong>Solde:</strong> {selectedAccount.balance}
                </p>
                <p>
                  <strong>Statut:</strong> {selectedAccount.status}
                </p>
                <p>
                  <strong>Dernier mouvement:</strong>{" "}
                  {selectedAccount.lastMovementAt
                    ? new Date(selectedAccount.lastMovementAt).toLocaleString()
                    : ""}
                </p>
              </div>
            )}
          </>
        )}
      </div>

      <div className="card operations-card">
        <h2>Dernières Opérations</h2>
        {opsLoading && <p>Chargement...</p>}
        {opsError && <p className="error-text">{opsError}</p>}
        {!opsLoading && opsPage && opsPage.content?.length === 0 && (
          <p>Aucune opération.</p>
        )}
        {!opsLoading && opsPage && opsPage.content?.length > 0 && (
          <>
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Type</th>
                    <th>Montant</th>
                    <th>Description</th>
                    <th>RIB en face</th>
                    <th>Référence</th>
                  </tr>
                </thead>
                <tbody>
                  {opsPage.content.map((op) => (
                    <tr key={op.id}>
                      <td>{new Date(op.operationDate).toLocaleString()}</td>
                      <td>
                        <span
                          className={`badge ${
                            op.type === "CREDIT" ? "credit" : "debit"
                          }`}
                        >
                          {op.type}
                        </span>
                      </td>
                      <td>{op.amount}</td>
                      <td>{op.description}</td>
                      <td>{op.counterpartRib || ""}</td>
                      <td>{op.reference || ""}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <div className="pagination">
              <button
                disabled={page === 0}
                onClick={() => setPage((p) => p - 1)}
              >
                <i class="ri-arrow-left-circle-fill"></i> Précédent
              </button>
              <span>
                Page {opsPage.number + 1} / {opsPage.totalPages}
              </span>
              <button
                disabled={page >= opsPage.totalPages - 1}
                onClick={() => setPage((p) => p + 1)}
              >
                Suivant <i class="ri-arrow-right-circle-fill"></i>
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

