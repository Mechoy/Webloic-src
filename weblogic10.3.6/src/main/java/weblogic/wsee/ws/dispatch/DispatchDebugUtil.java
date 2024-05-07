package weblogic.wsee.ws.dispatch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.wsee.handler.HandlerIterator;
import weblogic.wsee.handler.HandlerListImpl;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;

public class DispatchDebugUtil {
   private static Map<String, PrintWriter> _pwMap = new HashMap();

   public static PrintWriter getPrintWriterForThread() {
      Object var0 = (PrintWriter)_pwMap.get(Thread.currentThread().getName());
      if (var0 == null) {
         File var1 = new File("Thread-" + Thread.currentThread().getId() + ".out");

         try {
            FileOutputStream var2 = new FileOutputStream(var1, true);
            var0 = new MyPrintWriter(var2, true);
            _pwMap.put(Thread.currentThread().getName(), var0);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      return (PrintWriter)var0;
   }

   public static void dumpRequestContext(String var0, WlMessageContext var1, HandlerIterator var2, int var3) {
      PrintWriter var4 = getPrintWriterForThread();
      dumpLoader(var4);
      var4.println("  **" + var0);
      var4.println("    opName: " + var1.getProperty("weblogic.wsee.ws.server.OperationName"));
      var4.println("  SOAPMessage:");
      if (var1 instanceof SoapMessageContext) {
         SoapMessageContext var5 = (SoapMessageContext)var1;
         SOAPMessage var6 = var5.getMessage();
         if (var6 != null) {
            try {
               dumpSOAPMessage(var6, var4);
            } catch (Exception var8) {
               var8.printStackTrace(var4);
            }
         }
      }

      dumpMessageContext(var1, var4);
      dumpHandlerChain(var2, var4, var3);
      var4.flush();
   }

   private static void dumpHandlerChain(HandlerIterator var0, PrintWriter var1, int var2) {
      HandlerListImpl var3 = var0.getHandlers();
      String[] var4 = var3.getHandlerNames();
      var1.println("  Handlers:");

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var5 == var2) {
            var1.print("     *");
         } else {
            var1.print("      ");
         }

         var1.println("Handler[" + var5 + "].name=" + var4[var5]);
      }

   }

   private static void dumpMessageContext(WlMessageContext var0, PrintWriter var1) {
      var1.println("  Context Props:");
      Iterator var2 = var0.getPropertyNames();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Object var4 = var0.getProperty(var3);
         var1.println("    " + var3 + "=" + var4);
      }

   }

   private static void dumpLoader(PrintWriter var0) {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      String var2 = var1.toString();
      if (var1 instanceof GenericClassLoader) {
         Annotation var3 = ((GenericClassLoader)var1).getAnnotation();
         var2 = var3.getApplicationName() + "/" + var3.getModuleName();
      }

      var0.println("\n### Loader: " + var2);
      var0.println(Thread.currentThread().getName());
   }

   public static void dumpSOAPMessage(SOAPMessage var0) {
      PrintWriter var1 = getPrintWriterForThread();
      dumpLoader(var1);
      var1.println("** SOAPMessage just set to:");

      try {
         dumpSOAPMessage(var0, var1);
      } catch (Exception var3) {
         var3.printStackTrace(var1);
      }

      var1.println("**\n");
   }

   private static String getTimestamp() {
      SimpleDateFormat var0 = new SimpleDateFormat("hh:mm:ss.SSS");
      return var0.format(new Date());
   }

   private static void dumpSOAPMessage(SOAPMessage var0, PrintWriter var1) throws SOAPException {
      if (var0 != null) {
         SOAPBody var2 = var0.getSOAPBody();
         if (var2.hasFault()) {
            SOAPFault var3 = var2.getFault();
            SOAPFaultException var4 = new SOAPFaultException(var3.getFaultCodeAsQName(), var3.getFaultCode(), var3.getFaultString(), var3.getDetail());
            var4.printStackTrace(var1);
         } else {
            var1.println("HEADER");
            SOAPHeader var11 = var0.getSOAPHeader();
            NodeList var12 = var11.getChildNodes();
            Node var5 = null;

            for(int var6 = 0; var6 < var12.getLength(); ++var6) {
               var1.println("SOAP node " + (var6 + 1) + " of " + var12.getLength());
               Node var7 = var12.item(var6);
               if (var5 == null) {
                  var5 = var7;
               }

               ByteArrayOutputStream var8 = new ByteArrayOutputStream();
               serialize(var7, var8);
               String var9 = new String(var8.toByteArray());
               var1.println(var9);
            }

            var1.println("BODY");
            NodeList var13 = var2.getChildNodes();
            var5 = null;

            for(int var14 = 0; var14 < var13.getLength(); ++var14) {
               var1.println("SOAP node " + (var14 + 1) + " of " + var13.getLength());
               Node var15 = var13.item(var14);
               if (var5 == null) {
                  var5 = var15;
               }

               ByteArrayOutputStream var16 = new ByteArrayOutputStream();
               serialize(var15, var16);
               String var10 = new String(var16.toByteArray());
               var1.println(var10);
            }
         }
      } else {
         var1.println("Null");
      }

   }

   private static void serialize(Node var0, OutputStream var1) {
      TransformerFactory var2 = TransformerFactory.newInstance();

      try {
         Transformer var3 = var2.newTransformer();
         var3.setOutputProperty("indent", "yes");
         var3.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         var3.transform(new DOMSource(var0), new StreamResult(var1));
      } catch (TransformerException var5) {
         var5.printStackTrace();
         throw new RuntimeException(var5);
      }
   }

   private static class MyPrintWriter extends PrintWriter {
      private boolean _atStartOfLine = true;

      public MyPrintWriter(OutputStream var1, boolean var2) {
         super(var1, var2);
      }

      public void write(int var1) {
         super.write(var1);
         this._atStartOfLine = false;
      }

      public void write(char[] var1) {
         super.write(var1);
         this._atStartOfLine = false;
      }

      public void write(String var1, int var2, int var3) {
         super.write(var1, var2, var3);
         this._atStartOfLine = false;
      }

      public void write(String var1) {
         super.write(var1);
         this._atStartOfLine = false;
      }

      public void println() {
         super.println();
         this._atStartOfLine = true;
      }

      public void print(String var1) {
         if (this._atStartOfLine) {
            super.print(DispatchDebugUtil.getTimestamp());
            super.print(": ");
         }

         super.print(var1);
      }

      public void println(String var1) {
         if (this._atStartOfLine) {
            super.print(DispatchDebugUtil.getTimestamp());
            super.print(": ");
         }

         super.println(var1);
         this._atStartOfLine = true;
      }
   }
}
