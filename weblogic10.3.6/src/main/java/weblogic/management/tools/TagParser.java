package weblogic.management.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import weblogic.utils.Debug;
import weblogic.utils.compiler.CodeGenerationException;

public class TagParser {
   private List m_listener = new ArrayList();
   private String m_fileName;
   private List m_classTags = new ArrayList();
   private List m_taggedMethods = new ArrayList();
   private HashMap m_defaultValues = new HashMap();

   public TagParser(String var1) {
      this.m_fileName = var1;
   }

   public static void main(String[] var0) {
      try {
         TagParser var1 = new TagParser(var0[0]);
         var1.parse();
         p("============= " + var0[0]);
         p("CLASSTAGS:");
         Iterator var2 = var1.getClassTags();

         while(var2.hasNext()) {
            p("   " + var2.next().toString());
         }

         p("");
         p("METHOD TAGS:");
         var2 = var1.getTaggedMethods();

         while(var2.hasNext()) {
            TaggedMethod var3 = (TaggedMethod)var2.next();
            p("   METHOD:" + var3.getMethodName());
            String[] var4 = var3.getTags();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               p("      " + var4[var5]);
            }
         }

         p("");
         p("METHODS WITH A DEFAULT TAG:");
         TaggedMethod[] var7 = var1.getMethodsWithTag("@default");

         for(int var8 = 0; var8 < var7.length; ++var8) {
            p("   " + var7[var8].getMethodName());
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   private static void p(String var0) {
      Debug.say("@@@ " + var0);
   }

   public String getFileName() {
      return this.m_fileName;
   }

   public String getCurrentClassName() {
      int var1 = this.m_fileName.lastIndexOf("/") + 1;
      if (var1 <= 0) {
         var1 = this.m_fileName.lastIndexOf("\\") + 1;
      }

      if (var1 <= 0) {
         var1 = 0;
      }

      int var2 = this.m_fileName.indexOf(".java");
      return this.m_fileName.substring(var1, var2);
   }

   public DefaultValue getDefaultValue(String var1) {
      Debug.assertion(0 == var1.indexOf("get") || 0 == var1.indexOf("is"), "Illegal getDefaultValue for non-getter:" + var1 + " file:" + this.m_fileName);
      return (DefaultValue)this.m_defaultValues.get(var1);
   }

   public TaggedMethod[] getMethodsWithTag(String var1) {
      HashMap var2 = new HashMap();
      if (-1 == var1.indexOf("@")) {
         var1 = "@" + var1;
      }

      Iterator var3 = this.m_taggedMethods.iterator();

      while(var3.hasNext()) {
         TaggedMethod var4 = (TaggedMethod)var3.next();
         String[] var5 = var4.getTags();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (-1 != var5[var6].indexOf(var1) && null == var2.get(var4.getMethodSignature())) {
               var2.put(var4.getMethodSignature(), var4);
            }
         }
      }

      Object[] var7 = var2.values().toArray();
      TaggedMethod[] var8 = new TaggedMethod[var7.length];
      System.arraycopy(var7, 0, var8, 0, var7.length);
      return var8;
   }

   public Iterator getTaggedMethods() {
      return this.m_taggedMethods.iterator();
   }

   public Iterator getClassTags() {
      return this.m_classTags.iterator();
   }

   public void parse() throws IOException {
      FileString var1 = new FileString(this.m_fileName);
      this.parseClassTags(var1);
      this.parseMethodTags(var1);
      this.initDefaultValues();
      String[] var2 = this.getSuperClassFileNames(this.m_fileName);

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1 = new FileString(var2[var3]);

         for(String var4 = var1.readAndAdvance(); -1 == var4.indexOf("{"); var4 = var1.readAndAdvance()) {
         }

         this.parseMethodTags(var1);
      }

      if (!this.isAbstractClass()) {
         this.validateMethodTags();
      }

   }

   public String locateFileFromPackage(String var1) {
      String var2 = null;
      String var3 = var1.replace('.', File.separatorChar) + ".java";
      File var4 = new File(var3);
      if (var4.exists()) {
         var2 = var3;
      }

      return var2;
   }

   private String[] getSuperClassFileNames(String var1) throws FileNotFoundException, IOException {
      ArrayList var2 = new ArrayList();
      FileString var3 = new FileString(var1);
      String var4 = var3.getSuperClassName();

      while(null != var4 && -1 != var4.indexOf("MBean") && -1 == var4.indexOf("WeblogicMBean")) {
         String var5 = var3.getImportClass(var4);
         String var6 = var3.getPackageName();
         String var7 = null;
         if (null != var5) {
            var7 = this.locateFileFromPackage(var5);
         } else {
            var7 = this.locateFileFromPackage(var6 + "." + var4);
            if (null == var7) {
               var7 = this.locateFileFromPackage(var4);
            }
         }

         if (null != var7) {
            var2.add(var7);
            var3 = new FileString(var7);
            var4 = var3.getSuperClassName();
         } else {
            var4 = null;
         }
      }

      String[] var8 = new String[var2.size()];
      Iterator var9 = var2.iterator();

      for(int var10 = 0; var10 < var8.length; ++var10) {
         var8[var10] = (String)var9.next();
      }

      return var8;
   }

   private boolean isNewMethod(List var1, TaggedMethod var2) {
      Iterator var3 = var1.iterator();

      TaggedMethod var4;
      do {
         if (!var3.hasNext()) {
            return true;
         }

         var4 = (TaggedMethod)var3.next();
      } while(!var4.getMethodName().equals(var2.getMethodName()));

      return false;
   }

   private boolean isAbstractClass() {
      Iterator var1 = this.m_classTags.iterator();

      String var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (String)var1.next();
      } while(!"@abstract".equals(var2));

      return true;
   }

   private void parseMethodTags(FileString var1) throws IOException {
      String var2 = var1.readAndAdvance();
      int var3 = -1;

      while(!var1.eof()) {
         while(!var1.eof() && -1 == var2.indexOf("(") && -1 == var2.indexOf("/**")) {
            var2 = var1.readAndAdvance();
         }

         if (!var1.eof() && var2.indexOf("/**") > 0) {
            var3 = var1.getCurrentIndex();
         } else if (!var1.eof() && var2.indexOf("(") > 0 && var2.indexOf("*") == -1 && -1 != var3) {
            List var4 = this.parseTags(var1, var3, var1.getCurrentIndex());
            Iterator var6 = var4.iterator();
            ArrayList var7 = new ArrayList();

            while(var6.hasNext()) {
               String var8 = var6.next().toString();
               var7.add(var8);
            }

            TaggedMethod var9 = new TaggedMethod(var2, var7);
            this.m_taggedMethods.add(var9);
            var3 = -1;
         }

         if (!var1.eof()) {
            var2 = var1.readAndAdvance();
         }
      }

   }

   private void parseClassTags(FileString var1) throws IOException {
      while(-1 == var1.readCurrent().indexOf("interface") && -1 == var1.readCurrent().indexOf("/**")) {
         var1.advance();
      }

      if (-1 != var1.readCurrent().indexOf("/**")) {
         this.m_classTags = this.parseTags(var1);
         var1.advance();
      }

   }

   private List parseTags(FileString var1, int var2, int var3) throws IOException {
      ArrayList var4 = new ArrayList();

      for(int var5 = var2; var5 < var3; ++var5) {
         String var6 = var1.readLine(var5);
         int var7 = var6.indexOf("@");
         if (var7 != -1) {
            var4.add(var6.substring(var7));
         }
      }

      return var4;
   }

   private List parseTags(FileString var1) throws IOException {
      ArrayList var2 = new ArrayList();

      for(String var3 = var1.readAndAdvance(); !var1.eof() && -1 == var3.indexOf("*/"); var3 = var1.readAndAdvance()) {
         int var4 = var3.indexOf("@");
         if (var4 != -1) {
            var2.add(var3.substring(var4));
         }
      }

      return var2;
   }

   private void initDefaultValues() {
      try {
         TaggedMethod[] var1 = this.getMethodsWithTag("@default");

         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2].getTagValue("@default");
            String var4 = var1[var2].getReturnType();
            String var5 = var1[var2].getMethodName();
            DefaultValue var6 = null;
            if (!"String".equals(var4) && !"java.lang.String".equals(var4)) {
               if ("boolean".equals(var4)) {
                  var6 = new DefaultValue(Boolean.valueOf(var3).toString(), true);
               } else {
                  if (!"int".equals(var4) && !"Integer".equals(var4) && !"java.lang.Integer".equals(var4)) {
                     throw new CodeGenerationException("Can't set default value for " + var1[var2].getMethodName() + ". Its type isn't supported");
                  }

                  try {
                     var6 = new DefaultValue(Integer.valueOf(var3).toString(), true);
                  } catch (NumberFormatException var8) {
                     throw new CodeGenerationException("Error parsing int for " + var1[var2].getMethodName());
                  }
               }
            } else {
               var6 = new DefaultValue(var3, false);
            }

            this.m_defaultValues.put(var5, var6);
         }
      } catch (CodeGenerationException var9) {
         var9.printStackTrace();
      }

   }

   private void validateMethodTags() {
      TaggedMethod[] var1 = this.getMethodsWithTag("@dtd-order");
      TaggedMethod[] var2 = new TaggedMethod[var1.length];
      int var3 = 0;

      int var4;
      for(var4 = 0; var4 < var1.length; ++var4) {
         String var5 = var1[var4].getTagValue("@dtd-order");
         int var6 = var5.length();
         int var7 = var5.indexOf("|");
         int var8 = var5.indexOf("?");
         if (var7 != -1 && var8 != -1 && var7 > var8) {
            throw new RuntimeException(this.m_fileName + ": Error with dtd-order tag for method" + var1[var4].getMethodName() + " tag:" + var5 + ". The | must always come before the ?");
         }

         if (var7 != -1) {
            var6 = var7;
         } else if (var8 != -1) {
            var6 = var8;
         }

         Debug.assertion(-1 != var6, var1[var4].getMethodName() + " tag:" + var5);
         int var9 = new Integer(var5.substring(0, var6));
         if (var9 > var1.length - 1) {
            throw new RuntimeException(this.m_fileName + ": The dtd-order for method " + var1[var4].getMethodSignature() + " is too high.  Make sure the dtd-order in this interface is correct");
         }

         if (var2[var4] != null) {
            if (var7 == -1 || var2[var4].getTagValue("@dtd-order").indexOf("|") == -1) {
               throw new RuntimeException(this.m_fileName + ": The dtd-order for method " + var1[var4].getMethodSignature() + " is incorrect.  The method " + var2[var4].getMethodSignature() + "is also declared with a dtd-order of " + var9 + ".  You must fix the ordering of these methods or declare them with the '|' (or) tag.");
            }

            ++var3;
         } else {
            var2[var4] = var1[var4];
         }
      }

      var4 = 0;

      for(int var10 = 0; var10 < var2.length; ++var10) {
         if (var2[var10] == null) {
            ++var4;
         }
      }

      if (var4 != var3) {
         throw new RuntimeException(this.m_fileName + ": The dtd-order values for some methods in this class are incorrect.");
      }
   }
}
