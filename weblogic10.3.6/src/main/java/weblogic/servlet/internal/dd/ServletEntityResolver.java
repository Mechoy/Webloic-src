package weblogic.servlet.internal.dd;

import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.ResourceEntityResolver;

public final class ServletEntityResolver extends ResourceEntityResolver implements DescriptorConstants {
   private static final boolean debug = false;
   private String rootDir;

   public ServletEntityResolver() {
      this.addEntityResource("-//Sun Microsystems, Inc.//DTD Web Application 1.2//EN", "web-jar.dtd", this.getClass());
      this.addEntityResource("-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN", "web-jar.dtd", this.getClass());
      this.addEntityResource("-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN", "web-jar-23.dtd", this.getClass());
      this.addEntityResource("-//BEA Systems, Inc.//DTD Web Application 6.0//EN", "weblogic-web-jar.dtd", this.getClass());
      this.addEntityResource("-//BEA Systems, Inc.//DTD Web Application 6.1//EN", "weblogic-web-jar.dtd", this.getClass());
      this.addEntityResource("-//BEA Systems, Inc.//DTD Web Application 7.0//EN", "weblogic700-web-jar.dtd", this.getClass());
      this.addEntityResource("-//BEA Systems, Inc.//DTD Web Application 8.1//EN", "weblogic810-web-jar.dtd", this.getClass());
   }

   public ServletEntityResolver(String var1) {
      this();
      this.rootDir = var1;
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      if ("-//Sun Microsystems, Inc.//DTD Web Application 1.2//EN".equals(var1) || "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN".equals(var1)) {
         if (this.rootDir == null) {
            this.rootDir = "";
         }

         HTTPLogger.logOldPublicIDWarningWithContext(this.rootDir, "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN");
      }

      InputSource var3;
      try {
         var3 = super.resolveEntity(var1, var2);
      } catch (SAXException var5) {
         HTTPLogger.logErrorResolvingServletEntity(var1, this.rootDir, var5);
         throw var5;
      }

      if (var3 == null) {
         HTTPLogger.logCouldNotResolveServletEntity(var1, this.rootDir);
      }

      return var3;
   }
}
