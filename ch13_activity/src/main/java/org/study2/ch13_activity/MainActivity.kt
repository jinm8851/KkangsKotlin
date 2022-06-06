package org.study2.ch13_activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager


import org.study2.ch13_activity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var datas: MutableList<String>? = null
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //add................................

        /* 인텐트가 다시 돌아왔을때 엑티비틔를 실행시키기위해 라운처가필요함
        * val requestlauncher: ActivityResultLauncher<Intent> 여기에 받아둠
        * */
        val requestlauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            //사후처리 내용
            it.data!!.getStringExtra("result")?.let{
                datas?.add(it)
                adapter.notifyDataSetChanged()
            }
        }

        //유저가 mainFab버튼을 눌렀을때 리절트런처를 실행시켜 런처시킴
        binding.mainFab.setOnClickListener {
            val intent = Intent(this,AddActivity::class.java)
            requestlauncher.launch(intent)
        }

        // 번들에 상태값을 저장해 회전이나 꺼짐에 대응함함

        /* let 함수는  run 과 비슷하지만 확장함수 다입의 람다를 답지 않고 인자가 하나뿐인 함수 타입의
        *  람다를 받는다는 점이 다르다. 따라서 문맥 식의 값은 람다의 인자로 전달괸다. let 의 반환값은
        *  람다가 변환하는 값과 같다. 외부 영역에 새로운 변수를 도입하는 일을 피하고 싶을때 주로
        *  이 함수를 사용 한다.
        *  let 함수는 널이 아닌 경우에만 호출되기 때문에 컴파일러는 람다 안에서 it 파라미터가
        *  널이 될수 없는 값임을 알수있다.
        *  엘비스 연산자 ?: 왼쪽값이 널이면 오른쪽값 반화 그렇지 않으면 왼쪽값 반환*/
       datas = savedInstanceState?.let{
            it.getStringArrayList("datas")?.toMutableList()
        }?: let{
            mutableListOf<String>()
        }

        val layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.layoutManager=layoutManager
        adapter=MyAdapter(datas)
        binding.mainRecyclerView.adapter=adapter
        binding.mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    //add...............................
    // 번들 상태 데이터값을 저장함
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("datas",ArrayList(datas))
    }
}