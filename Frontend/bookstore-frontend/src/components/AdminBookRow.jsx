import { NavLink } from "react-router-dom";
import { useState } from "react";
import { deleteBook } from "../services/bookService";
import { useNavigate } from "react-router-dom";


function getStockBadge(availability) {
  if (availability === 0) {
    return <span className="badge bg-danger">Out of Stock</span>;
  }

  if (availability < 20) {
    return <span className="badge bg-warning text-dark">Low Stock</span>;
  }

  return <span className="badge bg-success">In Stock</span>;
}

function AdminBookRow({ book, onDelete }) {
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  const handleDelete = async () => {
    try {
      await deleteBook(book.id);
      onDelete(book.id);
    } catch (err) {
      console.error("Failed to delete book", err);
      alert("Failed to delete book. Please try again.");
    } finally {
      setShowModal(false);
    }
  };

  return (
    <>
      <tr>
        <td>
          <strong>{book.title}</strong>
          <div className="text-muted small">{book.genre}</div>
        </td>

        <td>{book.author}</td>

        <td className="text-muted">{book.id}</td>

        <td className="fw-semibold">
          €{book.price.toFixed(2)}
        </td>

        <td className="text-center">{getStockBadge(book.availability)}</td>

        <td className="text-end">
          <button
            className="btn btn-sm btn-outline-secondary me-2"
            title="Edit book"
            onClick={() => navigate(`/admin/products/${book.id}`)}
          >
            <i className="bi bi-pencil"></i>
          </button>

          <button
            className="btn btn-sm btn-outline-danger"
            title="Delete book"
            onClick={() => setShowModal(true)}
          >
            <i className="bi bi-trash"></i>
          </button>
        </td>
      </tr>

      {/* Confirmation Modal */}
      {showModal && (
        <>
          <div className="modal fade show d-block h-auto" tabIndex="-1" style={{ backgroundColor: "transparent" }}>
            <div className="modal-dialog modal-dialog-centered" style={{ marginTop: "0" }}>
              <div className="modal-content">

                <div className="modal-header border-0">
                  <h5 className="modal-title fw-bold">Delete Book</h5>
                  <button
                    type="button"
                    className="btn-close"
                    onClick={() => setShowModal(false)}
                  />
                </div>

                <div className="modal-body">
                  <p>
                    Are you sure you want to delete{" "}
                    <strong>"{book.title}"</strong>? This action cannot be undone.
                  </p>
                </div>

                <div className="modal-footer border-0">
                  <button
                    className="btn btn-outline-secondary"
                    onClick={() => setShowModal(false)}
                  >
                    Cancel
                  </button>
                  <button
                    className="btn btn-danger"
                    onClick={handleDelete}
                  >
                    <i className="bi bi-trash me-1"></i> Delete
                  </button>
                </div>

              </div>
            </div>
          </div>

          {/* Backdrop */}
          <div
            className="modal-backdrop fade show"
            onClick={() => setShowModal(false)}
          />
        </>
      )}
    </>
  );
}

export default AdminBookRow;