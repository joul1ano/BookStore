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


export const getAllBooks = async () => {
  const API_URL = "http://localhost:8080";
  const token = localStorage.getItem("token");
  try {
    const response = await axios.get(`${API_URL}/books`, {
      headers: {
        Authorization: `Bearer ${token}`, 
      },
    });

    return response.data; 
  } catch (error) {
    console.error('Error fetching books:', error);
    return []; // return empty array on error
  }
};

export const getBookById = async (id) => {
  const token = localStorage.getItem("token");
  try {
    const response = await axios.get(`${API_URL}/books/${id}`,{
      headers:{
        Authorization: `Bearer ${token}`,
      },
    });
    
    return response.data;
    
  } catch (error) {
    console.error('Error fetching books:', error);
    return null;
    
  }

}