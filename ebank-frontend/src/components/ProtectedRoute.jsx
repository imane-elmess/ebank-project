import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function ProtectedRoute({ children }) {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) return <div>Chargement...</div>;

  return isAuthenticated ? children : <Navigate to="/login" replace />;
}
