package weblogic.wsee.wsdl.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.util.NameValueList;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlExtensionParser;
import weblogic.wsee.wsdl.WsdlExtensionRegistry;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;
import weblogic.wsee.wsdl.builder.WsdlBindingMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlBindingOperationBuilder;

public final class WsdlBindingOperationImpl extends WsdlExtensibleImpl implements WsdlBindingOperationBuilder {
   private QName name;
   private WsdlBindingMessageImpl input;
   private WsdlBindingMessageImpl output;
   private PolicyURIs policyUris;
   private NameValueList faultList;
   private WsdlBindingBuilder binding;

   WsdlBindingOperationImpl(WsdlBindingBuilder var1) {
      this.faultList = new NameValueList();
      this.binding = var1;
   }

   WsdlBindingOperationImpl(QName var1, WsdlBindingBuilder var2) {
      this(var2);
      this.name = var1;
   }

   public WsdlBindingBuilder getBinding() {
      return this.binding;
   }

   public QName getName() {
      return this.name;
   }

   public WsdlBindingMessageBuilder getInput() {
      return this.input;
   }

   public WsdlBindingMessageBuilder createInput() {
      assert this.input == null;

      this.input = new WsdlBindingMessageImpl(this, 0);
      return this.input;
   }

   public WsdlBindingMessageBuilder getOutput() {
      return this.output;
   }

   public WsdlBindingMessageBuilder createOutput() {
      assert this.output == null;

      this.output = new WsdlBindingMessageImpl(this, 1);
      return this.output;
   }

   public void flipCallbackInputAndOutput() {
      WsdlBindingMessageImpl var1 = this.input;
      this.input = this.output;
      this.output = var1;
      if (this.input != null) {
         this.input.flipTypeForCallback();
      }

      if (this.output != null) {
         this.output.flipTypeForCallback();
      }

   }

   public Map<String, WsdlBindingMessageBuilder> getFaults() {
      HashMap var1 = new HashMap(this.faultList.size());
      int var2 = 0;

      for(Iterator var3 = this.faultList.values(); var3.hasNext(); ++var2) {
         WsdlBindingMessageBuilder var4 = (WsdlBindingMessageBuilder)var3.next();
         var1.put(var4.getName(), var4);
      }

      return var1;
   }

   public WsdlBindingMessageBuilder getFault(String var1) {
      Iterator var2 = this.faultList.values();

      WsdlBindingMessageBuilder var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WsdlBindingMessageBuilder)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public WsdlBindingMessageBuilder createFault(String var1) {
      WsdlBindingMessageImpl var2 = new WsdlBindingMessageImpl(this, 2);
      var2.setName(var1);
      this.faultList.put(var1, var2);
      return var2;
   }

   public PolicyURIs getPolicyUris() {
      return this.policyUris;
   }

   public void setPolicyUris(PolicyURIs var1) {
      this.policyUris = var1;
   }

   protected WsdlExtension parseChild(Element var1, String var2) throws WsdlException {
      if (WsdlReader.tagEquals(var1, "input", WsdlConstants.wsdlNS)) {
         this.input = new WsdlBindingMessageImpl(this, 0);
         this.input.parse(var1, var2);
      } else if (WsdlReader.tagEquals(var1, "output", WsdlConstants.wsdlNS)) {
         this.output = new WsdlBindingMessageImpl(this, 1);
         this.output.parse(var1, var2);
      } else {
         if (!WsdlReader.tagEquals(var1, "fault", WsdlConstants.wsdlNS)) {
            WsdlExtensionParser var5 = WsdlExtensionRegistry.getParser();
            WsdlExtension var4 = var5.parseBindingOperation(this, var1);
            return var4;
         }

         WsdlBindingMessageImpl var3 = new WsdlBindingMessageImpl(this, 2);
         var3.parse(var1, var2);
         this.faultList.put(var3.getName(), var3);
      }

      return null;
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
      Element var3 = var2.addChild(var1, "operation", WsdlConstants.wsdlNS);
      var2.setAttribute(var3, "name", WsdlConstants.wsdlNS, this.name.getLocalPart());
      if (this.policyUris != null) {
         this.policyUris.write(var3, var2);
      }

      this.writeExtensions(var3, var2);
      if (this.input != null) {
         this.input.write(var3, var2);
      }

      if (this.output != null) {
         this.output.write(var3, var2);
      }

      Iterator var4 = this.faultList.values();

      while(var4.hasNext()) {
         WsdlBindingMessageBuilder var5 = (WsdlBindingMessageBuilder)var4.next();
         var5.write(var3, var2);
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
      var1.writeField("input", this.input);
      var1.writeField("output", this.output);
      var1.writeField("faultList", this.faultList);
      var1.writeField("extensionList", this.getExtensions());
      var1.end();
   }
}
