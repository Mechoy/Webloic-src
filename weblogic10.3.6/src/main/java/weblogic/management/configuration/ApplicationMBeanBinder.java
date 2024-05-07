package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class ApplicationMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private ApplicationMBeanImpl bean;

   protected ApplicationMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (ApplicationMBeanImpl)var1;
   }

   public ApplicationMBeanBinder() {
      super(new ApplicationMBeanImpl());
      this.bean = (ApplicationMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AltDescriptorPath")) {
                  try {
                     this.bean.setAltDescriptorPath((String)var2);
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("AltWLSDescriptorPath")) {
                  try {
                     this.bean.setAltWLSDescriptorPath((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("ConnectorComponent")) {
                  try {
                     this.bean.addConnectorComponent((ConnectorComponentMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                     this.bean.removeConnectorComponent((ConnectorComponentMBean)var18.getExistingBean());
                     this.bean.addConnectorComponent((ConnectorComponentMBean)((AbstractDescriptorBean)((ConnectorComponentMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("DeploymentTimeout")) {
                  this.handleDeprecatedProperty("DeploymentTimeout", "9.0.0.0 Replaced by {@link weblogic.deploy.api.spi.DeploymentOptions#getClusterDeploymentTimeout()}");

                  try {
                     this.bean.setDeploymentTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("EJBComponent")) {
                  try {
                     this.bean.addEJBComponent((EJBComponentMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                     this.bean.removeEJBComponent((EJBComponentMBean)var16.getExistingBean());
                     this.bean.addEJBComponent((EJBComponentMBean)((AbstractDescriptorBean)((EJBComponentMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("JDBCPoolComponent")) {
                  try {
                     this.bean.addJDBCPoolComponent((JDBCPoolComponentMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                     this.bean.removeJDBCPoolComponent((JDBCPoolComponentMBean)var15.getExistingBean());
                     this.bean.addJDBCPoolComponent((JDBCPoolComponentMBean)((AbstractDescriptorBean)((JDBCPoolComponentMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("LoadOrder")) {
                  try {
                     this.bean.setLoadOrder(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("Notes")) {
                  try {
                     this.bean.setNotes((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("Path")) {
                  try {
                     this.bean.setPath((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("StagedTargets")) {
                  try {
                     this.bean.setStagedTargets(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("StagingMode")) {
                  try {
                     this.bean.setStagingMode((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("WebAppComponent")) {
                  try {
                     this.bean.addWebAppComponent((WebAppComponentMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     this.bean.removeWebAppComponent((WebAppComponentMBean)var8.getExistingBean());
                     this.bean.addWebAppComponent((WebAppComponentMBean)((AbstractDescriptorBean)((WebAppComponentMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WebServiceComponent")) {
                  try {
                     this.bean.addWebServiceComponent((WebServiceComponentMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                     this.bean.removeWebServiceComponent((WebServiceComponentMBean)var7.getExistingBean());
                     this.bean.addWebServiceComponent((WebServiceComponentMBean)((AbstractDescriptorBean)((WebServiceComponentMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("Deployed")) {
                  this.handleDeprecatedProperty("Deployed", "9.0.0.0");

                  try {
                     this.bean.setDeployed(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else {
                  if (var1.equals("Ear")) {
                     throw new AssertionError("can't set read-only property Ear");
                  }

                  if (var1.equals("TwoPhase")) {
                     this.handleDeprecatedProperty("TwoPhase", "Always returns true");

                     try {
                        this.bean.setTwoPhase(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var5) {
                        System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                     }
                  } else {
                     var3 = super.bindAttribute(var1, var2);
                  }
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var21) {
         System.out.println(var21 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var21;
      } catch (RuntimeException var22) {
         throw var22;
      } catch (Exception var23) {
         if (var23 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var23);
         } else if (var23 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var23.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var23);
         }
      }
   }
}
