import { apiFetch } from "./api";

// Demande de reset (envoie email)
export async function forgotPassword(email) {
  // 204 No Content
  return apiFetch("/api/auth/forgot-password", {
    method: "POST",
    body: JSON.stringify({ email }),
  });
}

// Reset avec token + nouveau mot de passe
export async function resetPassword(token, newPassword) {
  // 204 No Content
  return apiFetch("/api/auth/reset-password", {
    method: "POST",
    body: JSON.stringify({ token, newPassword }),
  });
}
