package weblogic.servlet.utils;

public class URLMatchMap implements URLMapping {
   protected static final int ARRAY_SIZE = 257;
   protected URLMatchNode[] nodes;
   protected URLExtensionNode[] exts;
   protected Object defaultObject;
   protected Object jspObject;
   private boolean caseInsensitive;
   private boolean extnCaseInsensitive;
   static String[][] mappings = new String[][]{{"/foo/*", "FooServlet"}, {"/foo/bar/*", "FooBarServlet"}, {"/baz/*", "BazServlet"}, {"*.html", "FileServlet"}, {"*.jsp", "JSPServlet"}, {"*.class", "ClasspathServlet"}, {"*.x", "SingleCharExtn"}, {"*.yz", "TwoCharExtn"}, {"/", "DefaultServlet"}, {"foo2/*", "FooServlet2"}, {"foo2/bar2/*", "FooBarServlet2"}, {"baz2/*", "BazServlet2"}, {"boom/*", "BoomServlet"}, {"/common/*", "Common1"}, {"/commonxyz/pdf/gy/*", "Common1"}, {"/common/kjh/*", "Common2"}, {"/commonjkhjk/*", "Common3"}, {"/commo/*", "Common4"}, {"/a/*", "a"}, {"/aa/*", "aa"}, {"/aaa/*", "aaa"}, {"/specviol/*", "SpecWildCard"}, {"/specviol", "SpecExact"}};
   static String[][] tests = new String[][]{{"/foo/xxx", "FooServlet"}, {"/baz/xxx", "BazServlet"}, {"/foo/bar/xxxx", "FooBarServlet"}, {"/foo/xxx.html", "FooServlet"}, {"/qqq/yyy/ttt.html", "FileServlet"}, {"/qqq/yyy/ttt", "DefaultServlet"}, {"/qqq/jjj.jsp", "JSPServlet"}, {"/qqq/jjj.class", "ClasspathServlet"}, {"/foo2/xxx", "FooServlet2"}, {"/foo2/bar2/xxxx", "FooBarServlet2"}, {"/baz2/xxx", "BazServlet2"}, {"foo2/xxx", "FooServlet2"}, {"foo2/bar2/xxxx", "FooBarServlet2"}, {"baz2/xxx", "BazServlet2"}, {"/commonxyz/pdf/gy/*", "Common1"}, {"/common/kjh/*", "Common2"}, {"/commonjkhjk/*", "Common3"}, {"/commo/*", "Common4"}, {"/blah.yz", "TwoCharExtn"}, {"/blah.x", "SingleCharExtn"}, {"/a", "a"}, {"/a/", "a"}, {"/a/x", "a"}, {"/a/xx", "a"}, {"/a/xxx", "a"}, {"/a/xxxx", "a"}, {"/a/xxxxx", "a"}, {"/aa", "aa"}, {"/aa/", "aa"}, {"/aa/x", "aa"}, {"/aa/xx", "aa"}, {"/aa/xxx", "aa"}, {"/aa/xxxx", "aa"}, {"/aa/xxxxx", "aa"}, {"/aaa", "aaa"}, {"/aaa/", "aaa"}, {"/aaa/x", "aaa"}, {"/aaa/xx", "aaa"}, {"/aaa/xxx", "aaa"}, {"/aaa/xxxx", "aaa"}, {"/aaa/xxxxx", "aaa"}, {"boom", "BoomServlet"}, {"boom/", "BoomServlet"}, {"boom/x", "BoomServlet"}, {"boom/xx", "BoomServlet"}, {"boom/xxx", "BoomServlet"}, {"boom/xxxx", "BoomServlet"}, {"/home", "DefaultServlet"}, {"/foo", "FooServlet"}, {"/specviol/foo", "SpecWildCard"}, {"/specviol/", "SpecWildCard"}, {"/specviol", "SpecExact"}};
   static String[] removes = new String[]{"/foo/bar/*", "foo2/*", "boom/*", "*.jsp", "*.class", "/"};
   static String[][] tests1 = new String[][]{{"/foo/xxx", "FooServlet"}, {"/baz/xxx", "BazServlet"}, {"/foo/bar/xxxx", "FooServlet"}, {"/foo/xxx.html", "FooServlet"}, {"/qqq/yyy/ttt.html", "FileServlet"}, {"/qqq/yyy/ttt", "null"}, {"/qqq/jjj.jsp", "null"}, {"/qqq/jjj.class", "null"}, {"/foo2/xxx", "null"}, {"/foo2/bar2/xxxx", "FooBarServlet2"}, {"/baz2/xxx", "BazServlet2"}, {"foo2/xxx", "null"}, {"foo2/bar2/xxxx", "FooBarServlet2"}, {"baz2/xxx", "BazServlet2"}, {"boom/xxx", "null"}, {"/commonxyz/pdf/gy/*", "Common1"}, {"/common/kjh/*", "Common2"}, {"/commonjkhjk/*", "Common3"}, {"/commo/*", "Common4"}, {"/home", "null"}, {"/foo", "FooServlet"}};

   public URLMatchMap() {
      this((Object)null, false, false);
   }

   public URLMatchMap(Object var1, boolean var2) {
      this(var1, var2, var2);
   }

   public URLMatchMap(Object var1, boolean var2, boolean var3) {
      this.nodes = null;
      this.exts = null;
      this.caseInsensitive = false;
      this.extnCaseInsensitive = false;
      this.defaultObject = var1;
      this.caseInsensitive = var2;
      this.extnCaseInsensitive = var3;
      this.nodes = new URLMatchNode[257];
      this.exts = new URLExtensionNode[0];
   }

   public void setDefault(Object var1) {
      this.defaultObject = var1;
   }

   public Object getDefault() {
      return this.defaultObject;
   }

   public boolean isCaseInsensitive() {
      return this.caseInsensitive;
   }

   public void setCaseInsensitive(boolean var1) {
      this.caseInsensitive = var1;
   }

   public boolean isExtensionCaseInsensitive() {
      return this.extnCaseInsensitive;
   }

   public void setExtensionCaseInsensitive(boolean var1) {
      this.extnCaseInsensitive = var1;
   }

   public void put(String var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException("null pattern put");
      } else if (var2 == null) {
         throw new NullPointerException("null object put");
      } else {
         if (var1.length() < 1 || var1.equals("/")) {
            var1 = "/*";
         }

         if (var1.startsWith("*.")) {
            this.putByExtension(var1, var2);
         } else {
            if (var1.charAt(0) != '/') {
               var1 = "/" + var1;
            }

            if (!var1.equals("/") && !var1.equals("/*") && !var1.equals("*")) {
               if (var1.length() != 0 && var1.charAt(0) == '/') {
                  if (this.caseInsensitive) {
                     var1 = var1.toLowerCase();
                  }

                  int var3 = hashPath(var1);
                  URLMatchNode var4 = new URLMatchNode(var1, var2, var3);
                  int var5 = (var3 & Integer.MAX_VALUE) % 257;
                  URLMatchNode var6 = this.nodes[var5];
                  if (var6 == null) {
                     this.nodes[var5] = var4;
                  } else if (var4.len >= var6.len) {
                     if (var4.len == var6.len && var4.exact == var6.exact && var4.pattern.equals(var6.pattern)) {
                        var6.val = var2;
                     } else {
                        this.nodes[var5] = var4;
                        var4.next = var6;
                     }
                  } else {
                     while(var6.next != null) {
                        if (var4.len >= var6.next.len) {
                           if (var4.len == var6.next.len && var4.exact == var6.next.exact && var4.pattern.equals(var6.next.pattern)) {
                              var6.next.val = var2;
                              return;
                           }

                           var4.next = var6.next;
                           var6.next = var4;
                           return;
                        }

                        var6 = var6.next;
                     }

                     var6.next = var4;
                     var4.next = null;
                  }
               } else {
                  throw new IllegalArgumentException("bad URLMatchMap path: '" + var1 + "'");
               }
            } else {
               this.defaultObject = var2;
            }
         }
      }
   }

   private void putByExtension(String var1, Object var2) {
      if (var1.equals(".jsp")) {
         this.jspObject = var2;
      }

      if (var1.startsWith("*.") && var1.length() >= 3) {
         var1 = var1.substring(2);
         if (this.caseInsensitive) {
            var1 = var1.toLowerCase();
         }

         int var3 = this.exts.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            if (var1.equals(this.exts[var4].ext)) {
               this.exts[var4] = new URLExtensionNode(var1, this.caseInsensitive || this.extnCaseInsensitive, var2);
               return;
            }
         }

         URLExtensionNode[] var5 = new URLExtensionNode[var3 + 1];
         System.arraycopy(this.exts, 0, var5, 0, var3);
         this.exts = var5;
         this.exts[var3] = new URLExtensionNode(var1, this.caseInsensitive || this.extnCaseInsensitive, var2);
      } else {
         throw new IllegalArgumentException("bad URLMath extension pattern: '" + var1 + "'");
      }
   }

   public Object get(String var1) {
      if (var1 != null && var1.length() != 0 && !var1.equals("/")) {
         if (var1.charAt(0) != '/') {
            StringBuffer var2 = new StringBuffer(var1.length() + 1);
            var2.append('/').append(var1);
            var1 = var2.toString();
         }

         if (this.caseInsensitive) {
            var1 = var1.toLowerCase();
         }

         Object var3 = this.getByPath(var1);
         if (var3 != null) {
            return var3;
         } else if (var1.endsWith(".jsp") && this.jspObject != null) {
            return this.jspObject;
         } else {
            var3 = this.getByExtension(var1);
            return var3 != null ? var3 : this.defaultObject;
         }
      } else {
         return this.defaultObject;
      }
   }

   private Object getByPath(String var1) {
      int var2 = hashPath(var1);
      int var3 = (var2 & Integer.MAX_VALUE) % 257;
      int var4 = var1.length();
      URLMatchNode var5 = this.nodes[var3];
      return var5 != null ? var5.match(var1, var4, var2) : null;
   }

   private Object getByExtension(String var1) {
      int var2 = var1.lastIndexOf(46);
      if (var2 < 0) {
         return null;
      } else {
         int var3 = var1.length();
         if (var2 == var3 - 1) {
            return null;
         } else {
            int var4 = var2 + 1;
            int var5 = var3 - var4;
            int var6 = this.exts.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               if (this.exts[var7].match(var1, var4, var5)) {
                  return this.exts[var7].val;
               }
            }

            return null;
         }
      }
   }

   public void remove(String var1) {
      if (var1 == null) {
         throw new NullPointerException("null pattern put");
      } else if (var1.length() >= 1 && !var1.equals("/")) {
         if (var1.startsWith("*.")) {
            this.removeByExtension(var1);
         } else {
            if (var1.charAt(0) != '/') {
               var1 = "/" + var1;
            }

            if (var1.length() != 0 && var1.charAt(0) == '/') {
               if (this.caseInsensitive) {
                  var1 = var1.toLowerCase();
               }

               int var2 = hashPath(var1);
               int var3 = (var2 & Integer.MAX_VALUE) % 257;
               URLMatchNode var4 = this.nodes[var3];
               if (var4 != null) {
                  if (var1.endsWith("*")) {
                     var1 = var1.substring(0, var1.length() - 1);
                  }

                  if (var1.equals(var4.pattern)) {
                     this.nodes[var3] = var4.next;
                  } else {
                     while(var4.next != null) {
                        if (var1.equals(var4.next.pattern)) {
                           var4.next = var4.next.next;
                           return;
                        }

                        var4 = var4.next;
                     }

                  }
               }
            } else {
               throw new IllegalArgumentException("bad URLMatchMap path: '" + var1 + "'");
            }
         }
      } else {
         this.defaultObject = null;
      }
   }

   private void removeByExtension(String var1) {
      if (var1.equals(".jsp")) {
         this.jspObject = null;
      }

      if (var1.startsWith("*.") && var1.length() >= 3) {
         var1 = var1.substring(2);
         if (this.caseInsensitive) {
            var1 = var1.toLowerCase();
         }

         int var2 = this.exts.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (var1.equals(this.exts[var3].ext)) {
               int var4 = var2 - 1;

               for(int var5 = var3; var5 < var4; ++var5) {
                  this.exts[var5] = this.exts[var5 + 1];
               }

               URLExtensionNode[] var6 = new URLExtensionNode[var4];
               System.arraycopy(this.exts, 0, var6, 0, var4);
               this.exts = var6;
               return;
            }
         }

      } else {
         throw new IllegalArgumentException("bad URLMath extension pattern: '" + var1 + "'");
      }
   }

   private static int hashPath(String var0) {
      int var1 = var0.indexOf(47, 1);
      if (var1 != -1 && var1 < 4) {
         var0 = var0.substring(0, var1);
      }

      int var2 = var0.length();
      char var3 = 0;
      char var4 = 0;
      char var5 = 0;
      if (var2 > 1) {
         var3 = var0.charAt(1);
         if (var2 > 2) {
            var4 = var0.charAt(2);
            if (var2 > 3) {
               var5 = var0.charAt(3);
            }
         }
      }

      int var6 = var3 + var4 + var5 + var3 * var4;
      return var6;
   }

   public Object clone() {
      URLMatchMap var1 = new URLMatchMap(this.defaultObject, this.caseInsensitive, this.extnCaseInsensitive);
      var1.jspObject = this.jspObject;
      var1.nodes = new URLMatchNode[257];

      int var2;
      for(var2 = 0; var2 < 257; ++var2) {
         var1.nodes[var2] = this.nodes[var2];
      }

      int var3 = this.exts.length;
      var1.exts = new URLExtensionNode[var3];

      for(var2 = 0; var2 < var3; ++var2) {
         var1.exts[var2] = this.exts[var2];
      }

      return var1;
   }

   public int size() {
      int var1 = this.exts.length;

      for(int var2 = 0; var2 < 257; ++var2) {
         URLMatchNode var3 = this.nodes[var2];
         if (var3 != null) {
            ++var1;

            while(var3.next != null) {
               var3 = var3.next;
               ++var1;
            }
         }
      }

      if (this.defaultObject != null) {
         ++var1;
      }

      return var1;
   }

   public Object[] values() {
      int var1 = this.size();
      int var2 = 0;
      Object[] var3 = new Object[var1];
      if (this.defaultObject != null) {
         var3[var2++] = this.defaultObject;
      }

      int var4;
      for(var4 = 0; var4 < this.exts.length; ++var4) {
         var3[var2] = this.exts[var4].val;
         ++var2;
      }

      for(var4 = 0; var4 < 257; ++var4) {
         URLMatchNode var5 = this.nodes[var4];
         if (var5 != null) {
            var3[var2] = var5.val;
            ++var2;

            while(var5.next != null) {
               var5 = var5.next;
               var3[var2] = var5.val;
               ++var2;
            }
         }
      }

      return var3;
   }

   public String[] keys() {
      int var1 = this.size();
      String[] var2 = new String[var1];
      int var3 = 0;
      if (this.defaultObject != null) {
         var2[var3++] = "";
      }

      int var4;
      for(var4 = 0; var4 < this.exts.length; ++var4) {
         var2[var3] = "*." + this.exts[var4].ext;
         ++var3;
      }

      for(var4 = 0; var4 < 257; ++var4) {
         URLMatchNode var5 = this.nodes[var4];
         if (var5 != null) {
            var2[var3] = var5.pattern;
            ++var3;

            while(var5.next != null) {
               var5 = var5.next;
               var2[var3] = var5.pattern;
               ++var3;
            }
         }
      }

      return var2;
   }

   public static void main(String[] var0) {
      URLMatchMap var1 = new URLMatchMap((Object)null, false);
      System.err.println("### RUN 1 ###");

      int var2;
      for(var2 = 0; var2 < mappings.length; ++var2) {
         var1.put(mappings[var2][0], mappings[var2][1]);
      }

      Object var3;
      for(var2 = 0; var2 < tests.length; ++var2) {
         var3 = var1.get(tests[var2][0]);
         if (!tests[var2][1].equals(var3)) {
            System.err.println("FAILED: expected '" + tests[var2][0] + "'->'" + tests[var2][1] + "' got '" + var3 + "'");
         } else {
            System.err.println("SUCCESS: '" + tests[var2][0] + "'->'" + tests[var2][1] + "'");
         }
      }

      System.err.println("### RUN 2 ###");

      for(var2 = 0; var2 < removes.length; ++var2) {
         var1.remove(removes[var2]);
      }

      for(var2 = 0; var2 < tests1.length; ++var2) {
         var3 = var1.get(tests1[var2][0]);
         if (!tests1[var2][1].equals("" + var3)) {
            System.err.println("FAILED: expected '" + tests1[var2][0] + "'->'" + tests1[var2][1] + "' got '" + var3 + "'");
         } else {
            System.err.println("SUCCESS: '" + tests1[var2][0] + "'->'" + tests1[var2][1] + "'");
         }
      }

   }

   public class URLMatchNode {
      String pattern;
      int len;
      int hash;
      boolean exact;
      Object val;
      URLMatchNode next;

      URLMatchNode(String var2, Object var3, int var4) {
         this.val = var3;
         this.hash = var4;
         if (var2.length() != 0 && var2.charAt(0) == '/' && !var2.equals("/") && !var2.equals("/*")) {
            if (var2.endsWith("/*")) {
               this.exact = false;
               var2 = var2.substring(0, var2.length() - 1);
            } else {
               this.exact = true;
            }

            this.pattern = var2;
            this.len = this.pattern.length();
         } else {
            throw new IllegalArgumentException("bad URLMatchMap path: '" + var2 + "'");
         }
      }

      public Object match(String var1, int var2, int var3) {
         Object var4 = this.exactMatch(var1, var2, var3);
         return var4 != null ? var4 : this.wildCardMatch(var1, var2, var3);
      }

      Object exactMatch(String var1, int var2, int var3) {
         URLMatchNode var4 = this;

         do {
            if (var4.exact && var4.hash == var3 && var2 == var4.len && var1.equals(var4.pattern)) {
               return var4.val;
            }

            var4 = var4.next;
         } while(var4 != null);

         return null;
      }

      Object wildCardMatch(String var1, int var2, int var3) {
         URLMatchNode var4 = this;

         do {
            if (!var4.exact && var4.hash == var3) {
               if (var2 >= var4.len) {
                  if (var1.startsWith(var4.pattern)) {
                     return var4.val;
                  }
               } else {
                  if (var2 == var4.len - 1 && var4.pattern.endsWith("/") && var4.pattern.startsWith(var1)) {
                     return var4.val;
                  }

                  if (var2 == var4.len && var1.equals(var4.pattern)) {
                     return var4.val;
                  }
               }
            }

            var4 = var4.next;
         } while(var4 != null);

         return null;
      }
   }

   public class URLExtensionNode {
      String ext;
      char[] extChars;
      int len;
      boolean caseInsensitive;
      Object val;

      URLExtensionNode(String var2, boolean var3, Object var4) {
         this.caseInsensitive = var3;
         this.val = var4;
         if (this.caseInsensitive) {
            var2 = var2.toLowerCase();
         }

         this.ext = var2;
         this.extChars = this.ext.toCharArray();
         this.len = this.extChars.length;
      }

      boolean match(String var1, int var2, int var3) {
         if (this.len != var3) {
            return false;
         } else {
            int var4;
            int var5;
            if (!this.caseInsensitive) {
               var4 = 0;

               for(var5 = var2; var4 < this.len; ++var5) {
                  if (this.extChars[var4] != var1.charAt(var5)) {
                     return false;
                  }

                  ++var4;
               }
            } else {
               var4 = 0;

               for(var5 = var2; var4 < this.len; ++var5) {
                  if (Character.toLowerCase(this.extChars[var4]) != Character.toLowerCase(var1.charAt(var5))) {
                     return false;
                  }

                  ++var4;
               }
            }

            return true;
         }
      }
   }
}
