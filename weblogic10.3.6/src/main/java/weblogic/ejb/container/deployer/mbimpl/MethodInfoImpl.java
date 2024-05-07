package weblogic.ejb.container.deployer.mbimpl;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.internal.RuntimeHelper;
import weblogic.ejb.container.utils.ToStringUtils;

public final class MethodInfoImpl implements MethodInfo {
   private RuntimeHelper helper = null;
   private boolean jaccEnabled;
   private Method method;
   private String methodIntf;
   private short methodDescriptorMethodType;
   private Set<String> roleNames = new HashSet();
   private String[] methodParams;
   private short transAttribute = -1;
   private int isolationLevel = -1;
   private int selectForUpdate = 0;
   private boolean isExcluded = false;
   private boolean isUnchecked = false;
   private boolean idempotent = false;
   private String realmName = null;
   private int retryOnRollbackCount = 0;

   public MethodInfoImpl(Method var1, String var2, String var3) {
      assert var1 != null;

      this.method = var1;
      Class[] var4 = var1.getParameterTypes();
      this.methodParams = new String[var4.length];

      for(int var5 = 0; var5 < var4.length; ++var5) {
         this.methodParams[var5] = var4[var5].getName();
      }

      this.methodIntf = var2;
      this.setJaccEnabled(var3);
   }

   public void setSelectForUpdate(int var1) {
      this.selectForUpdate = var1;
   }

   public int getSelectForUpdate() {
      return this.selectForUpdate;
   }

   public Method getMethod() {
      return this.method;
   }

   public String getSignature() {
      return DDUtils.getMethodSignature(this.method);
   }

   public String getMethodName() {
      return this.method.getName();
   }

   public String[] getMethodParams() {
      return (String[])((String[])this.methodParams.clone());
   }

   public void setMethodDescriptorMethodType(short var1) {
      this.methodDescriptorMethodType = var1;
   }

   public short getMethodDescriptorMethodType() {
      return this.methodDescriptorMethodType;
   }

   public String getMethodInterfaceType() {
      return this.methodIntf;
   }

   public void setRuntimeHelper(RuntimeHelper var1) {
      this.helper = var1;
   }

   public void setRealmName(String var1) {
      this.realmName = var1;
   }

   public boolean isJaccEnabled() {
      return this.jaccEnabled;
   }

   private void setJaccEnabled(String var1) {
      this.jaccEnabled = var1 != null;
   }

   public void setRetryOnRollbackCount(int var1) {
      this.retryOnRollbackCount = var1;
   }

   public int getRetryOnRollbackCount() {
      return this.retryOnRollbackCount;
   }

   public void addSecurityRoleRestriction(String var1) {
      this.roleNames.add(var1);
   }

   public Set<String> getSecurityRoleNames() {
      return this.roleNames;
   }

   public boolean hasRole(String var1) {
      return this.roleNames.contains(var1);
   }

   public boolean needsSecurityCheck() {
      assert this.realmName != null : " Error !  the realmName has not been set in MethodInfoImpl, " + this.toString();

      if (this.helper.fullyDelegateSecurityCheck(this.realmName)) {
         return true;
      } else {
         return !this.roleNames.isEmpty();
      }
   }

   public void setTransactionAttribute(short var1) {
      this.transAttribute = var1;
   }

   public short getTransactionAttribute() {
      return this.transAttribute;
   }

   public void setTxIsolationLevel(int var1) {
      this.isolationLevel = var1;
   }

   public int getTxIsolationLevel() {
      return this.isolationLevel;
   }

   public void setIsExcluded(boolean var1) {
      this.isExcluded = var1;
   }

   public boolean getIsExcluded() {
      return this.isExcluded;
   }

   public void setUnchecked(boolean var1) {
      this.isUnchecked = var1;
   }

   public boolean getUnchecked() {
      return this.isUnchecked;
   }

   public void setIdempotent(boolean var1) {
      this.idempotent = var1;
   }

   public boolean isIdempotent() {
      return this.idempotent;
   }

   public static MethodInfoImpl createMethodInfoImpl(Method var0, String var1) {
      return createMethodInfoImpl(var0, (String)null, var1);
   }

   public static MethodInfoImpl createMethodInfoImpl(Method var0, String var1, String var2) {
      assert var0 != null;

      MethodInfoImpl var3 = new MethodInfoImpl(var0, var1, var2);
      return var3;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("MethodInfo: Method: ");
      var1.append(this.getSignature());
      var1.append(" TxAttribute: " + ToStringUtils.txAttributeToString(this.transAttribute));
      var1.append(" Isolation Level: " + ToStringUtils.isoToString(this.isolationLevel));
      return var1.toString();
   }
}
