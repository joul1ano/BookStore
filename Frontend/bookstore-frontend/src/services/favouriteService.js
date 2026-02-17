import axios from "axios";

const API_URL = "http://localhost:8080";
const token = localStorage.getItem("token");

export const getFavouriteBooks = async () => {
  const response = await axios.get(
    `${API_URL}/users/me/favourites`,
    { headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      }, }
  );
  return response.data; // expect array of books
};

export const addToFavourites = async (bookId) => {
  await axios.post(
    `${API_URL}/users/me/favourites`,
    { bookId },
    { headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      }, }
  );
};

export const removeFromFavourites = async (bookId) => {
  await axios.delete(
    `${API_URL}/users/me/favourites/${bookId}`,
    { headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      }, }
  );
};