package weblogic.wsee.wsdl.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtensible;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;

public abstract class WsdlExtensibleImpl extends WsdlBase implements WsdlExtensible {
   private Map<String, List<WsdlExtension>> extensions = new HashMap();

   public WsdlExtension getExtension(String var1) {
      List var2 = (List)this.extensions.get(var1);
      return var2 != null ? (WsdlExtension)var2.iterator().next() : null;
   }

   public List<WsdlExtension> getExtensionList(String var1) {
      List var2 = (List)this.extensions.get(var1);
      return var2 != null ? var2 : Collections.EMPTY_LIST;
   }

   public void putExtension(WsdlExtension var1) {
      if (var1 != null) {
         Object var2 = (List)this.extensions.get(var1.getKey());
         if (var2 == null) {
            var2 = new ArrayList();
            this.extensions.put(var1.getKey(), var2);
         }

         if (!((List)var2).contains(var1)) {
            ((List)var2).add(var1);
         }
      }

   }

   public Map<String, List<WsdlExtension>> getExtensions() {
      return this.extensions;
   }

   public Set<String> getExtensionKeys() {
      return this.extensions.keySet();
   }

   public void parse(Element var1, String var2) throws WsdlException {
      this.addDocumentation(var1);
      this.parseAttributes(var1, var2);
      NodeList var3 = var1.getChildNodes();

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         Node var5 = var3.item(var4);
         if (!WsdlReader.isWhiteSpace(var5) && !WsdlReader.isDocumentation(var5)) {
            WsdlReader.checkDomElement(var5);
            WsdlExtension var6 = this.parseChild((Element)var5, var2);
            this.putExtension(var6);
         }
      }

   }

   protected void writeExtensions(Element var1, WsdlWriter var2) {
      this.writeDocumentation(var1, var2);
      Iterator var3 = this.extensions.values().iterator();

      while(var3.hasNext()) {
         List var4 = (List)var3.next();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            WsdlExtension var6 = (WsdlExtension)var5.next();
            var6.write(var1, var2);
         }
      }

   }

   protected abstract WsdlExtension parseChild(Element var1, String var2) throws WsdlException;

   protected abstract void parseAttributes(Element var1, String var2) throws WsdlException;
}
