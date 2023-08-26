package com.example.budgetapp.ui.Signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentSignInScreenBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignInScreen : Fragment(R.layout.fragment_sign_in_screen) {
    private lateinit var binding: FragmentSignInScreenBinding

    private lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInScreenBinding.bind(view)
        auth = Firebase.auth

        with(binding) {
            btnSignIn.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    signIn(email, password)
                } else {

                    Snackbar.make(view, "email ve şifre boş geçilemez", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            findNavController().navigate(R.id.signInToSummary)

        }.addOnFailureListener {
            Snackbar.make(requireView(), it.message.orEmpty(), 1000).show()

        }
    }


}