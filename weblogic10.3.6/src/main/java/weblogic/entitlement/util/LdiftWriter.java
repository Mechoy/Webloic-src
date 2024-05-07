package weblogic.entitlement.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import weblogic.entitlement.data.EResource;
import weblogic.entitlement.data.ERole;
import weblogic.entitlement.data.ERoleId;
import weblogic.entitlement.data.ldap.EData;
import weblogic.entitlement.expression.EExpression;
import weblogic.entitlement.parser.Parser;
import weblogic.utils.encoders.BASE64Encoder;

public class LdiftWriter {
   private static Escaping escaper;
   private static BASE64Encoder b64Encoder;
   private static final String ROLE_TYPE = "ERole";
   private static final String RESOURCE_TYPE = "EResource";
   private static final String PREDICATE_TYPE = "EPredicate";
   private static final String PROFILE_TYPE = "EProfile";
   private static final String PROF_DEF_TYPE = "EProfileDefinition";
   private static final String PROF_INST_TYPE = "EProfileInstance";
   private static String[] TYPES;
   private Writer out;

   public LdiftWriter(Writer var1) {
      if (var1 == null) {
         throw new NullPointerException("null writer");
      } else {
         this.out = var1;
      }
   }

   public void writeHeader() throws IOException {
      this.out.write("dn: dc=@domain@\n");
      this.out.write("objectclass: top\n");
      this.out.write("objectclass: domain\n");
      this.out.write("dc: @domain@\n");
      this.out.write("\n");
      this.out.write("dn: ou=@realm@,dc=@domain@\n");
      this.out.write("objectclass: top\n");
      this.out.write("objectclass: organizationalUnit\n");
      this.out.write("ou: @realm@\n");

      for(int var1 = 0; var1 < TYPES.length; ++var1) {
         this.out.write("\n");
         this.out.write("dn: ou=" + TYPES[var1] + ",ou=@realm@,dc=@domain@\n");
         this.out.write("objectclass: top\n");
         this.out.write("objectclass: organizationalUnit\n");
         this.out.write("ou: " + TYPES[var1] + "\n");
      }

   }

   public void write(EResource var1) throws IOException {
      String var2 = escaper.escapeString(var1.getName());
      this.write("EResource", var2, var1.getExpression(), (String)null);
   }

   public void write(ERole var1, String var2) throws IOException {
      String var3 = EData.PK2Name((ERoleId)var1.getPrimaryKey());
      this.write("ERole", var3, var1.getExpression(), var2);
   }

   public void writePredicate(String var1) throws IOException {
      this.write("EPredicate", var1);
   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public void close() throws IOException {
      this.out.close();
   }

   public void writeln() throws IOException {
      this.out.write(10);
   }

   private void write(String var1, String var2) throws IOException {
      this.out.write("\n");
      this.out.write("dn: cn=" + var2 + ",ou=" + var1 + ",ou=@realm@,dc=@domain@\n");
      this.out.write("objectclass: top\n");
      this.out.write("objectclass: " + var1 + "\n");
      this.out.write("cn: " + var2 + "\n");
   }

   private void write(String var1, String var2, EExpression var3, String var4) throws IOException {
      this.write(var1, var2);
      this.out.write("EExpr:: " + uuEncode(var3) + "\n");
      if (var4 != null && var4.length() > 0) {
         this.out.write("EAux:: " + uuEncode(var4) + "\n");
      }

   }

   private static String uuEncode(EExpression var0) throws UnsupportedEncodingException {
      String var1 = var0 == null ? "" : var0.serialize();
      return b64Encoder.encodeBuffer(var1.getBytes("UTF8"));
   }

   private static String uuEncode(String var0) throws UnsupportedEncodingException {
      String var1 = var0 == null ? "" : var0;
      return b64Encoder.encodeBuffer(var1.getBytes("UTF8"));
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length != 2) {
         System.out.println("LDIFT Writer: Convert XML doc describing resource and role policies to LDIFT used in WLS7.x bootstrap");
         System.out.println("Usage: java weblogic.entitlement.util.LdiftWriter <xml_in_file> <ldift_out_file>");
         throw new IllegalArgumentException("Number of arguments should be 2");
      } else {
         String var1 = var0[0];
         String var2 = var0[1];
         XMLProcessor var3 = null;
         FileInputStream var4 = null;
         LdiftWriter var5 = null;

         try {
            var4 = new FileInputStream(var1);
            var3 = new XMLProcessor(var4);
         } catch (FileNotFoundException var9) {
            System.out.println("Cannot find xml file: " + var1);
            throw new IllegalArgumentException("Invalid xml file: " + var1);
         } catch (Exception var10) {
            System.out.println("Failed to read policies from xml file: " + var1 + ". Make sure file xml format is valid\n" + var10.getMessage());
            throw new IllegalArgumentException("Invalid xml file: " + var1);
         }

         try {
            var5 = new LdiftWriter(new FileWriter(var2));
         } catch (Exception var8) {
            System.out.println("Failed to open output file: " + var2);
            throw new IllegalArgumentException("Invalid output file: " + var2);
         }

         try {
            var5.writeHeader();
            var3.writeElements(var5);
            var5.close();
            var4.close();
         } catch (Exception var7) {
            System.out.println("Failed to convert policies to ldift format. Make sure " + var1 + " contains valid policies.\n" + var7.getMessage());
            throw new IllegalArgumentException("Invalid xml file: " + var1);
         }
      }
   }

   public static void unitTest(String var0) throws Exception {
      EResource[] var1 = new EResource[]{new EResource("myresource:1", (EExpression)null), new EResource("myresource:1", Parser.parseResourceExpression("Usr(me)"))};
      ERole[] var2 = new ERole[]{new ERole("myresource:1", "myrole@1", Parser.parseRoleExpression("Grp(me)")), new ERole("myresource:1", "myrole@1", Parser.parseRoleExpression("Grp(me)"))};
      String[] var3 = new String[]{"weblogic.entitlement.rules.TimePredicate", "weblogic.entitlement.rules.InDevelopmentMode"};
      LdiftWriter var4 = new LdiftWriter(new FileWriter(var0));
      var4.writeHeader();

      int var5;
      for(var5 = 0; var5 < var1.length; ++var5) {
         var4.write(var1[var5]);
      }

      for(var5 = 0; var5 < var2.length; ++var5) {
         var4.write(var2[var5], "aux");
      }

      for(var5 = 0; var5 < var3.length; ++var5) {
         var4.writePredicate(var3[var5]);
      }

      var4.close();
   }

   static {
      escaper = EData.escaper;
      b64Encoder = new BASE64Encoder();
      TYPES = new String[]{"ERole", "EResource", "EPredicate", "EProfile", "EProfileDefinition", "EProfileInstance"};
   }
}
