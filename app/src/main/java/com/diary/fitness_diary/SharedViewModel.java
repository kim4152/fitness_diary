package com.diary.fitness_diary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Boolean> someEvent = new MutableLiveData<>();

    public LiveData<Boolean> getSomeEvent() {
        return someEvent;
    }

    public void triggerSomeEvent() {
        someEvent.setValue(true);
    }
}
