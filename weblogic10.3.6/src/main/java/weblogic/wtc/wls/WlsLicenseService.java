package weblogic.wtc.wls;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.intf.TCLicenseService;
import java.util.Properties;

public final class WlsLicenseService implements TCLicenseService {
   private static int myEncryptionLevel = 1;

   public WlsLicenseService() {
      this.updateInstalledEncryptionInfo();
   }

   public void shutdown(int var1) {
   }

   public boolean isTCLicensed() {
      return true;
   }

   public boolean updateLicenseInformation() {
      return true;
   }

   public int updateInstalledEncryptionInfo() {
      boolean var1 = ntrace.getTraceLevel() == 1000373;
      if (var1) {
         ntrace.doTrace("[/WlsLicenseService/updateInstalledEncryptionInfo/");
      }

      new Properties();
      if (var1) {
         ntrace.doTrace("]/WlsLicenseService/updateInstalledEncryptionInfo/10/GPE128");
      }

      myEncryptionLevel = 4;
      return 4;
   }

   public int getInstalledEncryption() {
      boolean var1 = ntrace.getTraceLevel() == 1000373;
      if (var1) {
         ntrace.doTrace("[/WlsLicenseService/getInstalledEncryption/");
         ntrace.doTrace("]/WlsLicenseService/getInstalledEncryption/10/" + myEncryptionLevel);
      }

      return myEncryptionLevel;
   }

   public int decideEncryptionLevel(int var1, int var2, int var3) {
      boolean var4 = ntrace.getTraceLevel() == 1000373;
      if (var4) {
         ntrace.doTrace("[/WlsLicenseService/decideEncryptionLevel(" + var2 + ", " + var3 + ")/");
      }

      switch (myEncryptionLevel) {
         case 1:
            if (var3 > 0) {
               var3 = 0;
            }
         case 4:
         default:
            break;
         case 32:
            if (var3 > 56) {
               var3 = 56;
            }
      }

      if (var2 > var3) {
         if (var4) {
            ntrace.doTrace("]/WlsLicenseService/decideEncryptionLevel/10/-1");
         }

         return -1;
      } else {
         int var5 = 1;
         if (var3 >= 128) {
            var5 |= 38;
         } else if (var3 >= 56) {
            var5 |= 34;
         } else if (var3 >= 40) {
            var5 |= 2;
         }

         if (var2 > 56) {
            var5 &= -36;
         } else if (var2 > 40) {
            var5 &= -4;
         } else if (var2 > 0) {
            var5 &= -2;
         }

         if (var4) {
            ntrace.doTrace("]/WlsLicenseService/decideEncryptionLevel/20/" + var5);
         }

         return var5;
      }
   }

   public int acceptEncryptionLevel(int var1, int var2, int var3, int var4) {
      boolean var5 = ntrace.getTraceLevel() == 1000373;
      if (var5) {
         ntrace.doTrace("[/WlsLicenseService/acceptEncryptionLevel/");
      }

      switch (myEncryptionLevel) {
         case 1:
            if (var3 > 0) {
               var3 = 0;
            }
         case 4:
         default:
            break;
         case 32:
            if (var3 > 56) {
               var3 = 56;
            }
      }

      if (var2 > var3) {
         if (var5) {
            ntrace.doTrace("]/WlsLicenseService/acceptEncryptionLevel/10/-1");
         }

         return -1;
      } else {
         int var6 = 1;
         if (var3 >= 128) {
            var6 |= 38;
         } else if (var3 >= 56) {
            var6 |= 34;
         } else if (var3 >= 40) {
            var6 |= 2;
         }

         if (var2 > 56) {
            var6 &= -36;
         } else if (var2 > 40) {
            var6 &= -4;
         } else if (var2 > 0) {
            var6 &= -2;
         }

         int var7 = var1 > 11 ? var4 : 1;
         var7 &= var6;
         if ((var7 & 4) != 0) {
            var7 = 4;
         } else if ((var7 & 32) != 0) {
            var7 = 32;
         } else if ((var7 & 2) != 0) {
            var7 = 2;
         }

         if (var5) {
            ntrace.doTrace("]/WlsLicenseService/acceptEncryptionLevel/20/" + var7);
         }

         return var7;
      }
   }
}
