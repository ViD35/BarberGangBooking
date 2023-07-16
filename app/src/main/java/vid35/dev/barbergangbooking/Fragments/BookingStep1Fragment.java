package vid35.dev.barbergangbooking.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import vid35.dev.barbergangbooking.Common.Common;
import vid35.dev.barbergangbooking.Model.Admin;
import vid35.dev.barbergangbooking.R;
import vid35.dev.barbergangbooking.databinding.FragmentBookingStepOneBinding;

public class BookingStep1Fragment extends Fragment {

    static BookingStep1Fragment instance;
    private FragmentBookingStepOneBinding binding;
    ArrayList<View> userDetailCards = new ArrayList<>();
    ArrayList<Admin> adminArrayList = new ArrayList<>();
    AlertDialog dialog;

    public static BookingStep1Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new SpotsDialog(getActivity(),"Cargando...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentBookingStepOneBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adminArrayList = loadAdminData();
    }

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

    private void fillAdminData(){
        String nombreCompleto, apodo;
        userDetailCards.clear();
        binding.layoutDetailedCardsUser.removeAllViews();

        for (int i = 0; i<adminArrayList.size();i++){
            userDetailCards.add(getLayoutInflater().inflate(R.layout.cards_detailed_user,null));
            TextView txtNombre = userDetailCards.get(i).findViewById(R.id.txt_nombre);
            TextView txtApodo = userDetailCards.get(i).findViewById(R.id.txt_apodo);

            nombreCompleto = adminArrayList.get(i).getNombre() + " " + adminArrayList.get(i).getApellido();
            apodo = adminArrayList.get(i).getAlias();

            txtNombre.setText(nombreCompleto);
            txtApodo.setText(apodo);

            userDetailCards.get(i).setOnClickListener(this::selectUserFromList);
            binding.layoutDetailedCardsUser.addView(userDetailCards.get(i));
        }
        dialog.dismiss();
    }

    private void selectUserFromList(View v){
        //Obtener posicion en arreglo de usuario seleccionado
        int pos = userDetailCards.indexOf(v);
        //Cambiar color de fondos a blanco
        for (View cardView:userDetailCards){
            CardView card = (CardView) cardView;
            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white));
        }
        //Cambiar color de objeto seleccionado a naranja
        CardView card = (CardView) v;
        card.setCardBackgroundColor(Color.parseColor("#FFA500"));
        //Habilitar el botón
        Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_SIGUIENTE);
        intent.putExtra(Common.KEY_BARBER_SELECTED,adminArrayList.get(pos));
        intent.putExtra(Common.KEY_STEP,1);
        requireContext().sendBroadcast(intent);
    }
}
