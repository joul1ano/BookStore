import { useNavigate, useLocation } from "react-router-dom";
import { useCart } from "../context/CartContext";

function Navbar() {
  const { cartItemCount } = useCart();
  const navigate = useNavigate();
  const location = useLocation();

  const isFavouritesActive = location.pathname === "/favourites";
  const isProfileActive = location.pathname === "/me";

  return (
    <nav className="navbar navbar-light bg-light px-4 d-flex justify-content-between">
      <span
        className="navbar-brand"
        style={{ cursor: "pointer" }}
        onClick={() => navigate("/books")}
      >
        My Bookstore
      </span>

      <div className="d-flex align-items-center gap-4">

        {/* Favourites */}
        <div
          style={{ cursor: "pointer" }}
          onClick={() => navigate("/favourites")}
        >
          <i
            className={`bi ${isFavouritesActive ? "bi-bookmark-fill" : "bi-bookmark"
              } fs-3`}
          />
        </div>

        {/* Cart */}
        <div
          className="position-relative"
          style={{ cursor: "pointer" }}
          onClick={() => navigate("/cart")}
        >
          <i className="bi bi-cart fs-3"></i>
          {cartItemCount > 0 && (
            <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
              {cartItemCount}
            </span>
          )}
        </div>

        {/* Profile */}
        <div
          style={{ cursor: "pointer" }}
          onClick={() => navigate("/me")}
        >
          <i
            className={`bi ${isProfileActive ? "bi-person-fill" : "bi-person"
              } fs-3`}
          />
        </div>

      </div>
    </nav>
  );
}

export default Navbar;