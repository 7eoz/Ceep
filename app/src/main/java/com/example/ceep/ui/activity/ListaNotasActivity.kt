package com.example.ceep.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ceep.R
import com.example.ceep.dao.NotaDAO
import com.example.ceep.model.Nota
import com.example.ceep.ui.recyclerview.adapter.ListaNotasAdapter
import com.example.ceep.ui.recyclerview.adapter.listener.OnItemClickListener
import com.example.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback


class ListaNotasActivity : AppCompatActivity(), NotaActivityConstantes {
    private var adapter: ListaNotasAdapter? = null

    val TITULO_APPBAR = "Notas"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_notas)

        setTitle(TITULO_APPBAR)

        val todasNotas = pegaTodasAsNotas()
        configuraRecyclerView(todasNotas)
        configuraBotaoInsereNota()
    }

    private fun configuraBotaoInsereNota() {
        val botaoInsereNota: TextView = findViewById(R.id.lista_notas_insere_nota)
        botaoInsereNota.setOnClickListener {
            vaiParaFormularioNotaActivityInsere()
        }
    }

    private fun vaiParaFormularioNotaActivityInsere() {
        val intent = Intent(this@ListaNotasActivity, FormularioNotaActivity::class.java)
        startActivityForResult(intent, CODIGO_REQUISICAO_INSERE_NOTA)
    }

    private fun pegaTodasAsNotas(): List<Nota>? {
        return NotaDAO().todos()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (ehResultadoInsereNota(requestCode, data)) {
                if (resultadoOK(resultCode)) {
                    val notaRecebida: Nota = data.getSerializableExtra("nota") as Nota
                    adicionaNota(notaRecebida)
                } //else if (resultCode == Activity.RESULT_CANCELED)
            }

            if (ehResultadoAlteraNota(requestCode, data)) {
                if (resultadoOK(resultCode)) {
                    val notaRecebida = data.getSerializableExtra(CHAVE_NOTA) as Nota
                    val posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA)
                    if (ehPosicaoValida(posicaoRecebida)) {
                        altera(posicaoRecebida, notaRecebida)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun altera(posicao: Int, nota: Nota) {
        NotaDAO().altera(posicao, nota)
        adapter?.altera(posicao, nota)
    }

    private fun ehPosicaoValida(posicaoRecebida: Int) = posicaoRecebida > POSICAO_INVALIDA

    private fun ehResultadoAlteraNota(requestCode: Int, data: Intent): Boolean {
        return ehCodigoRequisicaoAlteraNota(requestCode) && temNota(data)
    }

    private fun ehCodigoRequisicaoAlteraNota(requestCode: Int) =
        requestCode == CODIGO_REQUISICAO_ALTERA_NOTA

    private fun adicionaNota(notaRecebida: Nota) {
        NotaDAO().insere(notaRecebida)
        adapter!!.adiciona(notaRecebida)
    }

    private fun ehResultadoInsereNota(requestCode: Int, data: Intent) =
        ehCodigoRequisicaoInsereNota(requestCode)

    private fun temNota(data: Intent): Boolean {
        return data != null && data.hasExtra(CHAVE_NOTA)
    }

    private fun resultadoOK(resultCode: Int) =
        resultCode == Activity.RESULT_OK

    private fun ehCodigoRequisicaoInsereNota(requestCode: Int) =
        requestCode == CODIGO_REQUISICAO_INSERE_NOTA

    private fun configuraRecyclerView(todasNotas: List<Nota>?) {
        val listaNotas = findViewById<RecyclerView>(R.id.lista_notas_recyclerview)
        configuraAdapter(todasNotas, listaNotas)
        configuraItemTouchHelper(listaNotas)
    }

    private fun configuraItemTouchHelper(listaNotas: RecyclerView?) {
        var itemTouchHelper = ItemTouchHelper(NotaItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(listaNotas)
    }

    private fun configuraAdapter(todasNotas: List<Nota>?, listaNotas: RecyclerView) {
        adapter = ListaNotasAdapter(this, todasNotas as MutableList<Nota>)
        listaNotas.adapter = adapter
        adapter!!.setOnItemClickListener(object :
            OnItemClickListener {
            override fun onItemClick(nota: Nota?, posicao: Int) {
                vaiParaFormularioNotaActivityAltera(nota, posicao)
            }
        })
    }

    private fun vaiParaFormularioNotaActivityAltera(nota: Nota?, posicao: Int) {
        val abreFormularioComConta =
            Intent(this@ListaNotasActivity, FormularioNotaActivity::class.java)
        abreFormularioComConta.putExtra(CHAVE_NOTA, nota)
        abreFormularioComConta.putExtra(CHAVE_POSICAO, posicao)
        startActivityForResult(abreFormularioComConta, CODIGO_REQUISICAO_ALTERA_NOTA)
    }

}