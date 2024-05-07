package weblogic.servlet.jsp.dd;

import org.w3c.dom.Element;
import weblogic.management.descriptors.webapp.VariableMBean;
import weblogic.servlet.internal.dd.BaseServletDescriptor;
import weblogic.servlet.internal.dd.ToXML;
import weblogic.utils.io.XMLWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class VariableDescriptor extends BaseServletDescriptor implements VariableMBean, ToXML {
   private String name;
   private String type;
   private String description;
   boolean nameFromAttribute;
   int scope;
   boolean declare = true;

   public VariableDescriptor() {
   }

   public VariableDescriptor(Element var1) throws DOMProcessingException {
      Element var2 = null;
      var2 = DOMUtils.getOptionalElementByTagName(var1, "name-given");
      if (var2 != null) {
         this.name = DOMUtils.getTextData(var2);
         this.nameFromAttribute = false;
      } else {
         var2 = DOMUtils.getOptionalElementByTagName(var1, "name-from-attribute");
         this.name = DOMUtils.getTextData(var2);
         this.nameFromAttribute = true;
      }

      this.type = "java.lang.String";
      var2 = DOMUtils.getOptionalElementByTagName(var1, "variable-class");
      if (var2 != null) {
         this.type = DOMUtils.getTextData(var2);
      }

      this.declare = true;
      var2 = DOMUtils.getOptionalElementByTagName(var1, "declare");
      String var3;
      if (var2 != null) {
         var3 = DOMUtils.getTextData(var2);
         this.declare = "true".equalsIgnoreCase(var3) || "yes".equalsIgnoreCase(var3);
      }

      this.scope = 0;
      var2 = DOMUtils.getOptionalElementByTagName(var1, "scope");
      if (var2 != null) {
         var3 = DOMUtils.getTextData(var2);
         if ("AT_BEGIN".equalsIgnoreCase(var3)) {
            this.scope = 1;
         } else if ("AT_END".equalsIgnoreCase(var3)) {
            this.scope = 2;
         }
      }

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
      if (!comp(var2, var1)) {
         this.firePropertyChange("type", var2, var1);
      }

   }

   public boolean getNameFromAttribute() {
      return this.nameFromAttribute;
   }

   public void setNameFromAttribute(boolean var1) {
      if (this.nameFromAttribute != var1) {
         this.nameFromAttribute = var1;
         this.firePropertyChange("nameFromAttribute", new Boolean(!var1), new Boolean(var1));
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

   public boolean getDeclare() {
      return this.declare;
   }

   public void setDeclare(boolean var1) {
      if (this.declare != var1) {
         this.declare = var1;
         this.firePropertyChange("declare", new Boolean(!var1), new Boolean(var1));
      }

   }

   public int getScope() {
      return this.scope;
   }

   public void setScope(int var1) {
      int var2 = this.scope;
      this.scope = var1;
      if (this.scope != var1) {
         this.firePropertyChange("scope", new Integer(var2), new Integer(var1));
      }

   }

   public void setScopeStr(String var1) {
      String var2 = this.getScopeStr();
      if (var1 != null) {
         if (var1.equalsIgnoreCase("AT_BEGIN")) {
            this.scope = 1;
         } else if (var1.equalsIgnoreCase("AT_END")) {
            this.scope = 2;
         } else if (var1.equalsIgnoreCase("NESTED")) {
            this.scope = 0;
         }

         if (!comp(var2, this.getScopeStr())) {
            this.firePropertyChange("scopeStr", var2, this.getScopeStr());
         }

      }
   }

   public String getScopeStr() {
      if (this.scope == 1) {
         return "AT_BEGIN";
      } else {
         return this.scope == 2 ? "AT_END" : "NESTED";
      }
   }

   public void validate() {
      throw new Error("NYI");
   }

   public void toXML(XMLWriter var1) {
      var1.println("<variable>");
      var1.incrIndent();
      if (this.getNameFromAttribute()) {
         var1.println("<name-from-attribute>" + this.getName() + "</name-from-attribute>");
      } else {
         var1.println("<name-given>" + this.getName() + "</name-given>");
      }

      var1.println("<variable-class>" + this.getType() + "</variable-class>");
      var1.println("<declare>" + this.getDeclare() + "</declare>");
      var1.println("<scope>" + this.getScopeStr() + "</scope>");
      if (this.description != null && (this.description = this.description.trim()).length() > 0) {
         var1.println("<description>" + cdata(this.description) + "</description>");
      }

      var1.decrIndent();
      var1.println("</variable>");
   }

   public String toXML(int var1) {
      return TLDDescriptor.toXML(this, var1);
   }
}
