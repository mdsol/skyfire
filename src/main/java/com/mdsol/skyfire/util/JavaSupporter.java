package com.mdsol.skyfire.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.NamedElement;

import com.mdsol.skyfire.Mapping;

/**
 * A utility class used to support compilation of Java classes
 * 
 * @author Nan Li
 * @version 1.0 Feb 8, 2013
 *
 */

public class JavaSupporter {

	public JavaSupporter() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Returns all Java files in a directory specified by path
	 * 
	 * @param path the current path of a directory
	 * @return a {@link java.util.List} of {@link java.io.File}s that are Java files
	 */
	public static List<File> returnAllJavaFiles(String path){
		
		File folder = new File(path);
		File[] files = folder.listFiles();
		List<File> results = new ArrayList<File>();
		
		for(File file: files){
			if(file.isFile() && file.getName().endsWith(".java"))
				results.add(file);
		}
		
		return results;
	}
	
	/**
	 * Returns all Jar files in a directory specified by path
	 * 
	 * @param path the current path of a directory
	 * @return a {@link java.util.List} of {@link java.io.File}s that are Jar files
	 */
	public static List<File> returnAllJarFiles(String path){
		
		File folder = new File(path);
		File[] files = folder.listFiles();
		List<File> results = new ArrayList<File>();
		
		for(File file: files){
			if(file.isFile() && file.getName().endsWith(".jar"))
				results.add(file);
		}
		
		return results;
	}
	
	/**
	 * Removes the semicolon if there is one at the end of a String object.
	 * @param string	a statement having semicolons at the end in a String format
	 * @return			a String object without having a semicolon at the end
	 */
	public static String removeSemiColon(String string){
		String result = string;
		if(string.endsWith(";")){
			result = string.substring(0, string.length() - 1);
			if(result.endsWith(";"))
				result = removeSemiColon(result);
		}
		else{
			return result;
		}
		return result;	
	}
	
	/**
	 * Copies a File object from the source to the destination.
	 * 
	 * @param source		the source file
	 * @param destination	the destination file
	 * @throws IOException
	 */
	public static void copyFile(File source, File destination) throws IOException{
		InputStream oInStream = new FileInputStream(source);
		OutputStream oOutStream = new FileOutputStream(destination);

		// Transfer bytes from in to out
		byte[] oBytes = new byte[1024];
		int nLength;
		BufferedInputStream oBuffInputStream = new BufferedInputStream( oInStream );

		while ((nLength = oBuffInputStream.read(oBytes)) > 0) {
			oOutStream.write(oBytes, 0, nLength);
		}

		oInStream.close();
		oOutStream.close();
	}
	
	/**
	 * Returns all UML files in a directory specified by path.
	 * 
	 * @param 	path the current path of a directory
	 * @return 	a {@link java.util.List} of {@link java.io.File}s that are Java files
	 */
	public static List<File> returnAllUmlFiles(String path){
		
		File folder = new File(path);
		File[] files = folder.listFiles();
		List<File> results = new ArrayList<File>();
		
		for(File file: files){
			if(file.isFile() && file.getName().endsWith(".uml"))
				results.add(file);
		}
		
		return results;
	}
	
	/**
	 * Returns all sub-directories in a directory specified by path.
	 * 
	 * @param path the current path of a directory
	 * @return a {@link java.util.List} of {@link java.io.File}s that are Java files
	 */
	public static List<File> returnAllDirectories(String path){
		
		File folder = new File(path);
		File[] files = folder.listFiles();
		//System.out.println("file size: " + files.length);
		List<File> results = new ArrayList<File>();
		
		for(File file: files){
			if(file.isDirectory())
				results.add(file);
		}
		//System.out.println("result size: " + results.size());
		return results;
	}
	
	/**
	 * Get the names of a list of File objects.
	 * @param files	a list of File objects
	 * @return		an array of names of File objects
	 */
	public static Object[] getFileNames(List<File> files){
		List<String> fileNames = new ArrayList<String>();
		for(File file : files){
			fileNames.add(file.getName());
		}
		return fileNames.toArray();
	}
	
	/**
	 * Get the names of a list of {@link org.eclipse.uml2.uml.NamedElement} objects.
	 * @param elements	a list of {@link org.eclipse.uml2.uml.NamedElement} objects
	 * @return			an array of names of {@link org.eclipse.uml2.uml.NamedElement} objects
	 */
	public static Object[] getElementNames(List<NamedElement> elements){
		List<String> elementNames = new ArrayList<String>();
		for(NamedElement element : elements){
			elementNames.add(element.getName());
		}
		return elementNames.toArray();
	}
	
	/**
	 * Get the names of a list of {@link edu.gmu.swe.taf.Mapping} objects.
	 * @param elements	a list of {@link edu.gmu.swe.taf.Mapping} objects
	 * @return			an array of names of {@link edu.gmu.swe.taf.Mapping} objects
	 */
	public static Object[] getMappingNames(List<? extends Mapping> mappings){
		List<String> mappingNames = new ArrayList<String>();
		for(Mapping mapping : mappings){
			mappingNames.add(mapping.getName());
		}
		return mappingNames.toArray();
	}
	
	/**
	 * Create directories from the package.
	 * For instance, if a package name is "edu.gmu", two directories edu and gmu are created.
	 * @param directory		
	 * @param packageName
	 */
	public static final void createTestDirectory(String directory, String packageName){
		if(packageName != null){
			if(packageName.trim().length() > 0){
				String name = packageName;
				if(packageName.startsWith("package")){
					name = name.substring(7, name.length()).trim();
				}
				if(name.endsWith(";")){
					name = removeSemiColon(name);
				}
				
				String[] levels = name.split("\\.");
				String directories = directory;
				for(String level : levels){
					directories += level + "/";
					//System.out.println(level);
				}
				//System.out.println(directories);
				File file = new File(directories);
				file.mkdirs();
			}
		}		
	}
	
	/**
	 * Convert a packageName separated by dot to a file path having the package names separated by slash.
	 * @param packageName	a package name that could have dot or have no dot
	 * @return				a path in a String format		
	 */	
	public static String returnPackages(String packageName){
		if(packageName != null){
			if(packageName.trim().length() > 0){
				String name = packageName;
				if(packageName.startsWith("package")){
					name = name.substring(7, name.length()).trim();
				}
				if(name.endsWith(";")){
					name = removeSemiColon(name);
				}
				String[] levels = name.split("\\.");
				String directories = "";
				for(String level : levels){
					directories += level + "/";
				}
				return directories;
			}
		}	
		return "";
	}
	
	/**
	 * Removes the header "package" and ";" at the end if any.
	 * @param packageName	a package name in a String format
	 * @return				the package name without "package" keyword and ";"
	 */
	public static String cleanUpPackageName(String packageName){
		if(packageName != null){
			if(packageName.trim().length() > 0){
				String name = packageName;
				if(packageName.startsWith("package")){
					name = name.substring(7, name.length()).trim();
				}
				if(name.endsWith(";")){
					name = removeSemiColon(name);
				}
				return name.trim();
			}
		}	
		return "";
	}
	
	/**
	 * A hack method to add classes dynamically.
	 * @param url
	 * @throws Exception
	 */
	public static final void addURL(URL url) throws Exception {
		  URLClassLoader classLoader
		         = (URLClassLoader) ClassLoader.getSystemClassLoader();
		  Class clazz= URLClassLoader.class;

		  // Use reflection
		  Method method= clazz.getDeclaredMethod("addURL", new Class[] { URL.class });
		  method.setAccessible(true);
		  method.invoke(classLoader, new Object[] { url });
	}
	
	/**
	 * Removes the brackets in the toString() result used by List
	 * @param listString	toString() result by java.util.List
	 * @return			 	the same String representation without brackets 
	 */
	public static String removeBrackets(String listString){
		String noBracketsString = null;
		if(listString.startsWith("[") && listString.endsWith("]")){
			noBracketsString = listString.substring(1, listString.length() - 1);
			return noBracketsString;
		}
		else
			return listString;
	}

}
