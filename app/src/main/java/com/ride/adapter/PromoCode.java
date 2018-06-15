package com.ride.adapter;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by grepixinfotech on 17/04/17.
 */

public class PromoCode {
    private String promo_id;
    private String promo_code;
    private String promo_type;
    private float promo_value;
    private String promo_status;
    private String promo_created;
    private String promo_modified;
    private float percent;

    public static PromoCode parsePromoCode(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            return new Gson().fromJson(jsonArray.getJSONObject(0).toString(), PromoCode.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPromoCode() {
        return promo_code;
    }

    public String getPromoId() {
        return promo_id;
    }

    public float calucalateAmtByPromoCode(float tripAmmount) {
        if (isFixed()) {
            tripAmmount -= promo_value;
        } else {
            tripAmmount = (float) ((tripAmmount * promo_value) / 100.0f);
        }
        return tripAmmount;
    }

    //        {
//            "status": "OK",
//                "code": 200,
//                "message": null,
//                "next_offset": 0,
//                "last_offset": 0,
//                "response": [
//            {
//                "promo_id": "2",
//                    "promo_code": "DCP",
//                    "promo_type": "Percentage",
//                    "promo_value": "5",
//                    "promo_status": "1",
//                    "promo_created": "2016-11-08 10:38:47",
//                    "promo_modified": "2016-11-08 10:38:47",
//                    "percent": 0
//            }
//            ]
//        }
//        {
//            "status": "OK",
//                "code": 200,
//                "message": null,
//                "next_offset": 0,
//                "last_offset": 0,
//                "response": [
//            {
//                "promo_id": "1",
//                    "promo_code": "DC007",
//                    "promo_type": "Fixed Amt",
//                    "promo_value": "250",
//                    "promo_status": "1",
//                    "promo_created": "2016-11-08 10:38:47",
//                    "promo_modified": "2016-11-08 10:38:47",
//                    "percent": 60
//            }
//            ]
//        }
    public boolean isFixed() {
        return promo_type.equalsIgnoreCase("Fixed Amt");
    }

}
