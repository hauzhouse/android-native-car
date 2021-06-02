package com.mobile.vencarro

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(TabelaPreco::class)], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun tabelaPrecoDao(): TabelaPrecoDAO
}
