package weblogic.management.security;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;
import weblogic.management.commo.RequiredModelMBeanWrapper;
import weblogic.management.commo.StandardInterface;

public class BaseMBeanImpl {
   private ModelMBean requiredModelMBean = null;

   public BaseMBeanImpl(ModelMBean var1) {
      this.requiredModelMBean = var1;
   }

   protected BaseMBeanImpl(RequiredModelMBean var1) {
      this.requiredModelMBean = var1;
   }

   protected ModelMBean getRequiredModelMBean() {
      return this.requiredModelMBean;
   }

   protected StandardInterface getProxy() throws MBeanException {
      StandardInterface var1 = null;
      if (this.requiredModelMBean instanceof RequiredModelMBeanWrapper) {
         var1 = ((RequiredModelMBeanWrapper)this.requiredModelMBean).getProxy();
         return var1;
      } else {
         throw new AssertionError("GetProxy on BaseMBeanImpl called with old requiredModelMBean");
      }
   }
}
