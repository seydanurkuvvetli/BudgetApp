package com.example.budgetapp.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budgetapp.R
import com.example.budgetapp.data.CategoryOptions
import com.example.budgetapp.data.IncomeExpense
import com.example.budgetapp.databinding.FragmentDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID


class DetailFragment : BottomSheetDialogFragment(R.layout.fragment_detail),
    RadioGroup.OnCheckedChangeListener {
    private lateinit var binding: FragmentDetailBinding
    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var budget: IncomeExpense
    private lateinit var db: FirebaseFirestore
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)
        db = Firebase.firestore
        with(binding) {

            args.item?.let {
                etTitle.setText(it.title)
                etPrice.setText(it.price.toString())
                btnSave.visibility = View.VISIBLE
                btnAdd.visibility = View.GONE
                if (it.incomeExpenseType) {
                    rbIncome.isChecked = true
                } else {
                    rbExpense.isChecked = true
                }
                val categoryAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    CategoryOptions.listCategory()
                )
                spinner.adapter = categoryAdapter
            }
            val categoryAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                CategoryOptions.listCategory()
            )
            spinner.adapter = categoryAdapter


            btnAdd.setOnClickListener {
                val title = etTitle.text.toString()
                val price = etPrice.text.toString().toDouble()
                val incomeExpenseType = rbIncome.isChecked
                val category = binding.spinner.selectedItem as String
                if (title.isNotEmpty() && price.toString().isNotEmpty() && category.isNotEmpty()) {
                    addNote(title, price, incomeExpenseType, category)

                } else {
                    Toast.makeText(requireContext(), "boş geçilemez", Toast.LENGTH_LONG).show()
                }
            }

            btnSave.setOnClickListener {

                val title = binding.etTitle.text.toString()
                val price = binding.etPrice.text.toString().toDouble()
                val selectedCategory = binding.spinner.selectedItem as String
                val incomeExpenseType = binding.rbIncome.isChecked

                val incomeExpense = IncomeExpense(
                    args.item?.docId ?: "",
                    title,
                    price,
                    incomeExpenseType,
                    selectedCategory
                )
                saveNote(incomeExpense)
            }
        }
    }


    private fun saveNote(incomeExpense: IncomeExpense) {
        val data = mapOf(
            "docId" to incomeExpense.docId,
            "title" to incomeExpense.title,
            "price" to incomeExpense.price,  // Bu kısmı düzeltin
            "incomeExpenseType" to incomeExpense.incomeExpenseType,
            "category" to incomeExpense.category
        )
        db.collection("incomeAndexpense").document(incomeExpense.docId!!)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Successfully saved.", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save.", Toast.LENGTH_SHORT).show()
            }
    }
    private fun generateUniqueDocId(): String {
        return UUID.randomUUID().toString()
    }
    private fun addNote(
        title: String,
        price: Double,
        incomeExpenseType: Boolean,
        category: String
    ) {
        val docId = generateUniqueDocId()
        val note = IncomeExpense(
            docId = docId,
            title = title,
            price = price,
            incomeExpenseType = incomeExpenseType,
            category = category

        )
        db.collection("incomeAndexpense").document(docId).set(note)
            .addOnSuccessListener { documentReference ->
                findNavController().navigateUp()
            }
            .addOnFailureListener {
            }
    }
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val selectedRadioButton: RadioButton = binding.root.findViewById(checkedId)
        when (selectedRadioButton.text.toString()) {
            "Expense" -> {
                !budget.incomeExpenseType
            }
            "Income" -> {
                budget.incomeExpenseType

            }
        }
    }
}