package weblogic.wsee.wstx.wsat.config;

import java.lang.reflect.Method;
import weblogic.wsee.jws.JWSVisitor;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.TransactionalFeature;

public class WSATFeatureVisitor implements JWSVisitor {
   private TransactionalFeature atFeature;
   private Transactional portAnn;

   public void visitClass(JWSVisitor.JWSClass var1) {
      this.portAnn = (Transactional)var1.getServiceImpl().getAnnotation(Transactional.class);
      if (this.portAnn != null) {
         this.atFeature = new TransactionalFeature();
         this.atFeature.setEnabled(this.portAnn.enabled());
         this.atFeature.setVersion(this.portAnn.version());
         this.atFeature.setFlowType(this.portAnn.value());
      }

   }

   public void visitMethod(JWSVisitor.WsMethod var1) {
      Method var2 = var1.getImplMethod();
      Transactional var3 = (Transactional)var2.getAnnotation(Transactional.class);
      if (var3 == null) {
         var3 = this.portAnn;
      }

      if (var3 != null) {
         if (this.atFeature == null) {
            this.atFeature = new TransactionalFeature();
            this.atFeature.setExplicitMode(true);
         }

         String var4 = var1.getOperationName();
         this.atFeature.setEnabled(var4, var3.enabled());
         if (this.atFeature.isExplicitMode()) {
            this.atFeature.setVersion(this.portAnn.version());
         }

         this.atFeature.setFlowType(var4, var3.value());
      }

   }

   public TransactionalFeature getFeature() {
      return this.atFeature;
   }
}
