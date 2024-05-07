package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class NodeManagerMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private NodeManagerMBeanImpl bean;

   protected NodeManagerMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (NodeManagerMBeanImpl)var1;
   }

   public NodeManagerMBeanBinder() {
      super(new NodeManagerMBeanImpl());
      this.bean = (NodeManagerMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("Adapter")) {
                  try {
                     this.bean.setAdapter((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("AdapterName")) {
                  this.handleDeprecatedProperty("AdapterName", "10.3.4.0 Replaced by {@link weblogic.management.configuration.NodeManagerMBean#getAdapter()}");

                  try {
                     this.bean.setAdapterName((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("AdapterVersion")) {
                  this.handleDeprecatedProperty("AdapterVersion", "10.3.4.0 Replaced by {@link weblogic.management.configuration.NodeManagerMBean#setAdapter()}");

                  try {
                     this.bean.setAdapterVersion((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else {
                  if (var1.equals("InstalledVMMAdapters")) {
                     throw new AssertionError("can't set read-only property InstalledVMMAdapters");
                  }

                  if (var1.equals("ListenAddress")) {
                     try {
                        this.bean.setListenAddress((String)var2);
                     } catch (BeanAlreadyExistsException var13) {
                        System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                     }
                  } else if (var1.equals("ListenPort")) {
                     try {
                        this.bean.setListenPort(Integer.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var12) {
                        System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                     }
                  } else if (var1.equals("NMType")) {
                     try {
                        this.bean.setNMType((String)var2);
                     } catch (BeanAlreadyExistsException var11) {
                        System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                     }
                  } else if (var1.equals("Name")) {
                     try {
                        this.bean.setName((String)var2);
                     } catch (BeanAlreadyExistsException var10) {
                        System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                     }
                  } else if (var1.equals("NodeManagerHome")) {
                     try {
                        this.bean.setNodeManagerHome((String)var2);
                     } catch (BeanAlreadyExistsException var9) {
                        System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                     }
                  } else if (var1.equals("Password")) {
                     try {
                        if (this.bean.isPasswordEncryptedSet()) {
                           throw new IllegalArgumentException("Encrypted attribute corresponding to Password [ NodeManagerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                        }

                        this.bean.setPassword((String)var2);
                     } catch (BeanAlreadyExistsException var8) {
                        System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     }
                  } else if (var1.equals("PasswordEncrypted")) {
                     if (this.bean.isPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Unencrypted attribute corresponding to PasswordEncrypted [ NodeManagerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setPasswordEncryptedAsString((String)var2);
                  } else if (var1.equals("ShellCommand")) {
                     try {
                        this.bean.setShellCommand((String)var2);
                     } catch (BeanAlreadyExistsException var7) {
                        System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                     }
                  } else if (var1.equals("UserName")) {
                     try {
                        this.bean.setUserName((String)var2);
                     } catch (BeanAlreadyExistsException var6) {
                        System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                     }
                  } else if (var1.equals("DebugEnabled")) {
                     try {
                        this.bean.setDebugEnabled(Boolean.valueOf((String)var2));
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
      } catch (ClassCastException var17) {
         System.out.println(var17 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var17;
      } catch (RuntimeException var18) {
         throw var18;
      } catch (Exception var19) {
         if (var19 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var19);
         } else if (var19 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var19.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var19);
         }
      }
   }
}
