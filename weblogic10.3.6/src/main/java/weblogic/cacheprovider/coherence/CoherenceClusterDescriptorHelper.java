package weblogic.cacheprovider.coherence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.ModuleException;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.coherence.descriptor.wl.WeblogicCoherenceBean;
import weblogic.descriptor.DescriptorManager;

public class CoherenceClusterDescriptorHelper {
   public static WeblogicCoherenceBean createCoherenceDescriptor(InputStream var0, DescriptorManager var1, List var2, boolean var3) throws ModuleException {
      AbstractDescriptorLoader2 var4 = createDescriptorLoader(var0, var1, var2, var3);

      try {
         return getCoherenceBean(var4);
      } catch (Exception var6) {
         throw new ModuleException("Could not create the Coherence Cluster descriptor", var6);
      }
   }

   public static WeblogicCoherenceBean createCoherenceDescriptor(String var0) throws ModuleException {
      if (var0 == null) {
         throw new ModuleException("Null URI specified");
      } else {
         FileInputStream var1 = null;

         WeblogicCoherenceBean var3;
         try {
            var1 = new FileInputStream(var0);
            AbstractDescriptorLoader2 var2 = createDescriptorLoader(var1, (DescriptorManager)null, (List)null, true);
            var3 = getCoherenceBean(var2);
         } catch (Exception var12) {
            throw new ModuleException(var12);
         } finally {
            try {
               if (var1 != null) {
                  var1.close();
               }
            } catch (Exception var11) {
            }

         }

         return var3;
      }
   }

   private static WeblogicCoherenceBean getCoherenceBean(AbstractDescriptorLoader2 var0) throws IOException, XMLStreamException {
      return (WeblogicCoherenceBean)var0.loadDescriptorBean();
   }

   private static AbstractDescriptorLoader2 createDescriptorLoader(InputStream var0, DescriptorManager var1, List var2, boolean var3) {
      return new AbstractDescriptorLoader2(var0, var1, var2, var3) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            return CoherenceClusterDescriptorHelper.createVersionMunger(var1, this);
         }
      };
   }

   private static VersionMunger createVersionMunger(InputStream var0, AbstractDescriptorLoader2 var1) throws XMLStreamException {
      String var2 = "weblogic.coherence.descriptor.wl.WeblogicCoherenceBeanImpl$SchemaHelper2";
      return new VersionMunger(var0, var1, var2, "http://xmlns.oracle.com/weblogic/weblogic-coherence");
   }
}
