package weblogic.spring.monitoring;

import java.util.HashMap;
import java.util.Set;
import weblogic.management.ManagementException;
import weblogic.management.runtime.SpringApplicationContextRuntimeMBean;
import weblogic.spring.SpringLogger;
import weblogic.spring.monitoring.utils.AbstractApplicationContextDelegator;

public class SpringApplicationContextRuntimeMBeanImpl extends SpringBaseRuntimeMBeanImpl implements SpringApplicationContextRuntimeMBean {
   private AbstractApplicationContextDelegator appCtxDelegator;
   private String parentContext;
   private long startupDate;
   private Object beanFactory;
   private long numberOfPrototypeBeansCreated;
   private double elapsedTimesPrototypeBeanCreation;
   private long numberOfSingletonBeansCreated;
   private double elapsedTimesSingletonBeanCreation;
   private long refreshCount;
   private long refreshFailedCount;
   private double elapsedTimesRefresh;
   private double elapsedTimesGetBean;
   private long getBeanExecutions;
   private long getBeanFailedExecutions;
   private double elapsedTimesGetBeansOfType;
   private long getBeansOfTypeExecutions;
   private long getBeansOfTypeFailedExecutions;
   private double elapsedTimesGetBeanNamesForType;
   private long getBeanNamesForTypeExecutions;
   private long getBeanNamesForTypeFailedExecutions;
   private HashMap<String, CustomScopeInfo> customScopes;

   public SpringApplicationContextRuntimeMBeanImpl(String var1, Object var2) throws ManagementException {
      super(var2, var2, var1, false);
      this.appCtxDelegator = new AbstractApplicationContextDelegator(var2);
      this.parentContext = this.appCtxDelegator.getParentContext();
      this.startupDate = this.appCtxDelegator.getStartupDate();
      this.beanFactory = this.appCtxDelegator.getBeanFactory();
      this.customScopes = new HashMap();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringApplicationContextRuntimeMBeanImpl(" + var1 + ")");
      }

   }

   public String getDisplayName() {
      return this.getApplicationContextDisplayName();
   }

   public String getParentContext() {
      return this.parentContext;
   }

   public long getStartupDate() {
      return this.startupDate;
   }

   public Object getBeanFactory() {
      return this.beanFactory;
   }

   public synchronized double getAveragePrototypeBeanCreationTime() {
      return this.numberOfPrototypeBeansCreated == 0L ? 0.0 : this.elapsedTimesPrototypeBeanCreation / 1000000.0 / (double)this.numberOfPrototypeBeansCreated;
   }

   public synchronized long getPrototypeBeansCreatedCount() {
      return this.numberOfPrototypeBeansCreated;
   }

   public synchronized double getAverageSingletonBeanCreationTime() {
      return this.numberOfSingletonBeansCreated == 0L ? 0.0 : this.elapsedTimesSingletonBeanCreation / 1000000.0 / (double)this.numberOfSingletonBeansCreated;
   }

   public synchronized long getSingletonBeansCreatedCount() {
      return this.numberOfSingletonBeansCreated;
   }

   public String[] getCustomScopeNames() {
      if (this.customScopes != null && !this.customScopes.isEmpty()) {
         Set var1 = this.customScopes.keySet();
         return (String[])var1.toArray(new String[var1.size()]);
      } else {
         return null;
      }
   }

   public double getAverageCustomScopeBeanCreationTime(String var1) throws IllegalArgumentException {
      if (var1 != null && var1.length() != 0) {
         CustomScopeInfo var2 = (CustomScopeInfo)this.customScopes.get(var1);
         if (var2 != null) {
            return var2.numberOfCustomScopeBeansCreated == 0L ? 0.0 : var2.elapsedTimesCustomScopeBeanCreation / 1000000.0 / (double)var2.numberOfCustomScopeBeansCreated;
         } else {
            throw new IllegalArgumentException(SpringLogger.getUnregisteredScopeName(var1));
         }
      } else {
         throw new IllegalArgumentException(SpringLogger.getUnregisteredScopeName(var1));
      }
   }

   public long getCustomScopeBeansCreatedCount(String var1) throws IllegalArgumentException {
      if (var1 != null && var1.length() != 0) {
         CustomScopeInfo var2 = (CustomScopeInfo)this.customScopes.get(var1);
         if (var2 != null) {
            return var2.numberOfCustomScopeBeansCreated;
         } else {
            throw new IllegalArgumentException(SpringLogger.getUnregisteredScopeName(var1));
         }
      } else {
         throw new IllegalArgumentException(SpringLogger.getUnregisteredScopeName(var1));
      }
   }

   public synchronized double getAverageRefreshTime() {
      return this.refreshCount == 0L ? 0.0 : this.elapsedTimesRefresh / 1000000.0 / (double)this.refreshCount;
   }

   public synchronized long getRefreshCount() {
      return this.refreshCount;
   }

   public synchronized long getRefreshFailedCount() {
      return this.refreshFailedCount;
   }

   public synchronized double getAverageGetBeanTime() {
      return this.getBeanExecutions == 0L ? 0.0 : this.elapsedTimesGetBean / 1000000.0 / (double)this.getBeanExecutions;
   }

   public synchronized long getGetBeanCount() {
      return this.getBeanExecutions;
   }

   public synchronized long getGetBeanFailedCount() {
      return this.getBeanFailedExecutions;
   }

   public synchronized double getAverageGetBeansOfTypeTime() {
      return this.getBeansOfTypeExecutions == 0L ? 0.0 : this.elapsedTimesGetBeansOfType / 1000000.0 / (double)this.getBeansOfTypeExecutions;
   }

   public synchronized long getGetBeansOfTypeCount() {
      return this.getBeansOfTypeExecutions;
   }

   public synchronized long getGetBeansOfTypeFailedCount() {
      return this.getBeansOfTypeFailedExecutions;
   }

   public synchronized double getAverageGetBeanNamesForTypeTime() {
      return this.getBeanNamesForTypeExecutions == 0L ? 0.0 : this.elapsedTimesGetBeanNamesForType / 1000000.0 / (double)this.getBeanNamesForTypeExecutions;
   }

   public synchronized long getGetBeanNamesForTypeCount() {
      return this.getBeanNamesForTypeExecutions;
   }

   public synchronized long getGetBeanNamesForTypeFailedCount() {
      return this.getBeanNamesForTypeFailedExecutions;
   }

   public synchronized void addPrototypeBeanCreation(long var1) {
      ++this.numberOfPrototypeBeansCreated;
      this.elapsedTimesPrototypeBeanCreation += (double)var1;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringApplicationContextRuntimeMBeanImpl.addPrototypeBeanCreation() : " + this.name);
      }

   }

   public synchronized void addSingletonBeanCreation(long var1) {
      ++this.numberOfSingletonBeansCreated;
      this.elapsedTimesSingletonBeanCreation += (double)var1;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringApplicationContextRuntimeMBeanImpl.addSingletonBeanCreation() : " + this.name);
      }

   }

   public synchronized void addCustomScope(String var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringApplicationContextRuntimeMBeanImpl.addCustomScope(" + var1 + ") : " + this.name);
      }

      if (var1 != null && var1.length() != 0) {
         CustomScopeInfo var2 = (CustomScopeInfo)this.customScopes.get(var1);
         if (var2 == null) {
            var2 = new CustomScopeInfo();
            this.customScopes.put(var1, var2);
         }

      } else {
         throw new IllegalArgumentException();
      }
   }

   public synchronized void addCustomScopeBeanCreation(String var1, long var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringApplicationContextRuntimeMBeanImpl.addCustomScopeBeanCreation(" + var1 + ") : " + this.name);
      }

      if (var1 != null && var1.length() != 0) {
         CustomScopeInfo var4 = (CustomScopeInfo)this.customScopes.get(var1);
         if (var4 == null) {
            var4 = new CustomScopeInfo();
            this.customScopes.put(var1, var4);
         }

         ++var4.numberOfCustomScopeBeansCreated;
         var4.elapsedTimesCustomScopeBeanCreation += (double)var2;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public synchronized void addRefresh(boolean var1, long var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringApplicationContextRuntimeMBeanImpl.addRefresh() : " + this.name);
      }

      ++this.refreshCount;
      if (!var1) {
         ++this.refreshFailedCount;
      }

      this.elapsedTimesRefresh += (double)var2;
   }

   public synchronized void addGetBeanExecution(boolean var1, long var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringApplicationContextRuntimeMBeanImpl.addGetBeanExecution() : " + this.name);
      }

      ++this.getBeanExecutions;
      if (!var1) {
         ++this.getBeanFailedExecutions;
      }

      this.elapsedTimesGetBean += (double)var2;
   }

   public synchronized void addGetBeansOfTypeExecution(boolean var1, long var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringApplicationContextRuntimeMBeanImpl.addGetBeansOfTypeExecution() : " + this.name);
      }

      ++this.getBeansOfTypeExecutions;
      if (!var1) {
         ++this.getBeansOfTypeFailedExecutions;
      }

      this.elapsedTimesGetBeansOfType += (double)var2;
   }

   public synchronized void addGetBeanNamesForTypeExecution(boolean var1, long var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringApplicationContextRuntimeMBeanImpl.addGetBeanNamesForTypeExecution() : " + this.name);
      }

      ++this.getBeanNamesForTypeExecutions;
      if (!var1) {
         ++this.getBeanNamesForTypeFailedExecutions;
      }

      this.elapsedTimesGetBeanNamesForType += (double)var2;
   }

   public synchronized void updateBeanFactory() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("SpringApplicationContextRuntimeMBeanImpl.resetBeanFactoryStats() : " + this.name);
      }

      this.beanFactory = this.appCtxDelegator.getBeanFactory();
      this.numberOfPrototypeBeansCreated = 0L;
      this.elapsedTimesPrototypeBeanCreation = 0.0;
      this.numberOfSingletonBeansCreated = 0L;
      this.elapsedTimesSingletonBeanCreation = 0.0;
      this.elapsedTimesGetBean = 0.0;
      this.getBeanExecutions = 0L;
      this.getBeanFailedExecutions = 0L;
      this.elapsedTimesGetBeansOfType = 0.0;
      this.getBeansOfTypeExecutions = 0L;
      this.getBeansOfTypeFailedExecutions = 0L;
      this.elapsedTimesGetBeanNamesForType = 0.0;
      this.getBeanNamesForTypeExecutions = 0L;
      this.getBeanNamesForTypeFailedExecutions = 0L;
      if (this.customScopes != null) {
         this.customScopes.clear();
         this.customScopes = null;
      }

   }

   private static class CustomScopeInfo {
      public long numberOfCustomScopeBeansCreated;
      public double elapsedTimesCustomScopeBeanCreation;

      private CustomScopeInfo() {
      }

      // $FF: synthetic method
      CustomScopeInfo(Object var1) {
         this();
      }
   }
}
