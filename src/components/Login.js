import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await axios.post('http://localhost:8080/login', { email, password });

      localStorage.setItem('token', response.data.token);
      localStorage.setItem('role', response.data.role);
      localStorage.setItem('userId', response.data.userId);  // Сохраняем ID
      navigate(response.data.role === 'ADMIN' ? 'http://localhost:8080/user-roles' : 'http://localhost:8080/user-products');
    } catch (error) {
      console.error('Login failed:', error);
    }
  };


  return (
    <div className="login-container">
      <div className="login-box">
        <h2>Вход</h2>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="login-input"
        />
        <input
          type="password"
          placeholder="Пароль"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="login-input"
        />
        <button onClick={handleLogin} className="login-button">Войти</button>
      </div>
    </div>
  );
};

export default Login;
