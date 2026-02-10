import axios from "axios";

const API_URL = "http://localhost:8080/auth"; // change if needed

export const registerUser = async (userData) => {
  const response = await axios.post(
    `${API_URL}/register`,
    userData,
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  return response.data;
};