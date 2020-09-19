package com.example.mybus_driver.Common;

import com.example.mybus_driver.Model.CustomerLocation;
import com.example.mybus_driver.Model.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public
class common {

    public static final String USER_REF = "Users" ;

    public static final String SHIPPING_ORDER_DATA = "ShippingData" ;
    public static final String TRIP_START ="Trip" ;
    public static User user;
    public static CustomerLocation user2;

    public static float getBearing(LatLng being, LatLng end) {
        double lat = Math.abs(being.latitude-end.latitude);
        double lng = Math.abs(being.longitude - end.longitude);

        if(being.latitude < end.latitude && being.longitude < end.longitude)

            return (float)(Math.toDegrees(Math.atan(lng/lat)));

        else if(being.latitude >= end.latitude && being.longitude < end.longitude)

            return (float)((90 - Math.toDegrees(Math.atan(lng/lat)))+90);

        else if (being.latitude >= end.latitude && being.longitude >= end.longitude)

            return (float)(Math.toDegrees(Math.atan(lng/lat))+180);

        else if(being.latitude < end.latitude && being.longitude >= end.longitude)

            return (float)((90 - Math.toDegrees(Math.atan(lng/lat)))+270);

        return -1;
    }

    public static List<LatLng> decodePoly(String encoded) {
        List poly = new ArrayList();
        int index = 0,len=encoded.length();
        int lat = 0,lng = 0;
        while (index<len)
        {
            int b,shift=0,result=0;
            do
            {
                b=encoded.charAt(index++)-63;
                result |= (b & 0x1f) << shift;
                shift+=5;
            }while (b >= 0x20);
            int dlat= ((result & 1) != 0 ? ~(result >> 1 ) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do{
                b = encoded.charAt(index++)-63;
                result |= (b & 0x1f) << shift;
                shift +=5;
            }while (b >= 0x20);

            int dlng = ((result & 1)!=0 ? ~(result >> 1): (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double)lat / 1E5)),
                    (((double)lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
