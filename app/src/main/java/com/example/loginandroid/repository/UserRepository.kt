package com.example.loginandroid.repository

import android.content.Context
import com.example.loginandroid.dao.UserDao
import com.example.loginandroid.database.AppDatabase
import com.example.loginandroid.models.User

class UserRepository(context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()
    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    // Сохранить статус авторизации
    fun saveAuthState(userId: Int) {
        sharedPreferences.edit()
            .putBoolean("isAuthenticated", true)
            .putInt("userId", userId)
            .apply()
    }

    // Проверка статуса авторизации
    fun isUserAuthenticated(): Boolean {
        return sharedPreferences.getBoolean("isAuthenticated", false)
    }

    // Получить userId авторизованного пользователя
    fun getAuthenticatedUserId(): Int {
        return sharedPreferences.getInt("userId", -1)
    }

    // Очистить статус авторизации (выход из системы)
    fun clearAuthState() {
        sharedPreferences.edit().clear().apply()
    }
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
    suspend fun getUser(phone: String, password: String): User? {
        return userDao.getUser(phone, password)
    }
    // Проверка существования пользователя по номеру телефона
    suspend fun isUserExists(phone: String): Boolean {
        return userDao.getUserByPhone(phone) != null
    }

    // Получение пользователя по ID (для профиля)
    suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }
    suspend fun getUserByPhone(phone: String): User? {
        return userDao.getUserByPhone(phone)
    }
    suspend fun getUserByName(name: String): User? {
        return userDao.getUserByName(name)
    }
    suspend fun getUserByCity(city: String): User? {
        return userDao.getUserByCity(city)
    }


}
