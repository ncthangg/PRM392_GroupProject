package com.example.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.main.databinding.ActivityWalletBinding;
import com.example.main.dto.Transaction;

import java.util.ArrayList;
import java.util.List;

public class WalletActivity extends AppCompatActivity {
    private ActivityWalletBinding binding;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(this));
        binding.txtWalletBalance.setText("Wallet Balance");
        binding.txtBalanceAmount.setText("200.000VND");

        binding.btnAddMoney.setOnClickListener(v -> {
            // Xử lý khi bấm nút
        });

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        transactionList = new ArrayList<>();
        transactionList.add(new Transaction("Money Added to Wallet", "24 September", "7:30", "+200.000VND"));
        transactionAdapter = new TransactionAdapter(transactionList);
        binding.recyclerViewTransactions.setAdapter(transactionAdapter);
    }
}
