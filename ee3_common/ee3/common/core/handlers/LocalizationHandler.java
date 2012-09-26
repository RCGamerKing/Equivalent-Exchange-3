package ee3.common.core.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ee3.common.core.helper.LocalizationHelper;
import ee3.common.lib.Localizations;

/**
 * LocalizationHandler
 * 
 * Loads in all specified localizations for the mod
 * 
 * @author pahimar
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class LocalizationHandler {
	
	/***
	 * Loads in all the localization files from the Localizations library class
	 */
	public static void loadLanguages() {
		InputStream languageStream = null;
		Properties languageMappings = new Properties();
		Iterator<String> keyIter = null;
		String currentKey, currentLang;
		
		try {
			// For every file specified in the Localization library class, load them into the Language Registry
			for (String localizationFile : Localizations.localeFiles) {
				languageStream = LocalizationHandler.class.getResourceAsStream(localizationFile);
				
				// If this file is a XML file, load it from XML
				if (LocalizationHelper.isXMLLanguageFile(localizationFile)) {
					languageMappings.loadFromXML(languageStream);
				}
				// Otherwise, load it like any other Java Properties file
				else {
					languageMappings.load(languageStream);
				}
				
				// Read the locale from the file name of the localization file
				currentLang = LocalizationHelper.getLocaleFromFileName(localizationFile);
				
				// For every key in the localization file, add its key:value pair to the Language Registry for the given locale
				keyIter = (Iterator<String>)languageMappings.keys();
				while (keyIter.hasNext()) {
					currentKey = keyIter.next();
					LanguageRegistry.instance().addStringLocalization(currentKey, currentLang, languageMappings.getProperty(currentKey));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			try {
				if (languageStream != null) {
					languageStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace(System.err);
			}
		}
		
	}
	
}
