package com.example.budgetapp.ui.summary


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.budgetapp.R
import com.example.budgetapp.data.IncomeExpense
import com.example.budgetapp.databinding.FragmentSummaryScreenBinding
import com.example.budgetapp.ui.adapter.SummaryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class SummaryScreen : Fragment(R.layout.fragment_summary_screen) {
    private lateinit var binding: FragmentSummaryScreenBinding
    private val summaryAdapter by lazy { SummaryAdapter(::onNoteClick,::onDeleteClick) }
    private val user = Firebase.auth.currentUser
    private lateinit var db: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSummaryScreenBinding.bind(view)
        db = Firebase.firestore

        binding.rv.adapter = summaryAdapter
        showUserName()
        showSavedImage()
        listenItem()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_summaryScreen_to_detailFragment)
        }


        with(binding) {
            imageView.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

            }
            logoutImg.setOnClickListener {
                FirebaseAuth.getInstance().signOut()

                val action = SummaryScreenDirections.actionSummaryScreenToSignInScreen()
                findNavController().navigate(action)
            }
        }
    }


    private fun saveImageUrlToSharedPreferences(imageUrl: String) {
        val sharedPreferences =
            requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }

    private fun showUserName() {
        user?.reload()
        val txtUserName: TextView = requireView().findViewById(R.id.txt_userName)
        val email = user!!.email
        val userName = user.displayName


        val name = if (userName == null || userName == "") {
            val splitValue = email?.split("@")
            splitValue?.get(0).toString()
        } else {
            userName
        }

        txtUserName.text = "Hi, ${name}!"
    }

    val storageReference = FirebaseStorage.getInstance().reference
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                uploadImage(uri)
            }
        }

    private fun uploadImage(uri: Uri) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val imageRef = storageReference.child("profile_images/${currentUser.uid}.jpg")
            imageRef.putFile(uri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val imageUrl = downloadUri.toString()
                        saveImageUrlToSharedPreferences(imageUrl)
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .circleCrop()
                            .into(binding.imageView)
                    }
                    Toast.makeText(requireContext(), "Upload is successful", Toast.LENGTH_LONG)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_LONG).show()
                }
        }
    }
    private fun showSavedImage() {
        val sharedPreferences =
            requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val imageUrl = sharedPreferences.getString("imageUrl", "")

        if (imageUrl != null) {
            if (imageUrl.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(imageUrl)
                    .circleCrop()
                    .into(binding.imageView)
            }
        }
    }

    private fun listenItem() {
        db.collection("incomeAndexpense").addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Hata durumu
                return@addSnapshotListener
            }
            val tempList = arrayListOf<IncomeExpense>()
            var totalIncome: Double = 0.0
            var totalExpense: Double = 0.0
            snapshot?.forEach { document ->
                val id = document.getString("docId")
                val title = document.getString("title")
                val price = document.getDouble("price")
                val incomeExpenseType = document.getBoolean("incomeExpenseType")
                val category = document.getString("category")

                if (price != null && incomeExpenseType != null) {
                    tempList.add(
                        IncomeExpense(
                            id,
                            title,
                            price,
                            incomeExpenseType,
                            category
                        )
                    )
                    if (incomeExpenseType) {
                        // Gelir
                        totalIncome += price
                    } else {
                        // Gider
                        totalExpense += price
                    }
                }
            }

            var total = totalIncome - totalExpense
            binding.totalTxt.text = "$total"
            summaryAdapter.submitList(tempList)
        }
    }
    private fun deleteItem(docId:String){
        db.collection("incomeAndexpense").document(docId)
            .delete()
            .addOnSuccessListener {

            }
            .addOnFailureListener{

            }
    }

    fun onNoteClick(incomeExpense: IncomeExpense) {
        val action =
            SummaryScreenDirections.actionSummaryScreenToDetailFragment().setItem(incomeExpense)
        findNavController().navigate(action)
    }
    fun onDeleteClick(docId:String) {
       deleteItem(docId)
    }

}









