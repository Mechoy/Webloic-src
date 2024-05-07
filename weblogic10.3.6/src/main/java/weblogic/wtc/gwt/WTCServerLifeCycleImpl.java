package weblogic.wtc.gwt;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class WTCServerLifeCycleImpl extends AbstractServerService {
   private WTCService wtcService;

   public void start() throws ServiceFailureException {
      try {
         this.wtcService = WTCService.getService();
         this.wtcService.initializeCommon();
         this.wtcService.resumeCommon();
      } catch (Throwable var2) {
         throw new ServiceFailureException("WTC service failed in initialization", var2);
      }
   }

   public void stop() throws ServiceFailureException {
      try {
         this.wtcService.suspend(false);
         this.shutdown();
      } catch (Throwable var2) {
         var2.printStackTrace();
         throw new ServiceFailureException("Failed to suspend WTC service. The first Throwable is " + var2);
      }
   }

   public void halt() throws ServiceFailureException {
      try {
         this.wtcService.suspend(true);
         this.shutdown();
      } catch (Throwable var2) {
         var2.printStackTrace();
         throw new ServiceFailureException("Failed to suspend WTC service. The first Throwable is " + var2);
      }
   }

   private void shutdown() throws ServiceFailureException {
      try {
         this.wtcService.shutdownCommon();
      } catch (Exception var2) {
         throw new ServiceFailureException("WTCService failed to shutdown ", var2);
      }
   }
}
