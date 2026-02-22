import { useEffect, useState } from "react";
import BookCard from "../../components/BookCard.jsx";
import { getAllBooks } from "../../services/bookService";

const PAGE_SIZE = 9; 

function BooksPage() {
  const [books, setBooks] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const fetchBooks = async (page = 0) => {
    setLoading(true);
    try {
      const data = await getAllBooks(page, PAGE_SIZE);
      setBooks(data.content);
      setCurrentPage(data.page);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      setError("Failed to load books");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
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
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="text-center">Available Books</h2>
        <small className="text-muted">{totalElements} books available</small>
      </div>

      {/* Book grid */}
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

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="d-flex justify-content-center mt-4">
          <nav>
            <ul className="pagination">
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
      )}
    </div>
  );
}

export default BooksPage;