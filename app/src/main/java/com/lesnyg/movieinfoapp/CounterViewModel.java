package com.lesnyg.movieinfoapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CounterViewModel extends ViewModel {
    public MutableLiveData<Integer> goodcounter = new MutableLiveData<>();
    public MutableLiveData<Integer> bedcounter = new MutableLiveData<>();

    public MutableLiveData<Integer> getGoodcounter() {
        return goodcounter;
    }

    public void setGoodcounter(MutableLiveData<Integer> goodcounter) {
        this.goodcounter = goodcounter;
    }

    public MutableLiveData<Integer> getBedcounter() {
        return bedcounter;
    }

    public void setBedcounter(MutableLiveData<Integer> bedcounter) {
        this.bedcounter = bedcounter;
    }


    public CounterViewModel() {
        goodcounter.setValue(0);
        bedcounter.setValue(0);
    }


    public void increase() {
        goodcounter.setValue(goodcounter.getValue() + 1);
    }

    public void decrease() {
        bedcounter.setValue(bedcounter.getValue() + 1);
    }
}
