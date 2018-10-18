package com.example.ceo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
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

        // FIXME: Make this more elegant
        // Adding data to the graphs:

        GraphView happiness = findViewById(R.id.happiness);
        graphSettings(happiness, getResources().getString(R.string.customer_happiness_label));

        // Adding the data
        double x1[] = {2014, 2015, 2016, 2017, 2018};
        double y1[] = {3.4, 3.6, 4.0, 4.1, 4.35};
        happiness.addSeries(addSeries(x1, y1));

        GraphView orders = findViewById(R.id.orders);
        graphSettings(orders, getResources().getString(R.string.orders_number_label));

        // Adding the data
        double y2[] = {300456, 356523, 420546, 490523, 545622};
        double x2[] = {2014, 2015, 2016, 2017, 2018};
        orders.addSeries(addSeries(x2, y2));

        GraphView income = findViewById(R.id.income);
        graphSettings(income, getResources().getString(R.string.income_number));
        double x3[] = {2014, 2015, 2016, 2017, 2018};
        double y3[] = {50000, 27000, 49000, 53400, 55000};
        income.addSeries(addBarSeries(x3, y3));

    }

    public LineGraphSeries<DataPoint> addSeries(double x[], double y[]) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < x.length; i++) {
            DataPoint point = new DataPoint(x[i], y[i]);
            series.appendData(point, true, 100, false);

        }

        return series;
    }

    public BarGraphSeries<DataPoint> addBarSeries(double x[], double y[]) {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        for (int i = 0; i < x.length; i++) {
            DataPoint point = new DataPoint(x[i], y[i]);
            series.appendData(point, true, 100, false);
            series.setSpacing(3);
        }
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        return series;
    }

    public void graphSettings(GraphView graph, String title){
        graph.getGridLabelRenderer().setGridColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setVerticalLabelsColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(getColor(R.color.black));
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getColor(R.color.black));
        graph.setTitleColor(getColor(R.color.black));

        graph.setTitle(title);
        graph.setTitleTextSize(52);

    }
}
