package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class EditColegioInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_colegio_info)
        var id = intent.getStringExtra("colegioId")
        val colegioNombre = intent.getStringExtra("colegioNombre")
        val colegioInversion = intent.getDoubleExtra("colegioInversion", 0.0)
        val seccion = intent.getStringExtra("colegioSeccion")

        var textNombreColegio = this.findViewById<EditText>(R.id.tit_nombre_colegio)
        var textInversiones = this.findViewById<EditText>(R.id.tit_inversion)
        var textSeccion = this.findViewById<EditText>(R.id.tit_seccion)


        textNombreColegio.setText(colegioNombre)
        textInversiones.setText(colegioInversion.toString())
        textSeccion.setText(seccion)

        val btnSaveData = this.findViewById<Button>(R.id.btn_save)
        btnSaveData.setOnClickListener {


            if (!checkChanges(
                    colegioNombre,
                    colegioInversion,
                    seccion,
                    textNombreColegio.text.toString(),
                    textInversiones.text.toString().toDouble(),
                    textSeccion.text.toString()
                )
            ) {

                if (id == null) {
                    id = Date().time.toString()
                }
                saveData(
                    id!!,
                    textNombreColegio.text.toString(),
                    textInversiones.text.toString().toDouble(),
                    textSeccion.text.toString()
                )
            }
            irActividad(ColegioListActivity::class.java)
        }


    }

    private fun saveData(
        id: String,
        textNombreColegio: String,
        textInversiones: Double,
        textSeccion: String
    ) {
        val db = Firebase.firestore
        val colegio = db.collection("colegio")

        val data1 = hashMapOf(
            "id" to id,
            "nombreColegio" to textNombreColegio,
            "inversion" to textInversiones,
            "seccion" to textSeccion,
           )
        colegio.document(id).set(data1)
    }

    private fun checkChanges(
        colegioNombre: String?,
        colegioInversion: Double,
        seccion: String?,
        textNombreColegio: String,
        textInversiones: Double,
        textSeccion: String
    ): Boolean {
        return (colegioNombre == textNombreColegio && colegioInversion == textInversiones && seccion == textSeccion)
    }


    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }


}