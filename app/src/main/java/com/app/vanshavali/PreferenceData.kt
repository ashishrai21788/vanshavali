package com.app.vanshavali

import android.content.Context
import android.content.SharedPreferences

class PreferenceData(val context: Context) {

    private var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    private var instance: PreferenceData = this

    init {
        val prefsFile = context.getPackageName()
        sharedPreferences = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }
    fun getPrefsHelper(): SharedPreferences {
        return sharedPreferences
    }


    fun delete(key: String) {
        if (sharedPreferences.contains(key)) {
            editor.remove(key).commit()
        }
    }

    fun savePref(key: String, value: Any?) {
        delete(key)

        if (value is Boolean) {
            editor.putBoolean(key, (value as Boolean?)!!)
        } else if (value is Int) {
            editor.putInt(key, (value as Int?)!!)
        } else if (value is Float) {
            editor.putFloat(key, (value as Float?)!!)
        } else if (value is Long) {
            editor.putLong(key, (value as Long?)!!)
        } else if (value is String) {
            editor.putString(key, value as String?)
        } else if (value is Enum<*>) {
            editor.putString(key, value.toString())
        } else if (value != null) {
            throw RuntimeException("Attempting to save non-primitive preference")
        }

        editor.commit()
    }


    /*
      status "
      rideAccepted = 5
      rideArrived = 4
      rideBegin = 3
      rideDroped = 1, 2
     */
    fun getJourneyStatus(): Int {
        return sharedPreferences.getInt("journeyStatus", 0)
    }

    fun setJourneyStatus(status: Int) {
        editor.putInt("journeyStatus", status)
        editor.commit()
    }


    fun  getPref(key: String): String? {
        return sharedPreferences.getAll().get(key) as String
    }

    fun <T> getPref(key: String, defValue: T): T {
        val returnValue = sharedPreferences.getAll().get(key) as T
        return returnValue ?: defValue
    }

    fun isPrefExists(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    fun clearAllPref() {
        editor.clear()
        editor.commit()

    }

    fun setMyServicelatt(latt: String) {
        editor.putString("myServiceLatti", latt)
        editor.commit()
    }

    fun getMyServicelatt(): Double {
        val driverCurrentLongiStr = sharedPreferences.getString("myServiceLatti", "0.0")
        var driverCurrentLatti = 0.0
        try {
            driverCurrentLatti = java.lang.Double.parseDouble(driverCurrentLongiStr)
        } catch (e: NumberFormatException) {
        }

        return driverCurrentLatti
    }

    fun setMyServicelong(longi: String) {
        editor.putString("myServiceLongi", longi)
        editor.commit()
    }

    fun getMyServicelong(): Double {
        val driverCurrentLongiStr = sharedPreferences.getString("myServiceLongi", "0.0")
        var driverCurrentLongi = 0.0
        try {
            driverCurrentLongi = java.lang.Double.parseDouble(driverCurrentLongiStr)
        } catch (e: NumberFormatException) {
        }

        return driverCurrentLongi
    }

    fun setDistanceInDouble(distance: String) {
        editor.putString("DistanceDouble", distance)
        editor.commit()
    }

    fun getDistanceInDouble(): Double {
        val distancestr = sharedPreferences.getString("DistanceDouble", "0.0")
        var distanceDouble = 0.0
        try {
            distanceDouble = java.lang.Double.parseDouble(distancestr)
        } catch (e: NumberFormatException) {
        }

        return distanceDouble
    }



    fun setDistance(distance: String) {
        editor.putString("Distance", distance)
        editor.commit()
    }

    fun getDistance(): Double {
        val distancestr = sharedPreferences.getString("Distance", "0.0")
        var distanceDouble = 0.0
        try {
            distanceDouble = java.lang.Double.parseDouble(distancestr)
        } catch (e: NumberFormatException) {
        }

        return distanceDouble
    }

    fun setIsAgreementAccepted(isagreementAccecpted: String){
        editor.putString("isAgreementAccepted", isagreementAccecpted)
        editor.commit()
    }

    fun getIsAgreementAccepted(): String? {
        return sharedPreferences.getString("isAgreementAccepted", "false")
    }

    fun setCategoryId(categoryId: String){
        editor.putString("categoryId", categoryId)
        editor.commit()
    }

    fun getCategoryId(): String? {
        return sharedPreferences.getString("categoryId", "0")
    }
    fun setCategoryName(categoryName: String){
        editor.putString("categoryName", categoryName)
        editor.commit()
    }

    fun getCategoryName(): String? {
        return sharedPreferences.getString("categoryName", "")
    }
    fun setCategoryDetails(categoryDetail: String){
        editor.putString("categoryDetail", categoryDetail)
        editor.commit()
    }

    fun getCategoryDetails(): String? {
        return sharedPreferences.getString("categoryDetail", "")
    }
    fun setCategoryImage(categoryImage: String){
        editor.putString("categoryImage", categoryImage)
        editor.commit()
    }

    fun getCategoryImage(): String? {
        return sharedPreferences.getString("categoryImage", "")
    }
}