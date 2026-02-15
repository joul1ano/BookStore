import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";

function Navbar() {
  const { cartItemCount } = useCart();
  const navigate = useNavigate();

  return (
    <nav className="navbar navbar-light bg-light px-4">
      <span className="navbar-brand">My Bookstore</span>

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
    </nav>
  );
}

export default Navbar;