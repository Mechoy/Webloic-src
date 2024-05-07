package weblogic.cluster;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.common.internal.WLObjectInputStream;
import weblogic.management.provider.ManagementService;
import weblogic.management.servlet.ConnectionSigner;
import weblogic.protocol.URLManager;
import weblogic.rjvm.PeerGoneEvent;
import weblogic.rjvm.PeerGoneListener;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.io.DataIO;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class MemberManager implements PeerGoneListener, MulticastSessionIDConstants, RecoverListener {
   private final int idlePeriodsUntilTimeout;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String SECRET_STRING;
   private static final byte[] SERVER_HASH_VALUE;
   private static MemberManager theMemberManager;
   private Map remoteMembers = new HashMap();
   private long joinTime;
   private ArrayList ClusterMembersListeners;
   private boolean waitingForFirstHeartbeats = true;
   private String clusterName;
   private MulticastSession runtimeStateSender;

   public static MemberManager theOne() {
      return theMemberManager;
   }

   static void initialize(long var0, int var2) {
      Debug.assertion(theMemberManager == null, "intialize only once");
      theMemberManager = new MemberManager(var0, var2);
      ClusterDropoutListener.initialize();
   }

   private MemberManager(long var1, int var3) {
      this.joinTime = var1;
      this.ClusterMembersListeners = new ArrayList();
      this.clusterName = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getName();
      this.idlePeriodsUntilTimeout = var3;
      this.runtimeStateSender = MulticastManager.theOne().createSender(3, this, 1, true, true);
   }

   int getIdlePeriodsUntilTimeout() {
      return this.idlePeriodsUntilTimeout;
   }

   synchronized void waitToSyncWithCurrentMembers() {
      long var1 = (long)(ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getMemberWarmupTimeoutSeconds() * 1000);
      long var3 = (long)(this.idlePeriodsUntilTimeout * 10000);
      if (var1 > var3) {
         this.waitForDiscovery(var3);
         this.waitForSync();
      } else {
         if (var1 > 0L) {
            this.waitForDiscovery(var1);
         }

         this.waitForSync();
      }

   }

   private void waitForDiscovery(long var1) {
      if (var1 > 0L) {
         ClusterLogger.logStartWarmup(ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getName());
      }

      long var3 = 0L;

      for(long var5 = System.currentTimeMillis(); this.waitingForFirstHeartbeats && var3 < var1; var3 = System.currentTimeMillis() - var5) {
         try {
            this.wait(var1 - var3);
         } catch (InterruptedException var8) {
         }
      }

      this.waitingForFirstHeartbeats = false;
   }

   private void waitForSync() {
      HashSet var1;
      synchronized(this) {
         var1 = new HashSet(this.remoteMembers.values());
      }

      Iterator var2 = var1.iterator();
      MemberAttributes var3 = null;

      while(var2.hasNext()) {
         RemoteMemberInfo var4 = (RemoteMemberInfo)var2.next();
         MemberAttributes var5 = var4.getAttributes();
         if (var5 == null) {
            var2.remove();
         } else if (var4.isRunning()) {
            var3 = var5;
            break;
         }
      }

      if (var3 != null) {
         this.getJNDIStateDump(var3);
      }

   }

   private void getJNDIStateDump(MemberAttributes var1) throws AssertionError {
      URL var2 = null;
      HttpURLConnection var3 = null;
      DataInputStream var4 = null;
      WLObjectInputStream var5 = null;

      try {
         ClusterLogger.logFetchClusterStateDump(var1.serverName());
         var2 = this.getServerURL(var1);
         var3 = URLManager.createAdminHttpConnection(var2);
         ConnectionSigner.signConnection(var3, kernelId);
         var3.setRequestMethod("POST");
         var3.setDoInput(true);
         var3.setDoOutput(true);
         OutputStream var6 = var3.getOutputStream();
         var3.connect();
         var6.write(SERVER_HASH_VALUE);
         var6.flush();
         var6.close();
         var4 = new DataInputStream(var3.getInputStream());
         if (var3.getResponseCode() != 200) {
            throw new IOException("Unexpected error on the server didn''t receive response code 200");
         }

         if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
            ClusterAnnouncementsDebugLogger.debug("Request " + var2 + " CONTENT LENGTH " + var3.getContentLength());
         }

         byte[] var7 = this.readHttpResponse(var4, var3.getContentLength());
         var5 = MulticastManager.getInputStream(var7);
         int var8 = var5.readInt();

         for(int var9 = 0; var9 < var8; ++var9) {
            MemberAttributes var10 = (MemberAttributes)var5.readObject();
            StateDumpMessage var11 = (StateDumpMessage)var5.readObject();
            if (var11 != null) {
               this.processAttributes(var10);
               AnnouncementManager.theOne().receiveStateDump(var10.identity(), var11);
            }
         }

         MemberAttributes var33 = (MemberAttributes)var5.readObject();
         this.processAttributes(var33);
         StateDumpMessage var34 = (StateDumpMessage)var5.readObject();
         AnnouncementManager.theOne().receiveStateDump(var33.identity(), var34);
         ClusterLogger.logFetchClusterStateDumpComplete(var1.serverName());
      } catch (MalformedURLException var29) {
         throw new AssertionError("Unexpected exception", var29);
      } catch (IOException var30) {
         ClusterHelper.logStateDumpRequestRejection(var3, var30, var1.serverName());
      } catch (ClassNotFoundException var31) {
         ClusterLogger.logFailedToDeserializeStateDump(var1.serverName(), var31);
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var28) {
         }

         try {
            if (var5 != null) {
               var5.close();
            }
         } catch (IOException var27) {
         }

         if (var3 != null) {
            var3.disconnect();
         }

      }

   }

   private URL getServerURL(ClusterMemberInfo var1) throws MalformedURLException {
      return ClusterHelper.fabricateHTTPURL("/bea_wls_cluster_internal/a2e2gp2r2/p2s2" + SECRET_STRING, var1.identity());
   }

   synchronized void shutdown() {
      Iterator var1 = this.remoteMembers.values().iterator();

      while(var1.hasNext()) {
         RemoteMemberInfo var2 = (RemoteMemberInfo)var1.next();
         var2.shutdown();
      }

      this.remoteMembers.clear();
      this.ClusterMembersListeners.clear();
   }

   synchronized RemoteMemberInfo findOrCreate(HostID var1) {
      RemoteMemberInfo var2 = (RemoteMemberInfo)this.remoteMembers.get(var1);
      if (var2 == null) {
         var2 = new RemoteMemberInfo(var1, this.joinTime);
         this.remoteMembers.put(var1, var2);
         var2.numUnprocessedMessages = 1;
      } else {
         ++var2.numUnprocessedMessages;
      }

      return var2;
   }

   List<RemoteMemberInfo> getRemoteMemberInfos() {
      ArrayList var1 = new ArrayList();
      synchronized(this.remoteMembers) {
         Iterator var3 = this.remoteMembers.values().iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (var4 instanceof RemoteMemberInfo) {
               var1.add((RemoteMemberInfo)var4);
            }
         }

         return var1;
      }
   }

   synchronized void done(RemoteMemberInfo var1) {
      --var1.numUnprocessedMessages;
      if (this.waitingForFirstHeartbeats && var1.numUnprocessedMessages == 0 && var1.getAttributes() != null && var1.isRunning()) {
         this.waitingForFirstHeartbeats = false;
         this.notify();
      }

   }

   public void resetTimeout(HostID var1) {
      RemoteMemberInfo var2 = this.findOrCreate(var1);

      try {
         var2.resetTimeout();
      } finally {
         this.done(var2);
      }

   }

   synchronized void checkTimeouts() {
      Iterator var1 = this.remoteMembers.values().iterator();

      while(var1.hasNext()) {
         RemoteMemberInfo var2 = (RemoteMemberInfo)var1.next();
         boolean var3 = var2.checkTimeout();
         if (var2.numUnprocessedMessages == 0 && var3) {
            if (var2.getAttributes() != null) {
               ClusterLogger.logRemovingServerDueToTimeout(var2.getAttributes().toString());
            }

            var1.remove();
            var2.shutdown();
         }
      }

   }

   public synchronized void peerGone(final PeerGoneEvent var1) {
      final RemoteMemberInfo var2 = (RemoteMemberInfo)this.remoteMembers.get(var1.getID());
      if (var2 != null) {
         if (var2.numUnprocessedMessages == 0) {
            if (var2.getAttributes() != null) {
               ClusterLogger.logRemovingServerDueToPeerGone(var2.getAttributes().toString());
            }

            final Map var4 = this.remoteMembers;
            SecurityServiceManager.runAs(kernelId, kernelId, new PrivilegedAction() {
               public Object run() {
                  var4.remove(var1.getID());
                  var2.shutdown();
                  return null;
               }
            });
         } else {
            var2.forceTimeout();
         }
      }

   }

   synchronized void shutdown(final HostID var1) {
      final RemoteMemberInfo var2 = (RemoteMemberInfo)this.remoteMembers.get(var1);
      if (var2 != null && var2.getAttributes() != null) {
         ClusterLogger.logServerSuspended(var2.getAttributes().serverName());
         SecurityServiceManager.runAs(kernelId, kernelId, new PrivilegedAction() {
            public Object run() {
               MemberManager.this.remoteMembers.remove(var1);
               var2.shutdown();
               return null;
            }
         });
      }

   }

   Collection getRemoteMembers() {
      return this.getRemoteMembers(false);
   }

   Collection getRemoteMembers(boolean var1) {
      ArrayList var2 = new ArrayList();
      HashMap var3;
      synchronized(this) {
         var3 = (HashMap)((HashMap)this.remoteMembers).clone();
      }

      Iterator var4 = var3.values().iterator();

      while(true) {
         RemoteMemberInfo var5;
         MemberAttributes var6;
         do {
            while(true) {
               do {
                  if (!var4.hasNext()) {
                     return var2;
                  }

                  var5 = (RemoteMemberInfo)var4.next();
                  var6 = var5.getAttributes();
               } while(var6 == null);

               if (var1) {
                  break;
               }

               if (var5.isRunning()) {
                  var2.add(var6);
               }
            }
         } while(!var5.isRunning() && !var5.isSuspended());

         var2.add(var6);
      }
   }

   void fireClusterMembersChangeEvent(ClusterMemberInfo var1, int var2) {
      synchronized(this.ClusterMembersListeners) {
         final ClusterMembersChangeEvent var4 = new ClusterMembersChangeEvent(this, var2, var1);
         Iterator var5 = this.ClusterMembersListeners.iterator();

         while(var5.hasNext()) {
            final ClusterMembersChangeListener var6 = (ClusterMembersChangeListener)var5.next();
            WorkAdapter var7 = new WorkAdapter() {
               private ClusterMembersChangeEvent event = var4;
               private ClusterMembersChangeListener listener = var6;

               public void run() {
                  this.listener.clusterMembersChanged(this.event);
               }

               public String toString() {
                  return "Cluster Members Changed";
               }
            };
            WorkManagerFactory.getInstance().getSystem().schedule(var7);
         }

      }
   }

   void addClusterMembersListener(ClusterMembersChangeListener var1) {
      synchronized(this.ClusterMembersListeners) {
         this.ClusterMembersListeners.add(var1);
      }
   }

   void removeClusterMembersListener(ClusterMembersChangeListener var1) {
      synchronized(this.ClusterMembersListeners) {
         int var3 = this.ClusterMembersListeners.indexOf(var1);
         if (var3 > -1) {
            this.ClusterMembersListeners.remove(var3);
         }

      }
   }

   private byte[] readHttpResponse(DataInputStream var1, int var2) throws IOException, ProtocolException {
      byte[] var3 = new byte[var2];
      DataIO.readFully(var1, var3);
      return var3;
   }

   private void processAttributes(MemberAttributes var1) {
      RemoteMemberInfo var2 = this.findOrCreate(var1.identity());

      try {
         var2.processAttributes(var1);
      } finally {
         this.done(var2);
      }

   }

   public void sendMemberRuntimeState() throws IOException {
      GroupMessage var1 = this.createRecoverMessage();
      this.runtimeStateSender.send(var1);
   }

   public synchronized void updateMemberRuntimeState(HostID var1, int var2, long var3) {
      RemoteMemberInfo var5 = this.findOrCreate(var1);

      try {
         var5.updateRuntimeState(var2);
         MulticastReceiver var6 = var5.findOrCreateReceiver(3, true);
         if (var3 >= 0L) {
            var6.setInSync(var3);
         }
      } finally {
         this.done(var5);
      }

   }

   public synchronized GroupMessage createRecoverMessage() {
      int var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getStableState();
      long var2 = MulticastManager.theOne().findSender(3).getCurrentSeqNum();
      RuntimeStateMessage var4 = new RuntimeStateMessage(var1, AttributeManager.theOne().getLocalAttributes(), var2);
      return var4;
   }

   void dumpDiagnosticImageData(XMLStreamWriter var1) throws XMLStreamException, IOException {
      var1.writeStartElement("MemberManager");
      var1.writeAttribute("clusterName", this.clusterName);
      Iterator var2 = this.getRemoteMemberInfos().iterator();

      while(var2.hasNext()) {
         RemoteMemberInfo var3 = (RemoteMemberInfo)var2.next();
         var3.dumpDiagnosticImageData(var1);
      }

      var1.writeEndElement();
   }

   static {
      SECRET_STRING = "?PeerInfo=" + ClusterHelper.STRINGFIED_PEERINFO + "&ServerName=" + ManagementService.getRuntimeAccess(kernelId).getServer().getName();
      SERVER_HASH_VALUE = ClusterService.getClusterService().getSecureHash();
      theMemberManager = null;
   }
}
