import { useState } from "react";
import { Link } from "react-router-dom";

function BookCard({ book }) {
  return (
    <div className="card h-100 shadow-sm">
      <div className="card-body d-flex flex-column">
        <h5 className="card-title">{book.title}</h5>

        <h6 className="card-subtitle mb-2 text-muted">
          by {book.author}
        </h6>

        <p className="fw-bold mt-auto mb-3">
          €{book.price}
        </p>

        <div className="d-flex gap-2">
          <Link
            to={`/books/${book.id}`}
            className="btn btn-outline-primary w-100"
          >
            View Details
          </Link>

          <button
            className="btn btn-primary w-100"
            disabled
          >
            Add to Cart
          </button>
        </div>
      </div>
    </div>
  );
}

export default BookCard;