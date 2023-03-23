package com.example.crudfirebase

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Colegio(
    val id: String?=null,
    var nombreColegio: String? = null,
    var inversion: Double? = 0.0,
    var seccion: String? = "",
    ): Parcelable{
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel): this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombreColegio)
        inversion?.let { parcel.writeDouble(it) }
        parcel.writeString(seccion)
    }

    companion object CREATOR : Parcelable.Creator<Colegio> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Colegio {
            return Colegio(parcel)
        }

        override fun newArray(size: Int): Array<Colegio?> {
            return arrayOfNulls(size)
        }
    }
}
