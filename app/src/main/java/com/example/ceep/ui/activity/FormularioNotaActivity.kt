package com.example.ceep.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ceep.R
import com.example.ceep.model.Nota

class FormularioNotaActivity : AppCompatActivity(), NotaActivityConstantes {


    lateinit var titulo: TextView
    lateinit var descricao: TextView

    private val TITULO_APPBAR_INSERE = "Insere nota"
    private val TITULO_APPBAR_ALTERA = "Altera nota"

    private var posicaoRecebida: Int = POSICAO_INVALIDA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_nota)

        setTitle(TITULO_APPBAR_INSERE)
        var (titulo, descricao) = inicializaCampos()

        var dadosRecebidos = getIntent()
        if(dadosRecebidos.hasExtra(CHAVE_NOTA)) {
            setTitle(TITULO_APPBAR_ALTERA)
            var notaRecebida: Nota = dadosRecebidos.getSerializableExtra(CHAVE_NOTA) as Nota

            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA)

            preencheCampos(notaRecebida)
        }
    }

    private fun preencheCampos(notaRecebida: Nota) {
        titulo.text = notaRecebida.titulo
        descricao.text = notaRecebida.descricao
    }

    private fun inicializaCampos(): Pair<TextView, TextView> {
        titulo = findViewById(R.id.formulario_nota_titulo)
        descricao = findViewById(R.id.formulario_nota_descricao)
        return Pair(titulo, descricao)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_formulario_nota_salva, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(ehMenuSalvaNota(item)){
            val nota = criaNota()
            retornaNota(nota)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun retornaNota(nota: Nota) {
        val resultadoInsercao = Intent().putExtra(CHAVE_NOTA, nota)
        resultadoInsercao.putExtra("posicao", posicaoRecebida)
        setResult(Activity.RESULT_OK, resultadoInsercao)
    }

    private fun criaNota(): Nota {
        return Nota(
            findViewById<EditText>(R.id.formulario_nota_titulo).text.toString(),
            findViewById<EditText>(R.id.formulario_nota_descricao).text.toString()
        )
    }

    private fun ehMenuSalvaNota(item: MenuItem) =
        item.itemId == R.id.menu_fomulario_nota_ic_salva
}