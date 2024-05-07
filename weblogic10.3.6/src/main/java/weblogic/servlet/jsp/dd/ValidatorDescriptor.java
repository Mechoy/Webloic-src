package weblogic.servlet.jsp.dd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.descriptors.webapp.ParameterMBean;
import weblogic.management.descriptors.webapp.ValidatorMBean;
import weblogic.servlet.internal.dd.BaseServletDescriptor;
import weblogic.servlet.internal.dd.ParameterDescriptor;
import weblogic.servlet.internal.dd.ToXML;
import weblogic.utils.io.XMLWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class ValidatorDescriptor extends BaseServletDescriptor implements ValidatorMBean, ToXML {
   private String classname;
   private ParameterMBean[] params;

   public ValidatorDescriptor() {
   }

   public ValidatorDescriptor(Element var1) throws DOMProcessingException {
      Element var2 = DOMUtils.getElementByTagName(var1, "validator-class");
      this.classname = DOMUtils.getTextData(var2);
      List var3 = DOMUtils.getOptionalElementsByTagName(var1, "init-param");
      ArrayList var4 = new ArrayList();
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         var4.add(new ParameterDescriptor((Element)var5.next()));
      }

      this.params = new ParameterMBean[var4.size()];
      var4.toArray(this.params);
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

   public ParameterMBean[] getParams() {
      if (this.params == null) {
         this.params = new ParameterMBean[0];
      }

      return (ParameterMBean[])((ParameterMBean[])this.params.clone());
   }

   public void setParams(ParameterMBean[] var1) {
      ParameterMBean[] var2 = this.params;
      if (var1 != null) {
         this.params = (ParameterMBean[])((ParameterMBean[])var1.clone());
         if (!comp(var2, var1)) {
            this.firePropertyChange("params", var2, var1);
         }

      }
   }

   public void validate() {
      throw new Error("NYI");
   }

   public void toXML(XMLWriter var1) {
      if (this.getClassname() != null && this.getClassname().trim().length() != 0) {
         var1.println("<validator>");
         var1.incrIndent();
         var1.println("<validator-class>" + this.getClassname().trim() + "</validator-class>");
         ParameterMBean[] var2 = this.getParams();

         for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            ParameterDescriptor var4 = (ParameterDescriptor)var2[var3];
            var1.println("<init-param>");
            var1.incrIndent();
            String var5 = var4.getParamName();
            String var6 = var4.getParamValue();
            String var7 = var4.getDescription();
            var1.println("<param-name>" + var5 + "</param-name>");
            var1.println("<param-value>" + var6 + "</param-value>");
            if (var7 != null && (var7 = var7.trim()).length() > 0) {
               var7 = cdata(var7);
               var1.println("<description>" + var7 + "</description>");
            }

            var1.decrIndent();
            var1.println("</init-param>");
         }

         var1.decrIndent();
         var1.println("</validator>");
      }
   }

   public String toXML(int var1) {
      return TLDDescriptor.toXML(this, var1);
   }
}
