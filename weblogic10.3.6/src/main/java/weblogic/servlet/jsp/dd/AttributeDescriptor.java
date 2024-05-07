package weblogic.servlet.jsp.dd;

import org.w3c.dom.Element;
import weblogic.management.descriptors.webapp.AttributeMBean;
import weblogic.servlet.internal.dd.BaseServletDescriptor;
import weblogic.servlet.internal.dd.ToXML;
import weblogic.utils.io.XMLWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class AttributeDescriptor extends BaseServletDescriptor implements AttributeMBean, ToXML {
   private String name;
   private String type;
   private String description;
   private boolean required;
   private boolean rtexpr;
   private boolean _12;

   public AttributeDescriptor() {
      this._12 = true;
   }

   public AttributeDescriptor(Element var1, boolean var2) throws DOMProcessingException {
      this._12 = var2;
      String var3 = null;
      Element var4 = DOMUtils.getElementByTagName(var1, "name");
      this.name = DOMUtils.getTextData(var4);
      var4 = DOMUtils.getOptionalElementByTagName(var1, "required");
      if (var4 != null) {
         var3 = DOMUtils.getTextData(var4);
         this.required = "true".equalsIgnoreCase(var3) || "yes".equalsIgnoreCase(var3);
      }

      var4 = DOMUtils.getOptionalElementByTagName(var1, "rtexprvalue");
      if (var4 != null) {
         var3 = DOMUtils.getTextData(var4);
         this.rtexpr = "true".equalsIgnoreCase(var3) || "yes".equalsIgnoreCase(var3);
      } else {
         this.rtexpr = false;
      }

      if (var2) {
         var4 = DOMUtils.getOptionalElementByTagName(var1, "type");
         if (var4 != null) {
            this.type = DOMUtils.getTextData(var4);
         }
      }

   }

   public boolean is12() {
      return this._12;
   }

   public void set12(boolean var1) {
      this._12 = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      String var2 = this.name;
      this.name = var1;
      if (!comp(var2, this.name)) {
         this.firePropertyChange("name", var2, this.name);
      }

   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      String var2 = this.type;
      this.type = var1;
      if (!comp(var2, this.type)) {
         this.firePropertyChange("type", var2, this.type);
      }

   }

   public boolean isRequired() {
      return this.required;
   }

   public void setRequired(boolean var1) {
      if (var1 != this.required) {
         this.required = var1;
         this.firePropertyChange("required", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isRtexpr() {
      return this.rtexpr;
   }

   public void setRtexpr(boolean var1) {
      if (var1 != this.rtexpr) {
         this.rtexpr = var1;
         this.firePropertyChange("rtexpr", new Boolean(!var1), new Boolean(var1));
      }

   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      String var2 = this.description;
      this.description = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("description", var2, var1);
      }

   }

   public void validate() {
   }

   public void toXML(XMLWriter var1) {
      var1.println("<attribute>");
      var1.incrIndent();
      var1.println("<name>" + this.getName() + "</name>");
      var1.println("<required>" + this.isRequired() + "</required>");
      var1.println("<rtexprvalue>" + this.isRtexpr() + "</rtexprvalue>");
      if (this._12 && this.type != null && (this.type = this.type.trim()).length() > 0) {
         var1.println("<type>" + this.getType() + "</type>");
      }

      if (this._12 && this.description != null && (this.description = this.description.trim()).length() > 0) {
         var1.println("<description>" + cdata(this.description) + "</description>");
      }

      var1.decrIndent();
      var1.println("</attribute>");
   }

   public String toXML(int var1) {
      return TLDDescriptor.toXML(this, var1);
   }
}
