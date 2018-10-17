package com.example.ceo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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

        // FIXME: Make this more elegant
        // Adding data to the graphs:

        GraphView happiness = findViewById(R.id.happiness);
        happiness.setTitleTextSize(52); // FIXME: move to the values

        // Setting the color to black
        happiness.getGridLabelRenderer().setGridColor(getColor(R.color.black));
        happiness.getGridLabelRenderer().setHorizontalLabelsColor(getColor(R.color.black));
        happiness.getGridLabelRenderer().setVerticalLabelsColor(getColor(R.color.black));
        happiness.getGridLabelRenderer().setVerticalAxisTitleColor(getColor(R.color.black));
        happiness.getGridLabelRenderer().setHorizontalAxisTitleColor(getColor(R.color.black));
        happiness.setTitleColor(getColor(R.color.black));

        // Setting the title
        happiness.setTitle(getResources().getString(R.string.customer_happiness_label));

        // Adding the data
        double x1[] = {2014, 2015, 2016, 2017, 2018};
        double y1[] = {3.4, 3.6, 4.0, 4.1, 4.35};
        happiness.addSeries(addSeries(x1, y1));

        GraphView orders = findViewById(R.id.orders);

        // Setting the color to black
        orders.setTitleTextSize(52); // FIXME: move to the values
        orders.getGridLabelRenderer().setGridColor(getColor(R.color.black));
        orders.getGridLabelRenderer().setHorizontalLabelsColor(getColor(R.color.black));
        orders.getGridLabelRenderer().setVerticalLabelsColor(getColor(R.color.black));
        orders.getGridLabelRenderer().setVerticalAxisTitleColor(getColor(R.color.black));
        orders.getGridLabelRenderer().setHorizontalAxisTitleColor(getColor(R.color.black));
        orders.setTitleColor(getColor(R.color.black));

        // Setting the title
        orders.setTitle(getResources().getString(R.string.orders_number_label));

        // Adding the data
        double y2[] = {300456, 356523, 420546, 490523, 545622};
        double x2[] = {2014, 2015, 2016, 2017, 2018};
        orders.addSeries(addSeries(x2, y2));

    }

    public LineGraphSeries<DataPoint> addSeries(double x[], double y[]) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < x.length; i++) {
            DataPoint point = new DataPoint(x[i], y[i]);
            series.appendData(point, true, 100, false);

        }

        return series;
    }
}
