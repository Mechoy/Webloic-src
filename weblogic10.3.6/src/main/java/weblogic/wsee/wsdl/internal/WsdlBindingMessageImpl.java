package weblogic.wsee.wsdl.internal;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlExtensionParser;
import weblogic.wsee.wsdl.WsdlExtensionRegistry;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlBindingMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlBindingOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;

public final class WsdlBindingMessageImpl extends WsdlExtensibleImpl implements WsdlBindingMessageBuilder {
   private String name;
   private WsdlBindingOperationBuilder operation;
   private WsdlMessageBuilder message;
   private PolicyURIs policyUris;
   public static final int INPUT = 0;
   public static final int OUTPUT = 1;
   public static final int FAULT = 2;
   public static final int UNKNOWN = -1;
   private int type = -1;

   WsdlBindingMessageImpl(WsdlBindingOperationBuilder var1, int var2) {
      this.operation = var1;
      this.type = var2;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public WsdlBindingOperationBuilder getBindingOperation() {
      return this.operation;
   }

   public PolicyURIs getPolicyUris() {
      return this.policyUris;
   }

   public void setPolicyUris(PolicyURIs var1) {
      this.policyUris = var1;
   }

   public WsdlMessageBuilder getMessage() throws WsdlException {
      if (this.message != null) {
         return this.message;
      } else {
         QName var1 = this.operation.getName();
         WsdlPortTypeBuilder var2 = this.getBindingOperation().getBinding().getPortType();
         WsdlOperationBuilder var3 = (WsdlOperationBuilder)var2.getOperations().get(var1);
         if (this.type == 0) {
            this.message = var3.getInput();
         } else if (this.type == 1) {
            this.message = var3.getOutput();
         } else {
            if (this.type != 2) {
               throw new WsdlException("Unknown message type");
            }

            if (this.name == null) {
               throw new WsdlException("Name can not be null");
            }

            this.message = (WsdlMessageBuilder)var3.getFaults().get(this.name);
         }

         return this.message;
      }
   }

   public int getType() {
      return this.type;
   }

   void flipTypeForCallback() {
      if (this.type == 0) {
         this.type = 1;
      } else if (this.type == 1) {
         this.type = 0;
      }

   }

   protected WsdlExtension parseChild(Element var1, String var2) throws WsdlException {
      WsdlExtensionParser var3 = WsdlExtensionRegistry.getParser();
      WsdlExtension var4 = var3.parseBindingMessage(this, var1);
      return var4;
   }

   protected void parseAttributes(Element var1, String var2) throws WsdlException {
      this.setName(var1.getAttribute("name"));
      PolicyURIs var3 = this.getPolicyUri(var1);
      if (null != var3) {
         this.policyUris = var3;
      }

   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3;
      if (this.getType() == 0) {
         var3 = var2.addChild(var1, "input", WsdlConstants.wsdlNS);
      } else if (this.getType() == 1) {
         var3 = var2.addChild(var1, "output", WsdlConstants.wsdlNS);
      } else {
         var3 = var2.addChild(var1, "fault", WsdlConstants.wsdlNS);
      }

      if (this.name != null && this.name.length() > 0) {
         var3.setAttribute("name", this.name);
      }

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
      var1.writeField("type", this.type);
      var1.writeField("extensionList", this.getExtensions());
      var1.end();
   }
}
