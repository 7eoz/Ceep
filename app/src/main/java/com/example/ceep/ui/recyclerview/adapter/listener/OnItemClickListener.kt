package com.example.ceep.ui.recyclerview.adapter.listener

import com.example.ceep.model.Nota

interface OnItemClickListener {
    fun onItemClick(nota: Nota?, posicao: Int)
}