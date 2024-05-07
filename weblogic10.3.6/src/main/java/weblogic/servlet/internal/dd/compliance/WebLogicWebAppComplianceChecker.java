package weblogic.servlet.internal.dd.compliance;

import java.io.File;
import java.nio.charset.Charset;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.wl.CharsetMappingBean;
import weblogic.j2ee.descriptor.wl.CharsetParamsBean;
import weblogic.j2ee.descriptor.wl.InputCharsetBean;
import weblogic.j2ee.descriptor.wl.SessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.VirtualDirectoryMappingBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.utils.ErrorCollectionException;

public class WebLogicWebAppComplianceChecker extends BaseComplianceChecker {
   private static final String CHARSET_MAPPING = "charset-mapping";
   private static final String INPUT_CHARSET = "input-charset";
   private static final String URL_MATCH_MAP = "url-match-map";
   private static final String URL_MATCH_MAP_SUPERCLASS = "weblogic.servlet.utils.URLMapping";

   public void check(DeploymentInfo var1) throws ErrorCollectionException {
      WeblogicWebAppBean var2 = var1.getWeblogicWebAppBean();
      if (var2 != null) {
         CharsetParamsBean var3 = (CharsetParamsBean)DescriptorUtils.getOrCreateFirstChild(var2, var2.getCharsetParams(), "CharsetParams");
         if (var3 != null) {
            this.validateCharsetMapping(var3.getCharsetMappings());
            this.validateInputCharset(var3.getInputCharsets());
         }

         VirtualDirectoryMappingBean[] var4 = var2.getVirtualDirectoryMappings();
         this.validateVirtualDirectoryMappings(var4);
         this.validateURLMatchMap(var2.getUrlMatchMaps().length > 0 ? var2.getUrlMatchMaps()[0] : null, var1);
         this.validateSessionDescriptor((SessionDescriptorBean)DescriptorUtils.getOrCreateFirstChild(var2, var2.getSessionDescriptors(), "SessionDescriptor"));
         this.checkForExceptions();
      }
   }

   private void validateCharsetMapping(CharsetMappingBean[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            CharsetMappingBean var3 = var1[var2];
            String var4 = var3.getJavaCharsetName();
            if (!Charset.isSupported(var4)) {
               this.update(this.fmt.warning() + this.fmt.UNSUPPORTED_ENCODING("charset-mapping", var4));
            }
         }

      }
   }

   private void validateInputCharset(InputCharsetBean[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2].getJavaCharsetName();
            if (!Charset.isSupported(var3)) {
               this.update(this.fmt.warning() + this.fmt.UNSUPPORTED_ENCODING("input-charset", var3));
            }
         }

      }
   }

   private void validateVirtualDirectoryMappings(VirtualDirectoryMappingBean[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2].getLocalPath();
            if (var3 != null) {
               File var4 = new File(var3);
               if (!var4.exists()) {
                  this.update(this.fmt.warning() + this.fmt.INVALID_LOCAL_PATH(var3));
               }
            } else {
               this.update(this.fmt.warning() + this.fmt.INVALID_LOCAL_PATH(var3));
            }
         }

      }
   }

   private void validateURLMatchMap(String var1, DeploymentInfo var2) {
      ClassLoader var3 = var2.getClassLoader();
      if (var1 != null) {
         this.isClassAssignable(var3, "url-match-map", var1, "weblogic.servlet.utils.URLMapping");
      }

   }

   private void validateSessionDescriptor(SessionDescriptorBean var1) {
      if (var1 != null) {
         String var2 = var1.getCookieDomain();
         if (var2 != null && var2 != null && !var2.startsWith(".")) {
            this.update(this.fmt.warning() + this.fmt.INVALID_COOKIE_DOMAIN(var2));
         }

      }
   }
}
