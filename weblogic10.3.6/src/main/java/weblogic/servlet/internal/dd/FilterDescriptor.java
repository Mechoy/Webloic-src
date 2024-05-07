package weblogic.servlet.internal.dd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.FilterMBean;
import weblogic.management.descriptors.webapp.ParameterMBean;
import weblogic.management.descriptors.webapp.UIMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class FilterDescriptor extends BaseServletDescriptor implements ToXML, FilterMBean {
   private static final long serialVersionUID = 1549772288002198411L;
   private static final String FILTER_NAME = "filter-name";
   private static final String FILTER_CLASS = "filter-class";
   private static final String INIT_PARAM = "init-param";
   private String filterName;
   private String filterClass;
   private List initParams;
   private UIMBean uiData;

   public FilterDescriptor() {
      this.uiData = new UIDescriptor();
   }

   public FilterDescriptor(Element var1) throws DOMProcessingException {
      this.filterName = DOMUtils.getValueByTagName(var1, "filter-name");
      this.filterClass = DOMUtils.getOptionalValueByTagName(var1, "filter-class");
      this.uiData = new UIDescriptor(var1);
      List var2 = DOMUtils.getOptionalElementsByTagName(var1, "init-param");
      Iterator var3 = var2.iterator();
      this.initParams = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.initParams.add(new ParameterDescriptor((Element)var3.next()));
      }

   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      String var2 = this.getFilterName();
      if (var2 != null && (var2 = var2.trim()).length() != 0) {
         this.setFilterName(var2);
      } else {
         this.addDescriptorError("NO_FILTER_NAME");
         var1 = false;
      }

      var2 = this.getFilterClass();
      if (var2 != null) {
         var2 = var2.trim();
         this.setFilterClass(var2);
      }

      String var3 = this.getFilterClass();
      if (var3 == null || var3.length() == 0) {
         this.addDescriptorError("NO_FILTER_CLASS", this.getFilterName());
         var1 = false;
      }

      if (!var1) {
         throw new DescriptorValidationException();
      }
   }

   public String toString() {
      return this.getFilterName();
   }

   public String getFilterName() {
      return this.filterName != null ? this.filterName : "";
   }

   public void setFilterName(String var1) {
      String var2 = this.filterName;
      this.filterName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("filterName", var2, var1);
      }

   }

   public UIMBean getUIData() {
      return this.uiData;
   }

   public void setUIData(UIMBean var1) {
      this.uiData = var1;
   }

   public String getFilterClass() {
      return this.filterClass;
   }

   public void setFilterClass(String var1) {
      String var2 = this.filterClass;
      this.filterClass = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("filterClass", var2, var1);
      }

   }

   public ParameterMBean[] getInitParams() {
      if (this.initParams == null) {
         return new ParameterDescriptor[0];
      } else {
         ParameterDescriptor[] var1 = new ParameterDescriptor[this.initParams.size()];
         this.initParams.toArray(var1);
         return (ParameterMBean[])var1;
      }
   }

   public void setInitParams(ParameterMBean[] var1) {
      ParameterMBean[] var2 = this.getInitParams();
      this.initParams = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.initParams.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("initParams", var2, var1);
         }

      }
   }

   public void addInitParam(ParameterMBean var1) {
      if (this.initParams == null) {
         this.initParams = new ArrayList();
      }

      this.initParams.add(var1);
   }

   public void removeInitParam(ParameterMBean var1) {
      if (this.initParams != null) {
         this.initParams.remove(var1);
      }
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<filter>\n";
      var1 += 2;
      if (this.uiData != null) {
         String var3 = this.uiData.getSmallIconFileName();
         String var4 = this.uiData.getLargeIconFileName();
         if (var3 != null || var4 != null) {
            var2 = var2 + this.indentStr(var1) + "<icon>\n";
            if (var3 != null) {
               var2 = var2 + this.indentStr(var1 + 2) + "<small-icon>" + var3 + "</small-icon>\n";
            }

            if (var4 != null) {
               var2 = var2 + this.indentStr(var1 + 2) + "<large-icon>" + var4 + "</large-icon>\n";
            }

            var2 = var2 + this.indentStr(var1) + "</icon>\n";
         }
      }

      var2 = var2 + this.indentStr(var1) + "<filter-name>" + this.filterName + "</filter-name>\n";
      if (this.uiData != null) {
         if (this.uiData.getDisplayName() != null) {
            var2 = var2 + this.indentStr(var1) + "<display-name>" + this.uiData.getDisplayName() + "</display-name>\n";
         }

         if (this.uiData.getDescription() != null) {
            var2 = var2 + this.indentStr(var1) + "<description>" + this.uiData.getDescription() + "</description>\n";
         }
      }

      var2 = var2 + this.indentStr(var1) + "<filter-class>" + this.filterClass + "</filter-class>\n";
      if (this.initParams != null) {
         for(Iterator var6 = this.initParams.iterator(); var6.hasNext(); var2 = var2 + this.indentStr(var1) + "</init-param>\n") {
            ParameterDescriptor var7 = (ParameterDescriptor)var6.next();
            var2 = var2 + this.indentStr(var1) + "<init-param>\n";
            var1 += 2;
            var2 = var2 + this.indentStr(var1) + "<param-name>" + var7.getParamName() + "</param-name>\n";
            var2 = var2 + this.indentStr(var1) + "<param-value>" + var7.getParamValue() + "</param-value>\n";
            String var5 = var7.getDescription();
            if (var5 != null) {
               var2 = var2 + this.indentStr(var1) + "<description>" + var5 + "</description>\n";
            }

            var1 -= 2;
         }
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</filter>\n";
      return var2;
   }
}
