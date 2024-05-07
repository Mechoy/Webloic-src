package weblogic.ejb.container.dd.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;
import org.xml.sax.InputSource;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.logging.Loggable;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public abstract class DDLoader {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.dd.xml") != null;
   private static boolean verbose = System.getProperty("weblogic.ejb.container.dd.xml") != null;
   protected EjbDescriptorBean ejbDescriptor = null;
   protected Set ejbNamesWithValidatedCTs = new HashSet();
   protected boolean validate = true;
   protected String encoding = null;
   protected Set relationNames = new HashSet();
   protected HashSet ejbNames = new HashSet();

   public void setEJBDescriptor(EjbDescriptorBean var1) {
      this.ejbDescriptor = var1;
   }

   public EjbDescriptorBean getEJBDescriptor() {
      return this.ejbDescriptor;
   }

   public void setValidate(boolean var1) {
      this.validate = var1;
   }

   public void setEncoding(String var1) {
      this.encoding = var1;
   }

   public abstract void process(String var1) throws IOException, XMLParsingException, XMLProcessingException;

   public abstract void process(InputStream var1) throws IOException, XMLParsingException, XMLProcessingException;

   public abstract void process(Reader var1) throws IOException, XMLParsingException, XMLProcessingException;

   public abstract void process(File var1) throws IOException, XMLParsingException, XMLProcessingException;

   public abstract void process(InputSource var1) throws IOException, XMLParsingException, XMLProcessingException;

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

   protected void validatePositiveIntegerOrNoLimitString(String var1) throws Exception {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var4) {
         if (var1.equalsIgnoreCase("NO Limit")) {
            return;
         }

         throw new Exception("Parameter must be an integer");
      }

      if (var2 < 0) {
         throw new Exception("Parameter must be a positive integer");
      }
   }

   protected void validateConcurrencyStrategy(String var1) throws Exception {
      if (!var1.equalsIgnoreCase("readonly") && !var1.equalsIgnoreCase("readonlyexclusive") && !var1.equalsIgnoreCase("exclusive") && !var1.equalsIgnoreCase("database") && !var1.equalsIgnoreCase("optimistic")) {
         Loggable var2 = EJBLogger.logillegalConcurrencyStrategyLoggable(var1);
         throw new Exception(var2.getMessage());
      }
   }

   protected void validateIntegerGreaterThanZero(String var1) throws Exception {
      int var2;
      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var5) {
         Loggable var4 = EJBLogger.logparamIntegerLoggable();
         throw new Exception(var4.getMessage());
      }

      if (var2 <= 0) {
         Loggable var3 = EJBLogger.logparamPositiveIntegerLoggable();
         throw new Exception(var3.getMessage());
      }
   }

   protected void validateContainerTransaction(EjbJarBean var1, String var2) {
      if (!this.ejbNamesWithValidatedCTs.contains(var2)) {
         EnterpriseBeansBean var3 = var1.getEnterpriseBeans();
         SessionBeanBean[] var4 = var3.getSessions();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getEjbName().equals(var2) && var4[var5].getTransactionType().equals("Bean")) {
               Loggable var6 = EJBLogger.logContainerTransactionSetForBeanManagedEJBLoggable(var2);
               var6.log();
            }
         }

         if (var3 instanceof EnterpriseBeansBean) {
            MessageDrivenBeanBean[] var9 = var3.getMessageDrivens();

            for(int var7 = 0; var7 < var9.length; ++var7) {
               if (var9[var7].getEjbName().equals(var2) && var9[var7].getTransactionType().equals("Bean")) {
                  Loggable var8 = EJBLogger.logContainerTransactionSetForBeanManagedEJBLoggable(var2);
                  var8.log();
               }
            }
         }

         this.ejbNamesWithValidatedCTs.add(var2);
      }
   }

   protected boolean addEJBName(String var1) {
      if (this.ejbNames == null) {
         this.ejbNames = new HashSet();
      }

      return this.ejbNames.add(var1);
   }

   protected void setEntityAlwaysUsesTransactionDefault() {
      WeblogicEjbJarBean var1 = this.ejbDescriptor.getWeblogicEjbJarBean();
      var1.getWeblogicCompatibility().setEntityAlwaysUsesTransaction(true);
   }

   class PersistenceType {
      public String id;
      public String version;
   }
}
