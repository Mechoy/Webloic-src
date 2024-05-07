package weblogic.cluster;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public class AlternateLivelinessChecker {
   private static final boolean DEBUG = false;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String SECRET_STRING;
   private static final byte[] SERVER_HASH_VALUE;
   private HashMap unreachableMap = new HashMap();
   private Set underExecutionSet = new HashSet();

   public static AlternateLivelinessChecker getInstance() {
      return AlternateLivelinessChecker.Factory.THE_ONE;
   }

   public synchronized void reachable(HostID var1) {
      this.unreachableMap.remove(var1);
   }

   private int getRetryCount() {
      ClusterMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      return var1.getHTTPPingRetryCount();
   }

   private int getCurrentCount(HostID var1) {
      Integer var2 = (Integer)this.unreachableMap.get(var1);
      return var2 != null ? var2 : 0;
   }

   private int getActiveServerRetryCount() {
      Iterator var1 = this.unreachableMap.keySet().iterator();
      int var2 = 0;

      while(var1.hasNext()) {
         if (this.getCurrentCount((HostID)var1.next()) < this.getRetryCount()) {
            ++var2;
         }
      }

      return var2;
   }

   synchronized void addToUnreachableSet(HostID var1) {
      int var2 = this.getCurrentCount(var1);
      ++var2;
      this.unreachableMap.put(var1, var2);
      this.underExecutionSet.remove(var1);
   }

   synchronized void addToReachableSet(HostID var1) {
      this.unreachableMap.put(var1, 0);
      this.underExecutionSet.remove(var1);
   }

   public synchronized boolean isUnreachable(long var1, HostID var3) {
      ClusterMBean var4 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      if (var4.getMaxServerCountForHttpPing() <= 0) {
         return true;
      } else if (this.getCurrentCount(var3) >= this.getRetryCount()) {
         return true;
      } else if (this.underExecutionSet.contains(var3)) {
         return false;
      } else if (this.getActiveServerRetryCount() >= var4.getMaxServerCountForHttpPing()) {
         return true;
      } else {
         if (this.unreachableMap.get(var3) == null) {
            this.unreachableMap.put(var3, 0);
         }

         HTTPPingRequest var5 = new HTTPPingRequest(var1, var3);
         WorkManagerFactory.getInstance().getSystem().schedule(var5);
         this.underExecutionSet.add(var3);
         return false;
      }
   }

   private static void debug(String var0) {
      Debug.say("[AlternateLivenessChecker] " + var0);
   }

   static {
      SECRET_STRING = "&ServerName=" + ManagementService.getRuntimeAccess(kernelId).getServer().getName() + "&PingOnly=true";
      SERVER_HASH_VALUE = ClusterService.getClusterService().getSecureHash();
   }

   private final class HTTPPingRequest extends WorkAdapter {
      private static final boolean DEBUG = false;
      private HttpURLConnection con;
      private DataInputStream in;
      private final String request;
      private final HostID memberID;
      private final long lastSeqNum;

      HTTPPingRequest(long var2, HostID var4) {
         this.lastSeqNum = var2;
         this.request = this.getHeader();
         this.memberID = var4;
      }

      private void connect() throws ConnectException, IOException {
         URL var1 = this.getServerURL();
         this.con = (HttpURLConnection)var1.openConnection();
         this.con.setRequestMethod("POST");
         this.con.setDoInput(true);
         this.con.setDoOutput(true);
         OutputStream var2 = this.con.getOutputStream();
         this.con.connect();
         var2.write(AlternateLivelinessChecker.SERVER_HASH_VALUE);
         var2.flush();
         var2.close();
         this.in = new DataInputStream(this.con.getInputStream());
      }

      private URL getServerURL() throws MalformedURLException {
         return ClusterHelper.fabricateHTTPURL(this.request, this.memberID);
      }

      public void run() {
         try {
            this.connect();
            if (this.con.getResponseCode() == 200) {
               AlternateLivelinessChecker.this.addToReachableSet(this.memberID);
            } else {
               AlternateLivelinessChecker.this.addToUnreachableSet(this.memberID);
            }
         } catch (ConnectException var13) {
            AlternateLivelinessChecker.this.addToUnreachableSet(this.memberID);
         } catch (IOException var14) {
            AlternateLivelinessChecker.this.addToUnreachableSet(this.memberID);
         } finally {
            try {
               if (this.in != null) {
                  this.in.close();
               }
            } catch (IOException var12) {
            }

            if (this.con != null) {
               this.con.disconnect();
            }

         }

      }

      private String getHeader() {
         return "/bea_wls_cluster_internal/psquare/p2?senderNum=0&lastSeqNum=" + this.lastSeqNum + "&PeerInfo=" + ClusterHelper.STRINGFIED_PEERINFO + AlternateLivelinessChecker.SECRET_STRING;
      }
   }

   private static final class Factory {
      static final AlternateLivelinessChecker THE_ONE = new AlternateLivelinessChecker();
   }
}
