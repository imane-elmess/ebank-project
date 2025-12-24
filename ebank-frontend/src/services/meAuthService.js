import { apiFetch } from "./api";

export async function changePassword(oldPassword, newPassword) {
  return apiFetch("/api/me/change-password", {
    method: "POST",
    body: JSON.stringify({ oldPassword, newPassword }),
  });
}
