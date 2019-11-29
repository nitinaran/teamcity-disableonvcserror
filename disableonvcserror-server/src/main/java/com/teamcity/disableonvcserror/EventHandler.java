package com.teamcity.disableonvcserror;

import com.intellij.openapi.diagnostic.Logger;

import jetbrains.buildServer.BuildProblemData;
import jetbrains.buildServer.serverSide.SBuildAgent;
import jetbrains.buildServer.serverSide.SRunningBuild;

public class EventHandler implements Runnable {

    private static final Logger Log = Logger.getInstance(EventHandler.class.getName());

    private final SRunningBuild myRunningBuild;

    EventHandler(SRunningBuild myRunningBuild) {
        this.myRunningBuild = myRunningBuild;
    }

    @Override
    public void run() {
        Log.info(Thread.currentThread().getName() + "::Running job for build id: " + myRunningBuild.getBuildId());
        for (BuildProblemData buildProblem: myRunningBuild.getFailureReasons()) {
            if (buildProblem.getType().matches("UPDATE_SOURCES|CHECKING_FOR_CHANGES_ERROR|PREPARATION_FAILURE_TYPE")) {
                SBuildAgent myAgent = myRunningBuild.getAgent();
                Log.info(Thread.currentThread().getName() + "::Disabling the agent: " + myAgent.getName());
                myAgent.setEnabled(false, Constants.ADMIN_SUSER, "Automatically disabled because of " + buildProblem.getType());
                if (Constants.EMAIL_NOTIFIER != null) {
                    Log.info("Sending email to: " + Constants.NOTIFICATION_EMAIL_IDS);
                    Constants.EMAIL_NOTIFIER.sendMessage(
                        "Agent " + myAgent.getName() + " Disabled Automatically",
                        "...due to\n\n\n" + buildProblem.getDescription(),
                        Constants.NOTIFICATION_EMAIL_IDS
                    );
                } else {
                    Log.info("Not able to send emails");
                }
                break;
            }
        }
    }
}
