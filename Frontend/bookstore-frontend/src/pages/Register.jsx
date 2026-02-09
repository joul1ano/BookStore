import "../styles/Auth.css";
import { useState } from "react";

function Register() {
  const [showPassword, setShowPassword] = useState(false);
  
    const togglePassword = () => {
      setShowPassword(prev => !prev);
    };
  return (
    <div className="auth-container">
      <form className="auth-form">
        <h2>Register</h2>

        <div className="form-group">
          <label>Email</label>
          <input
            type="email"
            placeholder="Enter your email"
          />
        </div>

        <div className="form-group">
          <label>Name</label>
          <input
            placeholder="Enter your name"
          />
        </div>

        <div className="form-group">
          <label>Username</label>
          <input
            placeholder="Enter your username"
          />
        </div>

        <div className="form-group">
          <label>Phone Number</label>
          <input
            placeholder="Enter your phone number"
          />
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

        <div className="form-group password-group">
          <label>Confirm Password</label>
          <div className="password-wrapper">
            <input
              type={showPassword ? "text" : "password"}
              placeholder="Confirm your password"
            />
            <span className="toggle-password" onClick={togglePassword}>
              {showPassword ? "Hide" : "Show"}
            </span>
          </div>
        </div>

        <button type="submit" className="auth-button">
          Register
        </button>

        <p className="auth-switch">
          Already have an account? <span>Login</span>
        </p>
      </form>
    </div>
  );
}

export default Register;