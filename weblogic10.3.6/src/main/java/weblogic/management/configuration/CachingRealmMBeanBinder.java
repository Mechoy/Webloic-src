package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class CachingRealmMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private CachingRealmMBeanImpl bean;

   protected CachingRealmMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (CachingRealmMBeanImpl)var1;
   }

   public CachingRealmMBeanBinder() {
      super(new CachingRealmMBeanImpl());
      this.bean = (CachingRealmMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("ACLCacheEnable")) {
                  try {
                     this.bean.setACLCacheEnable(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("ACLCacheSize")) {
                  try {
                     this.bean.setACLCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("ACLCacheTTLNegative")) {
                  try {
                     this.bean.setACLCacheTTLNegative(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("ACLCacheTTLPositive")) {
                  try {
                     this.bean.setACLCacheTTLPositive(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("AuthenticationCacheEnable")) {
                  try {
                     this.bean.setAuthenticationCacheEnable(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("AuthenticationCacheSize")) {
                  try {
                     this.bean.setAuthenticationCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("AuthenticationCacheTTLNegative")) {
                  try {
                     this.bean.setAuthenticationCacheTTLNegative(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("AuthenticationCacheTTLPositive")) {
                  try {
                     this.bean.setAuthenticationCacheTTLPositive(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("BasicRealm")) {
                  this.bean.setBasicRealmAsString((String)var2);
               } else if (var1.equals("CacheCaseSensitive")) {
                  try {
                     this.bean.setCacheCaseSensitive(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("GroupCacheEnable")) {
                  try {
                     this.bean.setGroupCacheEnable(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("GroupCacheSize")) {
                  try {
                     this.bean.setGroupCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("GroupCacheTTLNegative")) {
                  try {
                     this.bean.setGroupCacheTTLNegative(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("GroupCacheTTLPositive")) {
                  try {
                     this.bean.setGroupCacheTTLPositive(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("GroupMembershipCacheTTL")) {
                  try {
                     this.bean.setGroupMembershipCacheTTL(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("PermissionCacheEnable")) {
                  try {
                     this.bean.setPermissionCacheEnable(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("PermissionCacheSize")) {
                  try {
                     this.bean.setPermissionCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("PermissionCacheTTLNegative")) {
                  try {
                     this.bean.setPermissionCacheTTLNegative(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("PermissionCacheTTLPositive")) {
                  try {
                     this.bean.setPermissionCacheTTLPositive(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("UserCacheEnable")) {
                  try {
                     this.bean.setUserCacheEnable(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("UserCacheSize")) {
                  try {
                     this.bean.setUserCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("UserCacheTTLNegative")) {
                  try {
                     this.bean.setUserCacheTTLNegative(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("UserCacheTTLPositive")) {
                  try {
                     this.bean.setUserCacheTTLPositive(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var27) {
         System.out.println(var27 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var27;
      } catch (RuntimeException var28) {
         throw var28;
      } catch (Exception var29) {
         if (var29 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var29);
         } else if (var29 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var29.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var29);
         }
      }
   }
}
