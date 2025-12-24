import { Link } from "react-router-dom";

function ForbiddenPage() {
  return (
    <div className="app-container">
      <div className="login-card">
        <div style={{ textAlign: "center", marginBottom: "1.5rem" }}>
          <div style={{ fontSize: "3.5rem", marginBottom: "0.75rem" }}>🚫</div>
        </div>
        <h2>Accès refusé</h2>
        <p className="subtitle">
          Vous n’avez pas le droit d’accéder à cette fonctionnalité. Veuillez
          contacter votre administrateur.
        </p>

        <Link
          to="/login"
          className="btn-login"
          style={{
            display: "block",
            textAlign: "center",
            textDecoration: "none",
            marginTop: "1.25rem",
          }}
        >
          Retour à la connexion
        </Link>
      </div>
    </div>
  );
}

export default ForbiddenPage;
