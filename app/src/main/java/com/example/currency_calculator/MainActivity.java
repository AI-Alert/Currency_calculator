package com.example.currency_calculator;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.widget.AdapterView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Document doc;
    private Thread secThread;
    private Runnable runnable;
    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;

//    protected void display(int i) {
//        TextView t = findViewById(R.id.display_text_view);
//        t.setText("" + i);
//    }
//    int dollars = 40;
//    int dollarsToYen = 119;
//    int yen = dollarsToYen * dollars;
//    display(yen);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Button act2 = (Button)findViewById(R.id.button);
        act2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Activity2.class);
                startActivity(intent);
            }
        });
    }

    private void init()
    {
        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new CustomArrayAdapter(this,R.layout.list_item_1,arrayList,getLayoutInflater());
        listView.setAdapter(adapter);


        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }
    private void getWeb()
    {

        try {
            doc = Jsoup.connect("http://www.cbr.ru/scripts/XML_daily.asp").get();
            Elements tables = doc.getElementsByTag("ValCurs");
            Element our_table = tables.get(0);
            Log.d("MyLog","Количество элементов списка: " + our_table.childrenSize() + our_table.children());

            for(int i = 0;i < our_table.childrenSize();i++ )
            {
                ListItemClass items = new ListItemClass();
                items.setData_1(our_table.children().get(i).child(1).text());
                items.setData_2(our_table.children().get(i).child(2).text());
                items.setData_3(our_table.children().get(i).child(3).text());
                items.setData_4(our_table.children().get(i).child(4).text());
                arrayList.add(items);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
