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

import com.example.main.models.BookingItem;

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
        holder.nameTextView.setText(booking.getService().getName());
        holder.categoryTextView.setText(booking.getService().getCategory().getName());
        holder.workerNameTextView.setText(booking.getService().getCategory().getName());
        if (booking.getService().getImage() != null && !booking.getService().getImage().isEmpty()) {
            Glide.with(holder.serviceImageView.getContext())
                    .load(booking.getService().getImage())
                    .placeholder(R.drawable.bg_label) // Ảnh mặc định
                    .error(R.drawable.close_icon) // Ảnh khi lỗi
                    .into(holder.serviceImageView);
        } else {
            holder.serviceImageView.setImageResource(R.drawable.bg_label);
        }
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(booking));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView, nameTextView, workerNameTextView;
        ImageView serviceImageView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.tvCategory);
            nameTextView = itemView.findViewById(R.id.tvServiceName);
            workerNameTextView = itemView.findViewById(R.id.tvWorkerName);
            serviceImageView = itemView.findViewById(R.id.tvServiceImageView);
        }
    }

}
