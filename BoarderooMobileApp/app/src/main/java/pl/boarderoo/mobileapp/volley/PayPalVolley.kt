package pl.boarderoo.mobileapp.volley

import android.content.Context
import android.util.Base64
import android.util.Log
import com.android.volley.NoConnectionError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import java.math.RoundingMode
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object PayPalVolley {
    private val clientId =
        "AZ8pZw6s44qSnIantY7aDEjGZ0mG8oMwKWLTtIpzub6boKxbGSk0OFSEfw5usTA2HfHU7me4daTaw23c"
    private val clientSecret =
        "EF0wmIvMIGKKiRhxYSXIBrvqw4r_z0PhyA0xCha_kNhjYGbTDuSvfsiZYea9OiUkxyaT0SHmY0QJ74pR"
    private var requestQueue: RequestQueue? = null
    private val gson = Gson()

    fun createRequestQueue(context: Context) {
        requestQueue = Volley.newRequestQueue(context)
    }

    suspend fun getAuthToken(): String? {
        try {
            val credentials = "$clientId:$clientSecret"
            val base64Auth = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
            val headers = mapOf(
                "Content-Type" to "application/x-www-form-urlencoded",
                "Authorization" to "Basic $base64Auth"
            )
            val params = mapOf(
                "grant_type" to "client_credentials"
            )
            val result = payPalRequest("v1/oauth2/token", headers, params).await()
            return result!!.get("token_type").asString + " " + result.get("access_token").asString
        } catch (_: Exception) {
            return null
        }
    }

    fun getOrderId(
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accessToken = getAuthToken()
                val headers = mapOf(
                    "Content-Type" to "application/json",
                    "Authorization" to accessToken!!
                )
                val bodyRaw = """{
                    "intent": "CAPTURE|AUTHORIZE",
                    "purchase_units": [
                        {
                            "amount": {
                                "currency_code": "PLN",
                                "value": "${AppRuntimeData.order!!.price.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()}"
                            }
                        }
                    ]
                }"""
                val body = gson.fromJson(bodyRaw, JsonObject::class.java)
                val result = payPalRequest("v2/checkout/orders", headers, body = body).await()
                onSuccess(result!!.get("id").asString)
            } catch (_: Exception) {

            }
        }
    }

    fun captureOrder(
        orderId: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accessToken = getAuthToken()
                val headers = mapOf(
                    "Content-Type" to "application/json",
                    "Authorization" to accessToken!!
                )
                val result = payPalRequest("v2/checkout/orders/${orderId}/capture", headers).await()
                onSuccess(result!!.getAsJsonArray("purchase_units").get(0).asJsonObject.getAsJsonObject("payments").getAsJsonArray("captures").get(0).asJsonObject.get("id").asString)
            } catch (_: Exception) {

            }
        }
    }

    private suspend fun payPalRequest(
        endpoint: String,
        headers: Map<String, String>? = null,
        params: Map<String, String>? = null,
        body: JsonObject? = null
    ): Deferred<JsonObject?> {
        return CoroutineScope(Dispatchers.IO).async {
            Log.println(Log.INFO, "INFO", "Starting VyOS Request")
            val url = "https://api-m.sandbox.paypal.com/$endpoint"
            val response = suspendCoroutine<String> { continuation ->
                val stringRequest = object : StringRequest(
                    Method.POST, url,
                    Response.Listener { response ->
                        Log.println(Log.INFO, "INFO", response)
                        continuation.resume(response)
                    },
                    Response.ErrorListener { error ->
                        Log.println(Log.WARN, "VOLLEY_DEBUG", error.toString())
                        if (error is TimeoutError || error is NoConnectionError) {
                            continuation.resumeWithException(Exception("Timeout - server unreachable"))
                        } else {
                            Log.println(
                                Log.ERROR,
                                "VOLLEY_DEBUG",
                                error.networkResponse.statusCode.toString()
                            )
                            continuation.resumeWithException(Exception("Network response code: ${error.networkResponse.statusCode}"))
                        }
                    }) {
                    override fun getParams(): MutableMap<String, String>? {
                        return params?.let {
                            val _params: MutableMap<String, String> = HashMap()
                            _params.putAll(it) // Copy all key-value pairs from the input params
                            _params
                        } ?: super.getParams()
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        return headers?.let {
                            val _headers: MutableMap<String, String> = HashMap()
                            _headers.putAll(it) // Copy all key-value pairs from the input headers
                            _headers
                        } ?: super.getHeaders()
                    }

                    override fun getBody(): ByteArray {
                        if(body == null) return super.getBody()
                        return gson.toJson(body).toByteArray(Charsets.UTF_8)
                    }
                }
                requestQueue!!.add(stringRequest)
            }
            return@async gson.fromJson(response, JsonObject::class.java)
        }
    }
}