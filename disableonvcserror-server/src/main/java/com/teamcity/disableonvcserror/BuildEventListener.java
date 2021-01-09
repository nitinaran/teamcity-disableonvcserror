package com.teamcity.disableonvcserror;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

import com.intellij.openapi.diagnostic.Logger;

import jetbrains.buildServer.notification.MessageSender;
import jetbrains.buildServer.serverSide.*;

public class BuildEventListener extends BuildServerAdapter{

    private static final Logger Log = Logger.getInstance(BuildEventListener.class.getName());

    private final BuildEventHandlerPool myBuildEventHandlerPool = new BuildEventHandlerPool();
    private final ExecutorService myExecutorService = myBuildEventHandlerPool.getMyExecutorService();
    private final SBuildServer mySBuildServer;

    BuildEventListener(SBuildServer mySBuildServer) {
        this.mySBuildServer = mySBuildServer;
        Log.info(BuildEventListener.class.getName() + " initialized");
    }

    @Override
    public void buildFinished(SRunningBuild build) {
        EventHandler worker = new EventHandler(build);
        myExecutorService.execute(worker);
    }

    @Override
    public void serverShutdown() {
        myExecutorService.shutdown();
        while (!myExecutorService.isTerminated()) {
        }
        Log.info("Server shutdown requested, so stopped all the threads");
    }

    @Override
    public void pluginsLoaded() {
        Collection<MessageSender> messageSenders = mySBuildServer.getExtensions(MessageSender.class);
        for (MessageSender a: messageSenders) {
            if (a.describe().toLowerCase().equals("email notifier")) {
                Constants.EMAIL_NOTIFIER = a;
                break;
            }
        }
        if (Constants.EMAIL_NOTIFIER == null)
            Log.info("Unable to find the email notifier");
        else
            Log.info("Email notifier found");
    }
}
