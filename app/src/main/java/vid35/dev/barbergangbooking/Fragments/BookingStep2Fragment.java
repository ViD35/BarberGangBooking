package vid35.dev.barbergangbooking.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import vid35.dev.barbergangbooking.Adapter.MyTimeSlotAdapter;
import vid35.dev.barbergangbooking.Common.Common;
import vid35.dev.barbergangbooking.Common.SpacesItemDecoration;
import vid35.dev.barbergangbooking.Interface.ITimeSlotLoadListener;
import vid35.dev.barbergangbooking.Model.TimeSlot;
import vid35.dev.barbergangbooking.R;
import vid35.dev.barbergangbooking.databinding.FragmentBookingStepTwoBinding;

public class BookingStep2Fragment extends Fragment implements ITimeSlotLoadListener {

    static BookingStep2Fragment instance;
    private FragmentBookingStepTwoBinding binding;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDateDay;
    private TextView txtDisplayDate;
    private TextView txtDateYear;

    DocumentReference barberDoc;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;
    SimpleDateFormat simpleDateFormat;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadAvailableTimeSlotBarber(Common.currentBarber.getTelefono(),
                    simpleDateFormat.format(Common.currentDate.getTime()));
        }
    };

    private void loadAvailableTimeSlotBarber(String telefono, String bookDate) {
        dialog.show();

        barberDoc = FirebaseFirestore.getInstance()
                .collection("Admin")
                .document(Common.currentBarber.getTelefono());

        barberDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                assert documentSnapshot != null;
                if (documentSnapshot.exists()){
                    CollectionReference date = FirebaseFirestore.getInstance()
                            .collection("Admin")
                            .document(Common.currentBarber.getTelefono())
                            .collection(bookDate);

                    date.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            QuerySnapshot querySnapshot = task1.getResult();
                            assert querySnapshot != null;
                            if (querySnapshot.isEmpty())
                                iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                            else {
                                List<TimeSlot> timeSlots = new ArrayList<>();
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task1.getResult()))
                                    timeSlots.add(document.toObject(TimeSlot.class));
                                iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                            }
                        }
                    }).addOnFailureListener(e -> iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage()));
                }
            }
        });
    }

    public static BookingStep2Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new SpotsDialog(getActivity(),"Cargando...");
        dialog.setCancelable(false);

        iTimeSlotLoadListener = this;
        this.requireActivity().registerReceiver(displayTimeSlot,new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));

        Common.currentDate = Calendar.getInstance();
    }

    @Override
    public void onDestroy() {
        this.requireActivity().unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentBookingStepTwoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        asignComponents();
        setViewDateToday();
        init();

        btnNext.setOnClickListener(v -> {
            btnNext.setClickable(false);
            Calendar newDate = Calendar.getInstance();
            newDate.setTime(Common.currentDate.getTime());
            newDate.add(Calendar.DAY_OF_YEAR,1);
            Common.currentDate = newDate;
            setViewDateToday();
            loadAvailableTimeSlotBarber(Common.currentBarber.getTelefono(),
                    simpleDateFormat.format(Common.currentDate.getTime()));
            btnNext.setClickable(true);
        });

        btnPrev.setOnClickListener(v -> {
            btnPrev.setClickable(false);
            if (!Common.currentDate.before(Calendar.getInstance())){
                Calendar newDate = Calendar.getInstance();
                newDate.setTime(Common.currentDate.getTime());
                newDate.add(Calendar.DAY_OF_YEAR,-1);
                Common.currentDate = newDate;
                setViewDateToday();
                loadAvailableTimeSlotBarber(Common.currentBarber.getTelefono(),
                        simpleDateFormat.format(Common.currentDate.getTime()));
            }
            btnPrev.setClickable(true);
        });
    }

    private void init() {
        binding.recyclerTimeSlot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        binding.recyclerTimeSlot.setLayoutManager(gridLayoutManager);
        binding.recyclerTimeSlot.addItemDecoration(new SpacesItemDecoration(8));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yy",Locale.forLanguageTag("es-MX"));
        //loadAvailableTimeSlotBarber(Common.currentBarber.getTelefono(),
        //        sdf.format(currentDate.getTime()));
    }

    private void asignComponents(){
        btnPrev = binding.calendarPrevButton;
        btnNext = binding.calendarNextButton;
        txtDateDay = binding.dateDisplayDay;
        txtDateYear = binding.dateDisplayYear;
        txtDisplayDate = binding.dateDisplayDate;
    }

    private void setViewDateToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,d MMM,yyyy",Locale.forLanguageTag("es-MX"));
        String[] dateToday = sdf.format(Common.currentDate.getTime()).split(",");
        txtDateDay.setText(dateToday[0]);
        txtDisplayDate.setText(dateToday[1]);
        txtDateYear.setText(dateToday[2]);

        if (Common.currentDate.before(Calendar.getInstance())){
            btnPrev.setVisibility(View.INVISIBLE);
            btnPrev.setClickable(false);
        }
        else{
            btnPrev.setVisibility(View.VISIBLE);
            btnPrev.setClickable(true);
        }
    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(),timeSlotList);
        binding.recyclerTimeSlot.setAdapter(adapter);
        
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        binding.recyclerTimeSlot.setAdapter(adapter);
        dialog.dismiss();
    }
}
