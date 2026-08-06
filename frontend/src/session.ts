import type { Session } from "./types";

const SESSION_KEY = "restaurant-session";

export function saveSession(session: Session): void {
  sessionStorage.setItem(SESSION_KEY, JSON.stringify(session));
}

export function loadSession(): Session | null {
  const raw = sessionStorage.getItem(SESSION_KEY);
  if (!raw) return null;

  try {
    return JSON.parse(raw) as Session;
  } catch {
    sessionStorage.removeItem(SESSION_KEY);
    return null;
  }
}

export function clearSession(): void {
  sessionStorage.removeItem(SESSION_KEY);
}
