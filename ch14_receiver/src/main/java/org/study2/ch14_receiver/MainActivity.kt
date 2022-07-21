package org.study2.ch14_receiver

import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.study2.ch14_receiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!.apply { //리시버를 적용하지안고 정보만사용하겠다.
            when(getIntExtra(BatteryManager.EXTRA_STATUS,-1)){ // 정보를 가져와서 분기프로그램작성
                BatteryManager.BATTERY_STATUS_CHARGING -> {
                    when(getIntExtra(BatteryManager.EXTRA_PLUGGED,-1 )){ //충전중이면
                        BatteryManager.BATTERY_PLUGGED_USB -> {  //usb 충전중이면
                            binding.chargingResultView.text = "USB Plugged"
                            binding.chargingImageView.setImageBitmap(BitmapFactory.decodeResource(
                                resources,R.drawable.usb
                            ))
                        }
                        BatteryManager.BATTERY_PLUGGED_AC -> { // AC 충전중이면
                            binding.chargingResultView.text = "AC Plugged"
                            binding.chargingImageView.setImageBitmap(BitmapFactory.decodeResource(
                                resources,R.drawable.ac
                            ))
                        }
                    }
                }
                else -> {  //충전중이 아니면
                    binding.chargingResultView.text = "Not Plugged"
                }
            }
            // 밧데리 남은 용량계산
            val level = getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
            val scale = getIntExtra(BatteryManager.EXTRA_SCALE,-1)
            val batteryPct = level/scale.toFloat() *100
            binding.percentResultView.text = "$batteryPct %"
        }

        binding.button.setOnClickListener {
            val intent = Intent(this, MyReceiver::class.java)
            sendBroadcast(intent)
        }
    }
}