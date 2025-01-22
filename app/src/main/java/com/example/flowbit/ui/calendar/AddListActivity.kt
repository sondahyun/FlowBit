package com.example.flowbit.ui.home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.flowbit.MainActivity
import com.example.flowbit.FlowBitApplication
import com.example.flowbit.data.entity.Expense
import com.example.flowbit.data.entity.Income
import com.example.flowbit.databinding.AddListBinding
import com.example.flowbit.ui.expense.ExpenseViewModel
import com.example.flowbit.ui.expense.ExpenseViewModelFactory
import com.example.flowbit.ui.income.IncomeViewModel
import com.example.flowbit.ui.income.IncomeViewModelFactory
import java.util.*

class AddListActivity : AppCompatActivity() {
    private lateinit var binding: AddListBinding

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((application as FlowBitApplication).expenseRepository)
    }

    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((application as FlowBitApplication).incomeRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기화
        setupCategorySpinner()
        setupToggleButtons()

        // 날짜 선택 이벤트
        binding.incomeDatePickerButton.setOnClickListener { showDatePicker(true) }
        binding.expenseDatePickerButton.setOnClickListener { showDatePicker(false) }

        // 저장 버튼 이벤트
        binding.submitButton.setOnClickListener { saveTransaction() }
    }

    private fun setupCategorySpinner() {
        val categories = listOf("식비", "교통", "쇼핑", "여행", "마트", "카페")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.expenseCategory.adapter = adapter
    }

    private fun setupToggleButtons() {
        binding.incomeButton.setOnClickListener {
            binding.incomeLayout.visibility = View.VISIBLE
            binding.expenseLayout.visibility = View.GONE
        }
        binding.expenseButton.setOnClickListener {
            binding.expenseLayout.visibility = View.VISIBLE
            binding.incomeLayout.visibility = View.GONE
        }
    }

    private fun showDatePicker(isIncome: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            if (isIncome) {
                binding.incomeDate.text = formattedDate
            } else {
                binding.expenseDate.text = formattedDate
            }
        }, year, month, day).show()
    }

    private fun saveTransaction() {
        if (binding.incomeLayout.visibility == View.VISIBLE) {
            saveIncome()
        } else if (binding.expenseLayout.visibility == View.VISIBLE) {
            saveExpense()
        }
    }

    private fun saveIncome() {
        val amount = binding.incomeAmount.text.toString().toIntOrNull()
        val place = binding.incomePlace.text.toString()
        val date = binding.incomeDate.text.toString()
        val description = binding.incomeDescription.text.toString()

        if (amount == null || amount <= 0 || place.isBlank() || date == "날짜를 선택하세요") {
            Toast.makeText(this, "모든 필드를 올바르게 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val income = Income(
            _id = 0, // Primary key auto-generated
            amount = amount,
            place = place,
            date = date,
            description = description
        )

        incomeViewModel.insertIncome(income)
        Toast.makeText(this, "수입이 저장되었습니다.", Toast.LENGTH_SHORT).show()
        Log.d("AddListActivity", "수입 저장 완료: $income")

        clearFields()
        navigateToHomeFragment()
    }

    private fun saveExpense() {
        val amount = binding.expenseAmount.text.toString().toIntOrNull()
        val place = binding.expensePlace.text.toString()
        val category = binding.expenseCategory.selectedItem?.toString()
        val date = binding.expenseDate.text.toString()
        val description = binding.expenseDescription.text.toString()

        if (amount == null || amount <= 0 || place.isBlank() || category.isNullOrEmpty() || date == "날짜를 선택하세요") {
            Toast.makeText(this, "모든 필드를 올바르게 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val expense = Expense(
            _id = 0, // Primary key auto-generated
            amount = amount,
            category = category,
            date = date,
            description = description,
            place = place
        )

        expenseViewModel.insertExpense(expense)
        Toast.makeText(this, "지출이 저장되었습니다.", Toast.LENGTH_SHORT).show()
        Log.d("AddListActivity", "지출 저장 완료: $expense")

        clearFields()
        navigateToHomeFragment()
    }

    private fun clearFields() {
        binding.incomeAmount.text.clear()
        binding.incomePlace.text.clear()
        binding.incomeDate.text = "날짜를 선택하세요"
        binding.incomeDescription.text.clear()

        binding.expenseAmount.text.clear()
        binding.expensePlace.text.clear()
        binding.expenseCategory.setSelection(0)
        binding.expenseDate.text = "날짜를 선택하세요"
        binding.expenseDescription.text.clear()
    }

    private fun navigateToHomeFragment() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigate_to", "HomeFragment")
        startActivity(intent)
        finish()
    }
}