package weblogic.jms.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.messaging.common.CompiledSQLExpression;
import weblogic.messaging.common.SQLFilter;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.Filter;
import weblogic.messaging.kernel.InvalidExpressionException;
import weblogic.messaging.kernel.Kernel;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Sink;
import weblogic.utils.expressions.ExpressionEvaluationException;
import weblogic.utils.expressions.ExpressionParser;
import weblogic.utils.expressions.ExpressionParserException;
import weblogic.utils.expressions.VariableBinder;

public class JMSSQLFilter extends SQLFilter {
   private final Map indexedSubscribers = new HashMap();
   private final Map filteredForwardedIndexedSubs = new HashMap();
   private final Set<Sink> filteredForwardedSubs = new HashSet();
   private final Map<Sink, Expression> filteredForwardedAndMoreSubs = new HashMap();

   public JMSSQLFilter(Kernel var1) {
      super(var1, JMSVariableBinder.THE_ONE);
   }

   public Expression createExpression(Object var1) throws KernelException {
      try {
         JMSSQLExpression var2 = (JMSSQLExpression)var1;
         return var2.isNull() ? null : new Exp(this.variableBinder, var2);
      } catch (ClassCastException var3) {
         throw new InvalidExpressionException("Wrong class: " + var1.getClass().getName());
      }
   }

   public void subscribe(Sink var1, Expression var2) {
      this.lock.lockWrite();

      try {
         if (var2 == null) {
            this.subscribers.put(var1, (Object)null);
         } else {
            Exp var3 = (Exp)var2;
            String var4 = var3.getSelectorID();
            if (var3.isJMSWLDDForwarded()) {
               this.filteredForwardedSubs.add(var1);
            } else if (var3.isJMSWLDDForwardedAndMore()) {
               String var5 = var3.getfilteredForwardedSelectorID();
               if (var5 != null) {
                  this.addIndexedSubscriber(var1, var5, true);
               } else {
                  this.filteredForwardedAndMoreSubs.put(var1, var2);
               }
            } else if (var4 != null) {
               this.addIndexedSubscriber(var1, var4, false);
            } else {
               this.subscribers.put(var1, var3);
            }
         }
      } finally {
         this.lock.unlockWrite();
      }

   }

   private void addIndexedSubscriber(Sink var1, String var2, boolean var3) {
      assert this.lock.isLockedForWrite();

      Map var4 = var3 ? this.filteredForwardedIndexedSubs : this.indexedSubscribers;
      Object var5 = var4.get(var2);
      if (var5 == null) {
         var4.put(var2, var1);
      } else if (var5 instanceof Set) {
         ((Set)var5).add(var1);
      } else {
         HashSet var6 = new HashSet();
         var6.add(var5);
         var6.add(var1);
         var4.put(var2, var6);
      }

   }

   public void unsubscribe(Sink var1) {
      if (var1 != null) {
         this.lock.lockWrite();

         try {
            if (this.subscribers.remove(var1) == null && !this.filteredForwardedSubs.remove(var1) && this.filteredForwardedAndMoreSubs.remove(var1) == null) {
               this.removeIndexedSubscriber(var1);
            }
         } finally {
            this.lock.unlockWrite();
         }

      }
   }

   private void removeIndexedSubscriber(Sink var1) {
      assert this.lock.isLockedForWrite();

      Iterator var2 = this.indexedSubscribers.values().iterator();

      Object var3;
      do {
         if (!var2.hasNext()) {
            Iterator var6 = this.filteredForwardedIndexedSubs.values().iterator();

            Object var7;
            do {
               if (!var6.hasNext()) {
                  return;
               }

               var7 = var6.next();
               if (var7 instanceof Set && ((Set)var7).contains(var1)) {
                  Set var5 = (Set)var7;
                  var5.remove(var1);
                  if (var5.isEmpty()) {
                     var6.remove();
                  }

                  return;
               }
            } while(!var7.equals(var1));

            var6.remove();
            return;
         }

         var3 = var2.next();
         if (var3 instanceof Set && ((Set)var3).contains(var1)) {
            Set var4 = (Set)var3;
            var4.remove(var1);
            if (var4.isEmpty()) {
               var2.remove();
            }

            return;
         }
      } while(!var3.equals(var1));

      var2.remove();
   }

   public Collection match(MessageElement var1) {
      ArrayList var2 = new ArrayList();

      MessageImpl var3;
      try {
         var3 = (MessageImpl)var1.getMessage();
      } catch (ClassCastException var13) {
         return null;
      }

      this.lock.lockRead();

      try {
         if (!this.filteredForwardedSubs.isEmpty() && !var3.getDDForwarded()) {
            var2.addAll(this.filteredForwardedSubs);
         }

         Iterator var4;
         Map.Entry var5;
         if (!this.filteredForwardedAndMoreSubs.isEmpty() && !var3.getDDForwarded()) {
            var4 = this.filteredForwardedAndMoreSubs.entrySet().iterator();

            while(var4.hasNext()) {
               var5 = (Map.Entry)var4.next();
               if (((CompiledSQLExpression)var5.getValue()).evaluate(var1)) {
                  var2.add(var5.getKey());
               }
            }
         }

         Object var17;
         if (!this.filteredForwardedIndexedSubs.isEmpty() && !var3.getDDForwarded()) {
            try {
               var4 = var3.getPropertyNameCollection().iterator();

               while(var4.hasNext()) {
                  var17 = this.filteredForwardedIndexedSubs.get(var4.next());
                  if (var17 != null) {
                     if (var17 instanceof Set) {
                        var2.addAll((Set)var17);
                     } else {
                        var2.add(var17);
                     }
                  }
               }
            } catch (javax.jms.JMSException var14) {
               var5 = null;
               return var5;
            }
         }

         if (!this.indexedSubscribers.isEmpty()) {
            try {
               var4 = var3.getPropertyNameCollection().iterator();

               while(var4.hasNext()) {
                  var17 = this.indexedSubscribers.get(var4.next());
                  if (var17 != null) {
                     if (var17 instanceof Set) {
                        var2.addAll((Set)var17);
                     } else {
                        var2.add(var17);
                     }
                  }
               }
            } catch (javax.jms.JMSException var15) {
               var5 = null;
               return var5;
            }
         }

         var4 = this.subscribers.entrySet().iterator();

         while(var4.hasNext()) {
            var5 = (Map.Entry)var4.next();
            CompiledSQLExpression var6 = (CompiledSQLExpression)var5.getValue();
            if (var6 == null || var6.evaluate(var1)) {
               var2.add(var5.getKey());
            }
         }

         ArrayList var18 = var2;
         return var18;
      } finally {
         this.lock.unlockRead();
      }
   }

   private final class Exp implements CompiledSQLExpression {
      private weblogic.utils.expressions.Expression wlExp;
      private boolean noLocal;
      private boolean notForwarded;
      private JMSID connectionId;
      private String clientId;
      private int clientIdPolicy = 0;

      Exp(VariableBinder var2, JMSSQLExpression var3) throws KernelException {
         this.noLocal = var3.isNoLocal();
         this.notForwarded = var3.isNotForwarded();
         this.connectionId = var3.getConnectionId();
         this.clientId = var3.getClientId();
         this.clientIdPolicy = var3.getClientIdPolicy();
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
         return JMSSQLFilter.this;
      }

      public boolean evaluate(MessageElement var1) {
         MessageImpl var2 = (MessageImpl)var1.getMessage();
         if (this.noLocal) {
            boolean var3 = var2.getConnectionId() != null && this.connectionId != null && var2.getConnectionId().equals(this.connectionId);
            boolean var4 = this.clientIdPolicy != 0 && var2.getClientId() != null && this.clientId != null && var2.getClientId().equals(this.clientId);
            if (var3 || var4) {
               return false;
            }
         }

         if (this.notForwarded && var2.getDDForwarded()) {
            return false;
         } else if (this.wlExp != null) {
            try {
               return this.wlExp.evaluate(var1);
            } catch (ExpressionEvaluationException var5) {
               return false;
            }
         } else {
            return true;
         }
      }

      String getSelectorID() {
         return this.wlExp == null ? null : this.wlExp.getSelectorID();
      }

      String getfilteredForwardedSelectorID() {
         return this.wlExp == null ? null : this.wlExp.getfilteredForwardedSelectorID();
      }

      boolean isJMSWLDDForwarded() {
         if (this.wlExp == null) {
            return false;
         } else {
            String var1 = this.wlExp.getStandardForwarderID();
            return var1 != null && var1.equals("JMS_WL_DDForwarded");
         }
      }

      boolean isJMSWLDDForwardedAndMore() {
         if (this.wlExp == null) {
            return false;
         } else {
            String var1 = this.wlExp.getComplexForwarderID();
            return var1 != null && var1.equals("JMS_WL_DDForwarded");
         }
      }
   }
}
