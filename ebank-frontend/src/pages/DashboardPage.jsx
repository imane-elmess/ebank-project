import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getClients } from "../services/clientService";
import { useAuth } from "../context/AuthContext";
import "./DashboardPage.css";

function DashboardPage() {
  const [clients, setClients] = useState([]);
  const [clientsLoading, setClientsLoading] = useState(false);
  const [clientsError, setClientsError] = useState("");

  const navigate = useNavigate();
  const { user, role } = useAuth(); //context

  const username = user.username;
  const totalClients = clients.length;

  useEffect(() => {
    async function fetchClients() {
      setClientsLoading(true);
      setClientsError("");

      try {
        const data = await getClients(); // apiFetch prend le token depuis localStorage
        setClients(data);
      } catch (error) {
        console.error("Erreur clients:", error);
        setClientsError("Erreur technique lors du chargement des clients.");
      } finally {
        setClientsLoading(false);
      }
    }

    fetchClients();
  }, []);

  return (
    <div className="agent-dashboard">
      <div className="agent-header">
        <div className="agent-header-text">
          <h2>Dashboard Agent</h2>
          <p className="subtitle">Gérer clients et les comptes</p>
        </div>
        <div className="agent-header-actions">
          <button
            className="action-btn action-btn-secondary"
            onClick={() => navigate("/accounts/new")}
          >
            <i className="ri-wallet-3-line"></i>
            <span>Nouveau compte</span>
          </button>
          <button
            className="action-btn action-btn-primary"
            onClick={() => navigate("/clients/new")}
          >
            <i className="ri-user-add-line"></i>
            <span>Nouveau Client</span>
          </button>
        </div>
      </div>

      <div className="stats-grid">
        <div className="stat-card stat-card-primary">
          <div className="stat-icon">
            <i class="ri-group-fill"></i>
          </div>
          <div>
            <p>Total Clients</p>
            <h3>{totalClients}</h3>
          </div>
        </div>
        <div className="stat-card stat-card-blue">
          <div className="stat-icon">
            <i class="ri-wallet-fill"></i>
          </div>
          <div>
            <p>Comptes actifs</p>
            <h3>--</h3>
          </div>
        </div>
      </div>

      <div className="agent-card">
        <div className="agent-card-header">
          <h3>Liste des clients</h3>
          <input
            id="client-search"
            className="client-search-input"
            type="text"
            placeholder="Rechercher par CIN..."
            aria-label="Rechercher par CIN"
          />
        </div>

        {clientsLoading && (
          <div className="loading-state">Chargement des clients...</div>
        )}
        {clientsError && <div className="error-state">{clientsError}</div>}

        {!clientsLoading && !clientsError && clients.length === 0 && (
          <div className="empty-state">Aucun client pour l'instant.</div>
        )}

        {clients.length > 0 && (
          <div className="clients-table">
            <table>
              <thead>
                <tr>
                  <th>Client</th>
                  <th>CIN</th>
                  <th className="actions-col">Actions</th>
                </tr>
              </thead>
              <tbody>
                {clients.map((c) => (
                  <tr key={c.id}>
                    <td>
                      {c.firstName} {c.lastName}
                    </td>
                    <td>{c.identityNumber}</td>
                    <td className="actions-col">
                      <button
                        type="button"
                        className="action-icon-btn"
                        title="Modifier"
                        aria-label="Modifier"
                      >
                        <i class="ri-pencil-fill"></i>
                      </button>
                      <button
                        type="button"
                        className="action-icon-btn danger"
                        title="Supprimer"
                        aria-label="Supprimer"
                      >
                        <i class="ri-delete-bin-fill"></i>
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

export default DashboardPage;
