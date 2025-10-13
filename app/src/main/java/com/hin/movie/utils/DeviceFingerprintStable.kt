package com.hin.movie.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import org.json.JSONObject
import java.security.MessageDigest

object DeviceFingerprintStable {

    @SuppressLint("HardwareIds")
    fun getDeviceJson(context: Context): JSONObject {
        val androidId = try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }

        val manufacturer = Build.MANUFACTURER ?: "unknown"
        val model = Build.MODEL ?: "unknown"
        val sdk = Build.VERSION.SDK_INT.toString()
        val release = Build.VERSION.RELEASE ?: "unknown"

        val memoryTotal = try {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            am?.getMemoryInfo(memInfo)
            memInfo.totalMem
        } catch (e: Exception) {
            -1L
        }

        val storageTotal = try {
            val stat = StatFs(Environment.getDataDirectory().path)
            stat.blockCountLong * stat.blockSizeLong
        } catch (e: Exception) {
            -1L
        }

        val jo = JSONObject()
        jo.put("android_id", androidId)
        jo.put("manufacturer", manufacturer)
        jo.put("model", model)
        jo.put("sdk", sdk)
        jo.put("release", release)
        jo.put("memory_total_bytes", memoryTotal)
        jo.put("storage_total_bytes", storageTotal)

        // fingerprint SHA-256 từ các trường ổn định
        jo.put("fingerprint", generateFingerprint(androidId, manufacturer, model, sdk, release, memoryTotal, storageTotal))

        return jo
    }

    // SHA-256 fingerprint
    private fun generateFingerprint(
        androidId: String,
        manufacturer: String,
        model: String,
        sdk: String,
        release: String,
        memory: Long,
        storage: Long
    ): String {
        val input = "$androidId;$manufacturer;$model;$sdk;$release;$memory;$storage"
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(input.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }
}
