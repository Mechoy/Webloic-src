package weblogic.ejb.container.utils.ddconverter;

import java.io.File;
import java.io.IOException;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public final class DDConverter_1020 extends DDConverterBase {
   private static final boolean debug = true;
   private EjbDescriptorBean dd;
   DDConverter_10 ddc10 = null;

   public DDConverter_1020(String[] var1, String var2, ConvertLog var3) throws DDConverterException {
      super(var1, var2, var3, true);
      this.ddc10 = new DDConverter_10(var3, true);
   }

   public boolean convert() throws DDConverterException, IOException {
      for(int var1 = 0; var1 < this.sources.length; ++var1) {
         EjbDescriptorBean var2 = null;
         System.out.println(this.fmt.converting(this.sources[var1]));

         try {
            var2 = this.loadDD(this.sources[var1]);
         } catch (Exception var5) {
            this.log.logError(this.fmt.readDDError(this.sources[var1], StackTraceUtils.throwable2StackTrace(var5)));
         }

         File var3 = new File(this.targetDirName, var2.getJarFileName());
         WorkingJar var4 = new WorkingJar(var3);
         this.writeEJBDDsToJar(var2, var4);
         var4.close();
      }

      return !this.log.hasErrors();
   }

   public boolean convert(String var1) throws DDConverterException, IOException {
      Debug.assertion(var1 != null);
      this.dd = new EjbDescriptorBean();

      for(int var2 = 0; var2 < this.sources.length; ++var2) {
         try {
            this.dd = this.loadDD(this.dd, this.sources[var2]);
         } catch (Exception var4) {
            this.log.logError(this.fmt.readDDError(this.sources[var2], StackTraceUtils.throwable2StackTrace(var4)));
         }
      }

      File var5 = new File(this.targetDirName, var1);
      WorkingJar var3 = new WorkingJar(var5);
      this.writeEJBDDsToJar(this.dd, var3);
      var3.close();
      return !this.log.hasErrors();
   }

   protected EjbDescriptorBean loadDD(EjbDescriptorBean var1, String var2) throws IOException, XMLProcessingException, XMLParsingException {
      this.dd = this.ddc10.invokeDDC10(var1, var2);
      String var3 = (new File(var2)).getName();
      String var4 = var3.substring(0, var3.length() - 3) + "jar";
      this.dd.setJarFileName(var4);
      return this.dd;
   }

   protected EjbDescriptorBean loadDD(String var1) throws IOException, XMLProcessingException, XMLParsingException {
      EjbDescriptorBean var2 = new EjbDescriptorBean();
      var2.createEjbJarBean();
      var2.createWeblogicEjbJarBean();
      var2.getEjbJarBean().createEnterpriseBeans();
      return this.loadDD(var2, var1);
   }
}
