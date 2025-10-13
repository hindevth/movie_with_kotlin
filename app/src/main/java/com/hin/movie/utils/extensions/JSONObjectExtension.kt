package com.hin.movie.utils.extensions

import org.json.JSONObject

fun JSONObject.toMap(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    val keys = keys()
    while (keys.hasNext()) {
        val key = keys.next()
        val value = get(key)
        // Nếu value là JSONObject lồng nhau, chuyển tiếp
        map[key] = if (value is JSONObject) value.toMap() else value
    }
    return map
}
