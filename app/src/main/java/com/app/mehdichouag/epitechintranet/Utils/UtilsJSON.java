package com.app.mehdichouag.epitechintranet.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mehdichouag on 28/01/15.
 */
public class  UtilsJSON {

    public static String getStringFromJSON(JSONObject obj, String TAG) {
        if (obj.has(TAG)) {
            try {
                return String.valueOf(obj.get(TAG));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static double getDoubleFromJSON(JSONObject obj, String TAG) {
        if (obj.has(TAG)) {
            try {
                return obj.getDouble(TAG);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static int getIntFromJSON(JSONObject obj, String TAG) {
        if (obj.has(TAG)) {
            try {
                return (int) obj.get(TAG);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static Date getDateFromJSON(JSONObject object, String key) {
        if (object.has(key)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = formatter.parse(object.getString(key));
            } catch (ParseException | JSONException e) {
                e.printStackTrace();
            }
            return date;
        }
        return null;
    }
}
