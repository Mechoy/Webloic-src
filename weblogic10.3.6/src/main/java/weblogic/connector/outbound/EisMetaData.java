package weblogic.connector.outbound;

import java.security.AccessController;
import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import weblogic.connector.common.Debug;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class EisMetaData {
   public String productName;
   public String productVersion;
   public String maxConnections;
   public String userName;
   private static String unavailable = Debug.getStringUnavailable();

   EisMetaData() {
      this.productName = unavailable;
      this.productVersion = unavailable;
      this.maxConnections = unavailable;
      this.userName = unavailable;
   }

   public static EisMetaData getMetaData(Object var0, ConnectionPool var1) {
      ManagedConnectionMetaData var2 = null;
      EisMetaData var3 = new EisMetaData();
      AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      String var6;
      try {
         var2 = var1.getRAInstanceManager().getAdapterLayer().getMetaData((ManagedConnection)var0, var4);
         if (var2 != null) {
            try {
               var3.productName = var1.getRAInstanceManager().getAdapterLayer().getEISProductName(var2, var4);
            } catch (ResourceException var10) {
            }

            try {
               var3.productVersion = var1.getRAInstanceManager().getAdapterLayer().getEISProductVersion(var2, var4);
            } catch (ResourceException var9) {
            }

            try {
               var3.maxConnections = String.valueOf(var1.getRAInstanceManager().getAdapterLayer().getMaxConnections(var2, var4));
            } catch (ResourceException var8) {
            }

            try {
               var3.userName = var1.getRAInstanceManager().getAdapterLayer().getUserName(var2, var4);
            } catch (ResourceException var7) {
            }
         }
      } catch (ResourceException var11) {
         var6 = var1.getRAInstanceManager().getAdapterLayer().toString(var11, var4);
         Debug.connections("For pool " + var1.getName() + ", ResourceException while getting meta data for ManagedConnection.  Reason: " + var6);
      } catch (Exception var12) {
         var6 = var1.getRAInstanceManager().getAdapterLayer().toString(var12, var4);
         Debug.connections("For pool " + var1.getName() + ", unexpected Exception while getting meta data for ManagedConnection.  Reason: " + var6);
      }

      return var3;
   }
}
