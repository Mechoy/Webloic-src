package weblogic.management.mbeanservers.runtime.internal;

import weblogic.server.ServiceFailureException;

public final class RegisterWithDomainRuntimeServiceLate extends RegisterWithDomainRuntimeService {
   public void start() throws ServiceFailureException {
      if (!this.doEarly()) {
         super.start();
      }

   }

   public void stop() throws ServiceFailureException {
      if (!this.doEarly()) {
         super.stop();
      }

   }
}
