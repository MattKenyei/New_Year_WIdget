package com.example.new_year;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

public class NewYearWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.d("NewYearWidget", "Updating widget with ID: " + appWidgetId);

        // Текущая дата
        Calendar today = Calendar.getInstance();

        // Дата Нового года
        Calendar newYear = Calendar.getInstance();
        newYear.set(Calendar.MONTH, Calendar.JANUARY);
        newYear.set(Calendar.DAY_OF_MONTH, 1);

        if (today.after(newYear)) {
            newYear.set(Calendar.YEAR, today.get(Calendar.YEAR) + 1);
        } else {
            newYear.set(Calendar.YEAR, today.get(Calendar.YEAR));
        }

        long diffInMillis = newYear.getTimeInMillis() - today.getTimeInMillis();
        long daysLeft = diffInMillis / (1000 * 60 * 60 * 24);

        // Получаем напоминание из SharedPreferences для конкретного виджета
        SharedPreferences prefs = context.getSharedPreferences("NewYearWidgetPrefs", Context.MODE_PRIVATE);
        String reminder = prefs.getString("reminder_" + appWidgetId, "Не забудьте про подарки!");

        Log.d("NewYearWidget", "Days left: " + daysLeft);
        Log.d("NewYearWidget", "Reminder: " + reminder);

        // Создаем RemoteViews для обновления виджета
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.tv_days_left, "Дней до Нового года: " + daysLeft);
        views.setTextViewText(R.id.tv_reminder, reminder);

        // Создание Intent для запуска MainActivity для обновления напоминания
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.button_update_reminder, pendingIntent);

        // Обновляем виджет
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    // Метод для изменения напоминания и обновления виджета
    public static void changeReminder(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String newReminder) {
        SharedPreferences prefs = context.getSharedPreferences("NewYearWidgetPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("reminder_" + appWidgetId, newReminder);
        editor.apply();

        updateAppWidget(context, appWidgetManager, appWidgetId);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        // handle other actions if needed
    }
}
