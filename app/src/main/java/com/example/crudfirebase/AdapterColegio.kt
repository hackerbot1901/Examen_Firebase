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


class AdapterColegio(var colegioList: ArrayList<Colegio>, private val contenidoIntentExplicito: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<AdapterColegio.MyViewHolderUser>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderUser {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.colegio_item, parent, false)

        return MyViewHolderUser(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolderUser, position: Int) {
        val currentItem = colegioList[position]
        holder.idColegio.text = currentItem.id
        holder.inversiones.text = currentItem.inversion.toString()
        holder.nombreColegio.text = currentItem.nombreColegio
        holder.seccion.text = currentItem.seccion
        holder.btnEditar.setOnClickListener {
            abrirActividadConParametros(EditColegioInfoActivity::class.java, currentItem, it)
        }


        holder.btnEliminar.setOnClickListener {

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Confirmacion de eliminación")
            builder.setMessage("Estás seguro que lo deseas eliminar??")
            builder.setPositiveButton("Si") { dialog, _ ->
                // Delete the item
                dialog.dismiss()
                // Perform the deletion here
                deleteColegio(currentItem.id!!)
                this.colegioList.remove(currentItem)
                notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        holder.btnVerEstudiantes.setOnClickListener {
            abrirActividadConParametros(EstudianteListActivity::class.java, currentItem, it)
        }
    }

    private fun deleteColegio(id: String) {
        val db = Firebase.firestore
        val colegio = db.collection("colegio")
        val colegioDoc = colegio.document(id)
        colegioDoc.delete().addOnSuccessListener {

        }.addOnFailureListener {

        }


    }

    private fun abrirActividadConParametros(clase: Class<*>, colegio: Colegio, it: View?) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("colegioId", colegio!!.id)
        intent.putExtra("colegioNombre", colegio!!.nombreColegio)
        intent.putExtra("colegioInversion", colegio!!.inversion)
        intent.putExtra("colegioSeccion", colegio!!.seccion)
        contenidoIntentExplicito.launch(intent)
    }

    override fun getItemCount(): Int {
        return colegioList.size
    }

    

    class MyViewHolderUser(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreColegio = itemView.findViewById<TextView>(R.id.tv_nombre_colegio)
        val btnEditar = itemView.findViewById<Button>(R.id.btn_editar)
        val btnEliminar = itemView.findViewById<Button>(R.id.btn_eliminar)
        val btnVerEstudiantes = itemView.findViewById<Button>(R.id.btn_ver_estudiante)
        val idColegio = itemView.findViewById<TextView>(R.id.tvIdColegio)
        val inversiones = itemView.findViewById<TextView>(R.id.tv_inversiones)
        val seccion = itemView.findViewById<TextView>(R.id.tv_seccion)
    }


}