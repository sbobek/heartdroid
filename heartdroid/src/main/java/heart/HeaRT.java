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


package heart;


import java.util.LinkedList;
import java.util.AbstractMap.SimpleEntry;

import heart.Debug.Level;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.UnknownValueException;
import heart.inference.DataDrivenInference;
import heart.inference.FixedOrderInference;
import heart.inference.GoalDrivenInference;
import heart.inference.InferenceAlgorithm;
import heart.uncertainty.ConflictSet;
import heart.uncertainty.UncertainTrue;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.XTTModel;

public class HeaRT {
	
	private static final WorkingMemory wm = new WorkingMemory();
	
	public static void fixedOrderInference(XTTModel model, String[] tablesNames) throws UnsupportedOperationException, NotInTheDomainException, AttributeNotRegisteredException{
		fixedOrderInference(model, tablesNames, new Configuration.Builder().getDefaultConfiguration());
	}
	
	public static void fixedOrderInference(XTTModel model, String[] tablesNames, Configuration cs) throws UnsupportedOperationException, NotInTheDomainException, AttributeNotRegisteredException{
		new FixedOrderInference(wm,model,cs).start(new InferenceAlgorithm.TableParameters(tablesNames));
	}
	
	public static void dataDrivenInference(XTTModel model, String[] tablesNames) throws UnsupportedOperationException, NotInTheDomainException, AttributeNotRegisteredException{
		dataDrivenInference(model, tablesNames, new Configuration.Builder().getDefaultConfiguration());
	}
	
	public static void dataDrivenInference(XTTModel model, String[] tablesNames, Configuration cs)throws UnsupportedOperationException, NotInTheDomainException, AttributeNotRegisteredException{
        new DataDrivenInference(wm,model,cs).start(new InferenceAlgorithm.TableParameters(tablesNames));
	}
	
	public static void goalDrivenInference(XTTModel model, String[] tablesNames)throws UnsupportedOperationException, NotInTheDomainException, AttributeNotRegisteredException{
		goalDrivenInference(model, tablesNames,  new Configuration.Builder().getDefaultConfiguration());
	}
	
	public static void goalDrivenInference(XTTModel model, String[] tablesNames,  Configuration cs)throws UnsupportedOperationException, NotInTheDomainException, AttributeNotRegisteredException{
        new GoalDrivenInference(wm,model,cs).start(new InferenceAlgorithm.TableParameters(tablesNames));
	}



	public static WorkingMemory getWm() {
		return wm;
	}
	
	

	
}
