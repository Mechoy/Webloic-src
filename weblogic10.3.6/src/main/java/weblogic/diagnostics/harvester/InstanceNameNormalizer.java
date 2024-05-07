package weblogic.diagnostics.harvester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.management.ObjectName;
import weblogic.diagnostics.i18n.DiagnosticsTextTextFormatter;

public class InstanceNameNormalizer {
   private String instanceNameSpecification;
   private boolean regexPattern;
   private String normalizedName;
   private boolean objectNamePattern;

   public InstanceNameNormalizer(String var1) {
      this.instanceNameSpecification = var1;
   }

   public boolean isPattern() {
      if (this.instanceNameSpecification == null) {
         try {
            this.translateHarvesterSpec();
         } catch (InvalidHarvesterInstanceNameException var2) {
         }
      }

      return this.regexPattern || this.objectNamePattern;
   }

   public boolean isRegexPattern() {
      if (this.instanceNameSpecification == null) {
         try {
            this.translateHarvesterSpec();
         } catch (InvalidHarvesterInstanceNameException var2) {
         }
      }

      return this.regexPattern;
   }

   public boolean isObjectNamePattern() {
      if (this.instanceNameSpecification == null) {
         try {
            this.translateHarvesterSpec();
         } catch (InvalidHarvesterInstanceNameException var2) {
         }
      }

      return this.objectNamePattern;
   }

   public synchronized String translateHarvesterSpec() throws InvalidHarvesterInstanceNameException {
      if (this.instanceNameSpecification == null) {
         throw new InvalidHarvesterInstanceNameException(DiagnosticsTextTextFormatter.getInstance().getInvalidHarvesterInstanceNameText(this.instanceNameSpecification));
      } else {
         if (this.normalizedName == null) {
            boolean var1 = false;
            ObjectName var2 = null;

            try {
               var2 = new ObjectName(this.instanceNameSpecification);
               var1 = true;
               this.normalizedName = var2.getCanonicalName();
               this.objectNamePattern = var2.isPattern();
            } catch (Exception var4) {
               var1 = false;
            }

            if (!var1) {
               this.regexPattern = this.instanceNameSpecification.contains("*");
               if (!this.isPattern()) {
                  throw new InvalidHarvesterInstanceNameException(DiagnosticsTextTextFormatter.getInstance().getInvalidHarvesterInstanceNameText(this.instanceNameSpecification));
               }

               this.normalizedName = this.normalizePattern(this.instanceNameSpecification);
               this.normalizedName = this.normalizedName.replaceAll("\\[", "\\\\[");
               this.normalizedName = this.normalizedName.replaceAll("\\]", "\\\\]");
               this.normalizedName = this.normalizedName.replaceAll("\\*", "(.*?)");
            }
         }

         return this.normalizedName;
      }
   }

   private String normalizePattern(String var1) throws InvalidHarvesterInstanceNameException {
      this.scanInstanceName(this.instanceNameSpecification);
      String var2 = var1;
      String[] var3 = var1.split(":");
      if (var3.length == 2) {
         try {
            var2 = this.normalizeObjectNamePattern(var3[0], var3[1]);
         } catch (InvalidSequence var5) {
         }
      } else if (var3.length > 2) {
         throw new InvalidHarvesterInstanceNameException(this.instanceNameSpecification);
      }

      return var2;
   }

   private String normalizeObjectNamePattern(String var1, String var2) throws InvalidHarvesterInstanceNameException, InvalidSequence {
      if (var1 != null && var2 != null) {
         String var3 = "";
         String[] var4 = var2.split(",");
         ArrayList var5 = new ArrayList(var4.length);
         HashMap var6 = new HashMap(var4.length);
         boolean var7 = false;
         String[] var8 = var4;
         int var9 = var4.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            String var11 = var8[var10];
            if (var11 != null) {
               var11 = var11.trim();
               String[] var12 = var11.split("=");
               if (var12.length < 2) {
                  throw new InvalidSequence();
               }

               String var13 = var12[0];
               if (var13 == null || var13.length() == 0) {
                  throw new InvalidSequence();
               }

               var13 = var13.trim();
               if (var13.contains("*")) {
                  throw new InvalidSequence();
               }

               var5.add(var13);
               var6.put(var13, var11);
            }
         }

         Collections.sort(var5);
         String var14 = "";

         for(var9 = 0; var9 < var5.size(); ++var9) {
            String var15 = (String)var5.get(var9);
            var14 = var14 + (String)var6.get(var15);
            if (var9 < var5.size() - 1) {
               var14 = var14 + ",";
            }
         }

         var3 = var1 + ":" + var14;
         if (var7) {
            var3 = var3 + ",*";
         }

         return var3;
      } else {
         throw new InvalidHarvesterInstanceNameException(this.instanceNameSpecification);
      }
   }

   private void scanInstanceName(String var1) throws InvalidHarvesterInstanceNameException {
      if (var1 != null && var1.length() != 0) {
         char[] var2 = new char[var1.length()];
         var1.getChars(0, var1.length(), var2, 0);
         char[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            char var6 = var3[var5];
            if (!this.isValidChar(var6)) {
               throw new InvalidHarvesterInstanceNameException(var1);
            }
         }

      } else {
         throw new InvalidHarvesterInstanceNameException(var1);
      }
   }

   private boolean isValidChar(char var1) {
      return Character.isLetterOrDigit(var1) || var1 == ':' || var1 == '.' || var1 == '=' || var1 == '*' || var1 == '_' || var1 == ',' || var1 == '\\' || var1 == '?' || var1 == '"' || var1 == '[' || var1 == ']';
   }

   public static void main(String[] var0) {
      String[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         System.out.println("Normalizing name: " + var4);

         try {
            InstanceNameNormalizer var5 = new InstanceNameNormalizer(var4);
            String var6 = var5.translateHarvesterSpec();
            System.out.println("Normalized name: " + var6);
         } catch (InvalidHarvesterInstanceNameException var7) {
            System.out.println("Normalization failed: " + var7.getMessage());
            var7.printStackTrace();
         }
      }

   }
}
