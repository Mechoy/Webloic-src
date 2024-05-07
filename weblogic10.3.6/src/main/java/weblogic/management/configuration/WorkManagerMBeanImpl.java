package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class WorkManagerMBeanImpl extends DeploymentMBeanImpl implements WorkManagerMBean, Serializable {
   private CapacityMBean _Capacity;
   private ContextRequestClassMBean _ContextRequestClass;
   private FairShareRequestClassMBean _FairShareRequestClass;
   private boolean _IgnoreStuckThreads;
   private MaxThreadsConstraintMBean _MaxThreadsConstraint;
   private MinThreadsConstraintMBean _MinThreadsConstraint;
   private ResponseTimeRequestClassMBean _ResponseTimeRequestClass;
   private WorkManagerShutdownTriggerMBean _WorkManagerShutdownTrigger;
   private static SchemaHelper2 _schemaHelper;

   public WorkManagerMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WorkManagerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public FairShareRequestClassMBean getFairShareRequestClass() {
      return this._FairShareRequestClass;
   }

   public String getFairShareRequestClassAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getFairShareRequestClass();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isFairShareRequestClassSet() {
      return this._isSet(9);
   }

   public void setFairShareRequestClassAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, FairShareRequestClassMBean.class, new ReferenceManager.Resolver(this, 9) {
            public void resolveReference(Object var1) {
               try {
                  WorkManagerMBeanImpl.this.setFairShareRequestClass((FairShareRequestClassMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         FairShareRequestClassMBean var2 = this._FairShareRequestClass;
         this._initializeProperty(9);
         this._postSet(9, var2, this._FairShareRequestClass);
      }

   }

   public void setFairShareRequestClass(FairShareRequestClassMBean var1) {
      WorkManagerLegalHelper.validateTargets(this, var1);
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 9, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return WorkManagerMBeanImpl.this.getFairShareRequestClass();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      FairShareRequestClassMBean var3 = this._FairShareRequestClass;
      this._FairShareRequestClass = var1;
      this._postSet(9, var3, var1);
   }

   public ResponseTimeRequestClassMBean getResponseTimeRequestClass() {
      return this._ResponseTimeRequestClass;
   }

   public String getResponseTimeRequestClassAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getResponseTimeRequestClass();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isResponseTimeRequestClassSet() {
      return this._isSet(10);
   }

   public void setResponseTimeRequestClassAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, ResponseTimeRequestClassMBean.class, new ReferenceManager.Resolver(this, 10) {
            public void resolveReference(Object var1) {
               try {
                  WorkManagerMBeanImpl.this.setResponseTimeRequestClass((ResponseTimeRequestClassMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         ResponseTimeRequestClassMBean var2 = this._ResponseTimeRequestClass;
         this._initializeProperty(10);
         this._postSet(10, var2, this._ResponseTimeRequestClass);
      }

   }

   public void setResponseTimeRequestClass(ResponseTimeRequestClassMBean var1) {
      WorkManagerLegalHelper.validateTargets(this, var1);
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 10, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return WorkManagerMBeanImpl.this.getResponseTimeRequestClass();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      ResponseTimeRequestClassMBean var3 = this._ResponseTimeRequestClass;
      this._ResponseTimeRequestClass = var1;
      this._postSet(10, var3, var1);
   }

   public ContextRequestClassMBean getContextRequestClass() {
      return this._ContextRequestClass;
   }

   public String getContextRequestClassAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getContextRequestClass();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isContextRequestClassSet() {
      return this._isSet(11);
   }

   public void setContextRequestClassAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, ContextRequestClassMBean.class, new ReferenceManager.Resolver(this, 11) {
            public void resolveReference(Object var1) {
               try {
                  WorkManagerMBeanImpl.this.setContextRequestClass((ContextRequestClassMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         ContextRequestClassMBean var2 = this._ContextRequestClass;
         this._initializeProperty(11);
         this._postSet(11, var2, this._ContextRequestClass);
      }

   }

   public void setContextRequestClass(ContextRequestClassMBean var1) {
      WorkManagerLegalHelper.validateTargets(this, var1);
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 11, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return WorkManagerMBeanImpl.this.getContextRequestClass();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      ContextRequestClassMBean var3 = this._ContextRequestClass;
      this._ContextRequestClass = var1;
      this._postSet(11, var3, var1);
   }

   public MinThreadsConstraintMBean getMinThreadsConstraint() {
      return this._MinThreadsConstraint;
   }

   public String getMinThreadsConstraintAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getMinThreadsConstraint();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isMinThreadsConstraintSet() {
      return this._isSet(12);
   }

   public void setMinThreadsConstraintAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, MinThreadsConstraintMBean.class, new ReferenceManager.Resolver(this, 12) {
            public void resolveReference(Object var1) {
               try {
                  WorkManagerMBeanImpl.this.setMinThreadsConstraint((MinThreadsConstraintMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         MinThreadsConstraintMBean var2 = this._MinThreadsConstraint;
         this._initializeProperty(12);
         this._postSet(12, var2, this._MinThreadsConstraint);
      }

   }

   public void setMinThreadsConstraint(MinThreadsConstraintMBean var1) {
      WorkManagerLegalHelper.validateTargets(this, var1);
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 12, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return WorkManagerMBeanImpl.this.getMinThreadsConstraint();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      MinThreadsConstraintMBean var3 = this._MinThreadsConstraint;
      this._MinThreadsConstraint = var1;
      this._postSet(12, var3, var1);
   }

   public MaxThreadsConstraintMBean getMaxThreadsConstraint() {
      return this._MaxThreadsConstraint;
   }

   public String getMaxThreadsConstraintAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getMaxThreadsConstraint();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isMaxThreadsConstraintSet() {
      return this._isSet(13);
   }

   public void setMaxThreadsConstraintAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, MaxThreadsConstraintMBean.class, new ReferenceManager.Resolver(this, 13) {
            public void resolveReference(Object var1) {
               try {
                  WorkManagerMBeanImpl.this.setMaxThreadsConstraint((MaxThreadsConstraintMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         MaxThreadsConstraintMBean var2 = this._MaxThreadsConstraint;
         this._initializeProperty(13);
         this._postSet(13, var2, this._MaxThreadsConstraint);
      }

   }

   public void setMaxThreadsConstraint(MaxThreadsConstraintMBean var1) {
      WorkManagerLegalHelper.validateTargets(this, var1);
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 13, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return WorkManagerMBeanImpl.this.getMaxThreadsConstraint();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      MaxThreadsConstraintMBean var3 = this._MaxThreadsConstraint;
      this._MaxThreadsConstraint = var1;
      this._postSet(13, var3, var1);
   }

   public CapacityMBean getCapacity() {
      return this._Capacity;
   }

   public String getCapacityAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getCapacity();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isCapacitySet() {
      return this._isSet(14);
   }

   public void setCapacityAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, CapacityMBean.class, new ReferenceManager.Resolver(this, 14) {
            public void resolveReference(Object var1) {
               try {
                  WorkManagerMBeanImpl.this.setCapacity((CapacityMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         CapacityMBean var2 = this._Capacity;
         this._initializeProperty(14);
         this._postSet(14, var2, this._Capacity);
      }

   }

   public void setCapacity(CapacityMBean var1) {
      WorkManagerLegalHelper.validateTargets(this, var1);
      if (var1 != null) {
         ResolvedReference var2 = new ResolvedReference(this, 14, (AbstractDescriptorBean)var1) {
            protected Object getPropertyValue() {
               return WorkManagerMBeanImpl.this.getCapacity();
            }
         };
         this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1, var2);
      }

      CapacityMBean var3 = this._Capacity;
      this._Capacity = var1;
      this._postSet(14, var3, var1);
   }

   public boolean getIgnoreStuckThreads() {
      return this._IgnoreStuckThreads;
   }

   public boolean isIgnoreStuckThreadsSet() {
      return this._isSet(15);
   }

   public void setIgnoreStuckThreads(boolean var1) {
      boolean var2 = this._IgnoreStuckThreads;
      this._IgnoreStuckThreads = var1;
      this._postSet(15, var2, var1);
   }

   public WorkManagerShutdownTriggerMBean getWorkManagerShutdownTrigger() {
      return this._WorkManagerShutdownTrigger;
   }

   public boolean isWorkManagerShutdownTriggerSet() {
      return this._isSet(16);
   }

   public void setWorkManagerShutdownTrigger(WorkManagerShutdownTriggerMBean var1) throws InvalidAttributeValueException {
      if (var1 != null && this.getWorkManagerShutdownTrigger() != null && var1 != this.getWorkManagerShutdownTrigger()) {
         throw new BeanAlreadyExistsException(this.getWorkManagerShutdownTrigger() + " has already been created");
      } else {
         if (var1 != null) {
            AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
            if (this._setParent(var2, this, 16)) {
               this._getReferenceManager().registerBean(var2, false);
               this._postCreate(var2);
            }
         }

         WorkManagerShutdownTriggerMBean var3 = this._WorkManagerShutdownTrigger;
         this._WorkManagerShutdownTrigger = var1;
         this._postSet(16, var3, var1);
      }
   }

   public WorkManagerShutdownTriggerMBean createWorkManagerShutdownTrigger() {
      WorkManagerShutdownTriggerMBeanImpl var1 = new WorkManagerShutdownTriggerMBeanImpl(this, -1);

      try {
         this.setWorkManagerShutdownTrigger(var1);
         return var1;
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void destroyWorkManagerShutdownTrigger() {
      try {
         AbstractDescriptorBean var1 = (AbstractDescriptorBean)this._WorkManagerShutdownTrigger;
         if (var1 != null) {
            List var2 = this._getReferenceManager().getResolvedReferences(var1);
            if (var2 != null && var2.size() > 0) {
               throw new BeanRemoveRejectedException(var1, var2);
            } else {
               this._getReferenceManager().unregisterBean(var1);
               this._markDestroyed(var1);
               this.setWorkManagerShutdownTrigger((WorkManagerShutdownTriggerMBean)null);
               this._unSet(16);
            }
         }
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      WorkManagerLegalHelper.validateWorkManager(this);
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
         var1 = 14;
      }

      try {
         switch (var1) {
            case 14:
               this._Capacity = null;
               if (var2) {
                  break;
               }
            case 11:
               this._ContextRequestClass = null;
               if (var2) {
                  break;
               }
            case 9:
               this._FairShareRequestClass = null;
               if (var2) {
                  break;
               }
            case 15:
               this._IgnoreStuckThreads = false;
               if (var2) {
                  break;
               }
            case 13:
               this._MaxThreadsConstraint = null;
               if (var2) {
                  break;
               }
            case 12:
               this._MinThreadsConstraint = null;
               if (var2) {
                  break;
               }
            case 10:
               this._ResponseTimeRequestClass = null;
               if (var2) {
                  break;
               }
            case 16:
               this._WorkManagerShutdownTrigger = null;
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
      return "WorkManager";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("Capacity")) {
         CapacityMBean var10 = this._Capacity;
         this._Capacity = (CapacityMBean)var2;
         this._postSet(14, var10, this._Capacity);
      } else if (var1.equals("ContextRequestClass")) {
         ContextRequestClassMBean var9 = this._ContextRequestClass;
         this._ContextRequestClass = (ContextRequestClassMBean)var2;
         this._postSet(11, var9, this._ContextRequestClass);
      } else if (var1.equals("FairShareRequestClass")) {
         FairShareRequestClassMBean var8 = this._FairShareRequestClass;
         this._FairShareRequestClass = (FairShareRequestClassMBean)var2;
         this._postSet(9, var8, this._FairShareRequestClass);
      } else if (var1.equals("IgnoreStuckThreads")) {
         boolean var7 = this._IgnoreStuckThreads;
         this._IgnoreStuckThreads = (Boolean)var2;
         this._postSet(15, var7, this._IgnoreStuckThreads);
      } else if (var1.equals("MaxThreadsConstraint")) {
         MaxThreadsConstraintMBean var6 = this._MaxThreadsConstraint;
         this._MaxThreadsConstraint = (MaxThreadsConstraintMBean)var2;
         this._postSet(13, var6, this._MaxThreadsConstraint);
      } else if (var1.equals("MinThreadsConstraint")) {
         MinThreadsConstraintMBean var5 = this._MinThreadsConstraint;
         this._MinThreadsConstraint = (MinThreadsConstraintMBean)var2;
         this._postSet(12, var5, this._MinThreadsConstraint);
      } else if (var1.equals("ResponseTimeRequestClass")) {
         ResponseTimeRequestClassMBean var4 = this._ResponseTimeRequestClass;
         this._ResponseTimeRequestClass = (ResponseTimeRequestClassMBean)var2;
         this._postSet(10, var4, this._ResponseTimeRequestClass);
      } else if (var1.equals("WorkManagerShutdownTrigger")) {
         WorkManagerShutdownTriggerMBean var3 = this._WorkManagerShutdownTrigger;
         this._WorkManagerShutdownTrigger = (WorkManagerShutdownTriggerMBean)var2;
         this._postSet(16, var3, this._WorkManagerShutdownTrigger);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Capacity")) {
         return this._Capacity;
      } else if (var1.equals("ContextRequestClass")) {
         return this._ContextRequestClass;
      } else if (var1.equals("FairShareRequestClass")) {
         return this._FairShareRequestClass;
      } else if (var1.equals("IgnoreStuckThreads")) {
         return new Boolean(this._IgnoreStuckThreads);
      } else if (var1.equals("MaxThreadsConstraint")) {
         return this._MaxThreadsConstraint;
      } else if (var1.equals("MinThreadsConstraint")) {
         return this._MinThreadsConstraint;
      } else if (var1.equals("ResponseTimeRequestClass")) {
         return this._ResponseTimeRequestClass;
      } else {
         return var1.equals("WorkManagerShutdownTrigger") ? this._WorkManagerShutdownTrigger : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DeploymentMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 8:
               if (var1.equals("capacity")) {
                  return 14;
               }
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 23:
            case 25:
            case 26:
            case 28:
            default:
               break;
            case 20:
               if (var1.equals("ignore-stuck-threads")) {
                  return 15;
               }
               break;
            case 21:
               if (var1.equals("context-request-class")) {
                  return 11;
               }
               break;
            case 22:
               if (var1.equals("max-threads-constraint")) {
                  return 13;
               }

               if (var1.equals("min-threads-constraint")) {
                  return 12;
               }
               break;
            case 24:
               if (var1.equals("fair-share-request-class")) {
                  return 9;
               }
               break;
            case 27:
               if (var1.equals("response-time-request-class")) {
                  return 10;
               }
               break;
            case 29:
               if (var1.equals("work-manager-shutdown-trigger")) {
                  return 16;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 16:
               return new WorkManagerShutdownTriggerMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 9:
               return "fair-share-request-class";
            case 10:
               return "response-time-request-class";
            case 11:
               return "context-request-class";
            case 12:
               return "min-threads-constraint";
            case 13:
               return "max-threads-constraint";
            case 14:
               return "capacity";
            case 15:
               return "ignore-stuck-threads";
            case 16:
               return "work-manager-shutdown-trigger";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 16:
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

   protected static class Helper extends DeploymentMBeanImpl.Helper {
      private WorkManagerMBeanImpl bean;

      protected Helper(WorkManagerMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 9:
               return "FairShareRequestClass";
            case 10:
               return "ResponseTimeRequestClass";
            case 11:
               return "ContextRequestClass";
            case 12:
               return "MinThreadsConstraint";
            case 13:
               return "MaxThreadsConstraint";
            case 14:
               return "Capacity";
            case 15:
               return "IgnoreStuckThreads";
            case 16:
               return "WorkManagerShutdownTrigger";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Capacity")) {
            return 14;
         } else if (var1.equals("ContextRequestClass")) {
            return 11;
         } else if (var1.equals("FairShareRequestClass")) {
            return 9;
         } else if (var1.equals("IgnoreStuckThreads")) {
            return 15;
         } else if (var1.equals("MaxThreadsConstraint")) {
            return 13;
         } else if (var1.equals("MinThreadsConstraint")) {
            return 12;
         } else if (var1.equals("ResponseTimeRequestClass")) {
            return 10;
         } else {
            return var1.equals("WorkManagerShutdownTrigger") ? 16 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         if (this.bean.getWorkManagerShutdownTrigger() != null) {
            var1.add(new ArrayIterator(new WorkManagerShutdownTriggerMBean[]{this.bean.getWorkManagerShutdownTrigger()}));
         }

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
            if (this.bean.isCapacitySet()) {
               var2.append("Capacity");
               var2.append(String.valueOf(this.bean.getCapacity()));
            }

            if (this.bean.isContextRequestClassSet()) {
               var2.append("ContextRequestClass");
               var2.append(String.valueOf(this.bean.getContextRequestClass()));
            }

            if (this.bean.isFairShareRequestClassSet()) {
               var2.append("FairShareRequestClass");
               var2.append(String.valueOf(this.bean.getFairShareRequestClass()));
            }

            if (this.bean.isIgnoreStuckThreadsSet()) {
               var2.append("IgnoreStuckThreads");
               var2.append(String.valueOf(this.bean.getIgnoreStuckThreads()));
            }

            if (this.bean.isMaxThreadsConstraintSet()) {
               var2.append("MaxThreadsConstraint");
               var2.append(String.valueOf(this.bean.getMaxThreadsConstraint()));
            }

            if (this.bean.isMinThreadsConstraintSet()) {
               var2.append("MinThreadsConstraint");
               var2.append(String.valueOf(this.bean.getMinThreadsConstraint()));
            }

            if (this.bean.isResponseTimeRequestClassSet()) {
               var2.append("ResponseTimeRequestClass");
               var2.append(String.valueOf(this.bean.getResponseTimeRequestClass()));
            }

            var5 = this.computeChildHashValue(this.bean.getWorkManagerShutdownTrigger());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            WorkManagerMBeanImpl var2 = (WorkManagerMBeanImpl)var1;
            this.computeDiff("Capacity", this.bean.getCapacity(), var2.getCapacity(), false);
            this.computeDiff("ContextRequestClass", this.bean.getContextRequestClass(), var2.getContextRequestClass(), false);
            this.computeDiff("FairShareRequestClass", this.bean.getFairShareRequestClass(), var2.getFairShareRequestClass(), false);
            this.computeDiff("IgnoreStuckThreads", this.bean.getIgnoreStuckThreads(), var2.getIgnoreStuckThreads(), false);
            this.computeDiff("MaxThreadsConstraint", this.bean.getMaxThreadsConstraint(), var2.getMaxThreadsConstraint(), false);
            this.computeDiff("MinThreadsConstraint", this.bean.getMinThreadsConstraint(), var2.getMinThreadsConstraint(), false);
            this.computeDiff("ResponseTimeRequestClass", this.bean.getResponseTimeRequestClass(), var2.getResponseTimeRequestClass(), false);
            this.computeChildDiff("WorkManagerShutdownTrigger", this.bean.getWorkManagerShutdownTrigger(), var2.getWorkManagerShutdownTrigger(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WorkManagerMBeanImpl var3 = (WorkManagerMBeanImpl)var1.getSourceBean();
            WorkManagerMBeanImpl var4 = (WorkManagerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Capacity")) {
                  var3.setCapacityAsString(var4.getCapacityAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("ContextRequestClass")) {
                  var3.setContextRequestClassAsString(var4.getContextRequestClassAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("FairShareRequestClass")) {
                  var3.setFairShareRequestClassAsString(var4.getFairShareRequestClassAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("IgnoreStuckThreads")) {
                  var3.setIgnoreStuckThreads(var4.getIgnoreStuckThreads());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("MaxThreadsConstraint")) {
                  var3.setMaxThreadsConstraintAsString(var4.getMaxThreadsConstraintAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("MinThreadsConstraint")) {
                  var3.setMinThreadsConstraintAsString(var4.getMinThreadsConstraintAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("ResponseTimeRequestClass")) {
                  var3.setResponseTimeRequestClassAsString(var4.getResponseTimeRequestClassAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("WorkManagerShutdownTrigger")) {
                  if (var6 == 2) {
                     var3.setWorkManagerShutdownTrigger((WorkManagerShutdownTriggerMBean)this.createCopy((AbstractDescriptorBean)var4.getWorkManagerShutdownTrigger()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("WorkManagerShutdownTrigger", var3.getWorkManagerShutdownTrigger());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
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
            WorkManagerMBeanImpl var5 = (WorkManagerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Capacity")) && this.bean.isCapacitySet()) {
               var5._unSet(var5, 14);
               var5.setCapacityAsString(this.bean.getCapacityAsString());
            }

            if ((var3 == null || !var3.contains("ContextRequestClass")) && this.bean.isContextRequestClassSet()) {
               var5._unSet(var5, 11);
               var5.setContextRequestClassAsString(this.bean.getContextRequestClassAsString());
            }

            if ((var3 == null || !var3.contains("FairShareRequestClass")) && this.bean.isFairShareRequestClassSet()) {
               var5._unSet(var5, 9);
               var5.setFairShareRequestClassAsString(this.bean.getFairShareRequestClassAsString());
            }

            if ((var3 == null || !var3.contains("IgnoreStuckThreads")) && this.bean.isIgnoreStuckThreadsSet()) {
               var5.setIgnoreStuckThreads(this.bean.getIgnoreStuckThreads());
            }

            if ((var3 == null || !var3.contains("MaxThreadsConstraint")) && this.bean.isMaxThreadsConstraintSet()) {
               var5._unSet(var5, 13);
               var5.setMaxThreadsConstraintAsString(this.bean.getMaxThreadsConstraintAsString());
            }

            if ((var3 == null || !var3.contains("MinThreadsConstraint")) && this.bean.isMinThreadsConstraintSet()) {
               var5._unSet(var5, 12);
               var5.setMinThreadsConstraintAsString(this.bean.getMinThreadsConstraintAsString());
            }

            if ((var3 == null || !var3.contains("ResponseTimeRequestClass")) && this.bean.isResponseTimeRequestClassSet()) {
               var5._unSet(var5, 10);
               var5.setResponseTimeRequestClassAsString(this.bean.getResponseTimeRequestClassAsString());
            }

            if ((var3 == null || !var3.contains("WorkManagerShutdownTrigger")) && this.bean.isWorkManagerShutdownTriggerSet() && !var5._isSet(16)) {
               WorkManagerShutdownTriggerMBean var4 = this.bean.getWorkManagerShutdownTrigger();
               var5.setWorkManagerShutdownTrigger((WorkManagerShutdownTriggerMBean)null);
               var5.setWorkManagerShutdownTrigger(var4 == null ? null : (WorkManagerShutdownTriggerMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getCapacity(), var1, var2);
         this.inferSubTree(this.bean.getContextRequestClass(), var1, var2);
         this.inferSubTree(this.bean.getFairShareRequestClass(), var1, var2);
         this.inferSubTree(this.bean.getMaxThreadsConstraint(), var1, var2);
         this.inferSubTree(this.bean.getMinThreadsConstraint(), var1, var2);
         this.inferSubTree(this.bean.getResponseTimeRequestClass(), var1, var2);
         this.inferSubTree(this.bean.getWorkManagerShutdownTrigger(), var1, var2);
      }
   }
}
