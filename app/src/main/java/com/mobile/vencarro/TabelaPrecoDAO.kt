package com.mobile.vencarro

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface TabelaPrecoDAO {
    // função para retornar todos os itens da TabelaPReco
    @Query("SELECT * FROM TAB_PRECO")
    fun getAll(): List<TabelaPreco>

    // cria registros em TAB_PRECO
    @Insert
    fun insertAll(vararg listCategories: TabelaPreco)

}
