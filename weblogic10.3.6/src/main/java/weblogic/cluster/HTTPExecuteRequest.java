package weblogic.cluster;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import weblogic.common.internal.WLObjectInputStream;
import weblogic.management.provider.ManagementService;
import weblogic.management.servlet.ConnectionSigner;
import weblogic.protocol.URLManager;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.io.DataIO;
import weblogic.work.WorkAdapter;

final class HTTPExecuteRequest extends WorkAdapter {
   private HttpURLConnection con;
   private DataInputStream in;
   private final String request;
   private final int senderNum;
   private final HostID memberID;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String SECRET_STRING;
   private static final byte[] SERVER_HASH_VALUE;
   private static final boolean DEBUG = false;

   HTTPExecuteRequest(long var1, int var3, HostID var4) {
      this.senderNum = var3;
      this.request = this.getHeader(var1);
      this.memberID = var4;
   }

   private void connect() throws ConnectException, IOException {
      URL var1 = this.getServerURL();
      this.con = URLManager.createAdminHttpConnection(var1);
      ConnectionSigner.signConnection(this.con, kernelId);
      this.con.setRequestMethod("POST");
      this.con.setDoInput(true);
      this.con.setDoOutput(true);
      OutputStream var2 = this.con.getOutputStream();
      this.con.connect();
      var2.write(SERVER_HASH_VALUE);
      var2.flush();
      var2.close();
      this.in = new DataInputStream(this.con.getInputStream());
   }

   private URL getServerURL() throws MalformedURLException {
      return ClusterHelper.fabricateHTTPURL(this.request, this.memberID);
   }

   public void run() {
      try {
         if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
            ClusterLogger.logFetchServerStateDump(this.memberID.toString());
         }

         this.connect();
         if (this.con.getResponseCode() != 200) {
            throw new IOException("Failed to get OK response");
         }

         byte[] var1 = this.readHttpResponse(this.in, this.con.getContentLength());
         WLObjectInputStream var2 = MulticastManager.getInputStream(var1);
         MemberAttributes var3 = (MemberAttributes)var2.readObject();
         this.processAttributes(var3);
         final GroupMessage var4 = (GroupMessage)var2.readObject();
         long var5 = var2.readLong();
         final HostID var7 = this.memberID;
         SecurityServiceManager.runAs(kernelId, kernelId, new PrivilegedAction() {
            public Object run() {
               var4.execute(var7);
               if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
                  ClusterLogger.logFetchClusterStateDumpComplete(HTTPExecuteRequest.this.memberID.toString());
               }

               return null;
            }
         });
      } catch (ConnectException var21) {
         if (ClusterAnnouncementsDebugLogger.isDebugEnabled()) {
            ClusterLogger.logFailedWhileReceivingStateDump(this.memberID.toString(), var21);
         }
      } catch (IOException var22) {
         ClusterHelper.logStateDumpRequestRejection(this.con, var22, this.memberID.toString());
      } catch (ClassNotFoundException var23) {
         ClusterLogger.logFailedToDeserializeStateDump(this.memberID.toString(), var23);
      } finally {
         try {
            if (this.in != null) {
               this.in.close();
            }
         } catch (IOException var20) {
         }

         if (this.con != null) {
            this.con.disconnect();
         }

         this.resetHTTPRequestDispatchFlag();
      }

   }

   private void resetHTTPRequestDispatchFlag() {
      RemoteMemberInfo var1 = MemberManager.theOne().findOrCreate(this.memberID);

      try {
         HybridMulticastReceiver var2 = (HybridMulticastReceiver)var1.findOrCreateReceiver(this.senderNum, true);
         var2.setHttpRequestDispatched(false);
      } finally {
         MemberManager.theOne().done(var1);
      }

   }

   private String getHeader(long var1) {
      return "/bea_wls_cluster_internal/psquare/p2?senderNum=" + this.senderNum + "&lastSeqNum=" + var1 + "&PeerInfo=" + ClusterHelper.STRINGFIED_PEERINFO + SECRET_STRING;
   }

   private byte[] readHttpResponse(DataInputStream var1, int var2) throws IOException, ProtocolException {
      byte[] var3 = new byte[var2];
      DataIO.readFully(var1, var3);
      return var3;
   }

   private void processAttributes(MemberAttributes var1) {
      RemoteMemberInfo var2 = MemberManager.theOne().findOrCreate(var1.identity());

      try {
         var2.processAttributes(var1);
      } finally {
         MemberManager.theOne().done(var2);
      }

   }

   static {
      SECRET_STRING = "&ServerName=" + ManagementService.getRuntimeAccess(kernelId).getServer().getName();
      SERVER_HASH_VALUE = ClusterService.getClusterService().getSecureHash();
   }
}
