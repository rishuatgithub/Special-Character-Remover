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

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.injection.Injection;
import org.pentaho.di.core.injection.InjectionSupported;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.kettle.step.plugins.scr.model.SCRPojo;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

/**
 * This class is part of the demo step plug-in implementation.
 * It demonstrates the basics of developing a plug-in step for PDI.
 *
 * This class is the implementation of StepMetaInterface.
 * Classes implementing this interface need to:
 *
 * - keep track of the step settings
 * - serialize step settings both to xml and a repository
 * - provide new instances of objects implementing StepDialogInterface, StepInterface and StepDataInterface
 * - report on how the step modifies the meta-data of the row-stream (row structure and field types)
 * - perform a sanity-check on the settings provided by the user
 *
 */
@Step(
		id = "SCRStep",
		name = "SCRStep.Name",
		description = "SCRStep.TooltipDesc",
		image = "org/pentaho/kettle/step/plugins/scr/resources/scr.png",
		categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Experimental",
		i18nPackageName = "org.pentaho.kettle.step.plugins.scr",
		documentationUrl = "SCRStep.DocumentationURL",
		casesUrl = "SCRStep.CasesURL",
		forumUrl = "SCRStep.ForumURL"
)
@InjectionSupported(localizationPrefix = "SCRStepMeta.Injection.")
public class SCRStepMeta extends BaseStepMeta implements StepMetaInterface{

	/**
	 *  The PKG member is used when looking up internationalized strings.
	 *  The properties file with localized keys is expected to reside in
	 *  {the package of the class specified}/messages/messages_{locale}.properties
	 */
	private static final Class<?> PKG = SCRStepMeta.class;

	/**
	private String outputField;
	private String inputDropData;
	private String inputDropDataIndex;
	private String[] algoBoxItems={ "Remove all the Special Characters other than A-Z,a-z,0-9 including white-spaces",
									"Remove all the Special Characters other than A-Z,a-z,0-9 keep the white-spaces",
									"Remove anything outside ASCII code 0 to 255",
									"Remove Unicode Block",
									"Keep Unicode Block, remove the rest",
									"Keep A-Z,a-z,0-9 and ADD Exceptions",
									"Custom Regular Expression"
	};
	private String algoBox¬ItemsSelected;
	private String customCode;
	**/

	SCRPojo scrPojo = new SCRPojo();


	/**
	 * Constructor should call super() to make sure the base class has a chance to initialize properly.
	 */
	public SCRStepMeta() {
		super();
	}

	/**
	 * Called by Spoon to get a new instance of the SWT dialog for the step.
	 * A standard implementation passing the arguments to the constructor of the step dialog is recommended.
	 *
	 * @param shell    an SWT Shell
	 * @param meta     description of the step
	 * @param transMeta  description of the transformation
	 * @param name    the name of the step
	 * @return       new instance of a dialog for this step
	 */
	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
		return new SCRStepDialog(shell, meta, transMeta, name);
	}

	/**
	 * Called by PDI to get a new instance of the step implementation.
	 * A standard implementation passing the arguments to the constructor of the step class is recommended.
	 *
	 * @param stepMeta        description of the step
	 * @param stepDataInterface    instance of a step data class
	 * @param cnr          copy number
	 * @param transMeta        description of the transformation
	 * @param disp          runtime implementation of the transformation
	 * @return            the new instance of a step implementation
	 */
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans disp) {
		return new SCRStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	/**
	 * Called by PDI to get a new instance of the step data class.
	 */
	public StepDataInterface getStepData() {
		return new SCRStepData();
	}

	public void setDefault() {
		scrPojo.setOutputField("result");
		scrPojo.setAlgoBoxItemsSelected("Remove all the Special Characters other than A-Z,a-z,0-9 including white-spaces");
	}


	/**
	 * This method is used when a step is duplicated in Spoon. It needs to return a deep copy of this
	 * step meta object. Be sure to create proper deep copies if the step configuration is stored in
	 * modifiable objects.
	 * See org.pentaho.di.trans.steps.rowgenerator.RowGeneratorMeta.clone() for an example on creating
	 * a deep copy.
	 *
	 * @return a deep copy of this
	 */
	public Object clone() {
		return super.clone();
	}

	/**
	 * This method is called by Spoon when a step needs to serialize its configuration to XML. The expected
	 * return value is an XML fragment consisting of one or more XML tags.
	 * Please use org.pentaho.di.core.xml.XMLHandler to conveniently generate the XML.
	 *
	 * @return a string containing the XML serialization of this step
	 */
	public String getXML() throws KettleValueException {
		return XMLHandler.addTagValue("outputField", scrPojo.getOutputField()) +
				XMLHandler.addTagValue("inputDropData", scrPojo.getInputDropData()) +
				XMLHandler.addTagValue("inputDropDataIndex", scrPojo.getInputDropDataIndex()) +
				XMLHandler.addTagValue("algoBoxItemsSelected", scrPojo.getAlgoBoxItemsSelected()) +
				XMLHandler.addTagValue("customCode", scrPojo.getCustomCode());
	}

	/**
	 * This method is called by PDI when a step needs to load its configuration from XML.
	 * Please use org.pentaho.di.core.xml.XMLHandler to conveniently read from the
	 * XML node passed in.
	 *
	 * @param stepnode  the XML node containing the configuration
	 * @param databases  the databases available in the transformation
	 * @param metaStore the metaStore to optionally read from
	 */
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {
		try {
			scrPojo.setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "outputField")));
			scrPojo.setInputDropData(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "inputDropData")));
			scrPojo.setInputDropDataIndex(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "inputDropDataIndex")));
			scrPojo.setAlgoBoxItemsSelected(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "algoBoxItemsSelected")));
			scrPojo.setCustomCode(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "customCode")));

		} catch (Exception e) {
			throw new KettleXMLException("Plugin unable to read step info from XML node", e);
		}
	}

	/**
	 * This method is called by Spoon when a step needs to serialize its configuration to a repository.
	 * The repository implementation provides the necessary methods to save the step attributes.
	 *
	 * @param rep                 the repository to save to
	 * @param metaStore           the metaStore to optionally write to
	 * @param id_transformation   the id to use for the transformation when saving
	 * @param id_step             the id to use for the step  when saving
	 */
	public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step) throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, "outputField", scrPojo.getOutputField());
			rep.saveStepAttribute(id_transformation, id_step, "inputDropData", scrPojo.getInputDropData());
			rep.saveStepAttribute(id_transformation, id_step, "inputDropDataIndex", scrPojo.getInputDropDataIndex());
			rep.saveStepAttribute(id_transformation, id_step, "algoBoxItemsSelected", scrPojo.getAlgoBoxItemsSelected());
			rep.saveStepAttribute(id_transformation, id_step, "customCode", scrPojo.getCustomCode());

		} catch (Exception e) {
			throw new KettleException("Unable to save step into repository:"+ id_step, e);
		}
	}

	/**
	 * This method is called by PDI when a step needs to read its configuration from a repository.
	 * The repository implementation provides the necessary methods to read the step attributes.
	 *
	 * @param rep        the repository to read from
	 * @param metaStore  the metaStore to optionally read from
	 * @param id_step    the id of the step being read
	 * @param databases  the databases available in the transformation
	 */
	public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases) throws KettleException {
		try {
			scrPojo.setOutputField(rep.getStepAttributeString(id_step, "outputField"));
			scrPojo.setInputDropData(rep.getStepAttributeString(id_step, "inputDropData"));
			scrPojo.setInputDropDataIndex(rep.getStepAttributeString(id_step, "inputDropDataIndex"));
			scrPojo.setAlgoBoxItemsSelected(rep.getStepAttributeString(id_step, "algoBoxItemsSelected"));
			scrPojo.setCustomCode(rep.getStepAttributeString(id_step, "customCode"));

		} catch (Exception e) {
			throw new KettleException("Unable to load step from repository", e);
		}
	}

	@Override
	public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
						  VariableSpace space, Repository repository, IMetaStore metaStore) throws KettleStepException {

		/*
		 * This implementation appends the outputField to the row-stream
		 */

		// a value meta object contains the meta data for a field
		ValueMetaInterface v = new ValueMetaString(scrPojo.getOutputField());

		// setting trim type to "both"
		v.setTrimType(ValueMetaInterface.TRIM_TYPE_BOTH);

		// the name of the step that adds this field
		v.setOrigin(name);

		// modify the row structure and add the field this step generates
		inputRowMeta.addValueMeta(v);

	}

	@Override
	public void check(List<CheckResultInterface> remarks, TransMeta transmeta,
					  StepMeta stepMeta, RowMetaInterface prev, String[] input,
					  String[] output, RowMetaInterface info) {

		CheckResult cr;

		// See if there are input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK,
					BaseMessages.getString(PKG,
							"SCRMeta.CheckResult.ReceivingRows.OK"), stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR,
					BaseMessages.getString(PKG,
							"SCRMeta.CheckResult.ReceivingRows.ERROR"), stepMeta);
			remarks.add(cr);
		}

	}

	/**
	public String getOutputField() {
		return outputField;
	}

	public void setOutputField(String outputField) {
		this.outputField = outputField;
	}

	public String getInputDropData() {
		
		return inputDropData;
	}

	public void setInputDropData(String inputDropData) {
		this.inputDropData = inputDropData;
	}
	
	public String getInputDropDataIndex() {
		return inputDropDataIndex;
	}

	public void setInputDropDataIndex(String inputDropDataIndex) {
		this.inputDropDataIndex = inputDropDataIndex;
	}

	public String[] getAlgoBoxItems() {
		return algoBoxItems;
	}

	public void setAlgoBoxItems(String[] algoBoxItems) {
		
		this.algoBoxItems = algoBoxItems;
	}

	public String getAlgoBoxItemsSelected() {
		return algoBoxItemsSelected;
	}

	public void setAlgoBoxItemsSelected(String algoBoxItemsSelected) {
		this.algoBoxItemsSelected = algoBoxItemsSelected;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}
**/
	

}
