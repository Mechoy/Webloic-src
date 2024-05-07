package weblogic.wsee.jws;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.HandlerChain;
import javax.jws.soap.SOAPMessageHandlers;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.PortComponentBean;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.StringUtil;

public abstract class HandlerChainDecl<T> {
   private T[] handlerChains;
   protected Map<EventLevel, List<JwsLogEvent>> logEvents;

   public HandlerChainDecl(JClass var1, JClass var2, ClassLoader var3) {
      this.logEvents = new HashMap();
      this.handlerChains = this.buildHandlerChains(var2, (ClassLoader)null);
      if (this.handlerChains == null && var1 != null && var1 != var2) {
         this.handlerChains = this.buildHandlerChains(var1, var3);
      }

   }

   public HandlerChainDecl(JClass var1, JClass var2) {
      this(var1, var2, (ClassLoader)null);
   }

   private T[] buildHandlerChains(JClass var1, ClassLoader var2) {
      Object[] var3 = null;
      JAnnotation var4 = var1.getAnnotation(HandlerChain.class);
      if (var4 != null) {
         String var5 = JamUtil.getAnnotationStringValue(var4, "file");
         String var6 = JamUtil.getAnnotationStringValue(var4, "name");
         if (!StringUtil.isEmpty(var5)) {
            URL var7 = this.getURL(var5, var1, var2);
            if (var7 != null) {
               var3 = this.processHandlerChain(var1, var7, var6);
            }
         }
      } else {
         JAnnotation var8 = var1.getAnnotation(SOAPMessageHandlers.class.getName());
         if (var8 != null) {
            var3 = this.processSOAPMessageHandlers(var1, var8);
         }
      }

      return var3;
   }

   public boolean isRelativeUrl(String var1) {
      if (var1.indexOf(58) > 0) {
         return false;
      } else {
         return !var1.startsWith("/");
      }
   }

   private URL getURL(String var1, JClass var2, ClassLoader var3) {
      URL var4 = null;

      try {
         if (this.isRelativeUrl(var1)) {
            var4 = this.getAbsoluteURL(var2, var1, var3);
         } else {
            var4 = new URL(var1);
         }
      } catch (MalformedURLException var6) {
         this.addLogEvent(EventLevel.ERROR, new JwsLogEvent(var2, "type.handlerChain.malformedUrl", new Object[]{var4, var6.getMessage()}));
      }

      return var4;
   }

   private URL getAbsoluteURL(JClass var1, String var2, ClassLoader var3) throws MalformedURLException {
      URL var4 = null;
      if (var1.getArtifact() instanceof Class) {
         Class var5 = (Class)var1.getArtifact();
         var4 = var5.getResource(var2);
      } else {
         if (var3 != null) {
            String var8 = var1.getQualifiedName();

            try {
               Class var6 = var3.loadClass(var8);
               var4 = var6.getResource(var2);
            } catch (Exception var7) {
            }

            if (var4 != null) {
               return var4;
            }
         }

         File var9 = new File(var1.getSourcePosition().getSourceURI());
         File var10 = new File(var9.getParentFile(), var2.trim());
         var4 = var10.toURL();
      }

      return var4;
   }

   protected abstract T[] processSOAPMessageHandlers(JClass var1, JAnnotation var2);

   protected abstract T[] processHandlerChain(JClass var1, URL var2, String var3);

   protected T[] getHandlerChains() {
      return this.handlerChains;
   }

   public Map<EventLevel, List<JwsLogEvent>> getLogEvents() {
      return this.logEvents;
   }

   protected void addLogEvent(EventLevel var1, JwsLogEvent var2) {
      Object var3 = (List)this.logEvents.get(var1);
      if (var3 == null) {
         var3 = new ArrayList();
         this.logEvents.put(var1, var3);
      }

      ((List)var3).add(var2);
   }

   public void populatePort(PortComponentBean var1) throws HandlerException {
      this.populatePort(var1, (EnvEntryBean[])null);
   }

   public abstract void populatePort(PortComponentBean var1, EnvEntryBean[] var2) throws HandlerException;

   public abstract String[] getHandlerClassNames();
}
