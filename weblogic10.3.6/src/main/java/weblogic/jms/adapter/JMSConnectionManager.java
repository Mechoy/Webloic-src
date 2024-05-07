package weblogic.jms.adapter;

import java.io.Serializable;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;

public class JMSConnectionManager implements ConnectionManager, Serializable {
   static final long serialVersionUID = -451611469432230705L;

   public Object allocateConnection(ManagedConnectionFactory var1, ConnectionRequestInfo var2) throws ResourceException {
      ManagedConnection var3 = var1.createManagedConnection((Subject)null, var2);
      return var3.getConnection((Subject)null, var2);
   }
}
