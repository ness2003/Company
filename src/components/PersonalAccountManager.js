import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom"; // Для навигации
import "./style.css"; // Убедитесь, что у вас есть этот файл с базовыми стилями

const PersonalAccountManager = () => {
  const userId = localStorage.getItem("userId"); // Получаем ID пользователя
  const [user, setUser] = useState({
    name: "",
    surname: "",
    patronymic: "",
    phoneNumber: "",
    email: "",
  });
  const [loading, setLoading] = useState(true); // Состояние загрузки
  const [error, setError] = useState(null); // Состояние ошибки
  const [isModalOpen, setIsModalOpen] = useState(false); // Состояние для модального окна
  const [modalMessage, setModalMessage] = useState(""); // Сообщение для модального окна
  const navigate = useNavigate(); // Для навигации

  // Получаем токен из localStorage
  const token = localStorage.getItem("token");

  if (!token) {
    console.error("Токен не найден. Пожалуйста, войдите в систему.");
    navigate("/login"); // Перенаправляем на страницу входа, если токен не найден
    return null;
  }

  // Создаем axios-инстанс с токеном в заголовках
  const axiosInstance = axios.create({
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  // Загрузка данных пользователя
  useEffect(() => {
    fetchUserData();
  }, []);

  const fetchUserData = async () => {
    try {
      const response = await axiosInstance.get(`http://localhost:8080/users/${userId}`);
      setUser(response.data);
    } catch (error) {
      console.error("Ошибка при загрузке данных:", error);
      setError("Ошибка при загрузке данных.");
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleUpdate = async () => {
    try {
      await axiosInstance.put(`http://localhost:8080/users/${userId}`, user);
      setModalMessage("Данные обновлены успешно!"); // Устанавливаем сообщение для модального окна
      setIsModalOpen(true); // Открываем модальное окно
    } catch (error) {
      console.error("Ошибка при обновлении:", error);
      alert("Ошибка при обновлении данных.");
    }
  };

  const closeModal = () => {
    setIsModalOpen(false); // Закрываем модальное окно
  };

  if (loading) {
    return <div>Загрузка...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="account-container">
      <h2>Личный кабинет</h2>
      <div className="account-form">
        <label>Имя:</label>
        <input type="text" name="name" value={user.name} onChange={handleChange} />

        <label>Фамилия:</label>
        <input type="text" name="surname" value={user.surname} onChange={handleChange} />

        <label>Отчество:</label>
        <input type="text" name="patronymic" value={user.patronymic} onChange={handleChange} />

        <label>Телефон:</label>
        <input type="text" name="phoneNumber" value={user.phoneNumber} onChange={handleChange} />

        <label>Email:</label>
        <input type="email" name="email" value={user.email} onChange={handleChange} />

        <button onClick={handleUpdate}>Сохранить изменения</button>
      </div>

      {/* Модальное окно */}
      {isModalOpen && (
        <div className="modal">
          <div className="modal-content">
            <p>{modalMessage}</p>
            <button onClick={closeModal}>Закрыть</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default PersonalAccountManager;
