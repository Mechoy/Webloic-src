package weblogic.messaging.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.Filter;
import weblogic.messaging.kernel.InvalidExpressionException;
import weblogic.messaging.kernel.Kernel;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Sink;
import weblogic.utils.concurrent.locks.NonRecursiveReadWriteLock;
import weblogic.utils.expressions.ExpressionEvaluationException;
import weblogic.utils.expressions.ExpressionParser;
import weblogic.utils.expressions.ExpressionParserException;
import weblogic.utils.expressions.VariableBinder;

public class SQLFilter implements Filter {
   protected final Map subscribers = new LinkedHashMap();
   protected final NonRecursiveReadWriteLock lock = new NonRecursiveReadWriteLock();
   protected Kernel kernel;
   protected VariableBinder variableBinder;

   public SQLFilter(Kernel var1, VariableBinder var2) {
      this.kernel = var1;
      this.variableBinder = var2;
   }

   public Expression createExpression(Object var1) throws KernelException {
      try {
         SQLExpression var2 = (SQLExpression)var1;
         return var2.isNull() ? null : new Exp(this.variableBinder, var2);
      } catch (ClassCastException var3) {
         throw new InvalidExpressionException("Invalid expression class: " + var1.getClass().getName());
      }
   }

   public void subscribe(Sink var1, Expression var2) {
      CompiledSQLExpression var3 = (CompiledSQLExpression)var2;
      this.lock.lockWrite();
      this.subscribers.put(var1, var3);
      this.lock.unlockWrite();
   }

   public void unsubscribe(Sink var1) {
      this.lock.lockWrite();
      this.subscribers.remove(var1);
      this.lock.unlockWrite();
   }

   public synchronized void resubscribe(Sink var1, Expression var2) {
      this.unsubscribe(var1);
      this.subscribe(var1, var2);
   }

   public Collection match(MessageElement var1) {
      ArrayList var2 = new ArrayList();
      this.lock.lockRead();

      try {
         Iterator var3 = this.subscribers.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            CompiledSQLExpression var5 = (CompiledSQLExpression)var4.getValue();
            if (var5 == null || var5.evaluate(var1)) {
               var2.add(var4.getKey());
            }
         }

         ArrayList var10 = var2;
         return var10;
      } finally {
         this.lock.unlockRead();
      }
   }

   public boolean match(MessageElement var1, Expression var2) {
      if (var2 == null) {
         return true;
      } else {
         CompiledSQLExpression var3 = (CompiledSQLExpression)var2;

         try {
            return var3.evaluate(var1);
         } catch (ClassCastException var5) {
            return false;
         }
      }
   }

   private final class Exp implements CompiledSQLExpression {
      private weblogic.utils.expressions.Expression wlExp;

      Exp(VariableBinder var2, SQLExpression var3) throws KernelException {
         String var4 = var3.getSelector();
         if (var4 != null) {
            try {
               ExpressionParser var5 = new ExpressionParser();
               this.wlExp = var5.parse(var4, var2);
            } catch (ExpressionParserException var6) {
               throw new InvalidExpressionException(var4);
            }
         }

      }

      public Filter getFilter() {
         return SQLFilter.this;
      }

      public boolean evaluate(MessageElement var1) {
         if (this.wlExp != null) {
            try {
               return this.wlExp.evaluate(var1);
            } catch (ExpressionEvaluationException var3) {
               return false;
            }
         } else {
            return true;
         }
      }

      public String getSelectorID() {
         return this.wlExp == null ? null : this.wlExp.getSelectorID();
      }
   }
}
