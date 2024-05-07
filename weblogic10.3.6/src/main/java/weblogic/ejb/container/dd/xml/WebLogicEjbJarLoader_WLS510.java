package weblogic.ejb.container.dd.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.InputSource;
import weblogic.j2ee.descriptor.wl.EjbReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.IdempotentMethodsBean;
import weblogic.j2ee.descriptor.wl.MethodBean;
import weblogic.j2ee.descriptor.wl.MethodParamsBean;
import weblogic.j2ee.descriptor.wl.PersistenceUseBean;
import weblogic.j2ee.descriptor.wl.ResourceDescriptionBean;
import weblogic.j2ee.descriptor.wl.SecurityRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.TransactionDescriptorBean;
import weblogic.j2ee.descriptor.wl.TransactionIsolationBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.xml.process.Functions;
import weblogic.xml.process.InProcessor;
import weblogic.xml.process.ProcessingContext;
import weblogic.xml.process.ProcessorDriver;
import weblogic.xml.process.SAXProcessorException;
import weblogic.xml.process.SAXValidationException;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;
import weblogic.xml.process.XMLProcessor;

public final class WebLogicEjbJarLoader_WLS510 extends DDLoader implements XMLProcessor, InProcessor {
   private static final boolean debug = true;
   private static final boolean verbose = true;
   private static final Map paths = new HashMap();
   private ProcessorDriver driver;
   private static final String publicId = "-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB//EN";
   private static final String localDTDResourceName = "/weblogic/ejb/container/dd/xml/weblogic510-ejb-jar.dtd";

   public ProcessorDriver getDriver() {
      return this.driver;
   }

   public WebLogicEjbJarLoader_WLS510() {
      this(true);
   }

   public WebLogicEjbJarLoader_WLS510(boolean var1) {
      this.driver = new ProcessorDriver(this, "-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB//EN", "/weblogic/ejb/container/dd/xml/weblogic510-ejb-jar.dtd", var1);
   }

   public void process(String var1) throws IOException, XMLParsingException, XMLProcessingException {
      this.driver.process(var1);
   }

   public void process(File var1) throws IOException, XMLParsingException, XMLProcessingException {
      this.driver.process(var1);
   }

   public void process(Reader var1) throws IOException, XMLParsingException, XMLProcessingException {
      this.driver.process(var1);
   }

   public void process(InputSource var1) throws IOException, XMLParsingException, XMLProcessingException {
      this.driver.process(var1);
   }

   public void process(InputStream var1) throws IOException, XMLParsingException, XMLProcessingException {
      this.driver.process(var1);
   }

   public void preProc(ProcessingContext var1) throws SAXProcessorException {
      Debug.assertion(var1 != null);
      String var2 = var1.getPath();
      Debug.assertion(var2 != null);
      Integer var3 = (Integer)paths.get(var2);
      if (var3 != null) {
         switch (var3) {
            case 128:
               this.__pre_128(var1);
               this.__post_128(var1);
               break;
            case 129:
               this.__pre_129(var1);
               break;
            case 130:
               this.__pre_130(var1);
               this.__post_130(var1);
               break;
            case 131:
               this.__pre_131(var1);
               break;
            case 132:
               this.__pre_132(var1);
               break;
            case 133:
               this.__pre_133(var1);
               break;
            case 134:
               this.__pre_134(var1);
               break;
            case 135:
               this.__pre_135(var1);
               break;
            case 136:
               this.__pre_136(var1);
               break;
            case 137:
               this.__pre_137(var1);
               break;
            case 138:
               this.__pre_138(var1);
               break;
            case 139:
               this.__pre_139(var1);
               break;
            case 140:
               this.__pre_140(var1);
               break;
            case 141:
               this.__pre_141(var1);
               break;
            case 142:
               this.__pre_142(var1);
               break;
            case 143:
               this.__pre_143(var1);
               break;
            case 144:
               this.__pre_144(var1);
               break;
            case 145:
               this.__pre_145(var1);
               break;
            case 146:
               this.__pre_146(var1);
               break;
            case 147:
               this.__pre_147(var1);
               break;
            case 148:
               this.__pre_148(var1);
               break;
            case 149:
               this.__pre_149(var1);
               break;
            case 150:
               this.__pre_150(var1);
               break;
            case 151:
               this.__pre_151(var1);
               this.__post_151(var1);
               break;
            case 152:
               this.__pre_152(var1);
               break;
            case 153:
               this.__pre_153(var1);
               break;
            case 154:
               this.__pre_154(var1);
               break;
            case 155:
               this.__pre_155(var1);
               break;
            case 156:
               this.__pre_156(var1);
               break;
            case 157:
               this.__pre_157(var1);
               break;
            case 158:
               this.__pre_158(var1);
               break;
            case 159:
               this.__pre_159(var1);
               break;
            case 160:
               this.__pre_160(var1);
               break;
            case 161:
               this.__pre_161(var1);
               break;
            case 162:
               this.__pre_162(var1);
               this.__post_162(var1);
               break;
            case 163:
               this.__pre_163(var1);
               break;
            case 164:
               this.__pre_164(var1);
               this.__post_164(var1);
               break;
            case 165:
               this.__pre_165(var1);
               break;
            case 166:
               this.__pre_166(var1);
               break;
            case 167:
               this.__pre_167(var1);
               break;
            case 168:
               this.__pre_168(var1);
               break;
            case 169:
               this.__pre_169(var1);
               this.__post_169(var1);
               break;
            case 170:
               this.__pre_170(var1);
               break;
            case 171:
               this.__pre_171(var1);
               this.__post_171(var1);
               break;
            case 172:
               this.__pre_172(var1);
               break;
            case 173:
               this.__pre_173(var1);
               break;
            case 174:
               this.__pre_174(var1);
               this.__post_174(var1);
               break;
            case 175:
               this.__pre_175(var1);
               break;
            case 176:
               this.__pre_176(var1);
               break;
            case 177:
               this.__pre_177(var1);
               this.__post_177(var1);
               break;
            case 178:
               this.__pre_178(var1);
               break;
            case 179:
               this.__pre_179(var1);
               break;
            default:
               throw new AssertionError(var3.toString());
         }

      }
   }

   public void postProc(ProcessingContext var1) throws SAXProcessorException {
      Debug.assertion(var1 != null);
      String var2 = var1.getPath();
      Debug.assertion(var2 != null);
      Integer var3 = (Integer)paths.get(var2);
      if (var3 != null) {
         switch (var3) {
            case 128:
            case 130:
            case 151:
            case 162:
            case 164:
            case 169:
            case 171:
            case 174:
            case 177:
               break;
            case 129:
               this.__post_129(var1);
               break;
            case 131:
               this.__post_131(var1);
               break;
            case 132:
               this.__post_132(var1);
               break;
            case 133:
               this.__post_133(var1);
               break;
            case 134:
               this.__post_134(var1);
               break;
            case 135:
               this.__post_135(var1);
               break;
            case 136:
               this.__post_136(var1);
               break;
            case 137:
               this.__post_137(var1);
               break;
            case 138:
               this.__post_138(var1);
               break;
            case 139:
               this.__post_139(var1);
               break;
            case 140:
               this.__post_140(var1);
               break;
            case 141:
               this.__post_141(var1);
               break;
            case 142:
               this.__post_142(var1);
               break;
            case 143:
               this.__post_143(var1);
               break;
            case 144:
               this.__post_144(var1);
               break;
            case 145:
               this.__post_145(var1);
               break;
            case 146:
               this.__post_146(var1);
               break;
            case 147:
               this.__post_147(var1);
               break;
            case 148:
               this.__post_148(var1);
               break;
            case 149:
               this.__post_149(var1);
               break;
            case 150:
               this.__post_150(var1);
               break;
            case 152:
               this.__post_152(var1);
               break;
            case 153:
               this.__post_153(var1);
               break;
            case 154:
               this.__post_154(var1);
               break;
            case 155:
               this.__post_155(var1);
               break;
            case 156:
               this.__post_156(var1);
               break;
            case 157:
               this.__post_157(var1);
               break;
            case 158:
               this.__post_158(var1);
               break;
            case 159:
               this.__post_159(var1);
               break;
            case 160:
               this.__post_160(var1);
               break;
            case 161:
               this.__post_161(var1);
               break;
            case 163:
               this.__post_163(var1);
               break;
            case 165:
               this.__post_165(var1);
               break;
            case 166:
               this.__post_166(var1);
               break;
            case 167:
               this.__post_167(var1);
               break;
            case 168:
               this.__post_168(var1);
               break;
            case 170:
               this.__post_170(var1);
               break;
            case 172:
               this.__post_172(var1);
               break;
            case 173:
               this.__post_173(var1);
               break;
            case 175:
               this.__post_175(var1);
               break;
            case 176:
               this.__post_176(var1);
               break;
            case 178:
               this.__post_178(var1);
               break;
            case 179:
               this.__post_179(var1);
               break;
            default:
               throw new AssertionError(var3.toString());
         }

      }
   }

   private void __pre_131(ProcessingContext var1) {
   }

   private void __post_131(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6563363](.weblogic-ejb-jar.weblogic-enterprise-bean.ejb-name.) must be a non-empty string");
      } else {
         var3.setEjbName(var2.replace('/', '.'));
         var3.setEnableCallByReference(true);
         var4.initialize(this.ejbDescriptor, var3);
         if (var4.beanDesc == null) {
            throw new SAXValidationException("Could not locate bean with ejb-name \"" + var2 + "\" in ejb-jar.xml");
         }
      }
   }

   private void __pre_154(ProcessingContext var1) {
   }

   private void __post_154(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6595526](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.home-is-clusterable.) must be a non-empty string");
      } else if (!"true".equals(var2) && !"True".equals(var2) && !"false".equals(var2) && !"False".equals(var2)) {
         throw new SAXValidationException("PAction[6595526](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.home-is-clusterable.) must be one of the values: true,True,false,False");
      } else {
         if (var4.isStateful()) {
            var4.getStatefulDescriptor().getStatefulSessionClustering().setHomeIsClusterable("True".equalsIgnoreCase(var2));
         } else if (var4.isEntity()) {
            var4.getEntityDescriptor().getEntityClustering().setHomeIsClusterable("True".equalsIgnoreCase(var2));
         }

         if (var4.isStateless()) {
            var4.getStatelessDescriptor().getStatelessClustering().setHomeIsClusterable("True".equalsIgnoreCase(var2));
         }

      }
   }

   private void __pre_132(ProcessingContext var1) {
   }

   private void __post_132(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6567294](.weblogic-ejb-jar.weblogic-enterprise-bean.enable-call-by-reference.) must be a non-empty string");
      } else if (!"true".equals(var2) && !"True".equals(var2) && !"false".equals(var2) && !"False".equals(var2)) {
         throw new SAXValidationException("PAction[6567294](.weblogic-ejb-jar.weblogic-enterprise-bean.enable-call-by-reference.) must be one of the values: true,True,false,False");
      } else {
         var3.setEnableCallByReference("True".equalsIgnoreCase(var2));
      }
   }

   private void __pre_169(ProcessingContext var1) {
      Object var2 = null;
      var1.addBoundObject(var2, "params");
   }

   private void __post_169(ProcessingContext var1) throws SAXProcessorException {
      MethodParamsBean var2 = (MethodParamsBean)var1.getBoundObject("params");
      MethodBean var3 = (MethodBean)var1.getBoundObject("method");
      TransactionIsolationBean var4 = (TransactionIsolationBean)var1.getBoundObject("tiso");
      WeblogicEnterpriseBeanBean var5 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var6 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var7 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      this.addBoundObject(var3.createMethodParams(), "params");
   }

   private void __pre_160(ProcessingContext var1) {
   }

   private void __post_160(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6604108](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.stateless-bean-methods-are-idempotent.) must be a non-empty string");
      } else if (!"true".equals(var2) && !"True".equals(var2) && !"false".equals(var2) && !"False".equals(var2)) {
         throw new SAXValidationException("PAction[6604108](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.stateless-bean-methods-are-idempotent.) must be one of the values: true,True,false,False");
      } else {
         if (var4.isStateless() && "True".equalsIgnoreCase(var2)) {
            IdempotentMethodsBean var6 = var5.getIdempotentMethods();
            if (var6 == null) {
               var6 = var5.createIdempotentMethods();
            }

            MethodBean var7 = var6.createMethod();
            var7.setEjbName(var3.getEjbName());
            var7.setMethodName("*");
         }

      }
   }

   private void __pre_179(ProcessingContext var1) {
   }

   private void __post_179(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SecurityRoleAssignmentBean var3 = (SecurityRoleAssignmentBean)var1.getBoundObject("sra");
      WeblogicEjbJarBean var4 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6626214](.weblogic-ejb-jar.security-role-assignment.principal-name.) must be a non-empty string");
      } else {
         var3.addPrincipalName(var2);
      }
   }

   private void __pre_151(ProcessingContext var1) {
      Object var2 = null;
      var1.addBoundObject(var2, "pu");
   }

   private void __post_151(ProcessingContext var1) throws SAXProcessorException {
      PersistenceUseBean var2 = (PersistenceUseBean)var1.getBoundObject("pu");
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      this.addBoundObject(var4.getPersistence().createPersistenceUse(), "pu");
   }

   private void __pre_175(ProcessingContext var1) {
   }

   private void __post_175(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EjbReferenceDescriptionBean var3 = (EjbReferenceDescriptionBean)var1.getBoundObject("ejbRefDesc");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6621973](.weblogic-ejb-jar.weblogic-enterprise-bean.reference-descriptor.ejb-reference-description.ejb-ref-name.) must be a non-empty string");
      } else {
         var3.setEjbRefName(var2);
      }
   }

   private void __pre_129(ProcessingContext var1) {
   }

   private void __post_129(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      WeblogicEjbJarBean var3 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      var3.setDescription(var2);
   }

   private void __pre_144(ProcessingContext var1) {
   }

   private void __post_144(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6584830](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.delay-updates-until-end-of-tx.) must be a non-empty string");
      } else if (!"true".equals(var2) && !"True".equals(var2) && !"false".equals(var2) && !"False".equals(var2)) {
         throw new SAXValidationException("PAction[6584830](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.delay-updates-until-end-of-tx.) must be one of the values: true,True,false,False");
      } else {
         if (var4.isEntity()) {
            var4.getPersistence().setDelayUpdatesUntilEndOfTx("True".equalsIgnoreCase(var2));
         }

      }
   }

   private void __pre_173(ProcessingContext var1) {
   }

   private void __post_173(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ResourceDescriptionBean var3 = (ResourceDescriptionBean)var1.getBoundObject("resDesc");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6619818](.weblogic-ejb-jar.weblogic-enterprise-bean.reference-descriptor.resource-description.jndi-name.) must be a non-empty string");
      } else {
         var3.setJNDIName(var2);
      }
   }

   private void __pre_155(ProcessingContext var1) {
   }

   private void __post_155(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6597296](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.home-load-algorithm.) must be a non-empty string");
      } else if (!"roundrobin".equals(var2) && !"RoundRobin".equals(var2) && !"round-robin".equals(var2) && !"random".equals(var2) && !"Random".equals(var2) && !"weightbased".equals(var2) && !"WeightBased".equals(var2) && !"weight-based".equals(var2)) {
         throw new SAXValidationException("PAction[6597296](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.home-load-algorithm.) must be one of the values: roundrobin,RoundRobin,round-robin,random,Random,weightbased,WeightBased,weight-based");
      } else {
         if (var4.isStateful()) {
            var4.getStatefulDescriptor().getStatefulSessionClustering().setHomeLoadAlgorithm(var2);
         } else if (var4.isEntity()) {
            var4.getEntityDescriptor().getEntityClustering().setHomeLoadAlgorithm(var2);
         }

         if (var4.isStateless()) {
            var4.getStatelessDescriptor().getStatelessClustering().setHomeLoadAlgorithm(var2);
         }

      }
   }

   private void __pre_141(ProcessingContext var1) {
   }

   private void __post_141(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6581183](.weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.read-timeout-seconds.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var7) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var7.getMessage());
         }

         int var6 = Integer.parseInt(var2);
         if (var4.isEntity()) {
            var4.getEntityDescriptor().getEntityCache().setReadTimeoutSeconds(var6);
         }

      }
   }

   private void __pre_149(ProcessingContext var1) {
   }

   private void __post_149(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6589706](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.db-is-shared.) must be a non-empty string");
      } else if (!"true".equals(var2) && !"True".equals(var2) && !"false".equals(var2) && !"False".equals(var2)) {
         throw new SAXValidationException("PAction[6589706](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.db-is-shared.) must be one of the values: true,True,false,False");
      } else {
         if (var4.isEntity()) {
            var4.getEntityDescriptor().getEntityCache().setCacheBetweenTransactions(!"True".equalsIgnoreCase(var2));
         }

      }
   }

   private void __pre_143(ProcessingContext var1) {
   }

   private void __post_143(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6583665](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.finders-call-ejbload.) must be a non-empty string");
      } else if (!"true".equals(var2) && !"True".equals(var2) && !"false".equals(var2) && !"False".equals(var2)) {
         throw new SAXValidationException("PAction[6583665](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.finders-call-ejbload.) must be one of the values: true,True,false,False");
      } else {
         if (var4.isEntity()) {
            var4.getPersistence().setFindersLoadBean("True".equalsIgnoreCase(var2));
         }

      }
   }

   private void __pre_162(ProcessingContext var1) {
      Object var2 = null;
      var1.addBoundObject(var2, "tiso");
   }

   private void __post_162(ProcessingContext var1) throws SAXProcessorException {
      TransactionIsolationBean var2 = (TransactionIsolationBean)var1.getBoundObject("tiso");
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      this.addBoundObject(var5.createTransactionIsolation(), "tiso");
   }

   private void __pre_165(ProcessingContext var1) {
   }

   private void __post_165(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      MethodBean var3 = (MethodBean)var1.getBoundObject("method");
      TransactionIsolationBean var4 = (TransactionIsolationBean)var1.getBoundObject("tiso");
      WeblogicEnterpriseBeanBean var5 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var6 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var7 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      var3.setDescription(var2);
   }

   private void __pre_177(ProcessingContext var1) {
      Object var2 = null;
      var1.addBoundObject(var2, "sra");
   }

   private void __post_177(ProcessingContext var1) throws SAXProcessorException {
      SecurityRoleAssignmentBean var2 = (SecurityRoleAssignmentBean)var1.getBoundObject("sra");
      WeblogicEjbJarBean var3 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      this.addBoundObject(var3.createSecurityRoleAssignment(), "sra");
   }

   private void __pre_168(ProcessingContext var1) {
   }

   private void __post_168(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      MethodBean var3 = (MethodBean)var1.getBoundObject("method");
      TransactionIsolationBean var4 = (TransactionIsolationBean)var1.getBoundObject("tiso");
      WeblogicEnterpriseBeanBean var5 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var6 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var7 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6614703](.weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.method.method-name.) must be a non-empty string");
      } else {
         var3.setMethodName(var2);
      }
   }

   private void __pre_135(ProcessingContext var1) {
   }

   private void __post_135(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6573015](.weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.max-beans-in-free-pool.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveIntegerOrNoLimitString(var2);
         } catch (Exception var9) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var9.getMessage());
         }

         int var6;
         try {
            var6 = Integer.parseInt(var2);
         } catch (NumberFormatException var8) {
            var6 = Integer.MAX_VALUE;
         }

         if (var4.isStateless()) {
            var4.getStatelessDescriptor().getPool().setMaxBeansInFreePool(var6);
         } else if (var4.isEntity()) {
            var4.getEntityDescriptor().getPool().setMaxBeansInFreePool(var6);
         }

      }
   }

   private void __pre_153(ProcessingContext var1) {
   }

   private void __post_153(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PersistenceUseBean var3 = (PersistenceUseBean)var1.getBoundObject("pu");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6594271](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-use.type-version.) must be a non-empty string");
      } else {
         if (var5.isEntity()) {
            var3.setTypeVersion(var2);
            var3.setTypeStorage(var5.getPersistenceStorage(var3.getTypeIdentifier(), var3.getTypeVersion(), var4.getEjbName()));
         }

      }
   }

   private void __pre_139(ProcessingContext var1) {
   }

   private void __post_139(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6578518](.weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.cache-type.) must be a non-empty string");
      } else if (!"NRU".equals(var2) && !"LRU".equals(var2)) {
         throw new SAXValidationException("PAction[6578518](.weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.cache-type.) must be one of the values: NRU,LRU");
      } else {
         if (var4.isStateful()) {
            var4.getStatefulDescriptor().getStatefulSessionCache().setCacheType(var2);
         }

      }
   }

   private void __pre_142(ProcessingContext var1) {
   }

   private void __post_142(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6582553](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.is-modified-method-name.) must be a non-empty string");
      } else {
         if (var4.isEntity()) {
            var4.getPersistence().setIsModifiedMethodName(var2);
         }

      }
   }

   private void __pre_146(ProcessingContext var1) {
   }

   private void __post_146(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      DDLoader.PersistenceType var3 = (DDLoader.PersistenceType)var1.getBoundObject("pType");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6586751](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-type.type-identifier.) must be a non-empty string");
      } else {
         var3.id = var2;
      }
   }

   private void __pre_137(ProcessingContext var1) {
   }

   private void __post_137(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6575788](.weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.max-beans-in-cache.) must be a non-empty string");
      } else {
         try {
            this.validateIntegerGreaterThanZero(var2);
         } catch (Exception var7) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var7.getMessage());
         }

         int var6 = Integer.parseInt(var2);
         if (var4.isStateful()) {
            var4.getStatefulDescriptor().getStatefulSessionCache().setMaxBeansInCache(var6);
         } else if (var4.isEntity()) {
            var4.getEntityDescriptor().getEntityCache().setMaxBeansInCache(var6);
         }

      }
   }

   private void __pre_152(ProcessingContext var1) {
   }

   private void __post_152(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PersistenceUseBean var3 = (PersistenceUseBean)var1.getBoundObject("pu");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6593181](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-use.type-identifier.) must be a non-empty string");
      } else {
         if (var5.isEntity()) {
            var3.setTypeIdentifier(var2);
         }

      }
   }

   private void __pre_130(ProcessingContext var1) {
      Object var2 = null;
      var1.addBoundObject(var2, "wlBean");
      WLDD51Helper var3 = new WLDD51Helper();
      var1.addBoundObject(var3, "bd");
   }

   private void __post_130(ProcessingContext var1) throws SAXProcessorException {
      WeblogicEnterpriseBeanBean var2 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var3 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var4 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      this.addBoundObject(var4.createWeblogicEnterpriseBean(), "wlBean");
   }

   private void __pre_145(ProcessingContext var1) {
      DDLoader.PersistenceType var2 = new DDLoader.PersistenceType();
      var1.addBoundObject(var2, "pType");
   }

   private void __post_145(ProcessingContext var1) throws SAXProcessorException {
      DDLoader.PersistenceType var2 = (DDLoader.PersistenceType)var1.getBoundObject("pType");
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
   }

   private void __pre_166(ProcessingContext var1) {
   }

   private void __post_166(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      MethodBean var3 = (MethodBean)var1.getBoundObject("method");
      TransactionIsolationBean var4 = (TransactionIsolationBean)var1.getBoundObject("tiso");
      WeblogicEnterpriseBeanBean var5 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var6 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var7 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6612553](.weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.method.ejb-name.) must be a non-empty string");
      } else {
         var3.setEjbName(var2.replace('/', '.'));
      }
   }

   private void __pre_178(ProcessingContext var1) {
   }

   private void __post_178(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SecurityRoleAssignmentBean var3 = (SecurityRoleAssignmentBean)var1.getBoundObject("sra");
      WeblogicEjbJarBean var4 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6625213](.weblogic-ejb-jar.security-role-assignment.role-name.) must be a non-empty string");
      } else {
         var3.setRoleName(var2);
      }
   }

   private void __pre_167(ProcessingContext var1) {
   }

   private void __post_167(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      MethodBean var3 = (MethodBean)var1.getBoundObject("method");
      TransactionIsolationBean var4 = (TransactionIsolationBean)var1.getBoundObject("tiso");
      WeblogicEnterpriseBeanBean var5 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var6 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var7 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6613650](.weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.method.method-intf.) must be a non-empty string");
      } else if (!"home".equals(var2) && !"Home".equals(var2) && !"remote".equals(var2) && !"Remote".equals(var2)) {
         throw new SAXValidationException("PAction[6613650](.weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.method.method-intf.) must be one of the values: home,Home,remote,Remote");
      } else {
         var3.setMethodIntf(var2);
      }
   }

   private void __pre_128(ProcessingContext var1) {
      Object var2 = null;
      var1.addBoundObject(var2, "wlJar");
   }

   private void __post_128(ProcessingContext var1) throws SAXProcessorException {
      WeblogicEjbJarBean var2 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      var2 = this.ejbDescriptor.createWeblogicEjbJarBean(this.encoding);
      this.addBoundObject(var2, "wlJar");
      this.setEntityAlwaysUsesTransactionDefault();
   }

   private void __pre_150(ProcessingContext var1) {
   }

   private void __post_150(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6590910](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.stateful-session-persistent-store-dir.) must be a non-empty string");
      } else {
         if (var4.isStateful()) {
            var4.getStatefulDescriptor().setPersistentStoreDir(var2);
         }

      }
   }

   private void __pre_133(ProcessingContext var1) {
   }

   private void __post_133(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6570924](.weblogic-ejb-jar.weblogic-enterprise-bean.clients-on-same-server.) must be a non-empty string");
      } else if (!"true".equals(var2) && !"True".equals(var2) && !"false".equals(var2) && !"False".equals(var2)) {
         throw new SAXValidationException("PAction[6570924](.weblogic-ejb-jar.weblogic-enterprise-bean.clients-on-same-server.) must be one of the values: true,True,false,False");
      } else {
         var3.setClientsOnSameServer("True".equalsIgnoreCase(var2));
      }
   }

   private void __pre_148(ProcessingContext var1) {
   }

   private void __post_148(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      DDLoader.PersistenceType var3 = (DDLoader.PersistenceType)var1.getBoundObject("pType");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6588699](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-type.type-storage.) must be a non-empty string");
      } else {
         var5.addPersistenceType(var3.id, var3.version, var2);
      }
   }

   private void __pre_171(ProcessingContext var1) {
      Object var2 = null;
      var1.addBoundObject(var2, "resDesc");
   }

   private void __post_171(ProcessingContext var1) throws SAXProcessorException {
      ResourceDescriptionBean var2 = (ResourceDescriptionBean)var1.getBoundObject("resDesc");
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      this.addBoundObject(var3.createResourceDescription(), "resDesc");
   }

   private void __pre_176(ProcessingContext var1) {
   }

   private void __post_176(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EjbReferenceDescriptionBean var3 = (EjbReferenceDescriptionBean)var1.getBoundObject("ejbRefDesc");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6622985](.weblogic-ejb-jar.weblogic-enterprise-bean.reference-descriptor.ejb-reference-description.jndi-name.) must be a non-empty string");
      } else {
         var3.setJNDIName(var2);
      }
   }

   private void __pre_157(ProcessingContext var1) {
   }

   private void __post_157(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6600447](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.stateless-bean-is-clusterable.) must be a non-empty string");
      } else if (!"true".equals(var2) && !"True".equals(var2) && !"false".equals(var2) && !"False".equals(var2)) {
         throw new SAXValidationException("PAction[6600447](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.stateless-bean-is-clusterable.) must be one of the values: true,True,false,False");
      } else {
         if (var4.isStateless()) {
            var4.getStatelessDescriptor().getStatelessClustering().setStatelessBeanIsClusterable("True".equalsIgnoreCase(var2));
         }

      }
   }

   private void __pre_164(ProcessingContext var1) {
      Object var2 = null;
      var1.addBoundObject(var2, "method");
   }

   private void __post_164(ProcessingContext var1) throws SAXProcessorException {
      MethodBean var2 = (MethodBean)var1.getBoundObject("method");
      TransactionIsolationBean var3 = (TransactionIsolationBean)var1.getBoundObject("tiso");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      this.addBoundObject(var3.createMethod(), "method");
   }

   private void __pre_161(ProcessingContext var1) {
      Object var2 = null;
      var1.addBoundObject(var2, "td");
   }

   private void __post_161(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      TransactionDescriptorBean var3 = (TransactionDescriptorBean)var1.getBoundObject("td");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6605798](.weblogic-ejb-jar.weblogic-enterprise-bean.transaction-descriptor.trans-timeout-seconds.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3 = var4.createTransactionDescriptor();
         this.addBoundObject(var3, "td");
         int var7 = Integer.parseInt(var2);
         var3.setTransTimeoutSeconds(var7);
      }
   }

   private void __pre_170(ProcessingContext var1) {
   }

   private void __post_170(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      MethodParamsBean var3 = (MethodParamsBean)var1.getBoundObject("params");
      MethodBean var4 = (MethodBean)var1.getBoundObject("method");
      TransactionIsolationBean var5 = (TransactionIsolationBean)var1.getBoundObject("tiso");
      WeblogicEnterpriseBeanBean var6 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var7 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var8 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      var3.addMethodParam(var2);
   }

   private void __pre_138(ProcessingContext var1) {
   }

   private void __post_138(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6577152](.weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.idle-timeout-seconds.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var7) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var7.getMessage());
         }

         int var6 = Integer.parseInt(var2);
         if (var4.isStateful()) {
            var4.getStatefulDescriptor().getStatefulSessionCache().setIdleTimeoutSeconds(var6);
         } else if (var4.isEntity()) {
            var4.getEntityDescriptor().getEntityCache().setIdleTimeoutSeconds(var6);
         }

      }
   }

   private void __pre_174(ProcessingContext var1) {
      Object var2 = null;
      var1.addBoundObject(var2, "ejbRefDesc");
   }

   private void __post_174(ProcessingContext var1) throws SAXProcessorException {
      EjbReferenceDescriptionBean var2 = (EjbReferenceDescriptionBean)var1.getBoundObject("ejbRefDesc");
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      this.addBoundObject(var3.createEjbReferenceDescription(), "ejbRefDesc");
   }

   private void __pre_158(ProcessingContext var1) {
   }

   private void __post_158(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6601675](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.stateless-bean-load-algorithm.) must be a non-empty string");
      } else if (!"roundrobin".equals(var2) && !"RoundRobin".equals(var2) && !"round-robin".equals(var2) && !"random".equals(var2) && !"Random".equals(var2) && !"weightbased".equals(var2) && !"WeightBased".equals(var2) && !"weight-based".equals(var2)) {
         throw new SAXValidationException("PAction[6601675](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.stateless-bean-load-algorithm.) must be one of the values: roundrobin,RoundRobin,round-robin,random,Random,weightbased,WeightBased,weight-based");
      } else {
         if (var4.isStateless()) {
            var4.getStatelessDescriptor().getStatelessClustering().setStatelessBeanLoadAlgorithm(var2);
         }

      }
   }

   private void __pre_172(ProcessingContext var1) {
   }

   private void __post_172(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ResourceDescriptionBean var3 = (ResourceDescriptionBean)var1.getBoundObject("resDesc");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6618813](.weblogic-ejb-jar.weblogic-enterprise-bean.reference-descriptor.resource-description.res-ref-name.) must be a non-empty string");
      } else {
         var3.setResRefName(var2);
      }
   }

   private void __pre_140(ProcessingContext var1) {
   }

   private void __post_140(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6579724](.weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.cache-strategy.) must be a non-empty string");
      } else if (!"read-only".equals(var2) && !"Read-Only".equals(var2) && !"read-write".equals(var2) && !"Read-Write".equals(var2)) {
         throw new SAXValidationException("PAction[6579724](.weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.cache-strategy.) must be one of the values: read-only,Read-Only,read-write,Read-Write");
      } else {
         if (var4.isEntity()) {
            if ("Read-Only".equalsIgnoreCase(var2)) {
               var4.getEntityDescriptor().getEntityCache().setConcurrencyStrategy("ReadOnly");
            } else {
               var4.getEntityDescriptor().getEntityCache().setConcurrencyStrategy("Exclusive");
            }
         }

      }
   }

   private void __pre_159(ProcessingContext var1) {
   }

   private void __post_159(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6602923](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.stateless-bean-call-router-class-name.) must be a non-empty string");
      } else {
         if (var4.isStateless()) {
            var4.getStatelessDescriptor().getStatelessClustering().setStatelessBeanCallRouterClassName(var2);
         }

      }
   }

   private void __pre_147(ProcessingContext var1) {
   }

   private void __post_147(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      DDLoader.PersistenceType var3 = (DDLoader.PersistenceType)var1.getBoundObject("pType");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6587724](.weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-type.type-version.) must be a non-empty string");
      } else {
         var3.version = var2;
      }
   }

   private void __pre_134(ProcessingContext var1) {
   }

   private void __post_134(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6572010](.weblogic-ejb-jar.weblogic-enterprise-bean.jndi-name.) must be a non-empty string");
      } else {
         var3.setJNDIName(var2);
      }
   }

   private void __pre_136(ProcessingContext var1) {
   }

   private void __post_136(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6574536](.weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.initial-beans-in-free-pool.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var7) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var7.getMessage());
         }

         int var6 = Integer.parseInt(var2);
         if (var4.isStateless()) {
            var4.getStatelessDescriptor().getPool().setInitialBeansInFreePool(var6);
         }

      }
   }

   private void __pre_156(ProcessingContext var1) {
   }

   private void __post_156(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      WeblogicEnterpriseBeanBean var3 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var4 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var5 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6598830](.weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.home-call-router-class-name.) must be a non-empty string");
      } else {
         if (var4.isStateful()) {
            var4.getStatefulDescriptor().getStatefulSessionClustering().setHomeCallRouterClassName(var2);
         } else if (var4.isEntity()) {
            var4.getEntityDescriptor().getEntityClustering().setHomeCallRouterClassName(var2);
         }

         if (var4.isStateless()) {
            var4.getStatelessDescriptor().getStatelessClustering().setHomeCallRouterClassName(var2);
         }

      }
   }

   private void __pre_163(ProcessingContext var1) {
   }

   private void __post_163(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      TransactionIsolationBean var3 = (TransactionIsolationBean)var1.getBoundObject("tiso");
      WeblogicEnterpriseBeanBean var4 = (WeblogicEnterpriseBeanBean)var1.getBoundObject("wlBean");
      WLDD51Helper var5 = (WLDD51Helper)var1.getBoundObject("bd");
      WeblogicEjbJarBean var6 = (WeblogicEjbJarBean)var1.getBoundObject("wlJar");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6608695](.weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.isolation-level.) must be a non-empty string");
      } else if (!"TRANSACTION_SERIALIZABLE".equals(var2) && !"TRANSACTION_READ_COMMITTED".equals(var2) && !"TRANSACTION_READ_UNCOMMITTED".equals(var2) && !"TRANSACTION_REPEATABLE_READ".equals(var2)) {
         throw new SAXValidationException("PAction[6608695](.weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.isolation-level.) must be one of the values: TRANSACTION_SERIALIZABLE,TRANSACTION_READ_COMMITTED,TRANSACTION_READ_UNCOMMITTED,TRANSACTION_REPEATABLE_READ");
      } else {
         String var7 = var2;
         if ("TRANSACTION_SERIALIZABLE".equalsIgnoreCase(var2)) {
            var7 = "TransactionSerializable";
         } else if ("TRANSACTION_READ_COMMITTED".equalsIgnoreCase(var2)) {
            var7 = "TransactionReadCommitted";
         } else if ("TRANSACTION_READ_UNCOMMITTED".equalsIgnoreCase(var2)) {
            var7 = "TransactionReadUncommitted";
         } else if ("TRANSACTION_REPEATABLE_READ".equalsIgnoreCase(var2)) {
            var7 = "TransactionRepeatableRead";
         }

         var3.setIsolationLevel(var7);
      }
   }

   public void addBoundObject(Object var1, String var2) {
      this.driver.currentNode().addBoundObject(var1, var2);
   }

   static {
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.ejb-name.", new Integer(131));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.home-is-clusterable.", new Integer(154));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.enable-call-by-reference.", new Integer(132));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.method.method-params.", new Integer(169));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.stateless-bean-methods-are-idempotent.", new Integer(160));
      paths.put(".weblogic-ejb-jar.security-role-assignment.principal-name.", new Integer(179));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-use.", new Integer(151));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.reference-descriptor.ejb-reference-description.ejb-ref-name.", new Integer(175));
      paths.put(".weblogic-ejb-jar.description.", new Integer(129));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.delay-updates-until-end-of-tx.", new Integer(144));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.reference-descriptor.resource-description.jndi-name.", new Integer(173));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.home-load-algorithm.", new Integer(155));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.read-timeout-seconds.", new Integer(141));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.db-is-shared.", new Integer(149));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.finders-call-ejbload.", new Integer(143));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.", new Integer(162));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.method.description.", new Integer(165));
      paths.put(".weblogic-ejb-jar.security-role-assignment.", new Integer(177));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.method.method-name.", new Integer(168));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.max-beans-in-free-pool.", new Integer(135));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-use.type-version.", new Integer(153));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.cache-type.", new Integer(139));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.is-modified-method-name.", new Integer(142));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-type.type-identifier.", new Integer(146));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.max-beans-in-cache.", new Integer(137));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-use.type-identifier.", new Integer(152));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.", new Integer(130));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-type.", new Integer(145));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.method.ejb-name.", new Integer(166));
      paths.put(".weblogic-ejb-jar.security-role-assignment.role-name.", new Integer(178));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.method.method-intf.", new Integer(167));
      paths.put(".weblogic-ejb-jar.", new Integer(128));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.stateful-session-persistent-store-dir.", new Integer(150));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.clients-on-same-server.", new Integer(133));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-type.type-storage.", new Integer(148));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.reference-descriptor.resource-description.", new Integer(171));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.reference-descriptor.ejb-reference-description.jndi-name.", new Integer(176));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.stateless-bean-is-clusterable.", new Integer(157));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.method.", new Integer(164));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.transaction-descriptor.trans-timeout-seconds.", new Integer(161));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.method.method-params.method-param.", new Integer(170));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.idle-timeout-seconds.", new Integer(138));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.reference-descriptor.ejb-reference-description.", new Integer(174));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.stateless-bean-load-algorithm.", new Integer(158));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.reference-descriptor.resource-description.res-ref-name.", new Integer(172));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.cache-strategy.", new Integer(140));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.stateless-bean-call-router-class-name.", new Integer(159));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.persistence-descriptor.persistence-type.type-version.", new Integer(147));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.jndi-name.", new Integer(134));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.caching-descriptor.initial-beans-in-free-pool.", new Integer(136));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.clustering-descriptor.home-call-router-class-name.", new Integer(156));
      paths.put(".weblogic-ejb-jar.weblogic-enterprise-bean.transaction-isolation.isolation-level.", new Integer(163));
   }
}
