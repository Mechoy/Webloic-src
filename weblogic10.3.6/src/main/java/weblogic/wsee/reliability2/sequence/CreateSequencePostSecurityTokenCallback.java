package weblogic.wsee.reliability2.sequence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.wsee.reliability.WsrmSecurityContext;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEvent;
import weblogic.wsee.reliability2.api_internal.WsrmLifecycleEventListenerRegistry;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.security.wssc.base.sct.SCTokenBase;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.v13.sct.SCTokenHandler;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss.provider.SecurityTokenHandler;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class CreateSequencePostSecurityTokenCallback {
   public static String PROPERTY_NAME = "CreateSequencePostSecurityTokenCallback";
   private static final Logger LOGGER = Logger.getLogger(CreateSequencePostSecurityTokenCallback.class.getName());
   private WsrmSecurityContext seqCtx;

   private CreateSequencePostSecurityTokenCallback(WsrmSecurityContext var1) {
      this.seqCtx = var1;
   }

   public static void addCallbackToMap(WsrmSecurityContext var0, Map var1) {
      if (var1 != null && var0 != null) {
         if (var0.isSecureWithWssc()) {
            CreateSequencePostSecurityTokenCallback var2 = new CreateSequencePostSecurityTokenCallback(var0);
            var1.put(PROPERTY_NAME, var2);
            if (LOGGER.isLoggable(Level.FINER)) {
               LOGGER.finer("added CreateSequencePostSecurityTokenCallback to map");
            }

            var0.setJaxWsSecurityTokenCallback(var2);
         }

      } else {
         if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("Ouch, got null map or seqCtx in addCallbackToMap");
         }

      }
   }

   public static void processCallback(MessageContext var0) throws WsrmException {
      CreateSequencePostSecurityTokenCallback var1 = (CreateSequencePostSecurityTokenCallback)var0.getProperty(PROPERTY_NAME);
      if (var1 != null) {
         var1.execute(var0);
         WsrmLifecycleEventListenerRegistry.getInstance().notifyEventType(WsrmLifecycleEvent.Type.AFTER_RSTR_BEFORE_CREATE_SEQ);
         var0.removeProperty(PROPERTY_NAME);
         var1.seqCtx.removeCreateSequencePostSecurityTokenCallback();
      }
   }

   public void execute(MessageContext var1) throws WsrmException {
      if (this.seqCtx.isSecureWithWssc()) {
         Object var4;
         if (this.seqCtx.isSecureWithWssp12Wssc13()) {
            var4 = new SCTokenHandler();
         } else {
            var4 = new weblogic.wsee.security.wssc.v200502.sct.SCTokenHandler();
         }

         SCCredential var5 = (SCCredential)var1.getProperty("weblogic.wsee.wssc.sct");
         if (LOGGER.isLoggable(Level.FINER)) {
            if (var5 != null) {
               LOGGER.finer("Got SCCredential from MessageContext '" + var5.toString() + "'");
            } else {
               LOGGER.finer("Could not find SCCredential in MessageContext, skipping insert of SecurityTokenReference in CreateSequence message");
            }
         }

         if (var5 == null) {
            return;
         }

         SecurityTokenReference var3;
         try {
            SCTokenBase var2 = (SCTokenBase)((SecurityTokenHandler)var4).getSecurityToken("NO-VALUE-TYPE", var5, (ContextHandler)null);
            var3 = ((SecurityTokenHandler)var4).getSTR(WSSConstants.REFERENCE_QNAME, var2.getValueType(), var2);
         } catch (WSSecurityException var14) {
            throw new WsrmException(var14.getMessage(), var14);
         }

         SOAPMessage var6 = ((SOAPMessageContext)var1).getMessage();

         SOAPBody var7;
         try {
            var7 = var6.getSOAPBody();
         } catch (Exception var13) {
            throw new WsrmException("Error getting SOAPBody for <CreateSequence> Element", var13);
         }

         Iterator var8 = var7.getChildElements();
         SOAPElement var9 = (SOAPElement)var8.next();
         if (var9 == null) {
            throw new WsrmException("Expected a SOAPMessage with a SOAPBody that contains <CreateSequence>.   SOAPBody has no children !");
         }

         QName var10 = var9.getElementQName();
         if (!var10.getLocalPart().equals("CreateSequence")) {
            throw new WsrmException("Expected SOAPBody Child to be <CreateSequence>, instead we found QName='" + var10.toString() + "'");
         }

         try {
            var3.marshal(var9, (Node)null, new HashMap());
         } catch (Exception var12) {
            throw new WsrmException("Error adding SecurityTokenReference to CreateSequence Message", var12);
         }

         ((SOAPMessageContext)var1).setMessage(var6);
      }

   }
}
