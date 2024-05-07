package weblogic.wsee.handler;

import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.MessageContext;

public interface WLHandler extends Handler {
   boolean handleClosure(MessageContext var1);
}
