package com.mobile.vencarro

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class VeiculoAdapter(context:Context) : RecyclerView.Adapter<VeiculoHolder>() {

    // criar uma variavel para armazenar localmente o
    // contexto passado como parametro
    val context = context

    // armazena o id da ultima marca selecionada
    var idUltimaMarcaSelecionada = ""
    // armazena o id do ultimo modelo selecionado
    var idUltimoModeloSelecionado = ""

    // representa a lista de objetos do tipo veiculo
    var veiculos = ArrayList<Veiculo>()

    // URL para obter as marcas de veiculo
    val URL_MARCA = "https://fipeapi.appspot.com/api/1/carros/marcas.json"

    // URL para obter os modelos de uma marca pelo idMarca
    val URL_MODELO = "http://fipeapi.appspot.com/api/1/carros/veiculos/"

    // URL para obter os anos de um modelo de veiculo pelo idModelo
    val URL_ANO = "https://fipeapi.appspot.com/api/1/carros/veiculo/"

    //URL para obter o preco final do veiculo
    val URL_PRECO = "https://fipeapi.appspot.com/api/1/carros/veiculo/"

    var queue : RequestQueue
    // bloco de inicializacao da classe Kotlin
    init {
        //veiculos.add(Veiculo("Ford", "Focus", "2010"))
        //veiculos.add(Veiculo("Fiat", "Uno", "2000"))

        // obtem uma fila para as requisicoes HTTP do Volley
        queue = Volley.newRequestQueue(context)

        val requestMarca = JsonArrayRequest(URL_MARCA,
                                            Response.Listener { // obtive uma resposta
                                                    response ->

                                                Log.i("VENCAR", response.toString())

                                                //adicionar marcas na lista veiculos
                                                // response é um array de JSONObject
                                                // obter a qtde de objetos no array
                                                val qtde = response.length() - 1
                                                // percorre cada elemento do array
                                                for (i in 0..qtde){

                                                    // obtem cada uma das marcas
                                                    // Ex: {"name":"AUDI","fipe_name":"Audi","order":2,"key":"audi-6","id":6}
                                                    val marca = response.getJSONObject(i)

                                                    // DESAFIO#1 - incluir o id da marca no objeto Veiculo
                                                    // além do fipe_name

                                                    veiculos.add(Veiculo(marca.getString("id"),
                                                                         "",
                                                                        "",
                                                                         marca.getString("fipe_name"),
                                                                 "-",
                                                                    "-"))

                                                }

                                                // avisa o Adapter (lista) que os dados foram atualizados
                                                notifyDataSetChanged()


                                            },
                                            Response.ErrorListener { // ocorreu algum erro
                                                    error -> Log.e("VENCAR", error.toString())
                                            })

        /// adiciona a requisicao na fila
        queue.add(requestMarca)

    }

    // instanciar o layout de uma linha
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VeiculoHolder {

        // LayoutInflater converte itens do layout de XML para objetos
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linha_lista, parent, false)
        return VeiculoHolder(view)

    }

    // retorna a quantidade de objetos na lista (no caso veiculos)
    override fun getItemCount(): Int {

        return veiculos.size

    }

    // cada linha é processada quando exibida na lista
    override fun onBindViewHolder(holder: VeiculoHolder, position: Int) {
        // transferir os dados (Veiculo) para os itens da lista
        var veiculoAtual = veiculos.get(position)
        holder.txtMarca.text = veiculoAtual.marca
        holder.txtModelo.text = veiculoAtual.modelo
        holder.txtAno.text = veiculoAtual.ano

        // registrar o evento de click na linha da lista
        holder.itemLista.setOnClickListener(object: View.OnClickListener {

            // quando o usuario clica em um item da lista esta funcao e acionada
            override fun onClick(v: View?) {

                Log.i("VENCARRO", "Objeto = ${veiculos.get(position)}")

                //DESAFIO#2 - efetuar a segunda requisicao passando o id da marca
                //como parâmetro URL_MODELO

                // obtem o id da marca do veiculo selecionado na lista
                val idMarca = veiculos.get(position).idMarca
                // obtem o id do modelo do veiculo selecionado na lista
                val idModelo = veiculos.get(position).idModelo
                // obtem o id do ano do veiculo selecionado na lista
                val idAno = veiculos.get(position).idAno

                Log.i("VENCARRO", "idMarca = $idMarca")
                Log.i("VENCARRO", "idModelo = $idModelo")
                Log.i("VENCARRO", "idAno = $idAno")

                // verifica se foi selecionada a marca
                if (idMarca != "") {
                    idUltimaMarcaSelecionada = idMarca
                    // aciona a funcao para obter os modelos a partir da marca
                    obterModelo(idMarca)
                // verifica se foi selecionado o modelo
                } else if (idModelo != "") {
                    idUltimoModeloSelecionado = idModelo
                    // aciona a funcao para obter os anos a partir do modelo
                    obterAno(idUltimaMarcaSelecionada, idModelo)
                }
                // verifica se foi selecionado o ano
                else if (idAno != "") {
                    // aciona a funcao para obter o preco final
                    obterPreco(idUltimaMarcaSelecionada, idUltimoModeloSelecionado, idAno)
                }

            }

        })

    }

    // realiza uma segunda requisição ao serviço Tabela Fipe
    // para obter todos os modelos de uma marca de veiculo pelo seu id
    fun obterModelo(idMarca:String) {

        val URL = "$URL_MODELO${idMarca}.json"

        Log.i("VENCARRO", "Obtendo os modelos da marca: $URL")

        // limpar a lista de veiculos previamente criada com as marcas
        veiculos.clear()
        val requestModelo = JsonArrayRequest(URL,
            Response.Listener { // obtive uma resposta
                    response ->

                Log.i("VENCAR", response.toString())

                //adicionar modelos na lista veiculos
                // response é um array de JSONObject
                // obter a qtde de objetos no array
                val qtde = response.length() - 1
                // percorre cada elemento do array
                for (i in 0..qtde){

                    // obtem cada uma dos modelos
                    // Ex: {"fipe_marca": "GM - Chevrolet", "name": "A-10 2.5/4.1", "marca": "CHEVROLET", "key": "a-10-926", "id": "926", "fipe_name": "A-10 2.5/4.1"}
                    val modelo = response.getJSONObject(i)

                    veiculos.add(Veiculo("", //idMarca
                        modelo.getString("id"), //idModelo
                        "", //idAno
                        modelo.getString("fipe_marca"), // marca
                        modelo.getString("fipe_name"), // modelo
                        "-")) // ano

                }

                // avisa o Adapter (lista) que os dados foram atualizados
                notifyDataSetChanged()


            },
            Response.ErrorListener { // ocorreu algum erro
                    error -> Log.e("VENCAR", error.toString())
            })

        /// adiciona a requisicao na fila
        queue.add(requestModelo)


    }

    // realiza uma terceira requisição ao serviço Tabela Fipe
    // para obter todos os anos de um modelo de veiculo pelo seu id
    // os anos sao obtidos por meio do id da marca e do modelo do veiculo
    fun obterAno(idMarca: String, idModelo:String) {

        val URL = "$URL_ANO${idMarca}/${idModelo}.json"

        Log.i("VENCARRO", "Obtendo os anos do modelo: $URL")

        // limpar a lista de veiculos previamente criada com os modelo
        veiculos.clear()

        val requestAno = JsonArrayRequest(URL,
            Response.Listener { // obtive uma resposta
                    response ->

                Log.i("VENCAR", response.toString())

                //adicionar anos na lista veiculos
                // response é um array de JSONObject
                // obter a qtde de objetos no array
                val qtde = response.length() - 1
                // percorre cada elemento do array
                for (i in 0..qtde){

                    // obtem cada uma dos anos
                    // Ex: {"fipe_marca":"Audi","fipe_codigo":"1995-1","name":"1995 Gasolina","marca":"AUDI","key":"1995-1","veiculo":"100 2.8 V6","id":"1995-1"}
                    val ano = response.getJSONObject(i)

                    veiculos.add(Veiculo("", // idMarca
                        "", // idModelo
                        ano.getString("id"), //idAno
                        ano.getString("fipe_marca"), // marca
                        ano.getString("veiculo"), // modelo
                        ano.getString("name"))) // ano

                }

                // avisa o Adapter (lista) que os dados foram atualizados
                notifyDataSetChanged()


            },
            Response.ErrorListener { // ocorreu algum erro
                    error -> Log.e("VENCAR", error.toString())
            })

        /// adiciona a requisicao na fila
        queue.add(requestAno)

    }

    // realiza uma quarta requisição ao serviço Tabela File
    // para obter finalmente o preço do veiculo
    // o preço é obtido por meio do id da marca, id do modelo e id do ano do veiculo
    fun obterPreco(idMarca: String, idModelo:String, idAno:String){

        val URL = "$URL_PRECO${idMarca}/${idModelo}/${idAno}.json"

        Log.i("VENCARRO", "Obtendo o preco: $URL")

        // a ultima requisicao nao reforta um array e sim um unico objeto JSON
        val requestPreco = JsonObjectRequest(URL, null,
            Response.Listener { // obtive uma resposta (JSONObject)
                    response ->

                // {"referencia":"março de 2020","fipe_codigo":"014062-7","name":"CITY Sedan EXL 1.5 Flex 16V 4p Mec.","combustivel":"Gasolina","marca":"Honda","ano_modelo":"2011","preco":"R$ 33.529,00","key":"city-2011","time":0,"veiculo":"CITY Sedan EXL 1.5 Flex 16V 4p Mec.","id":"2011"}
                Log.i("VENCAR", response.toString())
                // aciona a funcao exibirRemumo da MainActivity
                // esta funcao ira trocar os fragments
                (context as MainActivity).exibirResumo(response.getString("marca"),
                                                       response.getString("name"),
                                                  response.getString("combustivel") + " - " + response.getString("ano_modelo"),
                                                       response.getString("preco"))

            },
            Response.ErrorListener { // ocorreu algum erro
                    error -> Log.e("VENCAR", error.toString())
            })

        /// adiciona a requisicao na fila
        queue.add(requestPreco)


    }

}