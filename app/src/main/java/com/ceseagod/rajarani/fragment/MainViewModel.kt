package com.ceseagod.rajarani.fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ceseagod.rajarani.cart.Cart
import com.ceseagod.rajarani.cart.RoomRepo

class MainViewModel(application: Application) :  AndroidViewModel(application) {
    private val habitRepository: RoomRepo = RoomRepo(application)
    val allWords: LiveData<List<Cart>>
    val names: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val namess: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val image: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    // Declaring it
    val liveDataA = MutableLiveData<String>()// Trigger the value change
    lateinit var allWord: List<Cart>

    init {
        allWords = habitRepository.getAllWords()
    }

    fun insert(habits: Cart) {
        habitRepository.insert(habits)
    }

    fun deletePerticular(habits: Cart) {
        habitRepository.deteleperticular(habits)
    }
    fun deleteall()
    {
        habitRepository.deleteall()
    }

    fun delete(cart: Cart) {
//       val s= Completable.fromAction { habitRepository.delete(cart) }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.single())
//            .subscribe()
        val s = habitRepository.delete(cart)
        Log.e("C", s.toString())
//        habitRepository.delete()
    }

    //    fun get(id: String): List<Cart> {
//        allWord = habitRepository.getWords(id)
//        return allWord
//    }
    fun insertstr(someValue: String) {
        names.value = someValue

    }
    fun insertname(someValue: String) {
        name.value = someValue

    }
    fun insertnamess(someValue: String) {
        namess.value = someValue

    }
    fun insertimage(someValue: String) {
        image.value = someValue

    }

}