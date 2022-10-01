package org.study2.ch18_network.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.study2.ch18_network.databinding.ItemMainBinding
import org.study2.ch18_network.model.ItemModel


class MyViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)

class MyAdapter(val context: Context, val datas: MutableList<ItemModel>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding

        //add......................................
        //서버에서 다운받은 이미지를 글라이드로 작성
        val model = datas!![position]
        binding.itemTitle.text = model.title
        binding.itemDesc.text = model.description
        binding.itemTime.text = "${model.author } At ${model.publishedAt}"
        //이미지 유알엘이 찍히면 안되고 다운로드이미지가 보여야함 그걸 글라이드로작성(레트로빗과발리도 가능)
        Glide.with(context)
                //서버로 부터 받은 이미지 문자
            .load(model.urlToImage)
                //출력될이미지 주소
            .into(binding.itemImage)

    }
}