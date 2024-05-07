package weblogic.jms.extensions;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import weblogic.jms.client.ConnectionInternal;
import weblogic.jms.client.ConsumerInternal;
import weblogic.jms.client.ProducerInternal;
import weblogic.jms.client.SessionInternal;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSEditHelper;
import weblogic.jms.common.JMSException;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.runtime.JMSConnectionRuntimeMBean;
import weblogic.management.runtime.JMSConsumerRuntimeMBean;
import weblogic.management.runtime.JMSDestinationRuntimeMBean;
import weblogic.management.runtime.JMSProducerRuntimeMBean;
import weblogic.management.runtime.JMSRuntimeMBean;
import weblogic.management.runtime.JMSServerRuntimeMBean;
import weblogic.management.runtime.JMSSessionRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;

public class JMSRuntimeHelper {
   public static JMSConnectionRuntimeMBean getJMSConnectionRuntimeMBean(Context var0, Connection var1) throws JMSException {
      if (!(var1 instanceof ConnectionInternal)) {
         throw new JMSException("Unknown foreign connection");
      } else {
         ConnectionInternal var2 = (ConnectionInternal)var1;

         try {
            DomainRuntimeServiceMBean var3 = JMSEditHelper.getDomainRuntimeService(var0);
            ServerRuntimeMBean var10 = var3.lookupServerRuntime(var2.getWLSServerName());
            JMSRuntimeMBean var5 = var10.getJMSRuntime();
            JMSConnectionRuntimeMBean[] var6 = var5.getConnections();
            JMSConnectionRuntimeMBean var7 = null;

            for(int var8 = 0; var8 < var6.length; ++var8) {
               if (var2.getRuntimeMBeanName().equals(var6[var8].getName())) {
                  var7 = var6[var8];
                  break;
               }
            }

            if (var7 == null) {
               throw new JMSException("JMS Connection runtime mbean not found");
            } else {
               return var7;
            }
         } catch (Exception var9) {
            if (var9 instanceof JMSException) {
               throw (JMSException)var9;
            } else {
               JMSException var4 = new JMSException(var9.toString());
               var4.setLinkedException(var9);
               throw var4;
            }
         }
      }
   }

   public static JMSProducerRuntimeMBean getJMSMessageProducerRuntimeMBean(Context var0, MessageProducer var1) throws JMSException {
      if (!(var1 instanceof ProducerInternal)) {
         throw new JMSException("Unknown foreign message producer");
      } else {
         ProducerInternal var2 = (ProducerInternal)var1;

         try {
            DomainRuntimeServiceMBean var3 = JMSEditHelper.getDomainRuntimeService(var0);
            ServerRuntimeMBean var14 = var3.lookupServerRuntime(var2.getWLSServerName());
            JMSRuntimeMBean var5 = var14.getJMSRuntime();
            JMSConnectionRuntimeMBean[] var6 = var5.getConnections();
            JMSProducerRuntimeMBean var7 = null;

            for(int var8 = 0; var8 < var6.length; ++var8) {
               JMSSessionRuntimeMBean[] var9 = var6[var8].getSessions();

               for(int var10 = 0; var10 < var9.length; ++var10) {
                  JMSProducerRuntimeMBean[] var11 = var9[var10].getProducers();

                  for(int var12 = 0; var12 < var11.length; ++var12) {
                     if (var2.getRuntimeMBeanName().equals(var11[var12].getName())) {
                        var7 = var11[var12];
                        break;
                     }
                  }
               }
            }

            if (var7 == null) {
               throw new JMSException("JMS Prodcuer runtime mbean not found");
            } else {
               return var7;
            }
         } catch (Exception var13) {
            if (var13 instanceof JMSException) {
               throw (JMSException)var13;
            } else {
               JMSException var4 = new JMSException(var13.toString());
               var4.setLinkedException(var13);
               throw var4;
            }
         }
      }
   }

   public static JMSConsumerRuntimeMBean getJMSMessageConsumerRuntimeMBean(Context var0, MessageConsumer var1) throws JMSException {
      if (!(var1 instanceof ConsumerInternal)) {
         throw new JMSException("Unknown foreign message consumer");
      } else {
         ConsumerInternal var2 = (ConsumerInternal)var1;

         try {
            DomainRuntimeServiceMBean var3 = JMSEditHelper.getDomainRuntimeService(var0);
            ServerRuntimeMBean var14 = var3.lookupServerRuntime(var2.getWLSServerName());
            JMSRuntimeMBean var5 = var14.getJMSRuntime();
            JMSConnectionRuntimeMBean[] var6 = var5.getConnections();
            JMSConsumerRuntimeMBean var7 = null;

            for(int var8 = 0; var8 < var6.length; ++var8) {
               JMSSessionRuntimeMBean[] var9 = var6[var8].getSessions();

               for(int var10 = 0; var10 < var9.length; ++var10) {
                  JMSConsumerRuntimeMBean[] var11 = var9[var10].getConsumers();

                  for(int var12 = 0; var12 < var11.length; ++var12) {
                     if (var2.getRuntimeMBeanName().equals(var11[var12].getName())) {
                        var7 = var11[var12];
                        break;
                     }
                  }
               }
            }

            if (var7 == null) {
               throw new JMSException("JMS Consumer runtime mbean not found");
            } else {
               return var7;
            }
         } catch (Exception var13) {
            if (var13 instanceof JMSException) {
               throw (JMSException)var13;
            } else {
               JMSException var4 = new JMSException(var13.toString());
               var4.setLinkedException(var13);
               throw var4;
            }
         }
      }
   }

   public static JMSSessionRuntimeMBean getJMSSessionRuntimeMBean(Context var0, Session var1) throws JMSException {
      if (!(var1 instanceof SessionInternal)) {
         throw new JMSException("Unknown foreign customerSession");
      } else {
         SessionInternal var2 = (SessionInternal)var1;

         try {
            DomainRuntimeServiceMBean var3 = JMSEditHelper.getDomainRuntimeService(var0);
            ServerRuntimeMBean var12 = var3.lookupServerRuntime(var2.getWLSServerName());
            JMSRuntimeMBean var5 = var12.getJMSRuntime();
            JMSConnectionRuntimeMBean[] var6 = var5.getConnections();
            JMSSessionRuntimeMBean var7 = null;

            for(int var8 = 0; var8 < var6.length; ++var8) {
               JMSSessionRuntimeMBean[] var9 = var6[var8].getSessions();

               for(int var10 = 0; var10 < var9.length; ++var10) {
                  if (var2.getRuntimeMBeanName().equals(var9[var10].getName())) {
                     var7 = var9[var10];
                     break;
                  }
               }
            }

            if (var7 == null) {
               throw new JMSException("JMS Session runtime mbean not found");
            } else {
               return var7;
            }
         } catch (Exception var11) {
            if (var11 instanceof JMSException) {
               throw (JMSException)var11;
            } else {
               JMSException var4 = new JMSException(var11.toString());
               var4.setLinkedException(var11);
               throw var4;
            }
         }
      }
   }

   public static JMSServerRuntimeMBean getJMSServerRuntimeMBean(Context var0, Destination var1) throws JMSException {
      if (!(var1 instanceof DestinationImpl)) {
         throw new JMSException("Unknown foreign destination");
      } else {
         DestinationImpl var2 = (DestinationImpl)var1;
         String var3 = var2.getServerName();

         try {
            JMSServerRuntimeMBean var4 = null;
            DomainRuntimeServiceMBean var13 = JMSEditHelper.getDomainRuntimeService(var0);
            ServerRuntimeMBean[] var6 = var13.getServerRuntimes();

            for(int var7 = 0; var7 < var6.length; ++var7) {
               ServerRuntimeMBean var8 = var6[var7];
               JMSRuntimeMBean var9 = var8.getJMSRuntime();
               JMSServerRuntimeMBean[] var10 = var9.getJMSServers();

               for(int var11 = 0; var11 < var10.length; ++var11) {
                  if (var3.equals(var10[var11].getName())) {
                     var4 = var10[var11];
                     break;
                  }
               }
            }

            if (var4 == null) {
               throw new JMSException("JMS Server runtime mbean not found");
            } else {
               return var4;
            }
         } catch (Exception var12) {
            if (var12 instanceof JMSException) {
               throw (JMSException)var12;
            } else {
               JMSException var5 = new JMSException(var12.toString());
               var5.setLinkedException(var12);
               throw var5;
            }
         }
      }
   }

   public static JMSDestinationRuntimeMBean getJMSDestinationRuntimeMBean(Context var0, String var1, String var2) throws JMSException {
      try {
         JMSDestinationRuntimeMBean var3 = null;
         DomainRuntimeServiceMBean var14 = JMSEditHelper.getDomainRuntimeService(var0);
         ServerRuntimeMBean[] var5 = var14.getServerRuntimes();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            ServerRuntimeMBean var7 = var5[var6];
            JMSRuntimeMBean var8 = var7.getJMSRuntime();
            JMSServerRuntimeMBean[] var9 = var8.getJMSServers();

            for(int var10 = 0; var10 < var9.length; ++var10) {
               JMSDestinationRuntimeMBean[] var11 = var9[var10].getDestinations();

               for(int var12 = 0; var12 < var11.length; ++var12) {
                  if (var2.equals(var11[var12].getName())) {
                     var3 = var11[var12];
                     break;
                  }
               }
            }
         }

         if (var3 == null) {
            throw new JMSException("JMS Destination runtime mbean not found");
         } else {
            return var3;
         }
      } catch (Exception var13) {
         if (var13 instanceof JMSException) {
            throw (JMSException)var13;
         } else {
            JMSException var4 = new JMSException(var13.toString());
            var4.setLinkedException(var13);
            throw var4;
         }
      }
   }

   public static JMSDestinationRuntimeMBean getJMSDestinationRuntimeMBean(Context var0, Destination var1) throws JMSException {
      if (!(var1 instanceof DestinationImpl)) {
         throw new JMSException("Unknown foreign destination");
      } else {
         DestinationImpl var2 = (DestinationImpl)var1;

         try {
            JMSDestinationRuntimeMBean var3 = null;
            DomainRuntimeServiceMBean var14 = JMSEditHelper.getDomainRuntimeService(var0);
            ServerRuntimeMBean[] var5 = var14.getServerRuntimes();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               ServerRuntimeMBean var7 = var5[var6];
               JMSRuntimeMBean var8 = var7.getJMSRuntime();
               JMSServerRuntimeMBean[] var9 = var8.getJMSServers();

               for(int var10 = 0; var10 < var9.length; ++var10) {
                  JMSDestinationRuntimeMBean[] var11 = var9[var10].getDestinations();

                  for(int var12 = 0; var12 < var11.length; ++var12) {
                     if (var2.getName().equals(var11[var12].getName())) {
                        var3 = var11[var12];
                        break;
                     }
                  }
               }
            }

            if (var3 == null) {
               throw new JMSException("JMS Destination runtime mbean not found");
            } else {
               return var3;
            }
         } catch (Exception var13) {
            if (var13 instanceof JMSException) {
               throw (JMSException)var13;
            } else {
               JMSException var4 = new JMSException(var13.toString());
               var4.setLinkedException(var13);
               throw var4;
            }
         }
      }
   }

   public static JMSServerRuntimeMBean getJMSServerRuntimeMBean(Context var0, String var1) throws JMSException {
      try {
         JMSServerRuntimeMBean var2 = null;
         DomainRuntimeServiceMBean var11 = JMSEditHelper.getDomainRuntimeService(var0);
         ServerRuntimeMBean[] var4 = var11.getServerRuntimes();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            ServerRuntimeMBean var6 = var4[var5];
            JMSRuntimeMBean var7 = var6.getJMSRuntime();
            JMSServerRuntimeMBean[] var8 = var7.getJMSServers();

            for(int var9 = 0; var9 < var8.length; ++var9) {
               if (var1.equals(var8[var9].getName())) {
                  var2 = var8[var9];
                  break;
               }
            }
         }

         if (var2 == null) {
            throw new JMSException("JMS Server runtime mbean not found");
         } else {
            return var2;
         }
      } catch (Exception var10) {
         if (var10 instanceof JMSException) {
            throw (JMSException)var10;
         } else {
            JMSException var3 = new JMSException(var10.toString());
            var3.setLinkedException(var10);
            throw var3;
         }
      }
   }

   public String oldJMSMessageIDToNew(String var1, long var2) throws JMSException {
      try {
         String var4 = var1.substring(0, 4);
         long var5 = new Long(var1.substring(4));
         int var7 = (int)(var5 >>> 32);
         int var8 = (int)(var5 - ((long)var7 << 32));
         return var4 + "<" + var7 + "." + var2 + "." + var8 + ">";
      } catch (Throwable var9) {
         throw new JMSException("Invalid JMSMessageID, failed to convert it into 6.0 format");
      }
   }

   public String newJMSMessageIDToOld(String var1) throws JMSException {
      try {
         String var2 = var1.substring(0, 4);
         int var3 = var1.indexOf(".", 5);
         int var4 = new Integer(var1.substring(5, var3));
         int var5 = var1.indexOf(".", var3 + 1);
         var3 = var1.indexOf(".", var5 + 1);
         int var6 = new Integer(var1.substring(var5 + 1, var3));
         long var7 = (long)var4;
         var7 = (var7 << 32) + (long)var6;
         return var2 + var7;
      } catch (Throwable var9) {
         throw new JMSException("Invalid JMSMessageID, failed to convert it into pre-6.0 format");
      }
   }
}
