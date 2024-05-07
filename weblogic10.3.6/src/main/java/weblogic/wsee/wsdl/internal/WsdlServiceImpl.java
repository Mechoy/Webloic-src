package weblogic.wsee.wsdl.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlExtensionParser;
import weblogic.wsee.wsdl.WsdlExtensionRegistry;
import weblogic.wsee.wsdl.WsdlFilter;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;
import weblogic.wsee.wsdl.builder.WsdlServiceBuilder;

public final class WsdlServiceImpl extends WsdlExtensibleImpl implements WsdlServiceBuilder {
   private QName name;
   private WsdlDefinitionsBuilder definitions;
   private Map<QName, WsdlPortBuilder> portList;
   private PolicyURIs policyUris;
   private WsdlFilter wsdlFilter;

   WsdlServiceImpl(WsdlDefinitionsBuilder var1) {
      this.portList = new LinkedHashMap();
      this.wsdlFilter = new DefaultWsdlFilter();

      assert var1 != null;

      this.definitions = var1;
   }

   WsdlServiceImpl(WsdlDefinitionsBuilder var1, QName var2) {
      this(var1);
      this.name = var2;
   }

   public QName getName() {
      return this.name;
   }

   public void setName(QName var1) {
      this.name = var1;
   }

   public WsdlDefinitionsBuilder getDefinitions() {
      return this.definitions;
   }

   public List<WsdlPortTypeBuilder> getPortTypes() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.portList.values().iterator();

      while(var2.hasNext()) {
         WsdlPortBuilder var3 = (WsdlPortBuilder)var2.next();
         WsdlBindingBuilder var4 = var3.getBinding();
         var1.add(var4.getPortType());
      }

      return var1;
   }

   public WsdlPortBuilder getPort(QName var1) {
      return (WsdlPortBuilder)this.portList.get(var1);
   }

   public Map<QName, WsdlPortBuilder> getPorts() {
      return this.portList;
   }

   public PolicyURIs getPolicyUris() {
      return this.policyUris;
   }

   public void setPolicyUris(PolicyURIs var1) {
      this.policyUris = var1;
   }

   public WsdlPortBuilder addPort(QName var1, WsdlBinding var2) {
      WsdlPortImpl var3 = new WsdlPortImpl(var1, this, this.definitions);
      var3.setBinding(var2.getName());
      this.portList.put(var1, var3);
      return var3;
   }

   protected WsdlExtension parseChild(Element var1, String var2) throws WsdlException {
      if (WsdlReader.tagEquals(var1, "port", WsdlConstants.wsdlNS)) {
         WsdlPortImpl var4 = new WsdlPortImpl(this, this.definitions);
         var4.parse(var1, var2);
         this.portList.put(var4.getName(), var4);
         return null;
      } else {
         WsdlExtensionParser var3 = WsdlExtensionRegistry.getParser();
         return var3.parseService(this, var1);
      }
   }

   protected void parseAttributes(Element var1, String var2) throws WsdlException {
      String var3 = WsdlReader.getMustAttribute(var1, (String)null, "name");
      this.name = new QName(var2, var3);
      PolicyURIs var4 = this.getPolicyUri(var1);
      if (null != var4) {
         this.policyUris = var4;
      }

   }

   public void write(Element var1, WsdlWriter var2) {
      if (var2.isSameNS(this.getName().getNamespaceURI())) {
         Element var3 = var2.addChild(var1, "service", WsdlConstants.wsdlNS);
         var2.setAttribute(var3, "name", WsdlConstants.wsdlNS, this.name.getLocalPart());
         if (this.policyUris != null) {
            this.policyUris.write(var3, var2);
         }

         this.writeExtensions(var3, var2);
         Iterator var4 = this.getPorts().values().iterator();

         while(var4.hasNext()) {
            WsdlPortBuilder var5 = (WsdlPortBuilder)var4.next();
            if (this.wsdlFilter.isPortSupported(var5.getName())) {
               var5.write(var3, var2);
            }
         }

      }
   }

   public void setWsdlFilter(WsdlFilter var1) {
      this.wsdlFilter = var1;
   }

   public WsdlFilter getWsdlFilter() {
      return this.wsdlFilter;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.name);
      var1.writeField("portList", this.portList);
      var1.end();
   }
}
