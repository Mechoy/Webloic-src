package weblogic.nodemanager.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.InvalidPropertiesFormatException;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.utils.net.InetAddressHelper;

public class NetworkInfo {
   private InetAddress beginIPRange;
   private InetAddress endIPRange;
   private String interfaceName;
   private String netMask;
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   public NetworkInfo(String var1, String var2, InetAddress var3, InetAddress var4) {
      this.init(var1, var2, var3, var4);
   }

   public NetworkInfo(String var1, String var2) {
      this.init(var1, var2, (InetAddress)null, (InetAddress)null);
   }

   private void init(String var1, String var2, InetAddress var3, InetAddress var4) {
      this.interfaceName = var1;
      this.netMask = var2;
      this.beginIPRange = var3;
      this.endIPRange = var4;
   }

   public boolean isNetworkInfoFor(InetAddress var1) {
      return this.beginIPRange == null && this.endIPRange == null || isGreaterThanEqual(var1, this.beginIPRange) && isGreaterThanEqual(this.endIPRange, var1);
   }

   private static boolean isGreaterThanEqual(InetAddress var0, InetAddress var1) {
      if (InetAddressHelper.isIPV6Address(var0.getHostAddress())) {
         return isGreaterThanEqualIPv6(var0, var1);
      } else {
         byte[] var2 = var0.getAddress();
         byte[] var3 = var1.getAddress();
         if (var2.length == var3.length) {
            for(int var4 = 0; var4 < var2.length; ++var4) {
               int var5 = var2[var4] & 255;
               int var6 = var3[var4] & 255;
               if (var5 > var6 || var4 == var2.length - 1) {
                  return true;
               }

               if (var5 < var6) {
                  break;
               }
            }
         }

         return false;
      }
   }

   private static boolean isGreaterThanEqualIPv6(InetAddress var0, InetAddress var1) {
      byte[] var2 = var0.getAddress();
      byte[] var3 = var1.getAddress();
      if (var2.length == var3.length) {
         for(int var4 = 0; var4 < var2.length - 1; var4 += 2) {
            int var5 = ((var2[var4] & 255) << 8) + (var2[var4 + 1] & 255);
            int var6 = ((var3[var4] & 255) << 8) + (var3[var4 + 1] & 255);
            if (var5 > var6 || var4 == var2.length - 2) {
               return true;
            }

            if (var5 < var6) {
               break;
            }
         }
      }

      return false;
   }

   public String getInterfaceName() {
      return this.interfaceName;
   }

   public String getNetMask() {
      return this.netMask;
   }

   public static NetworkInfo convertConfEntry(String var0, String var1) throws IOException {
      InetAddress var2 = null;
      InetAddress var3 = null;
      String var4 = null;
      String[] var5 = var1.split(",");

      assert var5.length < 2;

      if (var5.length <= 2 && var5.length >= 1) {
         String var6 = var5[0].trim();
         if (!var6.equals("*")) {
            String[] var7 = var6.split("-");
            if (var7.length != 2) {
               throw new InvalidPropertiesFormatException(nmText.unknownIPRange(var1));
            }

            var2 = InetAddress.getByName(var7[0]);
            var3 = InetAddress.getByName(var7[1]);
            if (!isGreaterThanEqual(var3, var2)) {
               var2 = InetAddress.getByName(var7[1]);
               var3 = InetAddress.getByName(var7[0]);
            }
         }

         if (var5.length == 2) {
            String var11 = var5[1].trim();
            int var8 = var11.indexOf("=");
            if (var8 > 0 && var8 < var11.length() - 1) {
               var4 = var11.substring(var11.indexOf("=") + 1);

               try {
                  var4 = InetAddress.getByName(var4).getHostAddress();
               } catch (UnknownHostException var10) {
                  throw new InvalidPropertiesFormatException(nmText.invalidNetMask(var4, var1) + var10.toString());
               }
            }
         }

         return new NetworkInfo(var0, var4, var2, var3);
      } else {
         throw new InvalidPropertiesFormatException(nmText.unknownIPRange(var1));
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.interfaceName);
      var1.append("=");
      var1.append(this.getPropertyValueString());
      return var1.toString();
   }

   public String getPropertyValueString() {
      StringBuffer var1 = new StringBuffer();
      if (this.beginIPRange == null && this.endIPRange == null) {
         var1.append("*");
      } else if (this.beginIPRange != null && this.endIPRange != null) {
         var1.append(this.beginIPRange.getHostAddress());
         var1.append("-");
         var1.append(this.endIPRange.getHostAddress());
      }

      if (this.netMask != null) {
         var1.append(",NetMask=");
         var1.append(this.netMask);
      }

      return var1.toString();
   }
}
