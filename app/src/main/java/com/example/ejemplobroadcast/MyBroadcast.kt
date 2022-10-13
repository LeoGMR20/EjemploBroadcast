package com.example.ejemplobroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.ejemplobroadcast.databinding.ActivityBatteryBinding
import kotlin.math.round

//Para que esta clase se comporte como un
//BroadcastReceiver debes extender o heredar
//una clase abstracta que ustedes ya han utilizado
//3 veces en el ejercicio anterior
class MyBroadcast(
    private val bindingObject: ActivityBatteryBinding
): BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            Intent.ACTION_BATTERY_CHANGED -> {
                showBatteryLevel(intent)
                batteryState(intent)
            }
            Intent.ACTION_BATTERY_LOW -> evaluateLowBattery(intent, context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun evaluateLowBattery(intent: Intent?, context: Context?) {
        //El intent que resuelve el tema de la batería baja
        //maneja un dato en su registro temporal de tipo booleano

        val lowBattery = intent?.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false)
        lowBattery?.let {
            bindingObject.tvBatteryMessage.text = "Alerta Batería Baja"
            configureScreenBrightness(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun configureScreenBrightness(context: Context?) {
        //No pueden usar recursos de hardware y configuraciones del sisteam
        //a libre gusto, si no hay permisos o no están habilitadas las funciones
        //no pueden ajustar estos métodos
        if (hasWriteSettingsEnabled(context)){
            //1: El nivel de brillo se maneja a de 0 a 255
            //2: por defecto, el brillo se ajusta automaticamente, por ende primero hay que cambiar a modo manual
            val screenBrightnessLevel = 20
            Settings.System.putInt(
                context?.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            )
            Settings.System.putInt(
                context?.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                screenBrightnessLevel
            )
            val percentage = screenBrightnessLevel.toDouble() / 255
            Toast.makeText(context,"Nivel ${round(percentage * 100)}%",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No puedes configurar los settings", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showBatteryLevel(intent: Intent?) {
        //Cuando se trata del nivel de la batería, el servicio del sistema
        //les envia a traves de un intent un valor entero que representa
        //el porcentaje de bateria restante
        //El tema de la bateria generalmente es gestionado y configurado
        //en una clase que controla todos estos aspectos y se llama BatteryManager

        val batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        batteryLevel?.let {
            val percentage = "$it% bateria"
            bindingObject.tvBatteryPercentage.text = percentage
            bindingObject.pbBatteryLevel.progress = it
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasWriteSettingsEnabled(context: Context?): Boolean {
        return Settings.System.canWrite(context)
    }

    private fun batteryState(intent: Intent?){
        val batteryState = intent?.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
        batteryState?.let {
            var health = when(it){
                BatteryManager.BATTERY_HEALTH_DEAD -> "Salud de Batería: muerta"
                BatteryManager.BATTERY_HEALTH_COLD -> "Salud de la batería: fría"
                BatteryManager.BATTERY_HEALTH_GOOD -> "Salud de la batería: en buen estado"
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Batería: sobrecalentada"
                BatteryManager.BATTERY_HEALTH_UNKNOWN -> "Salud de la batería: desconocida"
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Salud de la batería: con sobrevoltaje"
                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Algo falló en la salud de la batería"
                else -> "Desconocido"
            }
            bindingObject.tvBatteryHealth.text = health
        }
    }
}