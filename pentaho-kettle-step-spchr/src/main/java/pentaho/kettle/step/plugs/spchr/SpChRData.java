package pentaho.kettle.step.plugs.spchr;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * This data class is the part of the SpecialCharacterRemover Plugin.
 * 
 * @author Rishu Shrivastava
 * @version 1.1.0
 *
 */
public class SpChRData extends BaseStepData implements StepDataInterface{
	public RowMetaInterface outputRowMeta;

	public SpChRData() {
		// TODO Auto-generated constructor stub
		super();
	}
}
