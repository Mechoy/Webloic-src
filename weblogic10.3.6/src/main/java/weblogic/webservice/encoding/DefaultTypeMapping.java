package weblogic.webservice.encoding;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.rpc.JAXRPCException;
import weblogic.xml.schema.binding.BindingException;
import weblogic.xml.schema.binding.internal.TypeMappingBase;
import weblogic.xml.schema.binding.internal.XSDTypeMapping;

/** @deprecated */
public class DefaultTypeMapping extends TypeMappingBase {
   public DefaultTypeMapping(String var1) {
      try {
         this.setParent(XSDTypeMapping.createXSDMapping());
      } catch (BindingException var3) {
         throw new JAXRPCException("Failed to create binding", var3);
      }

      this.loadTypeMapping(var1);
   }

   private URL loadFromClasspath(String var1) {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      if (var2 == null) {
         var2 = this.getClass().getClassLoader();
      }

      URL var3 = var2.getResource(var1);
      if (var3 == null) {
         var3 = this.getClass().getResource(var1);
      }

      return var3;
   }

   private void loadTypeMapping(String var1) {
      Object var2 = null;

      try {
         URL var3 = this.loadFromClasspath(var1);
         var2 = var3 == null ? new FileInputStream(var1) : var3.openStream();
         this.readXML((InputStream)var2);
      } catch (IOException var11) {
         throw new JAXRPCException("Failed to open file " + var11, var11);
      } finally {
         try {
            if (var2 != null) {
               ((InputStream)var2).close();
            }
         } catch (IOException var10) {
         }

      }

   }
}
