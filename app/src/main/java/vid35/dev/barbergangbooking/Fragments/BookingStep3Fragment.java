package vid35.dev.barbergangbooking.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vid35.dev.barbergangbooking.Common.Common;
import vid35.dev.barbergangbooking.Model.BookingInformation;
import vid35.dev.barbergangbooking.Model.Cita;
import vid35.dev.barbergangbooking.databinding.FragmentBookingStepThreeBinding;

public class BookingStep3Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    FragmentBookingStepThreeBinding binding;

    static BookingStep3Fragment instance;

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentBarber = null;
        Common.currentDate.add(Calendar.DATE,0);
    }

    private void setData() {
        String nombreCompleto = Common.currentBarber.getNombre() + " " + Common.currentBarber.getApellido();
        binding.textBookingUserData.setText(nombreCompleto);
        binding.textBookingData.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
        .append(" el ")
                .append(simpleDateFormat.format(Common.currentDate.getTime())));

        binding.txtAliasBooking.setText(Common.currentUser.getAlias());
        binding.txtNombreBooking.setText(Common.currentUser.getNombre());
        binding.txtApellidoBooking.setText(Common.currentUser.getApellido());
    }

    public static BookingStep3Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep3Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.requireActivity().registerReceiver(confirmBookingReceiver,new IntentFilter(Common.KEY_CONFIRM_BOOKING));

    }

    @Override
    public void onDestroy() {
        this.requireActivity().unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentBookingStepThreeBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnConfirmarCita.setOnClickListener(v -> {
            BookingInformation bookingInformation = new BookingInformation();

            bookingInformation.setBarberAlias(Common.currentBarber.getAlias());
            bookingInformation.setBarberNombre(Common.currentBarber.getNombre());
            bookingInformation.setBarberApellido(Common.currentBarber.getApellido());
            bookingInformation.setBarberId(Common.currentBarber.getTelefono());

            bookingInformation.setUserAlias(binding.txtAliasBooking.getText().toString());
            bookingInformation.setUserNombre(binding.txtNombreBooking.getText().toString());
            bookingInformation.setUserApellido(binding.txtApellidoBooking.getText().toString());
            bookingInformation.setUserId(Common.currentUser.getTelefono());
            bookingInformation.setSlot((long) Common.currentTimeSlot);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            WriteBatch batch = db.batch();

            DocumentReference bookingDate = db
                    .collection("Admin")
                    .document(Common.currentBarber.getTelefono())
                    .collection(Common.simpleFormatDate.format(Common.currentDate.getTime()))
                    .document(String.valueOf(Common.currentTimeSlot));

            batch.set(bookingDate, bookingInformation);

            Cita citaInformation = new Cita();

            citaInformation.setBarberId(Common.currentBarber.getTelefono());
            citaInformation.setFechaCita(Common.simpleFormatDate.format(Common.currentDate.getTime()));
            citaInformation.setSlot((long) Common.currentTimeSlot);

            String citaPathId = db
                    .collection("Cita")
                    .document().getId();

            DocumentReference bookingRelation = db
                    .collection("Cita")
                    .document(citaPathId);

            batch.set(bookingRelation, citaInformation);

            DocumentReference userRelation = db
                    .collection("Usuario")
                    .document(Common.currentUser.getTelefono());

            batch.update(userRelation, "citaId", citaPathId);

            batch.commit()
                    .addOnSuccessListener(unused -> {
                        Common.currentUser.setCitaId(citaPathId);
                        resetStaticData();
                        requireActivity().finish();
                        Toast.makeText(getContext(), "¡La cita se agendó con éxito!", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
