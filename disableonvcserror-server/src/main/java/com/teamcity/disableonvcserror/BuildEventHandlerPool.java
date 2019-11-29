package com.teamcity.disableonvcserror;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.intellij.openapi.diagnostic.Logger;

class BuildEventHandlerPool {

    private static final Logger Log = Logger.getInstance(BuildEventHandlerPool.class.getName());

    private ExecutorService myExecutorService;

    BuildEventHandlerPool() {
        myExecutorService = Executors.newFixedThreadPool(Constants.NUM_THREADS);
        Log.debug(myExecutorService.toString());
        Log.info(BuildEventHandlerPool.class.getName() + " initialized");
    }

    ExecutorService getMyExecutorService() {
        return myExecutorService;
    }
}
