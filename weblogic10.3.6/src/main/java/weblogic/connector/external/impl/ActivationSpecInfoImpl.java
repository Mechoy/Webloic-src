package weblogic.connector.external.impl;

import java.util.List;
import java.util.Vector;
import weblogic.connector.external.ActivationSpecInfo;
import weblogic.j2ee.descriptor.ActivationSpecBean;
import weblogic.j2ee.descriptor.RequiredConfigPropertyBean;

public class ActivationSpecInfoImpl implements ActivationSpecInfo {
   private ActivationSpecBean activationSpecBean;

   public ActivationSpecInfoImpl(ActivationSpecBean var1) {
      this.activationSpecBean = var1;
   }

   public String getActivationSpecClass() {
      return this.activationSpecBean.getActivationSpecClass();
   }

   public List getRequiredProps() {
      Vector var1 = new Vector();
      RequiredConfigPropertyBean[] var2 = this.activationSpecBean.getRequiredConfigProperties();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            RequiredConfigPropInfoImpl var4 = new RequiredConfigPropInfoImpl(var2[var3]);
            var1.add(var4);
         }
      }

      return var1;
   }
}
