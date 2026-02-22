import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAllOrders } from "../../services/orderService";

const PAGE_SIZE = 10;

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

function AdminOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [pendingCount, setPendingCount] = useState(0);
  const [shippedCount, setShippedCount] = useState(0);
  const [deliveredCount, setDeliveredCount] = useState(0);
  const [orderIdSearch, setOrderIdSearch] = useState("");
  const [userSearch, setUserSearch] = useState("");
  const navigate = useNavigate();

  const fetchOrders = async (page = 0) => {
    setLoading(true);
    try {
      const data = await getAllOrders(page, PAGE_SIZE);
      setOrders(data.content);
      setCurrentPage(data.page);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      console.error("Failed to fetch orders", err);
    } finally {
      setLoading(false);
    }
  };

  // Fetch status counts separately using the status filter
  // We use size=1 since we only need totalElements, not the actual content
  const fetchStatusCounts = async () => {
    try {
      const [pending, shipped, delivered] = await Promise.all([
        getAllOrders(0, 1, null, "PENDING"),
        getAllOrders(0, 1, null, "SHIPPED"),
        getAllOrders(0, 1, null, "DELIVERED"),
      ]);
      setPendingCount(pending.totalElements);
      setShippedCount(shipped.totalElements);
      setDeliveredCount(delivered.totalElements);
    } catch (err) {
      console.error("Failed to fetch status counts", err);
    }
  };

  useEffect(() => {
    fetchOrders();
    fetchStatusCounts();
  }, []);

  // Frontend filter for order ID and user ID search on current page
  const filtered = orders.filter((o) => {
    const matchesOrderId = orderIdSearch === "" ||
      o.id.toString().includes(orderIdSearch.trim());
    const matchesUser = userSearch === "" ||
      o.userId.toString().includes(userSearch.trim());
    return matchesOrderId && matchesUser;
  });

  if (loading) {
    return <div className="p-4 text-center">Loading orders...</div>;
  }

  return (
    <div className="p-4">
      {/* Breadcrumb */}
      <div className="text-muted mb-1">
        Dashboard <span className="mx-1">›</span>
        <span className="text-success fw-semibold">Orders</span>
      </div>

      {/* Title */}
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h2 className="fw-bold">Orders</h2>
          <small className="text-muted">Total: {totalElements} orders</small>
        </div>
      </div>

      {/* Status summary */}
      <div className="d-flex gap-3 mb-4">
        <span className="badge bg-warning text-dark fs-6 px-3 py-2">
          Pending: {pendingCount}
        </span>
        <span className="badge bg-secondary fs-6 px-3 py-2">
          Shipped: {shippedCount}
        </span>
        <span className="badge bg-success fs-6 px-3 py-2">
          Delivered: {deliveredCount}
        </span>
      </div>

      {/* Search bars */}
      <div className="d-flex gap-2 mb-4">
        <input
          className="form-control"
          placeholder="Search by order ID..."
          value={orderIdSearch}
          onChange={(e) => setOrderIdSearch(e.target.value)}
        />
        <input
          className="form-control"
          placeholder="Search by user ID..."
          value={userSearch}
          onChange={(e) => setUserSearch(e.target.value)}
        />
        <button className="btn btn-outline-secondary">
          <i className="bi bi-search"></i>
        </button>
      </div>

      {/* Table */}
      <div className="card shadow-sm">
        <table className="table align-middle mb-0">
          <thead className="table-light">
            <tr>
              <th>Order ID</th>
              <th>User ID</th>
              <th>Date Placed</th>
              <th>Address</th>
              <th>Total</th>
              <th>Payment Method</th>
              <th>Status</th>
              <th className="text-end">Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.length === 0 ? (
              <tr>
                <td colSpan={8} className="text-center text-muted py-4">
                  No orders found.
                </td>
              </tr>
            ) : (
              filtered.map((order) => (
                <tr key={order.id}>
                  <td className="text-muted">#{order.id}</td>
                  <td>{order.userId}</td>
                  <td>{formatDateTime(order.datePlaced)}</td>
                  <td>{order.shippingAddress || "—"}</td>
                  <td className="fw-semibold">€{order.totalCost.toFixed(2)}</td>
                  <td>{formatPaymentMethod(order.paymentMethod)}</td>
                  <td>{statusBadge(order.status)}</td>
                  <td className="text-end">
                    <button
                      className="btn btn-sm btn-outline-secondary"
                      title="View & update order"
                      onClick={() => navigate(`/admin/orders/${order.id}`)}
                    >
                      <i className="bi bi-pencil-square"></i>
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>

        {/* Pagination */}
        <div className="d-flex justify-content-between align-items-center px-3 py-2 border-top">
          <small className="text-muted">
            Showing {orders.length} of {totalElements} orders
          </small>
          <nav>
            <ul className="pagination pagination-sm mb-0">
              <li className={`page-item ${currentPage === 0 ? "disabled" : ""}`}>
                <button
                  className="page-link"
                  onClick={() => fetchOrders(currentPage - 1)}
                >
                  ← Prev
                </button>
              </li>

              {[...Array(totalPages)].map((_, i) => (
                <li key={i} className={`page-item ${currentPage === i ? "active" : ""}`}>
                  <button
                    className="page-link"
                    onClick={() => fetchOrders(i)}
                  >
                    {i + 1}
                  </button>
                </li>
              ))}

              <li className={`page-item ${currentPage === totalPages - 1 || totalPages === 0 ? "disabled" : ""}`}>
                <button
                  className="page-link"
                  onClick={() => fetchOrders(currentPage + 1)}
                >
                  Next →
                </button>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  );
}

export default AdminOrders;