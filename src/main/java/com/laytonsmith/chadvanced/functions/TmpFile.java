
package com.laytonsmith.chadvanced.functions;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CREIOException;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.Exceptions;
import java.io.File;

/**
 *
 */
public class TmpFile {
	
	public static String docs(){
		return "Provides file system access. These functions WILL be removed once file system functions are"
			+ " added to the core.";
	}
	
	@api public static class tmp_file_list_dir extends AbstractFunction {

		public Class[] thrown() {
			return new Class[]{CREIOException.class};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return null;
		}

		public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
			CArray ret = new CArray(t);
			File path = new File(t.file(), args[0].val());
			if(!path.isDirectory()){
				throw new CREIOException("Path is not a directory.", t);
			}
			String[] list = path.list();
			for(String s : list){
				ret.push(new CString(s, t), t);
			}
			return ret;
		}

		public String getName() {
			return "tmp_file_list_dir";
		}

		public Integer[] numArgs() {
			return new Integer[]{1};
		}

		public String docs() {
			return "array {path} Returns a list of files and folders in the specified directory. If the specified path"
				+ " isn't a directory, an IOException is thrown.";
		}

		public Version since() {
			return CHVersion.V3_3_1;
		}
		
	}
}
