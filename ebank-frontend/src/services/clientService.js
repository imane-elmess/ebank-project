import { apiFetch } from "./api";

/**
 * Récupérer la liste des clients
 */
export async function getClients() {
  return await apiFetch("/api/clients");
}

export async function getClient(id) {
  return await apiFetch(`/api/clients/${id}`);
}

export async function createClient(payload) {
  return apiFetch("/api/clients", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}
