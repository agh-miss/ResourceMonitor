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
package org.jage.examples.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.exception.ComponentException;
import org.jage.property.DuplicatePropertyNameException;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.functions.AvgFunction;
import org.jage.property.functions.CountFunction;
import org.jage.property.functions.MaxFunction;
import org.jage.property.functions.MinFunction;
import org.jage.property.functions.SumFunction;
import org.jage.workplace.IsolatedSimpleWorkplace;

public class FunctionsSimpleWorkspace extends IsolatedSimpleWorkplace {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(FunctionsSimpleWorkspace.class);

    @Override
    public void init() throws ComponentException {
        super.init();

        try {
	        this.addFunction(new CountFunction("count", "@Agents[*].value"));
	        this.addFunction(new SumFunction("sum", "@Agents[*].value"));
	        this.addFunction(new AvgFunction("avg", "@Agents[*].value", -1));
	        this.addFunction(new MinFunction("min", "@Agents[*].value", -1));
	        this.addFunction(new MaxFunction("max", "@Agents[*].value", -1));
        } catch (DuplicatePropertyNameException e) {
	        LOG.error("Could not create property function", e);
        }
    }

    @Override
    public void step() {
        super.step();

        String[] names = new String[] {"count", "sum", "avg", "min", "max"};
        Object[] values = new Object[5];

        try {
        	for (int i = 0; i < values.length; i++) {
	            values[i] = this.getProperty(names[i]).getValue();
        	}
        } catch (InvalidPropertyPathException e) {
	        e.printStackTrace();
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
	        builder.append(names[i] + "=" + values[i] + ", ");
        }
        LOG.info("{}: {}", this, builder.toString());
    }
}
