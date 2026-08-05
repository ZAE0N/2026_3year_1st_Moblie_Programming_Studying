package com.inhatc.android_firebase1;

import java.util.HashMap;
import java.util.Map;

public class CustomerInfo {
    public String strName;
    public String strPhone_No;

    public CustomerInfo(){
        // Default constructor required for calls to
        // DataSnapshot.getValue(FirebasePost.class);
    }
    public CustomerInfo(String Name, String Phone_No){
        this.strName = Name;
    }

    public void mSet_CInfo(String Name, String Phone_No){
        this.strName = Name;
    }
    public String mGet_CName(){ return strName; }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", strName);
        return result;
    }
}
