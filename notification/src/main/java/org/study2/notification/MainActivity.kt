package org.study2.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import org.study2.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
        setContentView(binding.root)

        binding.notificationButton.setOnClickListener{
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

//노티피케이션객체는 빌더에 의해서 만들어진다.
            val builder: NotificationCompat.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){// 26 버전 이상
                val channelId = "one-channel"
                val channelName = "My Channel One"
                val channel = NotificationChannel(
                    channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
//                    채널에 다양한 정보 설정
                    description = "My Channel One Description"
                    setShowBadge(true)
                    val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                    setSound(uri,audioAttributes)
                    enableVibration(true)
                }
//                채널을 Notification에 등록
                manager.createNotificationChannel(channel)
                //채널을 이용하여 builder 생성
                builder = NotificationCompat.Builder(this,channelId)
            }else {//26버전 이하
                builder = NotificationCompat.Builder(this)
            }
//            상태바에 나오는 내용 (알림의 기본정보)
            builder.run {
                setSmallIcon(R.drawable.small)
                setWhen(System.currentTimeMillis())
                setContentTitle("홍길동")
                setContentText("안녕하세요")
                setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.big))
            }

            val KEY_TEXT_REPLY = "key_text_reply"
            var replyLabel = "답장"
            var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
                setLabel(replyLabel)
                build()
            }
//            브로드케스트를 실행하기위해 인텐트를 발생시키기전 펜딩인텐트로 감싸줌
            val replyIntent = Intent(this,ReplyReceiver::class.java)
            val replyPendingIntent = PendingIntent.getBroadcast(
                this,30,replyIntent,PendingIntent.FLAG_MUTABLE
            )
//            노티피게이션 액션에 붙여서 발생시킴
//            리모트 인풋으로 답장을 누르면 브로드캐스트로 답장을 보냄
            builder.addAction(
                NotificationCompat.Action.Builder(
                    R.drawable.send,
                    "답장",
                    replyPendingIntent
                ).addRemoteInput(remoteInput).build()
            )

            manager.notify(11,builder.build())
        }
    }
}