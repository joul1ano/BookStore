import "../styles/Auth.css";
import { useState } from "react";
import { registerUser } from "../services/authService";

function Register() {
  const [showPassword, setShowPassword] = useState(false);

  const [formData, setFormData] = useState({
    email: "",
    name: "",
    username: "",
    phone: "",
    password: "",
    confirmPassword: "",
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
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    const { confirmPassword, ...requestData } = formData;

    try {
      const response = await registerUser(requestData);

      if (response.token) {
        localStorage.setItem("token", response.token);
      }

      setSuccess("Registration successful!");
      setError(null);
    } catch (err) {
      setError(err.response?.data?.message || { general: "Registration failed" });
      setSuccess("");
    }
  };

  return (
    <div className="auth-container">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Register</h2>

        {error && (
          <ul style={{ color: "red", marginBottom: "1rem" }}>
            {Object.values(error).map((msg, index) => (
              <li key={index}>{msg}</li>
            ))}
          </ul>
        )}
        {success && <p style={{ color: "green" }}>{success}</p>}

        <div className="form-group">
          <label>Email</label>
          <input name="email" onChange={handleChange} />
        </div>

        <div className="form-group">
          <label>Name</label>
          <input name="name" onChange={handleChange} />
        </div>

        <div className="form-group">
          <label>Username</label>
          <input name="username" onChange={handleChange} />
        </div>

        <div className="form-group">
          <label>Phone Number</label>
          <input name="phone" onChange={handleChange} />
        </div>

        <div className="form-group password-group">
          <label>Password</label>
          <div className="password-wrapper">
            <input
              name="password"
              type={showPassword ? "text" : "password"}
              onChange={handleChange}
            />
            <span className="toggle-password" onClick={togglePassword}>
              {showPassword ? "Hide" : "Show"}
            </span>
          </div>
        </div>

        <div className="form-group password-group">
          <label>Confirm Password</label>
          <div className="password-wrapper">
            <input
              name="confirmPassword"
              type={showPassword ? "text" : "password"}
              onChange={handleChange}
            />
          </div>
        </div>

        <button type="submit" className="auth-button">
          Register
        </button>
        <span className="auth-redirect">
          Already have an account? <a href="/">Log in</a>
        </span>
      </form>
    </div>
  );
}

export default Register;