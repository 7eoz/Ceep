package com.example.ceep.ui.recyclerview.helper.callback

import android.system.Os.remove
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ceep.dao.NotaDAO
import com.example.ceep.ui.recyclerview.adapter.ListaNotasAdapter

class NotaItemTouchHelperCallback(val adapter: ListaNotasAdapter?) : ItemTouchHelper.Callback() {

    override fun getMovementFlags( recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder ): Int {
        var marcacoesDeDeslize = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        var marcacoesDeArrastar = ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(marcacoesDeArrastar, marcacoesDeDeslize)
    }

    override fun onMove( recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                         target: RecyclerView.ViewHolder ): Boolean {
        val posicaoInicial = viewHolder.bindingAdapterPosition
        val posicaoFinal = target.bindingAdapterPosition
        trocaNotas(posicaoInicial, posicaoFinal)
        return true
    }

    private fun trocaNotas(posicaoInicial: Int, posicaoFinal: Int) {
        NotaDAO().troca(posicaoInicial, posicaoFinal)
        adapter?.troca(posicaoInicial, posicaoFinal)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var posicaoDaNotaDeslizada = viewHolder.bindingAdapterPosition
        removeNota(posicaoDaNotaDeslizada)
    }

    private fun removeNota(posicao: Int) {
        NotaDAO().remove(posicao)
        adapter!!.remove(posicao)
    }

}
