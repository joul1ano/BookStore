
import { useEffect, useState } from "react";
import BookCard from "../components/BookCard.jsx";
import { getAllBooks } from "../services/bookService";

function BooksPage() {
  const [books, setBooks] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);


  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const response = await getAllBooks();
        setBooks(response);
      } catch (err) {
        setError("Failed to load books");
      } finally {
        setLoading(false);
      }
    };

    fetchBooks();
  }, []);

  if (loading) {
    return (
      <div className="container mt-5 text-center">
        <p>Loading books...</p>
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

  return (
    <div className="container mt-5">
      <h2 className="mb-4 text-center">Available Books</h2>

      <div className="row">
        {books.length === 0 ? (
          <p className="text-center">No books available.</p>
        ) : (
          books.map(book => (
            <div className="col-md-4 mb-4" key={book.id}>
              <BookCard book={book} />
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default BooksPage;