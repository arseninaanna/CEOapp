package com.example.ceo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Objects;

public class GraphsActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        // Add the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.graphs_activity_label);
        setSupportActionBar(toolbar);

        // Set back button on toolbar
        toolbar.setNavigationOnClickListener(v -> finish());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        GraphView happiness = findViewById(R.id.happiness);
        double x[] = {1, 2, 3, 5};
        double y[] = {1, 2, 3, 4};
        LineGraphSeries<DataPoint> series = addSeries(x, y);
        happiness.addSeries(series);

    }

    public LineGraphSeries<DataPoint> addSeries(double x[], double y[]) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < x.length; i++) {
            DataPoint point = new DataPoint(x[i], y[i]);
            series.appendData(point, true, 100, false);

        }

        return series;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.back_arrow:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
