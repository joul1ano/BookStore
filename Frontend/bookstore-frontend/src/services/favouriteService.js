import axios from "axios";

const API_URL = "http://localhost:8080";


export const getFavouriteBooks = async () => {
  const token = localStorage.getItem("token");
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
  const token = localStorage.getItem("token");
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
  const token = localStorage.getItem("token");
  await axios.delete(
    `${API_URL}/users/me/favourites/${bookId}`,
    { headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      }, }
  );
};