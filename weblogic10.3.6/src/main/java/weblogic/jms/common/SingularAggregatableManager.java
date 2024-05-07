package weblogic.jms.common;

import java.security.AccessController;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import javax.naming.NamingException;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterService;
import weblogic.jms.JMSService;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.dispatcher.Request;
import weblogic.management.provider.ManagementService;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.messaging.dispatcher.Response;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.work.WorkManagerFactory;

public final class SingularAggregatableManager {
   private static SingularAggregatableManager singularAggregatableManager;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final Hashtable pendingRequests = new Hashtable();
   private final Hashtable boundAggregatables = new Hashtable();
   private final String serverName;

   public static synchronized SingularAggregatableManager findOrCreate() {
      return singularAggregatableManager != null ? singularAggregatableManager : (singularAggregatableManager = new SingularAggregatableManager());
   }

   private SingularAggregatableManager() {
      this.serverName = ManagementService.getRuntimeAccess(kernelId).getServerName();
   }

   private static JMSDispatcher findLeader() {
      ClusterService var1 = ClusterService.getClusterService();
      JMSDispatcher var0;
      ClusterMemberInfo var2;
      if ((var2 = var1.getLocalMember()) == null) {
         var0 = JMSDispatcherManager.getLocalDispatcher();
      } else {
         String var4 = var2.serverName();
         Collection var5 = var1.getRemoteMembers();
         ClusterMemberInfo var3 = var2;
         if (var5 != null) {
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               var2 = (ClusterMemberInfo)var6.next();
               String var7 = var2.serverName();
               if (var4.compareTo(var7) < 0) {
                  var3 = var2;
               }
            }
         }

         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("SingularAggregatableManager.findLeader Leader is " + var3.serverName());
         }

         DispatcherId var9 = new DispatcherId(var3.serverName(), var3.identity().getPersistentIdentity().toString());

         try {
            var0 = JMSDispatcherManager.dispatcherFindOrCreate(var9);
         } catch (DispatcherException var8) {
            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug("Exception:", var8);
            }

            var0 = JMSDispatcherManager.getLocalDispatcher();
         }
      }

      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("SingularAggregatableManager.findLeader(" + var0 + ")");
      }

      return var0;
   }

   public void singularBindStart(String var1, SingularAggregatable var2, Request var3) throws JMSException {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("SingularAggregatableManager.singularBindStart(" + var1 + ":" + var2 + ")");
      }

      if (var1 != null && var2 != null) {
         var2.setJNDIName(var1);
         JMSDispatcher var5 = findLeader();
         if (var5 == null) {
            throw new JMSException("Could not get the leader, impossible to bind");
         } else {
            var2.setLeaderDispatcher(var5);
            LeaderBindRequest var4;
            synchronized(this.pendingRequests) {
               var4 = new LeaderBindRequest(this.serverName, var1);
            }

            try {
               if (var3 != null) {
                  var3.rememberChild(var4);
                  var3.dispatchAsync(var5, var4);
               } else {
                  var5.dispatchAsync(var4);
                  synchronized(this.pendingRequests) {
                     this.pendingRequests.put(var1, var4);
                  }
               }

            } catch (DispatcherException var10) {
               throw new JMSException("Could not dispatch request to leader", var10);
            }
         }
      } else {
         throw new JMSException("Invalid arguments to SingularAggregatable.bind");
      }
   }

   private static void sendFailedToBind(SingularAggregatable var0, LeaderBindResponse var1) {
      LeaderBindFailedRequest var2 = new LeaderBindFailedRequest(var0.getJNDIName(), var0.getLeaderJMSID(), var0.getLeaderSequenceNumber());
      JMSDispatcher var3 = var0.getLeaderDispatcher();
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("SingularAggregatableManager.sendFailedToBind(" + var0 + ":" + var1 + ")");
      }

      if (var3 == null) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("SingularAggregatableManager.sendFailedToBind failed because leaderDispatcher is null");
         }

      } else {
         try {
            JMSServerUtilities.anonDispatchNoReply(var2, var3);
         } catch (javax.jms.JMSException var5) {
            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug("SingularAggregatableManager.singularBindFinish(failed to dispatch" + var5 + ")");
            }
         }

      }
   }

   public String singularBindFinish(SingularAggregatable var1, Request var2) throws JMSException {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("SingularAggregatableManager.singularBindFinish(" + var1 + ")");
      }

      if (var1 == null) {
         throw new JMSException("Invalid input parameters");
      } else {
         String var4 = var1.getJNDIName();

         LeaderBindResponse var3;
         try {
            Response var5;
            if (var2 != null && var2.getChild() != null) {
               var5 = var2.useChildResult(LeaderBindResponse.class);
            } else {
               LeaderBindRequest var6;
               synchronized(this.pendingRequests) {
                  var6 = (LeaderBindRequest)this.pendingRequests.remove(var4);
               }

               if (var6 == null) {
                  throw new JMSException("Cannot find the request to complete");
               }

               var5 = var6.getResult();
            }

            if (var5 == null) {
               throw new AssertionError("In singularBindFinish the response is null");
            }

            var3 = (LeaderBindResponse)var5;
         } catch (javax.jms.JMSException var15) {
            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug("Exception:", var15);
            }

            throw new JMSException("Unexpected response from leader", var15);
         }

         if (!var3.doBind()) {
            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug("SingularAggregatableManager.singularBindFinish leader denied request");
            }

            return var3.getReasonForRejection();
         } else {
            var1.setLeaderJMSID(var3.getLeaderJMSID());
            var1.setLeaderSequenceNumber(var3.getLeaderSequenceNumber());
            SingularAggregatable var16;
            synchronized(this.boundAggregatables) {
               var16 = (SingularAggregatable)this.boundAggregatables.put(var4, var1);
               if (var16 != null) {
                  this.boundAggregatables.put(var4, var16);
               }
            }

            if (var16 != null) {
               sendFailedToBind(var1, var3);
               throw new JMSException("Attempt to bind to a name we have already bound: (" + var3 + "/" + var1 + "/" + var16 + ")");
            } else {
               try {
                  PrivilegedActionUtilities.bindAsSU(JMSService.getContext(), var4, var1, kernelId);
               } catch (NamingException var12) {
                  synchronized(this.boundAggregatables) {
                     this.boundAggregatables.remove(var4);
                  }

                  sendFailedToBind(var1, var3);
                  throw new JMSException("Failed to bind name " + var4, var12);
               }

               if (JMSDebug.JMSCommon.isDebugEnabled()) {
                  JMSDebug.JMSCommon.debug("SingularAggregatableManager.singularBindFinish bound (" + var1 + ")");
               }

               return null;
            }
         }
      }
   }

   public String singularBind(String var1, SingularAggregatable var2) throws JMSException {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("SingularAggregatableManager.singularBind()");
      }

      this.singularBindStart(var1, var2, (Request)null);
      String var3 = this.singularBindFinish(var2, (Request)null);
      return var3;
   }

   public void singularUnbind(String var1) throws JMSException {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("SingularAggregatableManager.singularUnbind(" + var1 + ")");
      }

      if (var1 == null) {
         throw new JMSException("Invalid paramter to singularUnbind");
      } else {
         SingularAggregatable var2;
         synchronized(this.boundAggregatables) {
            if ((var2 = (SingularAggregatable)this.boundAggregatables.remove(var1)) == null) {
               return;
            }
         }

         try {
            PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(), var1, kernelId);
         } catch (NamingException var5) {
            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug("SingularAggregatableManager.singularUnbind got a naming exception unbinding " + var1 + ";  The exception is: " + var5);
            }
         }

         LeaderBindResponse var3 = new LeaderBindResponse(true, var2.getLeaderJMSID(), var2.getLeaderSequenceNumber());
         sendFailedToBind(var2, var3);
      }
   }

   public void aggregatableDidBind(String var1, SingularAggregatable var2) {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug("SingularAggregatableManager.aggregatableDidBind(" + var1 + ":" + var2 + ")");
      }

      if (var1 != null && var2 != null) {
         SingularAggregatable var3;
         synchronized(this.boundAggregatables) {
            if ((var3 = (SingularAggregatable)this.boundAggregatables.get(var1)) == null) {
               return;
            }
         }

         JMSID var5;
         if ((var5 = var3.getLeaderJMSID()) != null) {
            JMSID var4;
            if ((var4 = var2.getLeaderJMSID()) != null) {
               int var6;
               if ((var6 = var5.compareTo(var4)) != 0 || var3.getLeaderSequenceNumber() != var2.getLeaderSequenceNumber()) {
                  if (var6 == 0) {
                     if (var3.getLeaderSequenceNumber() <= var2.getLeaderSequenceNumber()) {
                        var6 = -1;
                     } else {
                        var6 = 1;
                     }
                  }

                  boolean var7 = var6 > 0;
                  var3.hadConflict(var7);
                  if (!var7) {
                     WorkManagerFactory.getInstance().getSystem().schedule(new SingularUnbindOnConflictThread(var1));
                  }

               }
            }
         }
      } else {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("SingularAggregatableManager.aggregatableDidBind failed due to bad input parameters");
         }

      }
   }

   private class SingularUnbindOnConflictThread implements Runnable {
      String jndiName;

      private SingularUnbindOnConflictThread(String var2) {
         this.jndiName = var2;
      }

      public void run() {
         try {
            SingularAggregatableManager.this.singularUnbind(this.jndiName);
         } catch (JMSException var2) {
         }

      }

      // $FF: synthetic method
      SingularUnbindOnConflictThread(String var2, Object var3) {
         this(var2);
      }
   }
}
