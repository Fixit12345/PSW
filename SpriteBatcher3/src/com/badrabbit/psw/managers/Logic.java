package com.badrabbit.psw.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class Logic {
	
	public static String readRawFile(int resourceId) 
	{
		String shaderText = "";
		
		try {
			InputStream inputStream = ContextManager.currentContext.getResources().openRawResource(resourceId);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				shaderText += line + "\n";
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return shaderText;
	}

	public static Document getDomElement(String xml)
	{
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
 
            DocumentBuilder db = dbf.newDocumentBuilder();
 
            InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                doc = db.parse(is); 
 
            } catch (ParserConfigurationException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            }
                // return DOM
            return doc;
    }
	
	public static String getValue(Element item, String str) {      
	    NodeList n = ((Document) item).getElementsByTagName(str);        
	    return getElementValue(n.item(0));
	}
	 
	public static  final String getElementValue( Node elem ) {
	         Node child;
	         if (elem != null) {
	             if (elem.hasChildNodes()) {
	                 for (child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
	                     if (child.getNodeType() == Node.TEXT_NODE) {
	                         return child.getNodeValue();
	                     }
	                 }
	             }
	         }
	         return "";
	  } 
}
