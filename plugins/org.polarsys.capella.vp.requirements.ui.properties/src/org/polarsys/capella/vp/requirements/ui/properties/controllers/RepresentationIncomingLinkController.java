/*******************************************************************************
 * Copyright (c) 2017, 2018 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.vp.requirements.ui.properties.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.polarsys.capella.common.mdsofa.common.misc.Couple;
import org.polarsys.capella.core.business.queries.IBusinessQuery;
import org.polarsys.capella.core.business.queries.capellacore.BusinessQueriesProvider;
import org.polarsys.capella.core.data.capellacore.CapellaElement;
import org.polarsys.capella.vp.requirements.CapellaRequirements.CapellaRequirementsPackage;
import org.polarsys.capella.vp.requirements.model.helpers.RelationAnnotationHelper;
import org.polarsys.kitalpha.vp.requirements.Requirements.RelationType;
import org.polarsys.kitalpha.vp.requirements.Requirements.Requirement;

/**
 */
public class RepresentationIncomingLinkController extends AbstractAllocationController {
  /**
   * Override this to display elements on Browse wizard
   */
  @Override
  protected IBusinessQuery getReadOpenValuesQuery(EObject semanticElement) {
    return BusinessQueriesProvider.getInstance().getContribution(ViewpointPackage.Literals.DREPRESENTATION,
        CapellaRequirementsPackage.Literals.CAPELLA_INCOMING_RELATION__SOURCE);
  }

  /**
   * Override this to process returned elements from Browse wizard
   */
  @Override
  public List<EObject> writeOpenValues(EObject semanticElement, EStructuralFeature semanticFeature,
      List<EObject> values) {
    if (semanticElement instanceof DRepresentationDescriptor) {
      DRepresentationDescriptor descriptor = (DRepresentationDescriptor) semanticElement;
      for (EObject eObj : values) {
        if (eObj instanceof Requirement) {
          Requirement requirement = (Requirement) eObj;
          Collection<Couple<EObject, EObject>> elts = new ArrayList<Couple<EObject, EObject>>();

          DiagramIncomingLink tempIncomingLink = createTempIncomingLink(semanticElement, null, requirement, null);
          elts.add(new Couple<EObject, EObject>(requirement, getDefaultType(tempIncomingLink)));
          RelationAnnotationHelper.addAllocations(descriptor, RelationAnnotationHelper.IncomingRelationAnnotation,
              elts);
        }
      }
    }
    return null;
  }

  /**
   * Override this to display elements on the table
   */
  @Override
  public List<EObject> loadValues(EObject semanticElement, EStructuralFeature semanticFeature) {
    List<EObject> result = new ArrayList<EObject>();
    if (semanticElement instanceof DRepresentationDescriptor) {
      for (Entry<String, Couple<Requirement, RelationType>> allocation : RelationAnnotationHelper
          .getAllocations((DRepresentationDescriptor) semanticElement, RelationAnnotationHelper.IncomingRelationAnnotation)
          .entrySet()) {
        DiagramIncomingLink tempIncomingLink = createTempIncomingLink(semanticElement, allocation.getKey(),
            allocation.getValue().getKey(), allocation.getValue().getValue());
        result.add(tempIncomingLink);
      }
    }
    return result;
  }

  protected DiagramIncomingLink createTempIncomingLink(EObject semanticElement, String id, Requirement requirement,
      RelationType relationType) {
    DiagramIncomingLink tempIncomingLink = new DiagramIncomingLink((DRepresentationDescriptor) semanticElement, id);
    tempIncomingLink.setSource(requirement);
    tempIncomingLink.setRelationType(relationType);

    // Choose the element containing the diagram as the source of the temporary link
    if (semanticElement instanceof DSemanticDecorator
        && ((DSemanticDecorator) semanticElement).getTarget() instanceof CapellaElement)
      tempIncomingLink.setTarget((CapellaElement) ((DSemanticDecorator) semanticElement).getTarget());
    return tempIncomingLink;
  }
}
