package weblogic.servlet.ejb2jsp.dd;

import org.w3c.dom.Element;
import weblogic.servlet.internal.dd.ToXML;
import weblogic.utils.io.XMLWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class MethodParamDescriptor implements ToXML {
   private String type;
   private String name;
   private String defalt;
   private String defaultValue;
   private String defaultMethod;

   static void p(String var0) {
      System.err.println("[MethodParamDesc]: " + var0);
   }

   public MethodParamDescriptor() {
      this.type = this.name = this.defalt = this.defaultValue = this.defaultMethod;
      this.setDefault("NONE");
   }

   public MethodParamDescriptor(Element var1) throws DOMProcessingException {
      this.type = DOMUtils.getValueByTagName(var1, "param-type");
      this.name = DOMUtils.getValueByTagName(var1, "param-name");
      this.defalt = DOMUtils.getValueByTagName(var1, "enable-default");
      this.defaultValue = DOMUtils.getValueByTagName(var1, "default-value").trim();
      this.defaultMethod = DOMUtils.getValueByTagName(var1, "default-method-body").trim();
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getDefault() {
      return this.defalt;
   }

   public void setDefault(String var1) {
      this.defalt = var1;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public void setDefaultValue(String var1) {
      this.defaultValue = var1;
   }

   public String getDefaultMethod() {
      return this.defaultMethod;
   }

   public void setDefaultMethod(String var1) {
      this.defaultMethod = var1;
   }

   public String toString() {
      return "Attribute: " + this.name;
   }

   public void toXML(XMLWriter var1) {
      var1.println("<parameter>");
      var1.incrIndent();
      var1.println("<param-type>" + this.type + "</param-type>");
      var1.println("<param-name>" + this.name + "</param-name>");
      var1.println("<enable-default>" + this.defalt + "</enable-default>");
      if (this.defaultValue != null) {
         var1.println("<default-value><![CDATA[" + this.defaultValue + "]]></default-value>");
      }

      if (this.defaultMethod != null) {
         var1.println("<default-method-body><![CDATA[");
         var1.printNoIndent(this.defaultMethod);
         var1.println("]]></default-method-body>");
      }

      var1.decrIndent();
      var1.println("</parameter>");
   }
}
