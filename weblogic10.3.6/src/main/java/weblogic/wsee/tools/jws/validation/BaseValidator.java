package weblogic.wsee.tools.jws.validation;

import com.bea.util.jam.JClass;
import com.bea.util.jam.visitor.JVisitor;
import com.bea.util.jam.visitor.TraversingJVisitor;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.validation.annotation.NoAnnotationValidator;
import weblogic.wsee.tools.logging.CompositeLogger;
import weblogic.wsee.tools.logging.CountLogger;
import weblogic.wsee.tools.logging.Logger;

public abstract class BaseValidator<T extends WebServiceDecl> extends JVisitor implements Validator {
   protected final T webService;
   private final JwsBuildContext ctx;
   private CountLogger countLogger = new CountLogger();
   private CompositeLogger compositeLogger = new CompositeLogger();

   protected BaseValidator(JwsBuildContext var1, T var2) {
      this.ctx = var1;
      this.webService = var2;
      this.compositeLogger.addLogger(var1.getLogger());
      this.compositeLogger.addLogger(this.countLogger);
   }

   protected void checkHandlerChain(JClass var1) {
      HandlerChainValidator var2 = new HandlerChainValidator(this.webService, this.compositeLogger, this.ctx.getClassLoader(), this.ctx.getSourcepath());
      var2.validate(var1);
   }

   public boolean validate() {
      TraversingJVisitor var1 = new TraversingJVisitor(this);
      this.getVisitee().accept(var1);
      NoAnnotationValidator var2 = this.getNoAnnotationValidator(this.compositeLogger);
      if (var2 != null && !var2.isEmpty()) {
         var1 = new TraversingJVisitor(var2);
         this.getVisitee().accept(var1);
      }

      return this.countLogger.getErrorCount() == 0;
   }

   protected Logger getLogger() {
      return this.compositeLogger;
   }

   protected ClassLoader getClassLoader() {
      return this.ctx.getClassLoader();
   }

   protected abstract NoAnnotationValidator getNoAnnotationValidator(Logger var1);

   protected abstract JClass getVisitee();
}
