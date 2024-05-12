package com.example.studenthunt.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.studenthunt.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import kotlinx.android.synthetic.main.fragment_customize.view.*


class fragment_customize : Fragment() {

private lateinit var homeview:View
lateinit var switch: SwitchCompat
override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        homeview = inflater.inflate(R.layout.fragment_customize, container, false)
       switch = homeview.findViewById(R.id.switch1)
switch.setOnCheckedChangeListener { buttonView, isChecked ->
    if (isChecked){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
    else
    {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

        return homeview
    }


}