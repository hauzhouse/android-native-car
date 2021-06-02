package com.mobile.vencarro

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

// Entidade TAB_PRECO (ID PRIMARY_KEY, PRECO)
@Entity(tableName = "TAB_PRECO")
data class TabelaPreco(@ColumnInfo(name="ID") @PrimaryKey(autoGenerate = true)
                       var id:Long = 0,
                       @ColumnInfo(name="PRECO")
                       var preco:Double)