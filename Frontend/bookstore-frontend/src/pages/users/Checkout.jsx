import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { placeOrder } from "../../services/orderService";
import { useCart } from "../../context/CartContext";

function Checkout() {
  const navigate = useNavigate();
  const { fetchCartCount } = useCart();

  const [form, setForm] = useState({
    shippingAddress: "",
    paymentMethod: "",
    userNotes: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!form.shippingAddress.trim() || !form.paymentMethod) {
      setError("Shipping address and payment method are required.");
      return;
    }

    try {
      setLoading(true);
      await placeOrder(form);
      navigate("/books"); // later → confirmation page
      await fetchCartCount();
    } catch (err) {
        console.log(err)
      setError("Failed to place order. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "600px" }}>
      <button
        className="btn btn-outline-secondary mb-4"
        onClick={() => navigate("/cart")}
      >
        ← Back
      </button>

      <h2 className="mb-4">Checkout</h2>

      {error && <div className="alert alert-danger">{error}</div>}

      <form onSubmit={handleSubmit} className="card shadow p-4">
        <div className="mb-3">
          <label className="form-label">Shipping Address *</label>
          <textarea
            name="shippingAddress"
            className="form-control"
            rows="3"
            value={form.shippingAddress}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Payment Method *</label>
          <select
            name="paymentMethod"
            className="form-select"
            value={form.paymentMethod}
            onChange={handleChange}
            required
          >
            <option value="">Select payment method</option>
            <option value="CREDIT_CARD">Credit Card</option>
            <option value="BANK_TRANSFER">Bank Transfer</option>
            <option value="CASH_ON_DELIVERY">Cash on Delivery</option>
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Notes (optional)</label>
          <textarea
            name="userNotes"
            className="form-control"
            rows="2"
            value={form.userNotes}
            onChange={handleChange}
          />
        </div>

        <div className="d-flex justify-content-end">
          <button
            type="submit"
            className="btn btn-success px-4"
            disabled={loading}
          >
            {loading ? "Placing Order..." : "Place Order"}
          </button>
        </div>
      </form>
    </div>
  );
}

export default Checkout;