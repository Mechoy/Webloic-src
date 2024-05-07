package weblogic.diagnostics.context;

import java.util.HashSet;

public final class DiagnosticContextHelper implements DiagnosticContextConstants {
   private static DyeInfo[] dyeInfos = new DyeInfo[]{new DyeInfo("ADDR1", 1L), new DyeInfo("ADDR2", 2L), new DyeInfo("ADDR3", 4L), new DyeInfo("ADDR4", 8L), new DyeInfo("USER1", 16L), new DyeInfo("USER2", 32L), new DyeInfo("USER3", 64L), new DyeInfo("USER4", 128L), new DyeInfo("COOKIE1", 256L), new DyeInfo("COOKIE2", 512L), new DyeInfo("COOKIE3", 1024L), new DyeInfo("COOKIE4", 2048L), new DyeInfo("EXECQ1", 4096L), new DyeInfo("EXECQ2", 8192L), new DyeInfo("EXECQ3", 16384L), new DyeInfo("EXECQ4", 32768L), new DyeInfo("THREADGROUP1", 65536L), new DyeInfo("THREADGROUP2", 131072L), new DyeInfo("THREADGROUP3", 262144L), new DyeInfo("THREADGROUP4", 524288L), new DyeInfo("PROTOCOL_T3", 1048576L), new DyeInfo("PROTOCOL_HTTP", 2097152L), new DyeInfo("PROTOCOL_RMI", 4194304L), new DyeInfo("PROTOCOL_SOAP", 8388608L), new DyeInfo("PROTOCOL_IIOP", 16777216L), new DyeInfo("PROTOCOL_JRMP", 33554432L), new DyeInfo("PROTOCOL_SSL", 67108864L), new DyeInfo("CONNECTOR1", 134217728L), new DyeInfo("CONNECTOR2", 268435456L), new DyeInfo("CONNECTOR3", 536870912L), new DyeInfo("CONNECTOR4", 1073741824L), new DyeInfo("THROTTLE", 4294967296L), new DyeInfo("JFR_THROTTLE", 8589934592L), new DyeInfo("DYE_0", 72057594037927936L), new DyeInfo("DYE_1", 144115188075855872L), new DyeInfo("DYE_2", 288230376151711744L), new DyeInfo("DYE_3", 576460752303423488L), new DyeInfo("DYE_4", 1152921504606846976L), new DyeInfo("DYE_5", 2305843009213693952L), new DyeInfo("DYE_6", 4611686018427387904L), new DyeInfo("DYE_7", Long.MIN_VALUE)};

   public static String getContextId() {
      DiagnosticContext var0 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
      return var0 != null ? var0.getContextId() : null;
   }

   public static void setDye(byte var0, boolean var1) throws InvalidDyeException {
      if (var0 >= 56 && var0 <= 63) {
         DiagnosticContext var2 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
         if (var2 != null) {
            var2.setDye(var0, var1);
         }

      } else {
         throw new InvalidDyeException("Invalid dye index " + var0);
      }
   }

   public static boolean isDyedWith(byte var0) throws InvalidDyeException {
      DiagnosticContext var1 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
      return var1 != null ? var1.isDyedWith(var0) : false;
   }

   public static String getPayload() {
      DiagnosticContext var0 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
      return var0 != null ? var0.getPayload() : null;
   }

   public static void setPayload(String var0) {
      DiagnosticContext var1 = DiagnosticContextFactory.findOrCreateDiagnosticContext();
      if (var1 != null) {
         var1.setPayload(var0);
      }

   }

   public static long parseDyeMask(String var0) {
      if (var0 == null) {
         return 0L;
      } else {
         String[] var1 = var0.split(",");
         return parseDyeMask(var1);
      }
   }

   public static long parseDyeMask(String[] var0) {
      long var1 = 0L;
      int var3 = var0 != null ? var0.length : 0;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var0[var4].trim();
         var1 |= getDyeValue(var5);
      }

      return var1;
   }

   public static String[] getDyeFlagNames() {
      String[] var0 = new String[dyeInfos.length];

      for(int var1 = 0; var1 < dyeInfos.length; ++var1) {
         var0[var1] = dyeInfos[var1].dyeName;
      }

      return var0;
   }

   public static void validateDyeFlagNames(String[] var0) {
      if (var0 != null && var0.length != 0) {
         HashSet var1 = new HashSet();

         int var2;
         for(var2 = 0; var2 < dyeInfos.length; ++var2) {
            var1.add(dyeInfos[var2].dyeName);
         }

         for(var2 = 0; var2 < var0.length; ++var2) {
            if (!var1.contains(var0[var2])) {
               throw new IllegalArgumentException("Dye name is invalid " + var0[var2]);
            }
         }

      }
   }

   private static long getDyeValue(String var0) {
      for(int var1 = 0; var1 < dyeInfos.length; ++var1) {
         if (var0.equals(dyeInfos[var1].dyeName)) {
            return dyeInfos[var1].dyeValue;
         }
      }

      return 0L;
   }

   public static synchronized void registerDye(String var0, int var1) throws InvalidDyeException {
      if (var1 >= 0 && var1 <= 63) {
         long var2 = 1L << var1;
         int var4 = dyeInfos.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            DyeInfo var6 = dyeInfos[var5];
            if (var0.equals(var6.dyeName)) {
               throw new InvalidDyeException("Dye " + var0 + " is already defined");
            }

            if (var6.dyeValue == var2) {
               throw new InvalidDyeException("Dye " + var6.dyeName + " is already defined as dye " + var1);
            }
         }

         DyeInfo[] var7 = new DyeInfo[var4 + 1];
         System.arraycopy(dyeInfos, 0, var7, 0, var4);
         var7[var4] = new DyeInfo(var0, var2);
         dyeInfos = var7;
      } else {
         throw new InvalidDyeException("Invalid dye index " + var1);
      }
   }

   private static class DyeInfo {
      String dyeName;
      long dyeValue;

      DyeInfo(String var1, long var2) {
         this.dyeName = var1;
         this.dyeValue = var2;
      }
   }
}
