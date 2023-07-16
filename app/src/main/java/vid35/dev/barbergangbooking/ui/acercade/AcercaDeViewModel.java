package vid35.dev.barbergangbooking.ui.acercade;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AcercaDeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AcercaDeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragmento de informacion");
    }

    public LiveData<String> getText() {
        return mText;
    }
}