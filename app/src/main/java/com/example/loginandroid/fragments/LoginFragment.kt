package com.example.loginandroid.fragments

import ProfileFragment
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.loginandroid.R
import com.example.loginandroid.databinding.FragmentLoginBinding
import com.example.loginandroid.repository.UserRepository
import com.example.loginandroid.viewmodels.AuthViewModel
import com.example.loginandroid.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Инициализация ViewBinding
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val repository = UserRepository(requireContext())
        val factory = ViewModelFactory(repository)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        // Проверка авторизации
        if (authViewModel.isUserAuthenticated()) {
            navigateToProfile(authViewModel.getAuthenticatedUserId())
        }

        // Обработчик кнопки логина
        binding.loginButton.setOnClickListener {
            val phone = binding.phoneInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (phone.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val user = authViewModel.loginUser(phone, password)
                    if (user != null) {
                        authViewModel.saveAuthState(user.id)
                        Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                        navigateToProfile(user.id)
                    } else {
                        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Обработчик кнопки регистрации
        binding.registerButton.setOnClickListener {
            loadFragment(RegisterFragment())
        }

        // Показать/скрыть пароль
//        binding.passwordToggle.setOnClickListener {
//            togglePasswordVisibility()
//        }

        return view
    }

//    private fun togglePasswordVisibility() {
//        if (isPasswordVisible) {
//            binding.passwordInput.inputType =
//                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
//            binding.passwordToggle.setImageResource(R.drawable.visibility) // Закрытый глаз
//        } else {
//            binding.passwordInput.inputType =
//                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
//            binding.passwordToggle.setImageResource(R.drawable.visibility_off_24dp_e8eaed) // Открытый глаз
//        }
//        isPasswordVisible = !isPasswordVisible
//        binding.passwordInput.setSelection(binding.passwordInput.text.length)
//    }

    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToProfile(userId: Int) {
        val bundle = Bundle().apply { putInt("userId", userId) }
        val profileFragment = ProfileFragment()
        profileFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, profileFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
