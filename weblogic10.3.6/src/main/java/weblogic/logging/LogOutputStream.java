package weblogic.logging;

import java.util.logging.Level;

public class LogOutputStream {
   public static final String UNKNOWN = "Default";
   private final String channel;

   public LogOutputStream(String var1) {
      if (var1 == null) {
         this.channel = "Default";
      } else {
         Subsystems.add(var1);
         this.channel = var1;
      }

   }

   public void emergency(String var1) {
      MessageLogger.log((Level)WLLevel.EMERGENCY, (String)this.channel, var1);
   }

   public void emergency(String var1, Throwable var2) {
      MessageLogger.log(WLLevel.EMERGENCY, this.channel, var1, var2);
   }

   public void alert(String var1) {
      MessageLogger.log((Level)WLLevel.ALERT, (String)this.channel, var1);
   }

   public void alert(String var1, Throwable var2) {
      MessageLogger.log(WLLevel.ALERT, this.channel, var1, var2);
   }

   public void critical(String var1) {
      MessageLogger.log((Level)WLLevel.CRITICAL, (String)this.channel, var1);
   }

   public void critical(String var1, Throwable var2) {
      MessageLogger.log(WLLevel.CRITICAL, this.channel, var1, var2);
   }

   public void error(String var1) {
      MessageLogger.log((Level)WLLevel.ERROR, (String)this.channel, var1);
   }

   public void error(String var1, Throwable var2) {
      MessageLogger.log(WLLevel.ERROR, this.channel, var1, var2);
   }

   public void notice(String var1) {
      MessageLogger.log((Level)WLLevel.NOTICE, (String)this.channel, var1);
   }

   public void notice(String var1, Throwable var2) {
      MessageLogger.log(WLLevel.NOTICE, this.channel, var1, var2);
   }

   public void warning(String var1) {
      MessageLogger.log((Level)WLLevel.WARNING, (String)this.channel, var1);
   }

   public void warning(String var1, Throwable var2) {
      MessageLogger.log(WLLevel.WARNING, this.channel, var1, var2);
   }

   public void info(String var1) {
      MessageLogger.log((Level)WLLevel.INFO, (String)this.channel, var1);
   }

   public void info(String var1, Throwable var2) {
      MessageLogger.log(WLLevel.INFO, this.channel, var1, var2);
   }

   public void debug(String var1) {
      MessageLogger.log((Level)WLLevel.DEBUG, (String)this.channel, var1);
   }

   public void debug(String var1, Throwable var2) {
      MessageLogger.log(WLLevel.DEBUG, this.channel, var1, var2);
   }
}
