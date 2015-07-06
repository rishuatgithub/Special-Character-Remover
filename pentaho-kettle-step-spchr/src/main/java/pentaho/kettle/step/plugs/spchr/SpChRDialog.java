package pentaho.kettle.step.plugs.spchr;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

/**
 * This dialog class is the part of the SpecialCharacterRemover Plugin.
 * 
 * @author Rishu Shrivastava
 * @version 1.1.0
 * 
 */
public class SpChRDialog extends BaseStepDialog implements StepDialogInterface{
	
	private static Class<?> PKG = SpChRMeta.class;   // for il8n purposes

	private SpChRMeta meta;
	
	private Text wFieldName;
	private Combo wInputDrop;
	private RowMetaInterface prevFields=null;
	private Map<String, String> prevFieldIndexMap;
	private Combo wAlgoBox;
	private Text wCustomLabel;
	//private Color customCodeColor;

	public SpChRDialog(Shell parent, Object in, TransMeta transMeta, String sname) {
		super(parent, (BaseStepMeta) in, transMeta, sname);
		meta = (SpChRMeta) in;
	}

	public String open() {

		// store some convenient SWT variables
		Shell parent = getParent();
		Display display = parent.getDisplay();

		// SWT code for preparing the dialog
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN
				| SWT.MAX);
		props.setLook(shell);
		setShellImage(shell, meta);

		// The ModifyListener used on all controls. It will update the meta
		// object to
		// indicate that changes are being made.
		ModifyListener lsMod = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				meta.setChanged();
			}
		};

		// Save the value of the changed flag on the meta object. If the user
				// cancels
				// the dialog, it will be restored to this saved value.
				// The "changed" variable is inherited from BaseStepDialog
		changed = meta.hasChanged();
		
		// ------------------------------------------------------- //
		// SWT code for building the actual settings dialog //
		// ------------------------------------------------------- //
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;
		
		//Setting some color standards
		final Color GRAY=display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT);
		final Color TEXBLUR=display.getSystemColor(SWT.COLOR_GRAY);
		final Color RED=display.getSystemColor(SWT.COLOR_RED);
		final Color GREEN=display.getSystemColor(SWT.COLOR_GREEN);

		shell.setLayout(formLayout);
		shell.setText(BaseMessages.getString(PKG, "SpChRMeta.Shell.Title"));
		

		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		// Stepname line
		wlStepname = new Label(shell, SWT.RIGHT);
		wlStepname.setText(BaseMessages.getString(PKG, "System.Label.StepName"));
		props.setLook(wlStepname);
		fdlStepname = new FormData();
		fdlStepname.left = new FormAttachment(0, 0);
		fdlStepname.right = new FormAttachment(middle, -margin);
		fdlStepname.top = new FormAttachment(0, margin);
		wlStepname.setLayoutData(fdlStepname);

		wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wStepname.setText(stepname);
		props.setLook(wStepname);
		wStepname.addModifyListener(lsMod);
		fdStepname = new FormData();
		fdStepname.left = new FormAttachment(middle, 0);
		fdStepname.top = new FormAttachment(0, margin);
		fdStepname.right = new FormAttachment(100, 0);
		wStepname.setLayoutData(fdStepname);
		
		// output field value
		Label wlValName = new Label(shell, SWT.RIGHT);
		wlValName.setText(BaseMessages.getString(PKG,"SpChRMeta.FieldName.Label"));
		props.setLook(wlValName);
		FormData fdlValName = new FormData();
		fdlValName.left = new FormAttachment(0, 0);
		fdlValName.right = new FormAttachment(middle, -margin);
		fdlValName.top = new FormAttachment(wStepname, margin);
		wlValName.setLayoutData(fdlValName);

		wFieldName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wFieldName);
		wFieldName.addModifyListener(lsMod);
		FormData fdValName = new FormData();
		fdValName.left = new FormAttachment(middle, 0);
		fdValName.right = new FormAttachment(100, 0);
		fdValName.top = new FormAttachment(wStepname, margin);
		wFieldName.setLayoutData(fdValName);
		
			
		//field num Dropdown
		
		Label inputDrop=new Label(shell,SWT.RIGHT);
		inputDrop.setText(BaseMessages.getString(PKG,"SpChRMeta.FieldNum.Label"));
		props.setLook(inputDrop);
		FormData fdInputDrop=new FormData();
		fdInputDrop.left=new FormAttachment(0,0);
		fdInputDrop.right=new FormAttachment(middle,-margin);
		fdInputDrop.top=new FormAttachment(wFieldName,margin);
		inputDrop.setLayoutData(fdInputDrop);
		
		wInputDrop=new Combo(shell, SWT.DROP_DOWN);
		props.setLook(wInputDrop);
		wInputDrop.addModifyListener(lsMod);
		FormData fdwInputDrop = new FormData();
		fdwInputDrop.left = new FormAttachment(middle, 0);
		fdwInputDrop.right = new FormAttachment(100, 0);
		fdwInputDrop.top = new FormAttachment(wFieldName, margin);
		wInputDrop.setLayoutData(fdwInputDrop);
		
		//Select algorithm combo box
		Label algoBox=new Label(shell, SWT.RIGHT);
		algoBox.setText(BaseMessages.getString(PKG,"SpChRMeta.Algo.Label"));
		props.setLook(algoBox);
		FormData fdalgoBox=new FormData();
		fdalgoBox.left=new FormAttachment(0,0);
		fdalgoBox.right=new FormAttachment(middle,-margin);
		fdalgoBox.top=new FormAttachment(wInputDrop,margin);
		algoBox.setLayoutData(fdalgoBox);
		
		wAlgoBox=new Combo(shell,SWT.DROP_DOWN);
		props.setLook(wAlgoBox);
		wAlgoBox.addModifyListener(lsMod);
		FormData fdwAlgoBox=new FormData();
		fdwAlgoBox.left=new FormAttachment(middle,0);
		fdwAlgoBox.right = new FormAttachment(100, 0);
		fdwAlgoBox.top = new FormAttachment(wInputDrop, margin);
		wAlgoBox.setLayoutData(fdwAlgoBox);
		
		
		//adding Exception/Custom Regex place
		final Label customLabel=new Label(shell, SWT.RIGHT);
		customLabel.setText(BaseMessages.getString(PKG,"SpChRMeta.AlgoExe.Label"));
		customLabel.setForeground(TEXBLUR);
		props.setLook(customLabel);
		FormData fdcustomLabel=new FormData();
		fdcustomLabel.left=new FormAttachment(0,0);
		fdcustomLabel.right=new FormAttachment(middle,-margin);
		fdcustomLabel.top=new FormAttachment(wAlgoBox,margin);
		customLabel.setLayoutData(fdcustomLabel);
		
		wCustomLabel = new Text(shell, SWT.READ_ONLY |SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wCustomLabel);
		wCustomLabel.addModifyListener(lsMod);
		wCustomLabel.setBackground(GRAY);
		FormData fdwCustomLabel = new FormData();
		fdwCustomLabel.left = new FormAttachment(middle, 0);
		fdwCustomLabel.right = new FormAttachment(100, 0);
		fdwCustomLabel.top = new FormAttachment(wAlgoBox, margin);
		wCustomLabel.setLayoutData(fdwCustomLabel);
		
		// OK and cancel buttons
		wOK = new Button(shell, SWT.PUSH);
		wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
		wCancel = new Button(shell, SWT.PUSH);
		wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

		BaseStepDialog.positionBottomButtons(shell,
				new Button[] { wOK, wCancel }, margin, wCustomLabel);

		// Add listeners for cancel and OK
		lsCancel = new Listener() {
			public void handleEvent(Event e) {
				cancel();
			}
		};
		lsOK = new Listener() {
			public void handleEvent(Event e) {
				ok();
			}
		};

		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener(SWT.Selection, lsOK);

		// default listener (for hitting "enter")
		lsDef = new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				ok();
			}
		};
		
		wStepname.addSelectionListener(lsDef);
		wFieldName.addSelectionListener(lsDef);
		wInputDrop.addSelectionListener(lsDef);
		wAlgoBox.addSelectionListener(lsDef);
		
		
		//add selection listener for the exception tab
		wAlgoBox.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				String selectedTextAlgoBox=wAlgoBox.getText();
				if(selectedTextAlgoBox.equals("Keep A-Z,a-z,0-9 and ADD Exceptions") || selectedTextAlgoBox.equals("Custom Regular Expression") ){
					wCustomLabel.setEditable(true);
					wCustomLabel.setBackground(null);
					customLabel.setForeground(null);
					wCustomLabel.setText("[enter your code here]");
					
				}else{
					wCustomLabel.setEditable(false);
					wCustomLabel.setBackground(GRAY);
					customLabel.setForeground(TEXBLUR);
					wCustomLabel.setText("");
				}
			}
			
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//Add Key UP Listener for Testing the regex and stuff in the Custom/Exception part
		wCustomLabel.addListener(SWT.KeyUp, new Listener() {
			
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				
				int textLen=wCustomLabel.getText().length();
				String textEnter=wCustomLabel.getText();
				String selectedAlgo=wAlgoBox.getText();
				int selectedAlgoIndex = 0;
				
				if(selectedAlgo.equals("Keep A-Z,a-z,0-9 and ADD Exceptions")){
					selectedAlgoIndex=1;
				}else if(selectedAlgo.equals("Custom Regular Expression")){
					selectedAlgoIndex=2;
				}
				
				switch(selectedAlgoIndex){
					case 1:
						if(textLen >=1 && textEnter.substring(0,1).equals("[") && textEnter.substring(textLen-1, textLen).equals("]")){
							wCustomLabel.setBackground(GREEN);
							//setCustomCodeColor(GREEN); //for future ref.
						}else{ 
							//initial conditions are false
							wCustomLabel.setBackground(RED);
							//setCustomCodeColor(RED);
						}				
						break;
					case 2:
							try {
								Pattern.compile(textEnter);
								wCustomLabel.setBackground(GREEN);
								//setCustomCodeColor(GREEN);
								
							} catch (Exception e) {
								wCustomLabel.setBackground(RED);
								//setCustomCodeColor(RED);
							}

							break;
				}
			}
		});
		
		
		// Detect X or ALT-F4 or something that kills this window and cancel the
		// dialog properly
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				cancel();
			}
		});
		
				
		// Set/Restore the dialog size based on last position on screen
		// The setSize() method is inherited from BaseStepDialog
		setSize();

		// populate the dialog with the values from the meta object
		populateDialog();
		
		//set asynchronous listing of the dropdown combo box
		setComboBox();

		// restore the changed flag to original value, as the modify listeners
		// fire during dialog population
		meta.setChanged(changed);

		// open dialog and enter event loop
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		// at this point the dialog has closed, so either ok() or cancel() have
		// been executed
		// The "stepname" variable is inherited from BaseStepDialog
		return stepname;
	}

	

	//asynchronously loading the ComboBox. call setComboBox
	private void setComboBox() {
			// TODO Auto-generated method stub
			Runnable fieldLoader=new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					try {
						prevFields=transMeta.getPrevStepFields(stepname);
					} catch (KettleStepException e) {
						
						prevFields=new RowMeta();
						logError("Unable to Find Input Fields");
					}
					
					prevFieldIndexMap=new HashMap<String, String>();
					
					String[] prevStepFieldsNames=prevFields.getFieldNames();
					
					for(int i=0;i<prevStepFieldsNames.length; i++){
				
						prevFieldIndexMap.put(prevStepFieldsNames[i],Integer.toString(i));				
					}
							
					wInputDrop.setItems(prevStepFieldsNames);
					
					if(meta.getInputDropDataIndex()!=null){ //checking for the previously selected entry
						int int_index=Integer.parseInt(meta.getInputDropDataIndex());
						wInputDrop.select(int_index);
					}
				}
			};
			new Thread(fieldLoader).run();
			
		}
	
	
	
	/**
	 * populateDialog method is called when we open or re-open the dialog. 
	 * Also displays the last selections done by the user.
	 * 
	 */
	private void populateDialog() {
		wStepname.selectAll();
		int algoindex=0;
		
		if(meta.getOutputField()!=null){
			wFieldName.setText(meta.getOutputField());
		}
				
		wAlgoBox.setItems(meta.getAlgoBoxItems());
				
		if(meta.getAlgoBoxItemsSelected()!=null){
			
			
			if(meta.getAlgoBoxItemsSelected().equals("Remove all the Special Characters other than A-Z,a-z,0-9 including white-spaces")) {
				algoindex=0;
			}else if(meta.getAlgoBoxItemsSelected().equals("Remove all the Special Characters other than A-Z,a-z,0-9 keep the white-spaces")){
				algoindex=1;
			}else if(meta.getAlgoBoxItemsSelected().equals("Remove anything outside ASCII code 0 to 255")){
				algoindex=2;
			}else if(meta.getAlgoBoxItemsSelected().equals("Remove Unicode Block")){
				algoindex=3;
			}else if(meta.getAlgoBoxItemsSelected().equals("Keep Unicode Block, remove the rest")){
				algoindex=4;
			}else if(meta.getAlgoBoxItemsSelected().equals("Keep A-Z,a-z,0-9 and ADD Exceptions")){
				algoindex=5;
			}else{
				algoindex=6;
			}
			
			wAlgoBox.select(algoindex);
		}
		
		
		if(meta.getCustomCode()!=null && (algoindex==5 || algoindex==6)){
			wCustomLabel.setText(meta.getCustomCode());
			wCustomLabel.setEditable(true);
			wCustomLabel.setBackground(null);
			//customLabel.setForeground(null);
		}
				
	}

	
	/**
	 * Called when the user cancels the dialog.
	 */
	private void cancel() {
		// The "stepname" variable will be the return value for the open()
		// method.
		// Setting to null to indicate that dialog was cancelled.
		stepname = null;
		// Restoring original "changed" flag on the met a object
		meta.setChanged(changed);
		// close the SWT dialog window
		dispose();
	}

	/**
	 * Called when the user confirms the dialog
	 */
	private void ok() {
		// The "stepname" variable will be the return value for the open()
		// method.
		// Setting to step name from the dialog control
		stepname = wStepname.getText();
	
		// Setting the settings to the meta object
		meta.setOutputField(wFieldName.getText());
		meta.setInputDropData(wInputDrop.getText());
		meta.setInputDropDataIndex(prevFieldIndexMap.get(wInputDrop.getText()));
		meta.setAlgoBoxItemsSelected(wAlgoBox.getText());
		meta.setCustomCode(wCustomLabel.getText());
		//meta.setCustomCodeBackgrndColor(getCustomCodeColor().toString());
	
		// close the SWT dialog window
		dispose();
	}

	public RowMetaInterface getPrevFields() {
		return prevFields;
	}

	public void setPrevFields(RowMetaInterface prevFields) {
		this.prevFields = prevFields;
	}

	public Map<String, String> getPrevFieldIndexMap() {
		return prevFieldIndexMap;
	}

	public void setPrevFieldIndexMap(Map<String, String> prevFieldIndexMap) {
		this.prevFieldIndexMap = prevFieldIndexMap;
	}

	
	

}
