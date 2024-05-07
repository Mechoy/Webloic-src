package weblogic.xml.security.specs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.NSOutputStream;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public abstract class SpecBase implements SpecConstants {
   protected SpecBase() {
   }

   public SpecBase(XMLInputStream var1, String var2) throws XMLStreamException {
      this.fromXMLInternal(var1, var2);
   }

   public void toXML(XMLOutputStream var1) throws XMLStreamException {
      this.toXML(var1, "http://www.openuri.org/2002/11/wsse/spec", 0);
   }

   public abstract void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException;

   protected abstract void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException;

   static String fillNamespace(XMLName var0, NSOutputStream var1, ArrayList var2) {
      Map var4 = var1.getNamespaces();
      String var5 = var0.getNamespaceUri();
      String var3 = (String)var4.get(var5);
      if (var3 != null) {
         return var3;
      } else {
         Iterator var6 = var2.iterator();

         Attribute var7;
         do {
            if (!var6.hasNext()) {
               var3 = var0.getPrefix();
               var1.addPrefix(var5, var3);
               var2.add(ElementFactory.createNamespaceAttribute(var3, var5));
               return var3;
            }

            var7 = (Attribute)var6.next();
         } while(!var5.equals(var7.getValue()));

         return var7.getName().getLocalName();
      }
   }
}
