package vid35.dev.barbergangbooking;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import vid35.dev.barbergangbooking.Common.Common;
import vid35.dev.barbergangbooking.Model.User;
import vid35.dev.barbergangbooking.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    //Proveniente de la documentacion de FirebaseAuthUI
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

    Intent signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //Listener en caso de que ya se haya iniciado sesion
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth1 -> {
            FirebaseUser user = firebaseAuth1.getCurrentUser();
            if (user != null)
            {
                checkUserExistance(user);
            }
            else{
                setContentView(binding.getRoot());
            }
        };
        //Codigo de botones
        binding.btnLogin.setOnClickListener(v -> signInLauncher.launch(signInIntent));

        binding.txtSkip.setOnClickListener(v -> {
            Intent intent = new Intent(this,ViewStatusScreen.class);
            intent.putExtra(Common.IS_LOGIN,false);
            startActivity(intent);
        });
    }

    //Cuando se tenga abierta o cerrada la aplicacion
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        super.onStop();
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        } else {
            Toast.makeText(this, "Se canceló el inicio de sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserExistance(FirebaseUser user){
        String telefono = user.getPhoneNumber().substring(1);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference pathDoc = db.collection("Usuario").document(telefono);

        pathDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (!Objects.requireNonNull(task.getResult()).exists()){
                    createUserData(pathDoc,telefono);
                }
                else {
                    Common.currentUser = task.getResult().toObject(User.class);
                    Intent intent = new Intent(this,ViewStatusScreen.class);
                    intent.putExtra(Common.IS_LOGIN,true);
                    startActivity(intent);
                    finish();
                }
            }
            else{
                Log.d("GetUsuarioException","Error getting documents: ",task.getException());
            }
        });
    }

    private void createUserData(DocumentReference pathDoc,String telefono){
        User usuario = new User("","","","",telefono);
        pathDoc.set(usuario).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Common.currentUser = usuario;
                Intent intent = new Intent(this,ViewStatusScreen.class);
                intent.putExtra(Common.IS_LOGIN,true);
                startActivity(intent);
                finish();
            }
        });
    }
}