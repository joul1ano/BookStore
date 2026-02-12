import axios from "axios";

const API_URL = "http://localhost:8080";

export const createBook = async (bookData) => {
  //const token = localStorage.getItem("token");
 //TODO ACCEPT TOKEN DYNAMICALLY
  const token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc3MDg1MTM3MCwiZXhwIjoxNzcwOTM3NzcwfQ.B1FYIRdQtw0J1-9beFmLGhbofxCO26RyhPNo0A-DzJ4";

  const response = await axios.post(
    `${API_URL}/books`,
    bookData,
    {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    }
  );

  return response.data;
};