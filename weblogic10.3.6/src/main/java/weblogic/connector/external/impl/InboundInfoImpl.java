package weblogic.connector.external.impl;

import java.util.List;
import java.util.Vector;
import weblogic.connector.external.ActivationSpecInfo;
import weblogic.connector.external.InboundInfo;
import weblogic.connector.external.RAInfo;
import weblogic.j2ee.descriptor.MessageListenerBean;
import weblogic.j2ee.descriptor.RequiredConfigPropertyBean;
import weblogic.j2ee.descriptor.wl.ResourceAdapterSecurityBean;

public class InboundInfoImpl implements InboundInfo {
   private RAInfo raInfo;
   private MessageListenerBean msgListenerBean;
   private ResourceAdapterSecurityBean raSecurityBean;

   public InboundInfoImpl(RAInfo var1, MessageListenerBean var2) {
      this.raInfo = var1;
      this.msgListenerBean = var2;
      this.raSecurityBean = this.raSecurityBean;
   }

   public String getRADescription() {
      return this.raInfo.getRADescription();
   }

   public String getDisplayName() {
      return this.raInfo.getDisplayName();
   }

   public String getVendorName() {
      return this.raInfo.getVendorName();
   }

   public String getEisType() {
      return this.raInfo.getEisType();
   }

   public List getActivationSpecProps() {
      RequiredConfigPropertyBean[] var1 = this.msgListenerBean.getActivationSpec().getRequiredConfigProperties();
      if (var1 == null) {
         return null;
      } else {
         Vector var2 = new Vector();

         for(int var4 = 0; var4 < var1.length; ++var4) {
            RequiredConfigPropInfoImpl var3 = new RequiredConfigPropInfoImpl(var1[var4]);
            var2.add(var3);
         }

         return var2;
      }
   }

   public String getMsgType() {
      return this.msgListenerBean.getMessageListenerType();
   }

   public ActivationSpecInfo getActivationSpec() {
      return new ActivationSpecInfoImpl(this.msgListenerBean.getActivationSpec());
   }
}
