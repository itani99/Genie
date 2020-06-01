package com.example.handymanapplication.Utils

import android.widget.ListView
import com.example.handymanapplication.Models.ItemCell

interface IActionsOngoing {

    fun onAccept(item: ItemCell)
    fun onDelete(item: ItemCell)
    fun onListClick(list: ItemCell)

    fun onViewImageClick(list: ItemCell)
}

interface IActionsOutgoing {
    fun onViewImageClick(list: ItemCell)
    fun onListClick(list: ItemCell)
    fun onItemPay(list: ItemCell)
    fun onRescheduleCllick(list: ItemCell)
}
