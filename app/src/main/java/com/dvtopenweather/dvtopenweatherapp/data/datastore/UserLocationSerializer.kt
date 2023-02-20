package com.dvtopenweather.dvtopenweatherapp.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.sample.android_sample_preference_datastore.UserDataAndLatestUpdateData
import java.io.InputStream
import java.io.OutputStream

class UserLocationSerializer : Serializer<UserDataAndLatestUpdateData> {

    override val defaultValue: UserDataAndLatestUpdateData = UserDataAndLatestUpdateData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserDataAndLatestUpdateData {
        try {
            return UserDataAndLatestUpdateData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserDataAndLatestUpdateData, output: OutputStream) {
        t.writeTo(output)
    }

}
