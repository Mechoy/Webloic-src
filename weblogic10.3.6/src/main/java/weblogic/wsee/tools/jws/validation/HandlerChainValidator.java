package weblogic.wsee.tools.jws.validation;

import com.bea.util.jam.JClass;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jws.HandlerChain;
import javax.jws.soap.SOAPMessageHandlers;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;

class HandlerChainValidator {
   private final Logger logger;
   private final WebServiceDecl webService;
   private final ClassLoader classLoader;
   private final String[] sourcepath;

   HandlerChainValidator(WebServiceDecl var1, Logger var2, ClassLoader var3, String[] var4) {
      this.logger = var2;
      this.webService = var1;
      this.classLoader = var3;
      this.sourcepath = var4;
   }

   void validate(JClass var1) {
      if (var1.getAnnotation(HandlerChain.class) != null && var1.getAnnotation(SOAPMessageHandlers.class) != null) {
         this.logger.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "type.handlerchain.duplicate", new Object[0])));
      }

      Map var2 = this.webService.getHandlerChainDecl().getLogEvents();
      Iterator var3 = var2.keySet().iterator();

      while(var3.hasNext()) {
         EventLevel var4 = (EventLevel)var3.next();
         Iterator var5 = ((List)var2.get(var4)).iterator();

         while(var5.hasNext()) {
            JwsLogEvent var6 = (JwsLogEvent)var5.next();
            this.logger.log(var4, (LogEvent)var6);
         }
      }

      String[] var7 = this.webService.getHandlerChainDecl().getHandlerClassNames();
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         String var10 = var7[var9];
         if (!this.findHandlerClass(var10)) {
            this.logger.log(EventLevel.WARNING, (LogEvent)(new JwsLogEvent(var1, "type.handlerchain.classNotFound", new Object[]{var10})));
         }
      }

   }

   private boolean findHandlerClass(String var1) {
      boolean var2 = false;
      if (this.classLoader != null) {
         var2 = this.findClass(var1);
      }

      if (!var2 && this.sourcepath != null) {
         var2 = this.findSource(var1);
      }

      return var2;
   }

   private boolean findSource(String var1) {
      String var2 = var1.replace('.', '/') + ".java";
      String[] var3 = this.sourcepath;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         File var7 = new File(var6, var2);
         if (var7.exists()) {
            return true;
         }
      }

      return false;
   }

   private boolean findClass(String var1) {
      try {
         this.classLoader.loadClass(var1);
         return true;
      } catch (ClassNotFoundException var3) {
         return false;
      }
   }
}
