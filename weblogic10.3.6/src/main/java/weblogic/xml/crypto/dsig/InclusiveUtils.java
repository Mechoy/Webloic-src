package weblogic.xml.crypto.dsig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Node;
import weblogic.xml.crypto.NodeSetDataImpl;
import weblogic.xml.crypto.api.NodeSetData;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.WSSecurityContext;

public class InclusiveUtils {
   static final String PREFIX_SEP = ":";

   static String[] gatherInclusivePrefixes(NodeSetData var0, Map var1, XMLCryptoContext var2) {
      HashSet var3 = new HashSet();
      HashSet var4 = new HashSet();
      Iterator var5 = var0.iterator();

      while(true) {
         while(true) {
            while(var5.hasNext()) {
               Node var6 = (Node)var5.next();
               String var7 = var6.getLocalName();
               String var8 = var6.getPrefix();
               String var9 = var6.getNamespaceURI();
               if (var6.getNodeType() == 2 && "http://www.w3.org/2000/xmlns/".equals(var9)) {
                  if (var8 == null && "xmlns".equals(var7)) {
                     var4.add("");
                  } else {
                     var4.add(var8);
                  }
               } else if (var8 != null && !var3.contains(var8)) {
                  var3.add(var8);
               }
            }

            HashSet var10 = new HashSet(var1.keySet());
            var3.removeAll(var4);
            var10.removeAll(var3);
            if (var10.contains("")) {
               var10.remove("");
               var10.add("#default");
            }

            return (String[])((String[])var10.toArray(new String[var10.size()]));
         }
      }
   }

   public static String[] getNonVisiblyUsed(Node var0, Map var1, XMLCryptoContext var2) {
      Set var3 = DOMUtils.getNodeSet(var0, false);
      return getNonVisiblyUsed((NodeSetData)(new NodeSetDataImpl(var3)), var1, var2);
   }

   public static String[] getNonVisiblyUsed(NodeSetData var0, Map var1, XMLCryptoContext var2) {
      HashSet var3 = new HashSet();
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         Node var5 = (Node)var4.next();
         if (var5.getNodeType() == 2) {
            String var6 = var5.getLocalName();
            String var7 = var5.getPrefix();
            if (isAttribute(var6, var7)) {
               String var8 = var5.getNamespaceURI();
               if (var8 != null) {
                  QName var9 = new QName(var8, var6);
                  if (DsigConstants.QNAME_VALUE_ATTRIBUTES.contains(var9)) {
                     String var10 = parsePrefix(var5.getNodeValue());
                     if (var10 != null) {
                        var3.add(var10);
                     }
                  }
               }
            }
         }
      }

      WSSecurityContext var11 = (WSSecurityContext)var2.getProperty("weblogic.xml.crypto.wss.WSSecurityContext");
      if (var11 != null) {
         MessageContext var12 = var11.getMessageContext();
         if (var12 != null) {
            Object var14 = var12.getProperty("com.bea.weblogic.xml.crypto.dsig.IncluisveNSPrefixList");
            if (var14 instanceof Boolean && (Boolean)var14 || var14 instanceof String && "true".equalsIgnoreCase((String)var14)) {
               String[] var15 = gatherInclusivePrefixes(var0, var1, var2);
               var3.addAll(Arrays.asList(var15));
            }
         }
      }

      String[] var13 = new String[var3.size()];
      var3.toArray(var13);
      return var13;
   }

   static boolean isAttribute(String var0, String var1) {
      if (var0 != null && var0.equals("xmlns")) {
         return false;
      } else {
         return var1 == null || !var1.equals("xmlns");
      }
   }

   static String parsePrefix(String var0) {
      int var1 = var0.indexOf(":");
      if (var1 < 0) {
         return null;
      } else {
         return var1 == 0 ? "" : var0.substring(0, var1);
      }
   }
}
