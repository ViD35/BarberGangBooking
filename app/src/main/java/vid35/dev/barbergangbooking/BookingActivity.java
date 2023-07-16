package vid35.dev.barbergangbooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import vid35.dev.barbergangbooking.Adapter.MyViewPageAdapter;
import vid35.dev.barbergangbooking.Common.Common;
import vid35.dev.barbergangbooking.Model.Admin;
import vid35.dev.barbergangbooking.databinding.ActivityBookingBinding;

public class BookingActivity extends AppCompatActivity {
    private ActivityBookingBinding binding;

    private final BroadcastReceiver btnSiguienteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int step = intent.getIntExtra(Common.KEY_STEP,0);
            if (step == 1)
                Common.currentBarber = intent.getParcelableExtra(Common.KEY_BARBER_SELECTED);
            if (step == 2)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT,-1);

            binding.btnSiguiente.setEnabled(true);
        }
    };

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(btnSiguienteReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.registerReceiver(btnSiguienteReceiver,new IntentFilter(Common.KEY_ENABLE_BUTTON_SIGUIENTE));

        ViewPager2 viewPager = binding.viewPager;
        viewPager.setUserInputEnabled(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new MyViewPageAdapter(getSupportFragmentManager(),getLifecycle()));
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.btnRegresar.setEnabled(position != 0);
                switch (position){
                    case 0:
                        binding.textBookingTitulo.setText(getResources()
                                .getString(R.string.text_titulo_booking));
                        break;
                    case 1:
                        binding.textBookingTitulo.setText(getResources().getString(R.string.text_titulo2_booking));
                        break;
                    case 2:
                        binding.textBookingTitulo.setText(getResources().getString(R.string.text_titulo3_booking));
                        break;
                }
            }
        });

        binding.btnRegresar.setOnClickListener(v -> {
            if (Common.step > 0){
                Common.step--;
                viewPager.setCurrentItem(Common.step);
                if (Common.currentBarber!=null){
                    binding.btnSiguiente.setEnabled(true);
                }
            }
        });

        binding.btnSiguiente.setOnClickListener(v -> {
            if (Common.step < 2){
                Common.step++;
                if (Common.step == 1 && Common.currentBarber!=null){
                    loadTimeSlotOfBarber();
                }
                else if (Common.step == 2 && Common.currentTimeSlot != -1){
                    confirmBooking();
                }
                viewPager.setCurrentItem(Common.step);
            }
        });
    }

    private void confirmBooking() {
        binding.btnSiguiente.setEnabled(false);
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        this.sendBroadcast(intent);
    }

    private void loadTimeSlotOfBarber() {
        binding.btnSiguiente.setEnabled(false);
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        this.sendBroadcast(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            resetStaticData();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        resetStaticData();
        super.onBackPressed();
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentBarber = null;
        Common.currentDate.add(Calendar.DATE,0);
    }
}