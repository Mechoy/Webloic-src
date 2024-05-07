package weblogic.t3.srvr;

import weblogic.protocol.configuration.ChannelHelper;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class EnableListenersService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      EnableListenersHelper.getInstance().start();
   }

   public void stop() throws ServiceFailureException {
      if (ChannelHelper.isLocalAdminChannelEnabled()) {
         EnableListenersHelper.getInstance().stop();
      }

   }

   public void halt() throws ServiceFailureException {
      if (ChannelHelper.isLocalAdminChannelEnabled()) {
         EnableListenersHelper.getInstance().halt();
      }

   }
}
