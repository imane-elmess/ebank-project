const BASE_URL = "http://localhost:8080";

/**
 * Fonction générique pour appeler le backend
 */
export async function apiFetch(endpoint, options = {}) {
  const token = localStorage.getItem("token");

  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {}),
  };

  // Si token présent → on l'ajoute automatiquement
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  // Si erreur HTTP
  if (!response.ok) {
    let errMsg = `Erreur API : ${response.status}`;

    // essayer de lire le message backend (ex: RG_3)
    try {
      const errBody = await response.json();
      if (errBody?.message) errMsg = errBody.message;
    } catch {
      // ignore si body pas en JSON
    }

    // Cas spécial RG_3 : token expiré/invalide
    if (response.status === 401 && errMsg.includes("Session invalide")) {
      localStorage.removeItem("token");
      localStorage.removeItem("username");
      localStorage.removeItem("role");
    }

    throw new Error(errMsg);
  }

  // Si réponse vide (204 No Content)
  if (response.status === 204) {
    return null;
  }

  return response.json();
}
