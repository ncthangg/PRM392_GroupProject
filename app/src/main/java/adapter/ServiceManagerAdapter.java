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
import com.example.main.models.ServiceItem;
import java.util.List;

import com.example.main.models.ServiceItem;

public class ServiceManagerAdapter extends RecyclerView.Adapter<ServiceManagerAdapter.ViewHolder> {
    private Context context;
    private List<ServiceItem> serviceList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ServiceItem service);
    }

    public ServiceManagerAdapter(Context context, List<ServiceItem> serviceList, OnItemClickListener listener) {
        this.context = context;
        this.serviceList = serviceList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item_in_list_manage_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceItem service = serviceList.get(position);
        holder.tvServiceName.setText(service.getName());
        holder.tvServicePrice.setText(String.format("$%s", service.getPrice()));

        Glide.with(context).load(service.getImage()).into(holder.ivServiceImage);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(service));
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvServicePrice;
        ImageView ivServiceImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServicePrice = itemView.findViewById(R.id.tvPrice);
            ivServiceImage = itemView.findViewById(R.id.tvServiceImageView);
        }
    }
}
