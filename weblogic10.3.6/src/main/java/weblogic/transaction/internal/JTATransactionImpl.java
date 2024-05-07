package weblogic.transaction.internal;

import java.io.Serializable;
import java.security.AccessController;
import java.util.Iterator;
import java.util.Map;
import javax.transaction.xa.Xid;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.JTATransaction;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class JTATransactionImpl implements JTATransaction, Serializable {
   private static final long serialVersionUID = -7339816685706551825L;
   private String name;
   private Xid xid;
   private String status;
   private int statusCode;
   private Map userProperties;
   private int secondsActive;
   private String[] servers;
   private Map resourceNamesAndStatus;
   private String coordinatorURL;
   private Map serversAndStatus;
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   JTATransactionImpl(ServerTransactionImpl var1) {
      this.name = var1.getName();
      this.xid = var1.getXID();
      this.status = var1.getStatusAsString();
      this.statusCode = var1.getStatus();
      this.userProperties = var1.getProperties();
      this.secondsActive = (int)(var1.getMillisSinceBegin() / 1000L);
      String[] var2 = var1.getSCNames();
      if (var2 == null) {
         this.servers = new String[1];
         this.servers[0] = ManagementService.getRuntimeAccess(kernelID).getServer().getName();
      } else {
         this.servers = new String[var2.length];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.servers[var3] = var2[var3];
         }
      }

      this.resourceNamesAndStatus = var1.getResourceNamesAndState();
      this.coordinatorURL = var1.getCoordinatorURL();
      this.serversAndStatus = var1.getServersAndState();
   }

   public String getName() {
      return this.name;
   }

   public Xid getXid() {
      return this.xid;
   }

   public String getStatus() {
      return this.status;
   }

   public Map getUserProperties() {
      return this.userProperties;
   }

   public int getSecondsActiveCurrentCount() {
      return this.secondsActive;
   }

   public String[] getServers() {
      return this.servers;
   }

   public Map getResourceNamesAndStatus() {
      return this.resourceNamesAndStatus;
   }

   public String getCoordinatorURL() {
      return this.coordinatorURL;
   }

   public Map getServersAndStatus() {
      return this.serversAndStatus;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(128);
      var1.append("{");
      var1.append(this.getClass().getName());
      var1.append(": ");
      var1.append("name=" + this.name);
      var1.append(", ");
      var1.append("xid=" + this.xid.toString());
      var1.append(", ");
      var1.append("status=" + this.status);
      var1.append(", ");
      var1.append("userProperties=");
      if (this.userProperties != null) {
         var1.append(this.userProperties.toString());
      }

      var1.append(", ");
      var1.append("secondsActive=" + this.secondsActive);
      var1.append(", ");
      var1.append("servers=");

      for(int var2 = 0; var2 < this.servers.length; ++var2) {
         var1.append(this.servers[var2]);
         if (var2 < this.servers.length - 1) {
            var1.append("+");
         }
      }

      var1.append(", ");
      var1.append("resourceNamesAndStatus=");
      Iterator var4 = this.resourceNamesAndStatus.entrySet().iterator();

      Map.Entry var3;
      while(var4.hasNext()) {
         var3 = (Map.Entry)var4.next();
         var1.append((String)var3.getKey());
         var1.append("/");
         var1.append((String)var3.getValue());
         if (var4.hasNext()) {
            var1.append("+");
         }
      }

      var1.append(", ");
      var1.append("coordinatorURL=" + this.coordinatorURL);
      var1.append(", ");
      var1.append("serversAndStatus=");
      var4 = this.serversAndStatus.entrySet().iterator();

      while(var4.hasNext()) {
         var3 = (Map.Entry)var4.next();
         var1.append((String)var3.getKey());
         var1.append("/");
         var1.append((String)var3.getValue());
         if (var4.hasNext()) {
            var1.append("+");
         }
      }

      var1.append("}");
      return var1.toString();
   }
}
