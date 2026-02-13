import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getBookById } from "../services/bookService";

function BookDetails() {
  const { id } = useParams();
  const [book, setBook] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchBook = async () => {
      try {
        const response = await getBookById(id);
        setBook(response);
      } catch (err) {
        setError("Failed to load book details");
      } finally {
        setLoading(false);
      }
    };

    fetchBook();
  }, [id]);

  if (loading) {
    return (
      <div className="container mt-5 text-center">
        <p>Loading book details...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container mt-5">
        <div className="alert alert-danger">{error}</div>
      </div>
    );
  }

  if (!book) return null;

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <div className="card shadow">
            <div className="card-body">
              <h3 className="card-title">{book.title}</h3>
              <h5 className="text-muted mb-3">by {book.author}</h5>

              <p className="mb-4">{book.description}</p>

              <ul className="list-group list-group-flush mb-4">
                <li className="list-group-item">
                  <strong>Id:</strong> {book.id}
                </li>
                <li className="list-group-item">
                  <strong>Genre:</strong> {book.genre}
                </li>
                <li className="list-group-item">
                  <strong>Pages:</strong> {book.numberOfPages}
                </li>
                <li className="list-group-item">
                  <strong>Price:</strong> €{book.price}
                </li>
              </ul>

              <div className="d-flex gap-2">
                <Link to="/books" className="btn btn-outline-secondary w-50">
                  Back
                </Link>

                <button className="btn btn-primary w-50" disabled>
                  Add to Cart
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default BookDetails;