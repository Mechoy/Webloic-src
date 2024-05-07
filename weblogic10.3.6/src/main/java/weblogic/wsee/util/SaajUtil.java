package weblogic.wsee.util;

import java.io.PrintStream;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import org.w3c.dom.Node;

public class SaajUtil {
   public static String getSoapProtocol(boolean var0) {
      return var0 ? "SOAP 1.2 Protocol" : "SOAP 1.1 Protocol";
   }

   public static SOAPElement getFirstChild(SOAPElement var0) {
      for(Node var1 = var0.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (1 == var1.getNodeType()) {
            return (SOAPElement)var1;
         }
      }

      return null;
   }

   public static QName qnameFromName(Name var0) {
      return var0 instanceof QName ? (QName)var0 : new QName(var0.getURI(), var0.getLocalName(), var0.getPrefix());
   }

   public static void dump(SOAPElement var0, PrintStream var1) {
      dump(var0, var1, 0);
   }

   private static void dump(SOAPElement var0, PrintStream var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         var1.print(" ");
      }

      var1.print("+");
      var1.println(var0.getElementName());
      ++var2;
      Iterator var5 = var0.getChildElements();

      while(var5.hasNext()) {
         Object var4 = var5.next();
         if (var4 instanceof SOAPElement) {
            dump((SOAPElement)var4, var1, var2);
         }
      }

   }
}
