import { FormEvent, useState } from "react";
import { api } from "../api";
import type { Session, UserRole } from "../types";

interface Props {
  onLogin: (session: Session) => void;
}

const defaultPositionByUsername: Record<string, number> = {
  counter1: 1,
  counter2: 2,
  counter3: 3
};

export default function LoginPage({ onLogin }: Props) {
  const [username, setUsername] = useState("counter1");
  const [password, setPassword] = useState("ChangeMe123!");
  const [role, setRole] = useState<UserRole>("ORDER_TAKER");
  const [positionId, setPositionId] = useState(1);
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);

  async function submit(event: FormEvent) {
    event.preventDefault();
    setBusy(true);
    setError("");

    const normalizedPosition =
      role === "ORDER_TAKER"
        ? defaultPositionByUsername[username] ?? positionId
        : undefined;

    const session: Session = {
      username,
      password,
      role,
      positionId: normalizedPosition
    };

    try {
      await api.verifyLogin(session);
      onLogin(session);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Login failed");
    } finally {
      setBusy(false);
    }
  }

  function applyPreset(nextRole: UserRole) {
    setRole(nextRole);
    if (nextRole === "ORDER_TAKER") {
      setUsername("counter1");
      setPositionId(1);
    } else if (nextRole === "KITCHEN") {
      setUsername("kitchen");
    } else {
      setUsername("admin");
    }
  }

  return (
    <div className="login-page">
      <form className="login-card" onSubmit={submit}>
        <h1>রেস্টুরেন্ট টোকেন সিস্টেম</h1>
        <p>আপনার ডিভাইস অনুযায়ী লগইন করুন</p>

        <div className="role-picker">
          <button type="button" onClick={() => applyPreset("ORDER_TAKER")}>
            কাউন্টার
          </button>
          <button type="button" onClick={() => applyPreset("KITCHEN")}>
            কিচেন
          </button>
          <button type="button" onClick={() => applyPreset("ADMIN")}>
            অ্যাডমিন
          </button>
        </div>

        <label>
          ইউজারনেম
          <input
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            autoComplete="username"
          />
        </label>

        <label>
          পাসওয়ার্ড
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            autoComplete="current-password"
          />
        </label>

        {role === "ORDER_TAKER" && (
          <label>
            কাউন্টার নম্বর
            <select
              value={positionId}
              onChange={(e) => setPositionId(Number(e.target.value))}
            >
              <option value={1}>Counter 1</option>
              <option value={2}>Counter 2</option>
              <option value={3}>Counter 3</option>
            </select>
          </label>
        )}

        {error && <div className="error-banner">{error}</div>}

        <button className="primary-button" disabled={busy}>
          {busy ? "লগইন হচ্ছে..." : "লগইন"}
        </button>
      </form>
    </div>
  );
}
