package org.study2.ch15_outer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

//메니페스트에  인텐트 필터를 등록해줘야 외부에서 사용할수 있음
class MyAIDLService : Service() {

    lateinit var player: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onBind(intent: Intent): IBinder {  //요청이 들어왔을때
        return object : MyAIDLInterface.Stub() {  //  객체를 MyAIDLInterface.Stub()리던
            // 프로세스간에 통신을 할수 있는 Stub()를 이용
             override fun getMaxDuration(): Int {  //외부함수를 오버라이드 해서 이용함
                return if (player.isPlaying)
                    player.duration
                    else 0

            }

            override fun start() {  //스타트함수가 콜됐을때
                if (!player.isPlaying)
                    player = MediaPlayer.create(this@MyAIDLService,R.raw.music)  //음악준비
                try {
                    player.start()  //플레이
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun stop() {  //스탑함수 콜됐을때
                if (player.isPlaying)
                    player.stop()
            }
        }
    }
}