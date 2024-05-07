package weblogic.connector.external.impl;

import weblogic.connector.external.RequiredConfigPropInfo;
import weblogic.j2ee.descriptor.RequiredConfigPropertyBean;

public class RequiredConfigPropInfoImpl implements RequiredConfigPropInfo {
   RequiredConfigPropertyBean configPropBean;

   public RequiredConfigPropInfoImpl(RequiredConfigPropertyBean var1) {
      this.configPropBean = var1;
   }

   public String getDescription() {
      String[] var1 = this.configPropBean.getDescriptions();
      return var1 != null && var1.length > 0 ? var1[0] : null;
   }

   public String[] getDescriptions() {
      return this.configPropBean.getDescriptions();
   }

   public String getName() {
      return this.configPropBean.getConfigPropertyName();
   }
}
