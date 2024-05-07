package weblogic.wsee.security.wss.sps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import weblogic.wsee.policy.factory.DefaultPolicyAssertion;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyAlternative;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.policy.SecurityPolicyAssertionHelper;
import weblogic.wsee.security.wss.plan.SecurityPolicyBlueprint;
import weblogic.wsee.security.wss.plan.SecurityPolicyBlueprintDesigner;
import weblogic.wsee.security.wss.plan.SecurityPolicyOutline;
import weblogic.wsee.security.wss.policy.SecurityPolicyArchitectureException;
import weblogic.wsee.security.wss.policy.SecurityPolicyInspectionException;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfo;
import weblogic.wsee.security.wssp.SecurityPolicyAssertionInfoFactory;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.wss.WSSecurityException;
import weblogic.xml.crypto.wss11.internal.SecurityBuilder;
import weblogic.xml.crypto.wss11.internal.SecurityBuilderImpl;
import weblogic.xml.crypto.wss11.internal.SecurityValidator;
import weblogic.xml.crypto.wss11.internal.WSS11Context;

public class SmartPolicySelector {
   private static final boolean verbose = Verbose.isVerbose(SmartPolicySelector.class);
   private static final boolean debug = false;
   private int[] policyPriorityIndex = null;
   private NormalizedExpression normalizedPolicy;
   private NormalizedExpression normalizedReferencePolicy;
   private List securityAssertionList;
   private SecurityBuilder sbuilder = null;
   private SecurityValidator securityValidator = null;
   private WSS11Context securityContex = null;
   private PolicySelectionPreference preference = null;
   private PolicyAlternative[] policyAlternatives = null;
   private int reasonCode = 0;
   private String reasons;

   public SmartPolicySelector(SecurityBuilder var1, NormalizedExpression var2, NormalizedExpression var3, Map<String, Object> var4, boolean var5, WSS11Context var6) throws WSSecurityException, SecurityPolicyArchitectureException {
      if (null == var1) {
         throw new IllegalArgumentException("Null security builder found");
      } else {
         this.sbuilder = var1;
         this.securityContex = var6;
         this.init(var2, var3, var4, var5);
      }
   }

   public SmartPolicySelector(SecurityValidator var1, SecurityBuilder var2, NormalizedExpression var3, NormalizedExpression var4, Map<String, Object> var5, boolean var6, WSS11Context var7) throws WSSecurityException, SecurityPolicyArchitectureException {
      if (null != var1 && null != var2) {
         this.securityValidator = var1;
         this.securityContex = var7;
         this.sbuilder = var2;
         this.init(var3, var4, var5, var6);
      } else {
         throw new IllegalArgumentException("Null security builder found");
      }
   }

   public SmartPolicySelector(NormalizedExpression var1, Map<String, Object> var2, boolean var3, WSS11Context var4) throws WSSecurityException, SecurityPolicyArchitectureException {
      this.securityValidator = null;
      this.securityContex = var4;
      this.sbuilder = new SecurityBuilderImpl(var4);
      this.init(var1, (NormalizedExpression)null, var2, var3);
   }

   private void init(NormalizedExpression var1, NormalizedExpression var2, Map<String, Object> var3, boolean var4) throws WSSecurityException, SecurityPolicyArchitectureException {
      this.normalizedPolicy = var1;
      if (var3 != null) {
         this.preference = (PolicySelectionPreference)((Map)var3).get("weblogic.wsee.policy.selection.preference");
      }

      if (null == this.preference) {
         this.preference = new PolicySelectionPreference();
      }

      this.normalizedReferencePolicy = var2;
      if (null == var3) {
         var3 = new HashMap();
      }

      if (var1 != null) {
         this.loadPolicyList(var1, (Map)var3, var4);
      } else {
         this.loadPolicyList(var2, (Map)var3, var4);
      }

   }

   public String tellMeWhy() {
      return null == this.reasons ? "Unable to find a good policy" : this.reasons;
   }

   public int getReasonCode() {
      return this.reasonCode;
   }

   public boolean hasSecurityPolicyAlternative() {
      if (this.normalizedPolicy != null && this.policyAlternatives != null && this.policyAlternatives.length != 0) {
         return this.securityAssertionList != null && 0 != this.getPolicyAlternativeCount();
      } else {
         return false;
      }
   }

   private void loadPolicyList(NormalizedExpression var1, Map<String, Object> var2, boolean var3) throws WSSecurityException, SecurityPolicyArchitectureException {
      if (null != var1 && SecurityPolicyAssertionInfoFactory.hasSecurityPolicy(var1)) {
         ArrayList var4 = new ArrayList();
         this.policyAlternatives = new PolicyAlternative[var1.getPolicyAlternatives().size()];
         Iterator var5 = var1.getPolicyAlternatives().iterator();

         PolicyAlternative var7;
         for(int var6 = 0; var5.hasNext(); this.policyAlternatives[var6++] = var7) {
            var7 = (PolicyAlternative)var5.next();
            SecurityPolicyAssertionInfo var8 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo(var7);
            var4.add(var8);
         }

         if (var4.size() > 0) {
            this.securityAssertionList = this.buildSmartPolicyList(var4, var2, var3);
         } else {
            this.securityAssertionList = null;
         }

      } else {
         this.securityAssertionList = null;
      }
   }

   private List buildSmartPolicyList(List var1, Map<String, Object> var2, boolean var3) throws WSSecurityException, SecurityPolicyArchitectureException {
      ArrayList var4 = new ArrayList(var1.size());
      int var5 = -1;
      SecurityPolicyAssertionInfo var6 = null;
      int var7 = -1;
      SecurityPolicyArchitectureException var8 = null;
      Iterator var9 = var1.iterator();
      String var10 = "";
      if (var1.size() > 1) {
         var10 = "Unable to find a matched policy alternative due to: ";
      }

      StringBuffer var11 = new StringBuffer(var10);
      LinkedList var12 = new LinkedList();

      while(true) {
         while(var9.hasNext() || !var12.isEmpty()) {
            SecurityPolicyAssertionInfo var13 = null;
            if (var9.hasNext()) {
               var13 = (SecurityPolicyAssertionInfo)var9.next();
               ++var5;
               if (null == var13) {
                  continue;
               }

               int var14 = SecurityPolicyAssertionHelper.policyLogicalErrorDetectorRuntime(this.policyAlternatives[var5]);
               if (var14 != 0) {
                  var11.append("-- Found policy logic errors, error code =" + var14 + ";  ");
                  continue;
               }

               PolicyAlternative var15 = SecurityPolicyAssertionHelper.getPolicyAlternativeWithoutOption(this.policyAlternatives[var5]);
               if (null != var15) {
                  var12.add(var15);
               }
            } else {
               PolicyAlternative var20 = (PolicyAlternative)var12.pollLast();
               var13 = SecurityPolicyAssertionInfoFactory.getSecurityPolicyAssertionInfo(var20);
            }

            if (var13.hasUnidentifiedAssertion()) {
               List var23 = var13.getUnidentifiedAssertions();
               StringBuffer var25 = new StringBuffer();

               for(int var26 = 0; var26 < var23.size(); ++var26) {
                  PolicyAssertion var27 = (PolicyAssertion)var23.get(var26);
                  String var18;
                  if (var27 instanceof DefaultPolicyAssertion) {
                     var18 = " unidentified assertion =" + ((DefaultPolicyAssertion)var27).toString();
                  } else {
                     var18 = " unidentified assertion " + var27 + " Name =" + var27.getName() + " Subject " + var27.getPolicySubject().name();
                  }

                  var25.append(var18);
                  if (verbose) {
                     Verbose.log((Object)("Found an" + var18));
                  }
               }

               var11.append(var25.toString());
               if (null == var6) {
                  var6 = var13;
                  var7 = var5;
               } else if (verbose) {
                  Verbose.log((Object)("Multiple ufo found, msg =" + var25.toString()));
               }
            } else {
               try {
                  SmartSecurityPolicyBlueprint var22 = this.buildSmartPolicy(var13, var2, var3, var5);
                  if (var22.hasUnsupportedFeature(524288L)) {
                     var11.append("-- " + var22.tellMeWhyNotSupported(524288L) + ";  ");
                  } else {
                     boolean var24 = false;
                     if (null != this.securityContex && this.securityContex.getCredentialProviders().size() > 0) {
                        String[] var16 = var22.getTokenTypes();
                        if (null != var16) {
                           for(int var17 = 0; var17 < var16.length; ++var17) {
                              if (this.securityContex.getCredentialProvider(var16[var17]) == null) {
                                 var11.append("-- No credential provider found for token type = [" + var16[var17] + "];  ");
                                 var24 = true;
                              }
                           }
                        }
                     }

                     if (!var24) {
                        var4.add(var22);
                     }
                  }
               } catch (SecurityPolicyArchitectureException var19) {
                  var8 = var19;
               }
            }
         }

         if (var4.size() == 0) {
            if (null != var6) {
               SmartSecurityPolicyBlueprint var21 = this.buildSmartPolicy(var6, var2, var3, var7);
               var4.add(var21);
               return var4;
            }

            if (var8 == null) {
               var8 = new SecurityPolicyArchitectureException(var11.toString());
            }

            throw var8;
         }

         return var4;
      }
   }

   private SmartSecurityPolicyBlueprint buildSmartPolicy(SecurityPolicyAssertionInfo var1, Map<String, Object> var2, boolean var3, int var4) throws WSSecurityException, SecurityPolicyArchitectureException {
      SecurityPolicyBlueprintDesigner var5;
      if (this.securityValidator != null) {
         var5 = new SecurityPolicyBlueprintDesigner(this.securityValidator);
      } else {
         var5 = new SecurityPolicyBlueprintDesigner(this.sbuilder);
      }

      var5.designOutboundBlueprint(var1, var2, var3);
      return new SmartSecurityPolicyBlueprint(var5.getBlueprint(), var4, this.policyAlternatives[var4], this.securityContex);
   }

   private boolean isGoodSecuritySecurityPolicyAssertion(SmartSecurityPolicyBlueprint var1) {
      return var1.isCertRequired() ? true : true;
   }

   public int getPolicyAlternativeCount() {
      return null == this.securityAssertionList ? 0 : this.securityAssertionList.size();
   }

   public SmartSecurityPolicyBlueprint[] getSmartPolicyBlueprint(SecurityPolicyOutline var1) throws SecurityPolicyInspectionException {
      if (this.securityAssertionList != null && 0 != this.getPolicyAlternativeCount()) {
         SmartSecurityPolicyBlueprint[] var3;
         SmartSecurityPolicyBlueprint var5;
         if (null != var1 && null != var1.getGeneralPolicy()) {
            SmartSecurityPolicyBlueprint var9 = new SmartSecurityPolicyBlueprint(var1);
            if (this.securityAssertionList.size() == 1) {
               var3 = new SmartSecurityPolicyBlueprint[]{(SmartSecurityPolicyBlueprint)this.securityAssertionList.get(0)};
               if (!var9.hasDescribed()) {
                  return var3;
               } else if (var3[0].getMessageOutlook() == var9.getMessageOutlook()) {
                  return var3;
               } else if (var3[0].getActualPolicyOutlook() == var9.getMessageOutlook()) {
                  return var3;
               } else if ((var3[0].getActualPolicyOutlook() & var9.getMessageOutlook()) != var3[0].getActualPolicyOutlook() && (var3[0].getActualPolicyOutlook() & var9.getMessageOutlook()) != var3[0].getPolicyOutlookWithMask(var9.getMessageOutlookMask()) && (var3[0].getMessageOutlook() & var9.getMessageOutlook()) != var3[0].getMessageOutlook()) {
                  if (verbose) {
                     Verbose.log((Object)("NO match on only one!\nBlueprint Outlook =" + var3[0].getActualPolicyOutlook() + " Msg outlook =" + var3[0].getMessageOutlook() + " getActualPolicyOutlook() & samrtOutline.getMessageOutlook()" + (var3[0].getActualPolicyOutlook() & var9.getMessageOutlook()) + " getPolicyOutlookWithMask(samrtOutline.getMessageOutlookMask() " + var3[0].getPolicyOutlookWithMask(var9.getMessageOutlookMask()) + " getMessageOutlook() & samrtOutline.getMessageOutlook()" + (var3[0].getMessageOutlook() & var9.getMessageOutlook())));
                  }

                  String var12 = var3[0].tellMeWhy(var9.getMessageOutlook());
                  int var14 = var3[0].getErrorCode(var9.getMessageOutlook());
                  throw new SecurityPolicyInspectionException(var14, var12);
               } else {
                  return var3;
               }
            } else {
               ArrayList var10 = new ArrayList();
               Iterator var11 = this.securityAssertionList.iterator();

               while(var11.hasNext()) {
                  var5 = (SmartSecurityPolicyBlueprint)var11.next();
                  if (var5.getMessageOutlook() == var9.getMessageOutlook()) {
                     var10.add(var5);
                  }
               }

               var11 = this.securityAssertionList.iterator();

               while(var11.hasNext()) {
                  var5 = (SmartSecurityPolicyBlueprint)var11.next();
                  if (var5.getMessageOutlook() != var9.getMessageOutlook() && var5.getActualPolicyOutlook() == var9.getMessageOutlook()) {
                     var10.add(var5);
                  }
               }

               StringBuffer var13 = new StringBuffer();
               var11 = this.securityAssertionList.iterator();

               while(true) {
                  while(true) {
                     SmartSecurityPolicyBlueprint var6;
                     do {
                        do {
                           if (!var11.hasNext()) {
                              if (var10.size() <= 0) {
                                 throw new SecurityPolicyInspectionException(var13.toString());
                              }

                              Object[] var15 = var10.toArray();
                              SmartSecurityPolicyBlueprint[] var16 = new SmartSecurityPolicyBlueprint[var15.length];

                              for(int var8 = 0; var8 < var15.length; ++var8) {
                                 var16[var8] = (SmartSecurityPolicyBlueprint)var15[var8];
                              }

                              return var16;
                           }

                           var6 = (SmartSecurityPolicyBlueprint)var11.next();
                        } while(var6.getMessageOutlook() == var9.getMessageOutlook());
                     } while(var6.getActualPolicyOutlook() == var9.getMessageOutlook());

                     if ((var6.getActualPolicyOutlook() & var9.getMessageOutlook()) != var6.getActualPolicyOutlook() && (var6.getActualPolicyOutlook() & var9.getMessageOutlook()) != var6.getPolicyOutlookWithMask(var9.getMessageOutlookMask()) && (var6.getMessageOutlook() & var9.getMessageOutlook()) != var6.getMessageOutlook()) {
                        String var7 = var6.tellMeWhy(var9.getMessageOutlook());
                        if (verbose) {
                           Verbose.log((Object)("Not a possible match policy reason " + var7 + "\nBlueprint Outlook =" + var6.getActualPolicyOutlook() + " Msg outlook =" + var6.getMessageOutlook() + " getActualPolicyOutlook() & samrtOutline.getMessageOutlook()" + (var6.getActualPolicyOutlook() & var9.getMessageOutlook()) + " getPolicyOutlookWithMask(samrtOutline.getMessageOutlookMask() " + var6.getPolicyOutlookWithMask(var9.getMessageOutlookMask()) + " getMessageOutlook() & samrtOutline.getMessageOutlook()" + (var6.getMessageOutlook() & var9.getMessageOutlook())));
                        }

                        var13.append(var7);
                     } else {
                        var10.add(var6);
                     }
                  }
               }
            }
         } else {
            Verbose.log((Object)"There is no information on the incoming SOAP message.");
            Iterator var2 = this.securityAssertionList.iterator();
            var3 = new SmartSecurityPolicyBlueprint[this.securityAssertionList.size()];

            for(int var4 = 0; var2.hasNext(); var3[var4++] = var5) {
               var5 = (SmartSecurityPolicyBlueprint)var2.next();
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   public SmartSecurityPolicyBlueprint[] getSmartPolicyBlueprint() {
      if (this.securityAssertionList != null && 0 != this.getPolicyAlternativeCount()) {
         Object[] var1 = this.securityAssertionList.toArray();
         SmartSecurityPolicyBlueprint[] var2 = new SmartSecurityPolicyBlueprint[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = (SmartSecurityPolicyBlueprint)var1[var3];
         }

         return var2;
      } else {
         return null;
      }
   }

   public SecurityPolicyBlueprint[] getPolicyBlueprint() {
      if (this.securityAssertionList != null && 0 != this.getPolicyAlternativeCount()) {
         this.setPolicyPriorityIndex(this.preference);
         return getSecurityPolicyBlueprintArray(this.securityAssertionList);
      } else {
         return null;
      }
   }

   public PolicyAlternative getFirstPolicyAlternative(PolicySelectionPreference var1) {
      if (this.securityAssertionList != null && 0 != this.getPolicyAlternativeCount()) {
         if (null == var1) {
            var1 = new PolicySelectionPreference();
         }

         this.setPolicyPriorityIndex(var1);
         SmartSecurityPolicyBlueprint[] var2 = this.getSmartPolicyBlueprint();
         SmartSecurityPolicyBlueprint var3 = var2[this.policyPriorityIndex[0]];
         return var3.getPolicyAlternative();
      } else {
         return null;
      }
   }

   protected void setPolicyPriorityIndex(PolicySelectionPreference var1) {
      if (null == var1) {
         var1 = new PolicySelectionPreference();
      }

      this.preference = var1;
      if (1 == this.getPolicyAlternativeCount()) {
         this.policyPriorityIndex = new int[1];
         this.policyPriorityIndex[0] = 0;
      } else {
         this.policyPriorityIndex = this.sortPolicies(this.securityAssertionList, var1);
      }

   }

   public int[] getPolicyPriorityIndex(PolicySelectionPreference var1) {
      if (null != var1 && !var1.isDefaut() && 1 != this.getPolicyAlternativeCount()) {
         return this.sortPolicies(this.securityAssertionList, var1);
      } else {
         int[] var2 = new int[this.getPolicyAlternativeCount()];

         for(int var3 = 0; var3 < var2.length; var2[var3] = var3++) {
         }

         return var2;
      }
   }

   public int[] getPolicyPriorityIndex() {
      if (null == this.policyPriorityIndex) {
         this.setPolicyPriorityIndex(this.preference);
      }

      return this.policyPriorityIndex;
   }

   /** @deprecated */
   private SecurityPolicyAssertionInfo getResponsePolicy() {
      return null == this.normalizedReferencePolicy ? null : null;
   }

   private boolean checkMixPolicyOfWSS() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.getPolicyAlternativeCount(); ++var2) {
         SmartSecurityPolicyBlueprint var3 = (SmartSecurityPolicyBlueprint)this.securityAssertionList.get(var2);
         if (var3.isWss11()) {
            ++var1;
         }
      }

      if (var1 != 0 && var1 != this.getPolicyAlternativeCount()) {
         return true;
      } else {
         return false;
      }
   }

   private int[] sortPolicies(List var1, PolicySelectionPreference var2) {
      if (null == var1) {
         return null;
      } else {
         int[] var3 = new int[var1.size()];
         int var4 = 0;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            SmartSecurityPolicyBlueprint var6 = (SmartSecurityPolicyBlueprint)var1.get(var5);
            var3[var5] = var6.getScore(var2);
            if (var3[var5] > var4) {
               var4 = var3[var5];
            }
         }

         int[] var9 = new int[var1.size()];

         int var7;
         for(int var10 = 0; var10 < var3.length; var4 = var7) {
            var7 = 0;

            for(int var8 = 0; var8 < var3.length; ++var8) {
               if (var3[var8] == var4) {
                  var9[var10++] = var8;
               } else if (var3[var8] < var4 && var3[var8] > var7) {
                  var7 = var3[var8];
               }
            }
         }

         return var9;
      }
   }

   private static SecurityPolicyBlueprint[] getSecurityPolicyBlueprintArray(List var0) {
      if (null == var0) {
         return null;
      } else {
         SecurityPolicyBlueprint[] var1 = new SecurityPolicyBlueprint[var0.size()];

         for(int var2 = 0; var2 < var0.size(); ++var2) {
            var1[var2] = getSecurityPolicyBlueprint(var0, var2);
         }

         return var1;
      }
   }

   private static SecurityPolicyBlueprint getSecurityPolicyBlueprint(List var0, int var1) {
      if (null == var0) {
         return null;
      } else {
         Object var2 = var0.get(var1);
         return ((SmartSecurityPolicyBlueprint)var2).getSecurityPolicyBlueprint();
      }
   }

   private SecurityPolicyBlueprint getSecurityPolicyBlueprint(int var1) {
      return getSecurityPolicyBlueprint(this.securityAssertionList, var1);
   }
}
