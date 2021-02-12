package com.asx.mdx.lib.util.system;

import java.io.File;
import java.net.URI;

import com.asx.mdx.MDX;

public enum OperatingSystem
{
	LINUX("linux", new String[] { "linux", "unix" }), 
	WINDOWS("windows", new String[] { "win" }), 
	OSX("osx", new String[] { "mac" }), 
	UNKNOWN("unknown", new String[0]);

	private final String NAME;
	private final String[] ALIASES;

	private OperatingSystem(String name, String[] aliases)
	{
		this.NAME = name;
		this.ALIASES = (aliases == null ? new String[0] : aliases);
	}

	public String getName()
	{
		return this.NAME;
	}

	public String[] getAliases()
	{
		return this.ALIASES;
	}

	public boolean isSupported()
	{
		return this != UNKNOWN;
	}

	public String getJavaDir()
	{
		String separator = System.getProperty("file.separator");
		String path = System.getProperty("java.home") + separator + "bin" + separator;

		if ((getCurrentPlatform() == WINDOWS) && (new File(path + "javaw.exe").isFile()))
		{
			return path + "javaw.exe";
		}

		return path + "java";
	}

	public static OperatingSystem getCurrentPlatform()
	{
		String osName = System.getProperty("os.name").toLowerCase();

		for (OperatingSystem os : values())
		{
			for (String alias : os.getAliases())
			{
				if (osName.contains(alias))
				{
					return os;
				}
			}
		}

		return UNKNOWN;
	}

	//TODO: Should add handling for applications such as MultiMC or other launchers. 
	public File getMinecraftWorkingDirectory()
	{
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory;
		
		switch (this)
		{
			case LINUX:
				workingDirectory = new File(userHome, ".minecraft/");
				break;
			case WINDOWS:
				String applicationData = System.getenv("APPDATA");
				String folder = applicationData != null ? applicationData : userHome;

				workingDirectory = new File(folder, ".minecraft/");
				break;
			case OSX:
				workingDirectory = new File(userHome, "Library/Application Support/.minecraft/");
				break;
			default:
				workingDirectory = new File(userHome, ".minecraft/");
		}

		return workingDirectory;
	}

	public static void openLink(URI link)
	{
		try
		{
			Class<?> desktopClass = Class.forName("java.awt.Desktop");
			Object o = desktopClass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
			desktopClass.getMethod("browse", new Class[] { URI.class }).invoke(o, new Object[] { link });
		}
		catch (Throwable e)
		{
			MDX.log().info("Failed to open link " + link.toString(), e);
		}
	}
}
