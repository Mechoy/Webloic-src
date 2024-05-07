package weblogic.jms.adapter;

import javax.resource.ResourceException;
import javax.resource.spi.EISSystemException;
import weblogic.jms.bridge.AdapterConnection;
import weblogic.jms.bridge.AdapterConnectionMetaData;

public class AdapterConnectionMetaDataImpl implements AdapterConnectionMetaData {
   private JMSManagedConnection mc;
   private JMSManagedConnectionFactory mcf;

   public AdapterConnectionMetaDataImpl(JMSManagedConnection var1, JMSManagedConnectionFactory var2) {
      this.mc = var1;
      this.mcf = var2;
   }

   public String getProductName() throws ResourceException {
      return new String("Java Messaging Service");
   }

   public String getProductVersion() throws ResourceException {
      return new String("1.0.2");
   }

   public String getUserName() throws ResourceException {
      return this.mc.getUserName();
   }

   public boolean implementsMDBTransaction() throws ResourceException {
      try {
         AdapterConnection var1 = this.mc.getJMSBaseConnection();
         return ((JMSBaseConnection)var1).implementsMDBTransaction();
      } catch (Exception var3) {
         EISSystemException var2 = new EISSystemException(var3.getMessage());
         var2.setLinkedException(var3);
         throw var2;
      }
   }

   public boolean isXAConnection() throws ResourceException {
      try {
         AdapterConnection var1 = this.mc.getJMSBaseConnection();
         return ((JMSBaseConnection)var1).isXAConnection();
      } catch (Exception var3) {
         EISSystemException var2 = new EISSystemException(var3.getMessage());
         var2.setLinkedException(var3);
         throw var2;
      }
   }
}
