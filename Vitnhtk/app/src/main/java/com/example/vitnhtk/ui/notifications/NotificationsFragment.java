package com.example.vitnhtk.ui.notifications;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.app.AlertDialog;


import com.example.vitnhtk.R;

import java.util.Calendar;

public class NotificationsFragment extends Fragment {

    private AppCompatButton btnSelectTime, btnSetNotification;
    private AppCompatTextView tvSelectedTime;
    private int selectedHour = -1, selectedMinute = -1;
    private static final int NOTIFICATION_REQUEST_CODE = 123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        initializeViews(rootView);
        setupClickListeners();
        return rootView;
    }

    private void initializeViews(View rootView) {
        btnSelectTime = rootView.findViewById(R.id.btn_select_time);
        btnSetNotification = rootView.findViewById(R.id.btn_set_notification);
        tvSelectedTime = rootView.findViewById(R.id.tv_selected_time);
    }

    private void setupClickListeners() {
        btnSelectTime.setOnClickListener(v -> showTimePickerDialog());
        btnSetNotification.setOnClickListener(v -> setNotification());
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    updateSelectedTimeDisplay();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void updateSelectedTimeDisplay() {
        tvSelectedTime.setText(String.format("Giờ đã chọn: %02d:%02d", selectedHour, selectedMinute));
    }

    private void setNotification() {
        if (!isTimeSelected()) {
            showErrorDialog();
            return;
        }

        Calendar notificationTime = calculateNotificationTime();
        scheduleNotification(notificationTime);
        showSuccessDialog(notificationTime);
    }

    private boolean isTimeSelected() {
        return selectedHour != -1 && selectedMinute != -1;
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Lỗi")
                .setMessage("Vui lòng chọn giờ trước khi đặt thông báo.")
                .setPositiveButton("OK", null)
                .show();
    }

    private Calendar calculateNotificationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // If time has passed today, schedule for tomorrow
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return calendar;
    }

    private void scheduleNotification(Calendar notificationTime) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), NotificationReceiver.class);

        // Add extra data if needed
        intent.putExtra("notification_hour", selectedHour);
        intent.putExtra("notification_minute", selectedMinute);

        // Use FLAG_IMMUTABLE for Android 12 and above
        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE :
                PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                NOTIFICATION_REQUEST_CODE,
                intent,
                flags
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    notificationTime.getTimeInMillis(),
                    pendingIntent
            );
        } else {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    notificationTime.getTimeInMillis(),
                    pendingIntent
            );
        }
    }

    private void showSuccessDialog(Calendar notificationTime) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Thông báo")
                .setMessage(String.format("Thông báo sẽ được gửi vào lúc %02d:%02d",
                        selectedHour, selectedMinute))
                .setPositiveButton("OK", null)
                .show();
    }
}