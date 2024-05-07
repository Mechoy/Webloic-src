package weblogic.jndi.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.rmi.Remote;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import weblogic.common.internal.PassivationUtils;
import weblogic.diagnostics.image.ImageSource;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.utils.classloaders.GenericClassLoader;

public final class JNDIImageSource implements ImageSource, JNDIImageSourceConstants {
   private boolean timedOutCreatingImage;

   public static final ImageSource getJNDIImageSource() {
      return JNDIImageSource.SingletonMaker.singleton;
   }

   private JNDIImageSource() {
   }

   public void createDiagnosticImage(OutputStream var1) {
      InitialContext var2 = null;
      ClassLoader var4 = Thread.currentThread().getContextClassLoader();

      try {
         GenericClassLoader var5 = new GenericClassLoader(var4);
         Thread.currentThread().setContextClassLoader(var5);
         var2 = new InitialContext();
         PrintWriter var3 = new PrintWriter(new OutputStreamWriter(var1, "UTF-8"));
         this.writeContextInfo(var3, var2.getNameInNamespace());
         this.printContextInfo(var3, var2);
         if (!this.timedOutCreatingImage && !var3.checkError()) {
            var3.flush();
         }
      } catch (NamingException var10) {
      } catch (IOException var11) {
      } finally {
         this.closeContext(var2);
         Thread.currentThread().setContextClassLoader(var4);
      }

   }

   private void printContextInfo(PrintWriter var1, Context var2) throws NamingException, IOException {
      NamingEnumeration var3 = var2.list("");

      while(var3.hasMoreElements()) {
         NameClassPair var4 = (NameClassPair)var3.nextElement();
         String var5 = var4.getName();
         String var6 = var4.getClassName();

         try {
            Object var7 = var2.lookup(var5);
            if (var7 instanceof Context) {
               Context var8 = (Context)var7;
               this.writeContextInfo(var1, var8.getNameInNamespace());
               this.printContextInfo(var1, var8);
            } else {
               this.writeBindingInfo(var1, new Binding(var5, var4.getClassName(), var7));
            }
         } catch (Exception var9) {
         }
      }

      var1.println("</context>");
   }

   private void closeContext(Context var1) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (NamingException var3) {
         }
      }

   }

   public void timeoutImageCreation() {
      this.timedOutCreatingImage = true;
   }

   private void writeContextInfo(PrintWriter var1, String var2) {
      var1.println();
      var1.print("<context");
      var1.println(">");
      var1.print("name=\"");
      var1.print(var2);
      var1.println("\"");
   }

   private void writeBindingInfo(PrintWriter var1, Binding var2) throws IOException {
      Object var3 = var2.getObject();
      short var4 = this.getObjectType(var3);
      var1.println("<binding");
      var1.print("jndi-name=\"");
      var1.print(var2.getName());
      var1.println("\"");
      var1.print("class-name=\"");
      var1.print(var2.getClassName());
      var1.println("\"");
      var1.print("size=\"");
      printObjectSize(var1, var4, var3);
      var1.println("\"");
      var1.print("type=\"");
      getTypeAsString(var1, var4);
      var1.println("\"");
      var1.print("clusterable=\"");
      if (var4 == 1) {
         if (ServerHelper.isClusterable((Remote)var3)) {
            var1.print("true");
         } else {
            var1.print("false");
         }
      } else {
         var1.print("false");
      }

      var1.println("\"");
      var1.print("string-representation=\"");
      var1.print(var3.toString());
      var1.print("\"");
      var1.println(">");
      var1.println("</binding>");
   }

   private short getObjectType(Object var1) {
      if (JNDIHelper.isCorbaObject(var1)) {
         return 0;
      } else if (var1 instanceof Remote) {
         return 1;
      } else if (var1 instanceof Externalizable) {
         return 2;
      } else if (var1 == "non-serializable") {
         return 4;
      } else {
         return (short)(var1 instanceof Serializable ? 3 : 4);
      }
   }

   private static void getTypeAsString(PrintWriter var0, short var1) {
      switch (var1) {
         case 0:
            var0.print("corba");
            break;
         case 1:
            var0.print("remote");
            break;
         case 2:
            var0.print("externalizable");
            break;
         case 3:
            var0.print("serializable");
            break;
         case 4:
            var0.print("non-serializable");
            break;
         default:
            throw new AssertionError("Unexpected type " + var1);
      }

   }

   private static void printObjectSize(PrintWriter var0, short var1, Object var2) throws IOException {
      switch (var1) {
         case 0:
            var0.print(0);
            break;
         case 1:
            var0.print(0);
            break;
         case 2:
            var0.print(PassivationUtils.sizeOf(var2));
            break;
         case 3:
            var0.print(PassivationUtils.sizeOf(var2));
            break;
         case 4:
            var0.print(-1);
            break;
         default:
            throw new AssertionError("Unexpected type " + var1);
      }

   }

   // $FF: synthetic method
   JNDIImageSource(Object var1) {
      this();
   }

   private static class SingletonMaker {
      static final ImageSource singleton = new JNDIImageSource();
   }
}
