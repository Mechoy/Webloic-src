package weblogic.jndi.internal;

import java.rmi.RemoteException;
import weblogic.diagnostics.image.ImageManager;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class RemoteNamingService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      try {
         ServerHelper.exportObject(RootNamingNode.getSingleton(), "");
         ServerHelper.exportObject(new RemoteContextFactoryImpl());
         ImageManager.getInstance().registerImageSource("JNDI_IMAGE_SOURCE", JNDIImageSource.getJNDIImageSource());
      } catch (RemoteException var2) {
         throw new ServiceFailureException(var2);
      }
   }
}
