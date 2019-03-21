package com.example.arafat.map04;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestClass {
    
    
    
    public static void main(String[] args) {
        String river = "[Address[addressLines=[0:\"Alurtol Road, Sylhet, Bangladesh\"],feature=Alurtol Road,admin=Sylhet Division,sub-admin=Sylhet District,locality=Sylhet,thoroughfare=Alurtol Road,postalCode=null,countryCode=BD,countryName=Bangladesh,hasLatitude=true,latitude=24.9113163,hasLongitude=true,longitude=91.9032062,phone=null,url=null,extras=null]]";
        Pattern pattern = Pattern.compile("0:\"(.*?), Sylhet");
        Pattern pattern2 = Pattern.compile("Alurtol Road, (.*?), Bangladesh");
        Pattern pattern3 = Pattern.compile("0:\"(.*?), Sylhet");
        Matcher matcher = pattern.matcher(river);
        Matcher matcher2 = pattern2.matcher(river);
        String roadName = "";
        String dist = "";
        String country = "";

        while (matcher.find()) {
            //System.out.println(matcher.group(1));
             roadName = matcher.group(1);
        }
        while (matcher2.find()) {
            dist = matcher2.group(1);
        }

        System.out.println(roadName + " " + dist);
        
    }
}
