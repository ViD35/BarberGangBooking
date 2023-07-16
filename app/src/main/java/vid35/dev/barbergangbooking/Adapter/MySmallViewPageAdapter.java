package vid35.dev.barbergangbooking.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vid35.dev.barbergangbooking.Fragments.BookingStep1Fragment;
import vid35.dev.barbergangbooking.Fragments.BookingStep2Fragment;
import vid35.dev.barbergangbooking.Fragments.BookingStep3Fragment;
import vid35.dev.barbergangbooking.Fragments.ViewBookedFragment;

public class MySmallViewPageAdapter extends FragmentStateAdapter {

    public MySmallViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return ViewBookedFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
