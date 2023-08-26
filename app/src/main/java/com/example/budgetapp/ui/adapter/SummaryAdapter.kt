package com.example.budgetapp.ui.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetapp.R
import com.example.budgetapp.data.IncomeExpense
import com.example.budgetapp.data.IncomeExpenseContent
import com.example.budgetapp.databinding.IncomeExpenseItemBinding


class SummaryAdapter(
    private val onNoteClick: (IncomeExpense) -> Unit,
    private val onDeleteClick:(String)->Unit


    ) : ListAdapter<IncomeExpense, SummaryAdapter.NoteViewHolder>(NoteDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(
            IncomeExpenseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onNoteClick,
            onDeleteClick



        )
    class NoteViewHolder(
        val binding: IncomeExpenseItemBinding,
        private val onNoteClick: (IncomeExpense) -> Unit,
        private val onDeleteClick:(String)->Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(ıncomeExpense: IncomeExpense) = with(binding) {

            var type = ıncomeExpense.incomeExpenseType

            txtTitle.text = ıncomeExpense.title
            txtPrice.text = ıncomeExpense.price.toString()
            txtCategory.text = ıncomeExpense.category
            if (type == true) {
                txtPrice.text = "+" + ıncomeExpense.price.toString()
                txtPrice.setTextColor(ContextCompat.getColor(root.context, R.color.green))
            } else {
                txtPrice.text = "-" + ıncomeExpense.price.toString()
                txtPrice.setTextColor(ContextCompat.getColor(root.context, R.color.red))

            }

            val categoryText = txtCategory.text.toString()
            val imageResource = when (categoryText) {
                IncomeExpenseContent.SHOPPING.text -> R.drawable.shopping
                IncomeExpenseContent.FOOD.text -> R.drawable.food
                IncomeExpenseContent.BILLS.text -> R.drawable.fatura
                IncomeExpenseContent.SALARY.text -> R.drawable.maas
                IncomeExpenseContent.AWARD.text -> R.drawable.yatirim
                IncomeExpenseContent.INVESTMENTRETURN.text -> R.drawable.yatirim
                else -> R.drawable.home
            }

            imgIcon.setImageResource(imageResource)

            root.setOnClickListener {
                onNoteClick(ıncomeExpense)

            }
            delete.setOnClickListener {
                onDeleteClick(ıncomeExpense.docId!!)
            }

        }
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
        holder.bind(getItem(position))


    class NoteDiffCallBack : DiffUtil.ItemCallback<IncomeExpense>() {
        override fun areItemsTheSame(oldItem: IncomeExpense, newItem: IncomeExpense): Boolean {
            return oldItem.docId == newItem.docId
        }

        override fun areContentsTheSame(oldItem: IncomeExpense, newItem: IncomeExpense): Boolean {
            return oldItem == newItem
        }
    }
}