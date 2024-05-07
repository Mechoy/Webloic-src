package weblogic.deployment;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.transaction.Transaction;
import weblogic.transaction.TransactionHelper;

public final class QueryProxyImpl implements Query {
   private final TransactionalEntityManagerProxyImpl emProxy;
   private final Method createQueryMethod;
   private final Object[] createQueryArgs;
   private final Map hints = new HashMap();
   private final Map params = new HashMap();
   private int firstResult = -1;
   private int maxResults = -1;
   private FlushModeType flushMode = null;

   public QueryProxyImpl(TransactionalEntityManagerProxyImpl var1, Method var2, Object[] var3) {
      this.emProxy = var1;
      this.createQueryMethod = var2;
      this.createQueryArgs = var3;
   }

   private Query createQuery(EntityManager var1) {
      Query var2 = null;
      String var3 = this.createQueryMethod.getName();
      if (var3.equals("createQuery")) {
         var2 = var1.createQuery((String)this.createQueryArgs[0]);
      } else if (var3.equals("createNamedQuery")) {
         var2 = var1.createNamedQuery((String)this.createQueryArgs[0]);
      } else if (this.createQueryArgs.length == 1) {
         var2 = var1.createNativeQuery((String)this.createQueryArgs[0]);
      } else if (this.createQueryArgs[1].getClass() == String.class) {
         var2 = var1.createNativeQuery((String)this.createQueryArgs[0], (String)this.createQueryArgs[1]);
      } else {
         var2 = var1.createNativeQuery((String)this.createQueryArgs[0], (Class)this.createQueryArgs[1]);
      }

      if (this.firstResult > 0) {
         var2.setFirstResult(this.firstResult);
      }

      if (this.maxResults > 0) {
         var2.setMaxResults(this.maxResults);
      }

      if (this.flushMode != null) {
         var2.setFlushMode(this.flushMode);
      }

      Iterator var4 = this.hints.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var2.setHint(var5, this.hints.get(var5));
      }

      var4 = this.params.keySet().iterator();

      while(var4.hasNext()) {
         Object var9 = var4.next();
         Object var6 = this.params.get(var9);
         TemporalType var7 = null;
         if (var6 instanceof ParameterValue) {
            var7 = ((ParameterValue)var6).temporalType;
            var6 = ((ParameterValue)var6).value;
         }

         if (var9 instanceof IndexKey) {
            int var8 = ((IndexKey)var9).index;
            if (var7 == null) {
               var2.setParameter(var8, var6);
            } else if (var6 instanceof Date) {
               var2.setParameter(var8, (Date)var6, var7);
            } else {
               var2.setParameter(var8, (Calendar)var6, var7);
            }
         } else if (var7 == null) {
            var2.setParameter((String)var9, var6);
         } else if (var6 instanceof Date) {
            var2.setParameter((String)var9, (Date)var6, var7);
         } else {
            var2.setParameter((String)var9, (Calendar)var6, var7);
         }
      }

      return var2;
   }

   public int executeUpdate() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransaction();
      EntityManager var2 = (EntityManager)this.emProxy.getPersistenceContext(var1);

      int var3;
      try {
         var3 = this.createQuery(var2).executeUpdate();
      } finally {
         if (var1 == null && var2 != null) {
            var2.close();
         }

      }

      return var3;
   }

   public List getResultList() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransaction();
      EntityManager var2 = (EntityManager)this.emProxy.getPersistenceContext(var1);

      List var3;
      try {
         var3 = this.createQuery(var2).getResultList();
      } finally {
         if (var1 == null && var2 != null) {
            var2.close();
         }

      }

      return var3;
   }

   public Object getSingleResult() {
      Transaction var1 = TransactionHelper.getTransactionHelper().getTransaction();
      EntityManager var2 = (EntityManager)this.emProxy.getPersistenceContext(var1);

      Object var3;
      try {
         var3 = this.createQuery(var2).getSingleResult();
      } finally {
         if (var1 == null && var2 != null) {
            var2.close();
         }

      }

      return var3;
   }

   public Query setFirstResult(int var1) {
      this.validatePositiveArgument(var1);
      this.firstResult = var1;
      return this;
   }

   public Query setFlushMode(FlushModeType var1) {
      this.flushMode = var1;
      return this;
   }

   public Query setHint(String var1, Object var2) {
      this.hints.put(var1, var2);
      return this;
   }

   public Query setMaxResults(int var1) {
      this.validatePositiveArgument(var1);
      this.maxResults = var1;
      return this;
   }

   public Query setParameter(int var1, Calendar var2, TemporalType var3) {
      this.params.put(new IndexKey(var1), new ParameterValue(var2, var3));
      return this;
   }

   public Query setParameter(int var1, Date var2, TemporalType var3) {
      this.params.put(new IndexKey(var1), new ParameterValue(var2, var3));
      return this;
   }

   public Query setParameter(int var1, Object var2) {
      this.params.put(new IndexKey(var1), var2);
      return this;
   }

   public Query setParameter(String var1, Calendar var2, TemporalType var3) {
      this.params.put(var1, new ParameterValue(var2, var3));
      return this;
   }

   public Query setParameter(String var1, Date var2, TemporalType var3) {
      this.params.put(var1, new ParameterValue(var2, var3));
      return this;
   }

   public Query setParameter(String var1, Object var2) {
      this.params.put(var1, var2);
      return this;
   }

   private void validatePositiveArgument(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Argument is negative: " + var1);
      }
   }

   private class IndexKey {
      public int index;

      public IndexKey(int var2) {
         this.index = var2;
      }

      public boolean equals(Object var1) {
         if (var1 instanceof IndexKey) {
            return this.index == ((IndexKey)var1).index;
         } else {
            return false;
         }
      }
   }

   private class ParameterValue {
      public Object value;
      public TemporalType temporalType;

      public ParameterValue(Object var2, TemporalType var3) {
         this.value = var2;
         this.temporalType = var3;
      }
   }
}
