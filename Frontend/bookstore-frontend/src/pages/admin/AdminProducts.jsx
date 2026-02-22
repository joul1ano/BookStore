import { useEffect, useState } from "react";
import { getAllBooks } from "../../services/bookService";
import AdminBookRow from "../../components/AdminBookRow";
import { useNavigate } from "react-router-dom";

function AdminProducts() {
  const [books, setBooks] = useState([]);
  const navigate = useNavigate();
  const [totalElements, setTotalElements] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const PAGE_SIZE = 5;

  const fetchBooks = async (page = 0) => {
    try {
      const data = await getAllBooks(page, PAGE_SIZE);
      setBooks(data.content);
      setTotalElements(data.totalElements);
      setCurrentPage(data.page);
      setTotalPages(data.totalPages);
    } catch (err) {
      console.error("Failed to fetch books", err);
    }
  };

  useEffect(() => {
    fetchBooks();
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
            Total: {totalElements} active listings
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

      {/* Pagination */}
      <div className="d-flex justify-content-between align-items-center mt-3">
        <small className="text-muted">
          Showing {books.length} of {totalElements} books
        </small>
        <nav>
          <ul className="pagination pagination-sm mb-0">
            <li className={`page-item ${currentPage === 0 ? "disabled" : ""}`}>
              <button
                className="page-link"
                onClick={() => fetchBooks(currentPage - 1)}
              >
                ← Prev
              </button>
            </li>

            {[...Array(totalPages)].map((_, i) => (
              <li key={i} className={`page-item ${currentPage === i ? "active" : ""}`}>
                <button
                  className="page-link"
                  onClick={() => fetchBooks(i)}
                >
                  {i + 1}
                </button>
              </li>
            ))}

            <li className={`page-item ${currentPage === totalPages - 1 ? "disabled" : ""}`}>
              <button
                className="page-link"
                onClick={() => fetchBooks(currentPage + 1)}
              >
                Next →
              </button>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  );
}

export default AdminProducts;