package com.societegenerale.commons.plugin.utils;

import static java.util.Collections.emptyList;

import com.societegenerale.commons.plugin.Log;
import com.societegenerale.commons.plugin.model.RootClassFolder;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Created by agarg020917 on 11/17/2017.
 */
public class ArchUtils {

    private static Log log;

    public ArchUtils(Log log) {
        this.log=log;
    }

    public static JavaClasses importAllClassesInPackage(RootClassFolder rootClassFolder, String packagePath){
        return importAllClassesInPackage(rootClassFolder, packagePath,emptyList());
    }

    public static JavaClasses importAllClassesInPackage(RootClassFolder rootClassFolder, String packagePath, Collection<String> excludedPaths) {

        //not great design, but since all the rules need to call this, it's very convenient to keep this method static
        if(log==null){
            throw new IllegalStateException("please make sure you instantiate "+ArchUtils.class+" with a proper "+Log.class+" before calling this static method");
        }

        Path classesPath = Paths.get(rootClassFolder.getValue() + packagePath);

        if (!classesPath.toFile().exists()) {
            StringBuilder warnMessage=new StringBuilder("classpath ").append(classesPath.toFile())
                    .append(" doesn't exist : loading all classes from root, ie ")
                    .append(rootClassFolder)
                    .append(" even though it's probably not what you want to achieve..");
            log.warn(warnMessage.toString());

            //logging content of directory, to help with debugging..
            log.warn("existing folders and files under root project : ");
            try {
                Files.walk(Paths.get(rootClassFolder.getValue()))
                        .forEach(f -> log.warn(f.toFile().getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            classesPath= Paths.get(rootClassFolder.getValue());
        }

        ClassFileImporter classFileImporter=new ClassFileImporter();

        for(String excludedPath : excludedPaths){
            ExclusionImportOption exclusionImportOption=new ExclusionImportOption(excludedPath);
            classFileImporter=classFileImporter.withImportOption(exclusionImportOption);
        }

        return classFileImporter.importPath(classesPath);

    }

}
