import { useEffect, useState } from "react";
import { getAllUsers } from "../../services/userService";

const formatDate = (isoString) => {
  if (!isoString) return "—";
  return new Date(isoString).toLocaleDateString("en-GB", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });
};

const roleBadge = (role) => {
  return role === "ADMIN"
    ? <span className="badge bg-danger">Admin</span>
    : <span className="badge bg-secondary">User</span>;
};

function UsersPage() {
  const [users, setUsers] = useState([]);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const data = await getAllUsers();
        setUsers(data);
      } catch (err) {
        console.error("Failed to fetch users", err);
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  const filtered = users.filter((u) => {
    const q = search.toLowerCase();
    return (
      u.id.toString().includes(q) ||
      u.username.toLowerCase().includes(q) ||
      u.email.toLowerCase().includes(q)
    );
  });

  if (loading) {
    return <div className="p-4 text-center">Loading users...</div>;
  }

  return (
    <div className="p-4">
      {/* Breadcrumb */}
      <div className="text-muted mb-1">
        Dashboard <span className="mx-1">›</span>
        <span className="text-success fw-semibold">Users</span>
      </div>

      {/* Title */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="fw-bold">Users</h2>
          <small className="text-muted">Total: {users.length} registered users</small>
        </div>
      </div>

      {/* Search bar */}
      <div className="d-flex gap-2 mb-4">
        <input
          className="form-control"
          placeholder="Search by ID, username, or email..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
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
              <th>ID</th>
              <th>Name</th>
              <th>Username</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Role</th>
              <th>Total Spent</th>
              <th>Created At</th>
              <th>Last Login</th>
            </tr>
          </thead>
          <tbody>
            {filtered.length === 0 ? (
              <tr>
                <td colSpan={9} className="text-center text-muted py-4">
                  No users found.
                </td>
              </tr>
            ) : (
              filtered.map((user) => (
                <tr key={user.id}>
                  <td className="text-muted">{user.id}</td>
                  <td><strong>{user.name}</strong></td>
                  <td>{user.username}</td>
                  <td>{user.email}</td>
                  <td>{user.phoneNumber || "—"}</td>
                  <td>{roleBadge(user.role)}</td>
                  <td>€{user.totalAmountSpent.toFixed(2)}</td>
                  <td>{formatDate(user.createdAt)}</td>
                  <td>{formatDate(user.lastLoginAt)}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default UsersPage;