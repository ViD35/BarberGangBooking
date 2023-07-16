package vid35.dev.barbergangbooking.Common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vid35.dev.barbergangbooking.Model.Admin;
import vid35.dev.barbergangbooking.Model.Cita;
import vid35.dev.barbergangbooking.Model.TimeSlot;
import vid35.dev.barbergangbooking.Model.User;

public class Common {
    public static final String KEY_CHECK_IF_BOOKED = "CHECK_IF_BOOKED";
    public static final String KEY_ENABLE_BUTTON_SIGUIENTE = "ENABLE_BUTTON_SIGUIENTE";
    public static final String KEY_BARBER_SELECTED = "BARBER_SELECTED";
    public static final String KEY_STEP = "STEP";
    public static final int TIME_SLOT_TOTAL = 16;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Admin currentBarber;
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static int step = 0;
    public static int currentTimeSlot=-1;
    public static Calendar currentDate=Calendar.getInstance();
    public static SimpleDateFormat simpleFormatDate = new SimpleDateFormat("dd_MM_yy");

    public static String convertTimeSlotToString(int slot) {
        switch (slot){
            case 0:
                return "12:00pm-12:30pm";
            case 1:
                return "12:30pm-1:00pm";
            case 2:
                return "1:00pm-1:30pm";
            case 3:
                return "1:30pm-2:00pm";
            case 4:
                return "2:00pm-2:30pm";
            case 5:
                return "2:30pm-3:00pm";
            case 6:
                return "3:00pm-3:30pm";
            case 7:
                return "3:30pm-4:00pm";
            case 8:
                return "4:00pm-4:30pm";
            case 9:
                return "4:30pm-5:00pm";
            case 10:
                return "5:00pm-5:30pm";
            case 11:
                return "5:30pm-6:00pm";
            case 12:
                return "6:00pm-6:30pm";
            case 13:
                return "6:30pm-7:00pm";
            case 14:
                return "7:00pm-7:30pm";
            case 15:
                return "7:30pm-8:00pm";
            default:
                return "Closed";
        }
    }
}
