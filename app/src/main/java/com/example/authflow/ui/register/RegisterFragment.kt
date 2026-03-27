package com.example.authflow.ui.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.authflow.R
import com.example.authflow.databinding.FragmentRegisterBinding
import com.example.authflow.models.UserEntity
import com.example.authflow.network.AppDatabase
import com.example.authflow.repository.AuthRepository
import com.example.authflow.viewmodel.AuthViewModel
import com.example.authflow.viewmodel.ViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = AuthRepository(database.userDao())
        ViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGenderSpinner()
        setupDatePicker()
        setupListeners()
        observeViewModel()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupListeners() {
        binding.btnSubmit.setOnClickListener {
            if (validateInputs()) {
                val user = UserEntity(
                    email = binding.etEmail.text.toString().trim(),
                    name = binding.etFullName.text.toString().trim(),
                    password = binding.etPassword.text.toString().trim(),
                    phone = binding.etPhone.text.toString().trim()
                )
                viewModel.register(user)
            }
        }
    }

    private fun setupGenderSpinner() {
        val genders = resources.getStringArray(R.array.gender_options)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, genders)
        binding.spinnerGender.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        binding.btnDatePicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date of Birth")
                .build()

            datePicker.addOnPositiveButtonClickListener {
                binding.btnDatePicker.text = datePicker.headerText
            }

            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }
    }

    private fun validateInputs(): Boolean {
        val name = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        var isValid = true

        if (name.isEmpty()) {
            binding.etFullName.error = "Name is required"
            isValid = false
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Valid email is required"
            isValid = false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            isValid = false
        }

        if (!binding.cbTerms.isChecked) {
            Toast.makeText(context, "Please accept Terms and Conditions", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun observeViewModel() {
        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> {
                    binding.progressIndicator.isVisible = true
                    binding.btnSubmit.isEnabled = false
                }
                is AuthViewModel.AuthState.Success -> {
                    binding.progressIndicator.isVisible = false
                    binding.btnSubmit.isEnabled = true
                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_dashboardFragment)
                }
                is AuthViewModel.AuthState.Error -> {
                    binding.progressIndicator.isVisible = false
                    binding.btnSubmit.isEnabled = true
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
