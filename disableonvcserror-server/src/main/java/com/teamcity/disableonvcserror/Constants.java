package com.teamcity.disableonvcserror;

import java.util.Set;

import jetbrains.buildServer.notification.MessageSender;
import jetbrains.buildServer.users.SUser;

class Constants {
    static final int NUM_THREADS = 5;
    static final String ADMIN_USERNAME = "teamcitymaster";
    static MessageSender EMAIL_NOTIFIER = null;
    static Set<String> NOTIFICATION_EMAIL_IDS = null;
    static SUser ADMIN_SUSER = null;
}
