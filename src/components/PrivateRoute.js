import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const PrivateRoute = ({ roles }) => {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  if (!token) {
    // Если нет токена, перенаправляем на страницу логина
    return <Navigate to="/login" />;
  }

  if (!roles.includes(role)) {
    // Если роль не соответствует, перенаправляем на страницу с ошибкой или доступ запрещен
    return <Navigate to="/login" />;
  }

  return <Outlet />;
};

export default PrivateRoute;
