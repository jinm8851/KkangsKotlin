package org.study2.ch15_service

import android.annotation.TargetApi
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.study2.ch15_outer.MyAIDLInterface
import org.study2.ch15_service.databinding.ActivityMainBinding
// 프로세스 간 통신하기(주석 달기 완료)
//아웃터 쪽 서비스를 이용하기 위해서 메니페스트에 팩키지등록 (아웃터 정보에 접근할수 있게) ..
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var connectionMode = "none"

    //Messenger......
    lateinit var messenger: Messenger
    lateinit var replyMessenger: Messenger
    var messengerJob: Job? = null  //코루틴쪽 잡


    //aidl...........
    //aidl 폴더를 만들고 아웃터랑 동일한 팩키지 명으로 만들고 아웃터에 화일을 복사 그리고 빌드에 메이크모듈실행
        var aidlService: MyAIDLInterface? = null
    var aidlJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //messenger..........
        onCreateMessengerService()

        //aidl................

        onCreateAIDLService()

        //jobscheduler......................
        onCreateJobScheduler()

    }

    override fun onStop() {
        super.onStop()
        if(connectionMode === "messenger"){
            onStopMessengerService()
        }else if(connectionMode === "aidl"){
            onStopAIDLService()
        }
        connectionMode="none"
        changeViewEnable()
    }

    fun changeViewEnable() = when (connectionMode) {
        "messenger" -> {
            binding.messengerPlay.isEnabled = false
            binding.aidlPlay.isEnabled = false
            binding.messengerStop.isEnabled = true
            binding.aidlStop.isEnabled = false
        }
        "aidl" -> {
            binding.messengerPlay.isEnabled = false
            binding.aidlPlay.isEnabled = false
            binding.messengerStop.isEnabled = false
            binding.aidlStop.isEnabled = true
        }
        else -> {
            //초기상태. stop 상태. 두 play 버튼 활성상태
            binding.messengerPlay.isEnabled = true
            binding.aidlPlay.isEnabled = true
            binding.messengerStop.isEnabled = false
            binding.aidlStop.isEnabled = false

            binding.messengerProgress.progress = 0
            binding.aidlProgress.progress = 0
        }
    }

    //messenger handler ......................
    //데이터가 전달됐을때 데이터를 받아서 처리하는 핸들러
    inner class HandlerReplyMsg: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                10 -> { //10번을 데이터를 받으면
                    val bundle = msg.obj as Bundle //전달받은 데이터를 번들타입으로 변환
                    bundle.getInt("duration")?.let { //키 값을 받아서
                        when {
                            it > 0 -> {
                                binding.messengerProgress.max = it //프로그래스바의 백스값을 전달받은 시간으로 설정
                                //프로그래스바 값을 증가 (기간이 길어 코루틴으로 처리)
                                val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
                                messengerJob = backgroundScope.launch {
                                    while (binding.messengerProgress.progress < binding.messengerProgress.max){
                                        delay(1000)
                                        binding.messengerProgress.incrementProgressBy(1000)
                                    }
                                }
                                changeViewEnable() //화면을 인에이블 디세이블
                            }
                            else -> {
                                connectionMode = "none"
                                unbindService(messengerConnection)
                                changeViewEnable()
                            }
                        }
                    }
                }
            }
        }
    }

    //messenger connection ....................
    //바인드 서비스로 이용이 되는것이면 바인드 매개변수로 지정해야되는 서비스커넥션이 객체가 필요
    val messengerConnection: ServiceConnection = object : ServiceConnection { /* messengerConnection 을
    바인드 서비스의 매개변수로 지정하면 바인드서비스에 의해서 바인드 서비스로 이용가능하면 onServiceConnected 실행*/
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) { //바인드 서비스로 이용가능하면 실행
            // p1: IBinder 이 매개변수가 서비스 쪽에서 넘어온 객체 (지금은 메신져 객체) 초기데이터를 넘기고있음
            messenger = Messenger(p1)
            val msg = Message()
            msg.replyTo = replyMessenger
            msg.what = 10
            messenger.send(msg)
            connectionMode = "messenger"
        }

        override fun onServiceDisconnected(p0: ComponentName?) {  //이용가능하지 않으면 실행

        }
    }


    private fun onCreateMessengerService() {
        replyMessenger = Messenger(HandlerReplyMsg())
        binding.messengerPlay.setOnClickListener { //플레이 버튼을 눌렀을때  서비스 연결
            val intent = Intent("ACTION_SERVICE_Messenger")
            intent.setPackage("org.study2.ch15_outer")
            bindService(intent,messengerConnection, Context.BIND_AUTO_CREATE)
        }
        binding.messengerStop.setOnClickListener {
            val msg = Message()
            msg.what = 20
            messenger.send(msg)
            unbindService(messengerConnection)
            messengerJob?.cancel()
            connectionMode = "none"
            changeViewEnable()
        }
    }
    private fun onStopMessengerService() {
        val msg = Message()
        msg.what = 20
        messenger.send(msg)
        unbindService(messengerConnection)
    }

    //aidl connection .......................
    // 바인드와 연결할수 있는 서비스 커넥션 만들기
    val aidlConnection: ServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            aidlService = MyAIDLInterface.Stub.asInterface(p1) //스터브 객체를 MyAIDLInterface 로 이용하겠다.
            aidlService!!.start() //aidl 방식은 인터페이스에 있는 함수를 바로 콜하는 방식
            binding.aidlProgress.max = aidlService!!.maxDuration
            val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
            aidlJob = backgroundScope.launch {
                while (binding.aidlProgress.progress < binding.aidlProgress.max ){
                    delay(1000)
                    binding.aidlProgress.incrementProgressBy(1000)
                }
            }
            connectionMode = "aidl"
            changeViewEnable()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            aidlService = null
        }
    }

    private fun onCreateAIDLService() {
        binding.aidlPlay.setOnClickListener {
            val intent = Intent("ACTION_SERVICE_AIDL")
            intent.setPackage("org.study2.ch15_outer")
            bindService(intent,aidlConnection,Context.BIND_AUTO_CREATE)
        }
        binding.aidlStop.setOnClickListener {
            aidlService!!.stop()
            unbindService(aidlConnection)
            aidlJob?.cancel()
            connectionMode = "none"
            changeViewEnable()
        }

    }
    private fun onStopAIDLService() {
        unbindService(aidlConnection)

    }

    //JobScheduler
    // 잡 서비스 등록
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onCreateJobScheduler(){
        //잡 스케줄러라는 시스템 서비스를 얻어내고
        var jobScheduler: JobScheduler? = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        // 정보 등록
        val builder = JobInfo.Builder(1, ComponentName(this,MyJobService::class.java))
        //언제 서비스를 실행시키는지 (와이파이가 연결됐을때)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
        //정보를 주기
        val jobInfo = builder.build()
        jobScheduler!!.schedule(jobInfo)
    }

}