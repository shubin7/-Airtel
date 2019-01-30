package com.example.android.airteldemo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.airteldemo.R
import com.example.android.airteldemo.models.KeyAction
import kotlinx.android.synthetic.main.rv_item_key_action.view.*

class RvIssuesAdapter(val onClick: (KeyAction) -> Unit) : RecyclerView.Adapter<RvIssuesAdapter.KeyActionVH>() {

    // ArrayList to hold all the keyActions
    private val keyActions = arrayListOf<KeyAction>()

    fun setKeyActions(keyActionsList: ArrayList<KeyAction>) {
        // Clear previous data and add the new ones
        keyActions.clear()
        keyActions.addAll(keyActionsList)

        // Tell the adapter to reload the bindings to the viewHolder
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyActionVH {
        // Create the KeyActionVH
        return KeyActionVH(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_key_action, parent, false))
    }


    override fun onBindViewHolder(holder: KeyActionVH, position: Int) {
        // Bind data to specific viewHolder position
        holder.bind(keyActions[position])
    }

    override fun getItemCount() = keyActions.size

    inner class KeyActionVH(v: View) : RecyclerView.ViewHolder(v) {

        fun bind(keyAction: KeyAction) {

            with(itemView) {

                // Replace the - with space to clean-up the title
                tvTitle.text = keyAction.action?.replace("-"," ")

                setOnClickListener {
                    // Invoke the lambda with the keyAction when a click happens
                    onClick(keyAction)
                }

            }
        }
    }
}