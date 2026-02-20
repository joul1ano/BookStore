import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getMyOrderById } from "../../services/orderService";   
import { getBookById } from "../../services/bookService";      

function OrderDetailsPage() {
  const { orderId } = useParams();
  const navigate = useNavigate();
  const [order, setOrder] = useState(null);
  const [enrichedItems, setEnrichedItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchOrderDetails = async () => {
      try {
        // 1. Fetch the order
        const orderData = await getMyOrderById(orderId);
        setOrder(orderData);

        // 2. For each item, fetch the corresponding book details
        const itemsWithBooks = await Promise.all(
          orderData.items.map(async (item) => {
            try {
              const book = await getBookById(item.bookId);
              return { ...item, book };
            } catch {
              // If book fetch fails, fall back gracefully
              return { ...item, book: null };
            }
          })
        );
        setEnrichedItems(itemsWithBooks);
      } catch (err) {
        console.error("Failed to fetch order details", err);
      } finally {
        setLoading(false);
      }
    };

    fetchOrderDetails();
  }, [orderId]);

  const formatDate = (isoString) => {
    if (!isoString) return "—";
    return new Date(isoString).toLocaleDateString("en-GB", {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const formatPaymentMethod = (method) => {
    if (!method) return "—";
    return method
      .replace(/_/g, " ")
      .toLowerCase()
      .replace(/\b\w/g, (c) => c.toUpperCase());
  };

  const statusBadge = (status) => {
    const map = {
      PENDING: "warning",
      PROCESSING: "info",
      SHIPPED: "primary",
      DELIVERED: "success",
      CANCELLED: "danger",
    };
    const color = map[status] || "secondary";
    return (
      <span className={`badge bg-${color}`}>
        {status.charAt(0) + status.slice(1).toLowerCase()}
      </span>
    );
  };

  if (loading) {
    return (
      <div className="container mt-5 text-center">Loading order details...</div>
    );
  }

  if (!order) {
    return (
      <div className="container mt-5 text-center text-muted">
        Order not found.
      </div>
    );
  }

  return (
    <div className="container mt-5" style={{ maxWidth: "860px" }}>
      {/* Back button */}
      <button
        className="btn btn-outline-secondary mb-3"
        onClick={() => navigate("/me/orders")}
      >
        ← Back
      </button>

      <h2 className="mb-4">Order #{order.orderId}</h2>

      {/* Order summary card */}
      <div className="card shadow-sm mb-4">
        <div className="card-body">
          <h5 className="card-title mb-3">Order Summary</h5>
          <div className="row g-3">

            <div className="col-sm-6">
              <small className="text-muted d-block"><strong>Date Placed</strong></small>
              <span>{formatDate(order.datePlaced)}</span>
            </div>

            <div className="col-sm-6">
              <small className="text-muted d-block"><strong>Shipping Address</strong></small>
              <span>{order.shippingAddress || "—"}</span>
            </div>

            <div className="col-sm-6">
              <small className="text-muted d-block"><strong>Payment Method</strong></small>
              <span>{formatPaymentMethod(order.paymentMethod)}</span>
            </div>

            <div className="col-sm-6">
              <small className="text-muted d-block"><strong>Status</strong></small>
              <span>{statusBadge(order.status)}</span>
            </div>

            <div className="col-sm-6">
              <small className="text-muted d-block"><strong>Status Last Updated</strong></small>
              <span>{formatDate(order.statusLastUpdated)}</span>
            </div>

            <div className="col-sm-6">
              <small className="text-muted d-block"><strong>Notes</strong></small>
              <span>{order.userNotes || "—"}</span>
            </div>

          </div>
        </div>
      </div>

      {/* Items table */}
      <h5 className="mb-3">Items</h5>
      <table className="table align-middle">
        <thead>
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
                    <span className="text-muted">{item.book.author}</span>
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

      {/* Order total */}
      <div className="d-flex justify-content-end mt-3">
        <h5>Order Total: €{order.totalCost.toFixed(2)}</h5>
      </div>
    </div>
  );
}

export default OrderDetailsPage;