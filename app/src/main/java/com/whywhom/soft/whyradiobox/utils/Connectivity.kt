/**
 * Check device's network connectivity and speed
 * @author emil http://stackoverflow.com/users/220710/emil
 *
 */
package com.whywhom.soft.whyradiobox.utils

import android.content.Context
import android.net.*
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi


/**
 * Created by wuhaoyong on 30/08/20.
 */
object Connectivity {
    const val TYPE_MOBILE = 0
    const val TYPE_WIFI = 1
    const val TYPE_OTHER = 2
    const val TYPE_NONE = 3
    var currentConnectType = TYPE_MOBILE
    val TAG: String = Connectivity::class.java.name.toString()
    // Global variable used to store network state
    var isNetworkConnected = false
    /*
    You need to call the below method once. It register the callback and fire it when there is a change in network state.
    Here I used a Global Static Variable, So I can use it to access the network state in anyware of the application.
    */

    fun getCurrentConnectType(context: Context?): Int{
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return currentConnectType
        }
        if(isConnectedWifi(context)){
            return TYPE_WIFI
        } else if(isConnectedMobile(context)){
            return TYPE_MOBILE
        } else if(isConnected(context)){
            return TYPE_OTHER
        }
        return TYPE_NONE
    }

    // Network Check
    @RequiresApi(Build.VERSION_CODES.N)
    fun registerNetworkCallback(context: Context) {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder = NetworkRequest.Builder()
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isNetworkConnected = true // Global Static Variable
                }

                override fun onLost(network: Network) {
                    isNetworkConnected = false // Global Static Variable
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            Log.d(TAG, "onCapabilitiesChanged: 网络类型为wifi")
                            currentConnectType = TYPE_WIFI
                        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            Log.d(TAG, "onCapabilitiesChanged: 蜂窝网络")
                            currentConnectType = TYPE_MOBILE
                        } else {
                            Log.d(TAG, "onCapabilitiesChanged: 其他网络")
                            currentConnectType = TYPE_OTHER
                        }
                    }
                }
            }
            )
            isNetworkConnected = false
        } catch (e: Exception) {
            isNetworkConnected = false
        }
    }

    /**
     * the methods below is deprecated
     *
     */
    /**
     * Get the network info
     * @param context
     * @return
     */
    fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    fun isConnected(context: Context?): Boolean {
        val info: NetworkInfo? = context?.let { Connectivity.getNetworkInfo(it) }
        return info != null && info.isConnected
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @return
     */
    fun isConnectedWifi(context: Context?): Boolean {
        val info: NetworkInfo? = context?.let { Connectivity.getNetworkInfo(it) }
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @return
     */
    fun isConnectedMobile(context: Context?): Boolean {
        val info: NetworkInfo? = context?.let { Connectivity.getNetworkInfo(it) }
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    fun isConnectedFast(context: Context?): Boolean {
        val info: NetworkInfo? = context?.let { Connectivity.getNetworkInfo(it) }
        return info != null && info.isConnected && Connectivity.isConnectionFast(
            info.type,
            info.subtype
        )
    }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    fun isConnectionFast(type: Int, subType: Int): Boolean {
        return if (type == ConnectivityManager.TYPE_WIFI) {
            true
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            when (subType) {
                TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
                TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> true // ~ 400-1000 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_A -> true // ~ 600-1400 kbps
                TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
                TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
                TelephonyManager.NETWORK_TYPE_HSPA -> true // ~ 700-1700 kbps
                TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
                TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps
                TelephonyManager.NETWORK_TYPE_EHRPD -> true // ~ 1-2 Mbps
                TelephonyManager.NETWORK_TYPE_EVDO_B -> true // ~ 5 Mbps
                TelephonyManager.NETWORK_TYPE_HSPAP -> true // ~ 10-20 Mbps
                TelephonyManager.NETWORK_TYPE_IDEN -> false // ~25 kbps
                TelephonyManager.NETWORK_TYPE_LTE -> true // ~ 10+ Mbps
                TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                else -> false
            }
        } else {
            false
        }
    }
}