package org.jboss.tools.esb.core.model.handlers;

import org.jboss.tools.common.meta.action.impl.handlers.DefaultRedirectHandler;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;

public class OpenESBResourceHandler extends DefaultRedirectHandler {

	protected XModelObject getTrueSource(XModelObject source) {
		String attr = action.getProperty("attribute");
		String path = source.getAttributeValue(attr);
		if(path == null || path.length() == 0) return null;
		XModelObject o = source.getModel().getByPath(path);
		if(o != null) return o;
		XModelObject r = FileSystemsHelper.getWebRoot(source.getModel());
		if(r == null) return null;
		return r.getChildByPath(path);
	}

}