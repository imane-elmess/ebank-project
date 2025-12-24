import { apiFetch } from "./api";

// Créer un compte (AGENT)
export async function createAccount(clientId, initialBalance) {
  return apiFetch("/api/accounts", {
    method: "POST",
    body: JSON.stringify({ clientId, initialBalance }),
  });
}

// Récupérer les comptes d’un client (AGENT)
export async function getAccountsByClient(clientId) {
  return apiFetch(`/api/accounts/client/${clientId}`, {
    method: "GET",
  });
}
