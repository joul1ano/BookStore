import { useEffect } from "react";
import { useFavourites } from "../../context/FavouriteContext";
import BookCard from "../../components/BookCard";

function FavouriteBooks() {
  const { favourites, fetchFavourites } = useFavourites();

  useEffect(() => {
    fetchFavourites();
  }, []);

  if (favourites.length === 0) {
    return (
      <div className="container mt-5 text-center">
        <h4>No favourite books yet ❤️</h4>
        <p className="text-muted">
          Click the bookmark icon to add favourites.
        </p>
      </div>
    );
  }

  return (
    <div className="container mt-5">
      <h2 className="mb-4 text-center">My Favourite Books</h2>

      <div className="row">
        {favourites.map(book => (
          <div className="col-md-4 mb-4" key={book.id}>
            <BookCard book={book} />
          </div>
        ))}
      </div>
    </div>
  );
}

export default FavouriteBooks;