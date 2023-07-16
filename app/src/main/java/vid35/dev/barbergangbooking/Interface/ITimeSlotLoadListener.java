package vid35.dev.barbergangbooking.Interface;

import java.util.List;

import vid35.dev.barbergangbooking.Model.TimeSlot;

public interface ITimeSlotLoadListener {
    void onTimeSlotLoadSuccess (List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed (String message);
    void onTimeSlotLoadEmpty ();
}
