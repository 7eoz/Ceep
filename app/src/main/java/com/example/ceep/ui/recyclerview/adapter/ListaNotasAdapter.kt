package com.example.ceep.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ceep.R
import com.example.ceep.model.Nota
import com.example.ceep.ui.recyclerview.adapter.listener.OnItemClickListener
import java.util.*

class ListaNotasAdapter(private val context: Context, private val notas: MutableList<Nota>) : RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder>() {

   /* private var onItemClickListener: OnItemClickListener()*/
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val viewCriada = criaView(parent)
        return NotaViewHolder(viewCriada)
    }

    override fun getItemCount(): Int {
        return notas.size
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = notas[position]
        holder.vincula(nota)
    }

    private fun criaView(parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false)
    }

    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titulo: TextView
        private val descricao: TextView
        private var nota: Nota? = null

        init {
            titulo = itemView.findViewById(R.id.item_nota_titulo)
            descricao = itemView.findViewById(R.id.item_nota_descricao)
            itemView.setOnClickListener { onItemClickListener!!.onItemClick(nota, bindingAdapterPosition) }
        }

        fun vincula(nota: Nota) {
            this.nota = nota
            preencheCampo(nota)
        }

        private fun preencheCampo(nota: Nota) {
            titulo.text = nota.titulo
            descricao.text = nota.descricao
        }
    }

    fun adiciona(nota: Nota){
        notas.add(nota)
        notifyDataSetChanged()
    }

    fun altera(posicao: Int, nota: Nota) {
        notas.set(posicao, nota)
        notifyDataSetChanged()
    }

    fun remove(posicao: Int) {
        notas.removeAt(posicao)
        notifyItemRemoved(posicao)
    }

    fun troca(posicaoInicial: Int, posicaoFinal: Int) {
        Collections.swap(notas, posicaoInicial, posicaoFinal)
        notifyItemMoved(posicaoInicial, posicaoFinal)
    }

}

