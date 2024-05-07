package weblogic.wsee.wsdl.internal;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.deploy.WsdlAddressInfo;
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
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;
import weblogic.wsee.wsdl.builder.WsdlServiceBuilder;

public final class WsdlPortImpl extends WsdlExtensibleImpl implements WsdlPortBuilder {
   private QName name;
   private QName binding;
   private WsdlBindingBuilder wsdlBinding;
   private PolicyURIs policyUris;
   private final WsdlDefinitionsBuilder definitions;
   private final WsdlServiceBuilder service;
   private String transport;
   private WsdlAddressInfo.PortAddress portAddress;

   WsdlPortImpl(WsdlServiceBuilder var1, WsdlDefinitionsBuilder var2) {
      this.service = var1;
      this.definitions = var2;
   }

   WsdlPortImpl(QName var1, WsdlServiceBuilder var2, WsdlDefinitionsBuilder var3) {
      this(var2, var3);
      this.name = var1;
   }

   public QName getName() {
      return this.name;
   }

   public WsdlServiceBuilder getService() {
      return this.service;
   }

   public WsdlBindingBuilder getBinding() {
      if (this.wsdlBinding == null) {
         this.wsdlBinding = (WsdlBindingBuilder)this.definitions.getBindings().get(this.binding);
      }

      return this.wsdlBinding;
   }

   public WsdlPortTypeBuilder getPortType() {
      return this.getBinding().getPortType();
   }

   public String getTransport() {
      if (this.transport == null) {
         this.transport = this.getBinding().getTransportProtocol();
      }

      return this.transport;
   }

   public void setTransport(String var1) {
      this.transport = var1;
   }

   public PolicyURIs getPolicyUris() {
      return this.policyUris;
   }

   public void setPolicyUris(PolicyURIs var1) {
      this.policyUris = var1;
   }

   public WsdlDefinitionsBuilder getDefinitions() {
      return this.definitions;
   }

   public void setBinding(QName var1) {
      this.binding = var1;
   }

   protected WsdlExtension parseChild(Element var1, String var2) throws WsdlException {
      WsdlExtensionParser var3 = WsdlExtensionRegistry.getParser();
      return var3.parsePort(this, var1);
   }

   protected void parseAttributes(Element var1, String var2) throws WsdlException {
      String var3 = WsdlReader.getMustAttribute(var1, (String)null, "name");
      this.name = new QName(var2, var3);
      String var4 = WsdlReader.getMustAttribute(var1, (String)null, "binding");
      this.binding = WsdlReader.createQName(var1, var4);
      PolicyURIs var5 = this.getPolicyUri(var1);
      if (null != var5) {
         this.policyUris = var5;
      }

   }

   public void setPortAddress(WsdlAddressInfo.PortAddress var1) {
      this.portAddress = var1;
   }

   public WsdlAddressInfo.PortAddress getPortAddress() {
      return this.portAddress;
   }

   public void write(Element var1, WsdlWriter var2) {
      var2.setCurrentWsdlPort(this);
      Element var3 = var2.addChild(var1, "port", WsdlConstants.wsdlNS);
      var2.setAttribute(var3, "name", WsdlConstants.wsdlNS, this.name.getLocalPart());
      var2.setAttribute(var3, "binding", WsdlConstants.wsdlNS, this.binding);
      if (this.policyUris != null) {
         this.policyUris.write(var3, var2);
      }

      this.writeExtensions(var3, var2);
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.name);
      var1.writeField("binding", this.binding);
      if (this.policyUris != null) {
         var1.writeField("policyUris", this.policyUris.toString());
      }

      var1.writeField("extensions", this.getExtensions());
      var1.end();
   }
}
