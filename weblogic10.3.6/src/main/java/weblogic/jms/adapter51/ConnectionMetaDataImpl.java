package weblogic.jms.adapter51;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionMetaData;
import weblogic.jms.bridge.AdapterConnection;

public class ConnectionMetaDataImpl implements ManagedConnectionMetaData {
   private JMSManagedConnection mc;

   public ConnectionMetaDataImpl(JMSManagedConnection var1) {
      this.mc = var1;
   }

   public String getEISProductName() throws ResourceException {
      AdapterConnection var1 = this.mc.getJMSBaseConnection();
      return var1.getMetaData().getProductName();
   }

   public String getEISProductVersion() throws ResourceException {
      AdapterConnection var1 = this.mc.getJMSBaseConnection();
      return var1.getMetaData().getProductVersion();
   }

   public int getMaxConnections() throws ResourceException {
      return this.mc.getMaxConnections();
   }

   public String getUserName() throws ResourceException {
      return this.mc.getUserName();
   }
}
