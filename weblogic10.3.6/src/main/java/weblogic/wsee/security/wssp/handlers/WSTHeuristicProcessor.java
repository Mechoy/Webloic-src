package weblogic.wsee.security.wssp.handlers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.security.wssc.utils.WSSCCompatibilityUtil;

public class WSTHeuristicProcessor {
   private WSSCCompatibilityUtil.OrderingIterator<String> ordering;
   private boolean requireHeuristicStrategy = false;
   private Reserve reserve;

   public Reserve getReserve() {
      return this.reserve;
   }

   public void setReserve(Reserve var1) {
      this.reserve = var1;
   }

   public boolean processRequest(MessageContext var1) {
      this.requireHeuristicStrategy = WSSCCompatibilityUtil.isHeuristicStrategyRequired(var1);
      if (!this.requireHeuristicStrategy) {
         return false;
      } else {
         try {
            this.reserve.reserveContext();
         } catch (SOAPException var5) {
            this.reserve.destroy();
            throw new JAXRPCException("Failed to reserve context, can't process heuristic retrying", var5);
         } catch (IOException var6) {
            this.reserve.destroy();
            throw new JAXRPCException("Failed to reserve context, can't process heuristic retrying", var6);
         }

         WssPolicyContext var2 = WSSCCompatibilityUtil.getPolicyContext(var1);
         String var3 = var2.getWssConfiguration().getCompatibilityOrderingPreference();
         this.ordering = WSSCCompatibilityUtil.getCompatibilityOrdering(var3);
         if (this.ordering.hasNext()) {
            String var4 = (String)this.ordering.next();
            var2.getWssConfiguration().setCompatibilityPreference(var4, var1);
         }

         return true;
      }
   }

   public boolean processResponse(MessageContext var1) {
      if (!this.requireHeuristicStrategy) {
         return false;
      } else {
         if (this.isHeuristicRequired(var1)) {
            if (this.hasNextHeuristic(var1)) {
               try {
                  this.reserve.resetContext();
                  this.nextHeuristic(this.reserve.getSoapMessageContext());
                  return true;
               } catch (IOException var3) {
               } catch (SOAPException var4) {
               }
            }
         } else {
            WSSCCompatibilityUtil.adjustOrderingPreference(var1);
         }

         this.reserve.destroy();
         this.reserve = null;
         this.ordering = null;
         this.requireHeuristicStrategy = false;
         return false;
      }
   }

   private boolean isHeuristicRequired(MessageContext var1) {
      Object var2 = var1.getProperty("weblogic.wsee.security.wssp.handlers.wst_heuristic");
      return var2 != null;
   }

   private boolean hasNextHeuristic(MessageContext var1) {
      return this.ordering.hasNext();
   }

   private boolean nextHeuristic(MessageContext var1) {
      if (this.ordering.hasNext()) {
         String var2 = (String)this.ordering.next();
         WssPolicyContext var3 = WSSCCompatibilityUtil.getPolicyContext(var1);
         var3.getWssConfiguration().setCompatibilityPreference(var2, var1);
         return true;
      } else {
         return false;
      }
   }

   public abstract static class Reserve {
      protected Map<String, Object> properties;
      private ByteArrayOutputStream messageStream;
      private MimeHeaders mimeHeaders;
      private int handlerIndex = -1;

      public abstract SoapMessageContext getSoapMessageContext();

      public void reserveContext() throws SOAPException, IOException {
         if (this.properties == null) {
            this.properties = new HashMap();
         } else {
            this.properties.clear();
         }

         SoapMessageContext var1 = this.getSoapMessageContext();
         Iterator var2 = var1.getPropertyNames();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            Object var4 = var1.getProperty(var3);
            this.properties.put(var3, var4);
         }

         this.mimeHeaders = var1.getMessage().getMimeHeaders();
         this.messageStream = new ByteArrayOutputStream();
         var1.getMessage().writeTo(this.messageStream);
         if (var1.containsProperty("weblogic.wsee.handler.index")) {
            this.handlerIndex = (Integer)var1.getProperty("weblogic.wsee.handler.index");
         }

      }

      public void resetContext() throws IOException, SOAPException {
         SoapMessageContext var1 = this.getSoapMessageContext();
         ArrayList var2 = new ArrayList();
         Iterator var3 = var1.getPropertyNames();

         String var4;
         while(var3.hasNext()) {
            var4 = (String)var3.next();
            var2.add(var4);
         }

         var2.removeAll(this.properties.keySet());
         var3 = var2.iterator();

         while(var3.hasNext()) {
            var4 = (String)var3.next();
            var1.removeProperty(var4);
         }

         var3 = this.properties.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var13 = (Map.Entry)var3.next();
            Object var5 = var1.getProperty((String)var13.getKey());
            if (var13.getValue() != var5) {
               try {
                  var1.setProperty((String)var13.getKey(), var13.getValue());
               } catch (UnsupportedOperationException var11) {
               }
            }
         }

         var1.setFault((Throwable)null);
         ByteArrayInputStream var12 = new ByteArrayInputStream(this.messageStream.toByteArray());

         try {
            var1.setMessage(this.getSoapMessageContext().getMessageFactory().createMessage(this.mimeHeaders, var12));
         } finally {
            var12.close();
         }

         if (this.handlerIndex > -1) {
            var1.setProperty("weblogic.wsee.handler.index", this.handlerIndex);
         }

      }

      public void destroy() {
         try {
            this.messageStream.close();
         } catch (IOException var2) {
         }

      }
   }
}
