package com.mobile.vencarro

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.cloudant.sync.documentstore.DocumentRevision
import com.cloudant.sync.documentstore.DocumentStore
import kotlinx.android.synthetic.main.fragment_resumo.*
import kotlinx.android.synthetic.main.fragment_resumo.view.*
import java.io.File

class ResumoFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    var marca = ""
    var modelo = ""
    var ano = ""
    var preco = ""

    // função acionada quando o fragment é criado
    // função onCreate é executada antes da onCreateView e portanto os elementos da interface
    // ainda não foram criados!!!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // recupera os dados passados no arguments dentro de um Bundle
        // armazena em variáveis locais
        marca = arguments!!.getString("MARCA")
        modelo = arguments!!.getString("MODELO")
        ano = arguments!!.getString("ANO")
        preco = arguments!!.getString("PRECO")

    }

    // retornar a visualização (view) do layout associado ao fragment
    // o desenho da tela é definido em layout/fragment_lista
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {


        // inflate instancia (cria) a "tela" a partir de um xml
        var tela = inflater.inflate(R.layout.fragment_resumo, container, false)
        // define os valores a serem exibidos nox TextViews (propriedade text)
        tela.txtMarca.text = marca
        tela.txtModelo.text = modelo
        tela.txtAno.text = ano
        tela.txtPreco.text = preco

        // registra o próprio fragment (ResumoFragment) como listener
        // para o evento selecionar item no Bottom Menu
        tela.menuResumo.setOnNavigationItemSelectedListener(this)

        return tela

    }

    // quando o usuario selecionar um item do Bottom Menu entao esta funcao será acionada
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // obter qual o item foi selecionado pelo usuario: Foto ou Salvar?
        when (item.itemId) {

                R.id.mnuFoto -> tirarFoto()
                // enviar os dados do veiculo para um servidor centralizado
                // aciona a função salvar de MainActivity passando marca, modelo, ano e preco
                R.id.mnuSalvar -> (context as MainActivity).salvar(marca, modelo, ano, preco)
        }

        return true;

    }

    // função que aciona a aplicação de câmera
    fun tirarFoto() {

        // ACTION_IMAGE_CAPTURE - declara a intenção de capturar uma imagem
        val acessoCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // verifica se existe uma app para atender à intenção
        if (acessoCameraIntent.resolveActivity(activity!!.packageManager) != null) {
            //envia a intenção para a captura de imagem
            //o retorno será direcionado para onActivityResult em MainActivity
            activity!!.startActivityForResult(acessoCameraIntent, 1)
        }
    }

}
