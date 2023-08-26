package com.example.budgetapp.data

import com.example.budgetapp.R

object CategoryOptions {


        fun listCategory(): ArrayList<String> {
            val listCategory = ArrayList<String>()
            listCategory.add("Food/Beverage")
            listCategory.add("Bills")
            listCategory.add("Shopping")
           listCategory.add("Salary")
            listCategory.add("Award")
           listCategory.add("Investment Return")
            return  listCategory
        }


}
enum class IncomeExpenseContent( val text: String) {
    FOOD( "Food/Beverage"),
    BILLS( "Bills"),
    SHOPPING( "Shopping"),
    SALARY("Salary"),
    AWARD( "Award"),
    INVESTMENTRETURN( "Investment Return"),



}