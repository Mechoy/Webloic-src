package weblogic.management.descriptors.webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ComponentsMBeanImpl extends XMLElementMBeanDelegate implements ComponentsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_javaClassComponents = false;
   private List javaClassComponents;
   private boolean isSet_statelessEJBs = false;
   private List statelessEJBs;
   private boolean isSet_useMultiplePorts = false;
   private boolean useMultiplePorts;
   private boolean isSet_jmsReceiveTopics = false;
   private List jmsReceiveTopics;
   private boolean isSet_jmsReceiveQueues = false;
   private List jmsReceiveQueues;
   private boolean isSet_jmsSendDestinations = false;
   private List jmsSendDestinations;
   private boolean isSet_usePortTypeName = false;
   private boolean usePortTypeName;
   private boolean isSet_statefulJavaClassComponents = false;
   private List statefulJavaClassComponents;

   public JavaClassMBean[] getJavaClassComponents() {
      if (this.javaClassComponents == null) {
         return new JavaClassMBean[0];
      } else {
         JavaClassMBean[] var1 = new JavaClassMBean[this.javaClassComponents.size()];
         var1 = (JavaClassMBean[])((JavaClassMBean[])this.javaClassComponents.toArray(var1));
         return var1;
      }
   }

   public void setJavaClassComponents(JavaClassMBean[] var1) {
      JavaClassMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getJavaClassComponents();
      }

      this.isSet_javaClassComponents = true;
      if (this.javaClassComponents == null) {
         this.javaClassComponents = Collections.synchronizedList(new ArrayList());
      } else {
         this.javaClassComponents.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.javaClassComponents.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("JavaClassComponents", var2, this.getJavaClassComponents());
      }

   }

   public void addJavaClassComponent(JavaClassMBean var1) {
      this.isSet_javaClassComponents = true;
      if (this.javaClassComponents == null) {
         this.javaClassComponents = Collections.synchronizedList(new ArrayList());
      }

      this.javaClassComponents.add(var1);
   }

   public void removeJavaClassComponent(JavaClassMBean var1) {
      if (this.javaClassComponents != null) {
         this.javaClassComponents.remove(var1);
      }
   }

   public StatelessEJBMBean[] getStatelessEJBs() {
      if (this.statelessEJBs == null) {
         return new StatelessEJBMBean[0];
      } else {
         StatelessEJBMBean[] var1 = new StatelessEJBMBean[this.statelessEJBs.size()];
         var1 = (StatelessEJBMBean[])((StatelessEJBMBean[])this.statelessEJBs.toArray(var1));
         return var1;
      }
   }

   public void setStatelessEJBs(StatelessEJBMBean[] var1) {
      StatelessEJBMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getStatelessEJBs();
      }

      this.isSet_statelessEJBs = true;
      if (this.statelessEJBs == null) {
         this.statelessEJBs = Collections.synchronizedList(new ArrayList());
      } else {
         this.statelessEJBs.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.statelessEJBs.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("StatelessEJBs", var2, this.getStatelessEJBs());
      }

   }

   public void addStatelessEJB(StatelessEJBMBean var1) {
      this.isSet_statelessEJBs = true;
      if (this.statelessEJBs == null) {
         this.statelessEJBs = Collections.synchronizedList(new ArrayList());
      }

      this.statelessEJBs.add(var1);
   }

   public void removeStatelessEJB(StatelessEJBMBean var1) {
      if (this.statelessEJBs != null) {
         this.statelessEJBs.remove(var1);
      }
   }

   public boolean getUseMultiplePorts() {
      return this.useMultiplePorts;
   }

   public void setUseMultiplePorts(boolean var1) {
      boolean var2 = this.useMultiplePorts;
      this.useMultiplePorts = var1;
      this.isSet_useMultiplePorts = true;
      this.checkChange("useMultiplePorts", var2, this.useMultiplePorts);
   }

   public JMSReceiveTopicMBean[] getJMSReceiveTopics() {
      if (this.jmsReceiveTopics == null) {
         return new JMSReceiveTopicMBean[0];
      } else {
         JMSReceiveTopicMBean[] var1 = new JMSReceiveTopicMBean[this.jmsReceiveTopics.size()];
         var1 = (JMSReceiveTopicMBean[])((JMSReceiveTopicMBean[])this.jmsReceiveTopics.toArray(var1));
         return var1;
      }
   }

   public void setJMSReceiveTopics(JMSReceiveTopicMBean[] var1) {
      JMSReceiveTopicMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getJMSReceiveTopics();
      }

      this.isSet_jmsReceiveTopics = true;
      if (this.jmsReceiveTopics == null) {
         this.jmsReceiveTopics = Collections.synchronizedList(new ArrayList());
      } else {
         this.jmsReceiveTopics.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.jmsReceiveTopics.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("JMSReceiveTopics", var2, this.getJMSReceiveTopics());
      }

   }

   public void addJMSReceiveTopic(JMSReceiveTopicMBean var1) {
      this.isSet_jmsReceiveTopics = true;
      if (this.jmsReceiveTopics == null) {
         this.jmsReceiveTopics = Collections.synchronizedList(new ArrayList());
      }

      this.jmsReceiveTopics.add(var1);
   }

   public void removeJMSReceiveTopic(JMSReceiveTopicMBean var1) {
      if (this.jmsReceiveTopics != null) {
         this.jmsReceiveTopics.remove(var1);
      }
   }

   public JMSReceiveQueueMBean[] getJMSReceiveQueues() {
      if (this.jmsReceiveQueues == null) {
         return new JMSReceiveQueueMBean[0];
      } else {
         JMSReceiveQueueMBean[] var1 = new JMSReceiveQueueMBean[this.jmsReceiveQueues.size()];
         var1 = (JMSReceiveQueueMBean[])((JMSReceiveQueueMBean[])this.jmsReceiveQueues.toArray(var1));
         return var1;
      }
   }

   public void setJMSReceiveQueues(JMSReceiveQueueMBean[] var1) {
      JMSReceiveQueueMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getJMSReceiveQueues();
      }

      this.isSet_jmsReceiveQueues = true;
      if (this.jmsReceiveQueues == null) {
         this.jmsReceiveQueues = Collections.synchronizedList(new ArrayList());
      } else {
         this.jmsReceiveQueues.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.jmsReceiveQueues.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("JMSReceiveQueues", var2, this.getJMSReceiveQueues());
      }

   }

   public void addJMSReceiveQueue(JMSReceiveQueueMBean var1) {
      this.isSet_jmsReceiveQueues = true;
      if (this.jmsReceiveQueues == null) {
         this.jmsReceiveQueues = Collections.synchronizedList(new ArrayList());
      }

      this.jmsReceiveQueues.add(var1);
   }

   public void removeJMSReceiveQueue(JMSReceiveQueueMBean var1) {
      if (this.jmsReceiveQueues != null) {
         this.jmsReceiveQueues.remove(var1);
      }
   }

   public JMSSendDestinationMBean[] getJMSSendDestinations() {
      if (this.jmsSendDestinations == null) {
         return new JMSSendDestinationMBean[0];
      } else {
         JMSSendDestinationMBean[] var1 = new JMSSendDestinationMBean[this.jmsSendDestinations.size()];
         var1 = (JMSSendDestinationMBean[])((JMSSendDestinationMBean[])this.jmsSendDestinations.toArray(var1));
         return var1;
      }
   }

   public void setJMSSendDestinations(JMSSendDestinationMBean[] var1) {
      JMSSendDestinationMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getJMSSendDestinations();
      }

      this.isSet_jmsSendDestinations = true;
      if (this.jmsSendDestinations == null) {
         this.jmsSendDestinations = Collections.synchronizedList(new ArrayList());
      } else {
         this.jmsSendDestinations.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.jmsSendDestinations.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("JMSSendDestinations", var2, this.getJMSSendDestinations());
      }

   }

   public void addJMSSendDestination(JMSSendDestinationMBean var1) {
      this.isSet_jmsSendDestinations = true;
      if (this.jmsSendDestinations == null) {
         this.jmsSendDestinations = Collections.synchronizedList(new ArrayList());
      }

      this.jmsSendDestinations.add(var1);
   }

   public void removeJMSSendDestination(JMSSendDestinationMBean var1) {
      if (this.jmsSendDestinations != null) {
         this.jmsSendDestinations.remove(var1);
      }
   }

   public boolean getUsePortTypeName() {
      return this.usePortTypeName;
   }

   public void setUsePortTypeName(boolean var1) {
      boolean var2 = this.usePortTypeName;
      this.usePortTypeName = var1;
      this.isSet_usePortTypeName = true;
      this.checkChange("usePortTypeName", var2, this.usePortTypeName);
   }

   public StatefulJavaClassMBean[] getStatefulJavaClassComponents() {
      if (this.statefulJavaClassComponents == null) {
         return new StatefulJavaClassMBean[0];
      } else {
         StatefulJavaClassMBean[] var1 = new StatefulJavaClassMBean[this.statefulJavaClassComponents.size()];
         var1 = (StatefulJavaClassMBean[])((StatefulJavaClassMBean[])this.statefulJavaClassComponents.toArray(var1));
         return var1;
      }
   }

   public void setStatefulJavaClassComponents(StatefulJavaClassMBean[] var1) {
      StatefulJavaClassMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getStatefulJavaClassComponents();
      }

      this.isSet_statefulJavaClassComponents = true;
      if (this.statefulJavaClassComponents == null) {
         this.statefulJavaClassComponents = Collections.synchronizedList(new ArrayList());
      } else {
         this.statefulJavaClassComponents.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.statefulJavaClassComponents.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("StatefulJavaClassComponents", var2, this.getStatefulJavaClassComponents());
      }

   }

   public void addStatefulJavaClassComponent(StatefulJavaClassMBean var1) {
      this.isSet_statefulJavaClassComponents = true;
      if (this.statefulJavaClassComponents == null) {
         this.statefulJavaClassComponents = Collections.synchronizedList(new ArrayList());
      }

      this.statefulJavaClassComponents.add(var1);
   }

   public void removeStatefulJavaClassComponent(StatefulJavaClassMBean var1) {
      if (this.statefulJavaClassComponents != null) {
         this.statefulJavaClassComponents.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<components");
      if (this.isSet_useMultiplePorts) {
         var2.append(" use-multiple-ports=\"").append(String.valueOf(this.getUseMultiplePorts())).append("\"");
      }

      if (this.isSet_usePortTypeName) {
         var2.append(" use-port-type-name=\"").append(String.valueOf(this.getUsePortTypeName())).append("\"");
      }

      var2.append(">\n");
      int var3;
      if (null != this.getStatelessEJBs()) {
         for(var3 = 0; var3 < this.getStatelessEJBs().length; ++var3) {
            var2.append(this.getStatelessEJBs()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getJavaClassComponents()) {
         for(var3 = 0; var3 < this.getJavaClassComponents().length; ++var3) {
            var2.append(this.getJavaClassComponents()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getStatefulJavaClassComponents()) {
         for(var3 = 0; var3 < this.getStatefulJavaClassComponents().length; ++var3) {
            var2.append(this.getStatefulJavaClassComponents()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getJMSSendDestinations()) {
         for(var3 = 0; var3 < this.getJMSSendDestinations().length; ++var3) {
            var2.append(this.getJMSSendDestinations()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getJMSReceiveTopics()) {
         for(var3 = 0; var3 < this.getJMSReceiveTopics().length; ++var3) {
            var2.append(this.getJMSReceiveTopics()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getJMSReceiveQueues()) {
         for(var3 = 0; var3 < this.getJMSReceiveQueues().length; ++var3) {
            var2.append(this.getJMSReceiveQueues()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</components>\n");
      return var2.toString();
   }
}
