package weblogic.management.security.authentication;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.security.BaseMBeanImpl;
import weblogic.management.security.RealmMBean;
import weblogic.security.internal.UserLockoutManagerMBeanCustomizerImpl;

public class UserLockoutManagerImpl extends BaseMBeanImpl {
   private UserLockoutManagerMBeanCustomizerImpl customizer;

   public UserLockoutManagerImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected UserLockoutManagerImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }

   private UserLockoutManagerMBean getUserLockoutManager() {
      try {
         return (UserLockoutManagerMBean)this.getProxy();
      } catch (MBeanException var2) {
         throw new AssertionError(var2);
      }
   }

   public String getCompatibilityObjectName() {
      String var1 = "Security:Name=";
      String var2 = var1 + this.getRealm().getName() + this.getUserLockoutManager().getName();
      return var2;
   }

   private synchronized UserLockoutManagerMBeanCustomizerImpl getCustomizer() {
      if (this.customizer == null) {
         this.customizer = new UserLockoutManagerMBeanCustomizerImpl(this.getUserLockoutManager());
      }

      return this.customizer;
   }

   public RealmMBean getRealm() {
      DescriptorBean var1 = this.getUserLockoutManager().getParentBean();
      return var1 instanceof RealmMBean ? (RealmMBean)var1 : null;
   }

   public long getUserLockoutTotalCount() throws MBeanException {
      return this.getCustomizer().getUserLockoutTotalCount();
   }

   public long getInvalidLoginAttemptsTotalCount() throws MBeanException {
      return this.getCustomizer().getInvalidLoginAttemptsTotalCount();
   }

   public long getLoginAttemptsWhileLockedTotalCount() throws MBeanException {
      return this.getCustomizer().getLoginAttemptsWhileLockedTotalCount();
   }

   public long getInvalidLoginUsersHighCount() throws MBeanException {
      return this.getCustomizer().getInvalidLoginUsersHighCount();
   }

   public long getUnlockedUsersTotalCount() throws MBeanException {
      return this.getCustomizer().getUnlockedUsersTotalCount();
   }

   public long getLockedUsersCurrentCount() throws MBeanException {
      return this.getCustomizer().getLockedUsersCurrentCount();
   }

   public boolean isLockedOut(String var1) throws MBeanException {
      return this.getCustomizer().isLockedOut(var1);
   }

   public long getLastLoginFailure(String var1) throws MBeanException {
      return this.getCustomizer().getLastLoginFailure(var1);
   }

   public long getLoginFailureCount(String var1) throws MBeanException {
      return this.getCustomizer().getLoginFailureCount(var1);
   }

   public void clearLockout(String var1) throws MBeanException {
      this.getCustomizer().clearLockout(var1);
   }
}
