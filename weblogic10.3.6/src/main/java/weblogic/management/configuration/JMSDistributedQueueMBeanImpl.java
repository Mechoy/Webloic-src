package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BootstrapProperties;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.ReferenceManager;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.JMSDistributedQueue;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class JMSDistributedQueueMBeanImpl extends JMSDistributedDestinationMBeanImpl implements JMSDistributedQueueMBean, Serializable {
   private int _ForwardDelay;
   private JMSDistributedQueueMemberMBean[] _JMSDistributedQueueMembers;
   private JMSTemplateMBean _JMSTemplate;
   private String _JNDIName;
   private String _LoadBalancingPolicy;
   private JMSDistributedQueueMemberMBean[] _Members;
   private String _Name;
   private String _Notes;
   private boolean _ResetDeliveryCountOnForward;
   private TargetMBean[] _Targets;
   private JMSTemplateMBean _Template;
   private JMSDistributedQueue _customizer;
   private static SchemaHelper2 _schemaHelper;

   public JMSDistributedQueueMBeanImpl() {
      try {
         this._customizer = new JMSDistributedQueue(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public JMSDistributedQueueMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new JMSDistributedQueue(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public JMSTemplateMBean createJMSTemplate(String var1) {
      JMSTemplateMBeanImpl var2 = new JMSTemplateMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.setJMSTemplate(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public String getJNDIName() {
      return this._customizer.getJNDIName();
   }

   public JMSDistributedQueueMemberMBean[] getMembers() {
      return this._customizer.getMembers();
   }

   public String getMembersAsString() {
      return this._getHelper()._serializeKeyList(this.getMembers());
   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public TargetMBean[] getTargets() {
      return this._customizer.getTargets();
   }

   public String getTargetsAsString() {
      return this._getHelper()._serializeKeyList(this.getTargets());
   }

   public boolean isJNDINameSet() {
      return this._isSet(9);
   }

   public boolean isMembersSet() {
      return this._isSet(13);
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isTargetsSet() {
      return this._isSet(7);
   }

   public void setMembersAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._Members);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, JMSDistributedQueueMemberMBean.class, new ReferenceManager.Resolver(this, 13) {
                  public void resolveReference(Object var1) {
                     try {
                        JMSDistributedQueueMBeanImpl.this.addMember((JMSDistributedQueueMemberMBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               JMSDistributedQueueMemberMBean[] var6 = this._Members;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  JMSDistributedQueueMemberMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeMember(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         JMSDistributedQueueMemberMBean[] var2 = this._Members;
         this._initializeProperty(13);
         this._postSet(13, var2, this._Members);
      }
   }

   public void setTargetsAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         String[] var13 = this._getHelper()._splitKeyList(var1);
         List var3 = this._getHelper()._getKeyList(this._Targets);

         String var5;
         for(int var4 = 0; var4 < var13.length; ++var4) {
            var5 = var13[var4];
            var5 = var5 == null ? null : var5.trim();
            if (var3.contains(var5)) {
               var3.remove(var5);
            } else {
               this._getReferenceManager().registerUnresolvedReference(var5, TargetMBean.class, new ReferenceManager.Resolver(this, 7) {
                  public void resolveReference(Object var1) {
                     try {
                        JMSDistributedQueueMBeanImpl.this.addTarget((TargetMBean)var1);
                     } catch (RuntimeException var3) {
                        throw var3;
                     } catch (Exception var4) {
                        throw new AssertionError("Impossible exception: " + var4);
                     }
                  }
               });
            }
         }

         Iterator var14 = var3.iterator();

         while(true) {
            while(var14.hasNext()) {
               var5 = (String)var14.next();
               TargetMBean[] var6 = this._Targets;
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  TargetMBean var9 = var6[var8];
                  if (var5.equals(var9.getName())) {
                     try {
                        this.removeTarget(var9);
                        break;
                     } catch (RuntimeException var11) {
                        throw var11;
                     } catch (Exception var12) {
                        throw new AssertionError("Impossible exception: " + var12);
                     }
                  }
               }
            }

            return;
         }
      } else {
         TargetMBean[] var2 = this._Targets;
         this._initializeProperty(7);
         this._postSet(7, var2, this._Targets);
      }
   }

   public void destroyJMSTemplate(JMSTemplateMBean var1) {
      try {
         AbstractDescriptorBean var2 = (AbstractDescriptorBean)this._JMSTemplate;
         if (var2 != null) {
            List var3 = this._getReferenceManager().getResolvedReferences(var2);
            if (var3 != null && var3.size() > 0) {
               throw new BeanRemoveRejectedException(var2, var3);
            } else {
               this._getReferenceManager().unregisterBean(var2);
               this._markDestroyed(var2);
               this.setJMSTemplate((JMSTemplateMBean)null);
               this._unSet(10);
            }
         }
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void setJNDIName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getJNDIName();
      this._customizer.setJNDIName(var1);
      this._postSet(9, var2, var1);
   }

   public void setMembers(JMSDistributedQueueMemberMBean[] var1) throws InvalidAttributeValueException {
      Object var3 = var1 == null ? new JMSDistributedQueueMemberMBeanImpl[0] : var1;
      JMSDistributedQueueMemberMBean[] var2 = this.getMembers();
      this._customizer.setMembers((JMSDistributedQueueMemberMBean[])var3);
      this._postSet(13, var2, var3);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      Object var4 = var1 == null ? new TargetMBeanImpl[0] : var1;
      var1 = (TargetMBean[])((TargetMBean[])this._getHelper()._cleanAndValidateArray(var4, TargetMBean.class));

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            ResolvedReference var3 = new ResolvedReference(this, 7, (AbstractDescriptorBean)var1[var2]) {
               protected Object getPropertyValue() {
                  return JMSDistributedQueueMBeanImpl.this.getTargets();
               }
            };
            this._getReferenceManager().registerResolvedReference((AbstractDescriptorBean)var1[var2], var3);
         }
      }

      TargetMBean[] var5 = this.getTargets();
      this._customizer.setTargets(var1);
      this._postSet(7, var5, var1);
   }

   public boolean addMember(JMSDistributedQueueMemberMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      return this._customizer.addMember(var1);
   }

   public boolean addTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 7)) {
         TargetMBean[] var2 = (TargetMBean[])((TargetMBean[])this._getHelper()._extendArray(this.getTargets(), TargetMBean.class, var1));

         try {
            this.setTargets(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            if (var4 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var4;
            }

            if (var4 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

      return true;
   }

   public JMSTemplateMBean getJMSTemplate() {
      return this._customizer.getJMSTemplate();
   }

   public String getNotes() {
      return this._customizer.getNotes();
   }

   public boolean isJMSTemplateSet() {
      return this._isSet(10);
   }

   public boolean isNotesSet() {
      return this._isSet(3);
   }

   public boolean removeMember(JMSDistributedQueueMemberMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      return this._customizer.removeMember(var1);
   }

   public boolean removeTarget(TargetMBean var1) throws InvalidAttributeValueException, DistributedManagementException {
      TargetMBean[] var2 = this.getTargets();
      TargetMBean[] var3 = (TargetMBean[])((TargetMBean[])this._getHelper()._removeElement(var2, TargetMBean.class, var1));
      if (var3.length != var2.length) {
         try {
            this.setTargets(var3);
            return true;
         } catch (Exception var5) {
            if (var5 instanceof RuntimeException) {
               throw (RuntimeException)var5;
            } else if (var5 instanceof InvalidAttributeValueException) {
               throw (InvalidAttributeValueException)var5;
            } else if (var5 instanceof DistributedManagementException) {
               throw (DistributedManagementException)var5;
            } else {
               throw new UndeclaredThrowableException(var5);
            }
         }
      } else {
         return false;
      }
   }

   public void setJMSTemplate(JMSTemplateMBean var1) {
      if (var1 != null && this.getJMSTemplate() != null && var1 != this.getJMSTemplate()) {
         throw new BeanAlreadyExistsException(this.getJMSTemplate() + " has already been created");
      } else {
         if (var1 != null) {
            AbstractDescriptorBean var2 = (AbstractDescriptorBean)var1;
            if (this._setParent(var2, this, 10)) {
               this._getReferenceManager().registerBean(var2, true);
               this._postCreate(var2);
            }
         }

         JMSTemplateMBean var5 = this.getJMSTemplate();

         try {
            this._customizer.setJMSTemplate(var1);
         } catch (InvalidAttributeValueException var4) {
            throw new UndeclaredThrowableException(var4);
         }

         this._postSet(10, var5, var1);
      }
   }

   public void setNotes(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this.getNotes();
      this._customizer.setNotes(var1);
      this._postSet(3, var2, var1);
   }

   public JMSTemplateMBean getTemplate() {
      return this._customizer.getTemplate();
   }

   public String getTemplateAsString() {
      AbstractDescriptorBean var1 = (AbstractDescriptorBean)this.getTemplate();
      return var1 == null ? null : var1._getKey().toString();
   }

   public boolean isTemplateSet() {
      return this._isSet(11);
   }

   public void setTemplateAsString(String var1) {
      if (var1 != null && var1.length() != 0) {
         var1 = var1 == null ? null : var1.trim();
         this._getReferenceManager().registerUnresolvedReference(var1, JMSTemplateMBean.class, new ReferenceManager.Resolver(this, 11) {
            public void resolveReference(Object var1) {
               try {
                  JMSDistributedQueueMBeanImpl.this.setTemplate((JMSTemplateMBean)var1);
               } catch (RuntimeException var3) {
                  throw var3;
               } catch (Exception var4) {
                  throw new AssertionError("Impossible exception: " + var4);
               }
            }
         });
      } else {
         JMSTemplateMBean var2 = this._Template;
         this._initializeProperty(11);
         this._postSet(11, var2, this._Template);
      }

   }

   public void useDelegates(DistributedQueueBean var1, SubDeploymentMBean var2) {
      this._customizer.useDelegates(var1, var2);
   }

   public void addJMSDistributedQueueMember(JMSDistributedQueueMemberMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 14)) {
         JMSDistributedQueueMemberMBean[] var2;
         if (this._isSet(14)) {
            var2 = (JMSDistributedQueueMemberMBean[])((JMSDistributedQueueMemberMBean[])this._getHelper()._extendArray(this.getJMSDistributedQueueMembers(), JMSDistributedQueueMemberMBean.class, var1));
         } else {
            var2 = new JMSDistributedQueueMemberMBean[]{var1};
         }

         try {
            this.setJMSDistributedQueueMembers(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public JMSDistributedQueueMemberMBean[] getJMSDistributedQueueMembers() {
      return this._JMSDistributedQueueMembers;
   }

   public boolean isJMSDistributedQueueMembersSet() {
      return this._isSet(14);
   }

   public void removeJMSDistributedQueueMember(JMSDistributedQueueMemberMBean var1) {
      this.destroyJMSDistributedQueueMember(var1);
   }

   public void setJMSDistributedQueueMembers(JMSDistributedQueueMemberMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new JMSDistributedQueueMemberMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 14)) {
            this._getReferenceManager().registerBean(var3, true);
            this._postCreate(var3);
         }
      }

      JMSDistributedQueueMemberMBean[] var5 = this._JMSDistributedQueueMembers;
      this._JMSDistributedQueueMembers = (JMSDistributedQueueMemberMBean[])var4;
      this._postSet(14, var5, var4);
   }

   public void setTemplate(JMSTemplateMBean var1) {
      JMSTemplateMBean var2 = this.getTemplate();

      try {
         this._customizer.setTemplate(var1);
      } catch (InvalidAttributeValueException var4) {
         throw new UndeclaredThrowableException(var4);
      }

      this._postSet(11, var2, var1);
   }

   public JMSDistributedQueueMemberMBean createJMSDistributedQueueMember(String var1) {
      JMSDistributedQueueMemberMBeanImpl var2 = new JMSDistributedQueueMemberMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addJMSDistributedQueueMember(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public String getLoadBalancingPolicy() {
      return this._customizer.getLoadBalancingPolicy();
   }

   public boolean isLoadBalancingPolicySet() {
      return this._isSet(12);
   }

   public JMSDistributedQueueMemberMBean createJMSDistributedQueueMember(String var1, JMSDistributedQueueMemberMBean var2) {
      return this._customizer.createJMSDistributedQueueMember(var1, var2);
   }

   public void setLoadBalancingPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Round-Robin", "Random"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("LoadBalancingPolicy", var1, var2);
      String var3 = this.getLoadBalancingPolicy();
      this._customizer.setLoadBalancingPolicy(var1);
      this._postSet(12, var3, var1);
   }

   public void destroyJMSDistributedQueueMember(JMSDistributedQueueMemberMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 14);
         JMSDistributedQueueMemberMBean[] var2 = this.getJMSDistributedQueueMembers();
         JMSDistributedQueueMemberMBean[] var3 = (JMSDistributedQueueMemberMBean[])((JMSDistributedQueueMemberMBean[])this._getHelper()._removeElement(var2, JMSDistributedQueueMemberMBean.class, var1));
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
               this.setJMSDistributedQueueMembers(var3);
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

   public void destroyJMSDistributedQueueMember(String var1, JMSDistributedQueueMemberMBean var2) {
      this._customizer.destroyJMSDistributedQueueMember(var1, var2);
   }

   public JMSDistributedQueueMemberMBean lookupJMSDistributedQueueMember(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._JMSDistributedQueueMembers).iterator();

      JMSDistributedQueueMemberMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (JMSDistributedQueueMemberMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public int getForwardDelay() {
      return this._customizer.getForwardDelay();
   }

   public boolean isForwardDelaySet() {
      return this._isSet(15);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setForwardDelay(int var1) throws InvalidAttributeValueException {
      int var2 = this.getForwardDelay();
      this._customizer.setForwardDelay(var1);
      this._postSet(15, var2, var1);
   }

   public boolean getResetDeliveryCountOnForward() {
      return this._ResetDeliveryCountOnForward;
   }

   public boolean isResetDeliveryCountOnForwardSet() {
      return this._isSet(16);
   }

   public void setResetDeliveryCountOnForward(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this.getResetDeliveryCountOnForward();
      this._customizer.setResetDeliveryCountOnForward(var1);
      this._postSet(16, var2, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
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
         var1 = 15;
      }

      try {
         switch (var1) {
            case 15:
               this._customizer.setForwardDelay(-1);
               if (var2) {
                  break;
               }
            case 14:
               this._JMSDistributedQueueMembers = new JMSDistributedQueueMemberMBean[0];
               if (var2) {
                  break;
               }
            case 10:
               this._customizer.setJMSTemplate((JMSTemplateMBean)null);
               if (var2) {
                  break;
               }
            case 9:
               this._customizer.setJNDIName((String)null);
               if (var2) {
                  break;
               }
            case 12:
               this._customizer.setLoadBalancingPolicy("Round-Robin");
               if (var2) {
                  break;
               }
            case 13:
               this._customizer.setMembers(new JMSDistributedQueueMemberMBean[0]);
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 3:
               this._customizer.setNotes((String)null);
               if (var2) {
                  break;
               }
            case 16:
               this._customizer.setResetDeliveryCountOnForward(true);
               if (var2) {
                  break;
               }
            case 7:
               this._customizer.setTargets(new TargetMBean[0]);
               if (var2) {
                  break;
               }
            case 11:
               this._customizer.setTemplate((JMSTemplateMBean)null);
               if (var2) {
                  break;
               }
            case 4:
            case 5:
            case 6:
            case 8:
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
      return "JMSDistributedQueue";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("ForwardDelay")) {
         int var9 = this._ForwardDelay;
         this._ForwardDelay = (Integer)var2;
         this._postSet(15, var9, this._ForwardDelay);
      } else {
         JMSDistributedQueueMemberMBean[] var8;
         if (var1.equals("JMSDistributedQueueMembers")) {
            var8 = this._JMSDistributedQueueMembers;
            this._JMSDistributedQueueMembers = (JMSDistributedQueueMemberMBean[])((JMSDistributedQueueMemberMBean[])var2);
            this._postSet(14, var8, this._JMSDistributedQueueMembers);
         } else {
            JMSTemplateMBean var4;
            if (var1.equals("JMSTemplate")) {
               var4 = this._JMSTemplate;
               this._JMSTemplate = (JMSTemplateMBean)var2;
               this._postSet(10, var4, this._JMSTemplate);
            } else {
               String var7;
               if (var1.equals("JNDIName")) {
                  var7 = this._JNDIName;
                  this._JNDIName = (String)var2;
                  this._postSet(9, var7, this._JNDIName);
               } else if (var1.equals("LoadBalancingPolicy")) {
                  var7 = this._LoadBalancingPolicy;
                  this._LoadBalancingPolicy = (String)var2;
                  this._postSet(12, var7, this._LoadBalancingPolicy);
               } else if (var1.equals("Members")) {
                  var8 = this._Members;
                  this._Members = (JMSDistributedQueueMemberMBean[])((JMSDistributedQueueMemberMBean[])var2);
                  this._postSet(13, var8, this._Members);
               } else if (var1.equals("Name")) {
                  var7 = this._Name;
                  this._Name = (String)var2;
                  this._postSet(2, var7, this._Name);
               } else if (var1.equals("Notes")) {
                  var7 = this._Notes;
                  this._Notes = (String)var2;
                  this._postSet(3, var7, this._Notes);
               } else if (var1.equals("ResetDeliveryCountOnForward")) {
                  boolean var6 = this._ResetDeliveryCountOnForward;
                  this._ResetDeliveryCountOnForward = (Boolean)var2;
                  this._postSet(16, var6, this._ResetDeliveryCountOnForward);
               } else if (var1.equals("Targets")) {
                  TargetMBean[] var5 = this._Targets;
                  this._Targets = (TargetMBean[])((TargetMBean[])var2);
                  this._postSet(7, var5, this._Targets);
               } else if (var1.equals("Template")) {
                  var4 = this._Template;
                  this._Template = (JMSTemplateMBean)var2;
                  this._postSet(11, var4, this._Template);
               } else if (var1.equals("customizer")) {
                  JMSDistributedQueue var3 = this._customizer;
                  this._customizer = (JMSDistributedQueue)var2;
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ForwardDelay")) {
         return new Integer(this._ForwardDelay);
      } else if (var1.equals("JMSDistributedQueueMembers")) {
         return this._JMSDistributedQueueMembers;
      } else if (var1.equals("JMSTemplate")) {
         return this._JMSTemplate;
      } else if (var1.equals("JNDIName")) {
         return this._JNDIName;
      } else if (var1.equals("LoadBalancingPolicy")) {
         return this._LoadBalancingPolicy;
      } else if (var1.equals("Members")) {
         return this._Members;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("Notes")) {
         return this._Notes;
      } else if (var1.equals("ResetDeliveryCountOnForward")) {
         return new Boolean(this._ResetDeliveryCountOnForward);
      } else if (var1.equals("Targets")) {
         return this._Targets;
      } else if (var1.equals("Template")) {
         return this._Template;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends JMSDistributedDestinationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
               break;
            case 5:
               if (var1.equals("notes")) {
                  return 3;
               }
               break;
            case 6:
               if (var1.equals("member")) {
                  return 13;
               }

               if (var1.equals("target")) {
                  return 7;
               }
            case 7:
            case 10:
            case 11:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 29:
            case 30:
            default:
               break;
            case 8:
               if (var1.equals("template")) {
                  return 11;
               }
               break;
            case 9:
               if (var1.equals("jndi-name")) {
                  return 9;
               }
               break;
            case 12:
               if (var1.equals("jms-template")) {
                  return 10;
               }
               break;
            case 13:
               if (var1.equals("forward-delay")) {
                  return 15;
               }
               break;
            case 21:
               if (var1.equals("load-balancing-policy")) {
                  return 12;
               }
               break;
            case 28:
               if (var1.equals("jms-distributed-queue-member")) {
                  return 14;
               }
               break;
            case 31:
               if (var1.equals("reset-delivery-count-on-forward")) {
                  return 16;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 10:
               return new JMSTemplateMBeanImpl.SchemaHelper2();
            case 14:
               return new JMSDistributedQueueMemberMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 2:
               return "name";
            case 3:
               return "notes";
            case 4:
            case 5:
            case 6:
            case 8:
            default:
               return super.getElementName(var1);
            case 7:
               return "target";
            case 9:
               return "jndi-name";
            case 10:
               return "jms-template";
            case 11:
               return "template";
            case 12:
               return "load-balancing-policy";
            case 13:
               return "member";
            case 14:
               return "jms-distributed-queue-member";
            case 15:
               return "forward-delay";
            case 16:
               return "reset-delivery-count-on-forward";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 13:
               return true;
            case 14:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 10:
               return true;
            case 14:
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends JMSDistributedDestinationMBeanImpl.Helper {
      private JMSDistributedQueueMBeanImpl bean;

      protected Helper(JMSDistributedQueueMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
               return "Notes";
            case 4:
            case 5:
            case 6:
            case 8:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "Targets";
            case 9:
               return "JNDIName";
            case 10:
               return "JMSTemplate";
            case 11:
               return "Template";
            case 12:
               return "LoadBalancingPolicy";
            case 13:
               return "Members";
            case 14:
               return "JMSDistributedQueueMembers";
            case 15:
               return "ForwardDelay";
            case 16:
               return "ResetDeliveryCountOnForward";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ForwardDelay")) {
            return 15;
         } else if (var1.equals("JMSDistributedQueueMembers")) {
            return 14;
         } else if (var1.equals("JMSTemplate")) {
            return 10;
         } else if (var1.equals("JNDIName")) {
            return 9;
         } else if (var1.equals("LoadBalancingPolicy")) {
            return 12;
         } else if (var1.equals("Members")) {
            return 13;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("Notes")) {
            return 3;
         } else if (var1.equals("ResetDeliveryCountOnForward")) {
            return 16;
         } else if (var1.equals("Targets")) {
            return 7;
         } else {
            return var1.equals("Template") ? 11 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getJMSDistributedQueueMembers()));
         if (this.bean.getJMSTemplate() != null) {
            var1.add(new ArrayIterator(new JMSTemplateMBean[]{this.bean.getJMSTemplate()}));
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
            if (this.bean.isForwardDelaySet()) {
               var2.append("ForwardDelay");
               var2.append(String.valueOf(this.bean.getForwardDelay()));
            }

            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getJMSDistributedQueueMembers().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getJMSDistributedQueueMembers()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            var5 = this.computeChildHashValue(this.bean.getJMSTemplate());
            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isJNDINameSet()) {
               var2.append("JNDIName");
               var2.append(String.valueOf(this.bean.getJNDIName()));
            }

            if (this.bean.isLoadBalancingPolicySet()) {
               var2.append("LoadBalancingPolicy");
               var2.append(String.valueOf(this.bean.getLoadBalancingPolicy()));
            }

            if (this.bean.isMembersSet()) {
               var2.append("Members");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getMembers())));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isNotesSet()) {
               var2.append("Notes");
               var2.append(String.valueOf(this.bean.getNotes()));
            }

            if (this.bean.isResetDeliveryCountOnForwardSet()) {
               var2.append("ResetDeliveryCountOnForward");
               var2.append(String.valueOf(this.bean.getResetDeliveryCountOnForward()));
            }

            if (this.bean.isTargetsSet()) {
               var2.append("Targets");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTargets())));
            }

            if (this.bean.isTemplateSet()) {
               var2.append("Template");
               var2.append(String.valueOf(this.bean.getTemplate()));
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
            JMSDistributedQueueMBeanImpl var2 = (JMSDistributedQueueMBeanImpl)var1;
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ForwardDelay", this.bean.getForwardDelay(), var2.getForwardDelay(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSDistributedQueueMembers", this.bean.getJMSDistributedQueueMembers(), var2.getJMSDistributedQueueMembers(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeChildDiff("JMSTemplate", this.bean.getJMSTemplate(), var2.getJMSTemplate(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("JNDIName", this.bean.getJNDIName(), var2.getJNDIName(), false);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("LoadBalancingPolicy", this.bean.getLoadBalancingPolicy(), var2.getLoadBalancingPolicy(), true);
            }

            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Members", this.bean.getMembers(), var2.getMembers(), true);
            }

            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("Notes", this.bean.getNotes(), var2.getNotes(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("ResetDeliveryCountOnForward", this.bean.getResetDeliveryCountOnForward(), var2.getResetDeliveryCountOnForward(), true);
            }

            this.computeDiff("Targets", this.bean.getTargets(), var2.getTargets(), true);
            if (BootstrapProperties.INCLUDE_OBSOLETE_PROPS_IN_DIFF) {
               this.computeDiff("Template", this.bean.getTemplate(), var2.getTemplate(), false);
            }

         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            JMSDistributedQueueMBeanImpl var3 = (JMSDistributedQueueMBeanImpl)var1.getSourceBean();
            JMSDistributedQueueMBeanImpl var4 = (JMSDistributedQueueMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ForwardDelay")) {
                  var3.setForwardDelay(var4.getForwardDelay());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("JMSDistributedQueueMembers")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addJMSDistributedQueueMember((JMSDistributedQueueMemberMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeJMSDistributedQueueMember((JMSDistributedQueueMemberMBean)var2.getRemovedObject());
                  }

                  if (var3.getJMSDistributedQueueMembers() == null || var3.getJMSDistributedQueueMembers().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  }
               } else if (var5.equals("JMSTemplate")) {
                  if (var6 == 2) {
                     var3.setJMSTemplate((JMSTemplateMBean)this.createCopy((AbstractDescriptorBean)var4.getJMSTemplate()));
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3._destroySingleton("JMSTemplate", var3.getJMSTemplate());
                  }

                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("JNDIName")) {
                  var3.setJNDIName(var4.getJNDIName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("LoadBalancingPolicy")) {
                  var3.setLoadBalancingPolicy(var4.getLoadBalancingPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("Members")) {
                  var3.setMembersAsString(var4.getMembersAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("Notes")) {
                  var3.setNotes(var4.getNotes());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 3);
               } else if (var5.equals("ResetDeliveryCountOnForward")) {
                  var3.setResetDeliveryCountOnForward(var4.getResetDeliveryCountOnForward());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("Targets")) {
                  var3.setTargetsAsString(var4.getTargetsAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("Template")) {
                  var3.setTemplateAsString(var4.getTemplateAsString());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
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
            JMSDistributedQueueMBeanImpl var5 = (JMSDistributedQueueMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if (var2 && (var3 == null || !var3.contains("ForwardDelay")) && this.bean.isForwardDelaySet()) {
               var5.setForwardDelay(this.bean.getForwardDelay());
            }

            if (var2 && (var3 == null || !var3.contains("JMSDistributedQueueMembers")) && this.bean.isJMSDistributedQueueMembersSet() && !var5._isSet(14)) {
               JMSDistributedQueueMemberMBean[] var6 = this.bean.getJMSDistributedQueueMembers();
               JMSDistributedQueueMemberMBean[] var7 = new JMSDistributedQueueMemberMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (JMSDistributedQueueMemberMBean)((JMSDistributedQueueMemberMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setJMSDistributedQueueMembers(var7);
            }

            if (var2 && (var3 == null || !var3.contains("JMSTemplate")) && this.bean.isJMSTemplateSet() && !var5._isSet(10)) {
               JMSTemplateMBean var4 = this.bean.getJMSTemplate();
               var5.setJMSTemplate((JMSTemplateMBean)null);
               var5.setJMSTemplate(var4 == null ? null : (JMSTemplateMBean)this.createCopy((AbstractDescriptorBean)var4, var2));
            }

            if (var2 && (var3 == null || !var3.contains("JNDIName")) && this.bean.isJNDINameSet()) {
               var5.setJNDIName(this.bean.getJNDIName());
            }

            if (var2 && (var3 == null || !var3.contains("LoadBalancingPolicy")) && this.bean.isLoadBalancingPolicySet()) {
               var5.setLoadBalancingPolicy(this.bean.getLoadBalancingPolicy());
            }

            if (var2 && (var3 == null || !var3.contains("Members")) && this.bean.isMembersSet()) {
               var5._unSet(var5, 13);
               var5.setMembersAsString(this.bean.getMembersAsString());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("Notes")) && this.bean.isNotesSet()) {
               var5.setNotes(this.bean.getNotes());
            }

            if (var2 && (var3 == null || !var3.contains("ResetDeliveryCountOnForward")) && this.bean.isResetDeliveryCountOnForwardSet()) {
               var5.setResetDeliveryCountOnForward(this.bean.getResetDeliveryCountOnForward());
            }

            if ((var3 == null || !var3.contains("Targets")) && this.bean.isTargetsSet()) {
               var5._unSet(var5, 7);
               var5.setTargetsAsString(this.bean.getTargetsAsString());
            }

            if (var2 && (var3 == null || !var3.contains("Template")) && this.bean.isTemplateSet()) {
               var5._unSet(var5, 11);
               var5.setTemplateAsString(this.bean.getTemplateAsString());
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
         this.inferSubTree(this.bean.getJMSDistributedQueueMembers(), var1, var2);
         this.inferSubTree(this.bean.getJMSTemplate(), var1, var2);
         this.inferSubTree(this.bean.getMembers(), var1, var2);
         this.inferSubTree(this.bean.getTargets(), var1, var2);
         this.inferSubTree(this.bean.getTemplate(), var1, var2);
      }
   }
}
