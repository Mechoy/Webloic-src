package weblogic.servlet.jsp.dd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.descriptors.webapp.AttributeMBean;
import weblogic.management.descriptors.webapp.TagMBean;
import weblogic.management.descriptors.webapp.VariableMBean;
import weblogic.servlet.internal.dd.BaseServletDescriptor;
import weblogic.servlet.internal.dd.ToXML;
import weblogic.utils.io.XMLWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class TagDescriptor extends BaseServletDescriptor implements TagMBean, ToXML {
   private String name;
   private String classname;
   private String extraInfoClassname;
   private String bodyContent;
   private String description;
   AttributeMBean[] atts;
   VariableMBean[] vars;
   private boolean _12;

   private String getTEIDTDName() {
      return this.is12() ? "tei-class" : "teiclass";
   }

   private String getTagClassDTDName() {
      return this.is12() ? "tag-class" : "tagclass";
   }

   private String getBodyContentDTDName() {
      return this.is12() ? "body-content" : "bodycontent";
   }

   private String getDescriptionDTDName() {
      return this.is12() ? "description" : "info";
   }

   public TagDescriptor() {
      this._12 = true;
   }

   public TagDescriptor(Element var1, boolean var2) throws DOMProcessingException {
      this._12 = var2;
      Element var3 = DOMUtils.getElementByTagName(var1, "name");
      this.name = DOMUtils.getTextData(var3);
      var3 = DOMUtils.getElementByTagName(var1, this.getTagClassDTDName());
      this.classname = DOMUtils.getTextData(var3);
      var3 = DOMUtils.getOptionalElementByTagName(var1, this.getTEIDTDName());
      if (var3 != null) {
         this.extraInfoClassname = DOMUtils.getTextData(var3);
      }

      var3 = DOMUtils.getOptionalElementByTagName(var1, this.getBodyContentDTDName());
      if (var3 != null) {
         this.bodyContent = DOMUtils.getTextData(var3);
      }

      var3 = DOMUtils.getOptionalElementByTagName(var1, this.getDescriptionDTDName());
      if (var3 != null) {
         this.description = DOMUtils.getTextData(var3);
      }

      List var4 = DOMUtils.getOptionalElementsByTagName(var1, "variable");
      ArrayList var5 = new ArrayList();
      Iterator var6 = var4.iterator();

      while(var6.hasNext()) {
         var5.add(new VariableDescriptor((Element)var6.next()));
      }

      this.vars = new VariableMBean[var5.size()];
      var5.toArray(this.vars);
      var4 = DOMUtils.getOptionalElementsByTagName(var1, "attribute");
      var5.clear();
      var6 = var4.iterator();

      while(var6.hasNext()) {
         var5.add(new AttributeDescriptor((Element)var6.next(), this.is12()));
      }

      this.atts = new AttributeMBean[var5.size()];
      var5.toArray(this.atts);
   }

   public boolean is12() {
      return this._12;
   }

   public void set12(boolean var1) {
      this._12 = var1;
      AttributeMBean[] var2 = this.getAtts();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         if (var2[var3] instanceof AttributeDescriptor) {
            AttributeDescriptor var4 = (AttributeDescriptor)var2[var3];
            var4.set12(this._12);
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      String var2 = this.name;
      this.name = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("name", var2, var1);
      }

   }

   public String getClassname() {
      return this.classname;
   }

   public void setClassname(String var1) {
      String var2 = this.classname;
      this.classname = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("classname", var2, var1);
      }

   }

   public String getExtraInfoClassname() {
      return this.extraInfoClassname;
   }

   public void setExtraInfoClassname(String var1) {
      String var2 = this.extraInfoClassname;
      this.extraInfoClassname = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("extraInfoClassname", var2, var1);
      }

   }

   public String getBodyContent() {
      return this.bodyContent;
   }

   public void setBodyContent(String var1) {
      String var2 = this.bodyContent;
      this.bodyContent = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("bodyContent", var2, var1);
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

   public VariableMBean[] getVars() {
      if (this.vars == null) {
         this.vars = new VariableMBean[0];
      }

      return (VariableMBean[])((VariableMBean[])this.vars.clone());
   }

   public void setVars(VariableMBean[] var1) {
      VariableMBean[] var2 = this.vars;
      if (var1 != null) {
         this.vars = (VariableMBean[])((VariableMBean[])var1.clone());
         if (!comp(var2, var1)) {
            this.firePropertyChange("vars", var2, var1);
         }

      }
   }

   public AttributeMBean[] getAtts() {
      if (this.atts == null) {
         this.atts = new AttributeMBean[0];
      }

      return (AttributeMBean[])((AttributeMBean[])this.atts.clone());
   }

   public void setAtts(AttributeMBean[] var1) {
      AttributeMBean[] var2 = this.atts;
      if (var1 != null) {
         this.atts = (AttributeMBean[])((AttributeMBean[])var1.clone());
         if (!comp(var2, var1)) {
            this.firePropertyChange("atts", var2, var1);
         }

      }
   }

   public String toString() {
      return "[TagDesc: name=" + this.getName() + " classname=" + this.getClassname() + " TEI=" + this.getExtraInfoClassname() + " bc=" + this.getBodyContent() + " desc=" + this.getDescription() + " #vars=" + this.getVars().length + " #atts=" + this.getAtts().length + "]";
   }

   public void validate() {
      throw new Error("NYI");
   }

   public void toXML(XMLWriter var1) {
      this.set12(this.is12());
      var1.println("<tag>");
      var1.incrIndent();
      var1.println("<name>" + this.getName() + "</name>");
      var1.println("<" + this.getTagClassDTDName() + ">" + this.getClassname() + "</" + this.getTagClassDTDName() + ">");
      if (this.getExtraInfoClassname() != null) {
         var1.println("<" + this.getTEIDTDName() + ">" + this.getExtraInfoClassname() + "</" + this.getTEIDTDName() + ">");
      }

      if (this.getBodyContent() != null) {
         var1.println("<" + this.getBodyContentDTDName() + ">" + this.getBodyContent() + "</" + this.getBodyContentDTDName() + ">");
      }

      if (this.getDescription() != null) {
         var1.println("<" + this.getDescriptionDTDName() + ">" + cdata(this.getDescription()) + "</" + this.getDescriptionDTDName() + ">");
      }

      int var3;
      if (this.is12()) {
         VariableMBean[] var2 = this.getVars();

         for(var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            VariableDescriptor var4 = (VariableDescriptor)var2[var3];
            var4.toXML(var1);
         }
      }

      AttributeMBean[] var5 = this.getAtts();

      for(var3 = 0; var5 != null && var3 < var5.length; ++var3) {
         AttributeDescriptor var6 = (AttributeDescriptor)var5[var3];
         var6.toXML(var1);
      }

      var1.decrIndent();
      var1.println("</tag>");
   }

   public String toXML(int var1) {
      return TLDDescriptor.toXML(this, var1);
   }
}
