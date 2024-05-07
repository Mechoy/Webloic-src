package weblogic.jms.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.cluster.ClusterMembersChangeEvent;
import weblogic.cluster.ClusterMembersChangeListener;
import weblogic.cluster.ClusterService;
import weblogic.cluster.ClusterServices;
import weblogic.jms.JMSService;
import weblogic.jms.dispatcher.Invocable;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;

public final class LeaderManager implements Invocable, ClusterMembersChangeListener {
   private static final LeaderManager leaderManager = new LeaderManager();
   private JMSID requestStatsSourceId;
   private long sequenceNumber;
   private final HashMap hashByName = new HashMap();

   private LeaderManager() {
      ClusterServices var1 = ClusterService.getServices();
      if (var1 != null) {
         var1.addClusterMembersListener(this);
      }

   }

   public JMSID getJMSID() {
      return null;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return null;
   }

   private void setRequestStatsSourceId(JMSID var1) {
      this.requestStatsSourceId = var1;
   }

   private JMSID getRequestStatsSourceId() {
      return this.requestStatsSourceId;
   }

   public int invoke(Request var1) throws Throwable {
      switch (var1.getMethodId()) {
         case 16405:
            return this.leaderBindSingular((LeaderBindRequest)var1);
         case 16661:
            LeaderBindFailedRequest var2 = (LeaderBindFailedRequest)var1;
            this.aggregatableDidBind(var2.getJNDIName(), var2.getLeaderID(), var2.getSequenceNumber());
         default:
            return Integer.MAX_VALUE;
      }
   }

   private synchronized long getNextSequenceNumber() {
      return (long)(this.sequenceNumber++);
   }

   private int leaderBindSingular(LeaderBindRequest var1) {
      String var2 = var1.getJNDIName();
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("LeaderManager.leaderBindSingular(" + var1 + ")");
      }

      if (this.requestStatsSourceId == null) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("LeaderManager.leaderBindSingular(Not Granted 10)");
         }

         var1.setResult(new LeaderBindResponse(false, this.requestStatsSourceId, this.getNextSequenceNumber(), "The statistics source id is null"));
         var1.setState(Integer.MAX_VALUE);
         return Integer.MAX_VALUE;
      } else if (var2 == null) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("LeaderManager.leaderBindSingular(Not Granted 30)");
         }

         var1.setResult(new LeaderBindResponse(false, this.requestStatsSourceId, this.getNextSequenceNumber(), "The requested JNDI name was null"));
         var1.setState(Integer.MAX_VALUE);
         return Integer.MAX_VALUE;
      } else {
         long var3;
         synchronized(this.hashByName) {
            label71: {
               int var10000;
               try {
                  Object var11 = JMSService.getContext().lookup(var2);
                  if (JMSDebug.JMSCommon.isDebugEnabled()) {
                     JMSDebug.JMSCommon.debug("LeaderManager.leaderBindSingular(Not Granted 40)");
                  }

                  var1.setResult(new LeaderBindResponse(false, this.requestStatsSourceId, this.getNextSequenceNumber(), "The JNDI name " + var2 + " was found, and was bound to an object of type " + var11.getClass().getName() + " : " + var11));
                  var1.setState(Integer.MAX_VALUE);
                  var10000 = Integer.MAX_VALUE;
               } catch (NamingException var9) {
                  if (!(var9 instanceof NameNotFoundException)) {
                     if (JMSDebug.JMSCommon.isDebugEnabled()) {
                        JMSDebug.JMSCommon.debug("LeaderManager.leaderBindSingular(Not Granted 50)");
                     }

                     var1.setResult(new LeaderBindResponse(false, this.requestStatsSourceId, this.getNextSequenceNumber(), "An unknown naming exception occured looking up " + var2 + ".  The error was " + var9));
                     var1.setState(Integer.MAX_VALUE);
                     return Integer.MAX_VALUE;
                  }

                  String var6 = var1.getServerName();
                  String var7 = var2.replace('/', '.');
                  if (this.hashByName.get(var7) != null) {
                     if (JMSDebug.JMSCommon.isDebugEnabled()) {
                        JMSDebug.JMSCommon.debug("LeaderManager.leaderBindSingular(Not Granted 60)");
                     }

                     var1.setResult(new LeaderBindResponse(false, this.requestStatsSourceId, this.getNextSequenceNumber(), "The name " + var7 + " has been previously reserved by server " + var6));
                     var1.setState(Integer.MAX_VALUE);
                     return Integer.MAX_VALUE;
                  }

                  var3 = this.getNextSequenceNumber();
                  this.hashByName.put(var7, new SerAndSeq(var6, var3));
                  if (JMSDebug.JMSCommon.isDebugEnabled()) {
                     JMSDebug.JMSCommon.debug("LeaderManager.leaderBindSingular name put in pending list=" + var7);
                  }
                  break label71;
               }

               return var10000;
            }
         }

         var1.setResult(new LeaderBindResponse(true, this.requestStatsSourceId, var3));
         var1.setState(Integer.MAX_VALUE);
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("LeaderManager.leaderBindSingular(Granted " + var2 + " to " + var1.getServerName() + ")");
         }

         return Integer.MAX_VALUE;
      }
   }

   public void aggregatableDidBind(String var1, JMSID var2, long var3) {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("LeaderManager.aggregatableDidBind(" + var1 + ":" + var2 + ":" + var3 + ")");
      }

      if (var1 != null && var2 != null && this.requestStatsSourceId != null) {
         if (var2.equals(this.requestStatsSourceId)) {
            SerAndSeq var5 = null;
            synchronized(this.hashByName) {
               var5 = (SerAndSeq)this.hashByName.get(var1);
               if (var5 != null && (var3 < 0L || var3 == var5.getSequenceNumber())) {
                  this.hashByName.remove(var1);
               }
            }

            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug("LeaderManager.aggregatableDidBind(removed " + var1 + " with value " + var5 + ")");
            }

         }
      }
   }

   public void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      if (var1.getAction() == 1) {
         String var2 = var1.getClusterMemberInfo().serverName();
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("LeaderManager.clusterMembersChanged(" + var2 + ") is being removed");
         }

         synchronized(this.hashByName) {
            Iterator var4 = this.hashByName.entrySet().iterator();

            while(var4.hasNext()) {
               Map.Entry var5 = (Map.Entry)var4.next();
               SerAndSeq var6 = (SerAndSeq)var5.getValue();
               String var7 = var6.getServerName();
               if (var2.equals(var7)) {
                  var4.remove();
               }
            }

         }
      }
   }

   public static synchronized LeaderManager getLeaderManager() {
      return leaderManager;
   }

   public static synchronized LeaderManager getLeaderManager(JMSID var0) {
      if (leaderManager.getRequestStatsSourceId() == null) {
         leaderManager.setRequestStatsSourceId(var0);
      }

      return leaderManager;
   }

   private static class SerAndSeq {
      private String serverName;
      private long sequenceNumber;

      private SerAndSeq(String var1, long var2) {
         this.serverName = var1;
         this.sequenceNumber = var2;
      }

      private String getServerName() {
         return this.serverName;
      }

      private long getSequenceNumber() {
         return this.sequenceNumber;
      }

      public String toString() {
         return "SerAndSeq(" + this.serverName + "/" + this.sequenceNumber + ")";
      }

      // $FF: synthetic method
      SerAndSeq(String var1, long var2, Object var4) {
         this(var1, var2);
      }
   }
}
