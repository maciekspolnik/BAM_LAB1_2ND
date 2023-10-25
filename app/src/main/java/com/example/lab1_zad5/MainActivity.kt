package com.example.lab1_zad5

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

class MainActivity : AppCompatActivity() {

    val PERMISION_NAME = "ms.projects.lab1.permission.READ_USER_DATA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                PERMISION_NAME
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(PERMISION_NAME), 101)
        } else {
            getDataFromMainApp()
        }
    }

    private fun getDataFromMainApp() {
        try {
            val cursor: Cursor? = contentResolver.query(
                Uri.parse("content://ms.projects.lab1.provider/user_data"),
                null,
                null,
                null,
                null
            )

            cursor?.let {
                if (
                    it.getColumnIndex("id") != -1 &&
                    it.getColumnIndex("username") != -1 &&
                    it.getColumnIndex("number") != -1
                ) {
                    while (it.moveToNext()) {
                        Log.d(

                            "Query result",
                            "ID: ${it.getInt(it.getColumnIndex("id"))}  " +
                                    "USERNAME: ${it.getString(it.getColumnIndex("username"))}  " +
                                    "NUMBER: ${it.getInt(it.getColumnIndex("number"))}"
                        )
                    }
                } else {
                    Log.e("Query result", "Oczekiwane kolumny nie zostały znalezione w kursorze.")
                }
            }
            cursor?.close()
        } catch (e: SecurityException) {
            Log.e("Query result", "Brak uprawnienia lub ContentProvider nie został znaleziony", e)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getDataFromMainApp()
                    return
                }
                Log.e("Query result", "Brak uprawnień")
                return
            }

            else -> {
                Log.e("Query result", "Nieznany błąd")
                return
            }
        }
    }
}
