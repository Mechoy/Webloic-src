package weblogic.management.tools;

import java.util.Locale;
import java.util.StringTokenizer;
import weblogic.utils.Debug;

class DTDMethod implements Comparable {
   private TaggedMethod m_taggedMethod;
   private String m_signature;
   private String m_methodName;
   private String m_dtdTag;
   private String m_returnType;
   private DefaultValue m_defaultValue;

   public DTDMethod(TaggedMethod var1, String var2, String var3, DefaultValue var4) {
      this.m_taggedMethod = var1;
      this.m_signature = var2;
      this.m_dtdTag = var3;
      this.m_defaultValue = var4;
      StringTokenizer var5 = new StringTokenizer(var2);
      String var6 = var5.nextToken();
      this.m_returnType = var5.nextToken();
      this.m_methodName = var5.nextToken();
      int var7 = this.m_methodName.indexOf("(");
      if (var7 != -1) {
         this.m_methodName = this.m_methodName.substring(0, var7);
      }

   }

   public DTDMethod(String var1) {
      this.m_methodName = var1;
   }

   public static String toElementName(String var0) {
      String var1 = new String();
      int var2 = var0.indexOf("MBean");
      if (-1 != var2) {
         var0 = var0.substring(0, var2);
      }

      var0 = Character.toUpperCase(var0.charAt(0)) + var0.substring(1);

      int var4;
      for(int var3 = 0; var3 < var0.length(); var1 = var1 + var0.substring(var4, var3).toLowerCase(Locale.US)) {
         for(var4 = var3; var3 < var0.length() && Character.isUpperCase(var0.charAt(var3)); ++var3) {
         }

         if (var3 - var4 <= 1) {
            while(var3 < var0.length() && !Character.isUpperCase(var0.charAt(var3))) {
               ++var3;
            }
         } else {
            --var3;
         }

         if (var4 > 0 && var4 < var0.length() - 1) {
            var1 = var1 + "-";
         }
      }

      return var1;
   }

   static String getFieldName(String var0) {
      StringBuffer var1 = new StringBuffer(var0);
      var1.setCharAt(0, Character.toLowerCase(var1.charAt(0)));

      for(int var2 = 1; var2 < var0.length() && !Character.isLowerCase(var1.charAt(var2)) && (var2 + 1 >= var0.length() || !Character.isLowerCase(var1.charAt(var2 + 1))); ++var2) {
         var1.setCharAt(var2, Character.toLowerCase(var1.charAt(var2)));
      }

      return var1.toString();
   }

   private static String unprefixField(String var0, String var1) {
      if (0 != var1.indexOf(var0)) {
         throw new RuntimeException("Field '" + var1 + "' does not start with '" + var0 + "'");
      } else {
         int var2 = var0.length();
         char var3 = Character.toLowerCase(var1.charAt(var2));
         return var3 + var1.substring(var2 + 1);
      }
   }

   public TaggedMethod getTaggedMethod() {
      return this.m_taggedMethod;
   }

   public boolean isBoolean() {
      return -1 != this.m_returnType.indexOf("boolean");
   }

   public boolean isString() {
      return -1 != this.m_returnType.indexOf("tring");
   }

   public boolean isPrimitiveStrict() {
      return this.isBoolean() || -1 != this.m_returnType.indexOf("int");
   }

   public boolean isPrimitiveOrString() {
      return this.isPrimitiveStrict() || this.isString();
   }

   public String getDTDTag() {
      return this.m_dtdTag;
   }

   public String getMethodName() {
      return this.m_methodName;
   }

   public String getSignature() {
      return this.m_signature;
   }

   public String getReturnType() {
      return this.m_returnType;
   }

   public boolean isMBean() {
      return -1 != this.getReturnType().indexOf("MBean");
   }

   public boolean isOr() {
      return -1 != this.getDTDTag().indexOf("|");
   }

   public boolean isOptional() {
      return -1 != this.getDTDTag().indexOf("?");
   }

   public boolean isMandatory() {
      return !this.isOr() && !this.isOptional();
   }

   public boolean isArray() {
      return -1 != this.getSignature().indexOf("[]");
   }

   public int getDTDOrder() {
      String var1 = this.getDTDTag();
      int var2 = var1.length();
      if (this.isOr()) {
         var2 = var1.indexOf("|");
      } else if (this.isOptional()) {
         var2 = var1.indexOf("?");
      }

      Debug.assertion(-1 != var2, this.getMethodName() + " tag:" + var1);
      return new Integer(var1.substring(0, var2));
   }

   public int compareTo(Object var1) {
      DTDMethod var2 = (DTDMethod)var1;
      return this.getDTDTag().compareTo(var2.getDTDTag());
   }

   public String capitalizeIfBoolean(String var1) {
      String var2 = var1 + "()";
      if (this.isBoolean()) {
         var2 = "ToXML.capitalize(Boolean.valueOf(" + var2 + ").toString())";
      }

      return var2;
   }

   public StringBuffer toXMLIfNotNullAndNotDefault(String var1) {
      StringBuffer var2 = new StringBuffer();
      if (!this.isBoolean() && this.isPrimitiveOrString()) {
         if (null != this.m_defaultValue) {
            var2.append(var1).append("if (" + this.testForDefault() + ") {\n");
         }

         var2.append(var1).append("  ").append(this.toXML());
         if (null != this.m_defaultValue) {
            var2.append(var1).append("}\n");
         }
      } else {
         if (!this.isPrimitiveOrString()) {
            var2.append(var1).append("if (" + this.testForNull() + ") {\n");
         }

         if (null != this.m_defaultValue) {
            var2.append(var1).append("  ").append("if (" + this.testForDefault() + ") {\n");
         }

         var2.append(var1).append("    ").append(this.toXML());
         if (null != this.m_defaultValue) {
            var2.append(var1).append("  ").append("}\n");
         }

         if (!this.isPrimitiveOrString()) {
            var2.append(var1).append("}\n");
         }
      }

      return var2;
   }

   public StringBuffer toXMLForOr(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(var1).append("  if (" + this.testForNotNullForOr() + ") {\n");
      var2.append(var1).append("    ").append(this.toXML());
      var2.append(var1).append("  }\n");
      return var2;
   }

   public StringBuffer toXMLIfNotNull(String var1) {
      StringBuffer var2 = new StringBuffer();
      if (!this.isBoolean() && this.isPrimitiveOrString()) {
         var2.append(var1).append(this.toXML());
      } else {
         var2.append(var1).append("if (" + this.testForNull() + ") {\n");
         var2.append(var1).append("    ").append(this.toXML());
         var2.append(var1).append("}\n");
      }

      return var2;
   }

   public String toXML() {
      String var1 = "";
      boolean var2 = this.getTaggedMethod().containsTag("@cdata");
      boolean var3 = this.getTaggedMethod().containsTag("@empty");
      if (this.getMethodName().indexOf("get") != 0 && this.getMethodName().indexOf("is") != 0) {
         throw new RuntimeException("Illegal @dtd-order found on a  method that is not a getter:" + this.getMethodName());
      } else {
         String var4 = this.getMethodName().substring("get".length());
         if (0 == this.getMethodName().indexOf("is")) {
            var4 = this.getMethodName().substring("is".length());
         }

         if (!this.isMBean()) {
            String var5 = toElementName(var4);
            if (this.isArray()) {
               if ('s' == var5.charAt(var5.length() - 1)) {
                  var5 = var5.substring(0, var5.length() - 1);
               }

               var1 = var1 + "for (int i = 0; i < " + this.getMethodName() + "().length; i++) {\n";
               var1 = var1 + "        result";
               var1 = var1 + ".append(ToXML.indent(indentLevel + 2))";
               if (!var3) {
                  var1 = var1 + ".append(\"<" + var5 + ">\")";
                  var1 = var1 + ".append(" + this.wrap(var2, this.getMethodName() + "()[i]") + ")";
                  var1 = var1 + ".append(\"</" + var5 + ">\\n\");\n";
               } else {
                  var1 = var1 + ".append((" + this.getMethodName() + "() ? \"<" + var5 + "/>\\n\" : \"\")" + ");\n";
               }

               var1 = var1 + "    }\n";
            } else {
               if (this.isString()) {
                  var1 = var1 + "if (" + this.testForNull() + ") {\n    ";
               }

               var1 = var1 + "  result";
               var1 = var1 + ".append(ToXML.indent(indentLevel + 2))";
               if (!var3) {
                  String var6 = this.capitalizeIfBoolean(this.getMethodName());
                  var1 = var1 + ".append(\"<" + var5 + ">\")";
                  var1 = var1 + ".append(" + this.wrap(var2, var6) + ")";
                  var1 = var1 + ".append(\"</" + var5 + ">\\n\");\n";
               } else {
                  var1 = var1 + ".append((" + this.getMethodName() + "() ? \"<" + var5 + "/>\\n\" : \"\")" + ");\n";
               }

               if (this.isString()) {
                  var1 = var1 + "    }\n";
               }
            }
         } else if (this.isArray()) {
            var1 = var1 + "for (int i = 0; i < " + this.getMethodName() + "().length; i++) {\n";
            var1 = var1 + "          result.append(" + this.getMethodName() + "()[i].toXML(indentLevel + 2));\n";
            var1 = var1 + "}\n";
         } else {
            var1 = "result.append(" + this.getMethodName() + "().toXML(indentLevel + 2)).append(\"\\n\");\n";
         }

         return var1;
      }
   }

   public String testForNull() {
      return this.isBoolean() ? this.getMethodName() + "()" : "null != " + this.getMethodName() + "()";
   }

   public String toElementName() {
      return toElementName(this.getMethodName());
   }

   public String toString() {
      return "[Method:" + this.getMethodName() + " dtd:" + this.getDTDTag() + "]";
   }

   private String wrap(boolean var1, String var2) {
      String var3 = var2;
      if (var1) {
         var3 = "\"<![CDATA[\" + " + var2 + " + \"]]>\"";
      }

      return var3;
   }

   private String testForDefault() {
      String var1 = null;
      String var2 = null;
      if (this.getMethodName().startsWith("get")) {
         var1 = "get";
      } else if (this.getMethodName().startsWith("is")) {
         var1 = "is";
      }

      var2 = getFieldName(this.getMethodName().substring(var1.length()));
      return null != this.m_defaultValue ? "((isSet_" + var2 + ")" + " || " + " ! (" + this.m_defaultValue.testForEquality(this.getMethodName() + "())") + ")" : "";
   }

   private String testForNotNullForOr() {
      String var1 = null;
      String var2 = null;
      if (this.getMethodName().startsWith("get")) {
         var1 = "get";
      } else if (this.getMethodName().startsWith("is")) {
         var1 = "is";
      }

      var2 = getFieldName(this.getMethodName().substring(var1.length()));
      if (this.isPrimitiveOrString()) {
         return null != this.m_defaultValue ? "((isSet_" + var2 + ")" + " || " + " ! (" + this.m_defaultValue.testForEquality(this.getMethodName() + "())") + ")" : "isSet_" + var2;
      } else {
         return this.isBoolean() ? this.getMethodName() : this.testForNull();
      }
   }
}
