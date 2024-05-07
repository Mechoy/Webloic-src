package weblogic.wsee.tools.jws.validation.jaxws;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.Transactional.TransactionFlowType;
import weblogic.wsee.wstx.wsat.validation.TXAttributesValidator;

public class WSATValidator {
   JAnnotation firstWsatAnno;
   JAnnotation classLevelEJBTxAnno;
   JAnnotation classLevelWsatAnno;
   JAnnotatedElement firstTransactionalEle;
   boolean isStatelessEJB;

   public void visitImpl(JClass var1, Logger var2) {
      this.isStatelessEJB = var1.getAnnotation(Stateless.class) != null;
      this.classLevelEJBTxAnno = var1.getAnnotation(TransactionAttribute.class);
      this.classLevelWsatAnno = var1.getAnnotation(Transactional.class);
      this.firstWsatAnno = this.classLevelWsatAnno;
      this.firstTransactionalEle = var1;
   }

   public void visitProvider(JClass var1, Logger var2) {
      this.isStatelessEJB = var1.getAnnotation(Stateless.class) != null;
      this.classLevelEJBTxAnno = var1.getAnnotation(TransactionAttribute.class);
      this.classLevelWsatAnno = var1.getAnnotation(Transactional.class);
      this.firstWsatAnno = this.classLevelWsatAnno;
      this.firstTransactionalEle = var1;
      JMethod[] var3 = var1.getMethods();
      JMethod[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         JMethod var7 = var4[var6];
         String var8 = var7.getQualifiedName();
         if ("public javax.xml.transform.Source invoke(javax.xml.transform.Source)".equals(var8) || "public javax.xml.soap.SOAPMessage invoke(javax.xml.soap.SOAPMessage)".equals(var8)) {
            this.checkEJBTransactionaAttributes(var7, var2);
         }
      }

   }

   public void visitImpl(WebMethodDecl var1, Logger var2) {
      this.checkEJBTransactionaAttributes(var1.getJMethod(), var2);
      this.checkWSATAnnotations(var1, var2);
   }

   private static TransactionAttributeType getEjbTransactionaAttributeValue(JAnnotation var0) {
      return var0 == null ? TransactionAttributeType.REQUIRED : TransactionAttributeType.valueOf(var0.getValue("value").asString());
   }

   private static Transactional.TransactionFlowType getWSATValue(JAnnotation var0) {
      return var0 != null && var0.getValue("enabled").asBoolean() ? TransactionFlowType.valueOf(var0.getValue("value").asString()) : TransactionFlowType.NEVER;
   }

   private void checkEJBTransactionaAttributes(JAnnotatedElement var1, Logger var2) {
      if (this.isStatelessEJB) {
         JAnnotation var3 = var1.getAnnotation(Transactional.class);
         JAnnotation var4 = var1.getAnnotation(TransactionAttribute.class);
         if (var4 == null) {
            var4 = this.classLevelEJBTxAnno;
         }

         if (var3 == null) {
            var3 = this.classLevelWsatAnno;
         }

         TransactionAttributeType var5 = getEjbTransactionaAttributeValue(var4);
         Transactional.TransactionFlowType var6 = getWSATValue(var3);
         if (!TXAttributesValidator.isValid(var5, var6)) {
            var2.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var1, "method.wsat.ejbtransaction.invalidCombination", new Object[]{var6, var5, var1})));
         }

      }
   }

   private void checkWSATAnnotations(WebMethodDecl var1, Logger var2) {
      JMethod var3 = var1.getJMethod();
      JAnnotation var4 = var3.getAnnotation(Transactional.class);
      Transactional.TransactionFlowType var5 = getWSATValue(var4);
      boolean var6 = TransactionFlowType.NEVER != var5;
      if (var6) {
         if (var1.isOneway()) {
            var2.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3, "method.oneway.wsatnotAllowed", new Object[]{var1.getName()})));
         } else {
            String var7 = var4.getValue("version").asString();
            if (this.classLevelWsatAnno != null) {
               if (!this.classLevelWsatAnno.getValue("version").asString().equals(var7) && !"DEFAULT".equals(var7)) {
                  var2.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3, "method.wsat.differentVersion", new Object[]{var1.getName(), this.firstTransactionalEle})));
               }
            } else if (this.firstWsatAnno == null) {
               this.firstWsatAnno = var4;
               this.firstTransactionalEle = var3;
            } else if (!this.firstWsatAnno.getValue("version").asString().equals(var7)) {
               var2.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3, "method.wsat.differentVersion", new Object[]{var1.getName(), this.firstTransactionalEle})));
            }

         }
      }
   }
}
