package com.example.budgetapp.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentSignUpScreenBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpScreen : Fragment(R.layout.fragment_sign_up_screen) {
    private lateinit var binding: FragmentSignUpScreenBinding
    private lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpScreenBinding.bind(view)
        auth = Firebase.auth
        auth.currentUser?.let {
            findNavController().navigate(R.id.signUpToSummary)
        }


        with(binding) {
            btnSignUp.setOnClickListener {

                val email = etEmail.text.toString()
                val password = etPassword.text.toString()


                if (email.isNotEmpty() && password.isNotEmpty() ) {
                    signUp(email, password)
                } else {
                    //show snackbar
                }
            }

            tvAlreadyHaveAnAccount.setOnClickListener {
                findNavController().navigate(R.id.signUpToSignIn)
            }
        }
    }


    private fun signUp(email: String, password: String) {
        /*auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                findNavController().navigate(R.id.signUpToNotes)
            } else {
                //show snackbar (task.exception?.message.orEmpty())
            }
        }*/

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            findNavController().navigate(R.id.signUpToSummary)

        }.addOnFailureListener {
            Snackbar.make(requireView(), it.message.orEmpty(), 1000).show()
            //show snackbar (it.message.orEmpty())
        }
    }
}
