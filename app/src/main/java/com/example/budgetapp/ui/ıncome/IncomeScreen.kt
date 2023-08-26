package com.example.budgetapp.ui.ıncome


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.budgetapp.R
import com.example.budgetapp.data.IncomeExpense
import com.example.budgetapp.databinding.FragmentIncomeScreenBinding
import com.example.budgetapp.ui.adapter.SummaryAdapter
import com.google.firebase.firestore.FirebaseFirestore


class IncomeScreen : Fragment(R.layout.fragment_income_screen) {
    private lateinit var binding: FragmentIncomeScreenBinding
    private val summaryAdapter2 by lazy { SummaryAdapter(::onNoteClick,::onDeleteClick) }
    private lateinit var db: FirebaseFirestore


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIncomeScreenBinding.bind(view)
        binding.rvIncome.adapter = summaryAdapter2
        db = FirebaseFirestore.getInstance()
        listenItem()
    }

    private fun listenItem() {

        val incomeExpenseCollection = FirebaseFirestore.getInstance().collection("incomeAndexpense")
        incomeExpenseCollection.whereEqualTo("incomeExpenseType", true)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Hata durumu
                    return@addSnapshotListener
                }

                val tempList = arrayListOf<IncomeExpense>()
                var totalIncome: Double = 0.0

                snapshot?.forEach { document ->
                    val id = document.getString("docId")
                    val title = document.getString("title")
                    val price = document.getDouble("price")
                    val category = document.getString("category")
                    val incomeExpenseType = document.getBoolean("incomeExpenseType")
                    if (price != null) {

                        totalIncome += price

                    }

                    incomeExpenseType?.let {
                        tempList.add(
                            IncomeExpense(
                                id,
                                title,
                                price,
                                incomeExpenseType,
                                category
                            )
                        )
                    }
                }
                binding.txtTotalIncome.text = "$totalIncome"
                summaryAdapter2.submitList(tempList)

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
            IncomeScreenDirections.actionIncomeScreenToDetailFragment().setItem(incomeExpense)
        findNavController().navigate(action)
    }
    fun onDeleteClick(docId:String) {
deleteItem(docId)
    }

}
