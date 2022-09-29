package com.example.ejemplobroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ejemplobroadcast.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Variables

    private lateinit var binding: ActivityMainBinding

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
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart(){
        super.onStart()
        //registrar su BroadcastReceiver
        registerReceiver(getAirplaneMode, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(getAirplaneMode)
    }
}