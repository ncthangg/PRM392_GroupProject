package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import model.ServiceItem;
import com.bumptech.glide.Glide;
import com.example.main.R;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private Context context;
    private List<ServiceItem> serviceList;

    public ServiceAdapter(Context context, List<ServiceItem> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item_in_list_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceItem service = serviceList.get(position);
        holder.nameTextView.setText(service.getName());
        holder.priceTextView.setText(service.getPrice() + " VND");

        if (service.getImage() != null && !service.getImage().isEmpty()) {
            Glide.with(holder.serviceImageView.getContext())
                    .load(service.getImage())
                    .placeholder(R.drawable.bg_label) // Ảnh mặc định
                    .error(R.drawable.close_icon) // Ảnh khi lỗi
                    .into(holder.serviceImageView);
        } else {
            holder.serviceImageView.setImageResource(R.drawable.bg_label);
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView;
        ImageView serviceImageView;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvServiceName);
            priceTextView = itemView.findViewById(R.id.tvPrice);
            serviceImageView = itemView.findViewById(R.id.tvServiceImageView);
        }
    }
}
