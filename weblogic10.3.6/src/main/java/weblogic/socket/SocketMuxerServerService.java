package weblogic.socket;

import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class SocketMuxerServerService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      SocketMuxer.initSocketMuxerOnServer();
   }
}
