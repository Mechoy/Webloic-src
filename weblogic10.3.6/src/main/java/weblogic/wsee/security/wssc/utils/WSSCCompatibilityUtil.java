package weblogic.wsee.security.wssc.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.rpc.handler.MessageContext;
import weblogic.utils.StringUtils;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.ws.WsService;

public class WSSCCompatibilityUtil {
   public static final int WSSC13 = 1;
   public static final int MSFT = 2;
   public static final int WSSC14 = 3;
   public static final String WST_HEURISTIC_FLAG = "weblogic.wsee.security.wssp.handlers.wst_heuristic";
   private static final String RST_ACTION_V200502 = "/trust/RST/";
   private static final String RST_ACTION_V13 = "ws-trust/200512/RST/";
   private static final String SCT_CANCEL = "/SCT/Cancel";
   private static final String SCT_RENEW = "/SCT/Renew";

   public static int getWSSCVersion(MessageContext var0) {
      String var1 = (String)var0.getProperty("weblogic.wsee.policy.compat.preference");
      return getWSSCVersion(var1);
   }

   public static int getWSSCVersion(String var0) {
      if ("wssc1.3".equals(var0)) {
         return 1;
      } else if ("msft".equals(var0)) {
         return 2;
      } else {
         return var0 != null && !"".equals(var0) && !"wssc1.4".equals(var0) ? 3 : 3;
      }
   }

   public static OrderingIterator<String> getCompatibilityOrdering(String var0) {
      if (var0 == null || "".equals(var0)) {
         var0 = "wssc1.4_wssc1.3_msft";
      }

      String[] var1 = StringUtils.splitCompletely(var0, "_");
      OrderingIterator var2 = new OrderingIterator(Arrays.asList(var1));
      return var2;
   }

   public static String getCompatibilityOrdering(OrderingIterator<String> var0) {
      return StringUtils.join((String[])var0.toArray(new String[var0.size()]), "_");
   }

   public static OrderingIterator<String> resetCompatibilityOrdering(OrderingIterator<String> var0, String var1) {
      var0.resetFirst(var1);
      return var0;
   }

   public static boolean isHeuristicCompatibility() {
      return "true".equalsIgnoreCase(System.getProperty("weblogic.wsee.policy.compat.dynamic"));
   }

   public static boolean isHeuristicStrategyRequired(MessageContext var0) {
      String var1 = (String)var0.getProperty("weblogic.wsee.addressing.Action");
      if (var1 == null) {
         WlMessageContext var2 = WlMessageContext.narrow(var0);
         MsgHeaders var3 = var2.getHeaders();
         ActionHeader var4 = (ActionHeader)var3.getHeader(ActionHeader.TYPE);
         if (var4 != null) {
            var1 = var4.getActionURI();
            var0.setProperty("weblogic.wsee.addressing.Action", var1);
         }
      }

      if (var1 == null || var1.indexOf("/trust/RST/") <= -1 && var1.indexOf("ws-trust/200512/RST/") <= -1) {
         return false;
      } else {
         return var1.endsWith("/SCT/Cancel") || var1.endsWith("/SCT/Renew");
      }
   }

   public static void adjustOrderingPreference(MessageContext var0) {
      WssPolicyContext var1 = getPolicyContext(var0);
      String var2 = var1.getWssConfiguration().getCompatibilityPreference(var0);
      synchronized(var1.getWssConfiguration()) {
         String var4 = var1.getWssConfiguration().getCompatibilityOrderingPreference();
         OrderingIterator var5 = getCompatibilityOrdering(var4);
         resetCompatibilityOrdering(var5, var2);
         String var6 = getCompatibilityOrdering(var5);
         var1.getWssConfiguration().setCompatibilityOrderingPreference(var6);
      }
   }

   public static WssPolicyContext getPolicyContext(MessageContext var0) {
      WssPolicyContext var1 = (WssPolicyContext)var0.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
      if (var1 == null) {
         WlMessageContext var2 = WlMessageContext.narrow(var0);
         WsService var3 = var2.getDispatcher().getWsPort().getEndpoint().getService();
         var1 = var3.getWssPolicyContext();
         var0.setProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx", var1);
      }

      return var1;
   }

   public static class OrderingIterator<E> implements Iterator<E> {
      private LinkedList<E> ordering = null;
      private Iterator<E> it = null;

      public OrderingIterator(List<E> var1) {
         if (var1 instanceof LinkedList) {
            this.ordering = (LinkedList)var1;
         } else {
            this.ordering = new LinkedList(var1);
         }

         this.it = this.ordering.iterator();
      }

      public Object[] toArray() {
         return this.ordering.toArray();
      }

      public <T> T[] toArray(T[] var1) {
         return this.ordering.toArray(var1);
      }

      public int size() {
         return this.ordering.size();
      }

      public void resetFirst(E var1) {
         if (!this.ordering.getFirst().equals(var1)) {
            this.ordering.removeFirstOccurrence(var1);
            this.ordering.addFirst(var1);
         }

         this.it = this.ordering.iterator();
      }

      public boolean hasNext() {
         return this.it.hasNext();
      }

      public E next() {
         return this.it.next();
      }

      public void remove() {
         this.it.remove();
      }
   }
}
