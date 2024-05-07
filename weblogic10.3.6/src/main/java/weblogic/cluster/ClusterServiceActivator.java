package weblogic.cluster;

import java.security.AccessController;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ActivatedService;
import weblogic.server.ServiceActivator;
import weblogic.server.ServiceFailureException;

public final class ClusterServiceActivator extends ServiceActivator {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final ClusterServiceActivator INSTANCE = new ClusterServiceActivator();

   private ClusterServiceActivator() {
      super("weblogic.cluster.ClusterService");
   }

   protected ActivatedService instantiateService() throws ServiceFailureException {
      return super.instantiateService();
   }

   public ClusterServices getClusterService() {
      return (ClusterServices)this.getServiceObj();
   }
}
