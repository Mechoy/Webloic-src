package weblogic.servlet.internal.dd.compliance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.stream.XMLStreamException;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.internal.WebAppDescriptor;
import weblogic.tools.ui.progress.ProgressListener;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class ComplianceUtils {
   private static final boolean debug = false;

   public static void checkCompliance(DeploymentInfo var0, ProgressListener var1) throws ErrorCollectionException {
      ComplianceChecker[] var2 = BaseComplianceChecker.makeComplianceCheckers(var0);
      ErrorCollectionException var3 = null;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var2[var4].setProgressListener(var1);
         var2[var4].setVerbose(var0.isVerbose());

         try {
            var2[var4].check(var0);
         } catch (ErrorCollectionException var8) {
            if (var3 == null) {
               var3 = new ErrorCollectionException();
            }

            if (var8 != null && !var8.isEmpty()) {
               Collection var6 = var8.getExceptions();
               Iterator var7 = var6.iterator();

               while(var7.hasNext()) {
                  var3.add((Throwable)var7.next());
               }
            }
         } catch (Exception var9) {
            if (var3 == null) {
               var3 = new ErrorCollectionException();
            }

            var3.add(var9);
         }
      }

      if (var3 != null && !var3.isEmpty()) {
         throw var3;
      }
   }

   public static void checkCompliance(File var0, ClassLoader var1, ProgressListener var2) throws ErrorCollectionException, IOException {
      WebAppBean var3 = null;
      WeblogicWebAppBean var4 = null;
      VirtualJarFile var5 = null;
      ErrorCollectionException var6 = null;

      try {
         var5 = VirtualJarFactory.createVirtualJar(var0);
         WebAppDescriptor var7 = new WebAppDescriptor(var5);
         var3 = var7.getWebAppBean();
         var4 = var7.getWeblogicWebAppBean();
      } catch (FileNotFoundException var13) {
      } catch (XMLStreamException var14) {
         if (var6 == null) {
            var6 = new ErrorCollectionException();
         }

         var6.add(var14);
         throw var6;
      } finally {
         if (var5 != null) {
            var5.close();
         }

      }

      DeploymentInfo var16 = new DeploymentInfo(var3, var4);
      var16.setClassLoader(var1);
      checkCompliance(var16, var2);
   }
}
