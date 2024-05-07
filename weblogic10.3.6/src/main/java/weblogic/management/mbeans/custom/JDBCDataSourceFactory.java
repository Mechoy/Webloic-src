package weblogic.management.mbeans.custom;

import java.util.HashMap;
import java.util.Map;
import javax.management.InvalidAttributeValueException;
import weblogic.management.EncryptionHelper;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public final class JDBCDataSourceFactory extends ConfigurationMBeanCustomizer {
   private static final long serialVersionUID = 4371886555953356822L;

   public JDBCDataSourceFactory(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void setProperties(Map var1) throws InvalidAttributeValueException {
      if (var1 != null) {
         if (!weblogic.kernel.Kernel.isServer()) {
            this.putValue("Properties", var1);
         } else {
            HashMap var2 = new HashMap();
            var2.putAll(var1);
            String var3 = (String)var2.remove("password");
            this.putValue("Properties", var2);
            Object var4 = null;
            if (var3 != null) {
               byte[] var5 = EncryptionHelper.encryptString(var3);
               this.putValue("PasswordEncrypted", var5);
            }

         }
      }
   }
}
