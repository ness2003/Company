import React, { createContext, useContext, useState, useEffect } from 'react';
import { Navigate } from 'react-router-dom';

// Создаём контекст
const AuthContext = createContext();

// Хук для использования контекста
export const useAuth = () => useContext(AuthContext);

// Провайдер контекста
export const AuthProvider = ({ children }) => {
  const [authData, setAuthData] = useState(null);

  useEffect(() => {
    const storedData = localStorage.getItem('authData');
    if (storedData) {
      setAuthData(JSON.parse(storedData)); // Восстановление данных из localStorage
    }
  }, []);

  const login = (userData) => {
    setAuthData(userData);
    localStorage.setItem('authData', JSON.stringify(userData)); // Сохраняем данные в localStorage
  };

  const logout = () => {
    setAuthData(null);
    localStorage.removeItem('authData'); // Удаляем данные из localStorage
  };

  return (
    <AuthContext.Provider value={{ authData, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
