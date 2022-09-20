package org.study2.ch17_database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// 이 헬퍼를 이용해서 데이터를 저장 혹은 획득가능
class DBHelper(context: Context): SQLiteOpenHelper(context,"testdb",null,1) {
//    애플리케이션에서 데이타베이스를  최초로 이용되는 순간 (onCreate 에서 테이블을 만듬)
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("create table TODO_TB ("+
                "_id integer primary key autoincrement,"+
                "todo not null)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
//        버전이 바뀔때 마다 콜됨
    }
}