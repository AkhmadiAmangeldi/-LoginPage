package com.example.loginandroid

import ProfileFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.loginandroid.database.AppDatabase
import com.example.loginandroid.fragments.LoginFragment
import com.example.loginandroid.fragments.RegisterFragment
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert

// Coroutines imports
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Android context
import android.content.Context
import com.example.loginandroid.models.User
import com.example.loginandroid.repository.UserRepository

class MainActivity : AppCompatActivity() {
    var authUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "app_database"
        ).build()


// Пример записи данных
//        val userRepository = UserRepository(this)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            userRepository.insertUser(User(name = "John", phone = "123456", password = "password", city = "City"))
//        }


        loadFragment(ProfileFragment())
        // Загрузка первого фрагмента
//        if (savedInstanceState == null) {
//        }
    }

    // Функция для замены фрагментов
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
