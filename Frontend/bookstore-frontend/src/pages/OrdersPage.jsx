import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getMyOrders } from "../services/orderService"; 

function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const data = await getMyOrders();
        setOrders(data);
      } catch (err) {
        console.error("Failed to fetch orders", err);
        setOrders([]);
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  // Helper to format the date nicely
  const formatDate = (isoString) => {
    if (!isoString) return "—";
    return new Date(isoString).toLocaleDateString("en-GB", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  // Helper to turn payment method enum into a readable label
  const formatPaymentMethod = (method) => {
    if (!method) return "—";
    return method
      .replace(/_/g, " ")
      .toLowerCase()
      .replace(/\b\w/g, (c) => c.toUpperCase());
  };

  // Color-coded badge for order status
  const statusBadge = (status) => {
    const map = {
      PENDING: "warning",
      PROCESSING: "info",
      SHIPPED: "primary",
      DELIVERED: "success",
      CONFIRMED: "primary",
    };
    const color = map[status] || "secondary";
    return (
      <span className={`badge bg-${color}`}>
        {status.charAt(0) + status.slice(1).toLowerCase()}
      </span>
    );
  };

  if (loading) {
    return <div className="container mt-5 text-center">Loading orders...</div>;
  }

  if (orders.length === 0) {
    return (
      <div className="container mt-5">
        <div className="row justify-content-center">
          <div className="col-md-6 col-lg-5">
            <div className="card shadow-sm text-center p-4">
              <div className="card-body">
                <i className="bi bi-box-seam fs-1 text-muted mb-3"></i>
                <h4 className="mb-3">No orders yet</h4>
                <p className="text-muted mb-4">
                  You haven't placed any orders yet.
                </p>
                <button
                  className="btn btn-primary"
                  onClick={() => navigate("/books")}
                >
                  ← Browse Books
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
      {/* Back button */}
      <button
        className="btn btn-outline-secondary mb-3"
        onClick={() => navigate(-1)}
      >
        ← Back
      </button>

      <h2 className="mb-4">My Orders</h2>

      <table className="table align-middle">
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Date Placed</th>
            <th>Shipping Address</th>
            <th>Payment Method</th>
            <th className="text-center">Total</th>
            <th className="text-center">Status</th>
            <th className="text-center">Actions</th>
          </tr>
        </thead>

        <tbody>
          {orders.map((order) => (
            <tr key={order.id}>
              <td><strong>#</strong>{order.id}</td>
              <td>{formatDate(order.datePlaced)}</td>
              <td>{order.shippingAddress || "—"}</td>
              <td>{formatPaymentMethod(order.paymentMethod)}</td>
              <td className="text-center"><strong>€{order.totalCost.toFixed(2)}</strong></td>
              <td className="text-center">{statusBadge(order.status)}</td>
              <td className="text-center">
                <i
                  className="bi bi-eye fs-5 text-primary"
                  style={{ cursor: "pointer" }}
                  title="View order details"
                  onClick={() => navigate(`/me/orders/${order.id}`)}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default OrdersPage;