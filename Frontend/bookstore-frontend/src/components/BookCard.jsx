import { useState } from "react";
import { Link } from "react-router-dom";
import { useCart } from "../context/CartContext.jsx";

function BookCard({ book }) {
  const { addToCart } = useCart();
  const [loading, setLoading] = useState(false);
  const [added, setAdded] = useState(false);

  const handleAddToCart = async () => {
    try {
      setLoading(true);
      await addToCart(book.id);
      setAdded(true);
      setTimeout(() => {
      setAdded(false);
    }, 1000);
    } catch (error) {
      alert("Failed to add to cart");
      console.log(error)
    } finally {
      setLoading(false);
    }
  };
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
            className={`btn ${added ? "btn-success" : "btn-primary"} w-100`}
            onClick={handleAddToCart}
            disabled={loading}
          >
            {added ? "Added ✓" : loading ? "Adding..." : "Add to Cart"}
          </button>
        </div>
      </div>
    </div>
  );
}

export default BookCard;