/**
 * Copyright (C) 2006 - 2010
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   and other students of AGH University of Science and Technology
 *   as indicated in each file separately.
 *
 * This file is part of jAgE.
 *
 * jAgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jAgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jAgE.  If not, see http://www.gnu.org/licenses/
 */
/*
 * File: CommandLineArgumentsService.java
 * Created: 2010-06-08
 * Author: kpietak
 * $Id: CommandLineArgumentsService.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.argument;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.jage.platform.node.NodeBootstrapper;

/**
 * A default implementation of {@link IRuntimeArgumentsService} based on Apache CLI Commons library.
 * <p>
 * Platform properties are defined in POSIX style (ie. <code>-option value</code>). <br/>
 * Custom properties are defined in the Java property style (ie. <code>-Doption=value</code>).
 * 
 * @author AGH AgE Team
 */
public class CommandLineArgumentsService implements IRuntimeArgumentsService {

	/**
	 * Java property prefix.
	 */
	private static final String CUSTOM_OPTIONS_PREFIX = "D";

	private static final String CUSTOM_OPTIONS_NAME = "property=value";

	private static final String CUSTOM_OPTIONS_DESC = "use value for given property";

	private static final String DESC_STARTER_CLASS = "specifies the starter class; type the fully qualified class name";

	private static final String DESC_HELP = "print this message";

	private static final String PARAM_STARTER_CLASS = "classname";

	private Options options;

	private CommandLine commandLine;

	/**
	 * Default constructor.
	 * 
	 * @param args
	 *            runtime arguments
	 * @throws InvalidRuntimeArgumentsException
	 *             When arguments are not possible to parse.
	 */
	public CommandLineArgumentsService(final String[] args) throws InvalidRuntimeArgumentsException {
		init(args);

	}

	// BEGIN Overridden Methods

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jage.platform.argument.IRuntimeArgumentsService#getPlatformOption(java.lang.String)
	 */
	@Override
	public String getPlatformOption(String key) {
		return commandLine.getOptionValue(key);

	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.jage.platform.argument.IRuntimeArgumentsService#containsPlatformOption(java.lang.String)
	 */
	@Override
	public boolean containsPlatformOption(String key) {
		return commandLine.hasOption(key);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.jage.platform.argument.IRuntimeArgumentsService#getCustomOption(java.lang.String)
	 */
	@Override
	public String getCustomOption(String key) {
		return commandLine.getOptionProperties(CUSTOM_OPTIONS_PREFIX).getProperty(key);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.jage.platform.argument.IRuntimeArgumentsService#containsCustomOption(java.lang.String)
	 */
	@Override
	public boolean containsCustomOption(String key) {
		return commandLine.getOptionProperties(CUSTOM_OPTIONS_PREFIX).containsKey(key);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.jage.platform.argument.IRuntimeArgumentsService#printUsage()
	 */
	@Override
	public void printUsage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java org.jage.platform.node.NodeBootstrapper", options);

	}

	// END Overridden Methods

	// BEGIN Helper Methods

	private void init(String[] args) throws InvalidRuntimeArgumentsException {
		initOptions();
		try {
			createCommandLine(args);
		} catch (ParseException e) {
			throw new InvalidRuntimeArgumentsException(e);
		}
	}

	@SuppressWarnings("static-access")
	private void initOptions() {
		options = new Options();

		options.addOption(NodeBootstrapper.OPT_HELP, false, DESC_HELP);

		Option starter = OptionBuilder.withArgName(PARAM_STARTER_CLASS).hasArg().withDescription(DESC_STARTER_CLASS)
		        .create(NodeBootstrapper.OPT_STARTER_CLASS);
		options.addOption(starter);

		// create a Java property option
		// -Dproperty=value
		Option property = OptionBuilder.withArgName(CUSTOM_OPTIONS_NAME).hasArgs(2).withValueSeparator()
		        .withDescription(CUSTOM_OPTIONS_DESC).create(CUSTOM_OPTIONS_PREFIX);
		options.addOption(property);

	}

	private void createCommandLine(String[] args) throws ParseException {
		// create the command line parser
		CommandLineParser parser = new PosixParser();
		commandLine = parser.parse(options, args);

	}

	// END Helper Methods

}
