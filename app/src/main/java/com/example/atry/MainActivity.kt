package com.example.atry

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.*
import androidx.appcompat.app.AppCompatActivity

import com.rvirin.onvif.onvifcamera.*

class MainActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {


    //private val _mediaPlayer: MediaPlayer? = null
  //  var _mediaPlayer: MediaPlayer? = null
    var _mediaPlayer: MediaPlayer? = null
    //private var _mediaPlayer: MediaPlayer? = null
    val USERNAME = "login"
    val PASSWORD = "camera"
    val RTSP_URL = "rtsp://192.168.1.140:5540/ch0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window: Window = window
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setBackgroundDrawableResource(android.R.color.black)
        setContentView(R.layout.activity_main)

        // Configure the view that renders live video.
        val surfaceView = findViewById<View>(R.id.surfaceView) as SurfaceView
        var _surfaceHolder = surfaceView.holder
        _surfaceHolder.addCallback(this)
        _surfaceHolder.setFixedSize(320, 240)




    }

    override fun onPrepared(mp: MediaPlayer?) {

        _mediaPlayer?.start();
    }

    override fun surfaceCreated(sh: SurfaceHolder) {
        val surfaceView = findViewById<View>(R.id.surfaceView) as SurfaceView
        var _surfaceHolder = surfaceView.holder
        _surfaceHolder.addCallback(this)
        _surfaceHolder.setFixedSize(320, 240)
        _mediaPlayer = MediaPlayer()
        _mediaPlayer!!.setDisplay(_surfaceHolder)

        val context: Context = applicationContext
        val headers: Map<String, String> = getRtspHeaders()
        val source: Uri = Uri.parse(RTSP_URL)

        try {
            // Specify the IP camera's URL and auth headers.
            _mediaPlayer!!.setDataSource(context, source, headers)

            // Begin the process of setting up a video stream.
            _mediaPlayer!!.setOnPreparedListener(this)
            _mediaPlayer!!.prepareAsync()
        } catch (e: Exception) {
        }
    }

    override fun surfaceChanged(sh: SurfaceHolder, f: Int, w: Int, h: Int) {

    }

    override fun surfaceDestroyed(sh: SurfaceHolder) {

        _mediaPlayer?.release()
    }

    private fun getRtspHeaders(): Map<String, String> {
        val headers: MutableMap<String, String> = HashMap()
        val basicAuthValue = getBasicAuthValue(USERNAME, PASSWORD)
        headers["Authorization"] = basicAuthValue
        return headers
    }

    private fun getBasicAuthValue(usr: String, pwd: String): String {
        val credentials = "$usr:$pwd"
        val flags: Int = Base64.URL_SAFE or Base64.NO_WRAP
        val bytes = credentials.toByteArray()
        return "Basic " + Base64.encodeToString(bytes, flags)
    }


}