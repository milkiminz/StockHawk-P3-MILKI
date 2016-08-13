package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;
import java.util.Collection;



public class StockDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int CURSOR_LOADRE_ID=0;
    private Cursor mCursor;
    private LineChartView LineChartView;
    private LineSet mLineSet;
    int maxRange,minRange,step;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        mLineSet=new LineSet();
        LineChartView =(LineChartView) findViewById(R.id.linechart);
        initLineChart();
        Intent intent =getIntent();
        Bundle args= new Bundle();
        args.putString(getResources().getString(R.string.string_symbol),intent.getStringExtra(getResources().getString(R.string.string_symbol)));
        getLoaderManager().initLoader(CURSOR_LOADRE_ID,args,this);
    }
    @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,QuoteProvider.Quotes.CONTENT_URI,new String[]{QuoteColumns.BIDPRICE},QuoteColumns.SYMBOL + "= ?",
                new String[]{args.getString(getResources().getString(R.string.string_symbol))},null);
    }
    @Override public void onLoadfinished(Loader<Cursor> loader){
        mCursor =data;
        findRange(mCursor);
        fillLineSet();

    }
    @Override public void onLoaderReset(Loader<Cursor> loader)
    {

    }
    private void fillLineSet()
    {
        mCursor.moveToFirst();
        for(int i=0;i<mCursor.getCount();i++){
            float price=Float.parseFloat(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE)));
            mLineSet.addPoint("test "+i,price);
            mCursor.moveToNext();
        }
        mLineSet.setColor(getResources().getColor(R.color.line_set)).setDotsStrokeThickness(Tools.fromDpToPx(2)).setDotsStrokeColor(getResources().getColor(R.color.line_stroke)).setDotsColor(getResources().getColor(R.color.line_stroke));
        lineChartView.addData(mLineSet);
        lineChartView.show();

    }
    private void initLineChart()
    {
        PaintgridPainy =new Paint();
        gridPaint.setColor(getResources().getColor(R.color.line_paint));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        grisPaint.setStrokeWidth(Tools.fromDpToPx(1f));


    }

}
