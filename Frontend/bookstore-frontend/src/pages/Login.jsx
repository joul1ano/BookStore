import { useState } from "react";
import { loginUser } from "../services/authService";

function Login() {
  const [showPassword, setShowPassword] = useState(false);

  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

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

      setSuccess("Registration successful!");
      setError(null);
      
    } catch (error) {
      setError(err.response?.data?.message || { general: "Registration failed" });
      setSuccess("");
      
    }
  }

  return (
    <div className="auth-container">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Login</h2>

        {error && <p style={{ color: "red", marginBottom: "1rem" }}>{error}</p>}

        <div className="form-group">
          <label>Username</label>
          <input
            name="username"
            className="form-control"
            placeholder="Enter your username"
            onChange={handleChange}
          />
        </div>

        <div className="form-group password-group">
          <label>Password</label>
          <div className="password-wrapper">
            <input
              name="password"
              type={showPassword ? "text" : "password"}
              className="form-control"
              placeholder="Enter your password"
              onChange={handleChange}
            />
            <span className="toggle-password" onClick={togglePassword}>
              {showPassword ? "Hide" : "Show"}
            </span>
          </div>
        </div>

        <button type="submit" className="auth-button btn btn-primary w-100">
          Login
        </button>

        <span className="auth-redirect">
          Don’t have an account? <a href="/register">Register</a>
        </span>
      </form>
    </div>
  );
}

export default Login;