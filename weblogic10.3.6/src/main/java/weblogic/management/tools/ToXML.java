package weblogic.management.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import weblogic.utils.Debug;

public class ToXML {
   public static final String TAG_DTD_ORDER = "@dtd-order";
   public static final String TAG_EMPTY = "@empty";
   public static final String TAG_CDATA = "@cdata";
   public static final String TAG_ATTRIBUTE = "@attribute";
   public static final String TAG_NAME = "@name";
   private static final int TAG_NONE = 0;
   private static final int TAG_OR = 1;
   private static final int TAG_OPTIONAL = 2;
   private static final int TAG_MANDATORY = 3;
   private static final String sp1 = "  ";
   private static final String sp2 = "    ";
   private static TagParser m_parser;
   private static boolean verbose = false;
   private static StringBuffer[] m_indent = new StringBuffer[]{new StringBuffer(""), new StringBuffer("  "), new StringBuffer("    "), new StringBuffer("      "), new StringBuffer("        "), new StringBuffer("          ")};

   public static String toXML(TagParser var0) {
      m_parser = var0;
      StringBuffer var1 = new StringBuffer("  public String toXML(int indentLevel) {");
      var1.append("\n");
      var1.append("    StringBuffer result = new StringBuffer();");
      var1.append("\n");
      TaggedMethod[] var2 = m_parser.getMethodsWithTag("@dtd-order");
      DTDMethod var3 = new DTDMethod(m_parser.getCurrentClassName());
      var1.append("    result.append(ToXML.indent(indentLevel)).append(\"").append("<" + var3.toElementName() + "\");\n");
      TaggedMethod[] var4 = m_parser.getMethodsWithTag("@attribute");

      int var5;
      for(var5 = 0; var5 < var4.length; ++var5) {
         StringBuffer var6 = new StringBuffer();
         String var7 = var4[var5].getTagValue("@name");
         if (var7 == null) {
            var7 = var4[var5].getFieldName();
         }

         String var8 = var4[var5].getGetMethodName();
         String var9 = var4[var5].getGetPrefix();
         var6.append("    if (isSet_" + DTDMethod.getFieldName(var8.substring(var9.length())) + ") {\n");
         var8 = var8 + "()";
         String var10 = var4[var5].getReturnType();
         if (var10.indexOf("XMLName") != -1) {
            var6.append("      result.append(\" xmlns:\").append(" + var8 + ".getPrefix()).append(\"=\\\"\").append(" + var8 + ".getNamespaceUri()).append(\"\\\" ");
            var6.append(var7).append("=\\\"\").append(" + var8 + ".getQualifiedName()).append(\"\\\"\");\n");
         } else {
            var6.append("      result.append(\" ").append(var7).append("=\\\"\").append(String.valueOf(" + var8 + ")).append(\"\\\"\");\n");
         }

         var6.append("    }\n");
         var1.append(var6);
      }

      var1.append("    result.append(\">\\n\");\n");

      for(var5 = 0; var5 < var2.length; ++var5) {
         Collection var14 = getMethodsWithDTDOrder(var2, var5);
         Iterator var15 = var14.iterator();
         if (var15.hasNext()) {
            DTDMethod var16 = (DTDMethod)var15.next();
            int var17 = var16.getDTDOrder();
            Iterator var18;
            DTDMethod var19;
            if (var16.isMandatory()) {
               var18 = var14.iterator();
               var19 = (DTDMethod)var18.next();
               if (var19.isBoolean()) {
                  var1.append(var19.toXML());
               } else {
                  var1.append(var19.toXMLIfNotNull("     "));
               }
            } else if (var16.isOptional()) {
               Debug.assertion(var14.size() > 0, "Not enough @dtd-order " + var16.getDTDOrder() + " tags in " + var0.getFileName());
               var18 = var14.iterator();
               var19 = (DTDMethod)var18.next();
               var1.append(var19.toXMLIfNotNullAndNotDefault("     "));
            } else {
               Debug.assertion(var14.size() > 0, "Not enough @dtd-order " + var16.getDTDOrder() + " tags in " + var0.getFileName());
               var18 = var14.iterator();
               int var11 = 0;

               DTDMethod var13;
               for(DTDMethod var12 = null; var18.hasNext(); var12 = var13) {
                  var13 = (DTDMethod)var18.next();
                  if (0 != var11++ && !var12.isArray()) {
                     var1.append("    else /*@*/ ");
                  }

                  var1.append(var13.toXMLForOr("  "));
               }
            }
         } else if (verbose) {
            System.out.println("Warning:" + var0.getFileName() + ":  couldn't find a method with @dtd-order " + var5);
         }
      }

      var1.append("    result.append(ToXML.indent(indentLevel)).append(\"").append("</" + var3.toElementName() + ">\\n\");\n");
      var1.append("    return result.toString();\n");
      var1.append("  }\n");
      return var1.toString();
   }

   public static StringBuffer indent(int var0) {
      if (var0 < m_indent.length * 2) {
         return m_indent[var0 / 2];
      } else {
         StringBuffer var1 = new StringBuffer("");

         for(int var2 = 0; var2 < var0 / 2; ++var2) {
            var1.append("  ");
         }

         return var1;
      }
   }

   public static String capitalize(String var0) {
      return Character.toUpperCase(var0.charAt(0)) + var0.substring(1);
   }

   public static void main(String[] var0) {
      String[] var1 = new String[]{"public void getSmallIconFileName()", "get-small-icon-file-name", "public void EJBJar()", "ejb-jar", "public void EJBQl()", "ejb-ql", "public void EJBJarMBean()", "ejb-jar", "public void providerURL()", "provider-url"};

      for(int var2 = 0; var2 < var1.length; var2 += 2) {
         DTDMethod var3 = new DTDMethod((TaggedMethod)null, var1[var2], "", (DefaultValue)null);
         Debug.assertion(var3.toElementName().equals(var1[var2 + 1]), var3.toElementName() + " != " + var1[var2 + 1]);
         Debug.say(var3.toElementName());
      }

   }

   public static String toElementName(String var0) {
      return DTDMethod.toElementName(var0);
   }

   private static Collection getMethodsWithDTDOrder(TaggedMethod[] var0, int var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var0.length; ++var3) {
         TaggedMethod var4 = var0[var3];
         String var5 = var4.getTagValue("@dtd-order");
         DefaultValue var6 = m_parser.getDefaultValue(var4.getMethodName());
         DTDMethod var7 = new DTDMethod(var0[var3], var4.getMethodSignature(), var5, var6);
         if (var1 == var7.getDTDOrder()) {
            var2.add(var7);
         }
      }

      return var2;
   }
}
