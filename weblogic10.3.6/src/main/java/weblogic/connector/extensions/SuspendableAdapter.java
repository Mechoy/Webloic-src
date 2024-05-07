package weblogic.connector.extensions;

import java.io.Serializable;
import java.util.Properties;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;

public class SuspendableAdapter implements ResourceAdapter, Suspendable, Serializable {
   public void start(BootstrapContext var1) throws ResourceAdapterInternalException {
   }

   public void stop() {
   }

   public void endpointActivation(MessageEndpointFactory var1, ActivationSpec var2) throws ResourceException {
      throw new NotSupportedException();
   }

   public void endpointDeactivation(MessageEndpointFactory var1, ActivationSpec var2) {
   }

   public XAResource[] getXAResources(ActivationSpec[] var1) throws ResourceException {
      throw new NotSupportedException();
   }

   public void suspend(int var1, Properties var2) throws ResourceException {
   }

   public void suspendInbound(MessageEndpointFactory var1, Properties var2) throws ResourceException {
   }

   public void resumeInbound(MessageEndpointFactory var1, Properties var2) throws ResourceException {
   }

   public void resume(int var1, Properties var2) throws ResourceException {
   }

   public boolean supportsSuspend(int var1) {
      return false;
   }

   public boolean isSuspended(int var1) {
      return false;
   }

   public boolean isInboundSuspended(MessageEndpointFactory var1) throws ResourceException {
      return false;
   }

   public boolean supportsInit() {
      return false;
   }

   public boolean supportsVersioning() {
      return false;
   }

   public void startVersioning(ResourceAdapter var1, Properties var2) throws ResourceException {
   }

   public void init(ResourceAdapter var1, Properties var2) throws ResourceException {
   }
}
