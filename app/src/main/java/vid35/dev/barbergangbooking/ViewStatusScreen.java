package vid35.dev.barbergangbooking;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import vid35.dev.barbergangbooking.Common.Common;
import vid35.dev.barbergangbooking.databinding.ActivityViewStatusScreenBinding;

public class ViewStatusScreen extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityViewStatusScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewStatusScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarViewStatusScreen.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        if (getIntent()!=null){
            boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN,false);
            if (isLogin){
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_inicio, R.id.nav_acercade, R.id.nav_configuracion)
                        .setOpenableLayout(drawer)
                        .build();
            }
            else{
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_inicio, R.id.nav_acercade)
                        .setOpenableLayout(drawer)
                        .build();
                binding.navView.getMenu().removeItem(R.id.nav_configuracion);
            }
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_view_status_screen);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_view_status_screen);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}