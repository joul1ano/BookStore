import axios from "axios";

const API_URL = "http://localhost:8080";


export const placeOrder = async (orderData) => {
  const token = localStorage.getItem("token");
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

export const getMyOrders = async () => {
  const token = localStorage.getItem("token");
  const response = await axios.get(
    `${API_URL}/users/me/orders`,
    { headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
     }
  )
  
  return response.data;
};

export const getMyOrderById = async (orderId) => {
  const token = localStorage.getItem("token");
  const response = await axios.get(
    `${API_URL}/users/me/orders/${orderId}`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
};

export const getOrderById = async (orderId) => {
  const token = localStorage.getItem("token");
  const response = await axios.get(
    `${API_URL}/orders/${orderId}`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
};

export const getAllOrders = async () => {
  const token = localStorage.getItem("token");
  const response = await axios.get(`${API_URL}/orders`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};

export const updateOrderStatus = async (id, status) => {
  const token = localStorage.getItem("token");
  const response = await axios.put(`${API_URL}/orders/${id}`, { status }, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};