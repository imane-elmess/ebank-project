// src/components/navbar/Navbar.jsx
import React, { useState, useRef, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import "./Navbar.css"; // Style séparé

export default function Navbar() {
  const { user, role, logout } = useAuth();
  const navigate = useNavigate();
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const dropdownRef = useRef(null);

  // Fermer le dropdown si clic en dehors
  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setDropdownOpen(false);
      }
    }
    if (dropdownOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    }
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [dropdownOpen]);

  // Pas de navbar si pas connecté - APRÈS tous les hooks
  if (!user) return null;

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const getRoleLabel = (role) => {
    if (role === "AGENT_GUICHET") return "Agent de guichet";
    if (role === "CLIENT") return "Client";
    return role;
  };

  return (
    <header className="navbar">
      <div className="navbar-left">
        <Link
          to={role === "CLIENT" ? "/dashboard-client" : "/dashboard"}
          className="navbar-logo-link"
        >
          <img
            src="/logo.png"
            alt="Imane&Amal eBank Logo"
            className="logo-img"
          />
        </Link>
        <div className="branding">
          <strong>Imane&Amal eBank</strong>
          <span>Your Money, Your Control</span>
        </div>
      </div>

      <div className="navbar-right">
        <div className="dropdown" ref={dropdownRef}>
          <button
            className="dropdown-btn"
            onClick={() => setDropdownOpen(!dropdownOpen)}
            aria-expanded={dropdownOpen}
            aria-haspopup="true"
          >
            <span className="user-avatar">
              <i class="ri-user-line"></i>
            </span>
            <span className="user-info">
              <span className="user-name">{user.username}</span>
              <span className="user-role">{getRoleLabel(role)}</span>
            </span>
            <span className="dropdown-arrow">▼</span>
          </button>
          {dropdownOpen && (
            <div className="dropdown-content">
              <div className="dropdown-header">
                <span className="dropdown-username">{user.username}</span>
                <span className="dropdown-role">{getRoleLabel(role)}</span>
              </div>
              <div className="dropdown-divider"></div>
              <button
                className="dropdown-item"
                onClick={() => {
                  navigate("/change-password");
                  setDropdownOpen(false);
                }}
              >
                <i class="ri-key-2-fill"></i> Changer mot de passe
              </button>
              <button
                className="dropdown-item logout-btn"
                onClick={() => {
                  handleLogout();
                  setDropdownOpen(false);
                }}
              >
                <i className="ri-logout-circle-r-line"></i> Se déconnecter
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
}
