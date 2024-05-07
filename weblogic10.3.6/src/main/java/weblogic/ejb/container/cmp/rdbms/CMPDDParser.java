package weblogic.ejb.container.cmp.rdbms;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.compliance.Log;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.logging.Loggable;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public abstract class CMPDDParser {
   private Map bean2defaultHelper = new HashMap();
   private String fileName;
   private WeblogicRdbmsJarBean cmpDescriptor;
   protected static EJBComplianceTextFormatter fmt;
   protected static Log log;
   protected EjbDescriptorBean ejbDescriptor;
   protected String encoding = null;

   public CMPDDParser() {
      log = new Log();
      fmt = new EJBComplianceTextFormatter();
   }

   public void setEJBDescriptor(EjbDescriptorBean var1) {
      this.ejbDescriptor = var1;
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   public String getFileName() {
      return this.fileName;
   }

   public Map getRDBMSDependentMap() {
      return new HashMap();
   }

   public void setEncoding(String var1) {
      this.encoding = var1;
   }

   public void setDescriptorMBean(WeblogicRdbmsJarBean var1) {
      this.cmpDescriptor = var1;
   }

   public WeblogicRdbmsJarBean getDescriptorMBean() {
      return this.cmpDescriptor;
   }

   public void addDefaultHelper(String var1, DefaultHelper var2) {
      this.bean2defaultHelper.put(var1, var2);
   }

   public DefaultHelper getDefaultHelper(String var1) {
      return (DefaultHelper)this.bean2defaultHelper.get(var1);
   }

   private void p(String var1) {
      System.out.println("***<CMPDDParser> " + var1);
   }

   public abstract void process(Reader var1) throws IOException, XMLParsingException, XMLProcessingException;

   public abstract void process(InputStream var1) throws IOException, XMLParsingException, XMLProcessingException;

   protected void validatePositiveInteger(String var1) throws Exception {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var5) {
         Loggable var4 = EJBLogger.logparamIntegerLoggable();
         throw new Exception(var4.getMessage());
      }

      if (var2 < 0) {
         Loggable var3 = EJBLogger.logparamPositiveIntegerLoggable();
         throw new Exception(var3.getMessage());
      }
   }
}
