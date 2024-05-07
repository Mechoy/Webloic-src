package weblogic.wsee.wsdl.internal;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlExtensionParser;
import weblogic.wsee.wsdl.WsdlExtensionRegistry;
import weblogic.wsee.wsdl.WsdlFilter;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlPartBuilder;

public final class WsdlMessageImpl extends WsdlExtensibleImpl implements WsdlMessageBuilder {
   private Map<String, WsdlPartBuilder> partList;
   private PolicyURIs policyUris;
   private QName name;

   WsdlMessageImpl() {
      this.partList = new LinkedHashMap();
   }

   WsdlMessageImpl(QName var1) {
      this();
      this.name = var1;
   }

   public QName getName() {
      return this.name;
   }

   public Map<String, WsdlPartBuilder> getParts() {
      return this.partList;
   }

   public WsdlPartBuilder getPart(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Name can not be null");
      } else {
         return (WsdlPartBuilder)this.partList.get(var1);
      }
   }

   public WsdlPartBuilder addPart(String var1) {
      WsdlPartImpl var2 = new WsdlPartImpl(var1);
      this.partList.put(var1, var2);
      return var2;
   }

   public PolicyURIs getPolicyUris() {
      return this.policyUris;
   }

   public void setPolicyUris(PolicyURIs var1) {
      this.policyUris = var1;
   }

   protected void parseAttributes(Element var1, String var2) throws WsdlException {
      String var3 = WsdlReader.getMustAttribute(var1, (String)null, "name");
      this.name = new QName(var2, var3);
      PolicyURIs var4 = this.getPolicyUri(var1);
      if (null != var4) {
         this.policyUris = var4;
      }

   }

   public WsdlExtension parseChild(Element var1, String var2) throws WsdlException {
      if (WsdlReader.tagEquals(var1, "part", WsdlConstants.wsdlNS)) {
         this.parsePart(var1, var2);
         return null;
      } else {
         WsdlExtensionParser var3 = WsdlExtensionRegistry.getParser();
         return var3.parseMessage(this, var1);
      }
   }

   private void parsePart(Element var1, String var2) throws WsdlException {
      WsdlPartImpl var3 = new WsdlPartImpl();
      var3.parse(var1, var2);
      this.partList.put(var3.getName(), var3);
   }

   public void write(Element var1, WsdlWriter var2) {
      if (var2.isSameNS(this.getName().getNamespaceURI())) {
         Element var3 = var2.addChild(var1, "message", WsdlConstants.wsdlNS);
         var2.setAttribute(var3, "name", WsdlConstants.wsdlNS, this.name.getLocalPart());
         if (this.policyUris != null) {
            this.policyUris.write(var3, var2);
         }

         WsdlFilter var4 = var2.getWsdlAddressInfo() == null ? null : var2.getWsdlAddressInfo().getWsdlFilter();
         this.writeExtensions(var3, var2);
         Iterator var5 = this.getParts().values().iterator();

         while(true) {
            WsdlPartBuilder var6;
            do {
               if (!var5.hasNext()) {
                  return;
               }

               var6 = (WsdlPartBuilder)var5.next();
            } while(var4 != null && !var4.isMessagePartSupported(this.getName(), var6.getName()));

            var6.write(var3, var2);
         }
      }
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.name);
      var1.writeArray("partList", this.partList.values().iterator());
      var1.end();
   }
}
