package com.example.handymanapplication.Utils

import android.widget.ListView
import com.example.handymanapplication.Models.ItemCell

interface IActionsOngoing {

    fun onAccept(item: ItemCell)
    fun onDelete(item: ItemCell)
    fun onListClick(list: ItemCell)
}

interface IActionsOutgoing {

    fun onListClick(list: ItemCell)
    fun onItemPay(list: ItemCell)
}
