package com.example.ejemplobroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

//Para que esta clase se comporte como un
//BroadcastReceiver debes extender o heredar
//una clase abstracta qwue ustedes ya han utilizado
//3 veces en el ejercicio anterior
class MyBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        TODO("Not yet implemented")
    }
}