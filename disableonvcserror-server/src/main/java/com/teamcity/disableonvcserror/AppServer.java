package com.teamcity.disableonvcserror;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import jetbrains.buildServer.serverSide.BuildServerListener;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.util.EventDispatcher;

public class AppServer {
    private static final Logger Log = Logger.getInstance(AppServer.class.getName());

    private final EventDispatcher<BuildServerListener> myEventDispatcher;

    public AppServer(@NotNull EventDispatcher<BuildServerListener> myEventDispatcher,
                     @NotNull SBuildServer mySBuildServer) {
        this.myEventDispatcher = myEventDispatcher;

        Constants.ADMIN_SUSER = mySBuildServer.getUserModel().findUserAccount(null, Constants.ADMIN_USERNAME);
        Constants.NOTIFICATION_EMAIL_IDS = new HashSet<>(Collections.singletonList(Constants.ADMIN_SUSER.getEmail()));

        BuildEventListener buildEventListener = new BuildEventListener(mySBuildServer);
        AddListener(buildEventListener);
        Log.info(AppServer.class.getName() + " initialized");
    }

    private void AddListener(BuildEventListener buildEventListener) {
        Log.info("Registering the event listener");
        myEventDispatcher.addListener(buildEventListener);
    }
}
