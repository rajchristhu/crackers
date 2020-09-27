package com.ceseagod.rajarani.cart

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ceseagod.rajarani.cart.Cart


@Dao
interface CartDao {

    @Insert
    fun insert(habit: Cart)

    @Query("DELETE FROM Cart")
    fun deleteAll()

    @Query("DELETE FROM Cart WHERE ids = :ids AND  count = :count")
    fun deleteWord(ids: String?, count: Int?)

    //    @Query("SELECT id FROM Cart WHERE id = :id LIMIT 1")
//    fun getItemId(id: String): String?
    @Query("SELECT * from Cart WHERE id= :id")
    open fun getItemId(id: Int): MutableList<Cart?>?

    @Query("SELECT * FROM Cart ORDER BY name ASC")
    fun getAllHabits(): LiveData<List<Cart>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(habit: Cart): Int

    @Query("SELECT * FROM Cart WHERE id = :minAge")
    fun loadAllUsersOlderThan(minAge: String): List<Cart>


    @Transaction
    fun deleteAndCreate(habit: Cart?) {
        deleteAll()
        insert(habit!!)
    }
}