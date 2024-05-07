package weblogic.wsee.sender.DefaultProvider;

import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.sender.api.PermanentSendException;

public class ConversationUtil {
   public static Throwable checkForPermanentSendFailure(Throwable var0) {
      return (Throwable)(WsUtil.isPermanentSendFailure(var0) ? new PermanentSendException(var0.toString(), var0) : var0);
   }
}
