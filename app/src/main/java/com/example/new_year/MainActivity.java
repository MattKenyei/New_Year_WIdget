package com.example.new_year;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextReminder;
    private Button buttonUpdate;
    private ListView widgetListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextReminder = findViewById(R.id.editTextReminder);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        widgetListView = findViewById(R.id.widgetList);

        // Получаем список виджетов
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName thisWidget = new ComponentName(this, NewYearWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Преобразуем массив идентификаторов в список строк
        String[] widgetIds = new String[appWidgetIds.length];
        for (int i = 0; i < appWidgetIds.length; i++) {
            widgetIds[i] = "Виджет " + appWidgetIds[i];  // Преобразуем идентификатор в строку
        }

        // Отображаем список виджетов
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, widgetIds);
        widgetListView.setAdapter(adapter);

        // Обработчик выбора виджета
        widgetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный ID виджета
                int appWidgetId = appWidgetIds[position];

                // Обновляем напоминание для выбранного виджета
                String newReminder = editTextReminder.getText().toString();
                if (!newReminder.isEmpty()) {
                    updateWidgetReminder(newReminder, appWidgetId);
                } else {
                    Toast.makeText(MainActivity.this, "Введите напоминание", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Обработчик кнопки обновления
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newReminder = editTextReminder.getText().toString();
                if (!newReminder.isEmpty()) {
                    updateWidgetReminder(newReminder, -1);  // Обновляем все виджеты, если не выбран конкретный
                } else {
                    Toast.makeText(MainActivity.this, "Введите напоминание", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateWidgetReminder(String newReminder, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName thisWidget = new ComponentName(this, NewYearWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Если не выбран конкретный виджет, обновляем все виджеты
        if (appWidgetId == -1) {
            for (int id : appWidgetIds) {
                NewYearWidget.changeReminder(this, appWidgetManager, id, newReminder);
            }
        } else {
            NewYearWidget.changeReminder(this, appWidgetManager, appWidgetId, newReminder);
        }

        Toast.makeText(this, "Напоминание обновлено!", Toast.LENGTH_SHORT).show();
    }
}
