package com.mobile.vencarro

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_lista.*

class ListaFragment : Fragment() {

    // retornar a visualização (view) do layout associado ao fragment
    // o desenho da tela é definido em layout/fragment_lista
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // inflate instancia (cria) a "tela" a partir de um xml
        return inflater.inflate(R.layout.fragment_lista, container, false)

    }

    // chama esta funcao quando a Activity esta instanciada
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // definir o layout do RecyclerView
        rvVeiculos.layoutManager = LinearLayoutManager(context!!)
        rvVeiculos.adapter = VeiculoAdapter(context!!)

    }

}
