package com.reggya.faintek.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reggya.faintek.databinding.ItemUserBinding
import com.reggya.faintek.core.data.model.User

class UserAdapter: RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var users = ArrayList<User>()


    fun setNewDAta(newData: List<User>?){
        if (newData == null) return
        users.clear()
        users.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        if (user!= null){

            holder.bind(user)
        }
    }

    override fun getItemCount(): Int = users.size


    inner class ViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.name.text = user.name
            binding.email.text = user.email
            binding.status.text = if(user.verify == true) "verified" else "not verified"
        }
    }

}