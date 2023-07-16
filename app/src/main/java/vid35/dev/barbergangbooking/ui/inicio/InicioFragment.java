package vid35.dev.barbergangbooking.ui.inicio;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import vid35.dev.barbergangbooking.BookingActivity;
import vid35.dev.barbergangbooking.Common.Common;
import vid35.dev.barbergangbooking.Fragments.ViewBookedFragment;
import vid35.dev.barbergangbooking.Model.Admin;
import vid35.dev.barbergangbooking.R;
import vid35.dev.barbergangbooking.ViewBookingActivity;
import vid35.dev.barbergangbooking.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {
    private InicioViewModel inicioViewModel;
    private FragmentInicioBinding binding;
    ArrayList<View> userCards = new ArrayList<>();
    ArrayList<Admin> adminArrayList = new ArrayList<>();
    ListenerRegistration adminChangeListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel =
                new ViewModelProvider(this).get(InicioViewModel.class);

        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        inicioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //Cargar lista de barberos en la aplicación
        adminArrayList = loadRTAdminData();

        checkIfBooked();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Legacy method
    /*
    private ArrayList<Admin> loadAdminData() {
        ArrayList<Admin> arrayAdmins = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference adminCollectionReference = db.collection("Admin");

        adminCollectionReference.orderBy("nombre");

        adminCollectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                    Admin datosAdmin = document.toObject(Admin.class);
                    arrayAdmins.add(datosAdmin);
                }
                fillAdminData();
            }
            else {
                Log.d("GetUsuarioException","Error getting documents: ",task.getException());
            }
        });
        return arrayAdmins;
    }
     */

    private ArrayList<Admin> loadRTAdminData() {
        ArrayList<Admin> arrayAdmins = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query adminCollectionReference = db
                .collection("Admin")
                .orderBy("status");

        adminChangeListener = adminCollectionReference.addSnapshotListener(requireActivity(),(value, error) -> {
            arrayAdmins.clear();
            assert value != null;
            for (QueryDocumentSnapshot document: value){
                Admin datosAdmin = document.toObject(Admin.class);
                arrayAdmins.add(datosAdmin);
            }
            fillAdminData();
        });
        return arrayAdmins;
    }

    private void fillAdminData(){
        try {
            String nombreCompleto, apodo, estado;
            userCards.clear();
            binding.layoutCardsUser.removeAllViews();

            for (int i = 0; i < adminArrayList.size(); i++) {
                userCards.add(getLayoutInflater().inflate(R.layout.cards_user, null));
                TextView txtNombre = userCards.get(i).findViewById(R.id.txt_nombre);
                TextView txtApodo = userCards.get(i).findViewById(R.id.txt_apodo);
                TextView txtEstado = userCards.get(i).findViewById(R.id.txt_estado);
                ImageView imgEstado = userCards.get(i).findViewById(R.id.imageStatusView);

                nombreCompleto = adminArrayList.get(i).getNombre() + " " + adminArrayList.get(i).getApellido();
                apodo = adminArrayList.get(i).getAlias();
                estado = adminArrayList.get(i).getStatus();

                txtNombre.setText(nombreCompleto);
                txtApodo.setText(apodo);
                txtEstado.setText(estado);
                if (estado.equals("Disponible")) {
                    imgEstado.setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.SRC_IN);
                }

                binding.layoutCardsUser.addView(userCards.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adminArrayList = loadRTAdminData();
        checkIfBooked();
    }

    private void checkIfBooked(){
        boolean isLogin = requireActivity().getIntent().getBooleanExtra(Common.IS_LOGIN, false);
        if (isLogin) {
            if (Common.currentUser.getCitaId().equals("")) {
                binding.btnAgendaCita.setText(getResources().getString(R.string.ir_agenda_cita));
                binding.btnAgendaCita.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), BookingActivity.class);
                    startActivity(intent);
                });
            } else {
                binding.btnAgendaCita.setText(getResources().getString(R.string.ir_consulta_cita));
                binding.btnAgendaCita.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), ViewBookingActivity.class);
                    startActivity(intent);
                });
            }
        }
        else {
            binding.btnAgendaCita.setVisibility(View.GONE);
        }
    }
}