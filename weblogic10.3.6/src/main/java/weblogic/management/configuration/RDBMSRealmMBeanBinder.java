package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class RDBMSRealmMBeanBinder extends BasicRealmMBeanBinder implements AttributeBinder {
   private RDBMSRealmMBeanImpl bean;

   protected RDBMSRealmMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (RDBMSRealmMBeanImpl)var1;
   }

   public RDBMSRealmMBeanBinder() {
      super(new RDBMSRealmMBeanImpl());
      this.bean = (RDBMSRealmMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("DatabaseDriver")) {
                  try {
                     this.bean.setDatabaseDriver((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("DatabasePassword")) {
                  try {
                     if (this.bean.isDatabasePasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to DatabasePassword [ RDBMSRealmMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setDatabasePassword((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("DatabasePasswordEncrypted")) {
                  if (this.bean.isDatabasePasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to DatabasePasswordEncrypted [ RDBMSRealmMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setDatabasePasswordEncryptedAsString((String)var2);
               } else if (var1.equals("DatabaseURL")) {
                  try {
                     this.bean.setDatabaseURL((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("DatabaseUserName")) {
                  try {
                     this.bean.setDatabaseUserName((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("RealmClassName")) {
                  try {
                     this.bean.setRealmClassName((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else if (var1.equals("SchemaProperties")) {
                  this.bean.setSchemaPropertiesAsString((String)var2);
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var10) {
         System.out.println(var10 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Exception var12) {
         if (var12 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var12);
         } else if (var12 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var12.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var12);
         }
      }
   }
}
