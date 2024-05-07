package weblogic.nodemanager.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import weblogic.nodemanager.NodeManagerTextTextFormatter;

public class NMProperties extends Properties implements Comparator {
   TreeMap lineInfoMap;
   HashMap nameToLineMap;
   Matcher varMatcher;
   Pattern varPattern;
   Matcher varOnlyMatcher;
   Pattern varOnlyPattern;
   Matcher contMatcher;
   Pattern contPattern;
   private static final long serialVersionUID = -9039140023562390804L;
   String varPatternString;
   String varOnlyPatternString;
   String contPatternString;
   private static final int FAB_LINE_BASE = 1073741824;
   int nextFabLine;
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   private void resetMaps() {
      this.lineInfoMap.clear();
      this.nameToLineMap.clear();
      this.nextFabLine = 1073741824;
   }

   public NMProperties() {
      this((Properties)null);
   }

   public NMProperties(Properties var1) {
      super(var1);
      this.varPatternString = "^                      # Match beginning of line\n\\s*                   # Match leading whitespaces (not captured)\n(\\w+)                 # The property name (made of word chars)\n\\s*                   # Followed by optional whitespaces\n(?:[=:]|\\s)           # =, : or whitespace as delimiter\n\\s*                 # More optional whitespace\n(.*?)                  # Match till end of line\n(\\\\?)                  # With an optional continuation\n$                      # End of line\n";
      this.varOnlyPatternString = "^                      # Match beginning of line\n\\s*                   # Match leading whitespaces (not captured)\n(\\w+)                 # The property name (made of word chars)\n\\s*                   # Followed by optional whitespaces\n$                      # End of line\n";
      this.contPatternString = "^                      # Match beginning of line\n\\s*                   # Match leading whitespaces (not captured)\n(.*?)                  # Match any character, relucatant\n(\\\\?)                  # Optional continuation\n$                      # End of line\n";
      this.nextFabLine = 1073741824;
      this.lineInfoMap = new TreeMap(this);
      this.nameToLineMap = new HashMap();
      this.nextFabLine = 1073741824;
      this.varPattern = Pattern.compile(this.varPatternString, 4);
      this.varOnlyPattern = Pattern.compile(this.varOnlyPatternString, 4);
      this.contPattern = Pattern.compile(this.contPatternString, 4);
   }

   public int getIntProperty(String var1, int var2) throws NumberFormatException {
      String var3 = this.getProperty(var1);

      try {
         return var3 != null ? Integer.parseInt(var3) : var2;
      } catch (NumberFormatException var5) {
         throw new NumberFormatException(nmText.getInvalidIntProperty(var1));
      }
   }

   public boolean isTrue(String var1, boolean var2) {
      String var3 = this.getProperty(var1);
      return var3 != null ? "true".equalsIgnoreCase(var3) : var2;
   }

   public boolean isTrue(String var1) {
      return "true".equalsIgnoreCase(this.getProperty(var1));
   }

   public synchronized void setProperty(String var1, String var2, Integer var3) {
      this.setProperty(var1, var2);
      if (var3 == null) {
         var3 = new Integer(this.nextFabLine);
         ++this.nextFabLine;
      }

      this.nameToLineMap.put(var1, var3);
      LineInfo var4 = new LineInfo();
      var4.setName(var1);
      this.lineInfoMap.put(var3, var4);
   }

   public synchronized void saveWithComments(OutputStream var1, String var2) throws IOException {
      BufferedWriter var3 = new BufferedWriter(new OutputStreamWriter(var1));
      Set var4 = this.keySet();
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         if (this.nameToLineMap.get(var6) == null) {
            Integer var7 = new Integer(this.nextFabLine);
            ++this.nextFabLine;
            this.nameToLineMap.put(var6, var7);
            LineInfo var8 = new LineInfo();
            var8.setName(var6);
            this.lineInfoMap.put(var7, var8);
         }
      }

      if (var2 != null) {
         var3.write("#" + var2);
         var3.newLine();
      }

      var3.write("#" + (new Date()).toString());
      var3.newLine();
      var4 = this.lineInfoMap.keySet();

      for(var5 = var4.iterator(); var5.hasNext(); var3.newLine()) {
         Integer var10 = (Integer)var5.next();
         LineInfo var11 = (LineInfo)this.lineInfoMap.get(var10);
         String var12 = var11.getName();
         if (var12 == null) {
            var3.write(var11.getRawLine());
         } else {
            var3.write(var12 + "=");
            String var9 = (String)this.get(var12);
            if (var9 != null) {
               var3.write(var9);
            }
         }
      }

      var3.close();
   }

   public synchronized void loadWithComments(InputStream var1) throws IOException {
      LineNumberReader var2 = new LineNumberReader(new InputStreamReader(var1));
      boolean var4 = false;
      String var5 = null;
      String var6 = null;
      String var7 = null;
      this.resetMaps();

      while(true) {
         while(true) {
            while(true) {
               String var3;
               while((var3 = var2.readLine()) != null) {
                  int var8 = var2.getLineNumber();
                  if (!var4) {
                     this.varMatcher = this.varPattern.matcher(var3);
                     if (this.varMatcher.matches()) {
                        var5 = this.varMatcher.group(1);
                        var6 = this.varMatcher.group(2);
                        var7 = this.varMatcher.group(3);
                        if (var7 != null && var7.length() != 0) {
                           var4 = true;
                        } else {
                           this.setProperty(var5, var6, new Integer(var8));
                           var5 = null;
                           var6 = null;
                           var4 = false;
                        }
                     } else {
                        this.varOnlyMatcher = this.varOnlyPattern.matcher(var3);
                        if (this.varOnlyMatcher.matches()) {
                           var5 = this.varOnlyMatcher.group(1);
                           var6 = "";
                           this.setProperty(var5, var6, new Integer(var8));
                        } else {
                           LineInfo var10 = new LineInfo();
                           var10.setRawLine(var3);
                           this.lineInfoMap.put(new Integer(var8), var10);
                        }
                     }
                  } else {
                     this.contMatcher = this.contPattern.matcher(var3);
                     if (this.contMatcher.matches()) {
                        String var9 = this.contMatcher.group(1);
                        var7 = this.contMatcher.group(2);
                        var6 = var6 + var9;
                        if (var7 != null && var7.length() != 0) {
                           var4 = true;
                        } else {
                           this.setProperty(var5, var6, new Integer(var8));
                           var4 = false;
                        }
                     } else {
                        assert false : "Pattern matching error.  Line should have been matched!!";
                     }
                  }
               }

               return;
            }
         }
      }
   }

   public void loadWithComments(File var1) throws IOException {
      FileInputStream var2 = new FileInputStream(var1);
      this.loadWithComments((InputStream)var2);
      var2.close();
   }

   public void load(File var1) throws IOException {
      FileInputStream var2 = new FileInputStream(var1);
      this.load(var2);
      var2.close();
   }

   public void saveWithComments(File var1, String var2) throws IOException {
      FileOutputStream var3 = new FileOutputStream(var1);

      try {
         this.saveWithComments((OutputStream)var3, var2);
      } finally {
         var3.close();
      }

   }

   public void saveWithComments(File var1) throws IOException {
      this.saveWithComments((File)var1, (String)null);
   }

   public void save(File var1, String var2) throws IOException {
      FileOutputStream var3 = new FileOutputStream(var1);

      try {
         this.store(var3, var2);
      } finally {
         var3.close();
      }

   }

   public void save(File var1) throws IOException {
      this.save(var1, (String)null);
   }

   public int compare(Object var1, Object var2) {
      Integer var3 = (Integer)var1;
      Integer var4 = (Integer)var2;
      return var3.compareTo(var4);
   }

   private static class LineInfo {
      String name;
      String rawLine;

      public LineInfo(String var1, String var2) {
         this.name = var1;
         this.rawLine = var2;
      }

      public LineInfo() {
         this((String)null, (String)null);
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public void setRawLine(String var1) {
         this.rawLine = var1;
      }

      public String getName() {
         return this.name;
      }

      public String getRawLine() {
         return this.rawLine;
      }
   }
}
