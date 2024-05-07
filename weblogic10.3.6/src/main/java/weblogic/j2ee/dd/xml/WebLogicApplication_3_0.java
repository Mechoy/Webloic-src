package weblogic.j2ee.dd.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.InputSource;
import weblogic.j2ee.dd.CustomModuleDescriptor;
import weblogic.j2ee.dd.WeblogicDeploymentDescriptor;
import weblogic.management.DeploymentException;
import weblogic.management.descriptors.application.weblogic.ApplicationParamMBeanImpl;
import weblogic.management.descriptors.application.weblogic.ClassloaderStructureMBeanImpl;
import weblogic.management.descriptors.application.weblogic.EjbMBeanImpl;
import weblogic.management.descriptors.application.weblogic.EntityCacheMBeanImpl;
import weblogic.management.descriptors.application.weblogic.EntityMappingMBeanImpl;
import weblogic.management.descriptors.application.weblogic.JdbcConnectionPoolMBeanImpl;
import weblogic.management.descriptors.application.weblogic.LibraryRefMBeanImpl;
import weblogic.management.descriptors.application.weblogic.ListenerMBeanImpl;
import weblogic.management.descriptors.application.weblogic.MaxCacheSizeMBeanImpl;
import weblogic.management.descriptors.application.weblogic.ModuleProviderMBeanImpl;
import weblogic.management.descriptors.application.weblogic.ModuleRefMBeanImpl;
import weblogic.management.descriptors.application.weblogic.ParserFactoryMBeanImpl;
import weblogic.management.descriptors.application.weblogic.SecurityMBeanImpl;
import weblogic.management.descriptors.application.weblogic.SecurityRoleAssignmentMBeanImpl;
import weblogic.management.descriptors.application.weblogic.ShutdownMBeanImpl;
import weblogic.management.descriptors.application.weblogic.StartupMBeanImpl;
import weblogic.management.descriptors.application.weblogic.XMLMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.ConnectionCheckParamsMBeanCustomImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.ConnectionFactoryMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.ConnectionParamsMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.ConnectionPropertiesMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.DriverParamsMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.ParameterMBeanImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.PoolParamsMBeanCustomImpl;
import weblogic.management.descriptors.application.weblogic.jdbc.PreparedStatementMBeanCustomImpl;
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

public final class WebLogicApplication_3_0 extends WADDLoader implements XMLProcessor, InProcessor {
   private static final boolean debug = true;
   private static final boolean verbose = true;
   private static final Map paths = new HashMap();
   private ProcessorDriver driver;
   private static final String publicId = "-//BEA Systems, Inc.//DTD WebLogic Application 9.0.0//EN";
   private static final String localDTDResourceName = "/weblogic/j2ee/dd/xml/weblogic-application_3_0.dtd";

   public ProcessorDriver getDriver() {
      return this.driver;
   }

   public WebLogicApplication_3_0() {
      this(true);
   }

   public WebLogicApplication_3_0(boolean var1) {
      this.driver = new ProcessorDriver(this, "-//BEA Systems, Inc.//DTD WebLogic Application 9.0.0//EN", "/weblogic/j2ee/dd/xml/weblogic-application_3_0.dtd", var1);
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
            case 208:
               this.__pre_208(var1);
               break;
            case 209:
               this.__pre_209(var1);
               break;
            case 210:
               this.__pre_210(var1);
               break;
            case 211:
               this.__pre_211(var1);
               break;
            case 212:
               this.__pre_212(var1);
               break;
            case 213:
               this.__pre_213(var1);
               break;
            case 214:
               this.__pre_214(var1);
               break;
            case 215:
               this.__pre_215(var1);
               break;
            case 216:
               this.__pre_216(var1);
               break;
            case 217:
               this.__pre_217(var1);
               break;
            case 218:
               this.__pre_218(var1);
               break;
            case 219:
               this.__pre_219(var1);
               break;
            case 220:
               this.__pre_220(var1);
               break;
            case 221:
               this.__pre_221(var1);
               break;
            case 222:
               this.__pre_222(var1);
               break;
            case 223:
               this.__pre_223(var1);
               break;
            case 224:
               this.__pre_224(var1);
               break;
            case 225:
               this.__pre_225(var1);
               break;
            case 226:
               this.__pre_226(var1);
               break;
            case 227:
               this.__pre_227(var1);
               break;
            case 228:
               this.__pre_228(var1);
               break;
            case 229:
               this.__pre_229(var1);
               break;
            case 230:
               this.__pre_230(var1);
               break;
            case 231:
               this.__pre_231(var1);
               break;
            case 232:
               this.__pre_232(var1);
               break;
            case 233:
               this.__pre_233(var1);
               break;
            case 234:
               this.__pre_234(var1);
               break;
            case 235:
               this.__pre_235(var1);
               break;
            case 236:
               this.__pre_236(var1);
               break;
            case 237:
               this.__pre_237(var1);
               break;
            case 238:
               this.__pre_238(var1);
               break;
            case 239:
               this.__pre_239(var1);
               break;
            case 240:
               this.__pre_240(var1);
               break;
            case 241:
               this.__pre_241(var1);
               break;
            case 242:
               this.__pre_242(var1);
               break;
            case 243:
               this.__pre_243(var1);
               break;
            case 244:
               this.__pre_244(var1);
               break;
            case 245:
               this.__pre_245(var1);
               break;
            case 246:
               this.__pre_246(var1);
               break;
            case 247:
               this.__pre_247(var1);
               break;
            case 248:
               this.__pre_248(var1);
               break;
            case 249:
               this.__pre_249(var1);
               break;
            case 250:
               this.__pre_250(var1);
               break;
            case 251:
               this.__pre_251(var1);
               break;
            case 252:
               this.__pre_252(var1);
               break;
            case 253:
               this.__pre_253(var1);
               break;
            case 254:
               this.__pre_254(var1);
               break;
            case 255:
               this.__pre_255(var1);
               break;
            case 256:
               this.__pre_256(var1);
               break;
            case 257:
               this.__pre_257(var1);
               break;
            case 258:
               this.__pre_258(var1);
               break;
            case 259:
               this.__pre_259(var1);
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
            case 208:
               this.__post_208(var1);
               break;
            case 209:
               this.__post_209(var1);
               break;
            case 210:
               this.__post_210(var1);
               break;
            case 211:
               this.__post_211(var1);
               break;
            case 212:
               this.__post_212(var1);
               break;
            case 213:
               this.__post_213(var1);
               break;
            case 214:
               this.__post_214(var1);
               break;
            case 215:
               this.__post_215(var1);
               break;
            case 216:
               this.__post_216(var1);
               break;
            case 217:
               this.__post_217(var1);
               break;
            case 218:
               this.__post_218(var1);
               break;
            case 219:
               this.__post_219(var1);
               break;
            case 220:
               this.__post_220(var1);
               break;
            case 221:
               this.__post_221(var1);
               break;
            case 222:
               this.__post_222(var1);
               break;
            case 223:
               this.__post_223(var1);
               break;
            case 224:
               this.__post_224(var1);
               break;
            case 225:
               this.__post_225(var1);
               break;
            case 226:
               this.__post_226(var1);
               break;
            case 227:
               this.__post_227(var1);
               break;
            case 228:
               this.__post_228(var1);
               break;
            case 229:
               this.__post_229(var1);
               break;
            case 230:
               this.__post_230(var1);
               break;
            case 231:
               this.__post_231(var1);
               break;
            case 232:
               this.__post_232(var1);
               break;
            case 233:
               this.__post_233(var1);
               break;
            case 234:
               this.__post_234(var1);
               break;
            case 235:
               this.__post_235(var1);
               break;
            case 236:
               this.__post_236(var1);
               break;
            case 237:
               this.__post_237(var1);
               break;
            case 238:
               this.__post_238(var1);
               break;
            case 239:
               this.__post_239(var1);
               break;
            case 240:
               this.__post_240(var1);
               break;
            case 241:
               this.__post_241(var1);
               break;
            case 242:
               this.__post_242(var1);
               break;
            case 243:
               this.__post_243(var1);
               break;
            case 244:
               this.__post_244(var1);
               break;
            case 245:
               this.__post_245(var1);
               break;
            case 246:
               this.__post_246(var1);
               break;
            case 247:
               this.__post_247(var1);
               break;
            case 248:
               this.__post_248(var1);
               break;
            case 249:
               this.__post_249(var1);
               break;
            case 250:
               this.__post_250(var1);
               break;
            case 251:
               this.__post_251(var1);
               break;
            case 252:
               this.__post_252(var1);
               break;
            case 253:
               this.__post_253(var1);
               break;
            case 254:
               this.__post_254(var1);
               break;
            case 255:
               this.__post_255(var1);
               break;
            case 256:
               this.__post_256(var1);
               break;
            case 257:
               this.__post_257(var1);
               break;
            case 258:
               this.__post_258(var1);
               break;
            case 259:
               this.__post_259(var1);
               break;
            default:
               throw new AssertionError(var3.toString());
         }

      }
   }

   private void __pre_259(ProcessingContext var1) {
   }

   private void __post_259(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      LibraryRefMBeanImpl var3 = (LibraryRefMBeanImpl)var1.getBoundObject("library");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setContextPath(var2);
   }

   private void __pre_171(ProcessingContext var1) {
   }

   private void __post_171(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6725419](.weblogic-application.jdbc-connection-pool.pool-params.size-params.shrinking-enabled.) must be a non-empty string");
      } else {
         var3.setShrinkingEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_166(ProcessingContext var1) {
   }

   private void __post_166(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ParameterMBeanImpl var3 = (ParameterMBeanImpl)var1.getBoundObject("parameter");
      ConnectionParamsMBeanImpl var4 = (ConnectionParamsMBeanImpl)var1.getBoundObject("connectionParams");
      ConnectionPropertiesMBeanImpl var5 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var6 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var7 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var8 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6720249](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.description.) must be a non-empty string");
      } else {
         var3.setDescription(var2);
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
         throw new SAXValidationException("PAction[6690911](.weblogic-application.ejb.entity-cache.max-cache-size.megabytes.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setMegabytes(Integer.valueOf(var2));
      }
   }

   private void __pre_245(ProcessingContext var1) {
   }

   private void __post_245(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ShutdownMBeanImpl var3 = (ShutdownMBeanImpl)var1.getBoundObject("shutdown");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6484847](.weblogic-application.shutdown.shutdown-class.) must be a non-empty string");
      } else {
         var3.setShutdownClass(var2);
      }
   }

   private void __pre_141(ProcessingContext var1) {
   }

   private void __post_141(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ParserFactoryMBeanImpl var3 = (ParserFactoryMBeanImpl)var1.getBoundObject("parserfactory");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6695097](.weblogic-application.xml.parser-factory.document-builder-factory.) must be a non-empty string");
      } else {
         var3.setDocumentBuilderFactory(var2);
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

   private void __pre_241(ProcessingContext var1) {
      StartupMBeanImpl var2 = new StartupMBeanImpl();
      var1.addBoundObject(var2, "startup");
   }

   private void __post_241(ProcessingContext var1) throws SAXProcessorException {
      StartupMBeanImpl var2 = (StartupMBeanImpl)var1.getBoundObject("startup");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addStartup(var2);
   }

   private void __pre_154(ProcessingContext var1) {
      PoolParamsMBeanCustomImpl var2 = new PoolParamsMBeanCustomImpl();
      var1.addBoundObject(var2, "poolParams");
   }

   private void __post_154(ProcessingContext var1) throws SAXProcessorException {
      PoolParamsMBeanCustomImpl var2 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var3 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setPoolParams(var2);
   }

   private void __pre_213(ProcessingContext var1) {
   }

   private void __post_213(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PreparedStatementMBeanCustomImpl var3 = (PreparedStatementMBeanCustomImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6778309](.weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.profiling-enabled.) must be a non-empty string");
      } else {
         var3.setProfilingEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_240(ProcessingContext var1) {
   }

   private void __post_240(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ListenerMBeanImpl var3 = (ListenerMBeanImpl)var1.getBoundObject("listener");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6479819](.weblogic-application.listener.listener-uri.) must be a non-empty string");
      } else {
         var3.setListenerUri(var2);
      }
   }

   private void __pre_250(ProcessingContext var1) {
   }

   private void __post_250(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      super.getCurrentModuleContext().setAltDDURI(var2);
   }

   private void __pre_256(ProcessingContext var1) {
   }

   private void __post_256(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      LibraryRefMBeanImpl var3 = (LibraryRefMBeanImpl)var1.getBoundObject("library");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setSpecificationVersion(var2);
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

   private void __pre_145(ProcessingContext var1) {
   }

   private void __post_145(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      EntityMappingMBeanImpl var3 = (EntityMappingMBeanImpl)var1.getBoundObject("entityMapping");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setPublicId(var2);
   }

   private void __pre_208(ProcessingContext var1) {
      PreparedStatementMBeanCustomImpl var2 = new PreparedStatementMBeanCustomImpl();
      var1.addBoundObject(var2, "preparedStatement");
   }

   private void __post_208(ProcessingContext var1) throws SAXProcessorException {
      PreparedStatementMBeanCustomImpl var2 = (PreparedStatementMBeanCustomImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var3 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setPreparedStatement(var2);
   }

   private void __pre_194(ProcessingContext var1) {
   }

   private void __post_194(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PoolParamsMBeanCustomImpl var3 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6758235](.weblogic-application.jdbc-connection-pool.pool-params.jdbcxa-debug-level.) must be a non-empty string");
      } else {
         var3.setJDBCXADebugLevel(Integer.valueOf(var2));
      }
   }

   private void __pre_161(ProcessingContext var1) {
   }

   private void __post_161(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionPropertiesMBeanImpl var3 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var4 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6715065](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.driver-class-name.) must be a non-empty string");
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
         throw new SAXValidationException("PAction[6698217](.weblogic-application.xml.entity-mapping.entity-mapping-name.) must be a non-empty string");
      } else {
         var3.setEntityMappingName(var2);
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
         throw new SAXValidationException("PAction[6686562](.weblogic-application.ejb.entity-cache.max-beans-in-cache.) must be a non-empty string");
      } else {
         try {
            this.validateIntegerGreaterThanZero(var2);
         } catch (Exception var7) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var7.getMessage());
         }

         var3.setMaxBeansInCache(Integer.valueOf(var2));
      }
   }

   private void __pre_201(ProcessingContext var1) {
   }

   private void __post_201(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanCustomImpl var3 = (ConnectionCheckParamsMBeanCustomImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6765528](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.refresh-minutes.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setRefreshMinutes(Integer.valueOf(var2));
      }
   }

   private void __pre_198(ProcessingContext var1) {
   }

   private void __post_198(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanCustomImpl var3 = (ConnectionCheckParamsMBeanCustomImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6762398](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.init-sql.) must be a non-empty string");
      } else {
         var3.setInitSQL(var2);
      }
   }

   private void __pre_203(ProcessingContext var1) {
   }

   private void __post_203(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanCustomImpl var3 = (ConnectionCheckParamsMBeanCustomImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6767660](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.connection-reserve-timeout-seconds.) must be a non-empty string");
      } else {
         try {
            this.validateIntegerGreaterThanNegativeOne(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setConnectionReserveTimeoutSeconds(Integer.valueOf(var2));
      }
   }

   private void __pre_142(ProcessingContext var1) {
   }

   private void __post_142(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ParserFactoryMBeanImpl var3 = (ParserFactoryMBeanImpl)var1.getBoundObject("parserfactory");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6696125](.weblogic-application.xml.parser-factory.transformer-factory.) must be a non-empty string");
      } else {
         var3.setTransformerFactory(var2);
      }
   }

   private void __pre_249(ProcessingContext var1) {
   }

   private void __post_249(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ModuleProviderMBeanImpl var3 = (ModuleProviderMBeanImpl)var1.getBoundObject("moduleProvider");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6349601](.weblogic-application.module-provider.module-factory-class.) must be a non-empty string");
      } else {
         var3.setModuleFactoryClassName(var2);
      }
   }

   private void __pre_212(ProcessingContext var1) {
   }

   private void __post_212(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      StatementMBeanImpl var3 = (StatementMBeanImpl)var1.getBoundObject("statement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6777283](.weblogic-application.jdbc-connection-pool.driver-params.statement.profiling-enabled.) must be a non-empty string");
      } else {
         var3.setProfilingEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_175(ProcessingContext var1) {
   }

   private void __post_175(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6729617](.weblogic-application.jdbc-connection-pool.pool-params.size-params.highest-num-unavailable.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setHighestNumUnavailable(Integer.valueOf(var2));
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

   private void __pre_181(ProcessingContext var1) {
   }

   private void __post_181(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6736024](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.tx-context-on-close-needed.) must be a non-empty string");
      } else {
         var3.setTxContextOnCloseNeeded(Boolean.valueOf(var2));
      }
   }

   private void __pre_206(ProcessingContext var1) {
   }

   private void __post_206(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanCustomImpl var3 = (ConnectionCheckParamsMBeanCustomImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6770948](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.test-frequency-seconds.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setTestFrequencySeconds(Integer.valueOf(var2));
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
         throw new SAXValidationException("PAction[6712083](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.user-name.) must be a non-empty string");
      } else {
         var3.setUserName(var2);
      }
   }

   private void __pre_251(ProcessingContext var1) {
      CustomModuleDescriptor var2 = new CustomModuleDescriptor();
      var1.addBoundObject(var2, "custom");
   }

   private void __post_251(ProcessingContext var1) throws SAXProcessorException {
      CustomModuleDescriptor var2 = (CustomModuleDescriptor)var1.getBoundObject("custom");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addModule(var2);
      super.setCurrentModuleContext(var2);
   }

   private void __pre_190(ProcessingContext var1) {
   }

   private void __post_190(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6753995](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.xa-retry-duration-seconds.) must be a non-empty string");
      } else {
         var3.setXARetryDurationSeconds(Integer.valueOf(var2));
      }
   }

   private void __pre_167(ProcessingContext var1) {
      SizeParamsMBeanCustomImpl var2 = new SizeParamsMBeanCustomImpl();
      var1.addBoundObject(var2, "sizeParams");
   }

   private void __post_167(ProcessingContext var1) throws SAXProcessorException {
      SizeParamsMBeanCustomImpl var2 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanCustomImpl var3 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setSizeParams(var2);
   }

   private void __pre_209(ProcessingContext var1) {
   }

   private void __post_209(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      DriverParamsMBeanImpl var3 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6774190](.weblogic-application.jdbc-connection-pool.driver-params.row-prefetch-enabled.) must be a non-empty string");
      } else {
         var3.setRowPrefetchEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_221(ProcessingContext var1) {
      SecurityRoleAssignmentMBeanImpl var2 = new SecurityRoleAssignmentMBeanImpl();
      var1.addBoundObject(var2, "securityroleassignment");
   }

   private void __post_221(ProcessingContext var1) throws SAXProcessorException {
      SecurityRoleAssignmentMBeanImpl var2 = (SecurityRoleAssignmentMBeanImpl)var1.getBoundObject("securityroleassignment");
      SecurityMBeanImpl var3 = (SecurityMBeanImpl)var1.getBoundObject("security");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addRoleAssignment(var2);
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

   private void __pre_200(ProcessingContext var1) {
   }

   private void __post_200(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanCustomImpl var3 = (ConnectionCheckParamsMBeanCustomImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6764465](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.check-on-release-enabled.) must be a non-empty string");
      } else {
         var3.setCheckOnReleaseEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_253(ProcessingContext var1) {
   }

   private void __post_253(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      CustomModuleDescriptor var3 = (CustomModuleDescriptor)var1.getBoundObject("custom");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setProviderName(var2);
   }

   private void __pre_168(ProcessingContext var1) {
   }

   private void __post_168(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6722308](.weblogic-application.jdbc-connection-pool.pool-params.size-params.initial-capacity.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setInitialCapacity(Integer.valueOf(var2));
      }
   }

   private void __pre_242(ProcessingContext var1) {
   }

   private void __post_242(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      StartupMBeanImpl var3 = (StartupMBeanImpl)var1.getBoundObject("startup");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6481849](.weblogic-application.startup.startup-class.) must be a non-empty string");
      } else {
         var3.setStartupClass(var2);
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

   private void __pre_182(ProcessingContext var1) {
   }

   private void __post_182(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6737078](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.new-conn-for-commit-enabled.) must be a non-empty string");
      } else {
         var3.setNewConnForCommitEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_191(ProcessingContext var1) {
   }

   private void __post_191(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6755044](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.xa-retry-interval-seconds.) must be a non-empty string");
      } else {
         var3.setXARetryIntervalSeconds(Integer.valueOf(var2));
      }
   }

   private void __pre_223(ProcessingContext var1) {
   }

   private void __post_223(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SecurityRoleAssignmentMBeanImpl var3 = (SecurityRoleAssignmentMBeanImpl)var1.getBoundObject("securityroleassignment");
      SecurityMBeanImpl var4 = (SecurityMBeanImpl)var1.getBoundObject("security");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6788780](.weblogic-application.security.security-role-assignment.principal-name.) must be a non-empty string");
      } else {
         var3.addPrincipalName(var2);
      }
   }

   private void __pre_222(ProcessingContext var1) {
   }

   private void __post_222(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SecurityRoleAssignmentMBeanImpl var3 = (SecurityRoleAssignmentMBeanImpl)var1.getBoundObject("securityroleassignment");
      SecurityMBeanImpl var4 = (SecurityMBeanImpl)var1.getBoundObject("security");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6787761](.weblogic-application.security.security-role-assignment.role-name.) must be a non-empty string");
      } else {
         var3.setRoleName(var2);
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
         throw new SAXValidationException("PAction[6694080](.weblogic-application.xml.parser-factory.saxparser-factory.) must be a non-empty string");
      } else {
         var3.setSaxparserFactory(var2);
      }
   }

   private void __pre_169(ProcessingContext var1) {
   }

   private void __post_169(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6723343](.weblogic-application.jdbc-connection-pool.pool-params.size-params.max-capacity.) must be a non-empty string");
      } else {
         try {
            this.validateIntegerGreaterThanZero(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setMaxCapacity(Integer.valueOf(var2));
      }
   }

   private void __pre_176(ProcessingContext var1) {
      XaParamsMBeanImpl var2 = new XaParamsMBeanImpl();
      var1.addBoundObject(var2, "xaParams");
   }

   private void __post_176(ProcessingContext var1) throws SAXProcessorException {
      XaParamsMBeanImpl var2 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var3 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setXaParams(var2);
   }

   private void __pre_228(ProcessingContext var1) {
      ClassloaderStructureMBeanImpl var2 = new ClassloaderStructureMBeanImpl();
      var1.addBoundObject(var2, "clNode1");
   }

   private void __post_228(ProcessingContext var1) throws SAXProcessorException {
      ClassloaderStructureMBeanImpl var2 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode1");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setClassloaderStructure(var2);
   }

   private void __pre_149(ProcessingContext var1) {
   }

   private void __post_149(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EntityMappingMBeanImpl var3 = (EntityMappingMBeanImpl)var1.getBoundObject("entityMapping");
      XMLMBeanImpl var4 = (XMLMBeanImpl)var1.getBoundObject("xmlMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6702266](.weblogic-application.xml.entity-mapping.cache-timeout-interval.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var7) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var7.getMessage());
         }

         var3.setCacheTimeoutInterval(Integer.valueOf(var2));
      }
   }

   private void __pre_180(ProcessingContext var1) {
   }

   private void __post_180(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6734971](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.recover-only-once-enabled.) must be a non-empty string");
      } else {
         var3.setRecoverOnlyOnceEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_178(ProcessingContext var1) {
   }

   private void __post_178(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6732874](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.keep-conn-until-tx-complete-enabled.) must be a non-empty string");
      } else {
         var3.setKeepConnUntilTxCompleteEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_229(ProcessingContext var1) {
      ClassloaderStructureMBeanImpl var2 = new ClassloaderStructureMBeanImpl();
      var1.addBoundObject(var2, "clNode2");
   }

   private void __post_229(ProcessingContext var1) throws SAXProcessorException {
      ClassloaderStructureMBeanImpl var2 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode2");
      ClassloaderStructureMBeanImpl var3 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode1");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addClassloaderStructure(var2);
   }

   private void __pre_235(ProcessingContext var1) {
   }

   private void __post_235(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ModuleRefMBeanImpl var3 = (ModuleRefMBeanImpl)var1.getBoundObject("modRef1");
      ClassloaderStructureMBeanImpl var4 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode1");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6802314](.weblogic-application.classloader-structure.module-ref.module-uri.) must be a non-empty string");
      } else {
         var3.setModuleUri(var2);
      }
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

   private void __pre_192(ProcessingContext var1) {
      ConnectionCheckParamsMBeanCustomImpl var2 = new ConnectionCheckParamsMBeanCustomImpl();
      var1.addBoundObject(var2, "connectionCheck");
   }

   private void __post_192(ProcessingContext var1) throws SAXProcessorException {
      ConnectionCheckParamsMBeanCustomImpl var2 = (ConnectionCheckParamsMBeanCustomImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanCustomImpl var3 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setConnectionCheckParams(var2);
   }

   private void __pre_199(ProcessingContext var1) {
   }

   private void __post_199(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanCustomImpl var3 = (ConnectionCheckParamsMBeanCustomImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6763391](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.check-on-reserve-enabled.) must be a non-empty string");
      } else {
         var3.setCheckOnReserveEnabled(Boolean.valueOf(var2));
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
         throw new SAXValidationException("PAction[6714075](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.url.) must be a non-empty string");
      } else {
         var3.setUrl(var2);
      }
   }

   private void __pre_195(ProcessingContext var1) {
   }

   private void __post_195(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PoolParamsMBeanCustomImpl var3 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6759256](.weblogic-application.jdbc-connection-pool.pool-params.leak-profiling-enabled.) must be a non-empty string");
      } else {
         var3.setLeakProfilingEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_238(ProcessingContext var1) {
      ListenerMBeanImpl var2 = new ListenerMBeanImpl();
      var1.addBoundObject(var2, "listener");
   }

   private void __post_238(ProcessingContext var1) throws SAXProcessorException {
      ListenerMBeanImpl var2 = (ListenerMBeanImpl)var1.getBoundObject("listener");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addListener(var2);
   }

   private void __pre_132(ProcessingContext var1) {
   }

   private void __post_132(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EntityCacheMBeanImpl var3 = (EntityCacheMBeanImpl)var1.getBoundObject("entityCache");
      EjbMBeanImpl var4 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6685570](.weblogic-application.ejb.entity-cache.entity-cache-name.) must be a non-empty string");
      } else {
         var3.setEntityCacheName(var2);
      }
   }

   private void __pre_189(ProcessingContext var1) {
   }

   private void __post_189(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6752928](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.rollback-localtx-upon-connclose.) must be a non-empty string");
      } else {
         var3.setRollbackLocalTxUponConnClose(Boolean.valueOf(var2));
      }
   }

   private void __pre_239(ProcessingContext var1) {
   }

   private void __post_239(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ListenerMBeanImpl var3 = (ListenerMBeanImpl)var1.getBoundObject("listener");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6478848](.weblogic-application.listener.listener-class.) must be a non-empty string");
      } else {
         var3.setListenerClass(var2);
      }
   }

   private void __pre_244(ProcessingContext var1) {
      ShutdownMBeanImpl var2 = new ShutdownMBeanImpl();
      var1.addBoundObject(var2, "shutdown");
   }

   private void __post_244(ProcessingContext var1) throws SAXProcessorException {
      ShutdownMBeanImpl var2 = (ShutdownMBeanImpl)var1.getBoundObject("shutdown");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addShutdown(var2);
   }

   private void __pre_246(ProcessingContext var1) {
   }

   private void __post_246(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ShutdownMBeanImpl var3 = (ShutdownMBeanImpl)var1.getBoundObject("shutdown");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6485818](.weblogic-application.shutdown.shutdown-uri.) must be a non-empty string");
      } else {
         var3.setShutdownUri(var2);
      }
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

   private void __pre_162(ProcessingContext var1) {
      ConnectionParamsMBeanImpl var2 = new ConnectionParamsMBeanImpl();
      var1.addBoundObject(var2, "connectionParams");
   }

   private void __post_162(ProcessingContext var1) throws SAXProcessorException {
      ConnectionParamsMBeanImpl var2 = (ConnectionParamsMBeanImpl)var1.getBoundObject("connectionParams");
      ConnectionPropertiesMBeanImpl var3 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var4 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setConnectionParams(var2);
   }

   private void __pre_247(ProcessingContext var1) {
      ModuleProviderMBeanImpl var2 = new ModuleProviderMBeanImpl();
      var1.addBoundObject(var2, "moduleProvider");
   }

   private void __post_247(ProcessingContext var1) throws SAXProcessorException {
      ModuleProviderMBeanImpl var2 = (ModuleProviderMBeanImpl)var1.getBoundObject("moduleProvider");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addModuleProvider(var2);
   }

   private void __pre_210(ProcessingContext var1) {
   }

   private void __post_210(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      DriverParamsMBeanImpl var3 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6775240](.weblogic-application.jdbc-connection-pool.driver-params.row-prefetch-size.) must be a non-empty string");
      } else {
         var3.setRowPrefetchSize(Integer.valueOf(var2));
      }
   }

   private void __pre_186(ProcessingContext var1) {
   }

   private void __post_186(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6749519](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.resource-health-monitoring-enabled.) must be a non-empty string");
      } else {
         var3.setResourceHealthMonitoringEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_134(ProcessingContext var1) {
   }

   private void __post_134(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EntityCacheMBeanImpl var3 = (EntityCacheMBeanImpl)var1.getBoundObject("entityCache");
      EjbMBeanImpl var4 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6687700](.weblogic-application.ejb.entity-cache.caching-strategy.) must be a non-empty string");
      } else {
         this.validateCachingStrategy(var3, var2);
         var3.setCachingStrategy(var2);
      }
   }

   private void __pre_237(ProcessingContext var1) {
   }

   private void __post_237(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ModuleRefMBeanImpl var3 = (ModuleRefMBeanImpl)var1.getBoundObject("modRef3");
      ClassloaderStructureMBeanImpl var4 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode3");
      ClassloaderStructureMBeanImpl var5 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode2");
      ClassloaderStructureMBeanImpl var6 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode1");
      WeblogicDeploymentDescriptor var7 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6804403](.weblogic-application.classloader-structure.classloader-structure.classloader-structure.module-ref.module-uri.) must be a non-empty string");
      } else {
         var3.setModuleUri(var2);
      }
   }

   private void __pre_215(ProcessingContext var1) {
   }

   private void __post_215(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PreparedStatementMBeanCustomImpl var3 = (PreparedStatementMBeanCustomImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6780427](.weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.cache-size.) must be a non-empty string");
      } else {
         try {
            this.validateStmtCacheSize(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setCacheSize(Integer.valueOf(var2));
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
         throw new SAXValidationException("PAction[6713080](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.password.) must be a non-empty string");
      } else {
         var3.setPassword(var2);
      }
   }

   private void __pre_216(ProcessingContext var1) {
   }

   private void __post_216(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PreparedStatementMBeanCustomImpl var3 = (PreparedStatementMBeanCustomImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6781469](.weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.cache-type.) must be a non-empty string");
      } else {
         try {
            this.validateStmtCacheType(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setCacheType(var2);
      }
   }

   private void __pre_177(ProcessingContext var1) {
   }

   private void __post_177(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6731860](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.debug-level.) must be a non-empty string");
      } else {
         var3.setDebugLevel(Integer.valueOf(var2));
      }
   }

   private void __pre_172(ProcessingContext var1) {
   }

   private void __post_172(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6726444](.weblogic-application.jdbc-connection-pool.pool-params.size-params.shrink-period-minutes.) must be a non-empty string");
      } else {
         try {
            this.validateIntegerGreaterThanZero(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setShrinkPeriodMinutes(Integer.valueOf(var2));
      }
   }

   private void __pre_226(ProcessingContext var1) {
   }

   private void __post_226(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ApplicationParamMBeanImpl var3 = (ApplicationParamMBeanImpl)var1.getBoundObject("appParam");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6792019](.weblogic-application.application-param.param-name.) must be a non-empty string");
      } else {
         var3.setParamName(var2);
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
         throw new SAXValidationException("PAction[6689879](.weblogic-application.ejb.entity-cache.max-cache-size.bytes.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setBytes(Integer.valueOf(var2));
      }
   }

   private void __pre_197(ProcessingContext var1) {
   }

   private void __post_197(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanCustomImpl var3 = (ConnectionCheckParamsMBeanCustomImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6761385](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.table-name.) must be a non-empty string");
      } else {
         var3.setTableName(var2);
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
         throw new SAXValidationException("PAction[6719269](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.param-value.) must be a non-empty string");
      } else {
         var3.setParamValue(var2);
      }
   }

   private void __pre_207(ProcessingContext var1) {
      StatementMBeanImpl var2 = new StatementMBeanImpl();
      var1.addBoundObject(var2, "statement");
   }

   private void __post_207(ProcessingContext var1) throws SAXProcessorException {
      StatementMBeanImpl var2 = (StatementMBeanImpl)var1.getBoundObject("statement");
      DriverParamsMBeanImpl var3 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setStatement(var2);
   }

   private void __pre_211(ProcessingContext var1) {
   }

   private void __post_211(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      DriverParamsMBeanImpl var3 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6776262](.weblogic-application.jdbc-connection-pool.driver-params.stream-chunk-size.) must be a non-empty string");
      } else {
         var3.setStreamChunkSize(Integer.valueOf(var2));
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
         throw new SAXValidationException("PAction[6718289](.weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.param-name.) must be a non-empty string");
      } else {
         var3.setParamName(var2);
      }
   }

   private void __pre_204(ProcessingContext var1) {
   }

   private void __post_204(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanCustomImpl var3 = (ConnectionCheckParamsMBeanCustomImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6768757](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.connection-creation-retry-frequency-seconds.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setConnectionCreationRetryFrequencySeconds(Integer.valueOf(var2));
      }
   }

   private void __pre_258(ProcessingContext var1) {
   }

   private void __post_258(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      LibraryRefMBeanImpl var3 = (LibraryRefMBeanImpl)var1.getBoundObject("library");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setExactMatch(var2);
   }

   private void __pre_248(ProcessingContext var1) {
   }

   private void __post_248(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ModuleProviderMBeanImpl var3 = (ModuleProviderMBeanImpl)var1.getBoundObject("moduleProvider");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6348627](.weblogic-application.module-provider.name.) must be a non-empty string");
      } else {
         var3.setName(var2);
      }
   }

   private void __pre_202(ProcessingContext var1) {
   }

   private void __post_202(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanCustomImpl var3 = (ConnectionCheckParamsMBeanCustomImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6766596](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.check-on-create-enabled.) must be a non-empty string");
      } else {
         var3.setCheckOnCreateEnabled(Boolean.valueOf(var2));
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

   private void __pre_163(ProcessingContext var1) {
      ParameterMBeanImpl var2 = new ParameterMBeanImpl();
      var1.addBoundObject(var2, "parameter");
   }

   private void __post_163(ProcessingContext var1) throws SAXProcessorException {
      ParameterMBeanImpl var2 = (ParameterMBeanImpl)var1.getBoundObject("parameter");
      ConnectionParamsMBeanImpl var3 = (ConnectionParamsMBeanImpl)var1.getBoundObject("connectionParams");
      ConnectionPropertiesMBeanImpl var4 = (ConnectionPropertiesMBeanImpl)var1.getBoundObject("connectionProperties");
      ConnectionFactoryMBeanImpl var5 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var6 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var7 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addParameter(var2);
   }

   private void __pre_156(ProcessingContext var1) {
   }

   private void __post_156(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionFactoryMBeanImpl var3 = (ConnectionFactoryMBeanImpl)var1.getBoundObject("connectionFactory");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6709825](.weblogic-application.jdbc-connection-pool.connection-factory.factory-name.) must be a non-empty string");
      } else {
         var3.setFactoryName(var2);
      }
   }

   private void __pre_236(ProcessingContext var1) {
   }

   private void __post_236(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ModuleRefMBeanImpl var3 = (ModuleRefMBeanImpl)var1.getBoundObject("modRef2");
      ClassloaderStructureMBeanImpl var4 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode2");
      ClassloaderStructureMBeanImpl var5 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode1");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6803345](.weblogic-application.classloader-structure.classloader-structure.module-ref.module-uri.) must be a non-empty string");
      } else {
         var3.setModuleUri(var2);
      }
   }

   private void __pre_196(ProcessingContext var1) {
   }

   private void __post_196(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PoolParamsMBeanCustomImpl var3 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6760309](.weblogic-application.jdbc-connection-pool.pool-params.remove-infected-connections-enabled.) must be a non-empty string");
      } else {
         var3.setRemoveInfectedConnectionsEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_220(ProcessingContext var1) {
   }

   private void __post_220(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SecurityMBeanImpl var3 = (SecurityMBeanImpl)var1.getBoundObject("security");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setRealmName(var2);
   }

   private void __pre_224(ProcessingContext var1) {
      ApplicationParamMBeanImpl var2 = new ApplicationParamMBeanImpl();
      var1.addBoundObject(var2, "appParam");
   }

   private void __post_224(ProcessingContext var1) throws SAXProcessorException {
      ApplicationParamMBeanImpl var2 = (ApplicationParamMBeanImpl)var1.getBoundObject("appParam");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addParameter(var2);
   }

   private void __pre_232(ProcessingContext var1) {
      ModuleRefMBeanImpl var2 = new ModuleRefMBeanImpl();
      var1.addBoundObject(var2, "modRef1");
   }

   private void __post_232(ProcessingContext var1) throws SAXProcessorException {
      ModuleRefMBeanImpl var2 = (ModuleRefMBeanImpl)var1.getBoundObject("modRef1");
      ClassloaderStructureMBeanImpl var3 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode1");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addModuleRef(var2);
   }

   private void __pre_227(ProcessingContext var1) {
   }

   private void __post_227(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ApplicationParamMBeanImpl var3 = (ApplicationParamMBeanImpl)var1.getBoundObject("appParam");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6793003](.weblogic-application.application-param.param-value.) must be a non-empty string");
      } else {
         var3.setParamValue(var2);
      }
   }

   private void __pre_230(ProcessingContext var1) {
      ClassloaderStructureMBeanImpl var2 = new ClassloaderStructureMBeanImpl();
      var1.addBoundObject(var2, "clNode3");
   }

   private void __post_230(ProcessingContext var1) throws SAXProcessorException {
      ClassloaderStructureMBeanImpl var2 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode3");
      ClassloaderStructureMBeanImpl var3 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode2");
      ClassloaderStructureMBeanImpl var4 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode1");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addClassloaderStructure(var2);
   }

   private void __pre_257(ProcessingContext var1) {
   }

   private void __post_257(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      LibraryRefMBeanImpl var3 = (LibraryRefMBeanImpl)var1.getBoundObject("library");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setImplementationVersion(var2);
   }

   private void __pre_254(ProcessingContext var1) {
      LibraryRefMBeanImpl var2 = new LibraryRefMBeanImpl();
      var1.addBoundObject(var2, "library");
   }

   private void __post_254(ProcessingContext var1) throws SAXProcessorException {
      LibraryRefMBeanImpl var2 = (LibraryRefMBeanImpl)var1.getBoundObject("library");
      WeblogicDeploymentDescriptor var3 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addLibrary(var2);
   }

   private void __pre_174(ProcessingContext var1) {
   }

   private void __post_174(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6728578](.weblogic-application.jdbc-connection-pool.pool-params.size-params.highest-num-waiters.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setHighestNumWaiters(Integer.valueOf(var2));
      }
   }

   private void __pre_151(ProcessingContext var1) {
   }

   private void __post_151(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      JdbcConnectionPoolMBeanImpl var3 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6704456](.weblogic-application.jdbc-connection-pool.data-source-name.) must be a non-empty string");
      } else {
         var3.setDataSourceName(var2);
      }
   }

   private void __pre_130(ProcessingContext var1) {
   }

   private void __post_130(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      EjbMBeanImpl var3 = (EjbMBeanImpl)var1.getBoundObject("ejbMBean");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6679921](.weblogic-application.ejb.start-mdbs-with-application.) must be a non-empty string");
      } else if (!"true".equals(var2) && !"True".equals(var2) && !"false".equals(var2) && !"False".equals(var2)) {
         throw new SAXValidationException("PAction[6679921](.weblogic-application.ejb.start-mdbs-with-application.) must be one of the values: true,True,false,False");
      } else {
         var3.setStartMdbsWithApplication("True".equalsIgnoreCase(var2));
      }
   }

   private void __pre_152(ProcessingContext var1) {
   }

   private void __post_152(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      JdbcConnectionPoolMBeanImpl var3 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6705477](.weblogic-application.jdbc-connection-pool.acl-name.) must be a non-empty string");
      } else {
         var3.setAclName(var2);
      }
   }

   private void __pre_187(ProcessingContext var1) {
   }

   private void __post_187(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6750589](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.xa-set-transaction-timeout.) must be a non-empty string");
      } else {
         var3.setXASetTransactionTimeout(Boolean.valueOf(var2));
      }
   }

   private void __pre_214(ProcessingContext var1) {
   }

   private void __post_214(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PreparedStatementMBeanCustomImpl var3 = (PreparedStatementMBeanCustomImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6779367](.weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.cache-profiling-threshold.) must be a non-empty string");
      } else {
         var3.setCacheProfilingThreshold(Integer.valueOf(var2));
      }
   }

   private void __pre_184(ProcessingContext var1) {
   }

   private void __post_184(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6739197](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.keep-logical-conn-open-on-release.) must be a non-empty string");
      } else {
         var3.setKeepLogicalConnOpenOnRelease(Boolean.valueOf(var2));
      }
   }

   private void __pre_219(ProcessingContext var1) {
      SecurityMBeanImpl var2 = new SecurityMBeanImpl();
      var1.addBoundObject(var2, "security");
   }

   private void __post_219(ProcessingContext var1) throws SAXProcessorException {
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

   private void __pre_173(ProcessingContext var1) {
   }

   private void __post_173(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6727509](.weblogic-application.jdbc-connection-pool.pool-params.size-params.shrink-frequency-seconds.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setShrinkFrequencySeconds(Integer.valueOf(var2));
      }
   }

   private void __pre_231(ProcessingContext var1) {
      ClassloaderStructureMBeanImpl var2 = new ClassloaderStructureMBeanImpl();
      var1.addBoundObject(var2, "clNode4");
   }

   private void __post_231(ProcessingContext var1) throws SAXProcessorException {
      ClassloaderStructureMBeanImpl var2 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode4");
      ClassloaderStructureMBeanImpl var3 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode3");
      ClassloaderStructureMBeanImpl var4 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode2");
      ClassloaderStructureMBeanImpl var5 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode1");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      String var7 = "classloader-structure element in weblogic-application.xml is nested too deeply. Nesting is restricted to 3 levels.";
      DeploymentException var8 = new DeploymentException(var7);
      throw new SAXProcessorException(var7, var8);
   }

   private void __pre_255(ProcessingContext var1) {
   }

   private void __post_255(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      LibraryRefMBeanImpl var3 = (LibraryRefMBeanImpl)var1.getBoundObject("library");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6355548](.weblogic-application.library-ref.library-name.) must be a non-empty string");
      } else {
         var3.setLibraryName(var2);
      }
   }

   private void __pre_218(ProcessingContext var1) {
   }

   private void __post_218(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PreparedStatementMBeanCustomImpl var3 = (PreparedStatementMBeanCustomImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6783548](.weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.max-parameter-length.) must be a non-empty string");
      } else {
         var3.setMaxParameterLength(Integer.valueOf(var2));
      }
   }

   private void __pre_225(ProcessingContext var1) {
   }

   private void __post_225(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ApplicationParamMBeanImpl var3 = (ApplicationParamMBeanImpl)var1.getBoundObject("appParam");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6791035](.weblogic-application.application-param.description.) must be a non-empty string");
      } else {
         var3.setDescription(var2);
      }
   }

   private void __pre_252(ProcessingContext var1) {
   }

   private void __post_252(ProcessingContext var1) throws SAXProcessorException {
      String var2 = Functions.value(var1);
      CustomModuleDescriptor var3 = (CustomModuleDescriptor)var1.getBoundObject("custom");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.setURI(var2);
   }

   private void __pre_179(ProcessingContext var1) {
   }

   private void __post_179(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6733944](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.end-only-once-enabled.) must be a non-empty string");
      } else {
         var3.setEndOnlyOnceEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_185(ProcessingContext var1) {
   }

   private void __post_185(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6748458](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.local-transaction-supported.) must be a non-empty string");
      } else {
         var3.setLocalTransactionSupported(Boolean.valueOf(var2));
      }
   }

   private void __pre_193(ProcessingContext var1) {
   }

   private void __post_193(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PoolParamsMBeanCustomImpl var3 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var4 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6757214](.weblogic-application.jdbc-connection-pool.pool-params.login-delay-seconds.) must be a non-empty string");
      } else {
         var3.setLoginDelaySeconds(Integer.valueOf(var2));
      }
   }

   private void __pre_205(ProcessingContext var1) {
   }

   private void __post_205(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      ConnectionCheckParamsMBeanCustomImpl var3 = (ConnectionCheckParamsMBeanCustomImpl)var1.getBoundObject("connectionCheck");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6769859](.weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.inactive-connection-timeout-seconds.) must be a non-empty string");
      } else {
         try {
            this.validatePositiveInteger(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

         var3.setInactiveConnectionTimeoutSeconds(Integer.valueOf(var2));
      }
   }

   private void __pre_183(ProcessingContext var1) {
   }

   private void __post_183(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6738138](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.prepared-statement-cache-size.) must be a non-empty string");
      } else {
         var3.setPreparedStatementCacheSize(Integer.valueOf(var2));
      }
   }

   private void __pre_188(ProcessingContext var1) {
   }

   private void __post_188(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      XaParamsMBeanImpl var3 = (XaParamsMBeanImpl)var1.getBoundObject("xaParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6751642](.weblogic-application.jdbc-connection-pool.pool-params.xa-params.xa-transaction-timeout.) must be a non-empty string");
      } else {
         var3.setXATransactionTimeout(Integer.valueOf(var2));
      }
   }

   private void __pre_243(ProcessingContext var1) {
   }

   private void __post_243(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      StartupMBeanImpl var3 = (StartupMBeanImpl)var1.getBoundObject("startup");
      WeblogicDeploymentDescriptor var4 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6482819](.weblogic-application.startup.startup-uri.) must be a non-empty string");
      } else {
         var3.setStartupUri(var2);
      }
   }

   private void __pre_233(ProcessingContext var1) {
      ModuleRefMBeanImpl var2 = new ModuleRefMBeanImpl();
      var1.addBoundObject(var2, "modRef2");
   }

   private void __post_233(ProcessingContext var1) throws SAXProcessorException {
      ModuleRefMBeanImpl var2 = (ModuleRefMBeanImpl)var1.getBoundObject("modRef2");
      ClassloaderStructureMBeanImpl var3 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode2");
      ClassloaderStructureMBeanImpl var4 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode1");
      WeblogicDeploymentDescriptor var5 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addModuleRef(var2);
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

   private void __pre_234(ProcessingContext var1) {
      ModuleRefMBeanImpl var2 = new ModuleRefMBeanImpl();
      var1.addBoundObject(var2, "modRef3");
   }

   private void __post_234(ProcessingContext var1) throws SAXProcessorException {
      ModuleRefMBeanImpl var2 = (ModuleRefMBeanImpl)var1.getBoundObject("modRef3");
      ClassloaderStructureMBeanImpl var3 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode3");
      ClassloaderStructureMBeanImpl var4 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode2");
      ClassloaderStructureMBeanImpl var5 = (ClassloaderStructureMBeanImpl)var1.getBoundObject("clNode1");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      var3.addModuleRef(var2);
   }

   private void __pre_217(ProcessingContext var1) {
   }

   private void __post_217(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      PreparedStatementMBeanCustomImpl var3 = (PreparedStatementMBeanCustomImpl)var1.getBoundObject("preparedStatement");
      DriverParamsMBeanImpl var4 = (DriverParamsMBeanImpl)var1.getBoundObject("driverParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6782482](.weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.parameter-logging-enabled.) must be a non-empty string");
      } else {
         var3.setParameterLoggingEnabled(Boolean.valueOf(var2));
      }
   }

   private void __pre_170(ProcessingContext var1) {
   }

   private void __post_170(ProcessingContext var1) throws SAXProcessorException, SAXValidationException {
      String var2 = Functions.value(var1);
      SizeParamsMBeanCustomImpl var3 = (SizeParamsMBeanCustomImpl)var1.getBoundObject("sizeParams");
      PoolParamsMBeanCustomImpl var4 = (PoolParamsMBeanCustomImpl)var1.getBoundObject("poolParams");
      JdbcConnectionPoolMBeanImpl var5 = (JdbcConnectionPoolMBeanImpl)var1.getBoundObject("jdbcConnectionPool");
      WeblogicDeploymentDescriptor var6 = (WeblogicDeploymentDescriptor)var1.getBoundObject("wlApplication");
      if (var2.length() == 0) {
         throw new SAXValidationException("PAction[6724380](.weblogic-application.jdbc-connection-pool.pool-params.size-params.capacity-increment.) must be a non-empty string");
      } else {
         try {
            this.validateIntegerGreaterThanZero(var2);
         } catch (Exception var8) {
            throw new SAXValidationException("Path " + var1.getPath() + ": " + var8.getMessage());
         }

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
      paths.put(".weblogic-application.library-ref.context-path.", new Integer(259));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.shrinking-enabled.", new Integer(171));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.description.", new Integer(166));
      paths.put(".weblogic-application.ejb.entity-cache.max-cache-size.megabytes.", new Integer(137));
      paths.put(".weblogic-application.shutdown.shutdown-class.", new Integer(245));
      paths.put(".weblogic-application.xml.parser-factory.document-builder-factory.", new Integer(141));
      paths.put(".weblogic-application.xml.entity-mapping.when-to-cache.", new Integer(148));
      paths.put(".weblogic-application.startup.", new Integer(241));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.", new Integer(154));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.profiling-enabled.", new Integer(213));
      paths.put(".weblogic-application.listener.listener-uri.", new Integer(240));
      paths.put(".weblogic-application.module.alt-dd.", new Integer(250));
      paths.put(".weblogic-application.library-ref.specification-version.", new Integer(256));
      paths.put(".weblogic-application.ejb.entity-cache.max-cache-size.", new Integer(135));
      paths.put(".weblogic-application.xml.entity-mapping.public-id.", new Integer(145));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.", new Integer(208));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.jdbcxa-debug-level.", new Integer(194));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.driver-class-name.", new Integer(161));
      paths.put(".weblogic-application.xml.entity-mapping.entity-mapping-name.", new Integer(144));
      paths.put(".weblogic-application.ejb.entity-cache.max-beans-in-cache.", new Integer(133));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.refresh-minutes.", new Integer(201));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.init-sql.", new Integer(198));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.connection-reserve-timeout-seconds.", new Integer(203));
      paths.put(".weblogic-application.xml.parser-factory.transformer-factory.", new Integer(142));
      paths.put(".weblogic-application.module-provider.module-factory-class.", new Integer(249));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.statement.profiling-enabled.", new Integer(212));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.highest-num-unavailable.", new Integer(175));
      paths.put(".weblogic-application.ejb.", new Integer(129));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.tx-context-on-close-needed.", new Integer(181));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.test-frequency-seconds.", new Integer(206));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.user-name.", new Integer(158));
      paths.put(".weblogic-application.module.custom.", new Integer(251));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.xa-retry-duration-seconds.", new Integer(190));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.", new Integer(167));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.row-prefetch-enabled.", new Integer(209));
      paths.put(".weblogic-application.security.security-role-assignment.", new Integer(221));
      paths.put(".weblogic-application.", new Integer(128));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.check-on-release-enabled.", new Integer(200));
      paths.put(".weblogic-application.module.custom.provider-name.", new Integer(253));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.initial-capacity.", new Integer(168));
      paths.put(".weblogic-application.startup.startup-class.", new Integer(242));
      paths.put(".weblogic-application.xml.", new Integer(138));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.new-conn-for-commit-enabled.", new Integer(182));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.xa-retry-interval-seconds.", new Integer(191));
      paths.put(".weblogic-application.security.security-role-assignment.principal-name.", new Integer(223));
      paths.put(".weblogic-application.security.security-role-assignment.role-name.", new Integer(222));
      paths.put(".weblogic-application.xml.parser-factory.saxparser-factory.", new Integer(140));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.max-capacity.", new Integer(169));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.", new Integer(176));
      paths.put(".weblogic-application.classloader-structure.", new Integer(228));
      paths.put(".weblogic-application.xml.entity-mapping.cache-timeout-interval.", new Integer(149));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.recover-only-once-enabled.", new Integer(180));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.keep-conn-until-tx-complete-enabled.", new Integer(178));
      paths.put(".weblogic-application.classloader-structure.classloader-structure.", new Integer(229));
      paths.put(".weblogic-application.classloader-structure.module-ref.module-uri.", new Integer(235));
      paths.put(".weblogic-application.jdbc-connection-pool.", new Integer(150));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.", new Integer(153));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.", new Integer(192));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.check-on-reserve-enabled.", new Integer(199));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.url.", new Integer(160));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.leak-profiling-enabled.", new Integer(195));
      paths.put(".weblogic-application.listener.", new Integer(238));
      paths.put(".weblogic-application.ejb.entity-cache.entity-cache-name.", new Integer(132));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.rollback-localtx-upon-connclose.", new Integer(189));
      paths.put(".weblogic-application.listener.listener-class.", new Integer(239));
      paths.put(".weblogic-application.shutdown.", new Integer(244));
      paths.put(".weblogic-application.shutdown.shutdown-uri.", new Integer(246));
      paths.put(".weblogic-application.ejb.entity-cache.", new Integer(131));
      paths.put(".weblogic-application.xml.entity-mapping.", new Integer(143));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.", new Integer(162));
      paths.put(".weblogic-application.module-provider.", new Integer(247));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.row-prefetch-size.", new Integer(210));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.resource-health-monitoring-enabled.", new Integer(186));
      paths.put(".weblogic-application.ejb.entity-cache.caching-strategy.", new Integer(134));
      paths.put(".weblogic-application.classloader-structure.classloader-structure.classloader-structure.module-ref.module-uri.", new Integer(237));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.cache-size.", new Integer(215));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.password.", new Integer(159));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.cache-type.", new Integer(216));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.debug-level.", new Integer(177));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.shrink-period-minutes.", new Integer(172));
      paths.put(".weblogic-application.application-param.param-name.", new Integer(226));
      paths.put(".weblogic-application.ejb.entity-cache.max-cache-size.bytes.", new Integer(136));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.table-name.", new Integer(197));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.param-value.", new Integer(165));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.statement.", new Integer(207));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.stream-chunk-size.", new Integer(211));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.", new Integer(155));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.param-name.", new Integer(164));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.connection-creation-retry-frequency-seconds.", new Integer(204));
      paths.put(".weblogic-application.library-ref.exact-match.", new Integer(258));
      paths.put(".weblogic-application.module-provider.name.", new Integer(248));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.check-on-create-enabled.", new Integer(202));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.", new Integer(157));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.connection-properties.connection-params.parameter.", new Integer(163));
      paths.put(".weblogic-application.jdbc-connection-pool.connection-factory.factory-name.", new Integer(156));
      paths.put(".weblogic-application.classloader-structure.classloader-structure.module-ref.module-uri.", new Integer(236));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.remove-infected-connections-enabled.", new Integer(196));
      paths.put(".weblogic-application.security.realm-name.", new Integer(220));
      paths.put(".weblogic-application.application-param.", new Integer(224));
      paths.put(".weblogic-application.classloader-structure.module-ref.", new Integer(232));
      paths.put(".weblogic-application.application-param.param-value.", new Integer(227));
      paths.put(".weblogic-application.classloader-structure.classloader-structure.classloader-structure.", new Integer(230));
      paths.put(".weblogic-application.library-ref.implementation-version.", new Integer(257));
      paths.put(".weblogic-application.library-ref.", new Integer(254));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.highest-num-waiters.", new Integer(174));
      paths.put(".weblogic-application.jdbc-connection-pool.data-source-name.", new Integer(151));
      paths.put(".weblogic-application.ejb.start-mdbs-with-application.", new Integer(130));
      paths.put(".weblogic-application.jdbc-connection-pool.acl-name.", new Integer(152));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.xa-set-transaction-timeout.", new Integer(187));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.cache-profiling-threshold.", new Integer(214));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.keep-logical-conn-open-on-release.", new Integer(184));
      paths.put(".weblogic-application.security.", new Integer(219));
      paths.put(".weblogic-application.xml.entity-mapping.entity-uri.", new Integer(147));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.shrink-frequency-seconds.", new Integer(173));
      paths.put(".weblogic-application.classloader-structure.classloader-structure.classloader-structure.classloader-structure.", new Integer(231));
      paths.put(".weblogic-application.library-ref.library-name.", new Integer(255));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.max-parameter-length.", new Integer(218));
      paths.put(".weblogic-application.application-param.description.", new Integer(225));
      paths.put(".weblogic-application.module.custom.uri.", new Integer(252));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.end-only-once-enabled.", new Integer(179));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.local-transaction-supported.", new Integer(185));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.login-delay-seconds.", new Integer(193));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.connection-check-params.inactive-connection-timeout-seconds.", new Integer(205));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.prepared-statement-cache-size.", new Integer(183));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.xa-params.xa-transaction-timeout.", new Integer(188));
      paths.put(".weblogic-application.startup.startup-uri.", new Integer(243));
      paths.put(".weblogic-application.classloader-structure.classloader-structure.module-ref.", new Integer(233));
      paths.put(".weblogic-application.xml.entity-mapping.system-id.", new Integer(146));
      paths.put(".weblogic-application.classloader-structure.classloader-structure.classloader-structure.module-ref.", new Integer(234));
      paths.put(".weblogic-application.jdbc-connection-pool.driver-params.prepared-statement.parameter-logging-enabled.", new Integer(217));
      paths.put(".weblogic-application.jdbc-connection-pool.pool-params.size-params.capacity-increment.", new Integer(170));
      paths.put(".weblogic-application.xml.parser-factory.", new Integer(139));
   }
}
