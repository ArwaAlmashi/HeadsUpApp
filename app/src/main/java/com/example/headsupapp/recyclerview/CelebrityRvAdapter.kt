package com.example.headsupapp.recyclerview

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.headsupapp.activity.CelebritiesDataActivity
import com.example.headsupapp.databinding.RowBinding
import com.example.headsupapp.model.CelebrityModel
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils


class CelebrityRvAdapter(private val context: Context, var celebrityList: CelebrityModel) : RecyclerView.Adapter<CelebrityRvAdapter.CelebrityViewHolder>() {

    private var lastPosition = -1
    // Holder
    class CelebrityViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root)

    // Adapter Fun's
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): CelebrityViewHolder {
        return CelebrityViewHolder(
                RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CelebrityViewHolder, position: Int) {

        val celebrity = celebrityList[position]
        holder.binding.apply {
            nameTv.text = celebrity.name
            wordsTv.text = "${celebrity.taboo1}, ${celebrity.taboo2}, ${celebrity.taboo3}"
        }
        setAnimation(holder.binding.row, position)
        holder.binding.row.setOnClickListener {
            CelebritiesDataActivity().updateAlert(context, celebrity)
        }
        holder.binding.deleteIcon.setOnClickListener {
            CelebritiesDataActivity().delete(celebrity, context)
        }

    }

    override fun getItemCount() = celebrityList.size

    // My Fun's
    @SuppressLint("NotifyDataSetChanged")
    fun updateCelebrityList(newList: CelebrityModel) {
        celebrityList = newList
        notifyDataSetChanged()
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {

            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

}
