/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.kettle.step.plugins.scr;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

/**
 * This Step class is the part of the SpecialCharacterRemover Plugin.
 *  
 * @author Rishu Shrivastava
 * @version 1.1.0
 *
 */
public class SCRStep extends BaseStep implements StepInterface{
	
	

	public SCRStep(StepMeta s, StepDataInterface stepDataInterface, int c,
				   TransMeta t, Trans dis) {
		// TODO Auto-generated constructor stub
		super(s, stepDataInterface, c, t, dis);
	}

	@Override
	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		// Casting to step-specific implementation classes is safe
		SCRStepMeta meta = (SCRStepMeta) smi;
		SCRStepData data = (SCRStepData) sdi;

		return super.init(meta, data);
	}

	@Override
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
			throws KettleException {

		// safely cast the step settings (meta) and runtime info (data) to
		// specific implementations
		SCRStepMeta meta = (SCRStepMeta) smi;
		SCRStepData data = (SCRStepData) sdi;

		// get incoming row, getRow() potentially blocks waiting for more rows,
		// returns null if no more rows expected
		Object[] r = getRow();

		// if no more rows are expected, indicate step is finished and
		// processRow() should not be called again
		if (r == null) {
			setOutputDone();
			return false;
		}

		// the "first" flag is inherited from the base step implementation
		// it is used to guard some processing tasks, like figuring out field
		// indexes
		// in the row structure that only need to be done once
		if (first) {
			first = false;
			// clone the input row structure and place it in our data object
			data.outputRowMeta = getInputRowMeta().clone();
			
			// use meta.getFields() to change it, so it reflects the output row
			// structure
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);
			
			
		}
		
		//choose the pattern based on selection
		SCRAlgoList algolistobj=new SCRAlgoList();
		String selectedPattern=algolistobj.getAlgoPattern(meta.getAlgoBoxItemsSelected(),meta.getCustomCode());
		
		//Call the business logic
		SCRPattern sppattrn=new SCRPattern();
		String cleanpattern=sppattrn.getCleanPattern(data.outputRowMeta, r,meta.getInputDropDataIndex(),selectedPattern,meta.getCustomCode());

		// safely add the output at the end of the output row
		// the row array will be resized if necessary
		Object[] outputRow = RowDataUtil.addValueData(r,
				data.outputRowMeta.size() - 1, cleanpattern);

		// put the row to the output row stream
		putRow(data.outputRowMeta, outputRow);
		
		// log progress if it is time to to so
		if (checkFeedback(getLinesRead())) {
			logBasic("Linenr " + getLinesRead()); // Some basic logging
		}

		// indicate that processRow() should be called again
		return true;
	}

	@Override
	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {

		// Casting to step-specific implementation classes is safe
		SCRStepMeta meta = (SCRStepMeta) smi;
		SCRStepData data = (SCRStepData) sdi;

		super.dispose(meta, data);
	}

}
