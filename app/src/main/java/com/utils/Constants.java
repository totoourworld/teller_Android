package com.utils;

/**
 * Created by mradul on 03/06/14.
 */
public interface Constants {
    public interface Urls {
        // String URL_BASE = "http://35.160.185.249/Taxi1.2/index.php";
        //String URL_BASE = "http://35.160.185.249/HireTaxi/1.0.1/index.php";
        String URL_BASE = "http://34.217.9.8/xeniaAPI/1.0.1/index.php";
        String URL_USER_REGISTRATION = URL_BASE + "/userapi/registration?";
        String URL_USER_FB_REGISTRATION = URL_BASE + "/userapi/fbregistration?";
        String URL_USER_SIGN_IN = URL_BASE + "/userapi/login?";
        String URL_USER_FB_LOGIN = URL_BASE + "/userapi/fblogin?";
        String URL_RESET_PASSWORD = URL_BASE + "/userapi/forgetpassword?";

        String URL_USER_UPDEATE_PROFILE = URL_BASE + "/userapi/updateuserprofile?";
        String URL_DRIVER_UPDEATE_PROFILE = URL_BASE + "/driverapi/updatedriverprofile?";
        String URL_USER_GET_CATEGORY = URL_BASE + "/categoryapi/getcategories?";
        // String IMAGE_BASE_URL1 = "http://35.160.185.249/HireTaxi/images/originals/";
        String IMAGE_BASE_URL1 = "http://appicial.com/HireTaxi/images/originals/";


        String URL_USER_GET_TRIP = URL_BASE + "/tripapi/gettrips?";
        String URL_USER_GET_DRIVER_INFO = URL_BASE + "/driverapi/getnearbydriverlists?";
        //String IMAGE_BASE_URL = "http://35.160.185.249/HireTaxi/images/originals/";
        String IMAGE_BASE_URL = "http://appicial.com/HireTaxi/images/originals/";
        String IMAGE_BASE_URL_DRIVER = "http://appicial.com/HireTaxi/images/originals/";

        String URL_USER_CREATE_TRIP = URL_BASE + "/tripapi/save?";
        String URL_USER_UPDATE_TRIP = URL_BASE + "/tripapi/updatetrip?";
        String URL_PAYMENT_SAVE = URL_BASE + "/paymentapi/save?";

        String URL_USER_SEND_NOTIFICATION_ANDROID1 = "http://34.217.9.8/xeniaAPI/push/DriverAndroidPushNotification.php";
        // String URL_DRIVER_NOTIFICATION = "http://35.160.185.249/HireTaxi/push/DriverAndroidIosPushNotification.php";
        String URL_DRIVER_NOTIFICATION = "http://34.217.9.8/xeniaAPI/push/DriverAndroidIosPushNotification.php";

        String URL_USER_SEND_NOTIFICATION_IOS1 = "http://34.217.9.8/xeniaAPI/push/DriverPushNotification.php";
        String URL_USER_GET_DRIVER = URL_BASE + "/driverapi/getdrivers?";
        //  String URL_VAAIDATE_PROMO = "http://35.160.185.249/HireTaxi/1.0.1/index.php/promoapi/validatepromos";
        String URL_VAAIDATE_PROMO = "http://34.217.9.8/xeniaAPI/1.0.1/index.php/promoapi/validatepromos";
        String URL_TO_SHARE = "https://play.google.com/store/apps/details?id=com.rider.hire_me&hl=en";
        String URL_FOR_TERM_OF_USE = "info@appicial.com";
        String EMAIL_FOR_SUPPORT = "info@appicial.com";
        String GET_CONSTANTS_API = URL_BASE + "/constantapi/getconstants?";

    }

    public interface Keys {

        String EMAIL_KEY = "u_email";
        String PASSWORD_KEY = "u_password";
        String API_KEY = "api_key";
        String U_FName = "u_fname";
        String USER_ID = "user_id";
        String U_LName = "u_lname";
        String U_EMAIL = "u_email";
        String U_PASSWORD = "u_password";
        String U_NAME = "u_name";
        String U_PHONE = "u_phone";
        String U_ADDRESS = "u_address";
        String U_CITY = "u_city";
        String U_STATE = "u_state";
        String U_COUNTRY = "u_country";
        String U_ZIP = "u_zip";
        String U_LAT = "u_lat";
        String U_LNG = "u_lng";
        String U_DEVICE_TYPE = "u_device_type";
        String U_DEVICE_TOKEN = "u_device_token";
        String U_CREATED = "u_created";
        String U_MODIFIED = "u_modified";
        String IMAGE_ID = "image_id";
        String IS_SEND_EMAIL = "is_send_email";


		/*For Get Driver Api*/

        String DRIVER_ID = "driver_id";
        String LAT = "lat";
        String LNG = "lng";
        String CATAGORY = "category_id";

        String PRICE = "price";







		/*For Create Trip Api*/

        String SCHEDULED_DROP_LAT = "trip_scheduled_drop_lat";
        String SCHEDULED_DROP_LNG = "trip_scheduled_drop_lng";
        String SCHEDULED_PICK_LAT = "trip_scheduled_pick_lat";
        String SCHEDULED_PICK_LNG = "trip_scheduled_pick_lng";
        String SEARCH_RESULT_ADDR = "trip_search_result_addr";
        String SEARCH_ADDR = "trip_searched_addr";
        String TRIP_DATE = "trip_date";
        String TRIP_PICK_LOC = "trip_from_loc";
        String TRIP_DEST_LOC = "trip_to_loc";
        String TRIP_STATUS = "trip_status";
        String TRIP_PAY_AMOUNT = "trip_pay_amount";
        String TRIP_PAY_MODE = "trip_pay_mode";
        String TRIP_PAY_STATUS = "trip_pay_status";

        String TRIP_DISTANCE = "trip_distance";

/*For Driver Notification*/


        String DRIVER_DEVICE_TOKEN = "device_token";
        String MESSAGE = "message";

	/*	For Check Trip Status*/

        String TRIP_ID = "trip_id";
    }


    public interface Value {
        public final String MilesRange = "2";
    }

    public interface TripStatus {
        public final String ACCEPT = "accept";
        public final String ARRIVE = "arrive";
        public final String BEGIN = "begin";
        public final String END = "end";
        public final String REJECTED = "rejected";
        public final String DRIVER_CANCEL = "driver_cancel";
    }
    public interface Message{
        //TRIP MESSAGES
        public final String REQUEST = "Hey, You received a new Trip Request. Act Fast";
        public final String REJECTED = "Ohh, Your Trip Request has been cancelled. Try Again";
  /*      public final String ACCEPTED = "Wow, Your Trip Request has been confirmed. Get Ready";
        public final String END = "Your trip completed successfully. Thanks";
        public final String ARRIVE = "Hey, Cab will arrive soon. Be Ready";
        public final String BEGIN = "Your trip has been started. Good Luck";
        public final String DRIVER_CANCEL = "Driver has cancelled the Trip. Ask him";*/
         public final String ACCEPTED = "Your Trip Request has been confirmed. Get Ready";
        public final String END = "Your trip completed successfully. Thanks";
        public final String ARRIVE = "Hey, Cab will arrive soon. Be Ready";
        public final String BEGIN = "Your trip has been started. Good Luck";
        public final String DRIVER_CANCEL = "Driver has cancelled the Trip. Ask him";
        public final String PAID = "Payment done. Thanks";
        public final String CASH = "Please collect cash from Rider.";
        public final String PAYPAL = "User made the payment.";

    }

}
