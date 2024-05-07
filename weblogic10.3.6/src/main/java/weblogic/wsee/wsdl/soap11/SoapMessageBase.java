package weblogic.wsee.wsdl.soap11;

import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;

public abstract class SoapMessageBase implements WsdlExtension {
   protected String encodingStyle;
   protected String use;
   protected String namespace;

   public String getEncodingStyle() {
      return this.encodingStyle;
   }

   public void setEncodingStyle(String var1) {
      this.encodingStyle = var1;
   }

   public String getUse() {
      return this.use;
   }

   public void setUse(String var1) {
      this.use = var1;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public void setNamespace(String var1) {
      this.namespace = var1;
   }

   protected void parse(Element var1) throws WsdlException {
      this.use = var1.getAttribute("use");
      if ("".equals(this.use)) {
         this.use = null;
      }

      this.encodingStyle = var1.getAttribute("encodingStyle");
      if ("".equals(this.encodingStyle)) {
         this.encodingStyle = null;
      }

      this.namespace = var1.getAttribute("namespace");
      if ("".equals(this.namespace)) {
         this.namespace = null;
      }

   }
}
