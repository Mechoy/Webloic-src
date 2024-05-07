package weblogic.servlet.utils;

public class SimpleApacheURLMatchMap extends URLMatchMap {
   static String[][] mappings = new String[][]{{"/foo/*", "FooServlet"}, {"/foo/bar/*", "FooBarServlet"}, {"/baz/*", "BazServlet"}, {"*.html", "FileServlet"}, {"*.jsp", "JSPServlet"}, {"*.class", "ClasspathServlet"}, {"/", "DefaultServlet"}, {"foo2/*", "FooServlet2"}, {"foo2/bar2/*", "FooBarServlet2"}, {"baz2/*", "BazServlet2"}, {"boom/*", "BoomServlet"}, {"/common/*", "Common1"}, {"/commonxyz/pdf/gy/*", "Common1"}, {"/common/kjh/*", "Common2"}, {"/commonjkhjk/*", "Common3"}, {"/commo/*", "Common4"}, {"/a/*", "a"}, {"/aa/*", "aa"}, {"/aaa/*", "aaa"}, {"*.jws", "JWSServlet"}};
   static String[][] tests = new String[][]{{"/foo/xxx", "FooServlet"}, {"/baz/xxx", "BazServlet"}, {"/foo/bar/xxxx", "FooBarServlet"}, {"/foo/xxx.html", "FooServlet"}, {"/qqq/yyy/ttt.html", "FileServlet"}, {"/qqq/yyy/ttt", "DefaultServlet"}, {"/qqq/jjj.jsp", "JSPServlet"}, {"/qqq/jjj.class", "ClasspathServlet"}, {"/foo2/xxx", "FooServlet2"}, {"/foo2/bar2/xxxx", "FooBarServlet2"}, {"/baz2/xxx", "BazServlet2"}, {"foo2/xxx", "FooServlet2"}, {"foo2/bar2/xxxx", "FooBarServlet2"}, {"baz2/xxx", "BazServlet2"}, {"/commonxyz/pdf/gy/*", "Common1"}, {"/common/kjh/*", "Common2"}, {"/commonjkhjk/*", "Common3"}, {"/commo/*", "Common4"}, {"/a", "a"}, {"/a/", "a"}, {"/a/x", "a"}, {"/a/xx", "a"}, {"/a/xxx", "a"}, {"/a/xxxx", "a"}, {"/a/xxxxx", "a"}, {"/aa", "aa"}, {"/aa/", "aa"}, {"/aa/x", "aa"}, {"/aa/xx", "aa"}, {"/aa/xxx", "aa"}, {"/aa/xxxx", "aa"}, {"/aa/xxxxx", "aa"}, {"/aaa", "aaa"}, {"/aaa/", "aaa"}, {"/aaa/x", "aaa"}, {"/aaa/xx", "aaa"}, {"/aaa/xxx", "aaa"}, {"/aaa/xxxx", "aaa"}, {"/aaa/xxxxx", "aaa"}, {"boom", "BoomServlet"}, {"boom/", "BoomServlet"}, {"boom/x", "BoomServlet"}, {"boom/xx", "BoomServlet"}, {"boom/xxx", "BoomServlet"}, {"boom/xxxx", "BoomServlet"}, {"/foo", "FooServlet"}, {"/foo1.jws", "JWSServlet"}, {"/foo2.jws/blah/somemore", "JWSServlet"}};
   static String[] removes = new String[]{"/foo/bar/*", "foo2/*", "boom/*", "*.jsp", "*.class"};
   static String[][] tests1 = new String[][]{{"/foo/xxx", "FooServlet"}, {"/baz/xxx", "BazServlet"}, {"/foo/bar/xxxx", "FooServlet"}, {"/foo/xxx.html", "FooServlet"}, {"/qqq/yyy/ttt.html", "FileServlet"}, {"/qqq/yyy/ttt", "DefaultServlet"}, {"/qqq/jjj.jsp", "DefaultServlet"}, {"/qqq/jjj.class", "DefaultServlet"}, {"/foo2/xxx", "DefaultServlet"}, {"/foo2/bar2/xxxx", "FooBarServlet2"}, {"/baz2/xxx", "BazServlet2"}, {"foo2/xxx", "DefaultServlet"}, {"foo2/bar2/xxxx", "FooBarServlet2"}, {"baz2/xxx", "BazServlet2"}, {"boom/xxx", "DefaultServlet"}, {"/commonxyz/pdf/gy/*", "Common1"}, {"/common/kjh/*", "Common2"}, {"/commonjkhjk/*", "Common3"}, {"/commo/*", "Common4"}, {"/foo", "FooServlet"}};

   public void put(String var1, Object var2) {
      super.put(var1, var2);
   }

   public Object get(String var1) {
      Object var2 = super.get(var1);
      if (!var1.equals("/") && (var2 == null || var2.equals(this.defaultObject))) {
         boolean var3 = false;
         String var4 = var1;
         int var7;
         if ((var7 = var1.indexOf(".")) != -1) {
            String var5 = var1.substring(0, var7 + 1);
            String var6 = var1.substring(var7 + 1);
            if ((var7 = var6.indexOf("/")) != -1) {
               var6 = var6.substring(0, var7);
               var4 = var5 + var6;
            }

            return super.get(var4);
         }
      }

      return var2;
   }

   public Object clone() {
      SimpleApacheURLMatchMap var1 = new SimpleApacheURLMatchMap();
      var1.defaultObject = this.defaultObject;
      var1.setCaseInsensitive(this.isCaseInsensitive());
      var1.setExtensionCaseInsensitive(this.isExtensionCaseInsensitive());
      var1.jspObject = this.jspObject;
      var1.nodes = new URLMatchMap.URLMatchNode[257];

      int var2;
      for(var2 = 0; var2 < 257; ++var2) {
         var1.nodes[var2] = this.nodes[var2];
      }

      int var3 = this.exts.length;
      var1.exts = new URLMatchMap.URLExtensionNode[var3];

      for(var2 = 0; var2 < var3; ++var2) {
         var1.exts[var2] = this.exts[var2];
      }

      return var1;
   }

   public static void main(String[] var0) {
      SimpleApacheURLMatchMap var1 = new SimpleApacheURLMatchMap();
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
         if (!tests1[var2][1].equals(var3)) {
            System.err.println("FAILED: expected '" + tests1[var2][0] + "'->'" + tests1[var2][1] + "' got '" + var3 + "'");
         } else {
            System.err.println("SUCCESS: '" + tests1[var2][0] + "'->'" + tests1[var2][1] + "'");
         }
      }

   }
}
