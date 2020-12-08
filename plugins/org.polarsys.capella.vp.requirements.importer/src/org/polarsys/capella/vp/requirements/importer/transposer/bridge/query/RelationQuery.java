/*******************************************************************************
 * Copyright (c) 2016 THALES GLOBAL SERVICES.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.vp.requirements.importer.transposer.bridge.query;

import java.util.Collections;

import org.eclipse.emf.diffmerge.api.scopes.IEditableModelScope;
import org.eclipse.emf.diffmerge.bridge.mapping.api.IQueryExecution;
import org.eclipse.emf.diffmerge.bridge.mapping.api.IQueryHolder;
import org.eclipse.emf.diffmerge.bridge.mapping.impl.Query;
import org.eclipse.rmf.reqif10.SpecRelation;
import org.polarsys.capella.vp.requirements.importer.transposer.bridge.ReqIFMappingQueries;

public class RelationQuery extends Query<IEditableModelScope, SpecRelation> {

  public RelationQuery(IQueryHolder<? extends IEditableModelScope> parent) {
    super(parent);
  }

  public Iterable<SpecRelation> evaluate(IEditableModelScope source, IQueryExecution environment) {
    try {
      return ReqIFMappingQueries.getAllRelations(source);
    } catch (Exception e) {
      return Collections.emptySet();
    }
  }
}
