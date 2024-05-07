package weblogic.ejb.container.internal;

import java.io.ObjectOutput;
import weblogic.protocol.ChannelHelperBase;

/** @deprecated */
@Deprecated
public interface URLDelegate {
   /** @deprecated */
   @Deprecated
   URLDelegate CHANNEL_URL_DELEGATE = new URLDelegate() {
      public String getURL(ObjectOutput var1) {
         return ChannelHelperBase.getChannelURL(var1);
      }
   };

   String getURL(ObjectOutput var1);
}
