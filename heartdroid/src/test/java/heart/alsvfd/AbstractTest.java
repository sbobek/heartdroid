/**
*
*     Copyright 2013-15 by Szymon Bobek, Grzegorz J. Nalepa, Mateusz Ślażyński
*
*
*     This file is part of HeaRTDroid.
*     HeaRTDroid is a rule engine that is based on HeaRT inference engine,
*     XTT2 representation and other concepts developed within the HeKatE project .
*
*     HeaRTDroid is free software: you can redistribute it and/or modify
*     it under the terms of the GNU General Public License as published by
*     the Free Software Foundation, either version 3 of the License, or
*     (at your option) any later version.
*
*     HeaRTDroid is distributed in the hope that it will be useful,
*     but WITHOUT ANY WARRANTY; without even the implied warranty of
*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*     GNU General Public License for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with HeaRTDroid.  If not, see <http://www.gnu.org/licenses/>.
*
**/


package heart.alsvfd;

import heart.exceptions.BuilderException;
import heart.xtt.Type;
import heart.xtt.Type.Builder;

import java.util.List;

import static org.junit.Assert.*;

public class AbstractTest {

	public void testException(Command command, ExceptionTestParameters parameters){
		try {
			command.execute(parameters.thisValue, parameters.otherValue, parameters.type);
			fail("Expected error!");
		} catch (Exception e) {
			boolean exceptionClassIsCorrect = e.getClass().equals(parameters.expectedException);
			boolean errorMessageIsCorrect = e.getMessage().equals(parameters.exceptedMessage);
			assertTrue(errorMessageIsCorrect);
			assertTrue(exceptionClassIsCorrect);
		}
	}

	protected Type createTypeWithDomain(List<Value> domain)
			throws BuilderException {
		SetValue domainValue = new SetValue(domain);
		Builder builder = createBuilderParameters(domainValue);

		return builder.build();
	}
	
	protected String getNotInTheDomainMessage(Value object){
		return "Value "+object+" not in the domain";
	}

	private Builder createBuilderParameters(SetValue domain) {
		Builder builder = new Builder();
		builder.setBase(Type.BASE_NUMERIC);
		builder.setDescription("description");
		builder.setId("id");
		builder.setLength(10);
		builder.setDomain(domain);
		builder.setName("name");
		builder.setOrdered(Type.ORDERED_YES);
		builder.setPrecision(5);

		return builder;
	}
}
