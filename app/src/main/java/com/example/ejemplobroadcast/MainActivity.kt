package com.example.ejemplobroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.example.ejemplobroadcast.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    //Variables

    private lateinit var binding: ActivityMainBinding
    private var contCambioTiempo: Int = 0
    private lateinit var tts: TextToSpeech

    //variable que sriva para configurar un broadcastReceiver
    //en este caso particular para comunicarse con el sistema
    //y saber si esta activo o no el modo avion

    private val getAirplaneMode = object: BroadcastReceiver(){

        //Al momento de configurar un BroadcastReceiver
        //es fundamental que ustedes sobreescriban el metodo
        //llamado onReceive -> posibilidad de recibir informacion
        //del evento del sistema y ustedes definen la logica
        // que desean aplicar a partir de ese evento y esa informacion

        override fun onReceive(context: Context?, intent: Intent?) {
            val airplaneMode = intent?.getBooleanExtra("state",false)
            airplaneMode?.let {
                val mensaje = if (it) "Modo Avión Activo" else "Modo avión desactivado"
                binding.tvAirplaneMode.text = mensaje
                speakMessage(mensaje)
            }
        }
    }

    //Configurar un BroadcastReceiver referido a tratar
    //los cambios de tiempo en el sistema -> Android
    //a esto se conoce como Time Tick
    /*private val getTimeChange = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                contCambioTiempo++
                val message = "el tiempo ha cambiado $contCambioTiempo veces"
                binding.tvTimeTick.text = message
                speakMessage(message)
            }catch (e: Exception){
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }*/

    //Crear una variable para configurar el Broadcast que nos
    //permitirá comunicarse con el servicio de Wifi...
    //Para manejar el wifi se va a usuar una clase propia que
    //alberga todas las configuraciones y tratamientos para este servicio
    //es un controlador del servicio Wifi a este se lo conoce como
    //WifiManager

    private val getWifiMode = object :BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //EXTRA_WIFI_STATE -> llave del registro temporal del datos del estado del wifi
            //Para el wifi el valor enterto que envia, su valor por defecto
            //tiene que referir a que no puede resolver el servicio y este es desconocido
            val wifiMode = intent?.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
            wifiMode?.let{
                val message = when(it){
                    WifiManager.WIFI_STATE_ENABLED -> "Wifi Habilitado"
                    WifiManager.WIFI_STATE_DISABLED -> "Wifi Deshabilitado"
                    WifiManager.WIFI_STATE_UNKNOWN -> "Erro en el servicio de Wifi"
                    else -> "Comprate un nuevo celular"
                }
                binding.tvWifiState.text = message
                speakMessage(message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tts = TextToSpeech(this,this)
        contCambioTiempo = 0
    }

    override fun onStart(){
        super.onStart()
        //registrar su BroadcastReceiver
        registerReceiver(getAirplaneMode, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        //registerReceiver(getTimeChange, IntentFilter(Intent.ACTION_TIME_TICK))
        registerReceiver(getWifiMode, IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(getAirplaneMode)
        //unregisterReceiver(getTimeChange)
        unregisterReceiver(getWifiMode)
    }

    private fun speakMessage(msg:String) {
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        //por defecto, el comando de voz está en ingles
        val respuesta = if (status == TextToSpeech.SUCCESS){
            tts.language = Locale.US
            "Todo ha salido bien"
        } else "Algo ha fallado, pruebe más tarde"
        Toast.makeText(this, respuesta, Toast.LENGTH_LONG).show()
    }
}