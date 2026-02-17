import { createContext, useContext, useEffect, useState } from "react";
import {
  getFavouriteBooks,
  addToFavourites,
  removeFromFavourites,
} from "../services/favouriteService";

const FavouriteContext = createContext();

export function FavouriteProvider({ children }) {
  const [favourites, setFavourites] = useState([]);

  const fetchFavourites = async () => {
    if (!localStorage.getItem("token")) {
      setFavourites([]);
      return;
    }

    try {
      const data = await getFavouriteBooks();
      setFavourites(data);
    } catch {
      setFavourites([]);
    }
  };

  useEffect(() => {
    fetchFavourites();
  }, []);

  const isFavourite = (bookId) =>
    favourites.some(book => book.id === bookId);

  const toggleFavourite = async (bookId) => {
    if (isFavourite(bookId)) {
      await removeFromFavourites(bookId);
      setFavourites(prev => prev.filter(b => b.id !== bookId));
    } else {
      await addToFavourites(bookId);
      setFavourites(prev => [...prev, { id: bookId }]);
      await fetchFavourites();
    }
  };

  return (
    <FavouriteContext.Provider
      value={{ favourites, fetchFavourites, isFavourite, toggleFavourite }}
    >
      {children}
    </FavouriteContext.Provider>
  );
}

export const useFavourites = () => useContext(FavouriteContext);