package weblogic.jms.safclient.agent.internal;

import weblogic.jms.safclient.agent.DestinationImpl;

public final class ErrorHandler {
   private String name;
   public static final int DISCARD_POLICY = 0;
   public static final int LOGGING_POLICY = 1;
   public static final int ERRORDESTINATION_POLICY = 2;
   public static final int ALWAYSFORWARD_POLICY = 3;
   private int policy = 0;
   private String logFormat;
   private String errorDestinationName;
   private DestinationImpl errorDestination;

   public ErrorHandler(String var1) {
      this.name = var1;
   }

   private static int policyStringToInt(String var0) {
      if (var0 != null && !"Discard".equals(var0)) {
         if ("Log".equals(var0)) {
            return 1;
         } else if ("Redirect".equals(var0)) {
            return 2;
         } else {
            return "Always-Forward".equals(var0) ? 3 : 0;
         }
      } else {
         return 0;
      }
   }

   public void setPolicy(String var1) {
      this.policy = policyStringToInt(var1);
   }

   public void setLogFormat(String var1) {
      this.logFormat = var1;
   }

   public void setErrorDestinationName(String var1) {
      this.errorDestinationName = var1;
   }

   public String getErrorDestinationName() {
      return this.errorDestinationName;
   }

   public void setErrorDestination(DestinationImpl var1) {
      this.errorDestination = var1;
   }

   public String getLogFormat() {
      return this.logFormat;
   }

   public int getPolicy() {
      return this.policy;
   }

   public DestinationImpl getErrorDestination() {
      return this.errorDestination;
   }

   public String toString() {
      return "ErrorHandler(" + this.name + ")";
   }
}
