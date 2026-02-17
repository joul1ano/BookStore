import axios from "axios";

const API_URL = "http://localhost:8080";
const token = localStorage.getItem("token");

export const getMe = async () => {
    if (!token) {
        throw new Error("User not authenticated");
    }

    const response = await axios.get(
        `${API_URL}/users/me`,
        {
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        }
    )
    return response.data;
};