package weblogic.ejb.container.internal;

import java.io.ObjectOutput;
import weblogic.protocol.configuration.ChannelHelper;

/** @deprecated */
@Deprecated
class URLDelegateProvider {
   private static final URLDelegate CLUSTERED_URL_DELEGATE = new URLDelegate() {
      public String getURL(ObjectOutput var1) {
         return ChannelHelper.getClusterURL(var1);
      }
   };

   static URLDelegate getURLDelegate(boolean var0) {
      return var0 ? CLUSTERED_URL_DELEGATE : URLDelegate.CHANNEL_URL_DELEGATE;
   }
}
