package widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by Mikli Minz on 13/08/16.
 */


public class QuoteWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent){
        return  new RemoteViewsFactory() {
            private Cursor data =null;
            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                        new String[]{
                                QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE, QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                        QuoteColumns.ISCURRENT + "= ?",
                        new String[]{"1"}, null);
            }


            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return  data ==null ? 0: data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if(position == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(position)){
                return null;
                }
                RemoteViews views =new RemoteViews(getPackageName(),R.layout.widget_collection_item);
                //bind data to the views
                views.setTextViewText(R.id.stock_symbol,data.getString(data.getColumnIndex(getResources().getString(R.string.string_symbol))));
                if (data.getInt(data.getColumnIndex(QuoteColumns.ISUP))==1){
                    views.setInt(R.id.change,getResources().getString(R.string.string_set_background_resources),R.drawable.percent_change_pill_green);

                }
                else {
                    views.setInt(R.id.change, getResources().getString(R.string.string_set_background_resource), R.drawable.percent_change_pill_red);

                }
                
            }


            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        }
    }
}
