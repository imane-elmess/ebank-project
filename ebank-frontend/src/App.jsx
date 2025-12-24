import { Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/login/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import ForbiddenPage from "./pages/ForbiddenPage";
import AddClientPage from "./pages/AddClientPage";
import AddAccountPage from "./pages/AddAccountPage";
import DashboardClientPage from "./pages/dashboard-client/DashboardClientPage";
import RoleRoute from "./components/RoleRoute";
import TransferPage from "./pages/TransferPage";
import ForgotPasswordPage from "./pages/ForgotPasswordPage";
import ResetPasswordPage from "./pages/ResetPasswordPage";
import ChangePasswordPage from "./pages/ChangePasswordPage";
import ProtectedRoute from "./components/ProtectedRoute";
import { ROLES } from "./constants/roles";
import Navbar from "./components/navbar/Navbar";

function App() {
  return (
    <div className="app-wrapper">
      <Navbar />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/dashboard"
          element={
            <RoleRoute allowedRoles={[ROLES.AGENT_GUICHET]}>
              <DashboardPage />
            </RoleRoute>
          }
        />
        <Route
          path="/clients/new"
          element={
            <RoleRoute allowedRoles={[ROLES.AGENT_GUICHET]}>
              <AddClientPage />
            </RoleRoute>
          }
        />

        <Route
          path="/accounts/new"
          element={
            <RoleRoute allowedRoles={[ROLES.AGENT_GUICHET]}>
              <AddAccountPage />
            </RoleRoute>
          }
        />
        <Route
          path="/dashboard-client"
          element={
            <RoleRoute allowedRoles={[ROLES.CLIENT]}>
              <DashboardClientPage />
            </RoleRoute>
          }
        />
        <Route
          path="/transfer"
          element={
            <RoleRoute allowedRoles={[ROLES.CLIENT]}>
              <TransferPage />
            </RoleRoute>
          }
        />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
        <Route path="/reset-password" element={<ResetPasswordPage />} />
        <Route
          path="/change-password"
          element={
            <ProtectedRoute>
              <ChangePasswordPage />
            </ProtectedRoute>
          }
        />

        <Route path="/forbidden" element={<ForbiddenPage />} />

        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </div>
  );
}

export default App;
