package com.ceseagod.rajarani.cart

import android.content.Context
import android.os.AsyncTask
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Cart::class], version = 1)
abstract class CartDB : RoomDatabase() {
    abstract fun wordDao(): CartDao
    private class PopulateDbAsync internal constructor(db: CartDB?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val mDao: CartDao = db!!.wordDao()
        protected override fun doInBackground(vararg params: Void?): Void? {
            mDao.deleteAll()
            return null
        }

    }

    companion object {
        private var INSTANCE: CartDB? = null
        fun getDatabase(context: Context): CartDB? {
            if (INSTANCE == null) {
                synchronized(CartDB::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            CartDB::class.java, "word_database"
                        )
                            .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        private val sRoomDatabaseCallback: Callback =
            object : Callback() {
                override fun onOpen(@NonNull db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    PopulateDbAsync(INSTANCE)
                        .execute()
                }
            }
    }
}