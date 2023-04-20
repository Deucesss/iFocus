package com.rencaihu.timer.ui.ongoingtimer;

import android.os.Handler;
import android.os.Message;

import timber.log.Timber;

public class TestTimer extends Handler {

    private int totalSeconds;

    private long lastMills = 0L;

    public TestTimer(int totalSeconds) {

        super();

        this.totalSeconds = totalSeconds;

    }

    public void start() {

        Message msg = obtainMessage();

        sendMessage(msg);

    }

    @Override

    public void handleMessage(Message msg) {

        super.handleMessage(msg);

        boolean isContinue = true;

        if (totalSeconds > 0) {

            // do sth when tick
            Timber.d("tick: %d", System.currentTimeMillis());

        } else {

            isContinue = false;

        }

        if (isContinue) {

            totalSeconds --;

            long interval = 1000;

            long now = System.currentTimeMillis();

            if (lastMills != 0L) {

                interval = interval - (now - lastMills);

                lastMills += 1000;

            } else {

                lastMills = now + 1000;

            }

            Message message = obtainMessage();

            sendMessageDelayed(message, interval);

        } else {

            finish();

        }

    }

    private void finish() {

// do things when finish

    }

}