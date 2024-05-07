package weblogic.diagnostics.instrumentation;

import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import weblogic.diagnostics.harvester.AttributeNameNormalizer;

public class HarvesterAttributeNormalizer implements AttributeNameNormalizer {
   private static final String METHOD_INVOCATION_STATS_ATTR = "MethodInvocationStatistics";
   private static final String METHOD_ALLOCATION_STATS_ATTR = "MethodMemoryAllocationStatistics";
   private static final String REGEX_PATTERN = "(\\((.)*?\\))";
   private static final int KEY_COUNT = 4;

   public String getNormalizedAttributeName(String var1) {
      boolean var2 = false;
      if (var1.startsWith("MethodInvocationStatistics")) {
         var1 = var1.substring("MethodInvocationStatistics".length());
      } else if (var1.startsWith("MethodMemoryAllocationStatistics")) {
         var2 = true;
         var1 = var1.substring("MethodMemoryAllocationStatistics".length());
      }

      var1 = ensureRegexGroups(var1);
      HarvesterAttributeNormalizerLexer var3 = new HarvesterAttributeNormalizerLexer(new StringReader(var1));
      HarvesterAttributeNormalizerParser var4 = new HarvesterAttributeNormalizerParser(var3);

      String var5;
      try {
         var5 = var4.normalizeAttributeSpec();
      } catch (Exception var7) {
         throw new IllegalArgumentException("Invalid attribute spec " + var7.getMessage(), var7);
      }

      return var2 ? "MethodMemoryAllocationStatistics" + var5 : "MethodInvocationStatistics" + var5;
   }

   private static String ensureRegexGroups(String var0) {
      if (var0 == null) {
         var0 = "";
      }

      Pattern var1 = Pattern.compile("(\\((.)*?\\))");
      Matcher var2 = var1.matcher(var0);
      StringBuilder var3 = new StringBuilder();

      for(int var4 = 0; var4 < 4; ++var4) {
         if (var2.find()) {
            var3.append(var2.group());
         } else {
            var3.append("(*)");
         }
      }

      return var3.toString();
   }

   public String getPartiallyNormalizedAllocationAttributeName(String var1) {
      return this.getPartiallyNormalizedAttributeName("MethodMemoryAllocationStatistics", var1);
   }

   public String getPartiallyNormalizedInvocationAttributeName(String var1) {
      return this.getPartiallyNormalizedAttributeName("MethodInvocationStatistics", var1);
   }

   private String getPartiallyNormalizedAttributeName(String var1, String var2) {
      String var3 = var2;
      if (var2 == null) {
         var3 = "";
      }

      if (var3.startsWith(var1)) {
         var3 = var3.substring(var1.length());
      }

      Pattern var4 = Pattern.compile("(\\((.)*?\\))");
      Matcher var5 = var4.matcher(var3);
      StringBuilder var6 = new StringBuilder();

      for(int var7 = 0; var7 < 4 && var5.find(); ++var7) {
         String var8 = var5.group();
         switch (var7) {
            case 0:
               var8 = this.normalizeClassName(var8);
               break;
            case 1:
               var8 = this.normalizeMethodName(var8);
               break;
            case 2:
               var8 = this.normalizeMethodParams(var8);
               break;
            case 3:
               var8 = this.normalizeMethodStats(var8);
         }

         var6.append(var8);
      }

      return var1 + var6.toString();
   }

   private String normalizeClassName(String var1) {
      HarvesterAttributeNormalizerLexer var2 = new HarvesterAttributeNormalizerLexer(new StringReader(var1));
      HarvesterAttributeNormalizerParser var3 = new HarvesterAttributeNormalizerParser(var2);

      try {
         String var4 = var3.classNameKey();
         return var4;
      } catch (Exception var6) {
         throw new IllegalArgumentException("Invalid attribute spec " + var6.getMessage(), var6);
      }
   }

   private String normalizeMethodName(String var1) {
      HarvesterAttributeNormalizerLexer var2 = new HarvesterAttributeNormalizerLexer(new StringReader(var1));
      HarvesterAttributeNormalizerParser var3 = new HarvesterAttributeNormalizerParser(var2);

      try {
         String var4 = var3.methodNameKey();
         return var4;
      } catch (Exception var6) {
         throw new IllegalArgumentException("Invalid attribute spec " + var6.getMessage(), var6);
      }
   }

   private String normalizeMethodParams(String var1) {
      HarvesterAttributeNormalizerLexer var2 = new HarvesterAttributeNormalizerLexer(new StringReader(var1));
      HarvesterAttributeNormalizerParser var3 = new HarvesterAttributeNormalizerParser(var2);

      try {
         String var4 = var3.methodParamsKey();
         return var4;
      } catch (Exception var6) {
         throw new IllegalArgumentException("Invalid attribute spec " + var6.getMessage(), var6);
      }
   }

   private String normalizeMethodStats(String var1) {
      HarvesterAttributeNormalizerLexer var2 = new HarvesterAttributeNormalizerLexer(new StringReader(var1));
      HarvesterAttributeNormalizerParser var3 = new HarvesterAttributeNormalizerParser(var2);

      try {
         String var4 = var3.methodStatsKey();
         return var4;
      } catch (Exception var6) {
         throw new IllegalArgumentException("Invalid attribute spec " + var6.getMessage(), var6);
      }
   }

   public static void main(String[] var0) {
      HarvesterAttributeNormalizer var1 = new HarvesterAttributeNormalizer();
      String var2 = var1.getPartiallyNormalizedInvocationAttributeName(var0[0]);
      System.out.println(var2);
   }
}
