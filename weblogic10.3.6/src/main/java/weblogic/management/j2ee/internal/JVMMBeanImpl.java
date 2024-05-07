package weblogic.management.j2ee.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.management.ObjectName;
import weblogic.management.j2ee.JVMMBean;

public final class JVMMBeanImpl extends J2EEManagedObjectMBeanImpl implements JVMMBean {
   private final ObjectName server;
   private String host = null;
   private static final String LA_DEFAULT = "127.0.0.1";

   public JVMMBeanImpl(String var1, ObjectName var2) {
      super(var1);
      this.server = var2;
   }

   public String getnode() {
      if (this.host != null) {
         return this.host;
      } else {
         String var1 = this.getListenAddress();

         try {
            if (!var1.equals("localhost") && !var1.equals("127.0.0.0")) {
               this.host = InetAddress.getByName(var1).getHostName();
            } else {
               this.host = InetAddress.getLocalHost().getHostName();
            }
         } catch (UnknownHostException var3) {
            throw new Error(var3);
         }

         return this.host;
      }
   }

   private String getListenAddress() {
      String var1 = null;

      try {
         var1 = (String)MBeanServerConnectionProvider.geRuntimeMBeanServerConnection().getAttribute(this.getServerON(), "ListenAddress");
      } catch (Throwable var3) {
         throw new Error(var3);
      }

      return var1 != null ? var1 : "127.0.0.1";
   }

   private ObjectName getServerON() {
      String var1 = this.server.getDomain() + ":Name=" + this.server.getKeyProperty("Name") + "," + "Type=Server";

      try {
         return new ObjectName(var1);
      } catch (Throwable var3) {
         throw new Error(var3);
      }
   }

   public String getjavaVendor() {
      return System.getProperty("java.vendor");
   }

   public String getjavaVersion() {
      return System.getProperty("java.version");
   }
}
