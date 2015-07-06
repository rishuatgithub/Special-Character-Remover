# Special Character Remover 
> A Pentaho Kettle Plugin to clean your input stream off special character


- **Plugin version**: 1.1.0
- **Author**: Rishu Shrivastava
- **Email**: rishu.shrivastava@gmail.com

## Description:

**Special Character Remover** is a Pentaho DI Step Plugin which aims at cleaning your input data off the Special Characters or junk values.
Use pre-defined algorithms in the Step to remove special characters. Users can also use his/her custom regular expression codes to 
work with the input stream.

A detailed explaination has been provided in the wordpress blog post as below:

[Link to wordpress Blog](https://anotherreeshu.wordpress.com/2015/01/07/special-character-remover-clean-your-data-of-special-characters-pentaho-kettle-step-plugin/)

## Features:

As compared to the `version 1.0.0` of this plugin, please find below the list of features added to the recent release:

- Pre-defined algorithms to the plugin, which aims at minimal coding effort in cleaning up the data.
- Custom codes are also added so that the user has the flexibility to write his/her code using regex.

Follow the [link](https://anotherreeshu.wordpress.com/2015/01/13/special-character-remover-version-1-1-0-pentaho-kettle-step-plugin/) here for more.

## Installing the plugin:

1. Download the Zip file from the `Marketplace` Github folder:

  [Click Here to download](https://github.com/rishuatgithub/Pentaho/blob/master/SpecialCharacterRemover/marketplace/SpecialCharacterRemover-ver-1.1.0.zip)
  
2. Simply copy the contents of the folder into : 

`/pentaho/design-tools/data-integration/plugins/steps/SpecialCharacterRemover` path of you Pentaho Installation.

3. **Start/ReStart** your Pentaho DI

4. Open a new Transformation

5. Open "**Experimental**" tab. You will find a step named "Special Character Remover".

Its done. Drag and Drop the Step and enjoy :)
