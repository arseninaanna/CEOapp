package com.example.ceo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphsActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        GraphView happiness = (GraphView) findViewById(R.id.happiness);
        double x[] = {1, 2, 3, 5};
        double y[] = {1, 2, 3, 4};
        LineGraphSeries<DataPoint> series = addSeries(x, y);
        happiness.addSeries(series);

    }

    public LineGraphSeries<DataPoint> addSeries(double x[],double y[]){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for(int i = 0; i < x.length; i++){
            DataPoint point = new DataPoint(x[i], y[i]);
            series.appendData(point, true, 100, false);

        }

        return series;
    }
}
