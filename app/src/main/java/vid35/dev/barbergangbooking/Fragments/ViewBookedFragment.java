package vid35.dev.barbergangbooking.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vid35.dev.barbergangbooking.Common.Common;
import vid35.dev.barbergangbooking.Model.BookingInformation;
import vid35.dev.barbergangbooking.Model.Cita;
import vid35.dev.barbergangbooking.R;
import vid35.dev.barbergangbooking.databinding.FragmentBookingStepThreeBinding;

public class ViewBookedFragment extends Fragment {

    static ViewBookedFragment instance;
    SimpleDateFormat simpleDateFormat;
    FragmentBookingStepThreeBinding binding;
    Date bookingDate;
    Cita citaLinkData;

    public static ViewBookedFragment getInstance() {
        if (instance == null)
            instance = new ViewBookedFragment();
        return instance;
    }

    private void setData(BookingInformation datosCita) {
        String nombreCompleto = datosCita.getBarberNombre() + " " + datosCita.getBarberApellido();
        binding.textBookingUserData.setText(nombreCompleto);
        binding.textBookingData.setText(new StringBuilder(Common.convertTimeSlotToString(datosCita.getSlot().intValue()))
                .append(" el ")
                .append(simpleDateFormat.format(bookingDate)));

        binding.txtAliasBooking.setText(datosCita.getUserAlias());
        binding.txtNombreBooking.setText(datosCita.getUserNombre());
        binding.txtApellidoBooking.setText(datosCita.getUserApellido());
    }

    private void getBookingLinkData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference bookingLinkCollectionRef = db.collection("Cita").document(Common.currentUser.getCitaId());

        bookingLinkCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                Cita datoLinkCita = document.toObject(Cita.class);
                assert datoLinkCita != null;
                getBookingData(datoLinkCita);
            } else {
                Log.d("GetUsuarioException", "Error getting documents: ", task.getException());
            }
        });
    }

    private void getBookingData(Cita citaLinkData) {
        this.citaLinkData = citaLinkData;
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("dd_MM_yy");
        try {
            bookingDate = oldDateFormat.parse(citaLinkData.getFechaCita());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference usuarioCollectionRef = db
                .collection("Admin")
                .document(citaLinkData.getBarberId())
                .collection(citaLinkData.getFechaCita())
                .document(String.valueOf(citaLinkData.getSlot()));

        usuarioCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                BookingInformation datoCita = document.toObject(BookingInformation.class);
                assert datoCita != null;
                setData(datoCita);
            } else {
                Log.d("GetUsuarioException", "Error getting documents: ", task.getException());
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
        getBookingLinkData();
        binding.txtAliasBooking.setInputType(EditorInfo.TYPE_NULL);
        binding.txtNombreBooking.setInputType(EditorInfo.TYPE_NULL);
        binding.txtApellidoBooking.setInputType(EditorInfo.TYPE_NULL);

        binding.btnConfirmarCita.setText(R.string.btn_cancelar_cita);
        binding.btnConfirmarCita.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("¿Estás seguro de que deseas cancelar la cita?")
                    .setTitle("Cancelar cita");

            builder.setPositiveButton("Si", (dialog, which) -> cancelarCita());

            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void cancelarCita() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();

        //Elimiar cita de citas del barbero
        DocumentReference currentUserBookingData = db.collection("Admin")
                .document(citaLinkData.getBarberId())
                .collection(citaLinkData.getFechaCita())
                .document(String.valueOf(citaLinkData.getSlot()));
        batch.delete(currentUserBookingData);

        //Eliminar datos de relación de cita
        DocumentReference currentUserLinkData = db.collection("Cita")
                .document(Common.currentUser.getCitaId());
        batch.delete(currentUserLinkData);

        //Desvincular cita con usuario
        DocumentReference currentUserRelation = db.collection("Usuario")
                .document(Common.currentUser.getTelefono());
        batch.update(currentUserRelation,"citaId","");

        batch.commit()
                .addOnSuccessListener(v -> {
                    Common.currentUser.setCitaId("");
                    resetStaticData();
                    requireActivity().finish();
                    Intent intent = new Intent (Common.KEY_CHECK_IF_BOOKED);
                    requireContext().sendBroadcast(intent);
                    Toast.makeText(getContext(), "Cita cancelada exitosamente", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentBarber = null;
        Common.currentDate.add(Calendar.DATE,0);
    }
}
