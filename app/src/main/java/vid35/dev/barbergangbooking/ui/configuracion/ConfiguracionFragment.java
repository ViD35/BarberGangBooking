package vid35.dev.barbergangbooking.ui.configuracion;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import vid35.dev.barbergangbooking.Common.Common;
import vid35.dev.barbergangbooking.Model.User;
import vid35.dev.barbergangbooking.R;
import vid35.dev.barbergangbooking.databinding.FragmentConfiguracionBinding;

public class ConfiguracionFragment extends Fragment {

    private ConfiguracionViewModel configuracionViewModel;
    private FragmentConfiguracionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        configuracionViewModel =
                new ViewModelProvider(this).get(ConfiguracionViewModel.class);

        binding = FragmentConfiguracionBinding.inflate(inflater, container, false);

        loadAlreadyLoadedUserData();

        binding.btnGuardar.setOnClickListener(v-> updateUserData());

        binding.btnCancelar.setOnClickListener(v-> {
            loadAlreadyLoadedUserData();
            disableEditUserData();
            Toast.makeText(this.getActivity(), "Datos restaurados exitosamente", Toast.LENGTH_SHORT).show();});

        binding.btnEditar.setOnClickListener(v-> enableEditUserData());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadAlreadyLoadedUserData() {
        TextView txtAlias = binding.txtAlias;
        TextView txtNombre = binding.txtNombre;
        TextView txtApellido = binding.txtApellido;
        TextView txtTelefono = binding.txtTelefono;

        txtAlias.setHint(Common.currentUser.getAlias());
        txtNombre.setHint(Common.currentUser.getNombre());
        txtApellido.setHint(Common.currentUser.getApellido());
        txtTelefono.setHint(Common.currentUser.getTelefono());
    }

    private void updateUserData(){
        User datosNuevos;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String telefono = Objects.requireNonNull(user.getPhoneNumber()).substring(1);
        DocumentReference usuarioDocumentReference = db.collection("Usuario").document(telefono);

        datosNuevos = loadNewUserData();

        usuarioDocumentReference.update(User.mapData(datosNuevos)).
                addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Common.currentUser = datosNuevos;
                disableEditUserData();
                loadAlreadyLoadedUserData();
                Toast.makeText(this.getActivity(), "Datos actualizados exitosamente", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.d("UpdateUsuarioException","Error updating documents: ",task.getException());
            }
        });
    }

    private User loadNewUserData(){
        User datosNuevos;
        TextView txtAlias = binding.txtAlias;
        TextView txtNombre = binding.txtNombre;
        TextView txtApellido = binding.txtApellido;
        TextView txtTelefono = binding.txtTelefono;

        datosNuevos = new User(Common.currentUser.getCitaId(),
                txtAlias.getText().toString(),
                txtNombre.getText().toString(),
                txtApellido.getText().toString(),
                txtTelefono.getText().toString());

        return datosNuevos;
    }

    private void enableEditUserData(){
        binding.txtAlias.setEnabled(true);
        binding.txtNombre.setEnabled(true);
        binding.txtApellido.setEnabled(true);
        binding.btnEditar.setEnabled(false);
        binding.btnGuardar.setEnabled(true);
        binding.btnCancelar.setEnabled(true);

        TextView txtAlias = binding.txtAlias;
        TextView txtNombre = binding.txtNombre;
        TextView txtApellido = binding.txtApellido;
        TextView txtTelefono = binding.txtTelefono;

        txtAlias.setText(Common.currentUser.getAlias());
        txtNombre.setText(Common.currentUser.getNombre());
        txtApellido.setText(Common.currentUser.getApellido());
        txtTelefono.setText(Common.currentUser.getTelefono());
    }

    private void disableEditUserData(){
        binding.txtAlias.setEnabled(false);
        binding.txtNombre.setEnabled(false);
        binding.txtApellido.setEnabled(false);
        binding.btnEditar.setEnabled(true);
        binding.btnGuardar.setEnabled(false);
        binding.btnCancelar.setEnabled(false);

        TextView txtAlias = binding.txtAlias;
        TextView txtNombre = binding.txtNombre;
        TextView txtApellido = binding.txtApellido;
        TextView txtTelefono = binding.txtTelefono;

        txtAlias.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
    }
}