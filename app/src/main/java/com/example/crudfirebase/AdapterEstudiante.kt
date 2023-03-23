package com.example.crudfirebase

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterEstudiante(
    var estudianteList: ArrayList<Estudiante>,
    private val contenidoIntentExplicito: ActivityResultLauncher<Intent>,
    val idColegio: String
) : RecyclerView.Adapter<AdapterEstudiante.MyViewHolderEstudiante>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderEstudiante {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.estudiante_item, parent, false)
        return MyViewHolderEstudiante(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolderEstudiante, position: Int) {
        val estudianteActual = estudianteList[position]
        holder.idEstudiante.text = estudianteActual.id
        holder.nombreEstudiante.text = estudianteActual.nombre
        holder.estatura.text = estudianteActual.estatura.toString()
        holder.bloque.text = estudianteActual.bloque
        holder.fechaNacimiento.text = estudianteActual.fechaNacimiento
        holder.btnEditarEstudiante.setOnClickListener {
            abrirActividadConParametros(EditEstudianteInfoActivity::class.java, estudianteActual, it, idColegio)
        }
        holder.btnEliminarEstudiante.setOnClickListener {

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Confirmar eliminación")
            builder.setMessage("Estás seguro que lo quieres eliminar?")
            builder.setPositiveButton("Si") { dialog, _ ->
                dialog.dismiss()
                deleteEstudiante(estudianteActual.id!!, idColegio)
                this.estudianteList.remove(estudianteActual)
                notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }

    }

    private fun deleteEstudiante(id: String, idColegio: String) {
        val db = Firebase.firestore
        val users = db.collection("colegio")
        val estudiantesCollectionsRef = users.document(idColegio).collection("cars")
        estudiantesCollectionsRef.document(id).delete().addOnSuccessListener {

        }.addOnFailureListener {

        }



    }

    override fun getItemCount(): Int {
        return this.estudianteList.size
    }

    private fun abrirActividadConParametros(clase: Class<*>, estudiante: Estudiante, it: View?, idColegio: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("estudianteId", estudiante!!.id)
        intent.putExtra("estudianteNombre", estudiante!!.nombre)
        intent.putExtra("colegioId", idColegio)
        intent.putExtra("estudianteEstatura", estudiante!!.estatura)
        intent.putExtra("estudianteNacimiento", estudiante!!.fechaNacimiento)
        intent.putExtra("estudianteBloque", estudiante!!.bloque)
        contenidoIntentExplicito.launch(intent)
    }

    class MyViewHolderEstudiante(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idEstudiante = itemView.findViewById<TextView>(R.id.tv_id_estudiante)
        val nombreEstudiante = itemView.findViewById<TextView>(R.id.tv_nombre_estudiante)
        val btnEliminarEstudiante = itemView.findViewById<Button>(R.id.btn_eliminar_estudiante)
        val btnEditarEstudiante = itemView.findViewById<Button>(R.id.btn_editar_estudiante)
        val estatura = itemView.findViewById<TextView>(R.id.tv_estatura)
        val bloque = itemView.findViewById<TextView>(R.id.tv_bloque)
        val fechaNacimiento = itemView.findViewById<TextView>(R.id.tv_fecha_nacimiento)
    }


}