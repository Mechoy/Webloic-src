package weblogic.wsee.bind.internal;

import com.bea.staxb.buildtime.internal.bts.BindingFile;
import com.bea.staxb.buildtime.internal.tylar.DebugTylarWriter;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaProperty;
import com.bea.xml.SchemaType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.xml.stax.util.XMLPrettyPrinter;

public class BindingDebugUtils {
   public static final boolean DUMPING_ENABLED = false;
   private static BindingDebugUtils INSTANCE = new BindingDebugUtils();
   private File mTempDir = new File("d:/pcaltemp");
   private long mId = System.currentTimeMillis() % 10000000L;

   public static BindingDebugUtils getInstance() {
      return INSTANCE;
   }

   private BindingDebugUtils() {
   }

   public void dumpSchema(String var1, SchemaDocument var2) {
      Writer var3 = null;

      try {
         var3 = this.getWriter(var1);
         var2.save(var3);
         var3.flush();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var12) {
               var12.printStackTrace();
            }
         }

      }

   }

   public void dumpJavaWsdlMappingBean(String var1, JavaWsdlMappingBean var2) {
      Writer var3 = null;

      try {
         var3 = this.getWriter(var1);
         dumpJavaWsdlMappingBean(var2, var3);
         var3.flush();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var12) {
               var12.printStackTrace();
            }
         }

      }

   }

   public void dumpTylar(String var1, Tylar var2) {
      Writer var3 = null;

      try {
         var3 = this.getWriter(var1);
         (new DebugTylarWriter(new PrintWriter(var3, true))).write(var2);
         var3.flush();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var12) {
               var12.printStackTrace();
            }
         }

      }

   }

   public void dumpBindingFile(String var1, BindingFile var2) {
      Writer var3 = null;

      try {
         var3 = this.getWriter(var1);
         (new DebugTylarWriter(new PrintWriter(var3, true))).writeBindingFile(var2);
         var3.flush();
      } catch (Exception var13) {
         var13.printStackTrace();
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var12) {
               var12.printStackTrace();
            }
         }

      }

   }

   public static void dumpSchemaType(SchemaType var0) {
      System.out.println("SchemaType '" + var0.getName() + "' element properties:");
      SchemaProperty[] var1 = var0.getElementProperties();
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            System.out.println("  " + var1[var2].getName());
         }
      }

      System.out.println("SchemaType '" + var0.getName() + "' attributeproperties:");
      SchemaProperty[] var4 = var0.getAttributeProperties();
      if (var4 != null) {
         for(int var3 = 0; var3 < var4.length; ++var3) {
            System.out.println("  " + var4[var3].getName());
         }
      }

   }

   private static void dumpJavaWsdlMappingBean(JavaWsdlMappingBean var0, Writer var1) throws IOException, XMLStreamException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      ((DescriptorBean)var0).getDescriptor().toXML(var2);
      StringReader var3 = new StringReader(var2.toString());
      XMLStreamReader var4 = XMLInputFactory.newInstance().createXMLStreamReader(var3);
      XMLPrettyPrinter var5 = new XMLPrettyPrinter(var1);

      while(var4.hasNext()) {
         var5.write(var4);
         var4.next();
      }

      var5.flush();
   }

   private Writer getWriter(String var1) throws IOException {
      File var2 = new File(this.mTempDir, String.valueOf((long)(this.mId++)));
      var2.mkdirs();
      this.dumpTrace(var2);
      File var3 = new File(var2, var1);
      return new FileWriter(var3);
   }

   private void dumpTrace(File var1) throws IOException {
      File var2 = new File(var1, "stacktrace.txt");
      FileWriter var3 = null;

      try {
         var3 = new FileWriter(var2);
         (new Exception()).printStackTrace(new PrintWriter(var3, true));
      } catch (IOException var9) {
         throw var9;
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         if (var3 != null) {
            var3.close();
         }

      }

   }
}
