package weblogic.wsee.reliability2.tube.processors;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.tube.WsrmTubeUtils;

public class MessageProcessorFactory {
   private static final Map<WsrmConstants.Action, Class<? extends MessageProcessor>> _processors = new HashMap();

   public static MessageProcessor createProcessorForAction(WsrmConstants.Action var0, WsrmTubeUtils var1) {
      try {
         Class var2 = (Class)_processors.get(var0);
         Constructor var3 = var2.getConstructor(WsrmTubeUtils.class);
         return (MessageProcessor)var3.newInstance(var1);
      } catch (Exception var4) {
         throw new RuntimeException(var4.toString(), var4);
      }
   }

   static {
      _processors.put(WsrmConstants.Action.ACK, AckProcessor.class);
      _processors.put(WsrmConstants.Action.ACK_REQUESTED, AckRequestedProcessor.class);
      _processors.put(WsrmConstants.Action.CREATE_SEQUENCE, CreateSequenceProcessor.class);
      _processors.put(WsrmConstants.Action.CREATE_SEQUENCE_RESPONSE, CreateSequenceResponseProcessor.class);
      _processors.put(WsrmConstants.Action.CLOSE_SEQUENCE, CloseSequenceProcessor.class);
      _processors.put(WsrmConstants.Action.CLOSE_SEQUENCE_RESPONSE, CloseSequenceResponseProcessor.class);
      _processors.put(WsrmConstants.Action.TERMINATE_SEQUENCE, TerminateSequenceProcessor.class);
      _processors.put(WsrmConstants.Action.TERMINATE_SEQUENCE_RESPONSE, TerminateSequenceResponseProcessor.class);
      _processors.put(WsrmConstants.Action.LAST_MESSAGE, LastMessageProcessor.class);
   }
}
