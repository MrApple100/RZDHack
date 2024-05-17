package com.example.realitylabandroid.viewmodel.component

import androidx.lifecycle.MutableLiveData

public class StateButton(text: String, enabled: Boolean) {
    public var text:MutableLiveData<String>
    public var enabled:MutableLiveData<Boolean>

    init {
        this.text = MutableLiveData(text)
        this.enabled = MutableLiveData(enabled)
    }

    public fun change(text:String,enabled: Boolean){
        this.text.postValue(text)
        this.enabled.postValue(enabled)
    }




}