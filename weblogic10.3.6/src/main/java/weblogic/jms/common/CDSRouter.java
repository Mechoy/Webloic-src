package weblogic.jms.common;

import weblogic.jms.dispatcher.Invocable;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;

public final class CDSRouter implements Invocable {
   private static Invocable remoteProxy;
   private static Invocable localProxy;
   private static CDSRouter singleton = new CDSRouter();

   public static CDSRouter getSingleton() {
      return singleton;
   }

   public int invoke(Request var1) throws Throwable {
      switch (var1.getMethodId()) {
         case 18455:
            if (remoteProxy != null) {
               return remoteProxy.invoke(var1);
            }

            throw new JMSException("No such method " + var1.getMethodId());
         case 18711:
            return localProxy.invoke(var1);
         case 18967:
            if (remoteProxy != null) {
               remoteProxy.invoke(var1);
            }

            return localProxy.invoke(var1);
         default:
            throw new JMSException("No such method " + var1.getMethodId());
      }
   }

   public JMSID getJMSID() {
      return null;
   }

   public ID getId() {
      return null;
   }

   public InvocableMonitor getInvocableMonitor() {
      return null;
   }

   static {
      try {
         remoteProxy = (Invocable)Class.forName("weblogic.jms.common.CDSRemoteProxy").getDeclaredMethod("getSingleton", (Class[])null).invoke((Object)null, (Object[])null);
      } catch (Exception var2) {
      }

      try {
         localProxy = (Invocable)Class.forName("weblogic.jms.common.CDSLocalProxy").getDeclaredMethod("getSingleton", (Class[])null).invoke((Object)null, (Object[])null);
      } catch (Exception var1) {
      }

   }
}
