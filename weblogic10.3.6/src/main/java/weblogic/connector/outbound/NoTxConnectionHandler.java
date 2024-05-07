package weblogic.connector.outbound;

import java.security.AccessController;
import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnection;
import weblogic.connector.security.outbound.SecurityContext;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class NoTxConnectionHandler extends ConnectionHandlerBaseImpl {
   NoTxConnectionHandler(ManagedConnection var1, ConnectionPool var2, SecurityContext var3, ConnectionInfo var4) {
      super(var1, var2, var3, var4, "NoTransaction");
      this.addConnectionRuntimeMBean();
   }

   public void enListResource() throws ResourceException {
   }

   protected void initializeConnectionEventListener() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.connPool.getRAInstanceManager().getAdapterLayer().addConnectionEventListener(this.managedConnection, new NoTxConnectionEventListener(this), var1);
   }
}
