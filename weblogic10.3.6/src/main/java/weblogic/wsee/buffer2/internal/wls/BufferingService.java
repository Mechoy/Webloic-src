package weblogic.wsee.buffer2.internal.wls;

import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.wsee.buffer2.api.common.BufferingFeature;

public class BufferingService extends AbstractServerService {
   private static final Logger LOGGER = Logger.getLogger(BufferingFeature.class.getName());

   public void stop() throws ServiceFailureException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("BufferingService:  stopping BufferingFeature.");
      }

      try {
         BufferingFeature.stop();
      } catch (Exception var2) {
         throw new ServiceFailureException(var2);
      }
   }
}
