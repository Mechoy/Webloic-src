package weblogic.wsee.buffer2.api.j2ee;

import java.util.Map;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import weblogic.wsee.buffer2.api.common.BufferingDispatch;
import weblogic.wsee.buffer2.api.common.BufferingManager;
import weblogic.wsee.buffer2.exception.BufferingException;
import weblogic.wsee.buffer2.internal.common.JmsSessionPool;

public class BufferingManager_J2EE extends BufferingManager {
   public BufferingManager_J2EE() throws BufferingException {
   }

   protected void sendMessagePlatform(String var1, QueueSender var2, Message var3) throws JMSException {
      throw new JMSException("J2EE version NYI.");
   }

   public <T extends BufferingDispatch> T newBufferingDispatch() {
      throw new RuntimeException("J2EE version NYI.");
   }

   protected JmsSessionPool getNonTransactedSessionPoolPlatform() throws BufferingException {
      throw new BufferingException("J2EE version NYI.");
   }

   protected QueueSender getQueueSenderPlatform(QueueSession var1, String var2, String var3) throws JMSException {
      throw new RuntimeException("J2EE version NYI.");
   }

   protected void bufferMessagePlatform(Message var1, QueueSender var2, String var3, long var4, int var6, String var7, long var8) throws BufferingException {
      throw new BufferingException("J2EE version NYI.");
   }

   public void cleanupDynamicMDBs(Map<String, Object> var1) {
   }
}
