import axios from "axios";

const API_URL = "http://localhost:8080";
const token = localStorage.getItem("token"); //Mporei na vgalei provlima, ksanvalto sta functions an nai

export const addToCart = async (bookId) => {
  const quantity = 1;

  if (!token) {
    throw new Error("User not authenticated");
  }

  const response = await axios.post(
    `${API_URL}/users/me/cart/items`,
    {
      bookId,
      quantity,
    },
    {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    }
  );

  return response.data;
};

export const getCartSummary = async () => {
  if (!token) {
    throw new Error("User not authenticated");
  }

  const response = await axios.get(
    `${API_URL}/users/me/cart`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );

  return response.data; // { userId, itemCount, totalCost, lastUpdatedAt }
};

export const getCartItems = async () => {
  const response = await axios.get(
    `${API_URL}/users/me/cart/items`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
};

export const removeCartItem = async (bookId) => {
  await axios.delete(
    `${API_URL}/users/me/cart/items/${bookId}`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
};