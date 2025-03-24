//package adapter;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager2.adapter.FragmentStateAdapter;
//
//import com.example.main.ServiceManagerActivity;
//import com.example.main.fragments.CreateServiceFragment;
//import com.example.main.fragments.ServiceListFragment;
//
//public class FragmentAdapter extends FragmentStateAdapter {
//    public FragmentAdapter(@NonNull ServiceManagerActivity activity) {
//        super(activity);
//    }
//
//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        if (position == 1) return new CreateServiceFragment();
//        return new ServiceListFragment();
//    }
//
//    @Override
//    public int getItemCount() {
//        return 2;
//    }
//}
