package weblogic.xml.dtdc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Stack;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.ParserFactory;

public class Handler implements DocumentHandler {
   private Stack stack;
   private Object topLevel;
   private boolean verbose;
   private String packageName;
   private Map map;
   private ClassLoader loader;

   public static void main(String[] var0) throws Exception {
      try {
         Handler var1 = new Handler();
         Parser var6 = ParserFactory.makeParser("com.ibm.xml.parser.SAXDriver");
         var6.setDocumentHandler(var1);
         var6.parse(var0[0]);
      } catch (SAXParseException var3) {
         System.out.println("** Parsing error, line " + var3.getLineNumber() + ", column " + var3.getColumnNumber() + ", uri " + var3.getSystemId());
         System.out.println("   " + var3.getMessage());
      } catch (SAXException var4) {
         Object var2 = var4;
         if (var4.getException() != null) {
            var2 = var4.getException();
         }

         ((Exception)var2).printStackTrace();
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }

   public Handler() {
      this.stack = new Stack();
      this.packageName = "weblogic.xml.objects";
      this.loader = this.getClass().getClassLoader();
   }

   public Handler(boolean var1) {
      this.stack = new Stack();
      this.packageName = "weblogic.xml.objects";
      this.loader = this.getClass().getClassLoader();
      this.verbose = var1;
   }

   public Handler(String var1, boolean var2) {
      this(var2);
      this.packageName = var1;
   }

   public Handler(Map var1, String var2, boolean var3) {
      this(var2, var3);
      this.map = var1;
   }

   public void setPackageName(String var1) {
      this.packageName = var1;
   }

   public void setClassLoader(ClassLoader var1) {
      this.loader = var1;
   }

   public Object getTopLevel() {
      return this.topLevel;
   }

   public void setDocumentLocator(Locator var1) {
      if (this.verbose) {
         System.out.println("setDocumentLocator: " + var1);
      }

   }

   public void startDocument() throws SAXException {
      if (this.verbose) {
         System.out.println("startDocument");
      }

   }

   public void endDocument() throws SAXException {
      if (this.verbose) {
         System.out.println("endDocument");
      }

   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
      if (this.verbose) {
         System.out.println("ignorableWhitespace: " + var3);
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (this.verbose) {
         System.out.println("characters: [");
         char[] var4 = new char[var3];
         System.arraycopy(var1, var2, var4, 0, var3);
         System.out.print(var4);
         System.out.println("]");
      }

      NameValuePair var8 = (NameValuePair)this.stack.peek();
      char[] var5 = new char[var3];
      System.arraycopy(var1, var2, var5, 0, var3);

      try {
         Method var6 = var8.value.getClass().getMethod("addDataElement", String.class);
         var6.invoke(var8.value, new String(var5));
      } catch (Exception var7) {
         throw new SAXException("Could not add data to element: " + var8.value + " because " + ((InvocationTargetException)var7).getTargetException());
      }
   }

   public void processingInstruction(String var1, String var2) throws SAXException {
      if (this.verbose) {
         System.out.println("processingInstruction: " + var1 + " <- " + var2);
      }

   }

   public void startElement(String var1, AttributeList var2) throws SAXException {
      try {
         String var4 = this.packageName + NameMangler.getpackage(var1);
         String var5 = this.map != null && this.map.get(var1) != null ? (String)this.map.get(var1) : var4 + "." + NameMangler.upcase(NameMangler.depackage(var1));
         GeneratedElement var3;
         if (this.loader == null) {
            var3 = (GeneratedElement)Class.forName(var5).newInstance();
         } else {
            var3 = (GeneratedElement)this.loader.loadClass(var5).newInstance();
         }

         var3.initialize(var1, var2);
         this.stack.push(new NameValuePair(var1, var3));
         if (this.verbose) {
            System.out.println("startElement: " + var1 + " -> " + var2);
         }

      } catch (Exception var6) {
         if (this.verbose) {
            var6.printStackTrace();
         }

         throw new SAXException(var6);
      }
   }

   public void endElement(String var1) throws SAXException {
      if (this.verbose) {
         System.out.println("endElement: " + var1);
      }

      NameValuePair var2 = (NameValuePair)this.stack.pop();
      if (this.stack.empty()) {
         this.topLevel = var2.value;
      } else {
         String var3 = "add" + NameMangler.upcase(NameMangler.depackage(var1)) + "Element";
         NameValuePair var4 = (NameValuePair)this.stack.peek();

         try {
            Method var5;
            if (this.loader == null) {
               var5 = var4.value.getClass().getMethod(var3, Class.forName(this.packageName + "." + NameMangler.upcase(NameMangler.depackage(var2.name))));
            } else {
               var5 = var4.value.getClass().getMethod(var3, this.loader.loadClass(this.packageName + "." + NameMangler.upcase(NameMangler.depackage(var2.name))));
            }

            var5.invoke(var4.value, var2.value);
         } catch (InvocationTargetException var7) {
            Throwable var6 = var7.getTargetException();
            if (this.verbose) {
               var6.printStackTrace();
            }

            if (var6 instanceof Exception) {
               throw new SAXException((Exception)var6);
            } else {
               throw (Error)var6;
            }
         } catch (NoSuchMethodException var8) {
            throw new SAXException("Could not find " + var3 + "(" + var2.value.getClass() + ") in " + var4.name);
         } catch (Exception var9) {
            if (this.verbose) {
               var9.printStackTrace();
            }

            throw new SAXException(var9);
         }
      }
   }

   private static class NameValuePair {
      String name;
      Object value;

      NameValuePair(String var1, Object var2) {
         this.name = var1;
         this.value = var2;
      }
   }
}
