package com.ceseagod.rajarani.cart

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
//import io.reactivex.Completable
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers


class RoomRepo(application: Application) {

    private val habitDao: CartDao
    private val listLiveData: LiveData<List<Cart>>
    private val listLiveDatas: MutableList<Cart?>? = null

    init {
        val habitRoomDatabase = CartDB.getDatabase(application)
        habitDao = habitRoomDatabase?.wordDao()!!
        listLiveData = habitDao?.getAllHabits()
    }

    fun getAllWords(): LiveData<List<Cart>> {
        return listLiveData
    }


    fun insert(word: Cart) {
//        habitDao.insert(word)
        insertAsyncTask(habitDao).execute(word)
    }
    fun deteleperticular(word: Cart)
    {
        deleteWordAsyncTask(habitDao).execute(word)
    }
fun deleteall()
{
    deleteAllWordsAsyncTask(habitDao).execute()
}
//    fun delete(word: Cart): Boolean {
//        var check = false
////        val deleteAllCompletable = Completable.fromAction(habitDao::deleteAll)
////        val insertUserCompletable =
////            Completable.fromAction { habitDao.insert(word) }
////        deleteAllCompletable
////            .andThen(Completable.fromAction { check = true })
////            .andThen(insertUserCompletable)
////            .andThen(Completable.fromAction { println("Insert finished") })
////            .observeOn(AndroidSchedulers.mainThread())
////            .subscribeOn(Schedulers.single())
////            .subscribe()
//        Completable.fromAction {
//            habitDao.deleteAndCreate(
//                word
//            )
//        }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.single())
//            .subscribe()
//        return check
////        deleteAllWordsAsyncTask(habitDao).execute()
//    }

    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: CartDao) :
        AsyncTask<Cart, Void, Void>() {

        override fun doInBackground(vararg params: Cart): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    private class deleteWordAsyncTask internal constructor(dao: CartDao) :
        AsyncTask<Cart?, Void?, Void?>() {
        private val mAsyncTaskDao: CartDao

        init {
            mAsyncTaskDao = dao
        }

        override fun doInBackground(vararg params: Cart?): Void? {
            mAsyncTaskDao.deleteWord(params[0]!!.ids,params[0]!!.count)
            return null
        }
    }

    private class deleteAllWordsAsyncTask internal constructor(dao: CartDao) :
        AsyncTask<Void?, Void?, Void?>() {
        private val mAsyncTaskDao: CartDao = dao

        override fun doInBackground(vararg params: Void?): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }

    }
}