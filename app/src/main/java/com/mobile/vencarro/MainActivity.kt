package com.mobile.vencarro

import android.arch.persistence.room.Room
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.cloudant.sync.documentstore.DocumentBodyFactory
import com.cloudant.sync.documentstore.DocumentRevision
import com.cloudant.sync.documentstore.DocumentStore
import com.cloudant.sync.replication.ReplicatorBuilder
import kotlinx.android.synthetic.main.fragment_resumo.*
import java.io.File
import java.net.URI


class MainActivity : AppCompatActivity() {

    lateinit var database: AppDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        supportActionBar?.hide() // ocultar a barra de titulo

        supportFragmentManager.beginTransaction().add(R.id.flPrincipal, ListaFragment()).commit()

        // abre conexao banco de dados
        database = Room.databaseBuilder(this, AppDB::class.java, "listaPreco-db").build()

    }

    // exibir o fragment com o resumo dos dados do veiculo selecionado
    // incluindo o preco para que o usuario tire uma foto do veiculo
    fun exibirResumo(marca:String, modelo:String, ano:String, preco:String) {

        // cria um hash map (Bundle) para que os parametros sejam passados para o ResumoFragment
        val bundle = Bundle()
        bundle.putString("MARCA", marca)
        bundle.putString("MODELO", modelo)
        bundle.putString("ANO", ano)
        bundle.putString("PRECO", preco)

        val resumoFragment = ResumoFragment()
        // associa os parametro do bundle ao fragment
        resumoFragment.arguments = bundle

        // troca o ListaFragment pelo ResumoFragment
        supportFragmentManager.beginTransaction().replace(R.id.flPrincipal, resumoFragment).commit()
        // DESAFIO!!!!!!
        // Implementar a tela de resumo utilizando os slides 31, 32 e 33 como referencia


    }

    // acionado após usuário tirar a foto para receber a imagem capturada
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // verifica se o resultado foi sucesso (RESULT_OK)
        if (resultCode == RESULT_OK) {
            // obtem a imagem (Bitmap) da foto capturada
            val imageBitmap = data.extras.get("data") as Bitmap
            // define no ImageView a imagem que foi capturada na foto
            imgFoto.setImageBitmap(imageBitmap)
        }
    }

    // envia dados para o banco de dados na Cloud (DBaaS)
    fun salvar(marca:String, modelo:String, ano:String, preco:String) {

        // filesDir é o diretório files dentro do diretório de dados da aplicação (/data/user/0/com.mobile.vencarro/files)
        Log.i("VENCARRO", "Diretorio: ${filesDir.absoluteFile.absolutePath}")
        // instanciar o DocumentStore (arquivo local para armazenar temporariamente os dados)
        val ds = DocumentStore.getInstance(File("${filesDir.absoluteFile.absolutePath}", "vencar_datastore"))

        // montar o corpo do documento
        // {"marca":"Fiat", "modelo":"147", "ano": ...}
        val body = HashMap<String, Any>()
        body["marca"] = marca
        body["modelo"] = modelo
        body["ano"] = ano
        body["preco"] = preco

        // salva o documento no DocumentStore
        val revision = DocumentRevision()
        revision.body = DocumentBodyFactory.create(body)
        val saved = ds.database().create(revision)

        // iniciar a sincronização
        // URL do serviço de banco de dados cloudant (credentials)
        // anexar o nome do banco de dados no final da URL (/vencar)
        val uri = URI("https://173ba38d-bc95-47ed-be32-6b364d5b6795-bluemix.cloudantnosqldb.appdomain.cloud/vencar")

        // instanciando o replicador
        val replicator = ReplicatorBuilder.push().
        from(ds).
        to(uri).
        iamApiKey("Xr2sCDVqJDiWJC4tOEP1u3v7zrY8QcLC6nh7BLD18N1m").
        build()

        // iniciando o replicador (deve ser feito uma única vez)
        replicator.start()

        // insere no banco de dados local
        database.tabelaPrecoDao().insertAll(TabelaPreco(preco = preco.toDouble()));

            var lista = Runnable() {

                fun run() {
                    // exibe o total de gistros na tabela TAB_PRECO
                    Log.i("PRECO", "TOTAL_REGISTROS: ${database.tabelaPrecoDao().getAll().size}");

                    var lista = database.tabelaPrecoDao().getAll()
                    lista.forEach {
                        Log.i("IMOVEL", "$it.lat")
                    }
                }
            };

        runOnUiThread(lista)


    }

    override fun onDestroy() {
        // fecha o banco de dados quando finalizar a
        database.close();
        super.onDestroy()
    }

}
