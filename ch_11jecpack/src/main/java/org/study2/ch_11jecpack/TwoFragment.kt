package com.example.ch11_jetpack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.study2.ch_11jecpack.databinding.FragmentTwoBinding

class TwoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentTwoBinding.inflate(inflater, container, false).root
    }

}