import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getOrderById, updateOrderStatus } from "../../services/orderService";
import { getBookById } from "../../services/bookService";

const formatDateTime = (isoString) => {
  if (!isoString) return "—";
  return new Date(isoString).toLocaleString("en-GB", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const formatPaymentMethod = (method) => {
  const map = {
    CREDIT_CARD: "Credit Card",
    BANK_TRANSFER: "Bank Transfer",
    CASH_ON_DELIVERY: "Cash on Delivery",
  };
  return map[method] || method;
};

const statusBadge = (status) => {
  const map = {
    PENDING: "warning",
    CONFIRMED: "info",
    PROCESSING: "primary",
    SHIPPED: "secondary",
    DELIVERED: "success",
  };
  const color = map[status] || "secondary";
  return (
    <span className={`badge bg-${color}`}>
      {status.charAt(0) + status.slice(1).toLowerCase()}
    </span>
  );
};

const STATUS_OPTIONS = ["PENDING", "CONFIRMED", "PROCESSING", "SHIPPED", "DELIVERED"];

function AdminOrderDetail() {
  const { orderId } = useParams();
  const navigate = useNavigate();

  const [order, setOrder] = useState(null);
  const [enrichedItems, setEnrichedItems] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState("");
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState(null);

  const fetchOrder = async () => {
    try {
      const data = await getOrderById(orderId);
      setOrder(data);
      setSelectedStatus(data.status);

      // Enrich items with book details
      if (data.items && data.items.length > 0) {
        const itemsWithBooks = await Promise.all(
          data.items.map(async (item) => {
            try {
              const book = await getBookById(item.bookId);
              return { ...item, book };
            } catch {
              return { ...item, book: null };
            }
          })
        );
        setEnrichedItems(itemsWithBooks);
      }
    } catch (err) {
      console.error("Failed to fetch order", err);
      setError("Failed to load order details.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrder();
  }, [orderId]);

  const handleStatusUpdate = async () => {
    if (selectedStatus === order.status) return;
    setUpdating(true);
    setError(null);
    try {
      await updateOrderStatus(orderId, selectedStatus);
      setSuccess("Order status updated successfully!");
      // Refresh order to show updated statusLastUpdated
      await fetchOrder();
      setTimeout(() => setSuccess(""), 3000);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to update status.");
    } finally {
      setUpdating(false);
    }
  };

  if (loading) {
    return <div className="p-4 text-center">Loading order details...</div>;
  }

  if (!order) {
    return <div className="p-4 text-center text-muted">Order not found.</div>;
  }

  return (
    <div className="p-4">
      {/* Breadcrumb */}
      <div className="text-muted mb-1">
        Dashboard <span className="mx-1">›</span>
        <span
          className="text-success fw-semibold"
          style={{ cursor: "pointer" }}
          onClick={() => navigate("/admin/orders")}
        >
          Orders
        </span>
        <span className="mx-1">›</span>
        <span className="fw-semibold">Order #{order.id}</span>
      </div>

      {/* Title row */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="fw-bold">Order #{order.id}</h2>
          <small className="text-muted">User ID: {order.userId}</small>
        </div>
        <button
          className="btn btn-outline-secondary"
          onClick={() => navigate("/admin/orders")}
        >
          ← Back to Orders
        </button>
      </div>

      {/* Alerts */}
      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      {/* Order summary card */}
      <div className="card shadow-sm mb-4">
        <div className="card-body p-4">
          <h5 className="fw-bold mb-3">Order Summary</h5>
          <div className="row g-3">
            <div className="col-sm-6">
              <small className="text-muted d-block">Date Placed</small>
              <span>{formatDateTime(order.datePlaced)}</span>
            </div>
            <div className="col-sm-6">
              <small className="text-muted d-block">Shipping Address</small>
              <span>{order.shippingAddress || "—"}</span>
            </div>
            <div className="col-sm-6">
              <small className="text-muted d-block">Payment Method</small>
              <span>{formatPaymentMethod(order.paymentMethod)}</span>
            </div>
            <div className="col-sm-6">
              <small className="text-muted d-block">Current Status</small>
              <span>{statusBadge(order.status)}</span>
            </div>
            <div className="col-sm-6">
              <small className="text-muted d-block">Status Last Updated</small>
              <span>{formatDateTime(order.statusLastUpdated)}</span>
            </div>
            <div className="col-sm-6">
              <small className="text-muted d-block">Notes</small>
              <span>{order.userNotes || "—"}</span>
            </div>
          </div>
        </div>
      </div>

      {/* Update status card */}
      <div className="card shadow-sm mb-4">
        <div className="card-body p-4">
          <h5 className="fw-bold mb-3">Update Status</h5>
          <div className="d-flex gap-3 align-items-center">
            <select
              className="form-select w-auto"
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
            >
              {STATUS_OPTIONS.map((s) => (
                <option key={s} value={s}>
                  {s.charAt(0) + s.slice(1).toLowerCase()}
                </option>
              ))}
            </select>
            <button
              className="btn btn-success"
              onClick={handleStatusUpdate}
              disabled={updating || selectedStatus === order.status}
            >
              {updating ? "Updating..." : "Save Status"}
            </button>
            {selectedStatus === order.status && (
              <small className="text-muted">Select a different status to update.</small>
            )}
          </div>
        </div>
      </div>

      {/* Items table */}
      <h5 className="fw-bold mb-3">Order Items</h5>
      <div className="card shadow-sm mb-4">
        <table className="table align-middle mb-0">
          <thead className="table-light">
            <tr>
              <th>Book</th>
              <th>Unit Price</th>
              <th>Quantity</th>
              <th>Subtotal</th>
            </tr>
          </thead>
          <tbody>
            {enrichedItems.map((item) => (
              <tr key={item.bookId}>
                <td>
                  {item.book ? (
                    <>
                      <strong>{item.book.title}</strong>
                      <br />
                      <span className="text-muted small">{item.book.author}</span>
                    </>
                  ) : (
                    <span className="text-muted">Book ID: {item.bookId}</span>
                  )}
                </td>
                <td>{item.book ? `€${item.book.price.toFixed(2)}` : "—"}</td>
                <td>{item.quantity}</td>
                <td>€{item.totalCost.toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Order total */}
      <div className="d-flex justify-content-end">
        <h5>Order Total: €{order.totalCost.toFixed(2)}</h5>
      </div>
    </div>
  );
}

export default AdminOrderDetail;