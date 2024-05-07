package weblogic.ejb.container.interfaces;

import java.lang.reflect.Method;
import java.util.Set;
import weblogic.ejb.container.internal.RuntimeHelper;

public interface MethodInfo {
   void setRuntimeHelper(RuntimeHelper var1);

   Method getMethod();

   String getSignature();

   String getMethodName();

   String[] getMethodParams();

   void setMethodDescriptorMethodType(short var1);

   short getMethodDescriptorMethodType();

   String getMethodInterfaceType();

   boolean isJaccEnabled();

   boolean hasRole(String var1);

   void setRealmName(String var1);

   boolean needsSecurityCheck();

   void setTxIsolationLevel(int var1);

   void addSecurityRoleRestriction(String var1);

   void setTransactionAttribute(short var1);

   short getTransactionAttribute();

   int getTxIsolationLevel();

   void setRetryOnRollbackCount(int var1);

   int getRetryOnRollbackCount();

   int getSelectForUpdate();

   void setSelectForUpdate(int var1);

   Set<String> getSecurityRoleNames();

   void setIsExcluded(boolean var1);

   boolean getIsExcluded();

   void setUnchecked(boolean var1);

   boolean getUnchecked();

   void setIdempotent(boolean var1);

   boolean isIdempotent();
}
