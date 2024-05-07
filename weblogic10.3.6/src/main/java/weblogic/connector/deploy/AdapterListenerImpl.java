package weblogic.connector.deploy;

import weblogic.connector.external.AdapterListener;
import weblogic.work.ShutdownCallback;

public class AdapterListenerImpl implements AdapterListener {
   ShutdownCallback shutdownCallback;

   public AdapterListenerImpl(ShutdownCallback var1) {
      this.shutdownCallback = var1;
   }

   public void completed() {
      this.shutdownCallback.completed();
   }
}
