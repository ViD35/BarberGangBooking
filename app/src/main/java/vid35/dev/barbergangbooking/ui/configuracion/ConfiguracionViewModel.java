package vid35.dev.barbergangbooking.ui.configuracion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfiguracionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConfiguracionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragmento de configuracion");
    }

    public LiveData<String> getText() {
        return mText;
    }
}