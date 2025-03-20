package com.example.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.main.dto.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.txtTransactionTitle.setText(transaction.getTitle());
        holder.txtTransactionTime.setText(transaction.getDateTime());
        holder.txtTransactionAmount.setText(transaction.getAmount());

        // Hiển thị tiêu đề ngày nếu là giao dịch đầu tiên của ngày
        if (position == 0 || !transactions.get(position - 1).getDate().equals(transaction.getDate())) {
            holder.txtTransactionDate.setVisibility(View.VISIBLE);
            holder.txtTransactionDate.setText(transaction.getDate());
        } else {
            holder.txtTransactionDate.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView txtTransactionDate, txtTransactionTitle, txtTransactionTime, txtTransactionAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTransactionDate = itemView.findViewById(R.id.txtTransactionDate);
            txtTransactionTitle = itemView.findViewById(R.id.txtTransactionTitle);
            txtTransactionTime = itemView.findViewById(R.id.txtTransactionTime);
            txtTransactionAmount = itemView.findViewById(R.id.txtTransactionAmount);
        }
    }
}
