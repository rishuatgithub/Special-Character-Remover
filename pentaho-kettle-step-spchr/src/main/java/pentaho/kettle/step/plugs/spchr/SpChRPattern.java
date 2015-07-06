package pentaho.kettle.step.plugs.spchr;

import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMetaInterface;

/**
 * This class is the part of the SpecialCharacterRemover Plugin.
 * 
 * Used for the replace code / pattern.
 * 
 * @author Rishu Shrivastava
 * @version 1.1.0
 *
 */
public class SpChRPattern {
	
	public String getCleanPattern(RowMetaInterface data,Object[] r,String fieldNum,String selectedPattern,String customCode) throws KettleValueException{
	
		String replace_pattern="";
		int _intfieldNum=0;
		
		if(fieldNum==null){
			_intfieldNum=0;
		}else{
			_intfieldNum=Integer.parseInt(fieldNum);
		}
		
		String data_row=data.getString(r, _intfieldNum);
		
		String SpecialCleaner=data_row.replaceAll(selectedPattern, replace_pattern);
		
		return SpecialCleaner;
	
	}

}
