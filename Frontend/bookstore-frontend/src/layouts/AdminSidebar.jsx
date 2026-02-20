import { NavLink, useNavigate } from "react-router-dom";
import { Navigate } from "react-router-dom";

function AdminSidebar() {

  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    navigate("/");
  };


  return (
    <aside
      className="bg-white border-end p-3 d-flex flex-column"
      style={{ width: "260px" }}
    >
      {/* Logo */}
      <h5 className="fw-bold mb-4 d-flex align-items-center gap-2">
        <i className="bi bi-book-fill text-success fs-4"></i>
        Admin
      </h5>

      {/* Navigation */}
      <nav className="nav flex-column gap-2">
        <NavLink
          to="/admin/products"
          className={({ isActive }) =>
            `nav-link fw-semibold d-flex align-items-center gap-2 ${isActive ? "bg-success bg-opacity-10 text-success rounded" : ""
            }`
          }
        >
          <i className="bi bi-box-seam"></i>
          Products
        </NavLink>

        <NavLink
          to="/admin/users"
          className={({ isActive }) =>
            `nav-link fw-semibold d-flex align-items-center gap-2 ${isActive ? "bg-success bg-opacity-10 text-success rounded" : ""}`
          }
        >
          <i className="bi bi-people"></i>
          Users
        </NavLink>

        <NavLink
          to="/admin/orders"
          className={({ isActive }) =>
            `nav-link fw-semibold d-flex align-items-center gap-2 ${isActive ? "bg-success bg-opacity-10 text-success rounded" : ""}`
          }
        >
          <i className="bi bi-cart-check"></i>
          Orders
        </NavLink>

        <span className="nav-link text-muted d-flex align-items-center gap-2">
          <i className="bi bi-building"></i>
          Publishers
        </span>
      </nav>

      {/* Logout */}
      <div className="mt-auto pt-4">
        <button className="btn btn-outline-danger w-100 d-flex align-items-center justify-content-center gap-2"
          onClick={handleLogout}>
          <i className="bi bi-box-arrow-right"></i>
          Logout
        </button>
      </div>
    </aside>
  );
}

export default AdminSidebar;