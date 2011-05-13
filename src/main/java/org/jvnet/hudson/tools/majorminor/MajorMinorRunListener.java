/**
 * 
 */
package org.jvnet.hudson.tools.majorminor;

import hudson.Extension;
import hudson.Util;
import hudson.model.Run;
import hudson.model.listeners.RunListener;
import hudson.util.RunList;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * Listens for build deletion events and removes symlinks to that build that
 * were created by the major minor plugin using the build name.
 * 
 * @author Oceus Networks - Nithin Thomas
 * 
 */
@Extension
public class MajorMinorRunListener<R extends Run> extends RunListener<R>
{
	private static final Logger LOGGER = Logger.getLogger(MajorMinorRunListener.class
			.getName());
	
	public void onDeleted(R r)
	{
		LOGGER.log(Level.FINEST, "Deleted called");
		// if we have a symlink, delete it
		RunList<R> prevBuilds = r.getParent().getBuilds();
		Run latestBuildWithSameName = null;
		// note that the deleted build is also a member of prevBuilds because that build is not deleted 
		// until after all listeners are finished
		for (Run build : prevBuilds)
		{
			if (build != r && build.getDisplayName().equals(r.getDisplayName()))
			{
				latestBuildWithSameName = build;
				break;
			}
		}
		if (latestBuildWithSameName != null)
		{
			LOGGER.log(Level.FINE, "Previous Build with same name found: " + latestBuildWithSameName.getDisplayName()
					+ " built on: " + new SimpleDateFormat().format(latestBuildWithSameName.getTime()));
			try
			{
				Util.createSymlink(
						new File(latestBuildWithSameName.getRootDir(), ".."),
						latestBuildWithSameName.getId(),
						r.getDisplayName(), null);
			} catch (InterruptedException e)
			{
			}
		} else
		{
			LOGGER.log(Level.FINE, "No previous builds with same name");
			File link = new File(r.getRootDir(), "..//" + r.getDisplayName());
			link.delete();
		}
	}

}
