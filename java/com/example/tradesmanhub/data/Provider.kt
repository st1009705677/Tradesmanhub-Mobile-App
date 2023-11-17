package com.example.tradesmanhub.data

import android.os.Parcel
import android.os.Parcelable

data class Provider(
    val spID: String = "",
    val name: String = "",
    val service_type: String = "",
    val description: String = "",
    val averageRating: Long = 0,
    val address: String =  "",
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong() ?: 0,
        parcel.readString() ?: ""
    ) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(spID)
        parcel.writeString(name)
        parcel.writeString(service_type)
        parcel.writeString(description)
        parcel.writeLong(averageRating)
        parcel.writeString(address)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Provider> {
        override fun createFromParcel(parcel: Parcel): Provider {
            return Provider(parcel)
        }

        override fun newArray(size: Int): Array<Provider?> {
            return arrayOfNulls(size)
        }
    }
}
