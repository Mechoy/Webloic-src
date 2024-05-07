package weblogic.jms.common;

import java.util.Date;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.messaging.dispatcher.DispatcherException;

public final class JMSDebug {
   public static final DebugLogger JMSBackEnd = DebugLogger.getDebugLogger("DebugJMSBackEnd");
   public static final DebugLogger JMSFrontEnd = DebugLogger.getDebugLogger("DebugJMSFrontEnd");
   public static final DebugLogger JMSCommon = DebugLogger.getDebugLogger("DebugJMSCommon");
   public static final DebugLogger JMSConfig = DebugLogger.getDebugLogger("DebugJMSConfig");
   public static final DebugLogger JMSLocking = DebugLogger.getDebugLogger("DebugJMSLocking");
   public static final DebugLogger JMSXA = DebugLogger.getDebugLogger("DebugJMSXA");
   public static final DebugLogger JMSStore = DebugLogger.getDebugLogger("DebugJMSStore");
   public static final DebugLogger JMSBoot = DebugLogger.getDebugLogger("DebugJMSBoot");
   public static final DebugLogger JMSDurSub = DebugLogger.getDebugLogger("DebugJMSDurSub");
   public static final DebugLogger JMSDispatcher = DebugLogger.getDebugLogger("DebugJMSDispatcher");
   public static final DebugLogger JMSDistTopic = DebugLogger.getDebugLogger("DebugJMSDistTopic");
   public static final DebugLogger JMSAME = DebugLogger.getDebugLogger("DebugJMSAME");
   public static final DebugLogger JMSPauseResume = DebugLogger.getDebugLogger("DebugJMSPauseResume");
   public static final DebugLogger JMSModule = DebugLogger.getDebugLogger("DebugJMSModule");
   public static final DebugLogger JMSMessagePath = DebugLogger.getDebugLogger("DebugJMSMessagePath");
   public static final DebugLogger JMSSAF = DebugLogger.getDebugLogger("DebugJMSSAF");
   public static final DebugLogger JMSCDS = DebugLogger.getDebugLogger("DebugJMSCDS");
   public static final DebugLogger JMSCrossDomainSecurity = DebugLogger.getDebugLogger("DebugJMSCrossDomainSecurity");
   public static final DebugLogger JMSDotNetProxy = DebugLogger.getDebugLogger("DebugJMSDotNetProxy");
   public static final DebugLogger JMSDotNetTransport = DebugLogger.getDebugLogger("DebugJMSDotNetTransport");
   public static final DebugLogger JMSDotNetT3Server = DebugLogger.getDebugLogger("DebugJMSDotNetT3Server");
   private static final boolean DEBUG = true;
   private long lastTimeThrew;
   private int millisecBetweenThrows = 50000;
   private int countMatchMask = 15;
   private int throwCount;

   public JMSDebug() {
   }

   public JMSDebug(int var1, int var2) {
      this.millisecBetweenThrows = var1;
      this.countMatchMask = var2;
   }

   public String shouldThrow(String var1) {
      long var2;
      String var4;
      label63: {
         try {
            if ((this.throwCount & this.countMatchMask) != 0) {
               debug("mask not satisfied " + var1 + (this.throwCount & this.countMatchMask));
               var4 = null;
               return var4;
            }

            var2 = System.currentTimeMillis();
            if (var2 - (long)this.millisecBetweenThrows >= this.lastTimeThrew) {
               break label63;
            }

            debug("too soon to throw: " + var1 + new Date());
            var4 = null;
         } finally {
            ++this.throwCount;
         }

         return var4;
      }

      this.lastTimeThrew = var2;
      var4 = "Testing resilience to: " + var1 + ", " + this.throwCount + " " + new Date();
      debug(var4);
      return var4;
   }

   public void periodicallyThrowJMSException(String var1) throws javax.jms.JMSException {
      String var2 = this.shouldThrow(var1);
      if (var2 != null) {
         throw new JMSException(var2);
      }
   }

   public void periodicallyThrowDispatcherException(String var1) throws DispatcherException {
      String var2 = this.shouldThrow(var1);
      if (var2 != null) {
         throw new DispatcherException(var2);
      }
   }

   public static final void unlocked(String var0, Object var1) {
      if (JMSLocking.isDebugEnabled()) {
         JMSLocking.debug("LCK!" + Thread.currentThread().getName() + ": " + var0 + " : unlocked " + var1);
      }

   }

   private static final void debug(String var0) {
      JMSDistTopic.debug(var0);
   }
}
