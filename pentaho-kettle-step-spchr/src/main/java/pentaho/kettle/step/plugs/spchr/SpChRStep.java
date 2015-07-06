package pentaho.kettle.step.plugs.spchr;

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
public class SpChRStep extends BaseStep implements StepInterface{
	
	

	public SpChRStep(StepMeta s, StepDataInterface stepDataInterface, int c,
			TransMeta t, Trans dis) {
		// TODO Auto-generated constructor stub
		super(s, stepDataInterface, c, t, dis);
	}

	@Override
	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		// Casting to step-specific implementation classes is safe
		SpChRMeta meta = (SpChRMeta) smi;
		SpChRData data = (SpChRData) sdi;

		return super.init(meta, data);
	}

	@Override
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
			throws KettleException {

		// safely cast the step settings (meta) and runtime info (data) to
		// specific implementations
		SpChRMeta meta = (SpChRMeta) smi;
		SpChRData data = (SpChRData) sdi;

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
		SpChRAlgoList algolistobj=new SpChRAlgoList();
		String selectedPattern=algolistobj.getAlgoPattern(meta.getAlgoBoxItemsSelected(),meta.getCustomCode());
		
		//Call the business logic
		SpChRPattern sppattrn=new SpChRPattern();
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
		SpChRMeta meta = (SpChRMeta) smi;
		SpChRData data = (SpChRData) sdi;

		super.dispose(meta, data);
	}

}
