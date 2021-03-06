/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.esb.project.ui.wizards.pages;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jst.common.project.facet.core.JavaFacet;
import org.eclipse.jst.j2ee.internal.plugin.J2EEUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.web.ui.internal.wizards.DataModelFacetCreationWizardPage;
import org.jboss.tools.esb.core.ESBProjectConstant;
import org.jboss.tools.esb.project.ui.messages.JBossESBUIMessages;

public class ESBProjectFirstPage extends DataModelFacetCreationWizardPage {

	public ESBProjectFirstPage(IDataModel dataModel, String pageName) {
		super(dataModel, pageName);
		setTitle(JBossESBUIMessages.ESBProjectFirstPage_Title);
		setDescription(JBossESBUIMessages.ESBProjectFirstPage_Description);
	}

	protected String getModuleTypeID() {
		return ESBProjectConstant.ESB_PROJECT_FACET;
	}
	
//	protected void createPresetPanel(Composite top) {
//		new Label(top, SWT.NONE);
//	}
	protected void createPrimaryFacetComposite(Composite top) {
		super.createPrimaryFacetComposite(top);
		super.handlePrimaryFacetVersionSelectedEvent();
	}

	@Override
	protected void validatePage() {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				validatePage(true);
			}
			
		});
		
	}
	
	@Override
	protected IDialogSettings getDialogSettings() {
        return J2EEUIPlugin.getDefault().getDialogSettings();
    }
	
	
	@Override
	protected Set<IProjectFacetVersion> getFacetConfiguration( final IProjectFacetVersion primaryFacetVersion ) {
	    final Set<IProjectFacetVersion> config = new HashSet<IProjectFacetVersion>();
		IFacetedProjectWorkingCopy fpjwc = (IFacetedProjectWorkingCopy) this.model
				.getProperty(FACETED_PROJECT_WORKING_COPY);
		for (IProjectFacet fixedFacet : fpjwc.getFixedProjectFacets()) {
			if (fixedFacet == primaryFacetVersion.getProjectFacet()) {
				config.add(primaryFacetVersion);
			} else if (fixedFacet == JavaFacet.FACET) {
				IProjectFacetVersion v = getDefaultJavaVersion();
				config.add(getDefaultJavaVersion());
			} else {
				config.add(fpjwc.getHighestAvailableVersion(fixedFacet));
			}
		}
	    return config;
	}
	
	
    private static final Map<String,IProjectFacetVersion> EXEC_ENV_TO_FACET_VER= new HashMap<String,IProjectFacetVersion>();
    static {
    	EXEC_ENV_TO_FACET_VER.put( "1.3",JavaFacet.VERSION_1_3 ); //$NON-NLS-1$
    	EXEC_ENV_TO_FACET_VER.put( "1.4",JavaFacet.VERSION_1_4 ); //$NON-NLS-1$
    	EXEC_ENV_TO_FACET_VER.put( "1.5",JavaFacet.VERSION_1_5 ); //$NON-NLS-1$
    	EXEC_ENV_TO_FACET_VER.put( "1.6",JavaFacet.VERSION_1_6 ); //$NON-NLS-1$
    	EXEC_ENV_TO_FACET_VER.put( "1.7",JavaFacet.VERSION_1_7 ); //$NON-NLS-1$
    }
    
	protected IProjectFacetVersion getDefaultJavaVersion() {
		IProjectFacetVersion val = EXEC_ENV_TO_FACET_VER.get(getCompilerLevel());
		return val == null ? JavaFacet.VERSION_1_5 : val; 
	}
	
    public static String getCompilerLevel() {
        String level = JavaCore.getOption( JavaCore.COMPILER_COMPLIANCE );
        if( level == null ) 
        	level = (String) JavaCore.getDefaultOptions().get( JavaCore.COMPILER_COMPLIANCE );
        return level;
    }
}
