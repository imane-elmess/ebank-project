import { useState } from "react";

function LoginForm({
  email,
  password,
  infoMessage,
  onEmailChange,
  onPasswordChange,
  onSubmit,
}) {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className="login-card dark-card">
      <img src="/logo.png" alt="Logo" className="logo-login" />
      <h2>Connexion eBank</h2>
      <p className="subtitle">Accédez à votre espace sécurisé</p>

      <form onSubmit={onSubmit}>
        <div className="form-group">
          <label>Email</label>
          <input
            type="email"
            placeholder="agent@ebank.com"
            value={email}
            onChange={(e) => onEmailChange(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label>Mot de passe</label>
          <div className="password-field">
            <input
              type={showPassword ? "text" : "password"}
              placeholder="Votre mot de passe"
              value={password}
              onChange={(e) => onPasswordChange(e.target.value)}
              required
            />
            <button
              type="button"
              className="toggle-visibility"
              aria-label={
                showPassword ? "Masquer le mot de passe" : "Afficher le mot de passe"
              }
              aria-pressed={showPassword}
              onClick={() => setShowPassword((prev) => !prev)}
            >
              <i
                className={showPassword ? "ri-eye-off-line" : "ri-eye-line"}
                aria-hidden="true"
              ></i>
            </button>
          </div>
        </div>

        <button type="submit" className="btn-login green-btn">
          Se connecter
        </button>
        <p className="forgot-link">
          <a href="/forgot-password">Mot de passe oublié ?</a>
        </p>
      </form>

      {infoMessage && (
        <div
          className={
            infoMessage.startsWith("✅") ? "info-message" : "error-message"
          }
        >
          {infoMessage}
        </div>
      )}
    </div>
  );
}

export default LoginForm;
