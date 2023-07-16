package vid35.dev.barbergangbooking.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InicioViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InicioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Lista de barberos");
    }

    public LiveData<String> getText() {
        return mText;
    }
}