package vid35.dev.barbergangbooking.ui.acercade;

import android.graphics.text.LineBreaker;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import vid35.dev.barbergangbooking.R;

public class AcercaDeFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View aboutPage = new AboutPage(getContext())
                .isRTL(false)
                .setImage(R.drawable.barbergangmainlogo_whitebackground)
                .setDescription("Se da inicio a labores en Julio del 2017, donde inicia trabajos en la calle Administradores #106 Colonia Unidad Modelo. Inician labores su propietario Luis Aldair Camarena y dos barberos más. Nace de la inquietud de su propietario por desarrollar una opción de trabajo y generar una fuente de ingresos, desarrollando sus aptitudes y talentos." +
                        " Ofrecemos la mejor calidad de atención a todos nuestros clientes y damos la mayor calidad de cortes innovadores posible, que se hagan distinguir y demuestren nuestro profesionalismo." +
                        " Buscamos ser la mejor barbería de la zona conurbada ofreciendo un servicio excelente para todos los clientes de la zona, con un personal experimentado y con actitud capaz de cumplir con las expectativas de todos sus clientes.")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Contacta con nosotros")
                .addFacebook("Bgang16","Facebook")
                .create();

        TextView description = aboutPage.findViewById(R.id.description);
        description.setGravity(Gravity.START);

        return aboutPage;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}