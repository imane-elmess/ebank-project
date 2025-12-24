import { apiFetch } from "./api";

export async function getMyAccounts() {
  return apiFetch("/api/me/accounts", { method: "GET" });
}

export async function getMyOperations(accountId, page = 0, size = 10) {
  return apiFetch(
    `/api/me/accounts/${accountId}/operations?page=${page}&size=${size}`,
    {
      method: "GET",
    }
  );
}
