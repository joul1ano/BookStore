import { useEffect, useState } from "react";
import { getMe } from "../services/userService";
import { useNavigate, useLocation } from "react-router-dom";

function ProfilePage() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const data = await getMe();
        setUser(data);
      } catch (err) {
        console.error("Failed to load profile", err);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    navigate("/");
  };

  if (loading) {
    return (
      <div className="container mt-5 text-center">
        Loading profile...
      </div>
    );
  }

  if (!user) return null;

  return (
    <div className="container mt-5 d-flex justify-content-center">
      <div className="card shadow-lg p-4" style={{ maxWidth: "500px", width: "100%" }}>

        {/* Back */}
        <button
          className="btn btn-outline-secondary mb-3 align-self-start"
          onClick={() => navigate(location.state?.from || "/books")}
        >
          ← Back
        </button>

        {/* Icon */}
        <div className="text-center mb-3">
          <i className="bi bi-person-circle fs-1 text-primary"></i>
        </div>

        {/* User info */}
        <h4 className="text-center mb-4">{user.name}</h4>

        <ul className="list-group list-group-flush mb-4">
          <li className="list-group-item">
            <strong>Username:</strong> {user.username}
          </li>
          <li className="list-group-item">
            <strong>Email:</strong> {user.email}
          </li>
          <li className="list-group-item">
            <strong>Phone:</strong> {user.phoneNumber || "—"}
          </li>
        </ul>

        {/* Actions */}
        <div className="d-grid gap-2">
          <button
            className="btn btn-primary"
            onClick={() => navigate("/me/orders")}
          >
            <i className="bi bi-box"></i> My Orders
          </button>

          <button
            className="btn btn-outline-danger"
            onClick={handleLogout}
          >
            <i className="bi bi-box-arrow-right"></i> Logout
          </button>
        </div>

      </div>
    </div>
  );
}

export default ProfilePage;