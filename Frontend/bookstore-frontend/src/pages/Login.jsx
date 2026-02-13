import { useState } from "react";
import { loginUser } from "../services/authService";
import { useNavigate } from "react-router-dom";

function Login() {
  const [showPassword, setShowPassword] = useState(false);

  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const navigate = useNavigate();
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState("");

  const togglePassword = () => {
    setShowPassword(prev => !prev);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError(null);
  }

  const handleSubmit = async (e) => {
    e.preventDefault();

    const loginData = formData;

    try {
      const response = await loginUser(loginData);

      if(response.token){
        localStorage.setItem("token",response.token);
      }

      setSuccess("Login successful!");
      setError(null);
      navigate("/books");
      
    } catch (error) {
      setError(error.response?.data?.message || { general: "Login failed" });
      setSuccess("");
      
    }
  }

  return (
    <div className="auth-container d-flex justify-content-center align-items-center vh-100">
      <form className="auth-form card shadow p-4" style={{ width: "100%", maxWidth: "400px" }} onSubmit={handleSubmit}>
        <h2 className="text-center mb-4">Login</h2>

        {error && <p style={{ color: "red", marginBottom: "1rem" }}>{error}</p>}
        {success && <p style={{ color: "green" }}>{success}</p>}

        <div className="form-group mb-3">
          <label>Username</label>
          <input
            name="username"
            className="form-control"
            placeholder="Enter your username"
            onChange={handleChange}
          />
        </div>

        <div className="form-group mb-3">
          <label>Password</label>
          <div className="password-wrapper input-group">
            <input
              name="password"
              type={showPassword ? "text" : "password"}
              className="form-control"
              placeholder="Enter your password"
              onChange={handleChange}
            />
            <button
              type="button"
              className="btn btn-outline-secondary"
              onClick={togglePassword}
            >
              {showPassword ? "Hide" : "Show"}
            </button>
          </div>
        </div>

        <button type="submit" className="btn btn-primary w-100 mb-3">
          Login
        </button>

        <p className="text-center mb-0">
          Don’t have an account? <a href="/register">Register</a>
        </p>
      </form>
    </div>
  );
}

export default Login;