package com.kookster.chbackport;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;

/**
 *
 * @author Luke Fairchild <lukecfairchild@gmail.com>
 */
@MSExtension("CHBackport")
public class CHBackport extends AbstractExtension {

	public Version getVersion() {
		return new SimpleVersion(1, 0, 0);
	}

	@Override
	public void onStartup() {
		System.out.println("CHBackport loaded: "  + getVersion() + " - kookster");
	}

	@Override
	public void onShutdown() {
		System.out.println("CHBackport unloaded: "  + getVersion() + " - kookster");
	}
}
