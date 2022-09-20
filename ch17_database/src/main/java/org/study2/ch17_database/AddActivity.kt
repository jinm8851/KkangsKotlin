package org.study2.ch17_database

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import org.study2.ch17_database.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_add, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_add,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.menu_add_save ->{
            //add........................
//                유저 입력텍스트 획득
            val inputData = binding.addEditView.text.toString()
            val db = DBHelper(this).writableDatabase
            db.execSQL("insert into TODO_TB (todo) values (?)",
            arrayOf<String>(inputData)
            )
            db.close()
//            저장처리가 완료된면 피니쉬시키고 메인엑티비티로 화면전환,유저가 저장한 데이터를 메인액티비티로 보내줌
            val intent = intent
            intent.putExtra("result",inputData)
            setResult(Activity.RESULT_OK,intent)
            finish()
            true
        }
        else -> true
    }
}