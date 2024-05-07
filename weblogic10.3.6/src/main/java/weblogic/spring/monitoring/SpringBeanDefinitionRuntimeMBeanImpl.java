package weblogic.spring.monitoring;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SpringBeanDefinitionRuntimeMBean;
import weblogic.management.runtime.SpringBeanDependencyValue;

public class SpringBeanDefinitionRuntimeMBeanImpl extends RuntimeMBeanDelegate implements SpringBeanDefinitionRuntimeMBean {
   private String beanId;
   private String beanClassname;
   private String scope;
   private int role;
   private boolean singleton;
   private boolean abstractFlag = false;
   private boolean autowire = false;
   private boolean lazyInit = false;
   private String resourceDescription;
   private String applicationContextDisplayName;
   private String[] aliases;
   private String[] dependencies;
   private String parentId;
   private SpringBeanDependencyValue[] dependencyValues;

   public SpringBeanDefinitionRuntimeMBeanImpl(String var1, RuntimeMBean var2, String var3, String var4, String var5, String[] var6, String[] var7, boolean var8, boolean var9, boolean var10, boolean var11, int var12, String var13, String var14, String var15, SpringBeanDependencyValue[] var16) throws ManagementException {
      super(var1, var2);
      this.beanId = var3;
      this.beanClassname = var4;
      this.parentId = var5;
      this.aliases = var6;
      this.dependencies = var7;
      this.singleton = var8;
      this.lazyInit = var9;
      this.abstractFlag = var10;
      this.autowire = var11;
      this.role = var12;
      this.scope = var13;
      this.resourceDescription = var14;
      this.applicationContextDisplayName = var15;
      this.dependencyValues = var16;
   }

   public String getBeanId() {
      return this.beanId;
   }

   public String getBeanClassname() {
      return this.beanClassname;
   }

   public boolean isAbstract() {
      return this.abstractFlag;
   }

   public boolean isLazyInit() {
      return this.lazyInit;
   }

   public String getResourceDescription() {
      return this.resourceDescription;
   }

   public boolean isSingleton() {
      return this.singleton;
   }

   public String getApplicationContextDisplayName() {
      return this.applicationContextDisplayName;
   }

   public String[] getAliases() {
      return this.aliases;
   }

   public String[] getDependencies() {
      return this.dependencies;
   }

   public String getParentId() {
      return this.parentId;
   }

   public int getRole() {
      return this.role;
   }

   public String getScope() {
      return this.scope;
   }

   public boolean isAutowireCandidate() {
      return this.autowire;
   }

   public SpringBeanDependencyValue[] getDependencyValues() {
      return this.dependencyValues;
   }

   public void unregister() throws ManagementException {
      super.unregister();
   }
}
