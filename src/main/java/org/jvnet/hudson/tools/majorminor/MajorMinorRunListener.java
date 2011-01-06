/**
 * 
 */
package org.jvnet.hudson.tools.majorminor;

import java.io.File;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.Run;
import hudson.model.listeners.RunListener;

/**
 * 
 * Listens for build deletion events and removes symlinks to that build that were
 * created by the major minor plugin using the build name.
 * 
 * @author Oceus Networks - Nithin Thomas
 *
 */
@Extension
public class MajorMinorRunListener extends RunListener
{
	public void onDeleted(Run r)
	{
		// if we have a symlink, delete it
        File link = new File(r.getRootDir(), "..//"+r.getDisplayName());
        link.delete();
	}
	
}
