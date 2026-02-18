import axios from "axios";

const API_URL = "http://localhost:8080";


export const addToCart = async (bookId) => {
  const token = localStorage.getItem("token"); 
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
  const token = localStorage.getItem("token"); 
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
  const token = localStorage.getItem("token"); 
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
  const token = localStorage.getItem("token"); 
  await axios.delete(
    `${API_URL}/users/me/cart/items/${bookId}`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
};