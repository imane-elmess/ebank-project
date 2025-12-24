import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function RoleRoute({ allowedRoles, children }) {
  const { role, isAuthenticated, isLoading } = useAuth();

  if (isLoading) return <div>Chargement...</div>;

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (!allowedRoles.includes(role)) {
    return <Navigate to="/forbidden" replace />;
  }

  return children;
}

export default RoleRoute;
