import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.loginandroid.R
import com.example.loginandroid.databinding.FragmentProfileBinding
import com.example.loginandroid.fragments.Favourite
import com.example.loginandroid.fragments.Help_info
import com.example.loginandroid.fragments.LoginFragment
import com.example.loginandroid.fragments.Payment
import com.example.loginandroid.fragments.ProfileSettingsFragment
import com.example.loginandroid.fragments.RegisterFragment
import com.example.loginandroid.fragments.Settings
import com.example.loginandroid.models.User
import com.example.loginandroid.repository.UserRepository
import com.example.loginandroid.viewmodels.AuthViewModel
import com.example.loginandroid.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userRepository: UserRepository
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val repository = UserRepository(requireContext())
        val factory = ViewModelFactory(repository)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        // Инициализация репозитория
        userRepository = UserRepository(requireContext())

        // Проверка авторизации пользователя
        if (isUserAuthenticated()) {
            // Если пользователь авторизован
            binding.isAuth.visibility = View.VISIBLE
            binding.notAuth.visibility = View.GONE

            binding.profileSettings.setOnClickListener {
                loadFragment(ProfileSettingsFragment())
            }
            binding.paymentMethods.setOnClickListener {
                loadFragment(Payment())
            }
            binding.favourite.setOnClickListener {
                loadFragment(Favourite())
            }
            binding.settings.setOnClickListener {
                loadFragment(Settings())
            }
            binding.helpInfo.setOnClickListener {
                loadFragment(Help_info())
            }

            // Пример: получаем ID пользователя и отображаем его имя
            val userId = userRepository.getAuthenticatedUserId()

            lifecycleScope.launch {
                val authUser = authViewModel.getUserById(userId = userId)
//                binding.city.text = "City: ${authUser?.city}"
                binding.userName.text = "${authUser?.name}"
//                binding.phone.text = "Phone: ${authUser?.phone}"
//                binding.balance.text = "Balance: ${authUser?.balance}"
            }

            binding.logoutButton.setOnClickListener {
                AuthViewModel(userRepository).logout() // Очистка данных авторизации
                navigateToLogin()
            }
        } else {
            // Если пользователь не авторизован
            binding.isAuth.visibility = View.GONE
            binding.notAuth.visibility = View.VISIBLE

            binding.login.setOnClickListener {
                loadFragment(LoginFragment())
            }
            binding.registerButton.setOnClickListener {
                loadFragment(RegisterFragment())
            }
        }

        return rootView
    }
    private fun navigateToLogin() {
        loadFragment(ProfileFragment())
    }
    private fun loadFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun isUserAuthenticated(): Boolean {
        // Проверяем статус авторизации через UserRepository
        return userRepository.isUserAuthenticated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
