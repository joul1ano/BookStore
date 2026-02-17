import { useNavigate, useLocation } from "react-router-dom";
import { useCart } from "../context/CartContext";

function Navbar() {
  const { cartItemCount } = useCart();
  const navigate = useNavigate();
  const location = useLocation();

  const isFavouritesActive = location.pathname === "/favourites";

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
            className={`bi ${
              isFavouritesActive ? "bi-bookmark-fill" : "bi-bookmark"
            } fs-3`}
          ></i>
        </div>

        {/* Cart */}
        <div
          className="position-relative"
          style={{ cursor: "pointer" }}
          onClick={() => navigate("/cart")}
        >
          <i className="bi bi-cart fs-2"></i>

          {cartItemCount > 0 && (
            <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
              {cartItemCount}
            </span>
          )}
        </div>

      </div>
    </nav>
  );
}

export default Navbar;