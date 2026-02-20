import { useEffect, useState } from "react";
import { getCartItems } from "../../services/cartService";
import { useCart } from "../../context/CartContext";
import { useNavigate } from "react-router-dom";

function PreviewCart() {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const { removeCartItem, fetchCartCount } = useCart();
  const navigate = useNavigate();

  const fetchCart = async () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      const data = await getCartItems();
      setCart(data);
    } catch (err) {
      console.error("Failed to fetch cart", err);
      setCart(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCart();
  }, []);

  const handleDelete = async (bookId) => {
    await removeCartItem(bookId);
    await fetchCart();
    await fetchCartCount();
  };

  if (loading) {
    return <div className="container mt-5 text-center">Loading cart...</div>;
  }

  if (!cart || cart.items.length === 0) {
    return (
      <div className="container mt-5">
        <div className="row justify-content-center">
          <div className="col-md-6 col-lg-5">
            <div className="card shadow-sm text-center p-4">
              <div className="card-body">
                <i className="bi bi-cart-x fs-1 text-muted mb-3"></i>

                <h4 className="mb-3">Your cart is empty</h4>
                <p className="text-muted mb-4">
                  Looks like you haven’t added any books yet.
                </p>

                <button
                  className="btn btn-primary"
                  onClick={() => navigate("/books")}
                >
                  ← Continue Shopping
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container mt-5">
      <button
        className="btn btn-outline-secondary mb-3"
        onClick={() => navigate("/books")}
      >
        ← Back
      </button>
      <h2 className="mb-4">My Cart</h2>

      <table className="table align-middle">
        <thead>
          <tr>
            <th>Book</th>
            <th>Unit Price</th>
            <th>Quantity</th>
            <th>Delete</th>
            <th>Total</th>
          </tr>
        </thead>

        <tbody>
          {cart.items.map(({ book, quantity }) => (
            <tr key={book.id}>
              <td>
                <strong>{book.title}</strong><br />
                {book.author}<br />
                ID: {book.id}
              </td>

              <td>€{book.price}</td>
              <td>{quantity}</td>

              <td>
                <i
                  className="bi bi-trash text-danger fs-5"
                  style={{ cursor: "pointer" }}
                  onClick={() => handleDelete(book.id)}
                />
              </td>

              <td>€{(book.price * quantity).toFixed(2)}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="d-flex justify-content-between align-items-center mt-4">
        <h4>Total: €{cart.totalCost.toFixed(2)}</h4>

        <button
          className="btn btn-info px-4"
          onClick={() => navigate("/me/cart/checkout")}>
          Next Step →
        </button>
      </div>
    </div>
  );
}

export default PreviewCart;