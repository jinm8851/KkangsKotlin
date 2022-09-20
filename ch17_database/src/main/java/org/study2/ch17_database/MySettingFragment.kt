package org.study2.ch17_database

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


class MySettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//        이렇게만해도 작동함
        setPreferencesFromResource(R.xml.settings,rootKey)
//        추가 세팅 코드
//        setting key값으로 설정객체를 얻어냄
        val idPreference: EditTextPreference? = findPreference("id")
        val colorPreference: ListPreference? = findPreference("color")

//        화면에 나오는 설명을 코드로 변경시킴
        colorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        idPreference?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference>{
                preference ->
                val text = preference.text  //유저가 설정한 값을 얻어냄
                if (TextUtils.isEmpty(text)){ //비어있다면
                    "설정이 되지 않았습니다."
                }else {                         //비어있지 않다면
                    "설정된 id 값은 $text 입니다"
                }
            }
    }
}