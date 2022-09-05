package org.study2.ch15_service

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.appcompat.resources.Compatibility
import androidx.appcompat.resources.Compatibility.Api18Impl.setAutoCancel

//잡 서비스도 메니페스트에 등록필요 퍼미션 등록필요
@TargetApi(Build.VERSION_CODES.LOLLIPOP)  //롤리팝이상
class MyJobService : JobService() { //안드로이드 시스템에의해 특정상황이 되면 실행 (잡 서비스를 상속받음)
    override fun onStartJob(p0: JobParameters?): Boolean {  //잡 서비스가 실행 됐을때 실행되는 코드
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("oneId","oneName",NotificationManager.IMPORTANCE_DEFAULT)
            channel.description= "oneDesc"
            manager?.createNotificationChannel(channel)
            Notification.Builder(this,"oneId")
        }else{
            Notification.Builder(this)
        }.run { setSmallIcon(android.R.drawable.ic_notification_overlay)
        setContentText("Content Message"    )
        setContentTitle("JobScheduler Title")
            setAutoCancel(true)
            manager?.notify(1,build())
        }
        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }


}