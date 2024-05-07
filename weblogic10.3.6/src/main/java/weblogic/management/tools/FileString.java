package weblogic.management.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

class FileString {
   private String[] m_file;
   private String m_fileName;
   private int m_current;

   public FileString(String var1) throws FileNotFoundException, IOException {
      this.m_fileName = var1;
      this.m_file = readFileInBuffer(var1);
      this.m_current = 0;
   }

   private static String[] readFileInBuffer(String var0) throws FileNotFoundException, IOException {
      File var2 = new File(var0);
      BufferedReader var3 = new BufferedReader(new FileReader(var2));
      ArrayList var4 = new ArrayList();

      for(String var5 = var3.readLine(); null != var5; var5 = var3.readLine()) {
         var4.add(var5);
      }

      String[] var1 = new String[var4.size()];
      Iterator var6 = var4.iterator();

      for(int var7 = 0; var7 < var4.size(); ++var7) {
         var1[var7] = (String)var6.next();
      }

      return var1;
   }

   public String getFileName() {
      return this.m_fileName;
   }

   public int getCurrentIndex() {
      return this.m_current;
   }

   public String getPackageName() {
      String var1 = null;
      String[] var2 = this.grep("package");
      if (var2.length > 0) {
         StringTokenizer var3 = new StringTokenizer(var2[0]);
         var3.nextToken();
         var1 = var3.nextToken();
         int var4 = var1.indexOf(";");
         var1 = var1.substring(0, var4);
      }

      return var1;
   }

   public String getImportClass(String var1) {
      String[] var2 = this.grep("import");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (-1 != var2[var3].indexOf(var1)) {
            StringTokenizer var4 = new StringTokenizer(var2[var3]);
            var4.nextToken();
            String var5 = var4.nextToken();
            int var6 = var5.indexOf(";");
            return var5.substring(0, var6);
         }
      }

      return null;
   }

   public String getSuperClassName() {
      String var1 = null;
      String var2 = new String();
      Integer[] var3 = this.grepByLineNumber("extends");
      if (var3.length > 0) {
         int var4 = var3[0];
         String var5 = this.readLine(var4++);

         for(var2 = var2 + var5; -1 == var5.indexOf("{"); var2 = var2 + " " + var5) {
            var5 = this.readLine(var4++);
         }

         StringTokenizer var6 = new StringTokenizer(var2);

         for(boolean var7 = false; !var7 && var6.hasMoreTokens(); var7 = var6.nextToken().equals("extends")) {
         }

         if (var6.hasMoreTokens()) {
            var1 = var6.nextToken();
         }
      }

      return var1;
   }

   public String toString() {
      return this.getFileName();
   }

   public String readAndAdvance() {
      String var1 = this.readCurrent();
      ++this.m_current;
      return var1;
   }

   public String readCurrent() {
      String var1 = this.m_file[this.m_current];
      return var1;
   }

   public String readLine(int var1) {
      return this.m_file[var1];
   }

   public void advance() {
      ++this.m_current;
   }

   public String[] grep(String var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.m_file.length; ++var3) {
         if (-1 != this.m_file[var3].indexOf(var1)) {
            var2.add(this.m_file[var3]);
         }
      }

      String[] var6 = new String[var2.size()];
      Iterator var4 = var2.iterator();

      for(int var5 = 0; var5 < var6.length; ++var5) {
         var6[var5] = (String)var4.next();
      }

      return var6;
   }

   public Integer[] grepByLineNumber(String var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.m_file.length; ++var3) {
         if (-1 != this.m_file[var3].indexOf(var1)) {
            var2.add(new Integer(var3));
         }
      }

      Integer[] var6 = new Integer[var2.size()];
      Iterator var4 = var2.iterator();

      for(int var5 = 0; var5 < var6.length; ++var5) {
         var6[var5] = (Integer)var4.next();
      }

      return var6;
   }

   public boolean eof() {
      return this.m_current >= this.m_file.length;
   }
}
