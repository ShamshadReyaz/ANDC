package com.mobiquel.srccapp.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobiquel.srccapp.room.converter.Converters
import com.mobiquel.srccapp.room.dao.AttendanceDao
import com.mobiquel.srccapp.room.entity.AttendanceClassEntity


@Database(entities = [AttendanceClassEntity::class], version = 1)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile
        private var INSTANCE:AppDatabase?=null

        fun getDataBase(context: Context):AppDatabase{
            if(INSTANCE==null){
                synchronized(this){
                    INSTANCE=buildDataBase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDataBase(context: Context):AppDatabase{
            return Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"srcc_db")
                .build()
        }
    }
}