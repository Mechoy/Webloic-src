package weblogic.management.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;

public class GenericBeanListener implements BeanUpdateListener {
   private DescriptorBean listenToMe;
   private HashMap beanMethods;
   private Object managedObject;
   private HashMap managedMethods;
   private HashMap additionMethods;
   private HashMap validationMethods;
   private boolean registered;
   private BeanListenerCustomizer customizer;
   private BeanUpdateEvent currentEvent;
   private static final int START_ADD = 0;
   private static final int FINISH_ADD = 1;
   private static final int START_REM = 2;
   private static final int FINISH_REM = 3;
   private static final int MAX_INDEX = 4;
   private static final String[] adderMethodNames = new String[]{"startAdd", "finishAdd", "startRemove", "finishRemove"};

   public GenericBeanListener(DescriptorBean var1, Object var2, Map var3, Map var4, boolean var5) {
      this.beanMethods = new HashMap();
      this.managedMethods = new HashMap();
      this.additionMethods = new HashMap();
      this.validationMethods = new HashMap();
      this.registered = false;
      this.listenToMe = var1;
      this.managedObject = var2;
      Class var6 = this.managedObject == null ? null : this.managedObject.getClass();
      Class var7 = this.listenToMe.getClass();
      Class[] var8 = new Class[1];
      Set var9;
      Iterator var10;
      String var13;
      if (var3 != null) {
         var9 = var3.keySet();

         Method var11;
         String var12;
         for(var10 = var9.iterator(); var10.hasNext(); this.beanMethods.put(var12, var11)) {
            var11 = null;
            var12 = (String)var10.next();
            var8[0] = (Class)var3.get(var12);
            if (this.managedObject != null) {
               var13 = "set" + var12;
               String var14 = "val" + var12;

               try {
                  var11 = var6.getMethod(var13, var8);
               } catch (NoSuchMethodException var24) {
                  throw new AssertionError("ERROR: A managed object did not have a " + var13 + " method");
               } catch (SecurityException var25) {
                  throw new AssertionError("ERROR: A managed object could not find the " + var13 + " method");
               }

               this.managedMethods.put(var12, var11);

               try {
                  var11 = var6.getMethod(var14, var8);
                  this.validationMethods.put(var12, var11);
               } catch (NoSuchMethodException var22) {
               } catch (SecurityException var23) {
                  throw new AssertionError("ERROR: A managed object could not find the " + var14 + " method");
               }
            }

            var13 = null;
            if (var8[0] == Boolean.TYPE) {
               var13 = "is" + var12;
            } else {
               var13 = "get" + var12;
            }

            try {
               var11 = var7.getMethod(var13, (Class[])null);
            } catch (NoSuchMethodException var20) {
               throw new AssertionError("ERROR: A bean did not have a " + var13 + " method");
            } catch (SecurityException var21) {
               throw new AssertionError("ERROR: A bean did not have the " + var13 + " method");
            }
         }
      }

      if (var4 != null && var6 != null) {
         var9 = var4.keySet();
         var10 = var9.iterator();
         Class[] var26 = new Class[]{null, Boolean.TYPE};

         while(var10.hasNext()) {
            Method[] var27 = new Method[4];
            var13 = (String)var10.next();

            for(int var28 = 0; var28 < 4; ++var28) {
               String var15 = adderMethodNames[var28] + var13;
               Class[] var16;
               if (var28 % 2 == 0) {
                  var8[0] = (Class)var4.get(var13);
                  var16 = var8;
               } else {
                  var26[0] = (Class)var4.get(var13);
                  var16 = var26;
               }

               try {
                  var27[var28] = var6.getMethod(var15, var16);
               } catch (NoSuchMethodException var18) {
                  throw new AssertionError("ERROR: A managed object did not have a " + var15 + " method");
               } catch (SecurityException var19) {
                  throw new AssertionError("ERROR: A managed object could not find the " + var15 + " method");
               }
            }

            this.additionMethods.put(var13, var27);
         }
      }

      if (var5) {
         this.listenToMe.addBeanUpdateListener(this);
         this.registered = true;
      } else {
         this.registered = false;
      }

   }

   public GenericBeanListener(DescriptorBean var1, Object var2, Map var3, boolean var4) {
      this(var1, var2, var3, (Map)null, var4);
   }

   public GenericBeanListener(DescriptorBean var1, Object var2, Map var3) {
      this(var1, var2, var3, (Map)null, true);
   }

   public GenericBeanListener(DescriptorBean var1, Object var2, Map var3, Map var4) {
      this(var1, var2, var3, var4, true);
   }

   public synchronized void setCustomizer(BeanListenerCustomizer var1) {
      this.customizer = var1;
   }

   public synchronized void open() {
      if (!this.registered) {
         this.listenToMe.addBeanUpdateListener(this);
         this.registered = true;
      }
   }

   public synchronized void close() {
      if (this.registered) {
         this.listenToMe.removeBeanUpdateListener(this);
         this.registered = false;
      }
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      this.currentEvent = var1;
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
      boolean var3 = false;
      int var4 = 0;

      try {
         if (this.additionMethods.size() > 0 || this.validationMethods.size() > 0) {
            for(var4 = 0; var4 < var2.length; ++var4) {
               BeanUpdateEvent.PropertyUpdate var5 = var2[var4];
               String var6 = var5.getPropertyName();
               Method[] var7 = (Method[])((Method[])this.additionMethods.get(var6));
               int var8 = var5.getUpdateType();
               Object[] var9;
               Throwable var11;
               if (var7 != null && var8 == 2) {
                  var9 = new Object[]{var5.getAddedObject()};

                  try {
                     var7[0].invoke(this.managedObject, var9);
                  } catch (IllegalAccessException var37) {
                     throw new BeanUpdateRejectedException(ManagementLogger.logAddBeanFailed1Loggable(var6, var37.toString()).getMessage(), var37);
                  } catch (IllegalArgumentException var38) {
                     throw new BeanUpdateRejectedException(ManagementLogger.logAddBeanFailed2Loggable(var6, var38.toString()).getMessage(), var38);
                  } catch (InvocationTargetException var39) {
                     var11 = var39.getCause();
                     if (var11 instanceof BeanUpdateRejectedException) {
                        throw (BeanUpdateRejectedException)var11;
                     }

                     throw new BeanUpdateRejectedException(ManagementLogger.logAddBeanFailed3Loggable(var6, var11.toString()).getMessage(), (Throwable)(var11 == null ? var39 : var11));
                  }
               }

               if (var7 != null && var8 == 3) {
                  var9 = new Object[]{var5.getRemovedObject()};

                  try {
                     var7[2].invoke(this.managedObject, var9);
                  } catch (IllegalAccessException var34) {
                     throw new BeanUpdateRejectedException(ManagementLogger.logRemoveBeanFailed1Loggable(var6, var34.toString()).getMessage(), var34);
                  } catch (IllegalArgumentException var35) {
                     throw new BeanUpdateRejectedException(ManagementLogger.logRemoveBeanFailed2Loggable(var6, var35.toString()).getMessage(), var35);
                  } catch (InvocationTargetException var36) {
                     var11 = var36.getCause();
                     if (var11 instanceof BeanUpdateRejectedException) {
                        throw (BeanUpdateRejectedException)var11;
                     }

                     throw new BeanUpdateRejectedException(ManagementLogger.logRemoveBeanFailed3Loggable(var6, var11.toString()).getMessage(), (Throwable)(var11 == null ? var36 : var11));
                  }
               }

               Method var41 = this.managedObject == null ? null : (Method)this.validationMethods.get(var6);
               Method var10 = this.managedObject == null ? null : (Method)this.beanMethods.get(var6);
               if (var41 != null && var10 != null && var8 == 1) {
                  DescriptorBean var42 = var1.getProposedBean();

                  try {
                     Object[] var12 = new Object[]{var10.invoke(var42, (Object[])null)};
                     var41.invoke(this.managedObject, var12);
                  } catch (IllegalAccessException var31) {
                     throw new BeanUpdateRejectedException(var31.getMessage(), var31);
                  } catch (IllegalArgumentException var32) {
                     throw new BeanUpdateRejectedException(var32.getMessage(), var32);
                  } catch (InvocationTargetException var33) {
                     Throwable var13 = var33.getCause();
                     if (var13 instanceof BeanUpdateRejectedException) {
                        throw (BeanUpdateRejectedException)var13;
                     }

                     throw new BeanUpdateRejectedException(var13.getMessage(), (Throwable)(var13 == null ? var33 : var13));
                  }
               }
            }
         }

         var3 = true;
      } finally {
         if (!var3 && var4 > 0) {
            try {
               this.activateOrRollbackAddition(var2, var4, false);
            } catch (BeanUpdateFailedException var30) {
               Throwable var17 = var30.getCause();
               ManagementLogger.logRollbackFailure(var30.getMessage(), var17 == null ? "null" : var17.toString());
            }
         }

      }

   }

   private void activateOrRollbackAddition(BeanUpdateEvent.PropertyUpdate[] var1, int var2, boolean var3) throws BeanUpdateFailedException {
      BeanUpdateFailedException var4 = null;
      if (this.additionMethods.size() > 0) {
         int var5 = var2 < 0 ? var1.length : (var2 < var1.length ? var2 : var1.length);

         for(int var6 = 0; var6 < var5; ++var6) {
            BeanUpdateEvent.PropertyUpdate var7 = var1[var6];
            String var8 = var7.getPropertyName();
            Method[] var9 = (Method[])((Method[])this.additionMethods.get(var8));
            if (var9 != null) {
               int var10 = var7.getUpdateType();
               Object[] var11;
               Throwable var13;
               if (var10 == 2) {
                  var11 = new Object[]{var7.getAddedObject(), new Boolean(var3)};

                  try {
                     var9[1].invoke(this.managedObject, var11);
                  } catch (IllegalAccessException var17) {
                     if (var4 == null) {
                        var4 = new BeanUpdateFailedException(ManagementLogger.logFinishAddFailed1Loggable(var8, var17.toString()).getMessage(), var17);
                     }

                     ManagementLogger.logFinishAddFailed1(var8, var17.toString());
                  } catch (IllegalArgumentException var18) {
                     if (var4 == null) {
                        var4 = new BeanUpdateFailedException(ManagementLogger.logFinishAddFailed2Loggable(var8, var18.toString()).getMessage(), var18);
                     }

                     ManagementLogger.logFinishAddFailed2(var8, var18.toString());
                  } catch (InvocationTargetException var19) {
                     var13 = var19.getCause();
                     if (var13 instanceof RuntimeException) {
                        ManagementLogger.logBeanUpdateRuntimeException(var13);
                     }

                     if (var4 == null) {
                        if (var13 instanceof BeanUpdateFailedException) {
                           var4 = (BeanUpdateFailedException)var13;
                        } else {
                           var4 = new BeanUpdateFailedException(ManagementLogger.logFinishAddFailed3Loggable(var8, var13 == null ? "null" : var13.toString()).getMessage(), (Throwable)(var13 == null ? var19 : var13));
                        }
                     }

                     ManagementLogger.logFinishAddFailed3(var8, var13 == null ? "null" : var13.toString());
                  }
               }

               if (var10 == 3) {
                  var11 = new Object[]{var7.getRemovedObject(), new Boolean(var3)};

                  try {
                     var9[3].invoke(this.managedObject, var11);
                  } catch (IllegalAccessException var14) {
                     if (var4 == null) {
                        var4 = new BeanUpdateFailedException(ManagementLogger.logFinishRemoveFailed1Loggable(var8, var14.toString()).getMessage(), var14);
                     }

                     ManagementLogger.logFinishRemoveFailed1(var8, var14.toString());
                  } catch (IllegalArgumentException var15) {
                     if (var4 == null) {
                        var4 = new BeanUpdateFailedException(ManagementLogger.logFinishRemoveFailed2Loggable(var8, var15.toString()).getMessage(), var15);
                     }

                     ManagementLogger.logFinishRemoveFailed2(var8, var15.toString());
                  } catch (InvocationTargetException var16) {
                     var13 = var16.getCause();
                     if (var13 instanceof RuntimeException) {
                        ManagementLogger.logBeanUpdateRuntimeException(var13);
                     }

                     if (var4 == null) {
                        if (var13 instanceof BeanUpdateFailedException) {
                           var4 = (BeanUpdateFailedException)var13;
                        } else {
                           var4 = new BeanUpdateFailedException(ManagementLogger.logFinishRemoveFailed3Loggable(var8, var16.getCause().toString()).getMessage(), (Throwable)(var13 == null ? var16 : var13));
                        }
                     }

                     ManagementLogger.logFinishRemoveFailed3(var8, var13 == null ? "null" : var13.toString());
                  }
               }
            }
         }
      }

      if (var4 != null) {
         throw var4;
      }
   }

   public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
      BeanUpdateFailedException var2 = null;

      try {
         BeanUpdateEvent.PropertyUpdate[] var3 = var1.getUpdateList();

         try {
            this.activateOrRollbackAddition(var3, -1, true);
         } catch (BeanUpdateFailedException var20) {
            var2 = var20;
         }

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (this.managedObject != null && var3[var4].isDynamic()) {
               String var5 = var3[var4].getPropertyName();
               Method var6 = (Method)this.beanMethods.get(var5);
               if (var6 != null) {
                  Method var7 = (Method)this.managedMethods.get(var5);
                  if (var7 != null) {
                     try {
                        Object[] var8 = new Object[]{var6.invoke(this.listenToMe, (Object[])null)};
                        var7.invoke(this.managedObject, var8);
                     } catch (IllegalAccessException var22) {
                        if (var2 == null) {
                           var2 = new BeanUpdateFailedException(ManagementLogger.logPropertyChangeFailed1Loggable(var5, var22.toString()).getMessage(), var22);
                        }

                        ManagementLogger.logPropertyChangeFailed1(var5, var22.toString());
                     } catch (IllegalArgumentException var23) {
                        if (var2 == null) {
                           var2 = new BeanUpdateFailedException(ManagementLogger.logPropertyChangeFailed2Loggable(var5, var23.toString()).getMessage(), var23);
                        }

                        ManagementLogger.logPropertyChangeFailed2(var5, var23.toString());
                     } catch (InvocationTargetException var24) {
                        Throwable var9 = var24.getCause();
                        if (var2 == null) {
                           if (var9 instanceof BeanUpdateFailedException) {
                              var2 = (BeanUpdateFailedException)var9;
                           } else {
                              var2 = new BeanUpdateFailedException(ManagementLogger.logPropertyChangeFailed3Loggable(var5, var24.getCause().toString()).getMessage(), (Throwable)(var9 == null ? var24 : var9));
                           }
                        }

                        ManagementLogger.logPropertyChangeFailed3(var5, var9 == null ? "null" : var9.toString());
                     }
                  }
               }
            }
         }
      } finally {
         if (this.customizer != null) {
            try {
               this.customizer.activateFinished();
            } catch (BeanUpdateFailedException var21) {
               if (var2 == null) {
                  var2 = var21;
               }
            }
         }

         this.currentEvent = null;
      }

      if (var2 != null) {
         throw var2;
      }
   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
      try {
         BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();
         this.activateOrRollbackAddition(var2, -1, false);
      } catch (BeanUpdateFailedException var8) {
         Throwable var3 = var8.getCause();
         ManagementLogger.logRollbackFailure(var8.getMessage(), var3 == null ? "null" : var3.toString());
      } finally {
         this.currentEvent = null;
      }

   }

   public BeanUpdateEvent getCurrentEvent() {
      return this.currentEvent;
   }

   public void initialize() throws ManagementException {
      this.initialize(true);
   }

   public void initialize(boolean var1) throws ManagementException {
      Set var2 = this.beanMethods.keySet();
      Iterator var3 = var2.iterator();

      while(true) {
         String var4;
         do {
            if (!var3.hasNext()) {
               return;
            }

            var4 = (String)var3.next();
         } while(!var1 && !this.listenToMe.isSet(var4));

         Method var5 = (Method)this.beanMethods.get(var4);
         if (var5 == null) {
            throw new AssertionError("ERROR: Could not set property " + var4 + " because the method signature was not specified in intialize");
         }

         Method var6 = (Method)this.managedMethods.get(var4);
         if (var6 == null) {
            throw new AssertionError("ERROR: Could not set property " + var4 + " because the method signature setter was not specified in initialize");
         }

         try {
            Object[] var7 = new Object[]{var5.invoke(this.listenToMe, (Object[])null)};
            var6.invoke(this.managedObject, var7);
         } catch (IllegalAccessException var9) {
            throw new ManagementException(ManagementLogger.logPropertyInitializationFailed1Loggable(var4, var9.toString()).getMessage());
         } catch (IllegalArgumentException var10) {
            throw new ManagementException(ManagementLogger.logPropertyInitializationFailed2Loggable(var4, var10.toString()).getMessage());
         } catch (InvocationTargetException var11) {
            Throwable var8 = var11.getCause();
            if (var8 instanceof ManagementException) {
               throw (ManagementException)var8;
            }

            throw new ManagementException(ManagementLogger.logPropertyInitializationFailed3Loggable(var4, var8.toString()).getMessage());
         }
      }
   }
}
