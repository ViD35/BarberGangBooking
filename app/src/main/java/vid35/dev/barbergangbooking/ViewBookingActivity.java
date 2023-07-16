package vid35.dev.barbergangbooking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Calendar;

import vid35.dev.barbergangbooking.Adapter.MySmallViewPageAdapter;
import vid35.dev.barbergangbooking.Adapter.MyViewPageAdapter;
import vid35.dev.barbergangbooking.Common.Common;
import vid35.dev.barbergangbooking.databinding.ActivityBookingBinding;

public class ViewBookingActivity extends AppCompatActivity {
    private ActivityBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewPager2 viewPager = binding.viewPager;
        viewPager.setUserInputEnabled(false);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new MySmallViewPageAdapter(getSupportFragmentManager(),getLifecycle()));

        binding.textBookingTitulo.setVisibility(View.GONE);
        binding.layoutButton.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
