package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var userList = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.user_item_holder, parent, false)

        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.setItem(user)
        updateBackgroundColor(user.light1 ?: "0", holder.cardView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setItems(list: MutableList<User>) {
        this.userList = list
        notifyDataSetChanged()
    }

    private fun updateBackgroundColor(light1: String, cardView: CardView) {
        val backgroundColor = if (light1 == "1") {
            R.color.red // Màu đỏ
        } else {
            R.color.blue // Màu xanh
        }
        cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, backgroundColor))
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        private val tvId: TextView = itemView.findViewById(R.id.tv_id)

        fun setItem(data: User) {
            tvId.text = data.id
        }
    }
}
