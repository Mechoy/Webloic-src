package weblogic.spring.monitoring;

import java.util.ArrayList;
import java.util.Iterator;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SpringApplicationContextRuntimeMBean;
import weblogic.management.runtime.SpringBeanDefinitionRuntimeMBean;
import weblogic.management.runtime.SpringRuntimeMBean;
import weblogic.management.runtime.SpringTransactionManagerRuntimeMBean;
import weblogic.management.runtime.SpringTransactionTemplateRuntimeMBean;
import weblogic.management.runtime.SpringViewResolverRuntimeMBean;
import weblogic.management.runtime.SpringViewRuntimeMBean;

public class SpringRuntimeMBeanImpl extends RuntimeMBeanDelegate implements SpringRuntimeMBean {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugSpringStatistics");
   private String version;
   private SpringBeanDefinitionRuntimeMBean[] springBeanDefinitionRuntiemMBeans;
   private ArrayList<SpringApplicationContextRuntimeMBean> springApplicationContextRuntimeMBeans;
   private ArrayList<SpringTransactionManagerRuntimeMBean> springTransactionManagerRuntimeMBeans;
   private ArrayList<SpringTransactionTemplateRuntimeMBean> springTransactionTemplateRuntimeMBeans;
   private ArrayList<SpringViewRuntimeMBean> springViewRuntimeMBeans;
   private ArrayList<SpringViewResolverRuntimeMBean> springViewResolverRuntimeMBeans;

   public SpringRuntimeMBeanImpl(String var1, RuntimeMBean var2) throws ManagementException {
      super(var1, var2);
   }

   public void setSpringVersion(String var1) {
      this.version = var1;
   }

   public String getSpringVersion() {
      return this.version;
   }

   public void setSpringBeanDefinitionRuntimeMBeans(SpringBeanDefinitionRuntimeMBean[] var1) {
      this.springBeanDefinitionRuntiemMBeans = var1;
   }

   public SpringBeanDefinitionRuntimeMBean[] getSpringBeanDefinitionRuntimeMBeans() {
      return this.springBeanDefinitionRuntiemMBeans;
   }

   public void unregister() throws ManagementException {
      super.unregister();
   }

   public SpringApplicationContextRuntimeMBean[] getSpringApplicationContextRuntimeMBeans() {
      return this.springApplicationContextRuntimeMBeans == null ? null : (SpringApplicationContextRuntimeMBean[])this.springApplicationContextRuntimeMBeans.toArray(new SpringApplicationContextRuntimeMBean[this.springApplicationContextRuntimeMBeans.size()]);
   }

   public SpringTransactionManagerRuntimeMBean[] getSpringTransactionManagerRuntimeMBeans() {
      return this.springTransactionManagerRuntimeMBeans == null ? null : (SpringTransactionManagerRuntimeMBean[])this.springTransactionManagerRuntimeMBeans.toArray(new SpringTransactionManagerRuntimeMBean[this.springTransactionManagerRuntimeMBeans.size()]);
   }

   public SpringTransactionTemplateRuntimeMBean[] getSpringTransactionTemplateRuntimeMBeans() {
      return this.springTransactionTemplateRuntimeMBeans == null ? null : (SpringTransactionTemplateRuntimeMBean[])this.springTransactionTemplateRuntimeMBeans.toArray(new SpringTransactionTemplateRuntimeMBean[this.springTransactionTemplateRuntimeMBeans.size()]);
   }

   public SpringViewResolverRuntimeMBean[] getSpringViewResolverRuntimeMBeans() {
      return this.springViewResolverRuntimeMBeans == null ? null : (SpringViewResolverRuntimeMBean[])this.springViewResolverRuntimeMBeans.toArray(new SpringViewResolverRuntimeMBean[this.springViewResolverRuntimeMBeans.size()]);
   }

   public SpringViewRuntimeMBean[] getSpringViewRuntimeMBeans() {
      return this.springViewRuntimeMBeans == null ? null : (SpringViewRuntimeMBean[])this.springViewRuntimeMBeans.toArray(new SpringViewRuntimeMBean[this.springViewRuntimeMBeans.size()]);
   }

   public void addSpringApplicationContextRuntimeMBean(SpringApplicationContextRuntimeMBean var1) {
      if (this.springApplicationContextRuntimeMBeans == null) {
         this.springApplicationContextRuntimeMBeans = new ArrayList();
      }

      this.springApplicationContextRuntimeMBeans.add(var1);
   }

   public void addSpringTransactionManagerRuntimeMBean(SpringTransactionManagerRuntimeMBean var1) {
      if (this.springTransactionManagerRuntimeMBeans == null) {
         this.springTransactionManagerRuntimeMBeans = new ArrayList();
      }

      this.springTransactionManagerRuntimeMBeans.add(var1);
   }

   public void addSpringTransactionTemplateRuntimeMBean(SpringTransactionTemplateRuntimeMBean var1) {
      if (this.springTransactionTemplateRuntimeMBeans == null) {
         this.springTransactionTemplateRuntimeMBeans = new ArrayList();
      }

      this.springTransactionTemplateRuntimeMBeans.add(var1);
   }

   public void addSpringViewResolverRuntimeMBean(SpringViewResolverRuntimeMBean var1) {
      if (this.springViewResolverRuntimeMBeans == null) {
         this.springViewResolverRuntimeMBeans = new ArrayList();
      }

      this.springViewResolverRuntimeMBeans.add(var1);
   }

   public void addSpringViewRuntimeMBean(SpringViewRuntimeMBean var1) {
      if (this.springViewRuntimeMBeans == null) {
         this.springViewRuntimeMBeans = new ArrayList();
      }

      this.springViewRuntimeMBeans.add(var1);
   }

   public void removeSpringApplicationContextRuntimeMBean(SpringApplicationContextRuntimeMBean var1) {
      if (this.springApplicationContextRuntimeMBeans != null) {
         this.springApplicationContextRuntimeMBeans.remove(var1);
      }

   }

   public void removeSpringTransactionManagerRuntimeMBean(SpringTransactionManagerRuntimeMBean var1) {
      if (this.springTransactionManagerRuntimeMBeans != null) {
         this.springTransactionManagerRuntimeMBeans.remove(var1);
      }

   }

   public void removeSpringTransactionTemplateRuntimeMBean(SpringTransactionTemplateRuntimeMBean var1) {
      if (this.springTransactionTemplateRuntimeMBeans != null) {
         this.springTransactionTemplateRuntimeMBeans.remove(var1);
      }

   }

   public void removeSpringViewResolverRuntimeMBean(SpringViewResolverRuntimeMBean var1) {
      if (this.springViewResolverRuntimeMBeans != null) {
         this.springViewResolverRuntimeMBeans.remove(var1);
      }

   }

   public void removeSpringViewRuntimeMBean(SpringViewRuntimeMBean var1) {
      if (this.springViewRuntimeMBeans != null) {
         this.springViewRuntimeMBeans.remove(var1);
      }

   }

   public void removeRegisteredApplicationContextRuntimeMBeans(Object var1) {
      this.removeRegisteredMBeans(var1, this.springApplicationContextRuntimeMBeans);
   }

   public void removeRegisteredTransactionTemplateRuntimeMBeans(Object var1) {
      this.removeRegisteredMBeans(var1, this.springTransactionTemplateRuntimeMBeans);
   }

   public void removeRegisteredViewRuntimeMBeans(Object var1) {
      this.removeRegisteredMBeans(var1, this.springViewRuntimeMBeans);
   }

   public void removeRegisteredViewResolverRuntimeMBeans(Object var1) {
      this.removeRegisteredMBeans(var1, this.springViewResolverRuntimeMBeans);
   }

   public void removeRegisteredTransactionManagerRuntimeMBeans(Object var1) {
      this.removeRegisteredMBeans(var1, this.springTransactionManagerRuntimeMBeans);
   }

   private void removeRegisteredMBeans(Object var1, ArrayList var2) {
      if (var2 != null && !var2.isEmpty()) {
         Iterator var3 = var2.iterator();

         while(true) {
            while(var3.hasNext()) {
               SpringBaseRuntimeMBeanImpl var4 = (SpringBaseRuntimeMBeanImpl)var3.next();
               if (var1 != null && (var4.getApplicationContext() == null || var4.getApplicationContext() != var1)) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("SpringRuntimeMBeanImpl skip unregister for: " + var4.getName());
                  }
               } else if (var4.isRegistered()) {
                  try {
                     if (debugLogger.isDebugEnabled()) {
                        debugLogger.debug("SpringRuntimeMBeanImpl unregister: " + var4.getName());
                     }

                     var4.unregister();
                  } catch (ManagementException var6) {
                     if (debugLogger.isDebugEnabled()) {
                        debugLogger.debug("removeRegisteredMBeans ignoring", var6);
                     }
                  }

                  var3.remove();
               }
            }

            return;
         }
      }
   }
}
