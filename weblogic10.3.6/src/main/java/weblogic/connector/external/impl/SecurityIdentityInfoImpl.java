package weblogic.connector.external.impl;

import weblogic.connector.external.SecurityIdentityInfo;
import weblogic.j2ee.descriptor.wl.AnonPrincipalBean;
import weblogic.j2ee.descriptor.wl.AnonPrincipalCallerBean;
import weblogic.j2ee.descriptor.wl.ResourceAdapterSecurityBean;

public class SecurityIdentityInfoImpl implements SecurityIdentityInfo {
   private ResourceAdapterSecurityBean raSecurityBean;

   public SecurityIdentityInfoImpl(ResourceAdapterSecurityBean var1) {
      this.raSecurityBean = var1;
   }

   public boolean useAnonForManageAs() {
      boolean var1 = true;
      AnonPrincipalBean var2 = this.raSecurityBean.getManageAsPrincipalName();
      if (var2 != null) {
         return var2.isUseAnonymousIdentity();
      } else {
         if (this.raSecurityBean.getDefaultPrincipalName() != null) {
            var1 = this.useAnonForDefault();
         }

         return var1;
      }
   }

   public boolean useAnonForRunAs() {
      boolean var1 = false;
      AnonPrincipalCallerBean var2 = this.raSecurityBean.getRunAsPrincipalName();
      if (var2 != null) {
         return var2.isUseAnonymousIdentity();
      } else {
         if (this.raSecurityBean.getDefaultPrincipalName() != null) {
            var1 = this.useAnonForDefault();
         }

         return var1;
      }
   }

   public boolean useCallerForRunAs() {
      boolean var1 = true;
      AnonPrincipalCallerBean var2 = this.raSecurityBean.getRunAsPrincipalName();
      if (var2 != null && !var2.isUseCallerIdentity()) {
         var1 = false;
      }

      return var1;
   }

   public boolean useAnonForRunWorkAs() {
      boolean var1 = false;
      AnonPrincipalCallerBean var2 = this.raSecurityBean.getRunWorkAsPrincipalName();
      if (var2 != null) {
         return var2.isUseAnonymousIdentity();
      } else {
         if (this.raSecurityBean.getDefaultPrincipalName() != null) {
            var1 = this.useAnonForDefault();
         }

         return var1;
      }
   }

   public boolean useCallerForRunWorkAs() {
      boolean var1 = true;
      AnonPrincipalCallerBean var2 = this.raSecurityBean.getRunWorkAsPrincipalName();
      if (var2 != null && !var2.isUseCallerIdentity()) {
         var1 = false;
      }

      return var1;
   }

   private boolean useAnonForDefault() {
      AnonPrincipalBean var1 = this.raSecurityBean.getDefaultPrincipalName();
      return var1 != null && var1.isUseAnonymousIdentity();
   }

   public String getManageAsPrincipalName() {
      String var1 = this.getDefaultPrincipalName();
      AnonPrincipalBean var2 = this.raSecurityBean.getManageAsPrincipalName();
      if (var2 != null && var2.getPrincipalName() != null) {
         var1 = var2.getPrincipalName();
      }

      return var1;
   }

   public String getRunAsPrincipalName() {
      String var1 = this.getDefaultPrincipalName();
      AnonPrincipalCallerBean var2 = this.raSecurityBean.getRunAsPrincipalName();
      if (var2 != null && var2.getPrincipalName() != null) {
         var1 = var2.getPrincipalName();
      }

      return var1;
   }

   public String getRunWorkAsPrincipalName() {
      String var1 = this.getDefaultPrincipalName();
      AnonPrincipalCallerBean var2 = this.raSecurityBean.getRunWorkAsPrincipalName();
      if (var2 != null && var2.getPrincipalName() != null) {
         var1 = var2.getPrincipalName();
      }

      return var1;
   }

   public String getDefaultPrincipalName() {
      AnonPrincipalBean var1 = this.raSecurityBean.getDefaultPrincipalName();
      return var1 != null ? var1.getPrincipalName() : null;
   }
}
