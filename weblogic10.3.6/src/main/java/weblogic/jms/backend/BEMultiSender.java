package weblogic.jms.backend;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jms.JMSException;
import weblogic.jms.JMSLogger;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSPushEntry;
import weblogic.jms.common.JMSPushRequest;
import weblogic.jms.common.JMSSecurityException;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.messaging.Message;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.MultiListener;

final class BEMultiSender implements MultiListener {
   private static final boolean debug = false;

   public void multiDeliver(Message var1, List var2) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Pushing message " + ((MessageImpl)var1).getJMSMessageID() + " to " + var2.size() + " consumers");
      }

      Iterator var3 = var2.iterator();
      HashMap var4 = new HashMap(4);

      while(var3.hasNext()) {
         MultiListener.DeliveryInfo var5 = (MultiListener.DeliveryInfo)var3.next();
         BEConsumerImpl var6 = (BEConsumerImpl)var5.getListener();

         try {
            var6.checkPermission(true, false);
         } catch (JMSSecurityException var18) {
            continue;
         }

         MessageElement var7 = var5.getMessageElement();
         MessageImpl var8 = (MessageImpl)var1;
         BESessionImpl var9 = var6.getSession();
         var7.setUserSequenceNum(var9.getNextSequenceNumber());
         var7.setUserData(var6);
         boolean var10 = var6.allowsImplicitAcknowledge();
         boolean var11 = var10 || var9.getAcknowledgeMode() == 4;
         JMSPushEntry var12 = var6.createPushEntry(var7, var10, var11);
         JMSDispatcher var13 = var9.getConnection().getDispatcher();
         JMSPushRequest var14 = (JMSPushRequest)var4.get(var13);
         if (var14 == null) {
            var14 = new JMSPushRequest(13, var9.getSequencerId(), var8, var12);
            var4.put(var13, var14);
         } else {
            var14.setInvocableType(1);
            var14.addPushEntry(var12);
         }

         if (!var11) {
            var6.adjustUnackedCount(1);
            var9.addPendingMessage(var7, var6);
         }

         if (var11 && !var6.isKernelAutoAcknowledge()) {
            try {
               KernelRequest var15 = var6.getKernelQueue().acknowledge(var7);
               if (var15 != null) {
                  var15.getResult();
               }
            } catch (KernelException var17) {
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug("Unexpected exception while implicitly acknowledging: " + var17, var17);
               }
            }
         }
      }

      Iterator var19 = var4.entrySet().iterator();

      while(var19.hasNext()) {
         Map.Entry var20 = (Map.Entry)var19.next();
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Pushing entries to dispatcher " + var20.getKey());
         }

         try {
            JMSServerUtilities.anonDispatchNoReply((JMSPushRequest)var20.getValue(), (JMSDispatcher)var20.getKey());
         } catch (JMSException var16) {
            JMSLogger.logErrorPushingMessage(var16.toString(), var16);
         }
      }

   }
}
