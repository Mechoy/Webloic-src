package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class SelfTuningMBeanImpl extends ConfigurationMBeanImpl implements SelfTuningMBean, Serializable {
   private CapacityMBean[] _Capacities;
   private ContextRequestClassMBean[] _ContextRequestClasses;
   private FairShareRequestClassMBean[] _FairShareRequestClasses;
   private MaxThreadsConstraintMBean[] _MaxThreadsConstraints;
   private MinThreadsConstraintMBean[] _MinThreadsConstraints;
   private ResponseTimeRequestClassMBean[] _ResponseTimeRequestClasses;
   private WorkManagerMBean[] _WorkManagers;
   private static SchemaHelper2 _schemaHelper;

   public SelfTuningMBeanImpl() {
      this._initializeProperty(-1);
   }

   public SelfTuningMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void addFairShareRequestClass(FairShareRequestClassMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         FairShareRequestClassMBean[] var2;
         if (this._isSet(7)) {
            var2 = (FairShareRequestClassMBean[])((FairShareRequestClassMBean[])this._getHelper()._extendArray(this.getFairShareRequestClasses(), FairShareRequestClassMBean.class, var1));
         } else {
            var2 = new FairShareRequestClassMBean[]{var1};
         }

         try {
            this.setFairShareRequestClasses(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public FairShareRequestClassMBean[] getFairShareRequestClasses() {
      return this._FairShareRequestClasses;
   }

   public boolean isFairShareRequestClassesSet() {
      return this._isSet(7);
   }

   public void removeFairShareRequestClass(FairShareRequestClassMBean var1) {
      this.destroyFairShareRequestClass(var1);
   }

   public void setFairShareRequestClasses(FairShareRequestClassMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new FairShareRequestClassMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 7)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      FairShareRequestClassMBean[] var5 = this._FairShareRequestClasses;
      this._FairShareRequestClasses = (FairShareRequestClassMBean[])var4;
      this._postSet(7, var5, var4);
   }

   public FairShareRequestClassMBean createFairShareRequestClass(String var1) {
      FairShareRequestClassMBeanImpl var2 = new FairShareRequestClassMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addFairShareRequestClass(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyFairShareRequestClass(FairShareRequestClassMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 7);
         FairShareRequestClassMBean[] var2 = this.getFairShareRequestClasses();
         FairShareRequestClassMBean[] var3 = (FairShareRequestClassMBean[])((FairShareRequestClassMBean[])this._getHelper()._removeElement(var2, FairShareRequestClassMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setFairShareRequestClasses(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public FairShareRequestClassMBean lookupFairShareRequestClass(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._FairShareRequestClasses).iterator();

      FairShareRequestClassMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (FairShareRequestClassMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addResponseTimeRequestClass(ResponseTimeRequestClassMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 8)) {
         ResponseTimeRequestClassMBean[] var2;
         if (this._isSet(8)) {
            var2 = (ResponseTimeRequestClassMBean[])((ResponseTimeRequestClassMBean[])this._getHelper()._extendArray(this.getResponseTimeRequestClasses(), ResponseTimeRequestClassMBean.class, var1));
         } else {
            var2 = new ResponseTimeRequestClassMBean[]{var1};
         }

         try {
            this.setResponseTimeRequestClasses(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ResponseTimeRequestClassMBean[] getResponseTimeRequestClasses() {
      return this._ResponseTimeRequestClasses;
   }

   public boolean isResponseTimeRequestClassesSet() {
      return this._isSet(8);
   }

   public void removeResponseTimeRequestClass(ResponseTimeRequestClassMBean var1) {
      this.destroyResponseTimeRequestClass(var1);
   }

   public void setResponseTimeRequestClasses(ResponseTimeRequestClassMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ResponseTimeRequestClassMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 8)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      ResponseTimeRequestClassMBean[] var5 = this._ResponseTimeRequestClasses;
      this._ResponseTimeRequestClasses = (ResponseTimeRequestClassMBean[])var4;
      this._postSet(8, var5, var4);
   }

   public ResponseTimeRequestClassMBean createResponseTimeRequestClass(String var1) {
      ResponseTimeRequestClassMBeanImpl var2 = new ResponseTimeRequestClassMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addResponseTimeRequestClass(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyResponseTimeRequestClass(ResponseTimeRequestClassMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 8);
         ResponseTimeRequestClassMBean[] var2 = this.getResponseTimeRequestClasses();
         ResponseTimeRequestClassMBean[] var3 = (ResponseTimeRequestClassMBean[])((ResponseTimeRequestClassMBean[])this._getHelper()._removeElement(var2, ResponseTimeRequestClassMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setResponseTimeRequestClasses(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public ResponseTimeRequestClassMBean lookupResponseTimeRequestClass(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ResponseTimeRequestClasses).iterator();

      ResponseTimeRequestClassMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ResponseTimeRequestClassMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addContextRequestClass(ContextRequestClassMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 9)) {
         ContextRequestClassMBean[] var2;
         if (this._isSet(9)) {
            var2 = (ContextRequestClassMBean[])((ContextRequestClassMBean[])this._getHelper()._extendArray(this.getContextRequestClasses(), ContextRequestClassMBean.class, var1));
         } else {
            var2 = new ContextRequestClassMBean[]{var1};
         }

         try {
            this.setContextRequestClasses(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public ContextRequestClassMBean[] getContextRequestClasses() {
      return this._ContextRequestClasses;
   }

   public boolean isContextRequestClassesSet() {
      return this._isSet(9);
   }

   public void removeContextRequestClass(ContextRequestClassMBean var1) {
      this.destroyContextRequestClass(var1);
   }

   public void setContextRequestClasses(ContextRequestClassMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new ContextRequestClassMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 9)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      ContextRequestClassMBean[] var5 = this._ContextRequestClasses;
      this._ContextRequestClasses = (ContextRequestClassMBean[])var4;
      this._postSet(9, var5, var4);
   }

   public ContextRequestClassMBean createContextRequestClass(String var1) {
      ContextRequestClassMBeanImpl var2 = new ContextRequestClassMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addContextRequestClass(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyContextRequestClass(ContextRequestClassMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 9);
         ContextRequestClassMBean[] var2 = this.getContextRequestClasses();
         ContextRequestClassMBean[] var3 = (ContextRequestClassMBean[])((ContextRequestClassMBean[])this._getHelper()._removeElement(var2, ContextRequestClassMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setContextRequestClasses(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public ContextRequestClassMBean lookupContextRequestClass(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._ContextRequestClasses).iterator();

      ContextRequestClassMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ContextRequestClassMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addMinThreadsConstraint(MinThreadsConstraintMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 10)) {
         MinThreadsConstraintMBean[] var2;
         if (this._isSet(10)) {
            var2 = (MinThreadsConstraintMBean[])((MinThreadsConstraintMBean[])this._getHelper()._extendArray(this.getMinThreadsConstraints(), MinThreadsConstraintMBean.class, var1));
         } else {
            var2 = new MinThreadsConstraintMBean[]{var1};
         }

         try {
            this.setMinThreadsConstraints(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public MinThreadsConstraintMBean[] getMinThreadsConstraints() {
      return this._MinThreadsConstraints;
   }

   public boolean isMinThreadsConstraintsSet() {
      return this._isSet(10);
   }

   public void removeMinThreadsConstraint(MinThreadsConstraintMBean var1) {
      this.destroyMinThreadsConstraint(var1);
   }

   public void setMinThreadsConstraints(MinThreadsConstraintMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new MinThreadsConstraintMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 10)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      MinThreadsConstraintMBean[] var5 = this._MinThreadsConstraints;
      this._MinThreadsConstraints = (MinThreadsConstraintMBean[])var4;
      this._postSet(10, var5, var4);
   }

   public MinThreadsConstraintMBean createMinThreadsConstraint(String var1) {
      MinThreadsConstraintMBeanImpl var2 = new MinThreadsConstraintMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addMinThreadsConstraint(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyMinThreadsConstraint(MinThreadsConstraintMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 10);
         MinThreadsConstraintMBean[] var2 = this.getMinThreadsConstraints();
         MinThreadsConstraintMBean[] var3 = (MinThreadsConstraintMBean[])((MinThreadsConstraintMBean[])this._getHelper()._removeElement(var2, MinThreadsConstraintMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setMinThreadsConstraints(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public MinThreadsConstraintMBean lookupMinThreadsConstraint(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._MinThreadsConstraints).iterator();

      MinThreadsConstraintMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MinThreadsConstraintMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addMaxThreadsConstraint(MaxThreadsConstraintMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 11)) {
         MaxThreadsConstraintMBean[] var2;
         if (this._isSet(11)) {
            var2 = (MaxThreadsConstraintMBean[])((MaxThreadsConstraintMBean[])this._getHelper()._extendArray(this.getMaxThreadsConstraints(), MaxThreadsConstraintMBean.class, var1));
         } else {
            var2 = new MaxThreadsConstraintMBean[]{var1};
         }

         try {
            this.setMaxThreadsConstraints(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public MaxThreadsConstraintMBean[] getMaxThreadsConstraints() {
      return this._MaxThreadsConstraints;
   }

   public boolean isMaxThreadsConstraintsSet() {
      return this._isSet(11);
   }

   public void removeMaxThreadsConstraint(MaxThreadsConstraintMBean var1) {
      this.destroyMaxThreadsConstraint(var1);
   }

   public void setMaxThreadsConstraints(MaxThreadsConstraintMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new MaxThreadsConstraintMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 11)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      MaxThreadsConstraintMBean[] var5 = this._MaxThreadsConstraints;
      this._MaxThreadsConstraints = (MaxThreadsConstraintMBean[])var4;
      this._postSet(11, var5, var4);
   }

   public MaxThreadsConstraintMBean createMaxThreadsConstraint(String var1) {
      MaxThreadsConstraintMBeanImpl var2 = new MaxThreadsConstraintMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addMaxThreadsConstraint(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyMaxThreadsConstraint(MaxThreadsConstraintMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 11);
         MaxThreadsConstraintMBean[] var2 = this.getMaxThreadsConstraints();
         MaxThreadsConstraintMBean[] var3 = (MaxThreadsConstraintMBean[])((MaxThreadsConstraintMBean[])this._getHelper()._removeElement(var2, MaxThreadsConstraintMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setMaxThreadsConstraints(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public MaxThreadsConstraintMBean lookupMaxThreadsConstraint(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._MaxThreadsConstraints).iterator();

      MaxThreadsConstraintMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MaxThreadsConstraintMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addCapacity(CapacityMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 12)) {
         CapacityMBean[] var2;
         if (this._isSet(12)) {
            var2 = (CapacityMBean[])((CapacityMBean[])this._getHelper()._extendArray(this.getCapacities(), CapacityMBean.class, var1));
         } else {
            var2 = new CapacityMBean[]{var1};
         }

         try {
            this.setCapacities(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public CapacityMBean[] getCapacities() {
      return this._Capacities;
   }

   public boolean isCapacitiesSet() {
      return this._isSet(12);
   }

   public void removeCapacity(CapacityMBean var1) {
      this.destroyCapacity(var1);
   }

   public void setCapacities(CapacityMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new CapacityMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 12)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      CapacityMBean[] var5 = this._Capacities;
      this._Capacities = (CapacityMBean[])var4;
      this._postSet(12, var5, var4);
   }

   public CapacityMBean createCapacity(String var1) {
      CapacityMBeanImpl var2 = new CapacityMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addCapacity(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyCapacity(CapacityMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 12);
         CapacityMBean[] var2 = this.getCapacities();
         CapacityMBean[] var3 = (CapacityMBean[])((CapacityMBean[])this._getHelper()._removeElement(var2, CapacityMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setCapacities(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public CapacityMBean lookupCapacity(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._Capacities).iterator();

      CapacityMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (CapacityMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void addWorkManager(WorkManagerMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 13)) {
         WorkManagerMBean[] var2;
         if (this._isSet(13)) {
            var2 = (WorkManagerMBean[])((WorkManagerMBean[])this._getHelper()._extendArray(this.getWorkManagers(), WorkManagerMBean.class, var1));
         } else {
            var2 = new WorkManagerMBean[]{var1};
         }

         try {
            this.setWorkManagers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public WorkManagerMBean[] getWorkManagers() {
      return this._WorkManagers;
   }

   public boolean isWorkManagersSet() {
      return this._isSet(13);
   }

   public void removeWorkManager(WorkManagerMBean var1) {
      this.destroyWorkManager(var1);
   }

   public void setWorkManagers(WorkManagerMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new WorkManagerMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 13)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      WorkManagerMBean[] var5 = this._WorkManagers;
      this._WorkManagers = (WorkManagerMBean[])var4;
      this._postSet(13, var5, var4);
   }

   public WorkManagerMBean createWorkManager(String var1) {
      WorkManagerMBeanImpl var2 = new WorkManagerMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addWorkManager(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyWorkManager(WorkManagerMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 13);
         WorkManagerMBean[] var2 = this.getWorkManagers();
         WorkManagerMBean[] var3 = (WorkManagerMBean[])((WorkManagerMBean[])this._getHelper()._removeElement(var2, WorkManagerMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setWorkManagers(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public WorkManagerMBean lookupWorkManager(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._WorkManagers).iterator();

      WorkManagerMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (WorkManagerMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 12;
      }

      try {
         switch (var1) {
            case 12:
               this._Capacities = new CapacityMBean[0];
               if (var2) {
                  break;
               }
            case 9:
               this._ContextRequestClasses = new ContextRequestClassMBean[0];
               if (var2) {
                  break;
               }
            case 7:
               this._FairShareRequestClasses = new FairShareRequestClassMBean[0];
               if (var2) {
                  break;
               }
            case 11:
               this._MaxThreadsConstraints = new MaxThreadsConstraintMBean[0];
               if (var2) {
                  break;
               }
            case 10:
               this._MinThreadsConstraints = new MinThreadsConstraintMBean[0];
               if (var2) {
                  break;
               }
            case 8:
               this._ResponseTimeRequestClasses = new ResponseTimeRequestClassMBean[0];
               if (var2) {
                  break;
               }
            case 13:
               this._WorkManagers = new WorkManagerMBean[0];
               if (var2) {
                  break;
               }
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "SelfTuning";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("Capacities")) {
         CapacityMBean[] var9 = this._Capacities;
         this._Capacities = (CapacityMBean[])((CapacityMBean[])var2);
         this._postSet(12, var9, this._Capacities);
      } else if (var1.equals("ContextRequestClasses")) {
         ContextRequestClassMBean[] var8 = this._ContextRequestClasses;
         this._ContextRequestClasses = (ContextRequestClassMBean[])((ContextRequestClassMBean[])var2);
         this._postSet(9, var8, this._ContextRequestClasses);
      } else if (var1.equals("FairShareRequestClasses")) {
         FairShareRequestClassMBean[] var7 = this._FairShareRequestClasses;
         this._FairShareRequestClasses = (FairShareRequestClassMBean[])((FairShareRequestClassMBean[])var2);
         this._postSet(7, var7, this._FairShareRequestClasses);
      } else if (var1.equals("MaxThreadsConstraints")) {
         MaxThreadsConstraintMBean[] var6 = this._MaxThreadsConstraints;
         this._MaxThreadsConstraints = (MaxThreadsConstraintMBean[])((MaxThreadsConstraintMBean[])var2);
         this._postSet(11, var6, this._MaxThreadsConstraints);
      } else if (var1.equals("MinThreadsConstraints")) {
         MinThreadsConstraintMBean[] var5 = this._MinThreadsConstraints;
         this._MinThreadsConstraints = (MinThreadsConstraintMBean[])((MinThreadsConstraintMBean[])var2);
         this._postSet(10, var5, this._MinThreadsConstraints);
      } else if (var1.equals("ResponseTimeRequestClasses")) {
         ResponseTimeRequestClassMBean[] var4 = this._ResponseTimeRequestClasses;
         this._ResponseTimeRequestClasses = (ResponseTimeRequestClassMBean[])((ResponseTimeRequestClassMBean[])var2);
         this._postSet(8, var4, this._ResponseTimeRequestClasses);
      } else if (var1.equals("WorkManagers")) {
         WorkManagerMBean[] var3 = this._WorkManagers;
         this._WorkManagers = (WorkManagerMBean[])((WorkManagerMBean[])var2);
         this._postSet(13, var3, this._WorkManagers);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Capacities")) {
         return this._Capacities;
      } else if (var1.equals("ContextRequestClasses")) {
         return this._ContextRequestClasses;
      } else if (var1.equals("FairShareRequestClasses")) {
         return this._FairShareRequestClasses;
      } else if (var1.equals("MaxThreadsConstraints")) {
         return this._MaxThreadsConstraints;
      } else if (var1.equals("MinThreadsConstraints")) {
         return this._MinThreadsConstraints;
      } else if (var1.equals("ResponseTimeRequestClasses")) {
         return this._ResponseTimeRequestClasses;
      } else {
         return var1.equals("WorkManagers") ? this._WorkManagers : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 8:
               if (var1.equals("capacity")) {
                  return 12;
               }
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 23:
            case 25:
            case 26:
            default:
               break;
            case 12:
               if (var1.equals("work-manager")) {
                  return 13;
               }
               break;
            case 21:
               if (var1.equals("context-request-class")) {
                  return 9;
               }
               break;
            case 22:
               if (var1.equals("max-threads-constraint")) {
                  return 11;
               }

               if (var1.equals("min-threads-constraint")) {
                  return 10;
               }
               break;
            case 24:
               if (var1.equals("fair-share-request-class")) {
                  return 7;
               }
               break;
            case 27:
               if (var1.equals("response-time-request-class")) {
                  return 8;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 7:
               return new FairShareRequestClassMBeanImpl.SchemaHelper2();
            case 8:
               return new ResponseTimeRequestClassMBeanImpl.SchemaHelper2();
            case 9:
               return new ContextRequestClassMBeanImpl.SchemaHelper2();
            case 10:
               return new MinThreadsConstraintMBeanImpl.SchemaHelper2();
            case 11:
               return new MaxThreadsConstraintMBeanImpl.SchemaHelper2();
            case 12:
               return new CapacityMBeanImpl.SchemaHelper2();
            case 13:
               return new WorkManagerMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "fair-share-request-class";
            case 8:
               return "response-time-request-class";
            case 9:
               return "context-request-class";
            case 10:
               return "min-threads-constraint";
            case 11:
               return "max-threads-constraint";
            case 12:
               return "capacity";
            case 13:
               return "work-manager";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            case 10:
               return true;
            case 11:
               return true;
            case 12:
               return true;
            case 13:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
               return true;
            case 9:
               return true;
            case 10:
               return true;
            case 11:
               return true;
            case 12:
               return true;
            case 13:
               return true;
            default:
               return super.isBean(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private SelfTuningMBeanImpl bean;

      protected Helper(SelfTuningMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "FairShareRequestClasses";
            case 8:
               return "ResponseTimeRequestClasses";
            case 9:
               return "ContextRequestClasses";
            case 10:
               return "MinThreadsConstraints";
            case 11:
               return "MaxThreadsConstraints";
            case 12:
               return "Capacities";
            case 13:
               return "WorkManagers";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Capacities")) {
            return 12;
         } else if (var1.equals("ContextRequestClasses")) {
            return 9;
         } else if (var1.equals("FairShareRequestClasses")) {
            return 7;
         } else if (var1.equals("MaxThreadsConstraints")) {
            return 11;
         } else if (var1.equals("MinThreadsConstraints")) {
            return 10;
         } else if (var1.equals("ResponseTimeRequestClasses")) {
            return 8;
         } else {
            return var1.equals("WorkManagers") ? 13 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getCapacities()));
         var1.add(new ArrayIterator(this.bean.getContextRequestClasses()));
         var1.add(new ArrayIterator(this.bean.getFairShareRequestClasses()));
         var1.add(new ArrayIterator(this.bean.getMaxThreadsConstraints()));
         var1.add(new ArrayIterator(this.bean.getMinThreadsConstraints()));
         var1.add(new ArrayIterator(this.bean.getResponseTimeRequestClasses()));
         var1.add(new ArrayIterator(this.bean.getWorkManagers()));
         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            var5 = 0L;

            int var7;
            for(var7 = 0; var7 < this.bean.getCapacities().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getCapacities()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getContextRequestClasses().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getContextRequestClasses()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getFairShareRequestClasses().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getFairShareRequestClasses()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getMaxThreadsConstraints().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getMaxThreadsConstraints()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getMinThreadsConstraints().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getMinThreadsConstraints()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getResponseTimeRequestClasses().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getResponseTimeRequestClasses()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = 0L;

            for(var7 = 0; var7 < this.bean.getWorkManagers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getWorkManagers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            SelfTuningMBeanImpl var2 = (SelfTuningMBeanImpl)var1;
            this.computeChildDiff("Capacities", this.bean.getCapacities(), var2.getCapacities(), true);
            this.computeChildDiff("ContextRequestClasses", this.bean.getContextRequestClasses(), var2.getContextRequestClasses(), true);
            this.computeChildDiff("FairShareRequestClasses", this.bean.getFairShareRequestClasses(), var2.getFairShareRequestClasses(), true);
            this.computeChildDiff("MaxThreadsConstraints", this.bean.getMaxThreadsConstraints(), var2.getMaxThreadsConstraints(), true);
            this.computeChildDiff("MinThreadsConstraints", this.bean.getMinThreadsConstraints(), var2.getMinThreadsConstraints(), true);
            this.computeChildDiff("ResponseTimeRequestClasses", this.bean.getResponseTimeRequestClasses(), var2.getResponseTimeRequestClasses(), true);
            this.computeChildDiff("WorkManagers", this.bean.getWorkManagers(), var2.getWorkManagers(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SelfTuningMBeanImpl var3 = (SelfTuningMBeanImpl)var1.getSourceBean();
            SelfTuningMBeanImpl var4 = (SelfTuningMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Capacities")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addCapacity((CapacityMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeCapacity((CapacityMBean)var2.getRemovedObject());
                  }

                  if (var3.getCapacities() == null || var3.getCapacities().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  }
               } else if (var5.equals("ContextRequestClasses")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addContextRequestClass((ContextRequestClassMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeContextRequestClass((ContextRequestClassMBean)var2.getRemovedObject());
                  }

                  if (var3.getContextRequestClasses() == null || var3.getContextRequestClasses().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  }
               } else if (var5.equals("FairShareRequestClasses")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addFairShareRequestClass((FairShareRequestClassMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeFairShareRequestClass((FairShareRequestClassMBean)var2.getRemovedObject());
                  }

                  if (var3.getFairShareRequestClasses() == null || var3.getFairShareRequestClasses().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  }
               } else if (var5.equals("MaxThreadsConstraints")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addMaxThreadsConstraint((MaxThreadsConstraintMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeMaxThreadsConstraint((MaxThreadsConstraintMBean)var2.getRemovedObject());
                  }

                  if (var3.getMaxThreadsConstraints() == null || var3.getMaxThreadsConstraints().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  }
               } else if (var5.equals("MinThreadsConstraints")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addMinThreadsConstraint((MinThreadsConstraintMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeMinThreadsConstraint((MinThreadsConstraintMBean)var2.getRemovedObject());
                  }

                  if (var3.getMinThreadsConstraints() == null || var3.getMinThreadsConstraints().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  }
               } else if (var5.equals("ResponseTimeRequestClasses")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addResponseTimeRequestClass((ResponseTimeRequestClassMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeResponseTimeRequestClass((ResponseTimeRequestClassMBean)var2.getRemovedObject());
                  }

                  if (var3.getResponseTimeRequestClasses() == null || var3.getResponseTimeRequestClasses().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  }
               } else if (var5.equals("WorkManagers")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addWorkManager((WorkManagerMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeWorkManager((WorkManagerMBean)var2.getRemovedObject());
                  }

                  if (var3.getWorkManagers() == null || var3.getWorkManagers().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  }
               } else {
                  super.applyPropertyUpdate(var1, var2);
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            SelfTuningMBeanImpl var5 = (SelfTuningMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            int var8;
            if ((var3 == null || !var3.contains("Capacities")) && this.bean.isCapacitiesSet() && !var5._isSet(12)) {
               CapacityMBean[] var6 = this.bean.getCapacities();
               CapacityMBean[] var7 = new CapacityMBean[var6.length];

               for(var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (CapacityMBean)((CapacityMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setCapacities(var7);
            }

            if ((var3 == null || !var3.contains("ContextRequestClasses")) && this.bean.isContextRequestClassesSet() && !var5._isSet(9)) {
               ContextRequestClassMBean[] var11 = this.bean.getContextRequestClasses();
               ContextRequestClassMBean[] var14 = new ContextRequestClassMBean[var11.length];

               for(var8 = 0; var8 < var14.length; ++var8) {
                  var14[var8] = (ContextRequestClassMBean)((ContextRequestClassMBean)this.createCopy((AbstractDescriptorBean)var11[var8], var2));
               }

               var5.setContextRequestClasses(var14);
            }

            if ((var3 == null || !var3.contains("FairShareRequestClasses")) && this.bean.isFairShareRequestClassesSet() && !var5._isSet(7)) {
               FairShareRequestClassMBean[] var12 = this.bean.getFairShareRequestClasses();
               FairShareRequestClassMBean[] var16 = new FairShareRequestClassMBean[var12.length];

               for(var8 = 0; var8 < var16.length; ++var8) {
                  var16[var8] = (FairShareRequestClassMBean)((FairShareRequestClassMBean)this.createCopy((AbstractDescriptorBean)var12[var8], var2));
               }

               var5.setFairShareRequestClasses(var16);
            }

            if ((var3 == null || !var3.contains("MaxThreadsConstraints")) && this.bean.isMaxThreadsConstraintsSet() && !var5._isSet(11)) {
               MaxThreadsConstraintMBean[] var13 = this.bean.getMaxThreadsConstraints();
               MaxThreadsConstraintMBean[] var18 = new MaxThreadsConstraintMBean[var13.length];

               for(var8 = 0; var8 < var18.length; ++var8) {
                  var18[var8] = (MaxThreadsConstraintMBean)((MaxThreadsConstraintMBean)this.createCopy((AbstractDescriptorBean)var13[var8], var2));
               }

               var5.setMaxThreadsConstraints(var18);
            }

            if ((var3 == null || !var3.contains("MinThreadsConstraints")) && this.bean.isMinThreadsConstraintsSet() && !var5._isSet(10)) {
               MinThreadsConstraintMBean[] var15 = this.bean.getMinThreadsConstraints();
               MinThreadsConstraintMBean[] var20 = new MinThreadsConstraintMBean[var15.length];

               for(var8 = 0; var8 < var20.length; ++var8) {
                  var20[var8] = (MinThreadsConstraintMBean)((MinThreadsConstraintMBean)this.createCopy((AbstractDescriptorBean)var15[var8], var2));
               }

               var5.setMinThreadsConstraints(var20);
            }

            if ((var3 == null || !var3.contains("ResponseTimeRequestClasses")) && this.bean.isResponseTimeRequestClassesSet() && !var5._isSet(8)) {
               ResponseTimeRequestClassMBean[] var17 = this.bean.getResponseTimeRequestClasses();
               ResponseTimeRequestClassMBean[] var21 = new ResponseTimeRequestClassMBean[var17.length];

               for(var8 = 0; var8 < var21.length; ++var8) {
                  var21[var8] = (ResponseTimeRequestClassMBean)((ResponseTimeRequestClassMBean)this.createCopy((AbstractDescriptorBean)var17[var8], var2));
               }

               var5.setResponseTimeRequestClasses(var21);
            }

            if ((var3 == null || !var3.contains("WorkManagers")) && this.bean.isWorkManagersSet() && !var5._isSet(13)) {
               WorkManagerMBean[] var19 = this.bean.getWorkManagers();
               WorkManagerMBean[] var22 = new WorkManagerMBean[var19.length];

               for(var8 = 0; var8 < var22.length; ++var8) {
                  var22[var8] = (WorkManagerMBean)((WorkManagerMBean)this.createCopy((AbstractDescriptorBean)var19[var8], var2));
               }

               var5.setWorkManagers(var22);
            }

            return var5;
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var10);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getCapacities(), var1, var2);
         this.inferSubTree(this.bean.getContextRequestClasses(), var1, var2);
         this.inferSubTree(this.bean.getFairShareRequestClasses(), var1, var2);
         this.inferSubTree(this.bean.getMaxThreadsConstraints(), var1, var2);
         this.inferSubTree(this.bean.getMinThreadsConstraints(), var1, var2);
         this.inferSubTree(this.bean.getResponseTimeRequestClasses(), var1, var2);
         this.inferSubTree(this.bean.getWorkManagers(), var1, var2);
      }
   }
}
