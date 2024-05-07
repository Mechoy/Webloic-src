package weblogic.jms.frontend;

import java.util.HashMap;
import java.util.Iterator;
import javax.jms.JMSException;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.LeaderManager;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.messaging.dispatcher.InvocableMonitor;

public final class FrontEnd {
   private final String mbeanName;
   private final HashMap connectionFactories = new HashMap();
   private long connectionFactoriesCurrentCount;
   private long connectionFactoriesHighCount;
   private long connectionFactoriesTotalCount;
   private final HashMap beDestinationTables = new HashMap();
   public static final String JMS_TEMP_DESTINATION_FTY_JNDI = "weblogic.jms.TempDestinationFactory";
   private static JMSService jmsService;
   private final LeaderManager leaderManager;
   private final JMSServerId frontEndId;
   private static FrontEnd frontEnd;
   private final InvocableMonitor invocableMonitor;
   private int state = 0;

   public FrontEnd(JMSService var1) {
      jmsService = var1;
      this.mbeanName = var1.getMbeanName();
      this.frontEndId = var1.getNextServerId();
      this.invocableMonitor = new InvocableMonitor(var1.getInvocableMonitor());
      this.leaderManager = LeaderManager.getLeaderManager(this.frontEndId.getId());
      frontEnd = this;
      this.state = 1;
   }

   public static FrontEnd getFrontEnd() {
      return frontEnd;
   }

   public String getMbeanName() {
      return this.mbeanName;
   }

   JMSService getService() {
      return jmsService;
   }

   public synchronized void markShuttingDown() {
      if ((this.state & 24) == 0) {
         this.state = 8;
         Iterator var1 = this.connectionFactories.values().iterator();

         while(var1.hasNext()) {
            ((FEConnectionFactory)var1.next()).markShuttingDown();
         }

         synchronized(this) {
            var1 = ((HashMap)this.getConnectionsMap().clone()).values().iterator();
         }

         while(var1.hasNext()) {
            ((FEConnection)var1.next()).markShuttingDown();
         }

      }
   }

   private synchronized void checkShutdown() throws JMSException {
      if ((this.state & 24) != 0) {
         throw new weblogic.jms.common.JMSException("JMS Server is shutdown or suspended");
      }
   }

   synchronized void checkShutdownOrSuspended() throws JMSException {
      if ((this.state & 27) != 0) {
         throw new weblogic.jms.common.JMSException("JMS server is shutdown or suspended");
      }
   }

   public synchronized void markSuspending() {
      if ((this.state & 27) == 0) {
         this.state = 2;
         Iterator var1 = this.connectionFactories.values().iterator();

         while(var1.hasNext()) {
            ((FEConnectionFactory)var1.next()).markSuspending();
         }

         synchronized(this) {
            var1 = ((HashMap)this.getConnectionsMap().clone()).values().iterator();
         }

         while(var1.hasNext()) {
            ((FEConnection)var1.next()).markSuspending();
         }

      }
   }

   public void prepareForSuspend(boolean var1) {
      Iterator var2;
      synchronized(this) {
         if ((this.state & 24) != 0) {
            return;
         }

         if ((this.state & 2) == 0) {
            this.markSuspending();
         }

         var2 = ((HashMap)this.connectionFactories.clone()).keySet().iterator();
      }

      while(var2.hasNext()) {
         try {
            String var3 = (String)var2.next();
            FEConnectionFactory var4 = (FEConnectionFactory)this.connectionFactories.get(var3);
            var4.suspend();
         } catch (Throwable var8) {
         }
      }

      if (!var1) {
         synchronized(this) {
            var2 = ((HashMap)this.getConnectionsMap().clone()).values().iterator();
         }

         while(var2.hasNext()) {
            try {
               FEConnection var10 = (FEConnection)var2.next();
               var10.stop();
            } catch (Throwable var6) {
            }
         }
      }

   }

   public void suspend(boolean var1) throws Exception {
      boolean var3 = false;
      Throwable var4 = null;
      synchronized(this) {
         if ((this.state & 24) != 0) {
            return;
         }

         if ((this.state & 2) == 0) {
            var3 = true;
            this.markSuspending();
         }

         if (var1) {
            this.invocableMonitor.forceInvocablesCompletion();
         }
      }

      try {
         if (var3) {
            try {
               this.prepareForSuspend(var1);
            } catch (Throwable var21) {
               if (var4 == null) {
                  var4 = var21;
               }
            }
         }

         if (!var1 && var3) {
            this.invocableMonitor.waitForInvocablesCompletion();
         }

         Iterator var2;
         synchronized(this) {
            var2 = ((HashMap)this.getConnectionsMap().clone()).values().iterator();
         }

         while(var2.hasNext()) {
            try {
               FEConnection var5 = (FEConnection)var2.next();
               var5.normalClose();
            } catch (Throwable var22) {
               if (var4 == null) {
                  var4 = var22;
               }
            }
         }
      } finally {
         synchronized(this) {
            this.state = 1;
         }

         if (var4 != null) {
            if (var4 instanceof Exception) {
               throw (Exception)var4;
            }

            throw new weblogic.jms.common.JMSException("Error occurred in suspending JMS Service", var4);
         }

      }

   }

   public void resume() throws JMSException {
      Iterator var1;
      synchronized(this) {
         if ((this.state & 4) != 0) {
            return;
         }

         if ((this.state & 1) == 0) {
            throw new JMSException("Failed to start JMS connection factories: wrong state");
         }

         var1 = ((HashMap)this.connectionFactories.clone()).keySet().iterator();
      }

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         FEConnectionFactory var3 = (FEConnectionFactory)this.connectionFactories.get(var2);
         String var4 = var3.getJNDIName();
         if (var3 != null) {
            try {
               var3.bind();
               if (var3.isDefaultConnectionFactory()) {
                  JMSLogger.logDefaultCFactoryDeployed(var2, var4);
               } else {
                  JMSLogger.logCFactoryDeployed(var2);
               }
            } catch (JMSException var9) {
               if (var3.isDefaultConnectionFactory()) {
                  JMSLogger.logErrorBindDefaultCF(var2, var4, var9);
               } else {
                  JMSLogger.logErrorBindCF(var2, var9);
               }

               throw var9;
            }
         }
      }

      synchronized(this) {
         this.state = 4;
      }
   }

   public void shutdown() {
      Iterator var1;
      synchronized(this) {
         if ((this.state & 16) != 0) {
            return;
         }

         if ((this.state & 8) == 0) {
            this.markShuttingDown();
         }

         var1 = ((HashMap)this.connectionFactories.clone()).keySet().iterator();
      }

      while(var1.hasNext()) {
         try {
            String var2 = (String)var1.next();
            FEConnectionFactory var3 = (FEConnectionFactory)this.connectionFactories.get(var2);
            var3.unbind();
            synchronized(this) {
               this.connectionFactoryRemove(var3);
            }
         } catch (Throwable var12) {
         }
      }

      synchronized(this) {
         var1 = ((HashMap)this.getConnectionsMap().clone()).values().iterator();
      }

      while(var1.hasNext()) {
         try {
            FEConnection var14 = (FEConnection)var1.next();
            var14.normalClose();
         } catch (Throwable var9) {
         }
      }

      synchronized(this) {
         this.state = 16;
      }
   }

   public synchronized void connectionFactoryAdd(FEConnectionFactory var1) throws JMSException {
      this.checkShutdown();
      String var2 = var1.getName();
      if (this.connectionFactories.put(var2, var1) == null) {
         ++this.connectionFactoriesTotalCount;
         if (++this.connectionFactoriesCurrentCount > this.connectionFactoriesHighCount) {
            this.connectionFactoriesHighCount = this.connectionFactoriesCurrentCount;
         }

      }
   }

   public synchronized void connectionFactoryRemove(FEConnectionFactory var1) {
      String var2 = var1.getName();
      if (this.connectionFactories.remove(var2) != null) {
         --this.connectionFactoriesCurrentCount;
      }
   }

   public synchronized FEConnectionFactory connectionFactoryFind(String var1) {
      return (FEConnectionFactory)this.connectionFactories.get(var1);
   }

   public FEConnectionFactory[] getConnectionFactories() {
      return (FEConnectionFactory[])((FEConnectionFactory[])this.connectionFactories.values().toArray(new FEConnectionFactory[this.connectionFactories.size()]));
   }

   synchronized void addBackEndDestination(JMSServerId var1, String var2, DestinationImpl var3, FEConnection var4) {
   }

   public synchronized void removeBackEndDestination(DestinationImpl var1) {
      HashMap var2 = (HashMap)this.beDestinationTables.get(var1.getBackEndId());
      if (var2 != null) {
         synchronized(var2) {
            var2.remove(var1.getName());
         }
      }
   }

   synchronized DestinationImpl findBackEndDestination(JMSServerId var1, String var2) {
      HashMap var3 = (HashMap)this.beDestinationTables.get(var1);
      if (var3 == null) {
         return null;
      } else {
         synchronized(var3) {
            return (DestinationImpl)var3.get(var2);
         }
      }
   }

   public static JMSService getJMSService() {
      return jmsService;
   }

   InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   private HashMap getConnectionsMap() {
      return InvocableManagerDelegate.delegate.getInvocableMap(7);
   }

   public JMSServerId getFrontEndId() {
      return this.frontEndId;
   }
}
