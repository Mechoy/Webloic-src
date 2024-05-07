package weblogic.ejb.container.utils.ddconverter;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Vector;
import weblogic.ejb.container.EJBLogger;

public final class FinderParser {
   private String oldMethodSig;
   private String oldQuery;
   private String newMethodSig;
   private String newQuery;
   private String newMethod;
   private Vector paramNames;
   private Vector paramTypes;
   private EJBddcTextFormatter fmt;
   private ConvertLog log;
   private static final int METHOD_NAME = 1;
   private static final int PARAM_BEGIN = 2;
   private static final int PARAM_TYPE = 3;
   private static final int PARAM_NAME = 4;
   private static final int PARAM_NEXT = 5;
   private static final int ARRAY_PARAM = 6;
   private static final int PARAM_END = 7;

   public FinderParser(String var1, String var2) {
      this.oldMethodSig = var1;
      this.oldQuery = var2;
      this.newMethodSig = new String();
      this.newQuery = new String();
      this.paramNames = new Vector();
      this.paramTypes = new Vector();
   }

   public FinderParser(String var1, String var2, EJBddcTextFormatter var3, ConvertLog var4) {
      this.fmt = var3;
      this.log = var4;
      this.oldMethodSig = var1;
      this.oldQuery = var2;
      this.newMethodSig = new String();
      this.newQuery = new String();
      this.paramNames = new Vector();
      this.paramTypes = new Vector();
   }

   public String getOldMethodSig() {
      return this.oldMethodSig;
   }

   public String getOldQuery() {
      return this.oldQuery;
   }

   public String getNewMethodSig() {
      return this.newMethodSig;
   }

   public String getNewQuery() {
      return this.newQuery;
   }

   public String getNewMethod() {
      return this.newMethod;
   }

   public Vector getParamNames() {
      return this.paramNames;
   }

   public Vector getParamTypes() {
      return this.paramTypes;
   }

   public void parseFinder() {
      this.parseMethodSig();
      this.parseQuery();
      this.checkParams();
   }

   private void checkParams() {
      String var1 = null;
      Class var2 = null;
      Vector var3 = new Vector();
      var3.addElement("int");
      var3.addElement("float");
      var3.addElement("boolean");
      var3.addElement("double");
      var3.addElement("short");
      var3.addElement("byte");
      var3.addElement("char");
      var3.addElement("long");

      for(int var4 = 0; var4 < this.paramTypes.size(); ++var4) {
         var2 = null;
         var1 = (String)this.paramTypes.elementAt(var4);
         if (!var3.contains(var1)) {
            try {
               var2 = Class.forName(var1);
            } catch (ClassNotFoundException var6) {
               this.log.logWarning(this.fmt.typesNotFullyQualified(var1, this.oldMethodSig));
            }
         }
      }

   }

   private void parseQuery() {
      String var1 = new String();
      int var2 = 0;
      boolean var3 = false;
      StreamTokenizer var4 = new StreamTokenizer(new StringReader(this.oldQuery));
      var4.eolIsSignificant(false);
      var4.ordinaryChar(39);
      var4.ordinaryChar(32);
      var4.ordinaryChar(95);
      var4.ordinaryChars(45, 46);
      var4.ordinaryChars(48, 57);
      var4.ordinaryChars(95, 95);
      var4.wordChars(45, 46);
      var4.wordChars(48, 57);
      var4.wordChars(95, 95);

      try {
         while(var2 != -1) {
            var2 = var4.nextToken();
            if (var4.ttype == -3) {
               if (var3) {
                  int var5 = this.paramNames.indexOf(var4.sval);
                  if (var5 <= -1) {
                     String var6 = "Invalid parameter name. " + var4.sval + ". Check to make sure query params match signature params";
                     throw new FinderParserException(var6);
                  }

                  var1 = var1 + var5;
                  var3 = false;
               } else {
                  var1 = var1 + var4.sval;
               }
            } else if (var4.ttype == -2) {
               var1 = var1 + var4.nval;
            } else if (var4.ttype != -1) {
               if (var2 == 36) {
                  var3 = true;
               }

               var1 = var1 + (char)var2;
            }
         }

         this.newQuery = var1;
      } catch (IOException var7) {
         EJBLogger.logStackTrace(var7);
      } catch (FinderParserException var8) {
         EJBLogger.logStackTrace(var8);
      }

   }

   private void parseMethodSig() {
      String var1 = new String();
      byte var2 = 1;
      int var3 = 0;
      int var4 = 0;

      try {
         StreamTokenizer var5 = new StreamTokenizer(new StringReader(this.oldMethodSig));
         var5.eolIsSignificant(false);
         var5.wordChars(95, 95);

         while(var3 != -1) {
            var3 = var5.nextToken();
            switch (var2) {
               case 1:
                  if (var5.sval == null) {
                     throw new FinderParserException();
                  }

                  var1 = var1 + var5.sval;
                  var2 = 2;
                  this.newMethod = var5.sval;
                  break;
               case 2:
                  if (var3 != 40) {
                     throw new FinderParserException();
                  }

                  var1 = var1 + (char)var3;
                  var2 = 3;
                  break;
               case 3:
                  if (var3 == 41) {
                     var1 = var1 + (char)var3;
                     var2 = 7;
                  } else {
                     if (var5.sval == null) {
                        throw new FinderParserException();
                     }

                     var1 = var1 + var5.sval;
                     this.paramTypes.addElement(var5.sval);
                     var2 = 4;
                  }
                  break;
               case 4:
                  if (var3 == 91) {
                     var2 = 6;
                     var1 = var1 + (char)var3;
                  } else {
                     if (var5.sval == null) {
                        throw new FinderParserException();
                     }

                     this.paramNames.addElement(var5.sval);
                     var1 = var1 + " $" + var4++;
                     var2 = 5;
                  }
                  break;
               case 5:
                  if (var3 == 44) {
                     var1 = var1 + (char)var3 + " ";
                     var2 = 3;
                  } else {
                     if (var3 != 41) {
                        if (var3 == 91) {
                           String var6 = "This parser can't deal with arrays declared in ths format: type name[]. Use format: type[] name";
                           throw new FinderParserException(var6);
                        }

                        throw new FinderParserException();
                     }

                     var1 = var1 + (char)var3;
                     var2 = 7;
                  }
                  break;
               case 6:
                  if (var3 != 93) {
                     throw new FinderParserException();
                  }

                  var1 = var1 + (char)var3;
                  var2 = 4;
               case 7:
            }
         }

         this.newMethodSig = var1;
      } catch (IOException var7) {
         EJBLogger.logStackTrace(var7);
      } catch (FinderParserException var8) {
         EJBLogger.logStackTrace(var8);
      }

   }

   public static void main(String[] var0) {
      new EJBddcTextFormatter();
      Object var2 = null;

      try {
         new ConvertLog();
      } catch (IOException var6) {
         System.err.println("Error create ddconverter.log");
         System.exit(1);
      }

      new String();
      new String();
      FinderParser var5 = null;
      System.out.println("\n");
      String var3 = "findString(String foo, int bar, java.lang.String baz)";
      String var4 = "(= foo $foo)";
      var5 = new FinderParser(var3, var4);
      var5.parseFinder();
      System.out.println(" old method sig: " + var5.getOldMethodSig());
      System.out.println(" new method sig: " + var5.getNewMethodSig());
      System.out.println("     new method: " + var5.getNewMethod());
      System.out.println("new param types: " + var5.getParamTypes());
      System.out.println("      old query: " + var5.getOldQuery());
      System.out.println("      new query: " + var5.getNewQuery());
      System.out.println("\n");
   }

   public final class FinderParserException extends Exception {
      private static final long serialVersionUID = -133777467782987526L;

      public FinderParserException() {
      }

      public FinderParserException(String var2) {
         super(var2);
      }
   }
}
