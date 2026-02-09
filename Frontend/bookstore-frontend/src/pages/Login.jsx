import { useState } from "react";
import "../styles/Auth.css";

function Login() {
  const [showPassword, setShowPassword] = useState(false);

  const togglePassword = () => {
    setShowPassword(prev => !prev);
  };

  return (
    <div className="auth-container">
      <form className="auth-form">
        <h2>Login</h2>

        <div className="form-group">
          <label>Email</label>
          <input type="email" placeholder="Enter your email" />
        </div>

        <div className="form-group password-group">
          <label>Password</label>
          <div className="password-wrapper">
            <input
              type={showPassword ? "text" : "password"}
              placeholder="Enter your password"
            />
            <span className="toggle-password" onClick={togglePassword}>
              {showPassword ? "Hide" : "Show"}
            </span>
          </div>
        </div>

        <button type="submit" className="auth-button">
          Login
        </button>

        <p className="auth-switch">
          Don’t have an account? <span>Register</span>
        </p>
      </form>
    </div>
  );
}

export default Login;