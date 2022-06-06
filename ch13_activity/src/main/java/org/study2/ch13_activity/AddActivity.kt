package org.study2.ch13_activity


import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import org.study2.ch13_activity.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //add............................
/* 세이브버튼을 눌렀을때 인텐트를 넘기는 함수
*  인텐트에 엑스트라 함수로 담아서 넘겨줌 intent.putExtra("result", binding.addEditView.text.toString())
*  setResult(Activity.RESULT_OK,intent) 으로 오케이와 인텐트를 넘겨주고
*  finish() 함수로 자신을종료하면 startActivity로 add엑티비티는 종료되고 메인엑티비티로 넘어감*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.menu_add_save -> {
            val intent = intent
            intent.putExtra("result", binding.addEditView.text.toString())
            setResult(Activity.RESULT_OK,intent)
            finish()
            true
        }
        else -> true

    }
}