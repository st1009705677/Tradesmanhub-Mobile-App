package com.example.tradesmanhub.data

import android.os.Parcel
import android.os.Parcelable

data class JobPost(
    val jobId: String,
    val client: String,
    val service: String,
    val description: String,
    val address: String,
    val budget: String,
    val isAssigned: Boolean,
    val status: String,
    val start_date: String,
    val provider: String = ""
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(jobId)
        parcel.writeString(client)
        parcel.writeString(service)
        parcel.writeString(description)
        parcel.writeString(address)
        parcel.writeString(budget)
        parcel.writeBoolean(isAssigned)
        parcel.writeString(status)
        parcel.writeString(start_date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JobPost> {
        override fun createFromParcel(parcel: Parcel): JobPost {
            return JobPost(parcel)
        }

        override fun newArray(size: Int): Array<JobPost?> {
            return arrayOfNulls(size)
        }
    }

}
