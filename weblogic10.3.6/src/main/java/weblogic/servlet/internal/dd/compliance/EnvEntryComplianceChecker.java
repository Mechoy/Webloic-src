package weblogic.servlet.internal.dd.compliance;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.utils.ErrorCollectionException;

public class EnvEntryComplianceChecker extends BaseComplianceChecker {
   private static final String[] ENTRY_TYPES = new String[]{"java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.String", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double"};

   public void check(DeploymentInfo var1) throws ErrorCollectionException {
      WebAppBean var2 = var1.getWebAppBean();
      if (var2 != null) {
         EnvEntryBean[] var3 = var2.getEnvEntries();
         if (var3 != null) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               this.checkEnvEntry(var3[var4]);
            }

            this.checkForExceptions();
         }
      }
   }

   private void checkEnvEntry(EnvEntryBean var1) {
      if (var1 != null) {
         String var2 = var1.getEnvEntryType();
         String var3 = var1.getEnvEntryValue();
         if (var2 != null && isEntryTypeValid(var2)) {
            this.validateEntryValue(var2, var3);
         } else {
            this.addDescriptorError(this.fmt.INVALID_ENV_ENTRY_TYPE(var2));
         }
      }
   }

   private void validateEntryValue(String var1, String var2) {
      if (var1.equals("java.lang.Character")) {
         if (var2 == null || var2.length() > 2) {
            this.update(this.fmt.warning() + this.fmt.INVALID_ENV_ENTRY_VALUE(var1, var2));
         }
      } else {
         try {
            Class var3 = this.getClass().getClassLoader().loadClass(var1);
            Constructor var4 = var3.getConstructor(String.class);
            var4.newInstance(var2);
         } catch (ClassNotFoundException var5) {
         } catch (IllegalAccessException var6) {
         } catch (NoSuchMethodException var7) {
         } catch (InstantiationException var8) {
            this.addDescriptorError(this.fmt.INVALID_ENV_ENTRY_VALUE(var1, var2));
         } catch (InvocationTargetException var9) {
            this.addDescriptorError(this.fmt.INVALID_ENV_ENTRY_VALUE(var1, var2));
         }
      }

   }

   private static boolean isEntryTypeValid(String var0) {
      for(int var1 = 0; var1 < ENTRY_TYPES.length; ++var1) {
         if (ENTRY_TYPES[var1].equals(var0)) {
            return true;
         }
      }

      return false;
   }
}
