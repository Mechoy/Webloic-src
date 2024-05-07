package weblogic.management.tools;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class TaggedMethod {
   public String[] m_tags;
   public String m_methodSignature;
   public String m_methodName;
   public String m_returnType;

   public TaggedMethod(String var1, List var2) {
      this.m_methodSignature = var1;
      StringTokenizer var3 = new StringTokenizer(var1);
      this.m_returnType = var3.nextToken();
      if ("public".equals(this.m_returnType) || "private".equals(this.m_returnType) || "protected".equals(this.m_returnType)) {
         this.m_returnType = var3.nextToken();
      }

      this.m_methodName = var3.nextToken();
      this.m_tags = new String[var2.size()];
      Iterator var4 = var2.iterator();

      for(int var5 = 0; var5 < this.m_tags.length; ++var5) {
         this.m_tags[var5] = var4.next().toString();
      }

   }

   public String getReturnType() {
      return this.m_returnType;
   }

   public String getMethodName() {
      return this.m_methodName;
   }

   public String getMethodSignature() {
      return this.m_methodSignature;
   }

   public String getFieldName() {
      String var1 = this.trimAction();
      return ToXML.toElementName(var1);
   }

   public String getGetMethodName() {
      String var1 = this.trimAction();
      return this.getGetPrefix() + var1;
   }

   public String[] getTags() {
      return this.m_tags;
   }

   public String getTagValue(String var1) {
      for(int var2 = 0; var2 < this.m_tags.length; ++var2) {
         if (-1 != this.m_tags[var2].indexOf(var1)) {
            return this.m_tags[var2].substring(1 + var1.length());
         }
      }

      return null;
   }

   public boolean containsTag(String var1) {
      for(int var2 = 0; var2 < this.m_tags.length; ++var2) {
         if (this.m_tags[var2].indexOf(var1) >= 0) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      return "[TaggedMethod:" + this.getMethodSignature() + "]";
   }

   String getGetPrefix() {
      return this.m_methodName.startsWith("is") ? "is" : "get";
   }

   private String trimAction() {
      String var1 = this.m_methodName;
      if (this.m_methodName.startsWith("get")) {
         var1 = this.m_methodName.substring(3);
      } else if (this.m_methodName.startsWith("set")) {
         var1 = this.m_methodName.substring(3);
      } else if (this.m_methodName.startsWith("is")) {
         var1 = this.m_methodName.substring(2);
      } else if (this.m_methodName.startsWith("add")) {
         var1 = this.m_methodName.substring(3);
      } else if (this.m_methodName.startsWith("remove")) {
         var1 = this.m_methodName.substring(6);
      }

      var1 = var1.substring(0, var1.length() - 3);
      return var1;
   }
}
