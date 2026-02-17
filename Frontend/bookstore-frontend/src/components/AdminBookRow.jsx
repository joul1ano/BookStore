import { NavLink } from "react-router-dom";

function getStockBadge(availability) {
  if (availability === 0) {
    return <span className="badge bg-danger">Out of Stock</span>;
  }

  if (availability < 20) {
    return <span className="badge bg-warning text-dark">Low Stock</span>;
  }

  return <span className="badge bg-success">In Stock</span>;
}

function AdminBookRow({ book }) {
  return (
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
        >
          <i className="bi bi-pencil"></i>
        </button>

        <button
          className="btn btn-sm btn-outline-danger"
          title="Delete book"
        >
          <i className="bi bi-trash"></i>
        </button>
      </td>
    </tr>
  );
}

export default AdminBookRow;