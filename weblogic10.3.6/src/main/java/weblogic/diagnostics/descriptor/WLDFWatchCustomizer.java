package weblogic.diagnostics.descriptor;

import weblogic.descriptor.DescriptorBean;

public class WLDFWatchCustomizer {
   private WLDFWatchBean watchBean;
   String severity = null;

   public WLDFWatchCustomizer(WLDFWatchBean var1) {
      this.watchBean = var1;
   }

   public String getSeverity() {
      if (((DescriptorBean)this.watchBean).isSet("Severity")) {
         return this.severity;
      } else {
         WLDFWatchNotificationBean var1 = (WLDFWatchNotificationBean)((DescriptorBean)this.watchBean).getParentBean();
         return var1 == null ? "Notice" : var1.getSeverity();
      }
   }

   public void setSeverity(String var1) {
      this.severity = var1;
   }
}
