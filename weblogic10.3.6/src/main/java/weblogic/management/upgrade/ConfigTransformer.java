package weblogic.management.upgrade;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import weblogic.management.ManagementException;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class ConfigTransformer implements ErrorListener {
   private static String[] XSLT_SCRIPT;
   private String[] scriptNames;
   private Transformer[] transformer;

   ConfigTransformer() throws ManagementException {
      this(XSLT_SCRIPT);
   }

   ConfigTransformer(String[] var1) throws ManagementException {
      if (var1 == null) {
         var1 = new String[0];
      }

      TransformerFactory var2 = TransformerFactory.newInstance();
      var2.setErrorListener(this);
      this.scriptNames = new String[var1.length];
      this.transformer = new Transformer[var1.length];

      try {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            Source var4 = getXSLTSource(var1[var3]);
            this.scriptNames[var3] = var1[var3];
            this.transformer[var3] = var2.newTransformer(var4);
            this.transformer[var3].setErrorListener(this);
         }

      } catch (TransformerConfigurationException var5) {
         throw new ManagementException("Error creating transformer", var5);
      }
   }

   private static Source getXSLTSource(String var0) {
      InputStream var1 = ClassLoader.getSystemResourceAsStream(var0);
      if (var1 == null) {
         throw new AssertionError("failed to find " + var0 + " in classpath");
      } else {
         return new SAXSource(new InputSource(var1));
      }
   }

   public void transform(InputStream var1, OutputStream var2) throws ManagementException {
      for(int var3 = 0; var3 < this.transformer.length; ++var3) {
         ByteArrayOutputStream var4 = new ByteArrayOutputStream();
         StreamSource var5 = new StreamSource((InputStream)var1);
         StreamResult var6 = new StreamResult(var4);

         try {
            this.transformer[var3].transform(var5, var6);
         } catch (TransformerException var9) {
            throw new ManagementException("Error during transformation: " + this.scriptNames[var3], var9);
         }

         var1 = new ByteArrayInputStream(var4.toByteArray());
      }

      try {
         XMLInputStream var10 = XMLInputStreamFactory.newInstance().newInputStream((InputStream)var1);
         XMLOutputStream var11 = XMLOutputStreamFactory.newInstance().newCanonicalOutputStream(var2);
         var11.add(var10);
         var11.flush();
      } catch (XMLStreamException var8) {
         throw new ManagementException(var8.getMessage(), var8);
      }
   }

   public void error(TransformerException var1) throws TransformerException {
      throw var1;
   }

   public void fatalError(TransformerException var1) throws TransformerException {
      throw var1;
   }

   public void warning(TransformerException var1) throws TransformerException {
      throw var1;
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length >= 1 && var0.length <= 2) {
         FileInputStream var1 = new FileInputStream(var0[0]);
         Object var2 = System.out;
         if (var0.length > 1) {
            var2 = new FileOutputStream(var0[1]);
         }

         (new ConfigTransformer()).transform(var1, (OutputStream)var2);
      } else {
         System.out.println("java " + ConfigTransformer.class.getName() + " <configFile> [<outFile>]");
         System.exit(1);
      }

   }

   static {
      XSLT_SCRIPT = ConfigFileHelperConstants.UPGRADE_XSLT_SCRIPTS;
   }
}
