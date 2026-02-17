import axios from "axios";

const API_URL = "http://localhost:8080";
const token = localStorage.getItem("token");

export const placeOrder = async (orderData) => {
  const response = await axios.post(
    `${API_URL}/users/me/orders`,
    orderData,
    { headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
     }
  );
  return response.data;
};