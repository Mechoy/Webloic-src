package weblogic.messaging.dispatcher;

import weblogic.messaging.ID;

public interface Invocable {
   int invoke(Request var1) throws Throwable;

   ID getId();

   InvocableMonitor getInvocableMonitor();
}
