package weblogic.management.mbeans.custom;

import java.util.ArrayList;
import weblogic.management.configuration.WLDFDataRetirementByAgeMBean;
import weblogic.management.configuration.WLDFDataRetirementMBean;
import weblogic.management.configuration.WLDFServerDiagnosticMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.utils.ArrayUtils;

public final class WLDFServerDiagnostic extends ConfigurationMBeanCustomizer {
   private static final long serialVersionUID = 6775637305805210195L;

   public WLDFServerDiagnostic(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public WLDFDataRetirementMBean[] getWLDFDataRetirements() {
      WLDFServerDiagnosticMBean var1 = (WLDFServerDiagnosticMBean)this.getMbean();
      ArrayList var2 = new ArrayList();
      addAll(var2, var1.getWLDFDataRetirementByAges());
      WLDFDataRetirementMBean[] var3 = new WLDFDataRetirementMBean[var2.size()];
      var3 = (WLDFDataRetirementMBean[])((WLDFDataRetirementMBean[])var2.toArray(var3));
      return var3;
   }

   public WLDFDataRetirementMBean lookupWLDFDataRetirement(String var1) {
      WLDFServerDiagnosticMBean var2 = (WLDFServerDiagnosticMBean)this.getMbean();
      WLDFDataRetirementByAgeMBean var3 = null;
      var3 = var2.lookupWLDFDataRetirementByAge(var1);
      return var3 != null ? var3 : null;
   }

   private static void addAll(ArrayList var0, WLDFDataRetirementMBean[] var1) {
      if (var1 != null && var1.length > 0) {
         ArrayUtils.addAll(var0, var1);
      }

   }
}
