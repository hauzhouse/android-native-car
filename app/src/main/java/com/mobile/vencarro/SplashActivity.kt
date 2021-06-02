package com.mobile.vencarro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportActionBar?.hide() // ocultar a barra de titulo

        val myHandler = Handler()
        // aguarda 5 segundos para executar a funcao finaizar()
        myHandler.postDelayed(Runnable {

            finalizar()

        }, 5000)
    }


    fun finalizar() {

        val toMain = Intent(this, MainActivity::class.java)

        startActivity(toMain) // acionar a MainActivity (assincrono)

        finish() // finaliza a SplashActivity

    }
}
