package com.mobile.vencarro

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.linha_lista.view.*

class VeiculoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // cada item do layout sera mapeado para uma variavel interna
    var txtMarca = itemView.txtMarca
    var txtModelo = itemView.txtModelo
    var txtAno = itemView.txtAno
    var itemLista = itemView.itemLista

}