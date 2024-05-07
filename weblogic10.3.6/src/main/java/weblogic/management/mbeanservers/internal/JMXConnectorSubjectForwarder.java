package weblogic.management.mbeanservers.internal;

import java.io.ObjectInputStream;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.loading.ClassLoaderRepository;
import javax.management.remote.MBeanServerForwarder;
import javax.security.auth.Subject;
import weblogic.management.NoAccessRuntimeException;
import weblogic.management.context.JMXContext;
import weblogic.management.context.JMXContextHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class JMXConnectorSubjectForwarder implements MBeanServerForwarder {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private MBeanServer delegate;

   public MBeanServer getMBeanServer() {
      return this.delegate;
   }

   public void setMBeanServer(MBeanServer var1) {
      this.delegate = var1;
   }

   public ObjectInstance createMBean(final String var1, final ObjectName var2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException {
      ObjectInstance var3 = null;
      AuthenticatedSubject var4 = this.getAuthenticatedSubject();

      try {
         var3 = (ObjectInstance)var4.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var2;
                  final String var4 = var1;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInstance>() {
                     public ObjectInstance run() throws Exception {
                        return JMXConnectorSubjectForwarder.this.getMBeanServer().createMBean(var4, var1x);
                     }
                  });
               } catch (PrivilegedActionException var3) {
                  Exception var2x = var3.getException();
                  if (var2x instanceof MBeanException) {
                     throw (MBeanException)var2x;
                  } else if (var2x instanceof MBeanRegistrationException) {
                     throw (MBeanRegistrationException)var2x;
                  } else if (var2x instanceof InstanceAlreadyExistsException) {
                     throw (InstanceAlreadyExistsException)var2x;
                  } else if (var2x instanceof NotCompliantMBeanException) {
                     throw (NotCompliantMBeanException)var2x;
                  } else if (var2x instanceof ReflectionException) {
                     throw (ReflectionException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
      } catch (PrivilegedActionException var6) {
         this.rethrowCreateException(var2, var6);
      }

      return var3;
   }

   public void rethrowCreateException(ObjectName var1, PrivilegedActionException var2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException {
      Exception var3 = var2.getException();
      if (var3 instanceof NoAccessRuntimeException) {
         throw (NoAccessRuntimeException)var3;
      } else if (var3 instanceof ReflectionException) {
         throw (ReflectionException)var3;
      } else if (var3 instanceof InstanceAlreadyExistsException) {
         throw (InstanceAlreadyExistsException)var3;
      } else if (var3 instanceof MBeanRegistrationException) {
         throw (MBeanRegistrationException)var3;
      } else if (var3 instanceof MBeanException) {
         throw (MBeanException)var3;
      } else if (var3 instanceof NotCompliantMBeanException) {
         throw (NotCompliantMBeanException)var3;
      } else if (var3 instanceof RuntimeException) {
         throw (RuntimeException)var3;
      } else {
         throw new AssertionError(var3);
      }
   }

   public ObjectInstance createMBean(final String var1, final ObjectName var2, final ObjectName var3) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException {
      ObjectInstance var4 = null;

      try {
         AuthenticatedSubject var5 = this.getAuthenticatedSubject();
         var4 = (ObjectInstance)var5.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var2;
                  final String var5 = var1;
                  final ObjectName var3x = var3;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInstance>() {
                     public ObjectInstance run() throws Exception {
                        return JMXConnectorSubjectForwarder.this.getMBeanServer().createMBean(var5, var1x, var3x);
                     }
                  });
               } catch (PrivilegedActionException var4) {
                  Exception var2x = var4.getException();
                  if (var2x instanceof InstanceNotFoundException) {
                     throw (InstanceNotFoundException)var2x;
                  } else if (var2x instanceof MBeanException) {
                     throw (MBeanException)var2x;
                  } else if (var2x instanceof MBeanRegistrationException) {
                     throw (MBeanRegistrationException)var2x;
                  } else if (var2x instanceof NotCompliantMBeanException) {
                     throw (NotCompliantMBeanException)var2x;
                  } else if (var2x instanceof InstanceAlreadyExistsException) {
                     throw (InstanceAlreadyExistsException)var2x;
                  } else if (var2x instanceof ReflectionException) {
                     throw (ReflectionException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
      } catch (PrivilegedActionException var7) {
         Exception var6 = var7.getException();
         if (var6 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var6;
         }

         this.rethrowCreateException(var2, var7);
      }

      return var4;
   }

   public ObjectInstance createMBean(final String var1, final ObjectName var2, final Object[] var3, final String[] var4) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException {
      ObjectInstance var5 = null;

      try {
         AuthenticatedSubject var6 = this.getAuthenticatedSubject();
         var5 = (ObjectInstance)var6.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var2;
                  final String var6 = var1;
                  final Object[] var3x = var3;
                  final String[] var4x = var4;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInstance>() {
                     public ObjectInstance run() throws Exception {
                        return JMXConnectorSubjectForwarder.this.getMBeanServer().createMBean(var6, var1x, var3x, var4x);
                     }
                  });
               } catch (PrivilegedActionException var5) {
                  Exception var2x = var5.getException();
                  if (var2x instanceof InstanceAlreadyExistsException) {
                     throw (InstanceAlreadyExistsException)var2x;
                  } else if (var2x instanceof MBeanException) {
                     throw (MBeanException)var2x;
                  } else if (var2x instanceof NotCompliantMBeanException) {
                     throw (NotCompliantMBeanException)var2x;
                  } else if (var2x instanceof MBeanRegistrationException) {
                     throw (MBeanRegistrationException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
      } catch (PrivilegedActionException var7) {
         this.rethrowCreateException(var2, var7);
      }

      return var5;
   }

   public ObjectInstance createMBean(final String var1, final ObjectName var2, final ObjectName var3, final Object[] var4, final String[] var5) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException {
      ObjectInstance var6 = null;

      try {
         AuthenticatedSubject var7 = this.getAuthenticatedSubject();
         var6 = (ObjectInstance)var7.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var2;
                  final String var7 = var1;
                  final ObjectName var3x = var3;
                  final Object[] var4x = var4;
                  final String[] var5x = var5;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInstance>() {
                     public ObjectInstance run() throws Exception {
                        return JMXConnectorSubjectForwarder.this.getMBeanServer().createMBean(var7, var1x, var3x, var4x, var5x);
                     }
                  });
               } catch (PrivilegedActionException var6) {
                  Exception var2x = var6.getException();
                  if (var2x instanceof InstanceNotFoundException) {
                     throw (InstanceNotFoundException)var2x;
                  } else if (var2x instanceof MBeanException) {
                     throw (MBeanException)var2x;
                  } else if (var2x instanceof MBeanRegistrationException) {
                     throw (MBeanRegistrationException)var2x;
                  } else if (var2x instanceof NotCompliantMBeanException) {
                     throw (NotCompliantMBeanException)var2x;
                  } else if (var2x instanceof InstanceAlreadyExistsException) {
                     throw (InstanceAlreadyExistsException)var2x;
                  } else if (var2x instanceof ReflectionException) {
                     throw (ReflectionException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
      } catch (PrivilegedActionException var9) {
         Exception var8 = var9.getException();
         if (var8 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var8;
         }

         this.rethrowCreateException(var2, var9);
      }

      return var6;
   }

   public Object getAttribute(final ObjectName var1, final String var2) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException {
      Object var3 = null;
      AuthenticatedSubject var4 = this.getAuthenticatedSubject();

      try {
         var3 = var4.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var1;
                  final String var4 = var2;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                     public Object run() throws Exception {
                        return JMXConnectorSubjectForwarder.this.getMBeanServer().getAttribute(var1x, var4);
                     }
                  });
               } catch (PrivilegedActionException var3) {
                  Exception var2x = var3.getException();
                  if (var2x instanceof InstanceNotFoundException) {
                     throw (InstanceNotFoundException)var2x;
                  } else if (var2x instanceof MBeanException) {
                     throw (MBeanException)var2x;
                  } else if (var2x instanceof AttributeNotFoundException) {
                     throw (AttributeNotFoundException)var2x;
                  } else if (var2x instanceof ReflectionException) {
                     throw (ReflectionException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
         return var3;
      } catch (PrivilegedActionException var7) {
         Exception var6 = var7.getException();
         if (var6 instanceof MBeanException) {
            throw (MBeanException)var6;
         } else if (var6 instanceof AttributeNotFoundException) {
            throw (AttributeNotFoundException)var6;
         } else if (var6 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var6;
         } else if (var6 instanceof ReflectionException) {
            throw (ReflectionException)var6;
         } else if (var6 instanceof RuntimeException) {
            throw (RuntimeException)var6;
         } else {
            throw new AssertionError(var6);
         }
      }
   }

   public AttributeList getAttributes(final ObjectName var1, final String[] var2) throws InstanceNotFoundException, ReflectionException {
      AttributeList var3 = null;
      AuthenticatedSubject var4 = this.getAuthenticatedSubject();

      try {
         var3 = (AttributeList)var4.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var1;
                  final String[] var4 = var2;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<AttributeList>() {
                     public AttributeList run() throws Exception {
                        return JMXConnectorSubjectForwarder.this.getMBeanServer().getAttributes(var1x, var4);
                     }
                  });
               } catch (PrivilegedActionException var3) {
                  Exception var2x = var3.getException();
                  if (var2x instanceof InstanceNotFoundException) {
                     throw (InstanceNotFoundException)var2x;
                  } else if (var2x instanceof ReflectionException) {
                     throw (ReflectionException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
         return var3;
      } catch (PrivilegedActionException var7) {
         Exception var6 = var7.getException();
         if (var6 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var6;
         } else if (var6 instanceof ReflectionException) {
            throw (ReflectionException)var6;
         } else if (var6 instanceof RuntimeException) {
            throw (RuntimeException)var6;
         } else {
            throw new AssertionError(var6);
         }
      }
   }

   public void unregisterMBean(final ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException {
      try {
         AuthenticatedSubject var2 = this.getAuthenticatedSubject();
         var2.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var1;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                     public Object run() throws Exception {
                        JMXConnectorSubjectForwarder.this.getMBeanServer().unregisterMBean(var1x);
                        return null;
                     }
                  });
               } catch (PrivilegedActionException var3) {
                  Exception var2 = var3.getException();
                  if (var2 instanceof InstanceNotFoundException) {
                     throw (InstanceNotFoundException)var2;
                  } else if (var2 instanceof MBeanRegistrationException) {
                     throw (MBeanRegistrationException)var2;
                  } else {
                     throw var2;
                  }
               }
            }
         });
      } catch (PrivilegedActionException var3) {
         this.rethrowUnregisterException(var3);
      }

   }

   public void rethrowUnregisterException(PrivilegedActionException var1) throws InstanceNotFoundException, MBeanRegistrationException {
      Exception var2 = var1.getException();
      if (var2 instanceof InstanceNotFoundException) {
         throw (InstanceNotFoundException)var2;
      } else if (var2 instanceof NoAccessRuntimeException) {
         throw (NoAccessRuntimeException)var2;
      } else if (var2 instanceof MBeanRegistrationException) {
         throw (MBeanRegistrationException)var2;
      } else if (var2 instanceof RuntimeException) {
         throw (RuntimeException)var2;
      } else {
         throw new AssertionError(var2);
      }
   }

   public ObjectInstance registerMBean(final Object var1, final ObjectName var2) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      ObjectInstance var3 = null;

      try {
         AuthenticatedSubject var4 = this.getAuthenticatedSubject();
         var3 = (ObjectInstance)var4.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final Object var1x = var1;
                  final ObjectName var4 = var2;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInstance>() {
                     public ObjectInstance run() throws Exception {
                        return JMXConnectorSubjectForwarder.this.getMBeanServer().registerMBean(var1x, var4);
                     }
                  });
               } catch (PrivilegedActionException var3) {
                  Exception var2x = var3.getException();
                  if (var2x instanceof InstanceAlreadyExistsException) {
                     throw (InstanceAlreadyExistsException)var2x;
                  } else if (var2x instanceof MBeanRegistrationException) {
                     throw (MBeanRegistrationException)var2x;
                  } else if (var2x instanceof NotCompliantMBeanException) {
                     throw (NotCompliantMBeanException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
      } catch (PrivilegedActionException var5) {
         this.rethrowRegisterException(var5);
      }

      return var3;
   }

   public void rethrowRegisterException(PrivilegedActionException var1) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      Exception var2 = var1.getException();
      if (var2 instanceof InstanceAlreadyExistsException) {
         throw (InstanceAlreadyExistsException)var2;
      } else if (var2 instanceof NoAccessRuntimeException) {
         throw (NoAccessRuntimeException)var2;
      } else if (var2 instanceof MBeanRegistrationException) {
         throw (MBeanRegistrationException)var2;
      } else if (var2 instanceof NotCompliantMBeanException) {
         throw (NotCompliantMBeanException)var2;
      } else if (var2 instanceof RuntimeException) {
         throw (RuntimeException)var2;
      } else {
         throw new AssertionError(var2);
      }
   }

   public void setAttribute(final ObjectName var1, final Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
      try {
         AuthenticatedSubject var3 = this.getAuthenticatedSubject();
         var3.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var1;
                  final Attribute var4 = var2;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                     public Object run() throws Exception {
                        JMXConnectorSubjectForwarder.this.getMBeanServer().setAttribute(var1x, var4);
                        return null;
                     }
                  });
               } catch (PrivilegedActionException var3) {
                  Exception var2x = var3.getException();
                  if (var2x instanceof InstanceNotFoundException) {
                     throw (InstanceNotFoundException)var2x;
                  } else if (var2x instanceof MBeanException) {
                     throw (MBeanException)var2x;
                  } else if (var2x instanceof InvalidAttributeValueException) {
                     throw (InvalidAttributeValueException)var2x;
                  } else if (var2x instanceof AttributeNotFoundException) {
                     throw (AttributeNotFoundException)var2x;
                  } else if (var2x instanceof ReflectionException) {
                     throw (ReflectionException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
      } catch (PrivilegedActionException var5) {
         Exception var4 = var5.getException();
         if (var4 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var4;
         } else if (var4 instanceof AttributeNotFoundException) {
            throw (AttributeNotFoundException)var4;
         } else if (var4 instanceof InvalidAttributeValueException) {
            throw (InvalidAttributeValueException)var4;
         } else if (var4 instanceof MBeanException) {
            throw (MBeanException)var4;
         } else if (var4 instanceof ReflectionException) {
            throw (ReflectionException)var4;
         } else if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new AssertionError(var4);
         }
      }
   }

   public AttributeList setAttributes(final ObjectName var1, final AttributeList var2) throws InstanceNotFoundException, ReflectionException {
      AttributeList var3 = null;

      try {
         AuthenticatedSubject var4 = this.getAuthenticatedSubject();
         var3 = (AttributeList)var4.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var1;
                  final AttributeList var4 = var2;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<AttributeList>() {
                     public AttributeList run() throws Exception {
                        return JMXConnectorSubjectForwarder.this.getMBeanServer().setAttributes(var1x, var4);
                     }
                  });
               } catch (PrivilegedActionException var3) {
                  Exception var2x = var3.getException();
                  if (var2x instanceof InstanceNotFoundException) {
                     throw (InstanceNotFoundException)var2x;
                  } else if (var2x instanceof ReflectionException) {
                     throw (ReflectionException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
         return var3;
      } catch (PrivilegedActionException var6) {
         Exception var5 = var6.getException();
         if (var5 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var5;
         } else if (var5 instanceof ReflectionException) {
            throw (ReflectionException)var5;
         } else if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else {
            throw new AssertionError(var5);
         }
      }
   }

   public Object invoke(final ObjectName var1, final String var2, final Object[] var3, final String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException {
      Object var5 = null;
      AuthenticatedSubject var6 = this.getAuthenticatedSubject();

      try {
         var5 = var6.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var1;
                  final String var6 = var2;
                  final Object[] var3x = var3;
                  final String[] var4x = var4;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                     public Object run() throws Exception {
                        return JMXConnectorSubjectForwarder.this.getMBeanServer().invoke(var1x, var6, var3x, var4x);
                     }
                  });
               } catch (PrivilegedActionException var5) {
                  Exception var2x = var5.getException();
                  if (var2x instanceof InstanceNotFoundException) {
                     throw (InstanceNotFoundException)var2x;
                  } else if (var2x instanceof MBeanException) {
                     throw (MBeanException)var2x;
                  } else if (var2x instanceof ReflectionException) {
                     throw (ReflectionException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
         return var5;
      } catch (PrivilegedActionException var9) {
         Exception var8 = var9.getException();
         if (var8 instanceof MBeanException) {
            throw (MBeanException)var8;
         } else if (var8 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var8;
         } else if (var8 instanceof ReflectionException) {
            throw (ReflectionException)var8;
         } else if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         } else {
            throw new AssertionError(var8);
         }
      }
   }

   public void addNotificationListener(final ObjectName var1, final NotificationListener var2, final NotificationFilter var3, final Object var4) throws InstanceNotFoundException {
      AuthenticatedSubject var5 = this.getAuthenticatedSubject();
      if (var5 == null) {
         this.getMBeanServer().addNotificationListener(var1, var2, var3, var4);
      } else {
         try {
            var5.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  try {
                     final ObjectName var1x = var1;
                     final NotificationListener var6 = var2;
                     final NotificationFilter var3x = var3;
                     final Object var4x = var4;
                     return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                        public Object run() throws Exception {
                           JMXConnectorSubjectForwarder.this.getMBeanServer().addNotificationListener(var1x, var6, var3x, var4x);
                           return null;
                        }
                     });
                  } catch (PrivilegedActionException var5) {
                     Exception var2x = var5.getException();
                     if (var2x instanceof InstanceNotFoundException) {
                        throw (InstanceNotFoundException)var2x;
                     } else {
                        throw var2x;
                     }
                  }
               }
            });
         } catch (PrivilegedActionException var7) {
            this.rethrowAddNotificationListenerException(var1, var7);
         }

      }
   }

   public void rethrowAddNotificationListenerException(ObjectName var1, PrivilegedActionException var2) throws InstanceNotFoundException {
      Exception var3 = var2.getException();
      if (var3 instanceof NoAccessRuntimeException) {
         throw (NoAccessRuntimeException)var3;
      } else if (var3 instanceof InstanceNotFoundException) {
         throw (InstanceNotFoundException)var3;
      } else if (var3 instanceof RuntimeException) {
         throw (RuntimeException)var3;
      } else {
         throw new AssertionError(var3);
      }
   }

   public void addNotificationListener(final ObjectName var1, final ObjectName var2, final NotificationFilter var3, final Object var4) throws InstanceNotFoundException {
      AuthenticatedSubject var5 = this.getAuthenticatedSubject();

      try {
         var5.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var1;
                  final ObjectName var6 = var2;
                  final NotificationFilter var3x = var3;
                  final Object var4x = var4;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                     public Object run() throws Exception {
                        JMXConnectorSubjectForwarder.this.getMBeanServer().addNotificationListener(var1x, var6, var3x, var4x);
                        return null;
                     }
                  });
               } catch (PrivilegedActionException var5) {
                  Exception var2x = var5.getException();
                  if (var2x instanceof InstanceNotFoundException) {
                     throw (InstanceNotFoundException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
      } catch (PrivilegedActionException var7) {
         this.rethrowAddNotificationListenerException(var1, var7);
      }

   }

   public void removeNotificationListener(final ObjectName var1, final ObjectName var2) throws InstanceNotFoundException, ListenerNotFoundException {
      AuthenticatedSubject var3 = this.getAuthenticatedSubject();

      try {
         var3.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var1;
                  final ObjectName var4 = var2;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                     public Object run() throws Exception {
                        JMXConnectorSubjectForwarder.this.getMBeanServer().removeNotificationListener(var1x, var4);
                        return null;
                     }
                  });
               } catch (PrivilegedActionException var3) {
                  Exception var2x = var3.getException();
                  if (var2x instanceof InstanceNotFoundException) {
                     throw (InstanceNotFoundException)var2x;
                  } else if (var2x instanceof ListenerNotFoundException) {
                     throw (ListenerNotFoundException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
      } catch (PrivilegedActionException var5) {
         this.rethrowRemoveNotificationListenerException(var1, var5);
      }

   }

   public void rethrowRemoveNotificationListenerException(ObjectName var1, PrivilegedActionException var2) throws InstanceNotFoundException, ListenerNotFoundException {
      Exception var3 = var2.getException();
      if (var3 instanceof NoAccessRuntimeException) {
         throw (NoAccessRuntimeException)var3;
      } else if (var3 instanceof InstanceNotFoundException) {
         throw (InstanceNotFoundException)var3;
      } else if (var3 instanceof ListenerNotFoundException) {
         throw (ListenerNotFoundException)var3;
      } else if (var3 instanceof RuntimeException) {
         throw (RuntimeException)var3;
      } else {
         throw new AssertionError(var3);
      }
   }

   public void removeNotificationListener(final ObjectName var1, final ObjectName var2, final NotificationFilter var3, final Object var4) throws InstanceNotFoundException, ListenerNotFoundException {
      AuthenticatedSubject var5 = this.getAuthenticatedSubject();

      try {
         var5.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               try {
                  final ObjectName var1x = var1;
                  final ObjectName var6 = var2;
                  final NotificationFilter var3x = var3;
                  final Object var4x = var4;
                  return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                     public Object run() throws Exception {
                        JMXConnectorSubjectForwarder.this.getMBeanServer().removeNotificationListener(var1x, var6, var3x, var4x);
                        return null;
                     }
                  });
               } catch (PrivilegedActionException var5) {
                  Exception var2x = var5.getException();
                  if (var2x instanceof InstanceNotFoundException) {
                     throw (InstanceNotFoundException)var2x;
                  } else if (var2x instanceof ListenerNotFoundException) {
                     throw (ListenerNotFoundException)var2x;
                  } else {
                     throw var2x;
                  }
               }
            }
         });
      } catch (PrivilegedActionException var7) {
         this.rethrowRemoveNotificationListenerException(var1, var7);
      }

   }

   public void removeNotificationListener(final ObjectName var1, final NotificationListener var2) throws InstanceNotFoundException, ListenerNotFoundException {
      AuthenticatedSubject var3 = this.getAuthenticatedSubject();
      if (var3 == null) {
         this.getMBeanServer().removeNotificationListener(var1, var2);
      } else {
         try {
            var3.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  try {
                     final ObjectName var1x = var1;
                     final NotificationListener var4 = var2;
                     return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                        public Object run() throws Exception {
                           JMXConnectorSubjectForwarder.this.getMBeanServer().removeNotificationListener(var1x, var4);
                           return null;
                        }
                     });
                  } catch (PrivilegedActionException var3) {
                     Exception var2x = var3.getException();
                     if (var2x instanceof InstanceNotFoundException) {
                        throw (InstanceNotFoundException)var2x;
                     } else if (var2x instanceof ListenerNotFoundException) {
                        throw (ListenerNotFoundException)var2x;
                     } else {
                        throw var2x;
                     }
                  }
               }
            });
         } catch (PrivilegedActionException var5) {
            this.rethrowRemoveNotificationListenerException(var1, var5);
         }

      }
   }

   public void removeNotificationListener(final ObjectName var1, final NotificationListener var2, final NotificationFilter var3, final Object var4) throws InstanceNotFoundException, ListenerNotFoundException {
      AuthenticatedSubject var5 = this.getAuthenticatedSubject();
      if (var5 == null) {
         this.getMBeanServer().removeNotificationListener(var1, var2, var3, var4);
      } else {
         try {
            var5.doAs(KERNEL_ID, new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  try {
                     final ObjectName var1x = var1;
                     final NotificationListener var6 = var2;
                     final NotificationFilter var3x = var3;
                     final Object var4x = var4;
                     return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                        public Object run() throws Exception {
                           JMXConnectorSubjectForwarder.this.getMBeanServer().removeNotificationListener(var1x, var6, var3x, var4x);
                           return null;
                        }
                     });
                  } catch (PrivilegedActionException var5) {
                     Exception var2x = var5.getException();
                     if (var2x instanceof InstanceNotFoundException) {
                        throw (InstanceNotFoundException)var2x;
                     } else if (var2x instanceof ListenerNotFoundException) {
                        throw (ListenerNotFoundException)var2x;
                     } else {
                        throw var2x;
                     }
                  }
               }
            });
         } catch (PrivilegedActionException var7) {
            this.rethrowRemoveNotificationListenerException(var1, var7);
         }

      }
   }

   public final ClassLoaderRepository getClassLoaderRepository() {
      try {
         return (ClassLoaderRepository)AccessController.doPrivileged(new PrivilegedExceptionAction<ClassLoaderRepository>() {
            public ClassLoaderRepository run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().getClassLoaderRepository();
            }
         });
      } catch (PrivilegedActionException var3) {
         Exception var2 = var3.getException();
         throw new AssertionError(var2);
      }
   }

   public ClassLoader getClassLoaderFor(final ObjectName var1) throws InstanceNotFoundException {
      try {
         return (ClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction<ClassLoader>() {
            public ClassLoader run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().getClassLoaderFor(var1);
            }
         });
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         if (var3 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var3;
         } else {
            throw new AssertionError(var3);
         }
      }
   }

   public ClassLoader getClassLoader(final ObjectName var1) throws InstanceNotFoundException {
      try {
         return (ClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction<ClassLoader>() {
            public ClassLoader run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().getClassLoader(var1);
            }
         });
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         if (var3 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var3;
         } else {
            throw new AssertionError(var3);
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public final ObjectInputStream deserialize(final ObjectName var1, final byte[] var2) throws OperationsException {
      try {
         return (ObjectInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInputStream>() {
            public ObjectInputStream run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().deserialize(var1, var2);
            }
         });
      } catch (PrivilegedActionException var5) {
         Exception var4 = var5.getException();
         if (var4 instanceof OperationsException) {
            throw (OperationsException)var4;
         } else {
            throw new AssertionError(var4);
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public final ObjectInputStream deserialize(final String var1, final byte[] var2) throws OperationsException, ReflectionException {
      try {
         return (ObjectInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInputStream>() {
            public ObjectInputStream run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().deserialize(var1, var2);
            }
         });
      } catch (PrivilegedActionException var5) {
         Exception var4 = var5.getException();
         if (var4 instanceof OperationsException) {
            throw (OperationsException)var4;
         } else if (var4 instanceof ReflectionException) {
            throw (ReflectionException)var4;
         } else {
            throw new AssertionError(var4);
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public final ObjectInputStream deserialize(final String var1, final ObjectName var2, final byte[] var3) throws OperationsException, ReflectionException {
      try {
         return (ObjectInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInputStream>() {
            public ObjectInputStream run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().deserialize(var1, var2, var3);
            }
         });
      } catch (PrivilegedActionException var7) {
         Exception var5 = var7.getException();
         if (var5 instanceof OperationsException) {
            throw (OperationsException)var5;
         } else if (var5 instanceof ReflectionException) {
            throw (ReflectionException)var5;
         } else {
            throw new AssertionError(var5);
         }
      }
   }

   public Object instantiate(final String var1) throws ReflectionException, MBeanException {
      try {
         return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
            public Object run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().instantiate(var1);
            }
         });
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         if (var3 instanceof MBeanException) {
            throw (MBeanException)var3;
         } else if (var3 instanceof ReflectionException) {
            throw (ReflectionException)var3;
         } else {
            throw new AssertionError(var3);
         }
      }
   }

   public Object instantiate(final String var1, final ObjectName var2) throws ReflectionException, MBeanException, InstanceNotFoundException {
      try {
         return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
            public Object run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().instantiate(var1, var2);
            }
         });
      } catch (PrivilegedActionException var5) {
         Exception var4 = var5.getException();
         if (var4 instanceof MBeanException) {
            throw (MBeanException)var4;
         } else if (var4 instanceof ReflectionException) {
            throw (ReflectionException)var4;
         } else if (var4 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var4;
         } else {
            throw new AssertionError(var4);
         }
      }
   }

   public Object instantiate(final String var1, final Object[] var2, final String[] var3) throws ReflectionException, MBeanException {
      try {
         return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
            public Object run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().instantiate(var1, var2, var3);
            }
         });
      } catch (PrivilegedActionException var7) {
         Exception var5 = var7.getException();
         if (var5 instanceof MBeanException) {
            throw (MBeanException)var5;
         } else if (var5 instanceof ReflectionException) {
            throw (ReflectionException)var5;
         } else {
            throw new AssertionError(var5);
         }
      }
   }

   public Object instantiate(final String var1, final ObjectName var2, final Object[] var3, final String[] var4) throws ReflectionException, MBeanException, InstanceNotFoundException {
      try {
         return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
            public Object run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().instantiate(var1, var2, var3, var4);
            }
         });
      } catch (PrivilegedActionException var9) {
         Exception var6 = var9.getException();
         if (var6 instanceof MBeanException) {
            throw (MBeanException)var6;
         } else if (var6 instanceof ReflectionException) {
            throw (ReflectionException)var6;
         } else if (var6 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var6;
         } else {
            throw new AssertionError(var6);
         }
      }
   }

   public boolean isInstanceOf(final ObjectName var1, final String var2) throws InstanceNotFoundException {
      try {
         return (Boolean)AccessController.doPrivileged(new PrivilegedExceptionAction<Boolean>() {
            public Boolean run() throws Exception {
               return new Boolean(JMXConnectorSubjectForwarder.this.getMBeanServer().isInstanceOf(var1, var2));
            }
         });
      } catch (PrivilegedActionException var5) {
         Exception var4 = var5.getException();
         if (var4 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var4;
         } else {
            throw new AssertionError(var4);
         }
      }
   }

   public MBeanInfo getMBeanInfo(final ObjectName var1) throws InstanceNotFoundException, IntrospectionException, ReflectionException {
      try {
         return (MBeanInfo)AccessController.doPrivileged(new PrivilegedExceptionAction<MBeanInfo>() {
            public MBeanInfo run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().getMBeanInfo(var1);
            }
         });
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         if (var3 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var3;
         } else if (var3 instanceof IntrospectionException) {
            throw (IntrospectionException)var3;
         } else if (var3 instanceof ReflectionException) {
            throw (ReflectionException)var3;
         } else {
            throw new AssertionError(var3);
         }
      }
   }

   public String[] getDomains() {
      try {
         return (String[])AccessController.doPrivileged(new PrivilegedExceptionAction<String[]>() {
            public String[] run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().getDomains();
            }
         });
      } catch (PrivilegedActionException var3) {
         Exception var2 = var3.getException();
         throw new AssertionError(var2);
      }
   }

   public String getDefaultDomain() {
      try {
         return (String)AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {
            public String run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().getDefaultDomain();
            }
         });
      } catch (PrivilegedActionException var3) {
         Exception var2 = var3.getException();
         throw new AssertionError(var2);
      }
   }

   public Integer getMBeanCount() {
      try {
         return (Integer)AccessController.doPrivileged(new PrivilegedExceptionAction<Integer>() {
            public Integer run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().getMBeanCount();
            }
         });
      } catch (PrivilegedActionException var3) {
         Exception var2 = var3.getException();
         throw new AssertionError(var2);
      }
   }

   public boolean isRegistered(final ObjectName var1) {
      try {
         return (Boolean)AccessController.doPrivileged(new PrivilegedExceptionAction<Boolean>() {
            public Boolean run() throws Exception {
               return new Boolean(JMXConnectorSubjectForwarder.this.getMBeanServer().isRegistered(var1));
            }
         });
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         throw new AssertionError(var3);
      }
   }

   public Set<ObjectName> queryNames(final ObjectName var1, final QueryExp var2) {
      try {
         return (Set)AccessController.doPrivileged(new PrivilegedExceptionAction<Set<ObjectName>>() {
            public Set<ObjectName> run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().queryNames(var1, var2);
            }
         });
      } catch (PrivilegedActionException var5) {
         Exception var4 = var5.getException();
         throw new AssertionError(var4);
      }
   }

   public Set<ObjectInstance> queryMBeans(final ObjectName var1, final QueryExp var2) {
      try {
         return (Set)AccessController.doPrivileged(new PrivilegedExceptionAction<Set<ObjectInstance>>() {
            public Set<ObjectInstance> run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().queryMBeans(var1, var2);
            }
         });
      } catch (PrivilegedActionException var5) {
         Exception var4 = var5.getException();
         throw new AssertionError(var4);
      }
   }

   public ObjectInstance getObjectInstance(final ObjectName var1) throws InstanceNotFoundException {
      try {
         return (ObjectInstance)AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInstance>() {
            public ObjectInstance run() throws Exception {
               return JMXConnectorSubjectForwarder.this.getMBeanServer().getObjectInstance(var1);
            }
         });
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         if (var3 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var3;
         } else {
            throw new AssertionError(var3);
         }
      }
   }

   private AuthenticatedSubject getAuthenticatedSubject() {
      final AccessControlContext var1 = AccessController.getContext();
      if (var1 != null) {
         Subject var2 = (Subject)AccessController.doPrivileged(new PrivilegedAction<Subject>() {
            public Subject run() {
               return Subject.getSubject(var1);
            }
         });
         if (var2 != null) {
            AuthenticatedSubject var3 = AuthenticatedSubject.getFromSubject(var2);
            if (!AuthenticatedSubject.ANON.equals(var3) && !KERNEL_ID.equals(var3)) {
               return var3;
            } else {
               AuthenticatedSubject var4 = getAuthenticatedSubjectFromJMXContext();
               if (var4 != null && !AuthenticatedSubject.ANON.equals(var4)) {
                  return var4;
               } else {
                  AuthenticatedSubject var5 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
                  return var5;
               }
            }
         } else {
            return AuthenticatedSubject.ANON;
         }
      } else {
         return AuthenticatedSubject.ANON;
      }
   }

   private static AuthenticatedSubject getAuthenticatedSubjectFromJMXContext() {
      JMXContext var0 = JMXContextHelper.getJMXContext(false);
      if (var0 != null) {
         Subject var1 = var0.getSubject();
         if (var1 != null) {
            return AuthenticatedSubject.getFromSubject(var1);
         }
      }

      return null;
   }
}
