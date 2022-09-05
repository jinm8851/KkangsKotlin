package org.study2.ch15_outer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*

//아우터 메니페스트에 인텐트필터로 액션 문자열 등록해 외부에서 실행되도록 함
class MyMessengerService : Service() {

    lateinit var messenger: Messenger
    lateinit var replyMessenger: Messenger
    lateinit var player:MediaPlayer

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
    /* 엑티비티로부터 데이터를 받아서 처리할수 있는 핸들러를 상속받아서 처리할수 있는 크래스*/
    inner class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ): Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) { //외부에서 전달되는 메세지를 받아서 핸들러메세지에서 처리
            when(msg.what){
                10 -> {replyMessenger = msg.replyTo //액티비티에서 10으로요청이들어왔을때
                if (!player.isPlaying){
                    player = MediaPlayer.create(this@MyMessengerService,R.raw.music)
                    try {
                        val replyMsg = Message() //요청이들어왔을때 넘기는데이타 메세지준비
                        replyMsg.what = 10
                        val replyBundle = Bundle()
                        replyBundle.putInt("duration",player.duration) //전체음원을 플레이할수있는 시간을담아서
                        replyMsg.obj = replyBundle // 오비제이로 액티비티에변환전달
                        replyMessenger.send(replyMsg)

                        player.start()
                    }catch (e : Exception) {
                        e.printStackTrace()
                    }
                }
                }
                20 -> { //20번으로 넘어오면 음악플레이 중지
                    if (player.isPlaying)
                        player.stop()
                }
                else -> super.handleMessage(msg)  // 나머지는 처리하지 않음
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {  //핸들러를 등록
        messenger = Messenger(IncomingHandler(this))  //서비스를 바인딩시킨곳에 메신저객체를 전달
        return messenger.binder  //메신저를 전달받아서 바인덜 핸들러에 전달해서 class IncomingHandler 핸들러에서 처리
    }
}