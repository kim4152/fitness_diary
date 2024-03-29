package com.diary.fitness_diary.calendar.dayDesign;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class Saturday implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
    public Saturday() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SATURDAY;
    }

    public void decorate(DayViewFacade view){
        view.addSpan(new ForegroundColorSpan(Color.BLUE));
    }

}

