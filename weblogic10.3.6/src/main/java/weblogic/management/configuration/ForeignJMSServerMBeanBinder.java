package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class ForeignJMSServerMBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private ForeignJMSServerMBeanImpl bean;

   protected ForeignJMSServerMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (ForeignJMSServerMBeanImpl)var1;
   }

   public ForeignJMSServerMBeanBinder() {
      super(new ForeignJMSServerMBeanImpl());
      this.bean = (ForeignJMSServerMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("ConnectionURL")) {
                  try {
                     this.bean.setConnectionURL((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("ForeignJMSConnectionFactory")) {
                  try {
                     this.bean.addForeignJMSConnectionFactory((ForeignJMSConnectionFactoryMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                     this.bean.removeForeignJMSConnectionFactory((ForeignJMSConnectionFactoryMBean)var9.getExistingBean());
                     this.bean.addForeignJMSConnectionFactory((ForeignJMSConnectionFactoryMBean)((AbstractDescriptorBean)((ForeignJMSConnectionFactoryMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("ForeignJMSDestination")) {
                  try {
                     this.bean.addForeignJMSDestination((ForeignJMSDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     this.bean.removeForeignJMSDestination((ForeignJMSDestinationMBean)var8.getExistingBean());
                     this.bean.addForeignJMSDestination((ForeignJMSDestinationMBean)((AbstractDescriptorBean)((ForeignJMSDestinationMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("InitialContextFactory")) {
                  try {
                     this.bean.setInitialContextFactory((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("JNDIProperties")) {
                  this.bean.setJNDIPropertiesAsString((String)var2);
               } else if (var1.equals("JNDIPropertiesCredential")) {
                  try {
                     if (this.bean.isJNDIPropertiesCredentialEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to JNDIPropertiesCredential [ ForeignJMSServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setJNDIPropertiesCredential((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("JNDIPropertiesCredentialEncrypted")) {
                  if (this.bean.isJNDIPropertiesCredentialEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to JNDIPropertiesCredentialEncrypted [ ForeignJMSServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setJNDIPropertiesCredentialEncryptedAsString((String)var2);
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else if (var1.equals("Targets")) {
                  this.bean.setTargetsAsString((String)var2);
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var11) {
         System.out.println(var11 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var11;
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Exception var13) {
         if (var13 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var13);
         } else if (var13 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var13.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var13);
         }
      }
   }
}
