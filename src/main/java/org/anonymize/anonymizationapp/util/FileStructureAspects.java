package org.anonymize.anonymizationapp.util;

import java.io.File;

import org.springframework.stereotype.Component;

//small utility class for certain issues related to the file structure and directory creation
@Component
public class FileStructureAspects {
	public void makeDirectory(File dir) {
		boolean successful = dir.mkdir();
		if (successful)
	    {
	      // creating the directory succeeded
	      System.out.println("Directory was created safely and successfully");
	    }
	    else
	    {
	      // creating the directory failed
	      System.out.println("Failed trying to create unique directory");
	    }
	}
}
