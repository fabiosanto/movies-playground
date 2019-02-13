package com.fabiosanto.movies

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

const val API_KEY = "933b65fca5ee88d5b921aa00f8d3e767"

class MovieDBRouter(method: Int, url: String, private val listener: (response: JSONArray) -> Unit, errorListener: Response.ErrorListener)
    : Request<JSONObject>(method, "https://api.themoviedb.org/3$url&api_key=$API_KEY", errorListener) {

    companion object {
        const val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
        const val DISCOVER = "/discover/movie?sort_by=popularity.desc"
        const val PAGE = "&page="
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        return try {
            val jsonString = String(
                    response.data,
                    Charset.defaultCharset())
            Response.success(
                    JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (je: JSONException) {
            Response.error(ParseError(je))
        }

    }

//    override fun getHeaders(): MutableMap<String, String> {
//        return HashMap<String, String>().apply {
//            this["Authorization"] = "Client-ID b1e6fe9774017a493f0005396a8010007a587e7320e247b6ec492b61931c2a33"
//        }
//    }

    override fun deliverResponse(response: JSONObject) {
        listener(response.getJSONArray("results"))
    }
}