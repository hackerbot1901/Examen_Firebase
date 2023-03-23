package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

class ColegioListActivity : AppCompatActivity() {


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


    private lateinit var userRecyclerView: RecyclerView
    private lateinit var colegioArrayList: ArrayList<Colegio>
    private lateinit var adaptador: AdapterColegio


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colegio_list)

        userRecyclerView = findViewById(R.id.userListRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        colegioArrayList = arrayListOf<Colegio>()
        adaptador = AdapterColegio(colegioArrayList, contenidoIntentExplicito)
        userRecyclerView.adapter = adaptador

        consultarDocumentos()

        val btnCreateData = this.findViewById<Button>(R.id.btn_crear_estudiante)
         btnCreateData.setOnClickListener {
             irActividad(EditColegioInfoActivity::class.java)
         }






    }





    private fun consultarDocumentos(){
        val db = Firebase.firestore
        val colegioRef = db.collection("colegio")
        limpiarArreglo()
        colegioRef.get().addOnSuccessListener { result ->
            for (document in result) {
                val colegio = Colegio(
                    document.get("id") as String?,
                    document.get("nombreColegio") as String?,
                    (document.get("inversion") as Number?)?.toDouble(),
                    document.get("seccion") as String?
                )
                colegioArrayList.add(colegio)
            }
            // Actualizar la lista de usuarios en la vista
            adaptador.colegioList = colegioArrayList
            adaptador.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.d(null, "Error al obtener colegio", exception)
        }
    }



    private fun limpiarArreglo() {
        this.colegioArrayList.clear()
    }

    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }





}