package weblogic.jms.safclient.admin;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.jms.JMSException;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import weblogic.jms.common.SecHelper;
import weblogic.jms.safclient.ClientSAFDelegate;
import weblogic.jms.safclient.agent.AgentManager;
import weblogic.jms.safclient.agent.DestinationImpl;
import weblogic.jms.safclient.agent.internal.Agent;
import weblogic.jms.safclient.agent.internal.ErrorHandler;
import weblogic.jms.safclient.agent.internal.RemoteContext;
import weblogic.jms.safclient.agent.internal.RuntimeHandlerImpl;
import weblogic.jms.safclient.jms.ConnectionFactoryImpl;
import weblogic.jms.safclient.jndi.ContextImpl;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.Sequence;

public final class ConfigurationUtils {
   private static final String ROOT_TAG = "weblogic-client-jms";
   private static final String STORE_TAG = "persistent-store";
   private static final String DIRPATH_TAG = "directory-path";
   private static final String SWPOLICY_TAG = "synchronous-write-policy";
   private static final String CF_TAG = "connection-factory";
   private static final String JNDI_TAG = "jndi-name";
   private static final String LOCAL_JNDI_TAG = "local-jndi-name";
   private static final String REMOTE_JNDI_TAG = "remote-jndi-name";
   private static final String NAME_ATT = "name";
   private static final String DDP_TAG = "default-delivery-params";
   private static final String DFDM_TAG = "default-delivery-mode";
   private static final String DFTD_TAG = "default-time-to-deliver";
   private static final String DFTTL_TAG = "default-time-to-live";
   private static final String DFPRI_TAG = "default-priority";
   private static final String DFRED_TAG = "default-redelivery-delay";
   private static final String DFSND_TAG = "send-timeout";
   private static final String DFCMP_TAG = "default-compression-threshold";
   private static final String DFUOO_TAG = "default-unit-of-order";
   private static final String CLIENT_TAG = "client-params";
   private static final String CID_TAG = "client-id";
   private static final String MM_TAG = "messages-maximum";
   private static final String MOR_TAG = "multicast-overrun-policy";
   private static final String SEC_TAG = "security-params";
   private static final String ATT_TAG = "attach-jmsx-user-id";
   private static final String SAFGROUP_TAG = "saf-imported-destinations";
   private static final String QUEUE_TAG = "saf-queue";
   private static final String TOPIC_TAG = "saf-topic";
   private static final String AGENT_TAG = "saf-agent";
   private static final String BYMAX_TAG = "bytes-maximum";
   private static final String MMS_TAG = "maximum-message-size";
   private static final String MBS_TAG = "message-buffer-size";
   private static final String SRC_TAG = "saf-remote-context";
   private static final String CMP_TAG = "compression-threshold";
   private static final String LOG_TAG = "saf-login-context";
   private static final String URL_TAG = "loginURL";
   private static final String UNAME_TAG = "username";
   private static final String PW_TAG = "password-encrypted";
   private static final String RDB_TAG = "default-retry-delay-base";
   private static final String RDM_TAG = "default-retry-delay-maximum";
   private static final String RDX_TAG = "default-retry-delay-multiplier";
   private static final String WIN_TAG = "window-size";
   private static final String LOE_TAG = "logging-enabled";
   private static final String WINI_TAG = "window-interval";
   private static final String SAFG_TAG = "saf-imported-destinations";
   private static final String NONQ_TAG = "non-persistent-qos";
   private static final String ERH_TAG = "saf-error-handling";
   private static final String POL_TAG = "policy";
   private static final String LOGF_TAG = "log-format";
   private static final String SED_TAG = "saf-error-destination";
   private static final String SEHG_TAG = "saf-error-handling";
   private static final String DEFAULT_CF_NAME = "weblogic.jms.safclient.ConnectionFactory";
   private static final String PREFIX_NAME = "ClientSAF_";
   public static final String EXACTLY_ONCE = "Exactly-Once";
   private static final String AT_LEAST_ONCE = "At-Least-Once";
   private static final String AT_MOST_ONCE = "At-Most-Once";

   private static final String getNestedSingleField(Element var0, String var1) {
      NodeList var2 = var0.getElementsByTagName(var1);
      if (var2 != null && var2.getLength() > 0) {
         Node var3 = var2.item(0);
         return getTextContent_14(var3);
      } else {
         return null;
      }
   }

   public static String getTextContent_14(Node var0, boolean var1) {
      if (!var0.hasChildNodes()) {
         return "";
      } else {
         Pattern var2 = Pattern.compile("[^ \t\r\n]");
         StringBuffer var3 = new StringBuffer();

         for(Node var4 = var0.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
            if (!(var4 instanceof Text) && !(var4 instanceof CDATASection)) {
               var3.append(getTextContent_14(var4, var1));
            } else {
               String var5 = var4.getNodeValue();
               if (var2.matcher(var5).find()) {
                  var3.append(var5);
               }
            }
         }

         return var3.toString();
      }
   }

   public static String getTextContent_14(Node var0) {
      if (!(var0 instanceof Document) && !(var0 instanceof DocumentType) && !(var0 instanceof Notation)) {
         if (!(var0 instanceof Text) && !(var0 instanceof CDATASection) && !(var0 instanceof Comment) && !(var0 instanceof ProcessingInstruction)) {
            return getTextContent_14(var0, true);
         } else {
            Pattern var1 = Pattern.compile("[^ \t\r\n]");
            String var2 = var0.getNodeValue();
            return var1.matcher(var2).find() ? var2 : "";
         }
      } else {
         return null;
      }
   }

   public static PersistentStoreBean getPersistentStore(Document var0) throws JMSException {
      PersistentStoreBeanImpl var1 = new PersistentStoreBeanImpl();
      NodeList var2 = var0.getElementsByTagName("weblogic-client-jms");
      if (var2.getLength() != 1) {
         throw new JMSException("This document must contain a root node of weblogic-client-jms");
      } else {
         Element var3 = (Element)var2.item(0);
         NodeList var4 = var3.getElementsByTagName("persistent-store");
         if (var4 != null && var4.getLength() > 0) {
            Element var5 = (Element)var4.item(0);
            String var6 = getNestedSingleField(var5, "directory-path");
            if (var6 != null) {
               var1.setStoreDirectory(var6);
            }

            var6 = getNestedSingleField(var5, "synchronous-write-policy");
            if (var6 != null) {
               var1.setPolicy(var6);
            }
         }

         return var1;
      }
   }

   private static void addToJNDIMap(HashMap var0, String var1, Object var2) throws JMSException {
      if (var1 != null) {
         if (var0.containsKey(var1)) {
            throw new JMSException(var1 + " is already bound into JNDI");
         } else {
            var0.put(var1, var2);
         }
      }
   }

   public static void doJNDIConnectionFactories(Document var0, ClientSAFDelegate var1, HashMap var2) throws JMSException {
      ConnectionFactoryImpl var3 = new ConnectionFactoryImpl("weblogic.jms.safclient.ConnectionFactory", var1);
      addToJNDIMap(var2, "weblogic.jms.safclient.ConnectionFactory", var3);
      NodeList var4 = var0.getElementsByTagName("weblogic-client-jms");
      if (var4.getLength() != 1) {
         throw new JMSException("This document must contain a root node of weblogic-client-jms");
      } else {
         Element var5 = (Element)var4.item(0);
         NodeList var6 = var5.getElementsByTagName("connection-factory");
         if (var6 != null && var6.getLength() > 0) {
            for(int var7 = 0; var7 < var6.getLength(); ++var7) {
               Element var8 = (Element)var6.item(var7);
               String var9 = var8.getAttribute("name");
               if (var9 == null) {
                  throw new JMSException("A connection factory does not have a name attribute");
               }

               String var10 = getNestedSingleField(var8, "jndi-name");
               String var11 = getNestedSingleField(var8, "local-jndi-name");
               if (var10 != null || var11 != null) {
                  var3 = new ConnectionFactoryImpl(var9, var1);
                  NodeList var12 = var8.getElementsByTagName("default-delivery-params");
                  if (var12 != null && var12.getLength() > 0) {
                     Element var13 = (Element)var12.item(0);
                     String var14 = getNestedSingleField(var13, "default-delivery-mode");
                     if (var14 != null) {
                        var3.setDefaultDeliveryMode(var14);
                     }

                     var14 = getNestedSingleField(var13, "default-time-to-deliver");
                     if (var14 != null) {
                        var3.setDefaultTimeToDeliver(var14);
                     }

                     var14 = getNestedSingleField(var13, "default-time-to-live");
                     if (var14 != null) {
                        var3.setDefaultTimeToLive(Long.parseLong(var14));
                     }

                     var14 = getNestedSingleField(var13, "default-priority");
                     if (var14 != null) {
                        var3.setDefaultPriority(Integer.parseInt(var14));
                     }

                     var14 = getNestedSingleField(var13, "default-redelivery-delay");
                     if (var14 != null) {
                        var3.setDefaultRedeliveryDelay(Long.parseLong(var14));
                     }

                     var14 = getNestedSingleField(var13, "send-timeout");
                     if (var14 != null) {
                        var3.setSendTimeout(Long.parseLong(var14));
                     }

                     var14 = getNestedSingleField(var13, "default-compression-threshold");
                     if (var14 != null) {
                        var3.setDefaultCompressionThreshold(Integer.parseInt(var14));
                     }

                     var14 = getNestedSingleField(var13, "default-unit-of-order");
                     if (var14 != null) {
                        var3.setDefaultUnitOfOrder(var14);
                     }
                  }

                  NodeList var18 = var8.getElementsByTagName("client-params");
                  if (var18 != null && var18.getLength() > 0) {
                     Element var20 = (Element)var18.item(0);
                     String var15 = getNestedSingleField(var20, "client-id");
                     if (var15 != null) {
                        var3.setClientId(var15);
                     }

                     var15 = getNestedSingleField(var20, "messages-maximum");
                     if (var15 != null) {
                        var3.setMessagesMaximum(Integer.parseInt(var15));
                     }

                     var15 = getNestedSingleField(var20, "multicast-overrun-policy");
                     if (var15 != null) {
                        var3.setMulticastOverrunPolicy(var15);
                     }
                  }

                  NodeList var21 = var8.getElementsByTagName("security-params");
                  if (var21 != null && var21.getLength() > 0) {
                     Element var19 = (Element)var21.item(0);
                     String var16 = getNestedSingleField(var19, "attach-jmsx-user-id");
                     if (var16 != null) {
                        boolean var17 = var16.equalsIgnoreCase("true");
                        var3.setAttachJMSXUserId(var17);
                     }
                  }

                  addToJNDIMap(var2, var10, var3);
                  addToJNDIMap(var2, var11, var3);
               }
            }

         }
      }
   }

   private static void doDestinations(String var0, NodeList var1, boolean var2, HashMap var3, HashMap var4) throws JMSException {
      for(int var5 = 0; var5 < var1.getLength(); ++var5) {
         Element var6 = (Element)var1.item(var5);
         String var7 = var6.getAttribute("name");
         if (var7 == null) {
            throw new JMSException("A saf destination does not have a name attribute");
         }

         DestinationImpl var8 = new DestinationImpl(var0, var7, var2);
         var3.put(var7, var8);
         String var9 = getNestedSingleField(var6, "local-jndi-name");
         if (var9 == null) {
            var9 = getNestedSingleField(var6, "remote-jndi-name");
         }

         if (var9 == null) {
            throw new JMSException("The saf destination " + var7 + " in saf group " + var0 + " does not have a JNDI name");
         }

         addToJNDIMap(var4, var9, var8);
      }

   }

   public static void doJNDIDestinations(Document var0, HashMap var1, HashMap var2) throws JMSException {
      NodeList var3 = var0.getElementsByTagName("weblogic-client-jms");
      if (var3.getLength() != 1) {
         throw new JMSException("This document must contain a root node of weblogic-client-jms");
      } else {
         Element var4 = (Element)var3.item(0);
         NodeList var5 = var4.getElementsByTagName("saf-imported-destinations");
         if (var5 != null && var5.getLength() > 0) {
            for(int var6 = 0; var6 < var5.getLength(); ++var6) {
               Element var7 = (Element)var5.item(var6);
               String var8 = var7.getAttribute("name");
               if (var8 == null) {
                  throw new JMSException("A saf imported destination group does not have a name attribute");
               }

               HashMap var9 = new HashMap();
               var1.put(var8, var9);
               NodeList var10 = var7.getElementsByTagName("saf-queue");
               if (var10 != null || var10.getLength() > 0) {
                  doDestinations(var8, var10, true, var9, var2);
               }

               NodeList var11 = var7.getElementsByTagName("saf-topic");
               if (var11 != null || var11.getLength() > 0) {
                  doDestinations(var8, var11, false, var9, var2);
               }
            }

         }
      }
   }

   public static void doAgent(Document var0, Agent var1) throws JMSException {
      NodeList var2 = var0.getElementsByTagName("weblogic-client-jms");
      if (var2.getLength() != 1) {
         throw new JMSException("This document must contain a root node of weblogic-client-jms");
      } else {
         Element var3 = (Element)var2.item(0);
         NodeList var4 = var3.getElementsByTagName("saf-agent");
         if (var4 != null && var4.getLength() > 0) {
            Element var5 = (Element)var4.item(0);
            String var6 = getNestedSingleField(var5, "bytes-maximum");
            if (var6 != null) {
               var1.setBytesMaximum(Long.parseLong(var6));
            }

            var6 = getNestedSingleField(var5, "messages-maximum");
            if (var6 != null) {
               var1.setMessagesMaximum(Long.parseLong(var6));
            }

            var6 = getNestedSingleField(var5, "maximum-message-size");
            if (var6 != null) {
               var1.setMaximumMessageSize(Integer.parseInt(var6));
            }

            var6 = getNestedSingleField(var5, "message-buffer-size");
            if (var6 != null) {
               var1.setMessageBufferSize((long)Integer.parseInt(var6));
            }

            var6 = getNestedSingleField(var5, "default-retry-delay-base");
            if (var6 != null) {
               var1.setDefaultRetryDelayBase(Long.parseLong(var6));
            }

            var6 = getNestedSingleField(var5, "default-retry-delay-maximum");
            if (var6 != null) {
               var1.setDefaultRetryDelayMaximum(Long.parseLong(var6));
            }

            var6 = getNestedSingleField(var5, "default-retry-delay-multiplier");
            if (var6 != null) {
               var1.setDefaultRetryDelayMultiplier(Double.parseDouble(var6));
            }

            var6 = getNestedSingleField(var5, "window-size");
            if (var6 != null) {
               var1.setWindowSize(Integer.parseInt(var6));
            }

            var6 = getNestedSingleField(var5, "logging-enabled");
            if (var6 != null) {
               boolean var7 = var6.equalsIgnoreCase("true");
               var1.setLoggingEnabled(var7);
            }

            var6 = getNestedSingleField(var5, "window-interval");
            if (var6 != null) {
               var1.setWindowInterval(Integer.parseInt(var6));
            }

         }
      }
   }

   public static void doRemoteContexts(Document var0, Agent var1, HashMap var2, char[] var3) throws JMSException {
      NodeList var4 = var0.getElementsByTagName("weblogic-client-jms");
      if (var4.getLength() != 1) {
         throw new JMSException("This document must contain a root node of weblogic-client-jms");
      } else {
         Element var5 = (Element)var4.item(0);
         NodeList var6 = var5.getElementsByTagName("saf-remote-context");
         if (var6 != null && var6.getLength() > 0) {
            int var7 = var6.getLength();

            for(int var8 = 0; var8 < var7; ++var8) {
               Element var9 = (Element)var6.item(var8);
               String var10 = var9.getAttribute("name");
               if (var10 == null) {
                  throw new JMSException("A saf remote context does not have a name attribute");
               }

               RemoteContext var11 = new RemoteContext(var10);
               String var12 = getNestedSingleField(var9, "compression-threshold");
               if (var12 != null) {
                  var11.setCompressionThreshold(Integer.parseInt(var12));
               }

               NodeList var13 = var9.getElementsByTagName("saf-login-context");
               if (var13 != null && var13.getLength() > 0) {
                  Element var14 = (Element)var13.item(0);
                  var12 = getNestedSingleField(var14, "loginURL");
                  if (var12 != null) {
                     var11.setLoginURL(var12);
                  }

                  var12 = getNestedSingleField(var14, "username");
                  if (var12 != null) {
                     var11.setUsername(var12);
                  }

                  var12 = getNestedSingleField(var14, "password-encrypted");
                  if (var12 != null) {
                     char[] var15;
                     try {
                        var15 = SecHelper.decryptString(var3, var12);
                     } catch (GeneralSecurityException var17) {
                        throw new weblogic.jms.common.JMSException("Invalid password key to unlock the passwords in the configuration file", var17);
                     } catch (IOException var18) {
                        throw new weblogic.jms.common.JMSException(var18);
                     }

                     var11.setPassword(new String(var15));

                     for(int var16 = 0; var16 < var15.length; ++var16) {
                        var15[var16] = 'x';
                     }
                  }
               }

               var11.setRetryDelayBase(var1.getDefaultRetryDelayBase());
               var11.setRetryDelayMaximum(var1.getDefaultRetryDelayMaximum());
               var11.setRetryDelayMultiplier(var1.getDefaultRetryDelayMultiplier());
               var11.setWindowSize(var1.getWindowSize());
               var11.setWindowInterval(var1.getWindowInterval());
               var2.put(var10, var11);
            }

         }
      }
   }

   public static void doErrorHandlers(Document var0, HashMap var1) throws JMSException {
      NodeList var2 = var0.getElementsByTagName("weblogic-client-jms");
      if (var2.getLength() != 1) {
         throw new JMSException("This document must contain a root node of weblogic-client-jms");
      } else {
         Element var3 = (Element)var2.item(0);
         NodeList var4 = var3.getElementsByTagName("saf-error-handling");
         if (var4 != null && var4.getLength() > 0) {
            int var5 = var4.getLength();

            for(int var6 = 0; var6 < var5; ++var6) {
               Element var7 = (Element)var4.item(var6);
               String var8 = var7.getAttribute("name");
               if (var8 == null) {
                  throw new JMSException("A saf remote context does not have a name attribute");
               }

               ErrorHandler var9 = new ErrorHandler(var8);
               String var10 = getNestedSingleField(var7, "policy");
               if (var10 != null) {
                  var9.setPolicy(var10);
               }

               var10 = getNestedSingleField(var7, "log-format");
               if (var10 != null) {
                  var9.setLogFormat(var10);
               }

               var10 = getNestedSingleField(var7, "log-format");
               if (var10 != null) {
                  var9.setLogFormat(var10);
               }

               var10 = getNestedSingleField(var7, "saf-error-destination");
               if (var10 != null) {
                  var9.setErrorDestinationName(var10);
               }

               var1.put(var8, var9);
            }

         }
      }
   }

   private static int qosStringToInt(String var0) {
      if ("Exactly-Once".equals(var0)) {
         return 2;
      } else {
         return "At-Least-Once".equals(var0) ? 3 : 1;
      }
   }

   private static void doImportedDestination(NodeList var0, String var1, Agent var2, ContextImpl var3, RemoteContext var4, ErrorHandler var5, HashMap var6) throws JMSException {
      int var7 = var0.getLength();

      for(int var8 = 0; var8 < var7; ++var8) {
         Element var9 = (Element)var0.item(var8);
         String var10 = var9.getAttribute("name");
         if (var10 == null) {
            throw new JMSException("A saf imported destination does not have a name attribute");
         }

         String var11 = getNestedSingleField(var9, "remote-jndi-name");
         if (var11 == null) {
            throw new JMSException("The remote JNDI name field is not set for destination " + var10 + " in saf group " + var1);
         }

         String var12 = getNestedSingleField(var9, "non-persistent-qos");
         if (var12 == null) {
            var12 = "At-Most-Once";
         }

         String var13 = AgentManager.constructDestinationName(var1, var10);
         String var14 = getNestedSingleField(var9, "saf-error-handling");
         ErrorHandler var15 = var5;
         if (var14 != null) {
            var15 = (ErrorHandler)var6.get(var14);
            if (var15 == null) {
               throw new JMSException("There is no error handler named " + var14 + " in SAF destination " + var13);
            }
         }

         Queue var16 = var2.addConfiguredDestination(var13);
         DestinationImpl var17 = var3.getDestination(var1, var10);
         if (var17 == null) {
            throw new JMSException("Could not find the configuration destination " + var13);
         }

         var17.setKernelQueue(var16);
         var17.setSequenceName(getLatestSequenceName(getBaseSequenceName(var13), var16));
         var17.setNonPersistentQOS(var12);
         var17.setLoggingEnabled(var2.isLoggingEnabled());
         var17.setErrorHandler(var15);
         var4.addForwarder(var2.getPersistentStore(), var2.getAsyncPushWorkManager(), new RuntimeHandlerImpl(var2.getName(), var10, var4.getName()), var16, var11, qosStringToInt(var12));
      }

   }

   public static void doImportedDestinationGroup(Document var0, HashMap var1, HashMap var2, Agent var3, ContextImpl var4) throws JMSException {
      NodeList var5 = var0.getElementsByTagName("weblogic-client-jms");
      if (var5.getLength() != 1) {
         throw new JMSException("This document must contain a root node of weblogic-client-jms");
      } else {
         Element var6 = (Element)var5.item(0);
         NodeList var7 = var6.getElementsByTagName("saf-imported-destinations");
         if (var7 != null && var7.getLength() > 0) {
            int var8 = var7.getLength();

            for(int var9 = 0; var9 < var8; ++var9) {
               Element var10 = (Element)var7.item(var9);
               String var11 = var10.getAttribute("name");
               if (var11 == null) {
                  throw new JMSException("A saf imported destination group does not have a name attribute");
               }

               String var12 = getNestedSingleField(var10, "saf-remote-context");
               if (var12 != null) {
                  RemoteContext var13 = (RemoteContext)var1.get(var12);
                  if (var13 == null) {
                     throw new JMSException("There is no remote context of name " + var12 + " in saf destination group " + var11);
                  }

                  String var14 = getNestedSingleField(var10, "saf-error-handling");
                  ErrorHandler var15 = null;
                  if (var14 != null) {
                     var15 = (ErrorHandler)var2.get(var14);
                     if (var15 == null) {
                        throw new JMSException("There is no error handler of name " + var14 + " in saf destination group " + var11);
                     }
                  }

                  NodeList var16 = var10.getElementsByTagName("saf-queue");
                  if (var16 != null && var16.getLength() > 0) {
                     doImportedDestination(var16, var11, var3, var4, var13, var15, var2);
                  }

                  NodeList var17 = var10.getElementsByTagName("saf-topic");
                  if (var17 != null && var17.getLength() > 0) {
                     doImportedDestination(var17, var11, var3, var4, var13, var15, var2);
                  }
               }
            }

         }
      }
   }

   public static void resolveErrorDestinations(HashMap var0, ContextImpl var1) throws JMSException {
      Iterator var2 = var0.keySet().iterator();

      while(var2.hasNext()) {
         ErrorHandler var3 = (ErrorHandler)var0.get(var2.next());
         String var4 = var3.getErrorDestinationName();
         if (var4 != null) {
            int var5 = var1.howManyDestinationsWithThisName(var4);
            if (var5 > 1) {
               throw new JMSException("There are " + var5 + " SAF destinations with the name " + var4 + ".  Hence a destination with that name cannot be used as an error destination");
            }

            if (var5 < 1) {
               throw new JMSException("No error destination of name " + var4 + " was found");
            }

            DestinationImpl var6 = var1.getDestination(var4);
            var3.setErrorDestination(var6);
         }
      }

   }

   private static String versionedName(String var0) {
      SimpleDateFormat var1 = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
      return var0 + var1.format(new Date(System.currentTimeMillis()));
   }

   private static String getBaseSequenceName(String var0) {
      return "ClientSAF_" + var0 + "@";
   }

   private static String getLatestSequenceName(String var0, Queue var1) {
      Collection var2 = var1.getSequences();
      if (var2 != null && var2.size() != 0) {
         Iterator var3 = var2.iterator();
         String var4 = var0;

         while(var3.hasNext()) {
            Sequence var5 = (Sequence)var3.next();
            if (var5.getName().contains(var0) && var5.getName().compareTo(var4) > 0) {
               var4 = var5.getName();
            }
         }

         return var4;
      } else {
         return versionedName(var0);
      }
   }

   public static String getSequenceNameFromQueue(Queue var0) {
      return getLatestSequenceName(getBaseSequenceName(var0.getName()), var0);
   }

   private static class PersistentStoreBeanImpl implements PersistentStoreBean {
      private static final String DEFAULT_DIRECTORY = "stores/default";
      private static final String DEFAULT_POLICY = "Direct-Write";
      private String storeDirectory;
      private String policy;

      private PersistentStoreBeanImpl() {
         this.storeDirectory = "stores/default";
         this.policy = "Direct-Write";
      }

      private void setStoreDirectory(String var1) {
         this.storeDirectory = var1;
      }

      public String getStoreDirectory() {
         return this.storeDirectory;
      }

      private void setPolicy(String var1) {
         this.policy = var1;
      }

      public String getSynchronousWritePolicy() {
         return this.policy;
      }

      // $FF: synthetic method
      PersistentStoreBeanImpl(Object var1) {
         this();
      }
   }
}
