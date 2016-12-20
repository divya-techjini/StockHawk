package com.udacity.stockhawk.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.StockDetails;

import yahoofinance.Stock;


public class QuoteIntentService extends IntentService {

    public QuoteIntentService() {
        super(QuoteIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean isCheckStockSymbol = false;
        isCheckStockSymbol = intent.getBooleanExtra("IsCheckStockSymbol", false);
        if (isCheckStockSymbol) {

            String symbol = intent.getStringExtra("Symbol");
            boolean isStockFound = QuoteSyncJob.isStockFound(symbol, getApplicationContext());
            Intent dataUpdatedIntent = new Intent(QuoteSyncJob.ACTION_STOCK_EXIST);
            dataUpdatedIntent.putExtra("IS_STOCK_EXIST", isStockFound);
            dataUpdatedIntent.putExtra("SYMBOL", symbol);
            LocalBroadcastManager.getInstance(this).sendBroadcast(dataUpdatedIntent);
        } else if (intent.getBooleanExtra("IsGetHistory",false)) {
            String symbol = intent.getStringExtra("Symbol");
            Stock stock = QuoteSyncJob.getHistory(symbol, getApplicationContext());
            StockDetails.stock=stock;
            Intent dataUpdatedIntent = new Intent(QuoteSyncJob.ACTION_STOCK_HISTORY);
            LocalBroadcastManager.getInstance(this).sendBroadcast(dataUpdatedIntent);

        } else {



            try {

                QuoteSyncJob.getQuotes(getApplicationContext());
            } catch (Exception e) {
                /**
                 * Create Handler to post Toast on UI thread
                 * http://stackoverflow.com/a/20062900
                 */
                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Context context = getApplicationContext();
                        Toast.makeText(context, context.getString(R.string.error_stock_not_found), Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }

        }

    }
}
