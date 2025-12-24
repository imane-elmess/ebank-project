import { apiFetch } from "./api";

export async function transferMoney(fromAccountId, toRib, amount, motif) {
  return apiFetch("/api/transfers", {
    method: "POST",
    body: JSON.stringify({
      fromAccountId,
      toRib,
      amount,
      motif,
    }),
  });
}
