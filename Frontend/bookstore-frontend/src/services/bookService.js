import axios from "axios";

const API_URL = "http://localhost:8080";

export const createBook = async (bookData) => {
  const token = localStorage.getItem("token");

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


export const getAllBooks = async (page = 0, size = 5, genre = null) => {
  const token = localStorage.getItem("token");
  const params = new URLSearchParams({ page, size });
  if (genre) params.append("genre", genre);

  const response = await axios.get(`${API_URL}/books?${params}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};

export const getBookById = async (id) => {
  const token = localStorage.getItem("token");
  try {
    const response = await axios.get(`${API_URL}/books/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    return response.data;

  } catch (error) {
    console.error('Error fetching books:', error);
    return null;

  }

}

export const updateBook = async (id, bookData) => {
  const token = localStorage.getItem("token");
  const response = await axios.put(`${API_URL}/books/${id}`, bookData, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};

export const deleteBook = async (id) => {
  const token = localStorage.getItem("token");
  await axios.delete(`${API_URL}/books/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
};