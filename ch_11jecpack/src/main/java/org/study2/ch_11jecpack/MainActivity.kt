package org.study2.ch_11jecpack


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ch11_jetpack.OneFragment
import com.example.ch11_jetpack.ThreeFragment
import com.example.ch11_jetpack.TwoFragment
import org.study2.ch_11jecpack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    //ViewPagerAdapter 작성..
    class MyFragmentPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
        //옆으로 넘길때 나오는 프레그먼트
        val fragments: List<Fragment>
        init {
            fragments = listOf(OneFragment(),TwoFragment(),ThreeFragment())
        }

        //프레그먼트 갯수
        override fun getItemCount(): Int {
          return  fragments.size
        }
        //프레그먼트 포지션
        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
        setContentView(binding.root)

        //원래 액션바에 들어갈 내용을 툴바에넘기기
        setSupportActionBar(binding.toolbar)

        toggle = ActionBarDrawerToggle(this,binding.drawer,R.string.drawer_opened,R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        val adapter = MyFragmentPagerAdapter(this)
        binding.viewpager.adapter=adapter
    }

    //메뉴를 구성하기위한 함수
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main,menu)


        val menuItem = menu?.findItem(R.id.menu_search)   // 메뉴 아이템객체 얻기
        val searchView = menuItem?.actionView as SearchView  //서치뷰 얻기

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{  //서치뷰 이벤트 처리리

            override fun onQueryTextSubmit(query: String?): Boolean {  //검색처리리
               Log.d("KKang","search text: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {  //유저가 검색하기 위해 한자한자 입력할때 이벤트
                return true
            }
        })
        return true
    }
  //토글버튼처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true

        }
        return super.onOptionsItemSelected(item)
    }
}