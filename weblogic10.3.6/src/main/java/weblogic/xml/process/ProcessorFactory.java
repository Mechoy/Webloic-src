package weblogic.xml.process;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.PushbackReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.ejb.container.EJBTextTextFormatter;
import weblogic.ejb.spi.XMLConstants;
import weblogic.utils.AssertionError;

public class ProcessorFactory {
   public static final String PROCESS_FACTORY_DEBUG = "weblogic.xml.process.debug";
   private static boolean debug;
   private static final String DOCTYPE_DECL_START = "<!DOCTYPE";
   private static final String PUBLIC_DECL_START = "PUBLIC";
   private static final String[] QUOTES = new String[]{"\"", "'"};
   private boolean validate = true;
   Map registeredProcessors = new HashMap();

   public ProcessorFactory() {
      debug = Boolean.getBoolean("weblogic.xml.process.debug");
      this.loadRegistry();
   }

   public ProcessorFactory(Map var1) {
      debug = Boolean.getBoolean("weblogic.xml.process.debug");
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String var4 = (String)var1.get(var3);
         this.registerProcessor(var3, var4);
      }

   }

   private void loadRegistry() {
      this.registerProcessor(ProcessorConstants.XML_TO_JAVA_PUBLIC_ID(), ProcessorConstants.XML_TO_JAVA_LOADER_CLASS());
      this.registerProcessor(ProcessorConstants.JAVA_TO_XML_PUBLIC_ID(), ProcessorConstants.JAVA_TO_XML_LOADER_CLASS());
      this.registerProcessors(XMLConstants.processors);
      this.registerProcessor("-//Sun Microsystems, Inc.//DTD J2EE Application 1.2//EN", "weblogic.j2ee.dd.xml.J2EEDeploymentDescriptorLoader_J2EE12");
      this.registerProcessor("-//Sun Microsystems, Inc.//DTD J2EE Application 1.3//EN", "weblogic.j2ee.dd.xml.J2EEDeploymentDescriptorLoader_J2EE13");
      this.registerProcessor("-//BEA Systems, Inc.//DTD WebLogic Application 7.0.0//EN", "weblogic.j2ee.dd.xml.WebLogicApplication_1_0");
      this.registerProcessor("-//BEA Systems, Inc.//DTD WebLogic Application 8.1.0//EN", "weblogic.j2ee.dd.xml.WebLogicApplication_2_0");
      this.registerProcessor("-//BEA Systems, Inc.//DTD WebLogic Application 9.0.0//EN", "weblogic.j2ee.dd.xml.WebLogicApplication_3_0");
   }

   private void registerProcessors(Map var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.registerProcessor(var3, (String)var1.get(var3));
      }

   }

   private void registerProcessor(String var1, String var2) {
      if (debug) {
         System.out.println("Registering " + var2 + " for " + var1);
      }

      this.registeredProcessors.put(var1, var2);
   }

   public XMLProcessor getProcessor(String var1, String[] var2) throws ProcessorFactoryException {
      FileInputStream var3 = null;

      try {
         var3 = new FileInputStream(var1);
      } catch (IOException var14) {
         throw new ProcessorFactoryException(var14);
      }

      XMLProcessor var4;
      try {
         var4 = this.getProcessor((InputStream)var3, var2);
      } finally {
         try {
            var3.close();
         } catch (IOException var12) {
         }

      }

      return var4;
   }

   public XMLProcessor getProcessor(File var1, String[] var2) throws ProcessorFactoryException {
      FileInputStream var3 = null;

      try {
         var3 = new FileInputStream(var1);
      } catch (IOException var14) {
         throw new ProcessorFactoryException(var14);
      }

      XMLProcessor var4;
      try {
         var4 = this.getProcessor((InputStream)var3, var2);
      } finally {
         try {
            var3.close();
         } catch (IOException var12) {
         }

      }

      return var4;
   }

   public XMLProcessor getProcessor(InputStream var1, String[] var2) throws ProcessorFactoryException {
      return this.getProcessor(var1, false, var2);
   }

   public XMLProcessor getProcessor(Reader var1, String[] var2) throws ProcessorFactoryException {
      return this.getProcessor(var1, false, var2);
   }

   public XMLProcessor getProcessor(InputStream var1, boolean var2, String[] var3) throws ProcessorFactoryException {
      try {
         String var4 = this.readPublicId(var1, var2);
         if (var4 != null) {
            var4 = var4.trim();
         }

         String var5 = (String)this.registeredProcessors.get(var4);
         if (var5 == null) {
            if (debug) {
               System.out.println("Failed to find publicid = \"" + var4 + "\"");
               System.out.println("processor registry = " + this.registeredProcessors);
            }

            EJBTextTextFormatter var18 = new EJBTextTextFormatter();
            String var7 = var18.INVALID_PUBLIC_ID(var4);
            if (var3 != null && var3.length > 0) {
               String var8 = "\n";

               for(int var9 = 0; var9 < var3.length; ++var9) {
                  var8 = var8 + "\"" + var3[var9] + "\"\n";
               }

               var7 = var7 + "  " + var18.USE_VALID_ID(var8);
            }

            throw new ProcessorFactoryException(var7);
         } else {
            Constructor var6 = Class.forName(var5).getConstructor(Boolean.TYPE);
            return (XMLProcessor)var6.newInstance(new Boolean(this.validate));
         }
      } catch (EOFException var10) {
         throw new ProcessorFactoryException("XML document does not appear to contain a properly formed DOCTYPE header");
      } catch (NoSuchMethodException var11) {
         throw new AssertionError("Cannot find boolean constructor of processor");
      } catch (SecurityException var12) {
         throw new AssertionError("Cannot invoke boolean constructor of processor", var12);
      } catch (InvocationTargetException var13) {
         throw new AssertionError("Cannot invoke boolean constructor of processor", var13);
      } catch (ClassNotFoundException var14) {
         throw new ProcessorFactoryException(var14);
      } catch (InstantiationException var15) {
         throw new ProcessorFactoryException(var15);
      } catch (IllegalAccessException var16) {
         throw new ProcessorFactoryException(var16);
      } catch (IOException var17) {
         throw new ProcessorFactoryException(var17);
      }
   }

   public XMLProcessor getProcessor(Reader var1, boolean var2, String[] var3) throws ProcessorFactoryException {
      try {
         String var4 = this.readPublicId(var1, var2);
         String var5 = (String)this.registeredProcessors.get(var4);
         if (var5 == null) {
            if (debug) {
               System.out.println("Failed to find publicid = \"" + var4 + "\"");
               System.out.println("processor registry = " + this.registeredProcessors);
            }

            EJBTextTextFormatter var18 = new EJBTextTextFormatter();
            String var7 = var18.INVALID_PUBLIC_ID(var4);
            if (var3 != null && var3.length > 0) {
               String var8 = "\n";

               for(int var9 = 0; var9 < var3.length; ++var9) {
                  var8 = var8 + "\"" + var3[var9] + "\"\n";
               }

               var7 = var7 + "  " + var18.USE_VALID_ID(var8);
            }

            throw new ProcessorFactoryException(var7);
         } else {
            Constructor var6 = Class.forName(var5).getConstructor(Boolean.TYPE);
            return (XMLProcessor)var6.newInstance(new Boolean(this.validate));
         }
      } catch (EOFException var10) {
         throw new ProcessorFactoryException("XML document does not appear to contain a properly formed DOCTYPE header");
      } catch (NoSuchMethodException var11) {
         throw new AssertionError("Cannot find boolean constructor of processor");
      } catch (SecurityException var12) {
         throw new AssertionError("Cannot invoke boolean constructor of processor", var12);
      } catch (InvocationTargetException var13) {
         throw new AssertionError("Cannot invoke boolean constructor of processor", var13);
      } catch (ClassNotFoundException var14) {
         throw new ProcessorFactoryException(var14);
      } catch (InstantiationException var15) {
         throw new ProcessorFactoryException(var15);
      } catch (IllegalAccessException var16) {
         throw new ProcessorFactoryException(var16);
      } catch (IOException var17) {
         throw new ProcessorFactoryException(var17);
      }
   }

   public void setValidating(boolean var1) {
      this.validate = var1;
   }

   public boolean isValidating() {
      return this.validate;
   }

   private String readPublicId(InputStream var1, boolean var2) throws IOException {
      var2 &= var1.markSupported();
      if (debug) {
         System.out.println("readPublicId: Reset = " + var2);
      }

      if (var2) {
         var1.mark(1000);
      }

      PushbackInputStream var4 = new PushbackInputStream(var1);
      if (debug) {
         System.out.println("reading through <!DOCTYPE");
      }

      ParsingUtils.read(var4, "<!DOCTYPE", true);
      if (debug) {
         System.out.println("reading through WS");
      }

      ParsingUtils.readWS(var4);
      if (debug) {
         System.out.println("reading through PUBLIC");
      }

      ParsingUtils.read(var4, "PUBLIC", true);
      if (debug) {
         System.out.println("reading through first quote");
      }

      String var5 = ParsingUtils.read(var4, QUOTES, true);
      char var6 = var5.charAt(var5.length() - 1);
      String var7 = new String(new char[]{var6});
      if (debug) {
         System.out.println("reading until close quote");
      }

      String var8 = ParsingUtils.read(var4, var7, false);
      if (var2) {
         try {
            var1.reset();
         } catch (IOException var10) {
            if (debug) {
               var10.printStackTrace();
            }
         }
      }

      if (debug) {
         System.out.println("Read publicId = " + var8);
      }

      return var8;
   }

   private String readPublicId(Reader var1, boolean var2) throws IOException {
      var2 &= var1.markSupported();
      if (debug) {
         System.out.println("readPublicId: Reset = " + var2);
      }

      if (var2) {
         try {
            var1.mark(1000);
         } catch (IOException var10) {
            var2 = false;
         }
      }

      PushbackReader var4 = new PushbackReader(var1);
      if (debug) {
         System.out.println("reading through <!DOCTYPE");
      }

      ParsingUtils.read(var4, "<!DOCTYPE", true);
      if (debug) {
         System.out.println("reading through WS");
      }

      ParsingUtils.readWS(var4);
      if (debug) {
         System.out.println("reading through PUBLIC");
      }

      ParsingUtils.read(var4, "PUBLIC", true);
      if (debug) {
         System.out.println("reading through first quote");
      }

      String var5 = ParsingUtils.read(var4, QUOTES, true);
      char var6 = var5.charAt(var5.length() - 1);
      String var7 = new String(new char[]{var6});
      if (debug) {
         System.out.println("reading until close quote");
      }

      String var8 = ParsingUtils.read(var4, var7, false);
      if (var2) {
         try {
            var1.reset();
         } catch (IOException var11) {
            if (debug) {
               var11.printStackTrace();
            }
         }
      }

      if (debug) {
         System.out.println("Read publicId = " + var8);
      }

      return var8;
   }

   public static void main(String[] var0) throws Exception {
      if (debug) {
         ProcessorFactory var1 = new ProcessorFactory();
         FileReader var2 = new FileReader(var0[0]);
         System.out.println("public id = " + var1.readPublicId((Reader)var2, false));
      }

   }
}
