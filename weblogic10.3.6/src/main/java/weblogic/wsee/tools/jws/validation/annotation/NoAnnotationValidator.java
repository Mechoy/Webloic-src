package weblogic.wsee.tools.jws.validation.annotation;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JElement;
import com.bea.util.jam.JField;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.JParameter;
import com.bea.util.jam.visitor.JVisitor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;

public class NoAnnotationValidator extends JVisitor {
   private final Logger logger;
   private List<MatchingRuleValidator> matchingRules;
   private Set<String> excludeClasses;
   private final String defaultKey;

   public NoAnnotationValidator(Logger var1) {
      this(var1, "annotation.notAllowed");
   }

   public NoAnnotationValidator(Logger var1, String var2) {
      this.matchingRules = new ArrayList();
      this.excludeClasses = new HashSet();

      assert var1 != null : "Logger not specified";

      assert var2 != null : "Error defaultKey not specified";

      this.logger = var1;
      this.defaultKey = var2;
   }

   public boolean isEmpty() {
      return this.matchingRules.isEmpty();
   }

   public void addMatchingRule(MatchingRule var1) {
      this.addMatchingRule(this.defaultKey, var1);
   }

   public void addMatchingRule(String var1, MatchingRule var2) {
      assert var2 != null : "No rule specified";

      this.matchingRules.add(new MatchingRuleValidator(var1, var2));
   }

   public void addExcludeClass(JClass var1) {
      assert var1 != null : "No exclude class specified";

      for(JClass var2 = var1; var2 != null; var2 = var2.getSuperclass()) {
         this.excludeClasses.add(var2.getQualifiedName());
         JClass[] var3 = var1.getInterfaces();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            JClass var6 = var3[var5];
            this.excludeClasses.add(var6.getQualifiedName());
         }
      }

   }

   public void visit(JClass var1) {
      if (!this.isExcluded(var1)) {
         this.findAnnotations(var1);
      }

   }

   public void visit(JMethod var1) {
      if (!this.isExcluded(var1.getContainingClass())) {
         this.findAnnotations(var1);
      }

   }

   public void visit(JParameter var1) {
      JElement var2 = var1.getParent();
      if (var2 instanceof JMethod) {
         JMethod var3 = (JMethod)var2;
         if (!this.isExcluded(var3.getContainingClass())) {
            this.findAnnotations(var1);
         }
      }

   }

   public void visit(JField var1) {
      if (!this.isExcluded(var1.getContainingClass())) {
         this.findAnnotations(var1);
      }

   }

   private boolean isExcluded(JClass var1) {
      return this.excludeClasses.contains(var1.getQualifiedName());
   }

   private void findAnnotations(JAnnotatedElement var1) {
      JAnnotation[] var2 = var1.getAnnotations();
      JAnnotation[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         JAnnotation var6 = var3[var5];
         Iterator var7 = this.matchingRules.iterator();

         while(var7.hasNext()) {
            MatchingRuleValidator var8 = (MatchingRuleValidator)var7.next();
            var8.validate(var1, var6);
         }
      }

   }

   private class MatchingRuleValidator {
      private MatchingRule rule;
      private String key;

      private MatchingRuleValidator(String var2, MatchingRule var3) {
         this.key = var2;
         this.rule = var3;
      }

      private void validate(JAnnotatedElement var1, JAnnotation var2) {
         if (this.rule.isMatch(var2)) {
            JAnnotatedElement var3 = this.getLogElement(var1);
            NoAnnotationValidator.this.logger.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(var3, this.key, new Object[]{var2.getQualifiedName(), var3.getQualifiedName()})));
         }

      }

      private JAnnotatedElement getLogElement(JAnnotatedElement var1) {
         return var1 instanceof JParameter ? (JAnnotatedElement)((JParameter)var1).getParent() : var1;
      }

      // $FF: synthetic method
      MatchingRuleValidator(String var2, MatchingRule var3, Object var4) {
         this(var2, var3);
      }
   }
}
