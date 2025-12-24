import { apiFetch } from "./api";

/**
 * Connexion : appelle /api/auth/login, stocke token + infos, puis retourne data
 */
export async function login(username, password) {
  const data = await apiFetch("/api/auth/login", {
    method: "POST",
    body: JSON.stringify({ username, password }),
  });

  // Stockage
  localStorage.setItem("token", data.token);
  localStorage.setItem("username", data.username);
  localStorage.setItem("role", data.role);

  return data;
}

/**
 * Déconnexion : supprime les infos stockées
 */
export function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("username");
  localStorage.removeItem("role");
}

/**
 * Utilitaire : savoir si l'utilisateur est connecté (token présent)
 */
export function isAuthenticated() {
  return !!localStorage.getItem("token");
}

/**
 * Utilitaire : récupérer infos utilisateur
 */
export function getAuthUser() {
  return {
    username: localStorage.getItem("username"),
    role: localStorage.getItem("role"),
  };
}
