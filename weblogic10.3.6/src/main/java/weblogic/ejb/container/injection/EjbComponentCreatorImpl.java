package weblogic.ejb.container.injection;

import com.oracle.pitchfork.interfaces.EjbComponentCreatorBroker;
import com.oracle.pitchfork.interfaces.PitchforkRuntimeMode;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.EjbComponentCreator;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.management.DeploymentException;
import weblogic.utils.StackTraceUtils;

public class EjbComponentCreatorImpl implements EjbComponentCreator {
   private static final DebugLogger debugLogger;
   public static final String SPRING_EJB_JAR_XML_LOCATION = "/META-INF/spring-ejb-jar.xml";
   private PitchforkContext pitchforkContext;
   private EjbComponentCreatorBroker ejbComponentCreatorBroker;

   public EjbComponentCreatorImpl(PitchforkContext var1) {
      this.pitchforkContext = var1;
      this.ejbComponentCreatorBroker = var1.getPitchforkUtils().createEjbComponentCreatorBroker();
   }

   public void initialize(DeploymentInfo var1, ClassLoader var2) throws DeploymentException {
      PitchforkRuntimeMode var3 = this.pitchforkContext.getPitchforkRuntimeMode();
      EjbComponentContributor var4 = new EjbComponentContributor(var1, var2, this.pitchforkContext);

      try {
         this.ejbComponentCreatorBroker.initialize(var2, "/META-INF/spring-ejb-jar.xml", var4, var3);
      } catch (Throwable var6) {
         this.debug("Exception when creating spring bean factory " + StackTraceUtils.throwable2StackTrace(var6));
         throw new DeploymentException(var6);
      }
   }

   public Object getBean(String var1, Class var2, boolean var3) throws IllegalAccessException, InstantiationException {
      return this.ejbComponentCreatorBroker.getBean(var1, var2, var3);
   }

   public void invokePostConstruct(Object var1) {
      this.ejbComponentCreatorBroker.invokePostConstruct(var1);
   }

   public Object assembleEJB3Proxy(Object var1, String var2) {
      return this.ejbComponentCreatorBroker.assembleEJB3Proxy(var1, var2);
   }

   protected void debug(String var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("[EjbComponentCreatorImpl] " + var1);
      }

   }

   static {
      debugLogger = EJBDebugService.deploymentLogger;
   }
}
