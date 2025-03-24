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

import model.ServiceItem;

public class ServiceManagerAdapter extends RecyclerView.Adapter<ServiceManagerAdapter.ServiceViewHolder> {
    private Context context;
    private List<ServiceItem> serviceList;
    private OnServiceClickListener listener;

    public interface OnServiceClickListener {
        void onServiceClick(ServiceItem service);
    }

    public ServiceManagerAdapter(Context context, List<ServiceItem> serviceList, OnServiceClickListener listener) {
        this.context = context;
        this.serviceList = serviceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item_in_list_manage_service, parent, false);
        return new ServiceViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceItem service = serviceList.get(position);

        // Load ảnh từ URL bằng Glide
        Glide.with(context).load(service.getImage()).into(holder.tvServiceImageView);

        // Gán dữ liệu vào các TextView
        holder.tvCategory.setText(service.getCategory().getName());
        holder.tvServiceName.setText(service.getName());
        holder.tvWorkerName.setText(service.getDescription());
        holder.tvPrice.setText(service.getPrice() + " VND");

        // Xử lý sự kiện click item
        holder.itemView.setOnClickListener(v -> listener.onServiceClick(service));
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView tvServiceImageView, btnBookmark;
        TextView tvCategory, tvServiceName, tvWorkerName, tvPrice;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceImageView = itemView.findViewById(R.id.tvServiceImageView);
            btnBookmark = itemView.findViewById(R.id.btnBookmark);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvWorkerName = itemView.findViewById(R.id.tvWorkerName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
