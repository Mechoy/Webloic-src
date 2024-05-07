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
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;
import weblogic.wsee.wsdl.builder.WsdlBindingOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;

public final class WsdlBindingImpl extends WsdlExtensibleImpl implements WsdlBindingBuilder {
   private QName name;
   private QName type;
   private Map<QName, WsdlBindingOperationBuilder> operationList;
   private PolicyURIs policyUris;
   private WsdlDefinitionsBuilder definitions;
   private WsdlPortTypeBuilder portType;
   private String bindingType;
   private String transportProtocol;
   private String transportURI;

   WsdlBindingImpl(WsdlDefinitionsBuilder var1) {
      this.operationList = new LinkedHashMap();
      this.bindingType = "unknown";
      this.transportProtocol = "unknown";
      this.transportURI = null;
      this.definitions = var1;
   }

   WsdlBindingImpl(QName var1, QName var2, WsdlDefinitionsBuilder var3) {
      this(var3);
      this.name = var1;
      this.setPortType(var2);
   }

   private void setPortType(QName var1) {
      this.type = var1;
   }

   public String getBindingType() {
      return this.bindingType;
   }

   public void setBindingType(String var1) {
      this.bindingType = var1;
   }

   public String getTransportProtocol() {
      return this.transportProtocol;
   }

   public void setTransportProtocol(String var1) {
      this.transportProtocol = var1;
   }

   public void setTransportURI(String var1) {
      this.transportURI = var1;
   }

   public String getTransportURI() {
      return this.transportURI;
   }

   public QName getName() {
      return this.name;
   }

   public WsdlPortTypeBuilder getPortType() {
      if (this.portType == null) {
         this.portType = (WsdlPortTypeBuilder)this.definitions.getPortTypes().get(this.type);
      }

      return this.portType;
   }

   public PolicyURIs getPolicyUris() {
      return this.policyUris;
   }

   public void setPolicyUris(PolicyURIs var1) {
      this.policyUris = var1;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.name);
      var1.writeField("type", this.type);
      var1.writeArray("operationList", this.operationList.keySet().iterator());
      var1.writeArray("extensions", this.getExtensionKeys().iterator());
      var1.end();
   }

   public WsdlBindingOperationBuilder addOperation(WsdlOperationBuilder var1) {
      WsdlBindingOperationImpl var2 = new WsdlBindingOperationImpl(var1.getName(), this);
      this.operationList.put(var1.getName(), var2);
      return var2;
   }

   public WsdlBindingOperationBuilder getOperation(QName var1) {
      return (WsdlBindingOperationBuilder)this.operationList.get(var1);
   }

   protected void parseAttributes(Element var1, String var2) throws WsdlException {
      String var3 = WsdlReader.getMustAttribute(var1, (String)null, "name");
      this.name = new QName(var2, var3);
      String var4 = WsdlReader.getMustAttribute(var1, (String)null, "type");
      this.setPortType(WsdlReader.createQName(var1, var4));
      PolicyURIs var5 = this.getPolicyUri(var1);
      if (null != var5) {
         this.policyUris = var5;
      }

   }

   protected WsdlExtension parseChild(Element var1, String var2) throws WsdlException {
      if ("operation".equals(var1.getLocalName())) {
         this.parseOperation(var1, this.type.getNamespaceURI());
         return null;
      } else {
         WsdlExtensionParser var3 = WsdlExtensionRegistry.getParser();
         return var3.parseBinding(this, var1);
      }
   }

   private void parseOperation(Element var1, String var2) throws WsdlException {
      WsdlBindingOperationImpl var3 = new WsdlBindingOperationImpl(this);
      var3.parse(var1, var2);
      this.operationList.put(var3.getName(), var3);
   }

   public void write(Element var1, WsdlWriter var2) {
      if (var2.isSameNS(this.getName().getNamespaceURI())) {
         Element var3 = var2.addChild(var1, "binding", WsdlConstants.wsdlNS);
         var2.setAttribute(var3, "name", WsdlConstants.wsdlNS, this.name.getLocalPart());
         var2.setAttribute(var3, "type", WsdlConstants.wsdlNS, this.type);
         if (this.policyUris != null) {
            this.policyUris.write(var3, var2);
         }

         this.writeExtensions(var3, var2);
         Iterator var4 = this.getOperations().values().iterator();

         while(var4.hasNext()) {
            WsdlBindingOperationBuilder var5 = (WsdlBindingOperationBuilder)var4.next();
            var5.write(var3, var2);
         }

      }
   }

   public Map<QName, WsdlBindingOperationBuilder> getOperations() {
      return this.operationList;
   }
}
