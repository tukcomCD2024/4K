package com.example.front_end_android

import android.util.Log
import com.example.front_end_android.models.MessageModel
import com.example.front_end_android.util.NewMessageInterface
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class SocketRepository(private val messageInterface: NewMessageInterface) {
    private var webSocket: WebSocketClient?= null
    private var userName:String?=null
    private val TAG = "SocketRepository"
    private val gson = Gson()

    fun sendMessageToSocket(message:MessageModel){
        try{
            webSocket?.send(Gson().toJson(message))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun initSocket(username:String){
        userName = username
        //uri에는 실제 url 주소가 들어가야함
        webSocket = object :WebSocketClient(URI("ws://192.168.219.105:3000")){
            override fun onOpen(handshakedata: ServerHandshake?) {
                sendMessageToSocket(
                    MessageModel(
                    "store_user",username,null,null
                )
                )
            }

            override fun onMessage(message: String?) {
                try {
                    messageInterface.onNewMessage(gson.fromJson(message,MessageModel::class.java))

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose: $reason")
            }

            override fun onError(ex: Exception?) {
                Log.d(TAG, "onError: $ex")
            }

        }
        webSocket?.connect()
    }
}