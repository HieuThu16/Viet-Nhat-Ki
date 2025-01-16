package com.example.vitnhtk.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> selectedDate;
    private final MutableLiveData<Boolean> saveStatus;

    public HomeViewModel() {
        selectedDate = new MutableLiveData<>();
        saveStatus = new MutableLiveData<>();
    }

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String date) {
        selectedDate.setValue(date);
    }

    public LiveData<Boolean> getSaveStatus() {
        return saveStatus;
    }

    public void saveJournalEntry(String date, String positive, String negative,
                                 String progress, String learned) {
        // TODO: Implement actual saving logic (e.g., to Room database)

        // For now, just log the data
        System.out.println("Date: " + date);
        System.out.println("Positive: " + positive);
        System.out.println("Negative: " + negative);
        System.out.println("Progress: " + progress);
        System.out.println("Learned: " + learned);

        // Notify observers that save was successful
        saveStatus.setValue(true);
        // Reset save status after a brief delay
        new android.os.Handler().postDelayed(
                () -> saveStatus.setValue(false),
                100
        );
    }
}