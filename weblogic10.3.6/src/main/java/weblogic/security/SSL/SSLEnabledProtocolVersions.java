package weblogic.security.SSL;

import java.util.ArrayList;
import java.util.Locale;

public class SSLEnabledProtocolVersions {
   private static final String SSLv2Hello = "SSLv2Hello";

   public static int getSSLContextDelegateProtocolVersions(String var0, LogListener var1) {
      if (null != var1 && var1.isDebugEnabled()) {
         var1.debug("supported protocol version modes: V2HELLO_SSL3_TLS1, TLS1_ONLY", (Throwable)null);
      }

      debug_givenMinProtocolVersion(var0, var1);
      if (null == var0) {
         throw new NullPointerException("Unexpected null minimumProtocolVersion.");
      } else if ("".equals(var0)) {
         if (null != var1) {
            var1.logUnsupportedMinimumProtocolVersion(var0, "SSLv2Hello");
         }

         debug_selectedProtocolMode(var1, "V2HELLO_SSL3_TLS1");
         return 3;
      } else {
         ProtocolVersion var2;
         try {
            var2 = new ProtocolVersion(var0);
         } catch (IllegalArgumentException var4) {
            debug_unableToInstantiateProtocolVersion(var0, "minimum", var1, var4);
            if (null != var1) {
               var1.logUnsupportedMinimumProtocolVersion(var0, "SSLv2Hello");
            }

            debug_selectedProtocolMode(var1, "V2HELLO_SSL3_TLS1");
            return 3;
         }

         if (var2.greaterThan(SSLEnabledProtocolVersions.ProtocolVersion.SSLV3)) {
            if (var2.greaterThan(SSLEnabledProtocolVersions.ProtocolVersion.TLSV1_0) && null != var1) {
               var1.logUnsupportedMinimumProtocolVersion(var0, SSLEnabledProtocolVersions.ProtocolVersion.TLSV1_0.toString());
            }

            debug_selectedProtocolMode(var1, "TLS1_ONLY");
            return 0;
         } else {
            if (null != var1) {
               var1.logUnsupportedMinimumProtocolVersion(var0, "SSLv2Hello");
            }

            debug_selectedProtocolMode(var1, "V2HELLO_SSL3_TLS1");
            return 3;
         }
      }
   }

   public static String[] getJSSEProtocolVersions(String var0, String[] var1, LogListener var2) throws IllegalArgumentException {
      if (null != var2 && var2.isDebugEnabled()) {
         var2.debug("supportedProtocolVersions=" + toString(var1), (Throwable)null);
      }

      if (null != var1 && 0 != var1.length) {
         debug_givenMinProtocolVersion(var0, var2);
         if (null == var0) {
            throw new NullPointerException("Unexpected null minimumProtocolVersion.");
         } else {
            boolean var4 = false;
            ProtocolVersion var3;
            if ("".equals(var0)) {
               var4 = true;
               var3 = SSLEnabledProtocolVersions.ProtocolVersion.SSLV3;
            } else {
               try {
                  var3 = new ProtocolVersion(var0);
               } catch (IllegalArgumentException var15) {
                  debug_unableToInstantiateProtocolVersion(var0, "minimum", var2, var15);
                  var4 = true;
                  var3 = SSLEnabledProtocolVersions.ProtocolVersion.SSLV3;
               }
            }

            ProtocolVersion var5 = null;
            ProtocolVersion var6 = null;
            ProtocolVersion var7 = null;
            ArrayList var8 = new ArrayList(3);
            String[] var9 = var1;
            int var10 = var1.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               String var12 = var9[var11];
               if (null != var12) {
                  ProtocolVersion var13;
                  try {
                     var13 = new ProtocolVersion(var12);
                  } catch (IllegalArgumentException var16) {
                     debug_unableToInstantiateProtocolVersion(var12, "provider-supported", var2, var16);
                     continue;
                  }

                  if (null == var7) {
                     if (var13.equals(var3)) {
                        var7 = var13;
                        addIfNotPresent(var8, var13);
                     } else if (var13.lessThan(var3)) {
                        if (null == var5) {
                           var5 = var13;
                        } else if (var13.greaterThan(var5)) {
                           var5 = var13;
                        }
                     } else {
                        if (null == var6) {
                           var6 = var13;
                        } else if (var13.lessThan(var6)) {
                           var6 = var13;
                        }

                        addIfNotPresent(var8, var13);
                     }
                  } else if (var13.greaterThan(var3)) {
                     addIfNotPresent(var8, var13);
                  }
               }
            }

            if (null == var7) {
               if (null != var5) {
                  if (null != var2) {
                     var2.logUnsupportedMinimumProtocolVersion(var0, var5.toString());
                  }

                  addIfNotPresent(var8, var5);
               } else if (null == var6) {
                  if (null != var2 && var2.isDebugEnabled()) {
                     var2.debug("nextHigherThanMin unexpectedly null. ", (Throwable)null);
                  }
               } else if (null != var2) {
                  var2.logUnsupportedMinimumProtocolVersion(var0, var6.toString());
               }
            } else if (var4 && null != var2) {
               var2.logUnsupportedMinimumProtocolVersion(var0, var7.toString());
            }

            return (String[])var8.toArray(new String[var8.size()]);
         }
      } else {
         throw new IllegalArgumentException("No supported SSL protocol versions.");
      }
   }

   private static void addIfNotPresent(ArrayList<String> var0, ProtocolVersion var1) {
      String var2 = var1.toString();
      if (!var0.contains(var2)) {
         var0.add(var2);
      }

   }

   static String toString(String[] var0) {
      if (null == var0) {
         return "null";
      } else {
         StringBuilder var1 = new StringBuilder(var0.length * 5);
         String[] var3 = var0;
         int var4 = var0.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (null != var6) {
               if (var1.length() > 0) {
                  var1.append(',');
               }

               var1.append(var6);
            }
         }

         return var1.toString();
      }
   }

   private static void debug_selectedProtocolMode(LogListener var0, String var1) {
      if (null != var0 && var0.isDebugEnabled()) {
         var0.debug("selected protocol version mode: " + var1, (Throwable)null);
      }

   }

   private static void debug_givenMinProtocolVersion(String var0, LogListener var1) {
      if (null != var1 && var1.isDebugEnabled()) {
         var1.debug("given minimumProtocolVersion=" + var0, (Throwable)null);
      }

   }

   private static void debug_unableToInstantiateProtocolVersion(String var0, String var1, LogListener var2, IllegalArgumentException var3) {
      if (null != var2 && var2.isDebugEnabled()) {
         var2.debug("Unable to instantiate ProtocolVersion for " + var1 + " protocol version " + var0 + ": " + var3.getMessage(), (Throwable)null);
      }

   }

   interface LogListener {
      boolean isDebugEnabled();

      void debug(String var1, Throwable var2);

      void logUnsupportedMinimumProtocolVersion(String var1, String var2);
   }

   static class ProtocolVersion {
      public static final ProtocolVersion SSLV3 = new ProtocolVersion("SSLv3");
      public static final ProtocolVersion TLSV1_0 = new ProtocolVersion("TLSv1.0");
      private static final byte SSLV3_ORDINAL = 0;
      private static final byte TLS_MAJOR_VERSION_ORDINAL_MULTIPLIER = 10;
      private final int ordinal;
      private final String originalString;

      ProtocolVersion(String var1) {
         if (null == var1) {
            throw new IllegalArgumentException("Null input string.");
         } else {
            this.originalString = var1;
            String var2 = var1.toLowerCase(Locale.US).trim();
            if (var2.equals("sslv3")) {
               this.ordinal = 0;
            } else {
               if (!var2.startsWith("tlsv")) {
                  throw new IllegalArgumentException("Unknown protocol: " + var1);
               }

               String var3 = var2.substring(4);
               if (var3.equals("1")) {
                  this.ordinal = 10;
               } else {
                  if (var3.length() < 3) {
                     throw new IllegalArgumentException("Bad prefix: " + var1);
                  }

                  int var4 = var3.indexOf(46);
                  if (-1 == var4) {
                     throw new IllegalArgumentException("Missing dot: " + var1);
                  }

                  String var5 = var3.substring(0, var4);
                  if (var5.length() != 1) {
                     throw new IllegalArgumentException("Only 1 digit major version supported: " + var1);
                  }

                  byte var6;
                  try {
                     var6 = Byte.valueOf(var5, 10);
                  } catch (NumberFormatException var11) {
                     throw new IllegalArgumentException("Major version not a number: " + var1);
                  }

                  if (var6 < 1 || var6 > 9) {
                     throw new IllegalArgumentException("Major version range 1-9: " + var1);
                  }

                  String var7 = var3.substring(var4 + 1);
                  if (var7.length() != 1) {
                     throw new IllegalArgumentException("Only 1 digit minor version supported: " + var1);
                  }

                  byte var8;
                  try {
                     var8 = Byte.valueOf(var7, 10);
                  } catch (NumberFormatException var10) {
                     throw new IllegalArgumentException("Minor version not a number: " + var1);
                  }

                  if (var8 < 0 || var8 > 9) {
                     throw new IllegalArgumentException("Minor version range 0-9: " + var1);
                  }

                  this.ordinal = var6 * 10 + var8;
               }
            }

         }
      }

      public String toString() {
         return this.originalString;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            ProtocolVersion var2 = (ProtocolVersion)var1;
            return this.ordinal == var2.ordinal;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.ordinal;
      }

      boolean lessThan(ProtocolVersion var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("Unexpected null ProtocolVersion.");
         } else {
            return this.ordinal < var1.ordinal;
         }
      }

      boolean greaterThan(ProtocolVersion var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("Unexpected null ProtocolVersion.");
         } else {
            return this.ordinal > var1.ordinal;
         }
      }
   }
}
