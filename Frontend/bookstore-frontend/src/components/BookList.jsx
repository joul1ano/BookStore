import { useEffect, useState } from "react";
import axios from "axios";

function BookList() {
  const [books, setBooks] = useState([]);

  const token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkaW1pdHJpcyIsImlhdCI6MTc3MDU3NDQ1MywiZXhwIjoxNzcwNjYwODUzfQ.-ekvqUsXc6Ye4bMteFk7HIRc89iBy50aRKi4gnFUdj4";

  useEffect(() => {
    axios.get("http://localhost:8080/books", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    .then(response => {
      setBooks(response.data);
    })
    .catch(error => {
      console.error("Error fetching books:", error);
    });
  }, []);

  return (
    <div>
      <h2>Available Books</h2>
      <ul>
        {books.map(book => (
          <li key={book.id}>
            <strong>{book.title}</strong> — {book.author}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default BookList;