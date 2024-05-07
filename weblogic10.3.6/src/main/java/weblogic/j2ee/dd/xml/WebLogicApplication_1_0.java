package weblogic.j2ee.dd.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.InputSource;
import weblogic.j2ee.dd.WeblogicDeploymentDescriptor;
import weblogic.management.descriptors.application.weblogic.ApplicationParamMBeanImpl;
import weblogic.management.descriptors.application.weblogic.EjbMBeanImpl;
import weblogic.management.descriptors.application.weblogic.EntityCacheMBeanImpl;
import weblogic.management.descriptors.application.weblogic.EntityMappingMBeanImpl;
import weblogic.management.descriptors.application.weblogic.JdbcConnectionPoolMBeanImpl;
import weblogic.management.descriptors.application.weblogic.MaxCacheSizeMBeanImpl;
import weblogic.management.descriptors.application.weblogic.ParserFactoryMBeanImpl;
import weblogic.management.descriptors.application.weblogic.SecurityMBeanImpl;
import weblogic.management.descriptors.application.weblogic.XMLMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.ConnectionCheckParamsMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.ConnectionFactoryMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.ConnectionParamsMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.ConnectionPropertiesMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.DriverParamsMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.ParameterMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.PoolParamsMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.PreparedStatementMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.SizeParamsMBeanCustomImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.StatementMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.XaParamsMBeanImpl;
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

public final class WebLogicApplication_1_0 extends WADDLoader implements XMLProcessor, InProcessor {
   private static final boolean debug = true;
   private static final boolean verbose = true;
   private static final Map paths = new HashMap();
   private ProcessorDriver driver;
   private static final String publicId = "-//BEA Systems, Inc.//DTD WebLogic Application 7.0.0//EN";
   private static final String localDTDResourceName = "/weblogic/j2ee/dd/xml/weblogic-application_1_0.dtd";

   public ProcessorDriver getDriver() {
      return this.driver;
   }

   public WebLogicApplication_1_0() {
      this(true);
   }

   public WebLogicApplication_1_0(boolean var1) {
      this.driver = new ProcessorDriver(this, "-//BEA Systems, Inc.//DTD WebLogic Application 7.0.0//EN", "/weblogic/j2ee/dd/xml/weblogic-application_1_0.dtd", var1);
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
               break;
            case 129:
               this.__pre_129(var1);
               break;
            case 130:
               this.__pre_130(var1);
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
               break;
            case 163:
               this.__pre_163(var1);
               break;
            case 164:
               this.__pre_164(var1);
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
               break;
            case 170:
               this.__pre_170(var1);
               break;
            case 171:
               this.__pre_171(var1);
               break;
            case 172:
               this.__pre_172(var1);
               break;
            case 173:
               this.__pre_173(var1);
               break;
            case 174:
               this.__pre_174(var1);
               break;
            case 175:
               this.__pre_175(var1);
               break;
            case 176:
               this.__pre_176(var1);
               break;
            case 177:
               this.__pre_177(var1);
               break;
            case 178:
               this.__pre_178(var1);
               break;
            case 179:
               this.__pre_179(var1);
               break;
            case 180:
               this.__pre_180(var1);
               break;
            case 181:
               this.__pre_181(var1);
               break;
            case 182:
               this.__pre_182(var1);
               break;
            case 183:
               this.__pre_183(var1);
               break;
            case 184:
               this.__pre_184(var1);
               break;
            case 185:
               this.__pre_185(var1);
               break;
            case 186:
               this.__pre_186(var1);
               break;
            case 187:
               this.__pre_187(var1);
               break;
            case 188:
               this.__pre_188(var1);
               break;
            case 189:
               this.__pre_189(var1);
               break;
            case 190:
               this.__pre_190(var1);
               break;
            case 191:
               this.__pre_191(var1);
               break;
            case 192:
               this.__pre_192(var1);
               break;
            case 193:
               this.__pre_193(var1);
               break;
            case 194:
               this.__pre_194(var1);
               break;
            case 195:
               this.__pre_195(var1);
               break;
            case 196:
               this.__pre_196(var1);
               break;
            case 197:
               this.__pre_197(var1);
               break;
            case 198:
               this.__pre_198(var1);
               break;
            case 199:
               this.__pre_199(var1);
               break;
            case 200:
               this.__pre_200(var1);
               break;
            case 201:
               this.__pre_201(var1);
               break;
            case 202:
               this.__pre_202(var1);
               break;
            case 203:
               this.__pre_203(var1);
               break;
            case 204:
               this.__pre_204(var1);
               break;
            case 205:
               this.__pre_205(var1);
               break;
            case 206:
               this.__pre_206(var1);
               break;
            case 207:
               this.__pre_207(var1);
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
               this.__post_128(var1);
               break;
            case 129:
               this.__post_129(var1);
               break;
            case 130:
               this.__post_130(var1);
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
            case 151:
               this.__post_151(var1);
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
            case 162:
               this.__post_162(var1);
               break;
            case 163:
               this.__post_163(var1);
               break;
            case 164:
               this.__post_164(var1);
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
            case 169:
               this.__post_169(var1);
               break;
            case 170:
               this.__post_170(var1);
               break;
            case 171:
               this.__post_171(var1);
               break;
            case 172:
               this.__post_172(var1);
               break;
            case 173:
               this.__post_173(var1);
               break;
            case 174:
               this.__post_174(var1);
               break;
            case 175:
               this.__post_175(var1);
               break;
            case 176:
               this.__post_176(var1);
               break;
            case 177:
               this.__post_177(var1);
               break;
            case 178:
               this.__post_178(var1);
               break;
            case 179:
               this.__post_179(var1);
               break;
            case 180:
               this.__post_180(var1);
               break;
            case 181:
               this.__post_181(var1);
               break;
            case 182:
               this.__post_182(var1);
               break;
            case 183:
               this.__post_183(var1);
               break;
            case 184:
               this.__post_184(var1);
               break;
            case 185:
               this.__post_185(var1);
               break;
            case 186:
               this.__post_186(var1);
               break;
            case 187:
               this.__post_187(var1);
               break;
            case 188:
               this.__post_188(var1);
               break;
            case 189:
               this.__post_189(var1);
               break;
            case 190:
               this.__post_190(var1);
               break;
            case 191:
               this.__post_191(var1);
               break;
            case 192:
               this.__post_192(var1);
               break;
            case 193:
               this.__post_193(var1);
               break;
            case 194:
               this.__post_194(var1);
               break;
            case 195:
               this.__post_195(var1);
               break;
            case 196:
               this.__post_196(var1);
               break;
            case 197:
               this.__post_197(var1);
               break;
            case 198:
               this.__post_198(var1);
               break;
            case 199:
               this.__post_199(var1);
               break;
            case 200:
               this.__post_200(var1);
               break;
            case 201:
               this.__post_201(var1);
               break;
            case 202:
               this.__post_202(var1);
               break;
            case 203:
               this.__post_203(var1);
               break;
            case 204:
               this.__post_204(var1);
               break;
            case 205:
               this.__post_205(var1);
               break;
            case 206:
               this.__post_206(var1);
               break;
            case 207:
               this.__post_207(var1);
               break;
            default:
               throw new AssertionError(var3.toString());
         }

      }
   }

   private void __pre_194(ProcessingContext var1) {
   }

   private void __post_194(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      DriverParamsMBeanImpl var3 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6631469](.weblogic-application.jdbc-connection-pool.driver-params.row-prefetch-size.) must be a non-empty string");
      } else {
         var3.setRowPrefetchSize(Integer.valueOf(var2));
      }
   }

   private void __pre_165(ProcessingContext var1) {
   }

   private void __post_165(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ParameterMBeanImpl var3 = (ParameterMBeanImpl)var1.getBoundObject("parameter");
      ConnectionParamsMBeanImpl var4 = (ConnectionParamsMBeanImpl)var1.getBoundObject("connectionParams");
      ConnectionPropertiesMBeanImpl var5 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var6 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var7 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var8 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6600649](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.description.) must be a non-empty string");
      } else {
         var3.setDescription(var2);
      }
   }

   private void __pre_170(ProcessingContext var1) {
   }

   private void __post_170(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6605761](.weblogic-application.jdbc-connection-pool.pool-params.size-params.shrinking-enabled.) must be a non-empty string");
      } else {
         var3.setShrinkingEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_182(ProcessingContext var1) {
   }

   private void __post_182(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6618446](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.resource-health-monitoring-enabled.) must be a non-empty string");
      } else {
         var3.setResourceHealthMonitoringEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_137(ProcessingContext var1) {
   }

   private void __post_137(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      MaxCacheSizeMBeanImpl var3 = (MaxCacheSizeMBeanImpl)var1.getBoundObject("maxCacheSize");
      EntityCacheMBeanImpl var4 = (EntityCacheMBeanImpl)var1.getBoundObject("entityCache");
      EjbMBeanImpl var5 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6572313](.weblogic-application.ejb.entity-cache.max-cache-size.megabytes.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setMegabytes(Integer.valueOf(var2));
      }
   }

   private void __pre_148(ProcessingContext var1) {
   }

   private void __post_148(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      EntityMappingMBeanImpl var3 = (EntityMappingMBeanImpl)var1.getBoundObject("entityMapping");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setWhenToCache(var2);
   }

   private void __pre_141(ProcessingContext var1) {
   }

   private void __post_141(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ParserFactoryMBeanImpl var3 = (ParserFactoryMBeanImpl)var1.getBoundObject("parserfactory");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6576500](.weblogic-application.xml.parser-factory.document-builder-factory.) must be a non-empty string");
      } else {
         var3.setDocumentBuilderFactory(var2);
      }
   }

   private void __pre_197(ProcessingContext var1) {
   }

   private void __post_197(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PreparedStatementMBeanImpl var3 = (PreparedStatementMBeanImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6634541](.weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.profiling-enabled.) must be a non-empty string");
      } else {
         var3.setProfilingEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_154(ProcessingContext var1) {
      PoolParamsMBeanImpl var2 = new PoolParamsMBeanImpl();
      var1.addBoundObject(var2, "poolParams");
   }

   private void __post_154(ProcessingContext var1) throws SAXProcessorException {
      PoolParamsMBeanImpl var2 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var3 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setPoolParams(var2);
   }

   private void __pre_134(ProcessingContext var1) {
   }

   private void __post_134(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EntityCacheMBeanImpl var3 = (EntityCacheMBeanImpl)var1.getBoundObject("entityCache");
      EjbMBeanImpl var4 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6569108](.weblogic-application.ejb.entity-cache.caching-strategy.) must be a non-empty string");
      } else {
         this.validateCachingStrategy(var3, var2);
         var3.setCachingStrategy(var2);
      }
   }

   private void __pre_199(ProcessingContext var1) {
   }

   private void __post_199(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PreparedStatementMBeanImpl var3 = (PreparedStatementMBeanImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6636659](.weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.cache-size.) must be a non-empty string");
      } else {
         var3.setCacheSize(Integer.valueOf(var2));
      }
   }

   private void __pre_192(ProcessingContext var1) {
      PreparedStatementMBeanImpl var2 = new PreparedStatementMBeanImpl();
      var1.addBoundObject(var2, "preparedStatement");
   }

   private void __post_192(ProcessingContext var1) throws SAXProcessorException {
      PreparedStatementMBeanImpl var2 = (PreparedStatementMBeanImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var3 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setPreparedStatement(var2);
   }

   private void __pre_145(ProcessingContext var1) {
   }

   private void __post_145(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      EntityMappingMBeanImpl var3 = (EntityMappingMBeanImpl)var1.getBoundObject("entityMapping");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setPublicId(var2);
   }

   private void __pre_135(ProcessingContext var1) {
      MaxCacheSizeMBeanImpl var2 = new MaxCacheSizeMBeanImpl();
      var1.addBoundObject(var2, "maxCacheSize");
   }

   private void __post_135(ProcessingContext var1) throws SAXProcessorException {
      MaxCacheSizeMBeanImpl var2 = (MaxCacheSizeMBeanImpl)var1.getBoundObject("maxCacheSize");
      EntityCacheMBeanImpl var3 = (EntityCacheMBeanImpl)var1.getBoundObject("entityCache");
      EjbMBeanImpl var4 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setMaxCacheSize(var2);
   }

   private void __pre_173(ProcessingContext var1) {
   }

   private void __post_173(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6608904](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.debug-level.) must be a non-empty string");
      } else {
         var3.setDebugLevel(Integer.valueOf(var2));
      }
   }

   private void __pre_171(ProcessingContext var1) {
   }

   private void __post_171(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6606786](.weblogic-application.jdbc-connection-pool.pool-params.size-params.shrink-period-minutes.) must be a non-empty string");
      } else {
         var3.setShrinkPeriodMinutes(Integer.valueOf(var2));
      }
   }

   private void __pre_160(ProcessingContext var1) {
   }

   private void __post_160(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionPropertiesMBeanImpl var3 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var4 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6595462](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.driver-class-name.) must be a non-empty string");
      } else {
         var3.setDriverClassName(var2);
      }
   }

   private void __pre_144(ProcessingContext var1) {
   }

   private void __post_144(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EntityMappingMBeanImpl var3 = (EntityMappingMBeanImpl)var1.getBoundObject("entityMapping");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6579620](.weblogic-application.xml.entity-mapping.entity-mapping-name.) must be a non-empty string");
      } else {
         var3.setEntityMappingName(var2);
      }
   }

   private void __pre_206(ProcessingContext var1) {
   }

   private void __post_206(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ApplicationParamMBeanImpl var3 = (ApplicationParamMBeanImpl)var1.getBoundObject("appParam");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6643928](.weblogic-application.application-param.param-name.) must be a non-empty string");
      } else {
         var3.setParamName(var2);
      }
   }

   private void __pre_187(ProcessingContext var1) {
   }

   private void __post_187(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanImpl var3 = (ConnectionCheckParamsMBeanImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6623777](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.table-name.) must be a non-empty string");
      } else {
         var3.setTableName(var2);
      }
   }

   private void __pre_136(ProcessingContext var1) {
   }

   private void __post_136(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      MaxCacheSizeMBeanImpl var3 = (MaxCacheSizeMBeanImpl)var1.getBoundObject("maxCacheSize");
      EntityCacheMBeanImpl var4 = (EntityCacheMBeanImpl)var1.getBoundObject("entityCache");
      EjbMBeanImpl var5 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6571287](.weblogic-application.ejb.entity-cache.max-cache-size.bytes.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setBytes(Integer.valueOf(var2));
      }
   }

   private void __pre_190(ProcessingContext var1) {
   }

   private void __post_190(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanImpl var3 = (ConnectionCheckParamsMBeanImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6627201](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.refresh-minutes.) must be a non-empty string");
      } else {
         var3.setRefreshMinutes(Integer.valueOf(var2));
      }
   }

   private void __pre_133(ProcessingContext var1) {
   }

   private void __post_133(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EntityCacheMBeanImpl var3 = (EntityCacheMBeanImpl)var1.getBoundObject("entityCache");
      EjbMBeanImpl var4 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6567970](.weblogic-application.ejb.entity-cache.max-beans-in-cache.) must be a non-empty string");
      } else {
         try {
            this.validateIntegerGreaterThanZero(var2);
         } catch (Exception var7) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var7.getMessage());
         }

         var3.setMaxBeansInCache(Integer.valueOf(var2));
      }
   }

   private void __pre_195(ProcessingContext var1) {
   }

   private void __post_195(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      DriverParamsMBeanImpl var3 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6632491](.weblogic-application.jdbc-connection-pool.driver-params.stream-chunk-size.) must be a non-empty string");
      } else {
         var3.setStreamChunkSize(Integer.valueOf(var2));
      }
   }

   private void __pre_191(ProcessingContext var1) {
      StatementMBeanImpl var2 = new StatementMBeanImpl();
      var1.addBoundObject(var2, "statement");
   }

   private void __post_191(ProcessingContext var1) throws SAXProcessorException {
      StatementMBeanImpl var2 = (StatementMBeanImpl)var1.getBoundObject("statement");
      DriverParamsMBeanImpl var3 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setStatement(var2);
   }

   private void __pre_142(ProcessingContext var1) {
   }

   private void __post_142(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ParserFactoryMBeanImpl var3 = (ParserFactoryMBeanImpl)var1.getBoundObject("parserfactory");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6577528](.weblogic-application.xml.parser-factory.transformer-factory.) must be a non-empty string");
      } else {
         var3.setTransformerFactory(var2);
      }
   }

   private void __pre_164(ProcessingContext var1) {
   }

   private void __post_164(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ParameterMBeanImpl var3 = (ParameterMBeanImpl)var1.getBoundObject("parameter");
      ConnectionParamsMBeanImpl var4 = (ConnectionParamsMBeanImpl)var1.getBoundObject("connectionParams");
      ConnectionPropertiesMBeanImpl var5 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var6 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var7 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var8 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6599666](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.param-value.) must be a non-empty string");
      } else {
         var3.setParamValue(var2);
      }
   }

   private void __pre_196(ProcessingContext var1) {
   }

   private void __post_196(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      StatementMBeanImpl var3 = (StatementMBeanImpl)var1.getBoundObject("statement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6633515](.weblogic-application.jdbc-connection-pool.driver-params.statement.profiling-enabled.) must be a non-empty string");
      } else {
         var3.setProfilingEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_129(ProcessingContext var1) {
      EjbMBeanImpl var2 = new EjbMBeanImpl();
      var1.addBoundObject(var2, "ejbMBean");
   }

   private void __post_129(ProcessingContext var1) throws SAXProcessorException {
      EjbMBeanImpl var2 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setEjb(var2);
   }

   private void __pre_177(ProcessingContext var1) {
   }

   private void __post_177(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6613134](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.tx-context-on-close-needed.) must be a non-empty string");
      } else {
         var3.setTxContextOnCloseNeeded(Boolean.valueOf(var2));
      }
   }

   private void __pre_155(ProcessingContext var1) {
      DriverParamsMBeanImpl var2 = new DriverParamsMBeanImpl();
      var1.addBoundObject(var2, "driverParams");
   }

   private void __post_155(ProcessingContext var1) throws SAXProcessorException {
      DriverParamsMBeanImpl var2 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var3 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setDriverParams(var2);
   }

   private void __pre_163(ProcessingContext var1) {
   }

   private void __post_163(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ParameterMBeanImpl var3 = (ParameterMBeanImpl)var1.getBoundObject("parameter");
      ConnectionParamsMBeanImpl var4 = (ConnectionParamsMBeanImpl)var1.getBoundObject("connectionParams");
      ConnectionPropertiesMBeanImpl var5 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var6 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var7 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var8 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6598686](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.param-name.) must be a non-empty string");
      } else {
         var3.setParamName(var2);
      }
   }

   private void __pre_158(ProcessingContext var1) {
   }

   private void __post_158(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionPropertiesMBeanImpl var3 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var4 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6593476](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.user-name.) must be a non-empty string");
      } else {
         var3.setUserName(var2);
      }
   }

   private void __pre_166(ProcessingContext var1) {
      SizeParamsMBeanCustomImpl var2 = new SizeParamsMBeanCustomImpl();
      var1.addBoundObject(var2, "sizeParams");
   }

   private void __post_166(ProcessingContext var1) throws SAXProcessorException {
      SizeParamsMBeanCustomImpl var2 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanImpl var3 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setSizeParams(var2);
   }

   private void __pre_193(ProcessingContext var1) {
   }

   private void __post_193(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      DriverParamsMBeanImpl var3 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6630419](.weblogic-application.jdbc-connection-pool.driver-params.row-prefetch-enabled.) must be a non-empty string");
      } else {
         var3.setRowPrefetchEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_185(ProcessingContext var1) {
   }

   private void __post_185(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PoolParamsMBeanImpl var3 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6621657](.weblogic-application.jdbc-connection-pool.pool-params.secs-to-trust-an-idle-pool-con.) must be a non-empty string");
      } else {
         var3.setSecondsToTrustAnIdlePoolConnection(Integer.valueOf(var2));
      }
   }

   private void __pre_128(ProcessingContext var1) {
      WeblogicDeploymentDescriptor var2 = new WeblogicDeploymentDescriptor();
      var1.addBoundObject(var2, "wlApplication");
   }

   private void __post_128(ProcessingContext var1) throws SAXProcessorException {
      WeblogicDeploymentDescriptor var2 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (this.applicationDescriptor != null) {
         this.applicationDescriptor.setWeblogicApplicationDescriptor(var2);
      }

   }

   private void __pre_189(ProcessingContext var1) {
   }

   private void __post_189(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanImpl var3 = (ConnectionCheckParamsMBeanImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6626138](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.check-on-release-enabled.) must be a non-empty string");
      } else {
         var3.setCheckOnReleaseEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_157(ProcessingContext var1) {
      ConnectionPropertiesMBeanImpl var2 = new ConnectionPropertiesMBeanImpl();
      var1.addBoundObject(var2, "connectionProperties");
   }

   private void __post_157(ProcessingContext var1) throws SAXProcessorException {
      ConnectionPropertiesMBeanImpl var2 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var3 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setConnectionProperties(var2);
   }

   private void __pre_162(ProcessingContext var1) {
      ParameterMBeanImpl var2 = new ParameterMBeanImpl();
      var1.addBoundObject(var2, "parameter");
   }

   private void __post_162(ProcessingContext var1) throws SAXProcessorException {
      ParameterMBeanImpl var2 = (ParameterMBeanImpl)var1.getBoundObject("parameter");
      ConnectionParamsMBeanImpl var3 = (ConnectionParamsMBeanImpl)var1.getBoundObject("connectionParams");
      ConnectionPropertiesMBeanImpl var4 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var5 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var6 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var7 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addParameter(var2);
   }

   private void __pre_167(ProcessingContext var1) {
   }

   private void __post_167(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6602708](.weblogic-application.jdbc-connection-pool.pool-params.size-params.initial-capacity.) must be a non-empty string");
      } else {
         var3.setInitialCapacity(Integer.valueOf(var2));
      }
   }

   private void __pre_156(ProcessingContext var1) {
   }

   private void __post_156(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionFactoryMBeanImpl var3 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6591218](.weblogic-application.jdbc-connection-pool.connection-factory.factory-name.) must be a non-empty string");
      } else {
         var3.setFactoryName(var2);
      }
   }

   private void __pre_178(ProcessingContext var1) {
   }

   private void __post_178(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6614188](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.new-conn-for-commit-enabled.) must be a non-empty string");
      } else {
         var3.setNewConnForCommitEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_138(ProcessingContext var1) {
      XMLMBeanImpl var2 = new XMLMBeanImpl();
      var1.addBoundObject(var2, "xmlMBean");
   }

   private void __post_138(ProcessingContext var1) throws SAXProcessorException {
      XMLMBeanImpl var2 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setXML(var2);
   }

   private void __pre_204(ProcessingContext var1) {
      ApplicationParamMBeanImpl var2 = new ApplicationParamMBeanImpl();
      var1.addBoundObject(var2, "appParam");
   }

   private void __post_204(ProcessingContext var1) throws SAXProcessorException {
      ApplicationParamMBeanImpl var2 = (ApplicationParamMBeanImpl)var1.getBoundObject("appParam");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addParameter(var2);
   }

   private void __pre_203(ProcessingContext var1) {
   }

   private void __post_203(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SecurityMBeanImpl var3 = (SecurityMBeanImpl)var1.getBoundObject("security");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setRealmName(var2);
   }

   private void __pre_207(ProcessingContext var1) {
   }

   private void __post_207(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ApplicationParamMBeanImpl var3 = (ApplicationParamMBeanImpl)var1.getBoundObject("appParam");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6644912](.weblogic-application.application-param.param-value.) must be a non-empty string");
      } else {
         var3.setParamValue(var2);
      }
   }

   private void __pre_140(ProcessingContext var1) {
   }

   private void __post_140(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ParserFactoryMBeanImpl var3 = (ParserFactoryMBeanImpl)var1.getBoundObject("parserfactory");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6575482](.weblogic-application.xml.parser-factory.saxparser-factory.) must be a non-empty string");
      } else {
         var3.setSaxparserFactory(var2);
      }
   }

   private void __pre_168(ProcessingContext var1) {
   }

   private void __post_168(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6603726](.weblogic-application.jdbc-connection-pool.pool-params.size-params.max-capacity.) must be a non-empty string");
      } else {
         var3.setMaxCapacity(Integer.valueOf(var2));
      }
   }

   private void __pre_172(ProcessingContext var1) {
      XaParamsMBeanImpl var2 = new XaParamsMBeanImpl();
      var1.addBoundObject(var2, "xaParams");
   }

   private void __post_172(ProcessingContext var1) throws SAXProcessorException {
      XaParamsMBeanImpl var2 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanImpl var3 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setXaParams(var2);
   }

   private void __pre_151(ProcessingContext var1) {
   }

   private void __post_151(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      JdbcConnectionPoolMBeanImpl var3 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6585852](.weblogic-application.jdbc-connection-pool.data-source-name.) must be a non-empty string");
      } else {
         var3.setDataSourceName(var2);
      }
   }

   private void __pre_149(ProcessingContext var1) {
   }

   private void __post_149(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EntityMappingMBeanImpl var3 = (EntityMappingMBeanImpl)var1.getBoundObject("entityMapping");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6583661](.weblogic-application.xml.entity-mapping.cache-timeout-interval.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var7) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var7.getMessage());
         }

         var3.setCacheTimeoutInterval(Integer.valueOf(var2));
      }
   }

   private void __pre_176(ProcessingContext var1) {
   }

   private void __post_176(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6612015](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.recover-only-once-enabled.) must be a non-empty string");
      } else {
         var3.setRecoverOnlyOnceEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_174(ProcessingContext var1) {
   }

   private void __post_174(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6609918](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.keep-conn-until-tx-complete-enabled.) must be a non-empty string");
      } else {
         var3.setKeepConnUntilTxCompleteEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_152(ProcessingContext var1) {
   }

   private void __post_152(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      JdbcConnectionPoolMBeanImpl var3 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6586873](.weblogic-application.jdbc-connection-pool.acl-name.) must be a non-empty string");
      } else {
         var3.setAclName(var2);
      }
   }

   private void __pre_130(ProcessingContext var1) {
   }

   private void __post_130(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EjbMBeanImpl var3 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6561330](.weblogic-application.ejb.start-mdbs-with-application.) must be a non-empty string");
      } else if (!"true".equals(var2) && !"True".equals(var2) && !"false".equals(var2) && !"False".equals(var2)) {
         throw new SAXValidationException("PAction[6561330](.weblogic-application.ejb.start-mdbs-with-application.) must be one of the values: true,True,false,False");
      } else {
         var3.setStartMdbsWithApplication("True".equalsIgnoreCase(var2));
      }
   }

   private void __pre_198(ProcessingContext var1) {
   }

   private void __post_198(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PreparedStatementMBeanImpl var3 = (PreparedStatementMBeanImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6635599](.weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.cache-profiling-threshold.) must be a non-empty string");
      } else {
         var3.setCacheProfilingThreshold(Integer.valueOf(var2));
      }
   }

   private void __pre_180(ProcessingContext var1) {
   }

   private void __post_180(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6616307](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.keep-logical-conn-open-on-release.) must be a non-empty string");
      } else {
         var3.setKeepLogicalConnOpenOnRelease(Boolean.valueOf(var2));
      }
   }

   private void __pre_202(ProcessingContext var1) {
      SecurityMBeanImpl var2 = new SecurityMBeanImpl();
      var1.addBoundObject(var2, "security");
   }

   private void __post_202(ProcessingContext var1) throws SAXProcessorException {
      SecurityMBeanImpl var2 = (SecurityMBeanImpl)var1.getBoundObject("security");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setSecurity(var2);
   }

   private void __pre_147(ProcessingContext var1) {
   }

   private void __post_147(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      EntityMappingMBeanImpl var3 = (EntityMappingMBeanImpl)var1.getBoundObject("entityMapping");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setEntityURI(var2);
   }

   private void __pre_150(ProcessingContext var1) {
      JdbcConnectionPoolMBeanImpl var2 = new JdbcConnectionPoolMBeanImpl();
      var1.addBoundObject(var2, "jdbcConnectionPool");
   }

   private void __post_150(ProcessingContext var1) throws SAXProcessorException {
      JdbcConnectionPoolMBeanImpl var2 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addJdbcConnectionPool(var2);
   }

   private void __pre_153(ProcessingContext var1) {
      ConnectionFactoryMBeanImpl var2 = new ConnectionFactoryMBeanImpl();
      var1.addBoundObject(var2, "connectionFactory");
   }

   private void __post_153(ProcessingContext var1) throws SAXProcessorException {
      ConnectionFactoryMBeanImpl var2 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var3 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setConnectionFactory(var2);
   }

   private void __pre_201(ProcessingContext var1) {
   }

   private void __post_201(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PreparedStatementMBeanImpl var3 = (PreparedStatementMBeanImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6638749](.weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.max-parameter-length.) must be a non-empty string");
      } else {
         var3.setMaxParameterLength(Integer.valueOf(var2));
      }
   }

   private void __pre_205(ProcessingContext var1) {
   }

   private void __post_205(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ApplicationParamMBeanImpl var3 = (ApplicationParamMBeanImpl)var1.getBoundObject("appParam");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6642944](.weblogic-application.application-param.description.) must be a non-empty string");
      } else {
         var3.setDescription(var2);
      }
   }

   private void __pre_183(ProcessingContext var1) {
      ConnectionCheckParamsMBeanImpl var2 = new ConnectionCheckParamsMBeanImpl();
      var1.addBoundObject(var2, "connectionCheck");
   }

   private void __post_183(ProcessingContext var1) throws SAXProcessorException {
      ConnectionCheckParamsMBeanImpl var2 = (ConnectionCheckParamsMBeanImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanImpl var3 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setConnectionCheckParams(var2);
   }

   private void __pre_188(ProcessingContext var1) {
   }

   private void __post_188(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanImpl var3 = (ConnectionCheckParamsMBeanImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6624794](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.check-on-reserve-enabled.) must be a non-empty string");
      } else {
         var3.setCheckOnReserveEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_159(ProcessingContext var1) {
   }

   private void __post_159(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionPropertiesMBeanImpl var3 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var4 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6594472](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.url.) must be a non-empty string");
      } else {
         var3.setUrl(var2);
      }
   }

   private void __pre_186(ProcessingContext var1) {
   }

   private void __post_186(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PoolParamsMBeanImpl var3 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6622727](.weblogic-application.jdbc-connection-pool.pool-params.leak-profiling-enabled.) must be a non-empty string");
      } else {
         var3.setLeakProfilingEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_132(ProcessingContext var1) {
   }

   private void __post_132(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EntityCacheMBeanImpl var3 = (EntityCacheMBeanImpl)var1.getBoundObject("entityCache");
      EjbMBeanImpl var4 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6566977](.weblogic-application.ejb.entity-cache.entity-cache-name.) must be a non-empty string");
      } else {
         var3.setEntityCacheName(var2);
      }
   }

   private void __pre_175(ProcessingContext var1) {
   }

   private void __post_175(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6610988](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.end-only-once-enabled.) must be a non-empty string");
      } else {
         var3.setEndOnlyOnceEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_184(ProcessingContext var1) {
   }

   private void __post_184(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PoolParamsMBeanImpl var3 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6620633](.weblogic-application.jdbc-connection-pool.pool-params.login-delay-seconds.) must be a non-empty string");
      } else {
         var3.setLoginDelaySeconds(Integer.valueOf(var2));
      }
   }

   private void __pre_181(ProcessingContext var1) {
   }

   private void __post_181(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6617385](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.local-transaction-supported.) must be a non-empty string");
      } else {
         var3.setLocalTransactionSupported(Boolean.valueOf(var2));
      }
   }

   private void __pre_179(ProcessingContext var1) {
   }

   private void __post_179(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6615248](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.prepared-statement-cache-size.) must be a non-empty string");
      } else {
         var3.setPreparedStatementCacheSize(Integer.valueOf(var2));
      }
   }

   private void __pre_146(ProcessingContext var1) {
   }

   private void __post_146(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      EntityMappingMBeanImpl var3 = (EntityMappingMBeanImpl)var1.getBoundObject("entityMapping");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setSystemId(var2);
   }

   private void __pre_143(ProcessingContext var1) {
      EntityMappingMBeanImpl var2 = new EntityMappingMBeanImpl();
      var1.addBoundObject(var2, "entityMapping");
   }

   private void __post_143(ProcessingContext var1) throws SAXProcessorException {
      EntityMappingMBeanImpl var2 = (EntityMappingMBeanImpl)var1.getBoundObject("entityMapping");
      XMLMBeanImpl var3 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addEntityMapping(var2);
   }

   private void __pre_131(ProcessingContext var1) {
      EntityCacheMBeanImpl var2 = new EntityCacheMBeanImpl();
      var1.addBoundObject(var2, "entityCache");
   }

   private void __post_131(ProcessingContext var1) throws SAXProcessorException {
      EntityCacheMBeanImpl var2 = (EntityCacheMBeanImpl)var1.getBoundObject("entityCache");
      EjbMBeanImpl var3 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addEntityCache(var2);
   }

   private void __pre_200(ProcessingContext var1) {
   }

   private void __post_200(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PreparedStatementMBeanImpl var3 = (PreparedStatementMBeanImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6637683](.weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.parameter-logging-enabled.) must be a non-empty string");
      } else {
         var3.setParameterLoggingEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_161(ProcessingContext var1) {
      ConnectionParamsMBeanImpl var2 = new ConnectionParamsMBeanImpl();
      var1.addBoundObject(var2, "connectionParams");
   }

   private void __post_161(ProcessingContext var1) throws SAXProcessorException {
      ConnectionParamsMBeanImpl var2 = (ConnectionParamsMBeanImpl)var1.getBoundObject("connectionParams");
      ConnectionPropertiesMBeanImpl var3 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var4 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setConnectionParams(var2);
   }

   private void __pre_169(ProcessingContext var1) {
   }

   private void __post_169(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanImpl var4 = (PoolParamsMBeanImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6604741](.weblogic-application.jdbc-connection-pool.pool-params.size-params.capacity-increment.) must be a non-empty string");
      } else {
         var3.setCapacityIncrement(Integer.valueOf(var2));
      }
   }

   private void __pre_139(ProcessingContext var1) {
      ParserFactoryMBeanImpl var2 = new ParserFactoryMBeanImpl();
      var1.addBoundObject(var2, "parserfactory");
   }

   private void __post_139(ProcessingContext var1) throws SAXProcessorException {
      ParserFactoryMBeanImpl var2 = (ParserFactoryMBeanImpl)var1.getBoundObject("parserfactory");
      XMLMBeanImpl var3 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setParserFactory(var2);
   }

   public void addBoundObject(Object var1, String var2) {
      this.driver.currentNode().addBoundObject(var1, var2);
   }

   static {
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.row-prefetch-size.", new Integer(194));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.description.", new Integer(165));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.shrinking-enabled.", new Integer(170));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.resource-health-monitoring-enabled.", new Integer(182));
      paths.put(".weblogic-application.ejb.entity-cache.max-cache-size.megabytes.", new Integer(137));
      paths.put(".weblogic-application.xml.entity-mapping.when-to-cache.", new Integer(148));
      paths.put(".weblogic-application.xml.parser-factory.document-builder-factory.", new Integer(141));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.profiling-enabled.", new Integer(197));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.", new Integer(154));
      paths.put(".weblogic-application.ejb.entity-cache.caching-strategy.", new Integer(134));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.cache-size.", new Integer(199));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.", new Integer(192));
      paths.put(".weblogic-application.xml.entity-mapping.public-id.", new Integer(145));
      paths.put(".weblogic-application.ejb.entity-cache.max-cache-size.", new Integer(135));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.debug-level.", new Integer(173));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.shrink-period-minutes.", new Integer(171));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.driver-class-name.", new Integer(160));
      paths.put(".weblogic-application.xml.entity-mapping.entity-mapping-name.", new Integer(144));
      paths.put(".weblogic-application.application-param.param-name.", new Integer(206));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.table-name.", new Integer(187));
      paths.put(".weblogic-application.ejb.entity-cache.max-cache-size.bytes.", new Integer(136));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.refresh-minutes.", new Integer(190));
      paths.put(".weblogic-application.ejb.entity-cache.max-beans-in-cache.", new Integer(133));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.stream-chunk-size.", new Integer(195));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.statement.", new Integer(191));
      paths.put(".weblogic-application.xml.parser-factory.transformer-factory.", new Integer(142));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.param-value.", new Integer(164));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.statement.profiling-enabled.", new Integer(196));
      paths.put(".weblogic-application.ejb.", new Integer(129));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.tx-context-on-close-needed.", new Integer(177));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.", new Integer(155));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.param-name.", new Integer(163));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.user-name.", new Integer(158));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.", new Integer(166));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.row-prefetch-enabled.", new Integer(193));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.secs-to-trust-an-idle-pool-con.", new Integer(185));
      paths.put(".weblogic-application.", new Integer(128));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.check-on-release-enabled.", new Integer(189));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.", new Integer(157));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.", new Integer(162));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.initial-capacity.", new Integer(167));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.factory-name.", new Integer(156));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.new-conn-for-commit-enabled.", new Integer(178));
      paths.put(".weblogic-application.xml.", new Integer(138));
      paths.put(".weblogic-application.application-param.", new Integer(204));
      paths.put(".weblogic-application.security.realm-name.", new Integer(203));
      paths.put(".weblogic-application.application-param.param-value.", new Integer(207));
      paths.put(".weblogic-application.xml.parser-factory.saxparser-factory.", new Integer(140));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.max-capacity.", new Integer(168));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.", new Integer(172));
      paths.put(".weblogic-application.jdbc-connection-pool.data-source-name.", new Integer(151));
      paths.put(".weblogic-application.xml.entity-mapping.cache-timeout-interval.", new Integer(149));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.recover-only-once-enabled.", new Integer(176));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.keep-conn-until-tx-complete-enabled.", new Integer(174));
      paths.put(".weblogic-application.jdbc-connection-pool.acl-name.", new Integer(152));
      paths.put(".weblogic-application.ejb.start-mdbs-with-application.", new Integer(130));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.cache-profiling-threshold.", new Integer(198));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.keep-logical-conn-open-on-release.", new Integer(180));
      paths.put(".weblogic-application.security.", new Integer(202));
      paths.put(".weblogic-application.xml.entity-mapping.entity-uri.", new Integer(147));
      paths.put(".weblogic-application.jdbc-connection-pool.", new Integer(150));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.", new Integer(153));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.max-parameter-length.", new Integer(201));
      paths.put(".weblogic-application.application-param.description.", new Integer(205));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.", new Integer(183));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.check-on-reserve-enabled.", new Integer(188));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.url.", new Integer(159));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.leak-profiling-enabled.", new Integer(186));
      paths.put(".weblogic-application.ejb.entity-cache.entity-cache-name.", new Integer(132));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.end-only-once-enabled.", new Integer(175));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.login-delay-seconds.", new Integer(184));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.local-transaction-supported.", new Integer(181));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.prepared-statement-cache-size.", new Integer(179));
      paths.put(".weblogic-application.xml.entity-mapping.system-id.", new Integer(146));
      paths.put(".weblogic-application.xml.entity-mapping.", new Integer(143));
      paths.put(".weblogic-application.ejb.entity-cache.", new Integer(131));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.parameter-logging-enabled.", new Integer(200));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.", new Integer(161));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.capacity-increment.", new Integer(169));
      paths.put(".weblogic-application.xml.parser-factory.", new Integer(139));
   }
}
