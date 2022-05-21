package com.example.ch11_jetpack

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import org.study2.ch_11jecpack.R
import org.study2.ch_11jecpack.databinding.FragmentOneBinding
import org.study2.ch_11jecpack.databinding.ItemRecyclerviewBinding

//항목 View를 가지는 역활
class MyViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)
//항목 구성자. Adapter
class MyAdapter(val datas: MutableList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //아이템 개수
    override fun getItemCount(): Int {
        return datas.size
    }
    // 사용할 홀더
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    // 항목 하나의 홀더를 꾸미기 위한함수
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemData.text = datas[position]
    }



}
//RecyclerView 꾸미기
class MyDecoration(val context: Context): RecyclerView.ItemDecoration() {
    //모든 항목이 출력된후 호출
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //뷰 사이즈 계산
        val width = parent.width
        val height = parent.height
        //이미지 사이즈 계산
        val dr: Drawable? = ResourcesCompat.getDrawable(context.getResources(), R.drawable.kbo, null)
        val drWidth = dr?.intrinsicWidth
        val drHeight = dr?.intrinsicHeight
        //이미지가 그려질 위치 계산
        val left = width / 2 - drWidth?.div(2) as Int
        val top = height / 2 - drHeight?.div(2) as Int
        //이미지 출력
        c.drawBitmap(
            BitmapFactory.decodeResource(context.getResources(), R.drawable.kbo),
            left.toFloat(),
            top.toFloat(),
            null
        )
    }
    //각 항목을 꾸미기 위해서 호출
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val index = parent.getChildAdapterPosition(view) + 1

        if (index % 3 == 0) //left, top, right, bottom
            outRect.set(10, 10, 10, 60)
        else
            outRect.set(10, 10, 10, 0)

        view.setBackgroundColor(Color.parseColor("#28A0FF"))
        ViewCompat.setElevation(view, 20.0f)

    }
}

class OneFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOneBinding.inflate(inflater, container, false)
        //리사이클러뷰를 위한 가상 데이터 준비...........
        val datas = mutableListOf<String>()
        for(i in 1..9){
            datas.add("Item $i")
        }
        //리사이클러뷰에 LayoutManager, Adapter, ItemDecoration 적용
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager=layoutManager
        val adapter=MyAdapter(datas)
        binding.recyclerView.adapter=adapter
        binding.recyclerView.addItemDecoration(MyDecoration(activity as Context))

        return binding.root
    }
}

