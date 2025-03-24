package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.main.R;

import java.util.List;

import model.BookingItem;
import model.ServiceItem;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private Context context;
    private List<BookingItem> bookingList;
    private BookingAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(BookingItem bookingItem);
    }

    public BookingAdapter(Context context, List<BookingItem> bookingList, BookingAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.bookingList = bookingList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BookingAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_booking_item, parent, false);
        return new BookingAdapter.BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.BookingViewHolder holder, int position) {
        BookingItem booking = bookingList.get(position);
        holder.nameTextView.setText(booking.getNote());
        holder.priceTextView.setText(booking.getAddress());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(booking));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView, nameTextView, workerNameTextView, priceTextView;
        ImageView serviceImageView, bookmarkImageView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.tvCategory);
            nameTextView = itemView.findViewById(R.id.tvServiceName);
            workerNameTextView = itemView.findViewById(R.id.tvWorkerName);
            priceTextView = itemView.findViewById(R.id.tvPrice);
            serviceImageView = itemView.findViewById(R.id.tvServiceImageView);
            bookmarkImageView = itemView.findViewById(R.id.btnBookmark);
        }
    }

}
