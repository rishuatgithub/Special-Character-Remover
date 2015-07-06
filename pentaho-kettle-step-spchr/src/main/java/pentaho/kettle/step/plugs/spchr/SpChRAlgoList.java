package pentaho.kettle.step.plugs.spchr;

import java.util.HashMap;
import java.util.Map;

/**
 * This AlgoList Class is the part of the SpecialCharacterRemover Plugin.
 * Used for defining the algorithms used in the plugin process.
 * 
 * @author Rishu Shrivastava
 * @version 1.1.0
 *
 */
public class SpChRAlgoList {
	
	private static Map<String,String> algomap;
	private String selectedPattern;
		
	public String getAlgoPattern(String selectedalgo,String customCode){
		
		algomap=new HashMap<String, String>();
		
		algomap.put("Remove all the Special Characters other than A-Z,a-z,0-9 including white-spaces", "[^A-Za-z0-9]");
		algomap.put("Remove all the Special Characters other than A-Z,a-z,0-9 keep the white-spaces", "[^A-Za-z0-9\\s]");
		algomap.put("Remove anything outside ASCII code 0 to 255", "[^\\x00-\\x7F]");
		algomap.put("Remove Unicode Block", "\\P{InBasic_Latin}");
		algomap.put("Keep Unicode Block, remove the rest", "\\p{InBasic_Latin}");
		algomap.put("Custom Regular Expression", "CUS");
		algomap.put("Keep A-Z,a-z,0-9 and ADD Exceptions","EXC");
		algomap.put("Default Selection", "[^A-Za-z0-9\\s]");
		
		
		if(algomap.containsKey(selectedalgo)){
			String val=algomap.get(selectedalgo);
			
			if(val == "CUS"){
				setSelectedPattern(customCode);
			}else if(val == "EXC"){
				setSelectedPattern(generatePattern(customCode.substring(1, customCode.length()-1)));
			}else{
				setSelectedPattern(algomap.get(selectedalgo));
			}		
			
		}else{
			setSelectedPattern(algomap.get("Default Selection"));
		}
		
		return getSelectedPattern();
	}


	public String getSelectedPattern() {
		return selectedPattern;
	}


	public void setSelectedPattern(String selectedPattern) {
		this.selectedPattern = selectedPattern;
	}
	
	private String generatePattern(String custom){
		
		StringBuilder sb=new StringBuilder();
		
		sb.append("[^A-Za-z0-9\\\\");
		
		if(custom.contains("\\")){
			String nwCustom=custom.replace("\\", "\\\\\\\\");
			sb.append(nwCustom);
		}else{
			sb.append(custom);
		}
		
		sb.append("]");
		
		return sb.toString();
	}

	
}
