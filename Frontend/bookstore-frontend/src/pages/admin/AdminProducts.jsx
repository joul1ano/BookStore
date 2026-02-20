import { useEffect, useState } from "react";
import { getAllBooks } from "../../services/bookService";
import AdminBookRow from "../../components/AdminBookRow";
import { useNavigate } from "react-router-dom";

function AdminProducts() {
  const [books, setBooks] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    getAllBooks().then(setBooks);
  }, []);

  const handleBookDeleted = (deletedId) => {
    setBooks(prev => prev.filter(book => book.id !== deletedId));
  };

  return (
    <div className="p-4">
      {/* Breadcrumb */}
      <div className="text-muted mb-1">
        Dashboard <span className="mx-1">›</span>
        <span className="text-success fw-semibold">Products</span>
      </div>

      {/* Title */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="fw-bold">Products</h2>
          <small className="text-muted">
            Total: {books.length} active listings
          </small>
        </div>

        <button className="btn btn-success"
          onClick={() => navigate("/admin/products/new")}>
          <i className="bi bi-plus-circle"></i> Add New Book
        </button>
      </div>

      {/* Search bar */}
      <div className="d-flex gap-2 mb-4">
        <input
          className="form-control"
          placeholder="Search by title, author, or ID..."
        />
        <button className="btn btn-outline-secondary">
          <i className="bi bi-search"></i>
        </button>
        <button className="btn btn-outline-secondary">
          <i className="bi bi-funnel"></i>
        </button>
      </div>

      {/* Table */}
      <div className="card shadow-sm">
        <table className="table align-middle mb-0">
          <thead className="table-light">
            <tr>
              <th>Book Details</th>
              <th>Author</th>
              <th>ID</th>
              <th>Price</th>
              <th className="text-center">Stock Status</th>
              <th className="text-end">Actions</th>
            </tr>
          </thead>

          <tbody>
            {books.map(book => (
              <AdminBookRow key={book.id} book={book} onDelete={handleBookDeleted} />
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default AdminProducts;