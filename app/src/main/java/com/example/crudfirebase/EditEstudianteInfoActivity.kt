package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class EditEstudianteInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_estudiante_info)
        var id = intent.getStringExtra("colegioId")
        var estudianteId = intent.getStringExtra("estudianteId")
        var nombre = intent.getStringExtra("estudianteNombre")
        var estatura = intent.getDoubleExtra("estudianteEstatura", 0.0)
        var fechaNacimiento = intent.getStringExtra("estudianteNacimiento")
        var bloque = intent.getStringExtra("estudianteBloque")


        var textNombre = this.findViewById<EditText>(R.id.tit_nombre_estudiante)
        textNombre.setText(nombre)
        var textEstatura = this.findViewById<EditText>(R.id.tit_estatura)
        textEstatura.setText(estatura.toString())
        val datePicker = findViewById<DatePicker>(R.id.date_picker_nacimiento)

        if(fechaNacimiento != null){
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdf.parse(fechaNacimiento)
            val calendar = Calendar.getInstance()
            calendar.time = date

            datePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }

        var textFechaNacimiento: String = ""
        var year = datePicker.year
        var month = datePicker.month
        var dayOfMonth = datePicker.dayOfMonth
        var textCalendar = Calendar.getInstance()
        textCalendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        textFechaNacimiento = dateFormat.format(textCalendar.time)
        datePicker.init(
            year,
            month,
            dayOfMonth,
            DatePicker.OnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                val textCalendar = Calendar.getInstance()
                textCalendar.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                textFechaNacimiento = dateFormat.format(textCalendar.time)
            }
        )

        var textBloque = this.findViewById<EditText>(R.id.tit_bloque)
        textBloque.setText(bloque)


        val btnGuardarEstudiante = this.findViewById<Button>(R.id.btn_guardar_estudiante)
        btnGuardarEstudiante.setOnClickListener {
            if (!checkChange(
                    nombre,
                    estatura,
                    fechaNacimiento,
                    bloque,
                    textNombre.text.toString(),
                    textEstatura.text.toString(),
                    textFechaNacimiento,
                    textBloque.text.toString()

                )
            ) {

                if (estudianteId == null) {
                    estudianteId = Date().time.toString()
                }
                saveData(
                    id!!,
                    estudianteId!!,
                    textNombre.text.toString(),
                    textEstatura.text.toString().toDouble(),
                    textFechaNacimiento,
                    textBloque.text.toString()
                )
            }
            abrirActividadConParametros(EstudianteListActivity::class.java, it, id!!)

        }


    }

    private fun saveData(
        id: String,
        estudianteId: String,
        textNombre: String,
        textEstatura: Double,
        textFechaNacimiento: String,
        textBloque: String
    ) {

        val db = Firebase.firestore
        val users = db.collection("colegio")
        val estudiantesCollectionsRef = users.document(id!!).collection("estudiante")
        val dataEstudiante = hashMapOf(
            "id" to estudianteId,
            "nombre" to textNombre,
            "estatura" to textEstatura,
            "fechaNacimiento" to textFechaNacimiento,
            "bloque" to textBloque
        )
        estudiantesCollectionsRef.document(estudianteId).set(dataEstudiante)

    }

    private fun checkChange(
        nombre: String?,
        estatura: Double,
        fechaNacimiento: String?,
        bloque: String?,
        textNombre: String,
        textEstatura: String,
        textFechaNacimiento: String,
        textBloque: String
    ): Boolean {
        return (nombre == textNombre && estatura == textEstatura.toDouble() && fechaNacimiento == textFechaNacimiento && bloque == textBloque)
    }

    private val contenidoIntentExplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            if (result.data != null) {
                val data = result.data
                Log.i("Intente-epn", "${data?.getStringExtra("nombreModificado")}")
            }
        }
    }

    private fun abrirActividadConParametros(clase: Class<*>, it: View?, idColegio: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("colegioId", idColegio)
        contenidoIntentExplicito.launch(intent)
    }


    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}